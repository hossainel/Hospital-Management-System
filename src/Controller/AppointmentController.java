package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.Appointment;
import java.util.ArrayList;

public class AppointmentController {
    private static final String FILE = "appointment";

    /**
     * Appends a newly scheduled Appointment record line to the flat file.
     */
    public void addAppointment(Appointment a) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        a.setId(newId);
        DatabaseModifier.appendRecord(FILE, a.toLineData());
    }

    /**
     * Edits an existing appointment record line matching the appointment's ID.
     */
    public void updateAppointment(Appointment a) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(a.getId()), a.toLineData());
    }

    /**
     * Permanently drops an appointment record line matching the specified ID.
     */
    public void removeAppointment(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches appointment tracking rows organized by user selection filters.
     * File Layout Indices: [0] Appt ID, [1] Patient ID, [2] Doctor ID, [3] Date, [4] Time
     */
    public ArrayList<String[]> getAppointments(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);

        switch (sortBy.toUpperCase()) {
            case "DATE"      -> DatabaseSorter.sortAlphabetically(rows, 3);
            case "PATIENTID" -> DatabaseSorter.sortByInteger(rows, 1);
            case "DOCTORID"  -> DatabaseSorter.sortByInteger(rows, 2);
            default          -> DatabaseSorter.sortByInteger(rows, 0); // Default fallback sorting: Appointment ID
        }
        return rows;
    }
}
