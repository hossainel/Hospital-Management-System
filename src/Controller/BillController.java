package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Bill;
import java.util.ArrayList;

public class BillController {
    private static final String FILE = "bill";

    /**
     * Appends a newly generated Bill record row to the flat file.
     */
    public void addBill(Bill b) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        b.setId(newId);
        DatabaseModifier.appendRecord(FILE, b.toLineData());
    }

    /**
     * Updates an existing billing record line matching the invoice ID.
     */
    public void updateBill(Bill b) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(b.getId()), b.toLineData());
    }

    /**
     * Permanently deletes a billing record line matching the specified ID.
     */
    public void removeBill(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches financial invoicing tracking lines organized by user selection parameters.
     * File Layout Indices: [0] Bill ID, [1] Patient ID, [2] Total Amount, [3] Status
     */
    public ArrayList<String[]> getBills(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);

        switch (sortBy.toUpperCase()) {
            case "AMOUNT"    -> DatabaseSorter.sortByDouble(rows, 2);
            case "STATUS"    -> DatabaseSorter.sortAlphabetically(rows, 3);
            case "PATIENTID" -> DatabaseSorter.sortByInteger(rows, 1);
            default          -> DatabaseSorter.sortByInteger(rows, 0); // Default fallback sorting: Bill ID
        }
        return rows;
    }
}
