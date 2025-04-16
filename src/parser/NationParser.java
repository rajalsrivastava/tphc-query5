// src/parser/NationParser.java
package parser;

import model.Nation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class NationParser {

    public static List<Nation> parse(String filePath) {
        List<Nation> nations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                Nation nation = new Nation();
                nation.nationKey = Integer.parseInt(parts[0]);
                nation.name = parts[1];
                nation.regionKey = Integer.parseInt(parts[2]);
                nation.comment = parts[3];
                nations.add(nation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nations;
    }
}
