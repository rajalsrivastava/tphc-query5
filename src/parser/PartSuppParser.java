package parser;

import model.PartSupp;
import java.io.*;
import java.util.*;

public class PartSuppParser {

    public static List<PartSupp> parse(String filePath) throws IOException {
        List<PartSupp> partSuppList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|"); // Assuming "|" is the delimiter


                // Ensure the columns match the expected types
                int partKey = Integer.parseInt(parts[0]);
                int suppKey = Integer.parseInt(parts[1]);
                int availableQuantity = Integer.parseInt(parts[2]);       // 3325
                double supplyCost = Double.parseDouble(parts[3]);         // 771.64
                String comment = parts[4];                                // the rest


                // Create the PartSupp object and add it to the list
                PartSupp partSupp = new PartSupp(partKey, suppKey, supplyCost, availableQuantity, comment);
                partSuppList.add(partSupp);
            }
        }
        return partSuppList;
    }
}
