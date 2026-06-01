package Models;

public class Medicine {
    private int id;
    private String name;
    private int stock;
    private double price;

    // No-arg Constructor
    public Medicine() {}

    // Parameterized Constructor
    public Medicine(int id, String name, int stock, double price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    // --- GETTERS & SETTERS ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    /**
     * Converts this Medicine model state into a safe flat string array row.
     */
    public String[] toLineData() {
        return new String[] {
                String.valueOf(this.id),
                this.name,
                String.valueOf(this.stock),
                String.valueOf(this.price)
        };
    }

    /**
     * Parses a raw text array row safely back into a clean Medicine object.
     */
    public static Medicine fromLineData(String[] fields) {
        try {
            if (fields == null || fields.length < 4) return null;

            int id = Integer.parseInt(fields[0].trim());
            String name = fields[1].trim();
            int stock = Integer.parseInt(fields[2].trim());
            double price = Double.parseDouble(fields[3].trim());

            return new Medicine(id, name, stock, price);
        } catch (Exception e) {
            System.err.println("Skipping corrupted row in medicine.txt: " + e.getMessage());
            return null; // Gracefully keeps malformed records from breaking data feeds
        }
    }
}
