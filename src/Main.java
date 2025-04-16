
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.*;
import utils.DataLoader;
import model.Customer;
import model.Order;
import model.LineItem;
import model.Supplier;
import model.Nation;
import model.Region;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        // Default values
        String region = "ASIA";
        String startDate = "1994-01-01";
        String endDate = "1995-01-01";
        int numThreads = 10;
        String dataDir = "./data"; // Default data directory
        String resultDir = "./results"; // Default result directory

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

        // Filter region = 'ASIA'
        final String finalRegion = region; // Final variable for lambda expression
        Set<Integer> asiaRegionKeys = regions.stream()
                .filter(r -> r.name.equals(finalRegion))
                .map(r -> r.regionKey)
                .collect(Collectors.toSet());

        // Get nations in Asia
        Set<Integer> asiaNationKeys = nations.stream()
                .filter(n -> asiaRegionKeys.contains(n.regionKey))
                .map(n -> n.nationKey)
                .collect(Collectors.toSet());

        // Get customers from nations in Asia
        Set<Integer> customerKeysInAsia = customers.stream()
                .filter(c -> asiaNationKeys.contains(c.nationKey))
                .map(c -> c.custKey)
                .collect(Collectors.toSet());

        // Get orders placed by those customers
        Set<Integer> orderKeysFromAsiaCustomers = orders.stream()
                .filter(o -> customerKeysInAsia.contains(o.custKey))
                .map(o -> o.orderKey)
                .collect(Collectors.toSet());

        // Filter suppliers from nations in Asia (for join with lineitems)
        Map<Integer, Integer> supplierNationMap = suppliers.stream()
                .filter(s -> asiaNationKeys.contains(s.nationKey))
                .collect(Collectors.toMap(s -> s.suppKey, s -> s.nationKey));

        // Initialize a map to store the revenue by nation
        Map<String, Double> revenueByNation = new ConcurrentHashMap<>();  // Use ConcurrentHashMap for thread safety

        // ExecutorService to handle parallel execution
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);  // Use user-specified number of threads

        // Submit tasks to the executor for each lineitem
        List<Future<Void>> futures = new ArrayList<>();
        for (LineItem li : lineitems) {
            futures.add(executor.submit(() -> {
                if (orderKeysFromAsiaCustomers.contains(li.orderKey) && supplierNationMap.containsKey(li.suppKey)) {
                    int nationKey = supplierNationMap.get(li.suppKey);
                    String nationName = nations.stream()
                            .filter(n -> n.nationKey == nationKey)
                            .findFirst().get().name;

                    double revenue = li.extendedPrice * (1 - li.discount);
                    revenueByNation.merge(nationName, revenue, Double::sum);  // Merge revenues safely
                }
                return null;
            }));
        }

        // Wait for all tasks to finish
        for (Future<Void> future : futures) {
            future.get();  // Wait for each task to complete
        }

        // Shutdown the executor
        executor.shutdown();

        // Sort result
        List<Map.Entry<String, Double>> sortedResults = revenueByNation.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());

        // Print result
        sortedResults.forEach(entry
                -> System.out.printf("%s: %.2f\n", entry.getKey(), entry.getValue())
        );


                  // Save to result file
        File outputFile = new File(resultDir, "query5_result.txt");
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            for (Map.Entry<String, Double> entry : sortedResults) {
                out.printf("%s: %.2f\n", entry.getKey(), entry.getValue());
            }
            System.out.println("✅ Results saved to: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("❌ Failed to write result file: " + e.getMessage());
        }
    }
}
