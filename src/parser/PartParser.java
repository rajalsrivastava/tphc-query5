package parser;

import model.Part;

import java.io.*;
import java.util.*;

public class PartParser {

    public static List<Part> parse(String filePath) {
        List<Part> parts = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\\|");
                Part part = new Part();
                part.partKey = Integer.parseInt(tokens[0]);
                part.name = tokens[1];
                part.mfgr = tokens[2];
                part.brand = tokens[3];
                part.type = tokens[4];
                part.size = Integer.parseInt(tokens[5]);
                part.container = tokens[6];
                part.retailPrice = Double.parseDouble(tokens[7]);
                part.comment = tokens[8];
                parts.add(part);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parts;
    }
}
