package Models;

public class Appointment {
    private int id;
    private int patientId;
    private int doctorId;
    private String date;
    private String time;

    // No-arg Constructor
    public Appointment() {}

    // Parameterized Constructor
    public Appointment(int id, int patientId, int doctorId, String date, String time) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    /**
     * Converts this Appointment model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                String.valueOf(this.patientId),
                String.valueOf(this.doctorId),
                this.date,
                this.time
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Appointment object.
     */
    public static Appointment fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 5) return null;

            int id = Integer.parseInt(fields[0].trim());
            int patientId = Integer.parseInt(fields[1].trim());
            int doctorId = Integer.parseInt(fields[2].trim());
            String date = fields[3].trim();
            String time = fields[4].trim();

            return new Appointment(id, patientId, doctorId, date, time);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in appointment.txt: " + e.getMessage());
            return null; // Gracefully handles malformed tokens without crash events
        }
    }
}
