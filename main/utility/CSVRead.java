package main.utility;

import java.io.*;
import java.util.*;
import java.util.regex.*;


public class CSVRead {
    /**
     * Read a CSV file and return its content as a list of arrows
     * each list represented as a list of strings
     * @param filePath The path of the CSV file to be read
     * @param hasHeader A boolean indicating whether the file has header row or not
     * @return list of rows, each list represented as a list of strings
     */

     public static ArrayList<LinkedHashMap<String, String>> CSVRead(String filePath, boolean hasHeader) {
        ArrayList<LinkedHashMap<String, String>> list = new ArrayList<>();
        List<String> headers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            // If the file has a BOM, handle it by reading the first character
            if (br.ready()) {
                line = br.readLine();
                // Remove BOM (if present) by trimming unwanted characters like "?"
                if (line.charAt(0) == 0xFEFF) {  // BOM marker in UTF-8
                    line = line.substring(1);
                }
                // Now split headers
                headers = Arrays.asList(line.split(","));
                headers.replaceAll(String::trim);  // Trim all header values
            }

            // Regular expression pattern to match CSV values
            Pattern pattern = Pattern.compile("(?<=,|^)(\"[^\"]*\"|[^,]*)(?=,|$)");

            // Read each line
            while ((line = br.readLine()) != null) {
                // Remove BOM (if present) from line
                if (line.charAt(0) == 0xFEFF) {
                    line = line.substring(1);
                }
                List<String> row = new ArrayList<>();
                Matcher matcher = pattern.matcher(line);

                while (matcher.find()) {
                    String value = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                    row.add(value != null ? value.trim() : "");
                }

                // If has header, create a map
                if (hasHeader && row.size() == headers.size()) {
                    LinkedHashMap<String, String> rowMap = new LinkedHashMap<>();
                    for (int i = 0; i < headers.size(); i++) {
                        rowMap.put(headers.get(i), row.get(i));
                    }
                    list.add(rowMap);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return list;
    }
    

    

}