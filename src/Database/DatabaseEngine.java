package Database;

import java.io.*;
import java.util.ArrayList;

public class DatabaseEngine {
    private static final String FILE_PATH = "src/Files/";

    /**
     * Resolves a raw name (e.g. "Doctor") into a normalized path string (e.g. "src/Files/doctor.txt").
     */
    public static String resolvePath(String fileName) {
        if (fileName == null) return "";
        // If it's already resolved, don't double-resolve it
        if (fileName.startsWith(FILE_PATH)) return fileName;

        String cleanName = fileName.toLowerCase().trim();
        if (!cleanName.endsWith(".txt")) {
            cleanName += ".txt";
        }
        return FILE_PATH + cleanName;
    }

    /**
     * Verifies file existence. Creates directories and a blank text file if missing.
     */
    public static void checkAndCreateFile(String fileName) {
        String fullPath = resolvePath(fileName);
        File file = new File(fullPath);

        // Ensure parent directories exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Error creating system file " + fullPath + ": " + e.getMessage());
            }
        }
    }

    /**
     * Reads a text file table and breaks lines into parsed String arrays.
     */
    public static ArrayList<String[]> readTable(String fileName) {
        checkAndCreateFile(fileName);
        String fullPath = resolvePath(fileName);
        ArrayList<String[]> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    records.add(line.split(";"));
                }
            }
        } catch (IOException e) {
            System.err.println("Read operation failed on " + fullPath);
        }
        return records;
    }

    /**
     * Queries a file and returns only the rows where a specific column matches a keyword.
     */
    public static ArrayList<String[]> querySearch(String fileName, int columnIndex, String searchTerm) {
        ArrayList<String[]> allRows = readTable(fileName);
        ArrayList<String[]> matchingRows = new ArrayList<>();

        for (String[] row : allRows) {
            if (row.length > columnIndex && row[columnIndex].equalsIgnoreCase(searchTerm.trim())) {
                matchingRows.add(row);
            }
        }
        return matchingRows;
    }

    /**
     * Scans a data table file, finds the highest Integer ID at index 0, and returns the next sequential value.
     * If the file is empty, it returns a baseline index value (e.g., 1001).
     */
    public static int getNextAutoIncrementId(String fileName) {
        ArrayList<String[]> rows = readTable(fileName);
        int maxId = 1000; // Baseline starting index to make IDs look uniform

        for (String[] row : rows) {
            if (row.length > 0) {
                try {
                    int currentId = Integer.parseInt(row[0].trim());
                    if (currentId > maxId) {
                        maxId = currentId;
                    }
                } catch (NumberFormatException e) {
                    // Ignore headers or malformed text lines safely
                }
            }
        }
        return maxId + 1;
    }

    /**
     * Reads a given data table file and maps ID (index 0) to a Name string value (index 1).
     * Useful for turning numeric cell row IDs into recognizable person names.
     */
    public static java.util.HashMap<String, String> getNameLookupMap(String fileName) {
        java.util.HashMap<String, String> lookupMap = new java.util.HashMap<>();
        ArrayList<String[]> rows = readTable(fileName);
        for (String[] row : rows) {
            if (row.length > 1) {
                lookupMap.put(row[0].trim(), row[1].trim());
            }
        }
        return lookupMap;
    }
}
