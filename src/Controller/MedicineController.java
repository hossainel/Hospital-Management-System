package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Medicine;
import java.util.ArrayList;

public class MedicineController {
    private static final String FILE = "medicine";

    /**
     * Appends a newly created Medicine stock record line to the flat file database.
     */
    public void addMedicine(Medicine m) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        m.setId(newId);
        DatabaseModifier.appendRecord(FILE, m.toLineData());
    }

    /**
     * Updates an existing medicine's details matching the provided stock item ID.
     */
    public void updateMedicine(Medicine m) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(m.getId()), m.toLineData());
    }

    /**
     * Permanently drops a medicine row from the text file by its unique item ID.
     */
    public void removeMedicine(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches pharmacy inventory rows organized by your requested sorting parameters.
     * File Layout Indices: [0] Medicine ID, [1] Name, [2] Remaining Stock, [3] Unit Price
     */
    public ArrayList<String[]> getMedicines(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);

        switch (sortBy.toUpperCase()) {
            case "NAME"  -> DatabaseSorter.sortAlphabetically(rows, 1);
            case "STOCK" -> DatabaseSorter.sortByInteger(rows, 2);
            case "PRICE" -> DatabaseSorter.sortByDouble(rows, 3);
            default      -> DatabaseSorter.sortByInteger(rows, 0); // Default sorting: Medicine ID
        }
        return rows;
    }
}
