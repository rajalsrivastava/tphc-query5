
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import model.*;
import utils.DataLoader;

public class Main {

    public static void main(String[] args) throws Exception {
        // Default values
        String region = "ASIA";
        String startDate = "1994-01-01";
        String endDate = "1995-01-01";
        int numThreads = 10;
        String dataDir = "./data";
        String resultDir = "./results";

        // Parse command-line arguments
        for (int i = 0; i < args.length; i++) {
            if ("--region".equals(args[i])) {
                region = args[++i];
            } else if ("--start-date".equals(args[i])) {
                startDate = args[++i];
            } else if ("--end-date".equals(args[i])) {
                endDate = args[++i];
            } else if ("--threads".equals(args[i])) {
                numThreads = Integer.parseInt(args[++i]);
            } else if ("--data-dir".equals(args[i])) {
                dataDir = args[++i];
            } else if ("--result-dir".equals(args[i])) {
                resultDir = args[++i];
            }
        }

        // Load data
        List<Customer> customers = DataLoader.loadCustomers(dataDir + "/customer.tbl");
        List<Order> orders = DataLoader.loadOrders(dataDir + "/orders.tbl");
        List<LineItem> lineitems = DataLoader.loadLineItems(dataDir + "/lineitem.tbl");
        List<Supplier> suppliers = DataLoader.loadSuppliers(dataDir + "/supplier.tbl");
        List<Nation> nations = DataLoader.loadNations(dataDir + "/nation.tbl");
        List<Region> regions = DataLoader.loadRegions(dataDir + "/region.tbl");

        // Parse dates
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        // Region filter
        final String regionFinal = region;
        Set<Integer> regionKeys = regions.stream()
                .filter(r -> r.name.equals(regionFinal))
                .map(r -> r.regionKey)
                .collect(Collectors.toSet());

        Set<Integer> nationKeysInRegion = nations.stream()
                .filter(n -> regionKeys.contains(n.regionKey))
                .map(n -> n.nationKey)
                .collect(Collectors.toSet());

        Set<Integer> customerKeysInRegion = customers.stream()
                .filter(c -> nationKeysInRegion.contains(c.nationKey))
                .map(c -> c.custKey)
                .collect(Collectors.toSet());

        Set<Integer> orderKeysFromRegion = orders.stream()
                .filter(o -> customerKeysInRegion.contains(o.custKey))
                .filter(o -> {
                    try {
                        Date orderDate = sdf.parse(o.orderDate);
                        return !orderDate.before(start) && orderDate.before(end);
                    } catch (Exception e) {
                        return false;
                    }
                })
                .map(o -> o.orderKey)
                .collect(Collectors.toSet());

        Map<Integer, Integer> supplierNationMap = suppliers.stream()
                .filter(s -> nationKeysInRegion.contains(s.nationKey))
                .collect(Collectors.toMap(s -> s.suppKey, s -> s.nationKey));

        Map<Integer, String> nationKeyToName = nations.stream()
                .collect(Collectors.toMap(n -> n.nationKey, n -> n.name));

        // Multi-threaded execution
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        long startTime = System.currentTimeMillis();

        Map<String, Double> revenueByNation = new ConcurrentHashMap<>();

        int lineItemChunkSize = (int) Math.ceil((double) lineitems.size() / numThreads);
        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < numThreads; i++) {
            int chunkStart = i * lineItemChunkSize;
            int chunkEnd = Math.min(chunkStart + lineItemChunkSize, lineitems.size());
            List<LineItem> chunk = lineitems.subList(chunkStart, chunkEnd);

            futures.add(executor.submit(() -> {
                Map<String, Double> localRevenue = new HashMap<>();
                for (LineItem li : chunk) {
                    if (orderKeysFromRegion.contains(li.orderKey) && supplierNationMap.containsKey(li.suppKey)) {
                        int nationKey = supplierNationMap.get(li.suppKey);
                        String nationName = nationKeyToName.get(nationKey);
                        double revenue = li.extendedPrice * (1 - li.discount);
                        localRevenue.merge(nationName, revenue, Double::sum);
                    }
                }
                for (Map.Entry<String, Double> entry : localRevenue.entrySet()) {
                    revenueByNation.merge(entry.getKey(), entry.getValue(), Double::sum);
                }
                return null;
            }));
        }

        for (Future<Void> future : futures) {
            future.get();
        }


        for (Future<Void> future : futures) {
            future.get();
        }

        executor.shutdown();
        long endTime = System.currentTimeMillis();
        long multiThreadedTime = endTime - startTime;

        System.out.println("Multi-threaded execution time: " + multiThreadedTime + " ms");

        // Single-threaded execution
        startTime = System.currentTimeMillis();
        revenueByNation.clear();

        for (LineItem li : lineitems) {
            if (orderKeysFromRegion.contains(li.orderKey) && supplierNationMap.containsKey(li.suppKey)) {
                int nationKey = supplierNationMap.get(li.suppKey);
                String nationName = nationKeyToName.get(nationKey);
                double revenue = li.extendedPrice * (1 - li.discount);
                revenueByNation.merge(nationName, revenue, Double::sum);
            }
        }

        endTime = System.currentTimeMillis();
        long singleThreadedTime = endTime - startTime;

        System.out.println("Single-threaded execution time: " + singleThreadedTime + " ms");

        System.out.println("==== Runtime Summary ====");
        System.out.println("Multi-threaded time: " + multiThreadedTime + " ms");
        System.out.println("Single-threaded time: " + singleThreadedTime + " ms");
        System.out.printf("Speedup: %.2fx\n", (double) singleThreadedTime / multiThreadedTime);

        // Sort and print results
        List<Map.Entry<String, Double>> sortedResults = revenueByNation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        sortedResults.forEach(entry
                -> System.out.printf("%s: %.2f\n", entry.getKey(), entry.getValue())
        );

        // Save to file
        File outputFile = new File(resultDir, "query5_result.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Double> entry : sortedResults) {
                out.printf("%s: %.2f\n", entry.getKey(), entry.getValue());
            }
            System.out.println("✅ Results saved to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("❌ Failed to write output to file.");
            e.printStackTrace();
        }
    }
}
