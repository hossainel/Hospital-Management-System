package Models;

public class Patient implements Human {
    private int id;
    private String name;
    private Gender gender;
    private double age;
    private String ailment;

    public Patient() {}

    public Patient(int id, String name, Gender gender, double age, String ailment) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.ailment = ailment;
    }

    @Override public int getId() { return id; }
    @Override public void setId(int id) { this.id = id; }
    @Override public String getName() { return name; }
    @Override public void setName(String name) { this.name = name; }
    @Override public Gender getGender() { return gender; }
    @Override public void setGender(Gender gender) { this.gender = gender; }
    @Override public double getAge() { return age; }
    @Override public void setAge(double age) { this.age = age; }

    public String getAilment() { return ailment; }
    public void setAilment(String ailment) { this.ailment = ailment; }

    /**
     * Converts this Patient model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                this.name,
                this.gender.toString(),
                String.valueOf(this.age),
                this.ailment
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Patient object.
     */
    public static Patient fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 5) return null;

            int id = Integer.parseInt(fields[0].trim());
            String name = fields[1].trim();
            Gender gender = Gender.valueOf(fields[2].trim().toUpperCase());
            double age = Double.parseDouble(fields[3].trim());
            String ailment = fields[4].trim();

            return new Patient(id, name, gender, age, ailment);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in patient.txt: " + e.getMessage());
            return null; // Prevents data stream formatting breaks
        }
    }
}
