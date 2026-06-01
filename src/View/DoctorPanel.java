package View;

import Controller.DoctorController;
import Models.Doctor;
import Models.Human;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class DoctorPanel extends BaseManagementPanel<Doctor> {
    private final DoctorController controller = new DoctorController();

    public DoctorPanel() {
        super(
                "Doctor Directory",
                "doctor",
                new String[]{"ID", "Name", "Gender", "Age", "Specialization", "Contact"}, // Table Columns
                new String[]{"ID", "Name", "Age"}                                         // Sort Options
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        return controller.getDoctors(sortBy);
    }

    @Override
    protected Doctor convertRowToModel(String[] row) {
        return Doctor.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Doctor doc) {
        return doc.toLineData();
    }

    @Override
    protected boolean matchesSearchCriteria(Doctor doc, String query) {
        return doc.getName().toLowerCase().contains(query) ||
                doc.getSpecialization().toLowerCase().contains(query);
    }

    @Override
    protected void openAddDialog() {
        // UI Fields (No ID field needed to match the Auto-Increment rules)
        JTextField nameF = new JTextField();
        JComboBox<Human.Gender> genF = new JComboBox<>(Human.Gender.values());
        JTextField ageF = new JTextField();
        JTextField specF = new JTextField();
        JTextField contactF = new JTextField();

        JPanel form = buildFormLayout(nameF, genF, ageF, specF, contactF);

        int result = JOptionPane.showConfirmDialog(this, form, "Add New Doctor (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Instantiated with a temporary placeholder ID of 0; Controller handles auto-assignment
                Doctor d = new Doctor(
                        0,
                        nameF.getText().trim(),
                        (Human.Gender) genF.getSelectedItem(),
                        Double.parseDouble(ageF.getText().trim()),
                        specF.getText().trim(),
                        contactF.getText().trim()
                );
                controller.addDoctor(d);
                loadData(); // Universal refresh method from parent
            } catch (Exception ex) {
                showParsingErrorMessage();
            }
        }
    }

    @Override
    protected void openEditDialog(Doctor existingDoc) {
        // Pre-populate input configurations with the selected record's current state
        JTextField nameF = new JTextField(existingDoc.getName());
        JComboBox<Human.Gender> genF = new JComboBox<>(Human.Gender.values());
        genF.setSelectedItem(existingDoc.getGender());
        JTextField ageF = new JTextField(String.valueOf(existingDoc.getAge()));
        JTextField specF = new JTextField(existingDoc.getSpecialization());
        JTextField contactF = new JTextField(existingDoc.getContact());

        JPanel form = buildFormLayout(nameF, genF, ageF, specF, contactF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Doctor ID: " + existingDoc.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Create updated instance holding the permanent ID key anchor intact
                Doctor updated = new Doctor(
                        existingDoc.getId(),
                        nameF.getText().trim(),
                        (Human.Gender) genF.getSelectedItem(),
                        Double.parseDouble(ageF.getText().trim()),
                        specF.getText().trim(),
                        contactF.getText().trim()
                );
                controller.updateDoctor(updated);
                loadData();
            } catch (Exception ex) {
                showParsingErrorMessage();
            }
        }
    }

    // --- Helper Interface Mockups Layout Grid ---
    private JPanel buildFormLayout(JTextField name, JComboBox<Human.Gender> gen, JTextField age, JTextField spec, JTextField contact) {
        JPanel form = new JPanel(new GridLayout(5, 2, 5, 5));
        form.add(new JLabel("Full Name:")); form.add(name);
        form.add(new JLabel("Gender Context:")); form.add(gen);
        form.add(new JLabel("Age:")); form.add(age);
        form.add(new JLabel("Specialization Field:")); form.add(spec);
        form.add(new JLabel("Contact String:")); form.add(contact);
        return form;
    }

    private void showParsingErrorMessage() {
        JOptionPane.showMessageDialog(this, "Validation Error: Please verify that the age parameter is numeric.", "Data Entry Error", JOptionPane.ERROR_MESSAGE);
    }
}
