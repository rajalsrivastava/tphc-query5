package parser;

import model.Customer;

import java.io.*;
import java.util.*;

public class CustomerParser {

    public static List<Customer> parse(String filePath) throws IOException {
        List<Customer> customers = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");

                // Parsing logic
                Customer customer = new Customer();

                customer.custKey = Integer.parseInt(parts[0].trim());  // Parsing as int
                customer.name = parts[1].trim();  // Parsing as String
                customer.address = parts[2].trim();  // Parsing as String
                customer.nationKey = Integer.parseInt(parts[3].trim());  // Parsing as int
                customer.phone = parts[4].trim();  // Parsing as String
                customer.acctBal = Double.parseDouble(parts[5].trim());  // Parsing as double (fix here)
                customer.mktSegment = parts[6].trim();  // Parsing as String
                customer.comment = parts[7].trim();  // Parsing as String

                customers.add(customer);
            }
        }
        return customers;
    }
}
