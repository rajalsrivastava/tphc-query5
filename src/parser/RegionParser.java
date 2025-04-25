package parser;

import model.Region;

import java.io.*;
import java.util.*;

public class RegionParser {

    public static List<Region> parse(String filePath) {
        List<Region> regions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                Region r = new Region();
                r.regionKey = Integer.parseInt(parts[0]);
                r.name = parts[1];
                r.comment = parts[2];
                regions.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return regions;
    }
}
