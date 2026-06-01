package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Doctor;
import java.util.ArrayList;

public class DoctorController {
    private static final String FILE = "doctor";

    /**
     * Saves a Doctor object to the text file database.
     */
    public void addDoctor(Doctor d) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        d.setId(newId);
        DatabaseModifier.appendRecord(FILE, d.toLineData());
    }

    /**
     * Updates an existing doctor profile line matching the doctor's ID.
     */
    public void updateDoctor(Doctor d) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(d.getId()), d.toLineData());
    }

    /**
     * Permanently deletes a doctor profile line matching the specified ID.
     */
    public void removeDoctor(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches doctor profiles sorted dynamically using index layout configs.
     * Columns: [0] ID, [1] Name, [2] Gender, [3] Age, [4] Specialization, [5] Contact
     */
    public ArrayList<String[]> getDoctors(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);
        switch (sortBy.toUpperCase()) {
            case "NAME" -> DatabaseSorter.sortAlphabetically(rows, 1);
            case "AGE"  -> DatabaseSorter.sortByDouble(rows, 3);
            default     -> DatabaseSorter.sortByInteger(rows, 0); // Default fallback: Doctor ID
        }
        return rows;
    }
}
