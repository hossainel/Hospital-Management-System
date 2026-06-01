package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Prescription;
import java.util.ArrayList;

public class PrescriptionController {
    private static final String FILE = "prescription";

    /**
     * Appends a newly authorized medical Prescription record line to the flat file.
     */
    public void addPrescription(Prescription p) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        p.setId(newId);
        DatabaseModifier.appendRecord(FILE, p.toLineData());
    }

    /**
     * Updates an existing prescription line matching the target script reference ID.
     */
    public void updatePrescription(Prescription p) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(p.getId()), p.toLineData());
    }

    /**
     * Permanently drops a prescription row out of the system file by its unique script ID.
     */
    public void removePrescription(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches clinical scripts organized by your requested sorting parameters.
     * File Layout Indices: [0] Script ID, [1] Appointment ID, [2] Medicine Details, [3] Dosage
     */
    public ArrayList<String[]> getPrescriptions(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);

        switch (sortBy.toUpperCase()) {
            case "MEDICINE"      -> DatabaseSorter.sortAlphabetically(rows, 2);
            case "APPOINTMENTID" -> DatabaseSorter.sortByInteger(rows, 1);
            default              -> DatabaseSorter.sortByInteger(rows, 0); // Default sorting: Prescription ID
        }
        return rows;
    }
}
