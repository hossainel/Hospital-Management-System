package Controller;

import Database.DatabaseEngine;
import Database.DatabaseModifier;
import Database.DatabaseRemover;
import Database.DatabaseSorter;
import Models.FollowUp;
import java.util.ArrayList;

public class FollowUpController {
    private static final String FILE = "followup";

    /**
     * Appends a newly created FollowUp model row directly into the text file.
     */
    public void addFollowUp(FollowUp f) {
        int newId = DatabaseEngine.getNextAutoIncrementId(FILE);
        f.setId(newId);
        DatabaseModifier.appendRecord(FILE, f.toLineData());
    }

    /**
     * Updates an existing follow-up line matching the provided unique reference ID.
     */
    public void updateFollowUp(FollowUp f) {
        DatabaseModifier.updateRecordLine(FILE, String.valueOf(f.getId()), f.toLineData());
    }

    /**
     * Permanently drops a follow-up record row from the text file by its unique ID.
     */
    public void removeFollowUp(int id) {
        DatabaseRemover.deleteRecordById(FILE, String.valueOf(id));
    }

    /**
     * Fetches follow-up timeline sheets organized dynamically based on UI selection context.
     * File Layout Indices: [0] FollowUp ID, [1] Patient ID, [2] Return Date, [3] Progress Notes
     */
    public ArrayList<String[]> getFollowUps(String sortBy) {
        ArrayList<String[]> rows = DatabaseEngine.readTable(FILE);

        switch (sortBy.toUpperCase()) {
            case "DATE"      -> DatabaseSorter.sortAlphabetically(rows, 2);
            case "PATIENTID" -> DatabaseSorter.sortByInteger(rows, 1);
            default          -> DatabaseSorter.sortByInteger(rows, 0); // Default sorting: FollowUp ID
        }
        return rows;
    }
}
