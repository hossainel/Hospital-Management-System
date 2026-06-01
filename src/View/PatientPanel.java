package View;

import Controller.PatientController;
import Models.Patient;
import Models.Human;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

public class PatientPanel extends BaseManagementPanel<Patient> {
    private final PatientController controller = new PatientController();

    public PatientPanel() {
        super(
                "Patients Registry",
                "patient",
                new String[]{"ID", "Name", "Gender", "Age", "Ailment"}, // Table Columns
                new String[]{"ID", "Name", "Age"}                       // Sort Options
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        return controller.getPatients(sortBy);
    }

    @Override
    protected Patient convertRowToModel(String[] row) {
        return Patient.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Patient p) {
        return p.toLineData();
    }

    @Override
    protected boolean matchesSearchCriteria(Patient p, String query) {
        return p.getName().toLowerCase().contains(query) ||
                p.getAilment().toLowerCase().contains(query);
    }

    @Override
    protected void openAddDialog() {
        // Form Fields (ID text input component completely stripped out)
        JTextField nameF = new JTextField();
        JComboBox<Human.Gender> genF = new JComboBox<>(Human.Gender.values());
        JTextField ageF = new JTextField();
        JTextField ailF = new JTextField();

        JPanel form = buildFormLayout(nameF, genF, ageF, ailF);

        int result = JOptionPane.showConfirmDialog(this, form, "Admit New Patient (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Instantiated with a temporary placeholder ID of 0; Controller calculates the next real sequential key
                Patient p = new Patient(
                        0,
                        nameF.getText().trim(),
                        (Human.Gender) genF.getSelectedItem(),
                        Double.parseDouble(ageF.getText().trim()),
                        ailF.getText().trim()
                );
                controller.addPatient(p);
                loadData(); // Refreshes the panel dataset instantly
            } catch (Exception ex) {
                showParsingErrorMessage();
            }
        }
    }

    @Override
    protected void openEditDialog(Patient existingPatient) {
        // Pre-populate components with the selected row's historical data snapshot
        JTextField nameF = new JTextField(existingPatient.getName());
        JComboBox<Human.Gender> genF = new JComboBox<>(Human.Gender.values());
        genF.setSelectedItem(existingPatient.getGender());
        JTextField ageF = new JTextField(String.valueOf(existingPatient.getAge()));
        JTextField ailF = new JTextField(existingPatient.getAilment());

        JPanel form = buildFormLayout(nameF, genF, ageF, ailF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Patient ID: " + existingPatient.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                // Reconstruct updated instance keeping the permanent key anchor unchanged
                Patient updated = new Patient(
                        existingPatient.getId(),
                        nameF.getText().trim(),
                        (Human.Gender) genF.getSelectedItem(),
                        Double.parseDouble(ageF.getText().trim()),
                        ailF.getText().trim()
                );
                controller.updatePatient(updated);
                loadData();
            } catch (Exception ex) {
                showParsingErrorMessage();
            }
        }
    }

    // --- Helper UI Component Matrix Mapping Layout ---
    private JPanel buildFormLayout(JTextField name, JComboBox<Human.Gender> gen, JTextField age, JTextField ailment) {
        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));
        form.add(new JLabel("Full Name:")); form.add(name);
        form.add(new JLabel("Gender Context:")); form.add(gen);
        form.add(new JLabel("Age:")); form.add(age);
        form.add(new JLabel("Diagnosed Ailment:")); form.add(ailment);
        return form;
    }

    private void showParsingErrorMessage() {
        JOptionPane.showMessageDialog(this, "Validation Error: Please verify your age value formatting metrics match real decimals.", "Data Entry Error", JOptionPane.ERROR_MESSAGE);
    }
}
