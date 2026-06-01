package Models;

public class Prescription {
    private int id;
    private int appointmentId;
    private String medicineDetails;
    private String dosage;

    // No-arg Constructor
    public Prescription() {}

    // Parameterized Constructor
    public Prescription(int id, int appointmentId, String medicineDetails, String dosage) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.medicineDetails = medicineDetails;
        this.dosage = dosage;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getMedicineDetails() { return medicineDetails; }
    public void setMedicineDetails(String medicineDetails) { this.medicineDetails = medicineDetails; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    /**
     * Converts this Prescription model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                String.valueOf(this.appointmentId),
                this.medicineDetails,
                this.dosage
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Prescription object.
     */
    public static Prescription fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 4) return null;

            int id = Integer.parseInt(fields[0].trim());
            int appointmentId = Integer.parseInt(fields[1].trim());
            String medicineDetails = fields[2].trim();
            String dosage = fields[3].trim();

            return new Prescription(id, appointmentId, medicineDetails, dosage);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in prescription.txt: " + e.getMessage());
            return null; // Gracefully keeps malformed records from breaking data feeds
        }
    }
}
