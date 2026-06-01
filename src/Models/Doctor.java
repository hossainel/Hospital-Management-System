package Models;

public class Doctor implements Human {
    private int id;
    private String name;
    private Gender gender;
    private double age;
    private String specialization;
    private String contact;

    public Doctor() {}

    public Doctor(int id, String name, Gender gender, double age, String specialization, String contact) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.specialization = specialization;
        this.contact = contact;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }
    @Override public Gender getGender() { return gender; }
    @Override public void setGender(Gender gender) { this.gender = gender; }
    @Override public double getAge() { return age; }
    @Override public void setAge(double age) { this.age = age; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    /**
     * Converts this Doctor model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                this.name,
                this.gender.toString(),
                String.valueOf(this.age),
                this.specialization,
                this.contact
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Doctor object.
     */
    public static Doctor fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 6) return null;

            int id = Integer.parseInt(fields[0].trim());
            String name = fields[1].trim();
            Gender gender = Gender.valueOf(fields[2].trim().toUpperCase());
            double age = Double.parseDouble(fields[3].trim());
            String specialization = fields[4].trim();
            String contact = fields[5].trim();

            return new Doctor(id, name, gender, age, specialization, contact);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in doctor.txt: " + e.getMessage());
            return null; // Prevents runtime exceptions from breaking the data pipeline
        }
    }
}
