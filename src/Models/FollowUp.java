package Models;

public class FollowUp {
    private int id;
    private int patientId;
    private String nextDate;
    private String notes;

    // No-arg Constructor
    public FollowUp() {}

    // Parameterized Constructor
    public FollowUp(int id, int patientId, String nextDate, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.nextDate = nextDate;
        this.notes = notes;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public String getNextDate() { return nextDate; }
    public void setNextDate(String nextDate) { this.nextDate = nextDate; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Converts this FollowUp model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                String.valueOf(this.patientId),
                this.nextDate,
                this.notes
        };
    }

    /**
     * Parses a raw text array row safely back into a clean FollowUp object.
     */
    public static FollowUp fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 4) return null;

            int id = Integer.parseInt(fields[0].trim());
            int patientId = Integer.parseInt(fields[1].trim());
            String nextDate = fields[2].trim();
            String notes = fields[3].trim();

            return new FollowUp(id, patientId, nextDate, notes);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in followup.txt: " + e.getMessage());
            return null; // Gracefully filters formatting issues out of data pipelines
        }
    }
}
