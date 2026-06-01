package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Patient;
import java.util.ArrayList;

public class PatientController {
    private static final String FILE = "patient";

    /**
     * Saves a Patient object to the text file database.
     */
    public void addPatient(Patient p) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        p.setId(newId);
        DatabaseModifier.appendRecord(FILE, p.toLineData());
    }

    /**
     * Updates an existing patient record matching the target ID.
     */
    public void updatePatient(Patient p) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(p.getId()), p.toLineData());
    }

    /**
     * Permanently deletes a patient record matching the specified ID.
     */
    public void removePatient(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches patient profiles sorted dynamically using database index configurations.
     * Columns: [0] ID, [1] Name, [2] Gender, [3] Age, [4] Ailment
     */
    public ArrayList<String[]> getPatients(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);
        switch (sortBy.toUpperCase()) {
            case "NAME" -> DatabaseSorter.sortAlphabetically(rows, 1);
            case "AGE"  -> DatabaseSorter.sortByDouble(rows, 3);
            default     -> DatabaseSorter.sortByInteger(rows, 0); // Default fallback: Patient ID
        }
        return rows;
    }
}
