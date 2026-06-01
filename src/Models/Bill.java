package Models;

public class Bill {
    private int id;
    private int patientId;
    private double amount;
    private String status;

    // No-arg Constructor
    public Bill() {}

    // Parameterized Constructor
    public Bill(int id, int patientId, double amount, String status) {
        this.id = id;
        this.patientId = patientId;
        this.amount = amount;
        this.status = status;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    /**
     * Converts this Bill model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                String.valueOf(this.patientId),
                String.valueOf(this.amount),
                this.status
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Bill object.
     */
    public static Bill fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 4) return null;

            int id = Integer.parseInt(fields[0].trim());
            int patientId = Integer.parseInt(fields[1].trim());
            double amount = Double.parseDouble(fields[2].trim());
            String status = fields[3].trim();

            return new Bill(id, patientId, amount, status);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in bill.txt: " + e.getMessage());
            return null; // Gracefully keeps malformed records from breaking data feeds
        }
    }
}
