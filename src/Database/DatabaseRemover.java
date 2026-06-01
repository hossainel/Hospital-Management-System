package Database;

import java.io.File;
import java.util.ArrayList;

public class DatabaseRemover {

    /**
     * Deletes a record from a file matching a given primary identifier ID.
     */
    public static boolean deleteRecordById(String fileName, String targetId) {
        ArrayList<String[]> currentRows = DatabaseEngine.readTable(fileName);

        boolean removed = currentRows.removeIf(row -> row.length > 0 && row[0].equals(targetId.trim()));

        if (removed) {
            DatabaseModifier.rewriteTableFile(fileName, currentRows);
        }
        return removed;
    }

    /**
     * Completely purges a database text file from the project directory.
     */
    public static void dropTableFile(String fileName) {
        String fullPath = DatabaseEngine.resolvePath(fileName);
        File f = new File(fullPath);
        if (f.exists()) {
            if (f.delete()) {
                System.out.println("File table dropped successfully: " + fullPath);
            } else {
                System.err.println("Access restriction failed to clear " + fullPath);
            }
        }
    }
}
