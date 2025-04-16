package  parser;

import model.LineItem;
import java.io.*;
import java.util.*;

public class LineItemParser {

    public static List<LineItem> parse(String filePath) throws IOException {
        List<LineItem> lineItems = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Create a new LineItem and set fields based on parts
                LineItem lineItem = new LineItem();
                lineItem.orderKey = Integer.parseInt(parts[0].trim());
                lineItem.partKey = Integer.parseInt(parts[1].trim());
                lineItem.suppKey = Integer.parseInt(parts[2].trim());
                lineItem.linenumber = Integer.parseInt(parts[3].trim());
                lineItem.quantity = Double.parseDouble(parts[4].trim());
                lineItem.extendedPrice = Double.parseDouble(parts[5].trim());
                lineItem.discount = Double.parseDouble(parts[6].trim());
                lineItem.tax = Double.parseDouble(parts[7].trim());
                lineItem.returnFlag = parts[8].trim();
                lineItem.lineStatus = parts[9].trim();
                lineItem.shipDate = parts[10].trim();
                lineItem.commitDate = parts[11].trim();
                lineItem.receiptDate = parts[12].trim();
                lineItem.shipInstruct = parts[13].trim();
                lineItem.shipMode = parts[14].trim();
                lineItem.comment = parts[15].trim();

                // Add the parsed line item to the list
                lineItems.add(lineItem);
            }
        }
        return lineItems;
    }
}
