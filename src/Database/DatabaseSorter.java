package Database;

import java.util.ArrayList;

public class DatabaseSorter {

    /**
     * Sorts table row datasets alphabetically based on a text column index.
     */
    public static void sortAlphabetically(ArrayList<String[]> data, int columnIndex) {
        if (data == null || data.isEmpty()) return;

        data.sort((row1, row2) -> {
            if (row1.length <= columnIndex || row2.length <= columnIndex) return 0;
            return row1[columnIndex].compareToIgnoreCase(row2[columnIndex]);
        });
    }

    /**
     * Sorts table row datasets numerically (Integer parameters).
     */
    public static void sortByInteger(ArrayList<String[]> data, int columnIndex) {
        if (data == null || data.isEmpty()) return;

        data.sort((row1, row2) -> {
            try {
                if (row1.length <= columnIndex || row2.length <= columnIndex) return 0;
                int val1 = Integer.parseInt(row1[columnIndex].trim());
                int val2 = Integer.parseInt(row2[columnIndex].trim());
                return Integer.compare(val1, val2);
            } catch (NumberFormatException e) {
                return 0;
            }
        });
    }

    /**
     * Sorts table row datasets numerically by floating decimal parameters (Double parameters).
     */
    public static void sortByDouble(ArrayList<String[]> data, int columnIndex) {
        if (data == null || data.isEmpty()) return;

        data.sort((row1, row2) -> {
            try {
                if (row1.length <= columnIndex || row2.length <= columnIndex) return 0;
                double val1 = Double.parseDouble(row1[columnIndex].trim());
                double val2 = Double.parseDouble(row2[columnIndex].trim());
                return Double.compare(val1, val2);
            } catch (NumberFormatException e) {
                return 0;
            }
        });
    }
}
