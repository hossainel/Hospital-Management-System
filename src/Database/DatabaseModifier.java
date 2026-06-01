package Database;

import java.io.*;
import java.util.ArrayList;

public class DatabaseModifier {

    /**
     * Appends a new data row to the bottom of a text file.
     */
    public static void appendRecord(String fileName, String[] fields) {
        DatabaseEngine.checkAndCreateFile(fileName);
        String fullPath = DatabaseEngine.resolvePath(fileName);

        try (FileWriter fw = new FileWriter(fullPath, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(String.join(";", fields));
            System.out.println("Writing at "+fileName);
        } catch (IOException e) {
            System.err.println("Append operation failed: " + e.getMessage());
        }
    }

    /**
     * Finds a record by its primary ID (Index 0) and updates its entire row data.
     */
    public static boolean updateRecordLine(String fileName, String targetId, String[] newFields) {
        ArrayList<String[]> currentRows = DatabaseEngine.readTable(fileName);
        boolean foundAndUpdated = false;

        for (int i = 0; i < currentRows.size(); i++) {
            String[] row = currentRows.get(i);
            if (row.length > 0 && row[0].equals(targetId.trim())) {
                currentRows.set(i, newFields);
                foundAndUpdated = true;
                break;
            }
        }

        if (foundAndUpdated) {
            rewriteTableFile(fileName, currentRows);
        }
        return foundAndUpdated;
    }

    /**
     * Overwrites a text file completely with an updated dataset.
     */
    public static void rewriteTableFile(String fileName, ArrayList<String[]> dataRows) {
        DatabaseEngine.checkAndCreateFile(fileName);
        String fullPath = DatabaseEngine.resolvePath(fileName);

        try (FileWriter fw = new FileWriter(fullPath, false);
             PrintWriter pw = new PrintWriter(fw)) {
            for (String[] row : dataRows) {
                pw.println(String.join(";", row));
            }
            System.out.println("Updating at "+fileName);
        } catch (IOException e) {
            System.err.println("File modification overwrite failed: " + e.getMessage());
        }
    }
}
