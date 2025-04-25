package parser;

import model.Order;

import java.io.*;
import java.util.*;

public class OrdersParser {

    public static List<Order> parse(String filePath) throws IOException {
        List<Order> orders = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Parsing logic
                Order order = new Order();

                // Parsing fields accordingly
                order.orderKey = Integer.parseInt(parts[0].trim());  // Parsing as int
                order.custKey = Integer.parseInt(parts[1].trim());  // Parsing as int
                order.orderStatus = parts[2].trim();  // Parsing as String
                order.totalPrice = Double.parseDouble(parts[3].trim());  // Parsing as double
                order.orderDate = parts[4].trim();  // Parsing as String
                order.orderPriority = parts[5].trim();  // Parsing as String
                order.clerk = parts[6].trim();  // Parsing as String
                order.shipPriority = Integer.parseInt(parts[7].trim());  // Parsing as int
                order.comment = parts[8].trim();  // Parsing as String

                orders.add(order);
            }
        }
        return orders;
    }
}
