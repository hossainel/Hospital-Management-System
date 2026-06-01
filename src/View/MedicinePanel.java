package View;

import Controller.MedicineController;
import Models.Medicine;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class MedicinePanel extends BaseManagementPanel<Medicine> {
    private final MedicineController controller = new MedicineController();

    public MedicinePanel() {
        super(
                "Pharmacy Inventory Management",
                "medicine",
                new String[]{"ID", "Medicine Name", "Available Stock", "Price ($)"},
                new String[]{"ID", "Name", "Stock", "Price"}
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        return controller.getMedicines(sortBy);
    }

    @Override
    protected Medicine convertRowToModel(String[] row) {
        return Medicine.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Medicine m) {
        return m.toLineData();
    }

    @Override
    protected boolean matchesSearchCriteria(Medicine m, String query) {
        return m.getName().toLowerCase().contains(query);
    }

    @Override
    protected void openAddDialog() {
        // ID field removed; IDs are auto-assigned by the system
        JTextField nameF = new JTextField();
        JTextField stockF = new JTextField();
        JTextField priceF = new JTextField();

        JPanel form = buildFormLayout(nameF, stockF, priceF);

        int result = JOptionPane.showConfirmDialog(this, form, "Add Pharmacy Inventory (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Instantiated with placeholder ID 0; managed by controller
                Medicine m = new Medicine(
                        0,
                        nameF.getText().trim(),
                        Integer.parseInt(stockF.getText().trim()),
                        Double.parseDouble(priceF.getText().trim())
                );
                controller.addMedicine(m);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    @Override
    protected void openEditDialog(Medicine existingMed) {
        JTextField nameF = new JTextField(existingMed.getName());
        JTextField stockF = new JTextField(String.valueOf(existingMed.getStock()));
        JTextField priceF = new JTextField(String.valueOf(existingMed.getPrice()));

        JPanel form = buildFormLayout(nameF, stockF, priceF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Inventory Entry ID: " + existingMed.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Keep permanent unique identifier anchor constant
                Medicine updated = new Medicine(
                        existingMed.getId(),
                        nameF.getText().trim(),
                        Integer.parseInt(stockF.getText().trim()),
                        Double.parseDouble(priceF.getText().trim())
                );
                controller.updateMedicine(updated);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    // --- Helper Interface Layout Mapping ---
    private JPanel buildFormLayout(JTextField name, JTextField stock, JTextField price) {
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Generic Formula Name:")); form.add(name);
        form.add(new JLabel("Available Batch Stock:")); form.add(stock);
        form.add(new JLabel("Unit Price ($):")); form.add(price);
        return form;
    }

    private void showValidationErrorAlert() {
        JOptionPane.showMessageDialog(this, "Validation Failure: Please verify all numeric input fields.", "Data Entry Error", JOptionPane.ERROR_MESSAGE);
    }
}
