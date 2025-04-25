package parser;


import model.Supplier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

public class SupplierParser {

    public static List<Supplier> parse(String filePath) {
        List<Supplier> suppliers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Remove trailing |
                if (line.endsWith("|")) {
                    line = line.substring(0, line.length() - 1);
                }

                String[] tokens = line.split("\\|");

                Supplier s = new Supplier();
                s.suppKey = Integer.parseInt(tokens[0]);
                s.name = tokens[1];
                s.address = tokens[2];
                s.nationKey = Integer.parseInt(tokens[3]);
                s.phone = tokens[4];
                s.acctBal = Double.parseDouble(tokens[5]);
                s.comment = tokens[6];

                suppliers.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return suppliers;
    }
}
