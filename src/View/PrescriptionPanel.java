package View;

import Controller.PrescriptionController;
import Database.DatabaseEngine;
import Models.Prescription;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class PrescriptionPanel extends BaseManagementPanel<Prescription> {
    private final PrescriptionController controller = new PrescriptionController();
    private HashMap<String, String> appointmentMap;

    public PrescriptionPanel() {
        super(
                "Clinical Prescription Scripts",
                "prescription",
                new String[]{"Script ID", "Appointment Ref", "Medicine Details", "Dosage Instructions"},
                new String[]{"ID", "Medicine", "AppointmentID"}
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        // Build reference map for Appointments (Assuming appointment.txt uses ID at index 0 and Date/ID at index 1 for display)
        appointmentMap = DatabaseEngine.getNameLookupMap("appointment.txt");
        return controller.getPrescriptions(sortBy);
    }

    @Override
    protected Prescription convertRowToModel(String[] row) {
        return Prescription.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Prescription p) {
        String apptRef = appointmentMap.getOrDefault(String.valueOf(p.getAppointmentId()), "ID: " + p.getAppointmentId());
        return new String[] {
                String.valueOf(p.getId()),
                apptRef,
                p.getMedicineDetails(),
                p.getDosage()
        };
    }

    @Override
    protected boolean matchesSearchCriteria(Prescription p, String query) {
        return p.getMedicineDetails().toLowerCase().contains(query) ||
                String.valueOf(p.getAppointmentId()).contains(query);
    }

    @Override
    protected void openAddDialog() {
        SearchableComboBox apptDropdown = new SearchableComboBox("appointment.txt");
        JTextField detailsF = new JTextField();
        JTextField doseF = new JTextField("1 tab x 3 times daily");

        JPanel form = buildFormLayout(apptDropdown, detailsF, doseF);

        int result = JOptionPane.showConfirmDialog(this, form, "Issue Clinical Script (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String apptId = apptDropdown.getSelectedId();
                if (apptId.isEmpty()) throw new Exception("Invalid Selection");

                Prescription p = new Prescription(0, Integer.parseInt(apptId), detailsF.getText().trim(), doseF.getText().trim());
                controller.addPrescription(p);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    @Override
    protected void openEditDialog(Prescription existingPresc) {
        SearchableComboBox apptDropdown = new SearchableComboBox("appointment.txt");
        apptDropdown.setSelectedById(String.valueOf(existingPresc.getAppointmentId()));

        JTextField detailsF = new JTextField(existingPresc.getMedicineDetails());
        JTextField doseF = new JTextField(existingPresc.getDosage());

        JPanel form = buildFormLayout(apptDropdown, detailsF, doseF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Script ID: " + existingPresc.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String apptId = apptDropdown.getSelectedId();
                Prescription updated = new Prescription(existingPresc.getId(), Integer.parseInt(apptId), detailsF.getText().trim(), doseF.getText().trim());
                controller.updatePrescription(updated);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    private JPanel buildFormLayout(SearchableComboBox apptBox, JTextField details, JTextField dosage) {
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Appointment Reference:")); form.add(apptBox);
        form.add(new JLabel("Medicine Formulation:")); form.add(details);
        form.add(new JLabel("Dosage Instructions:")); form.add(dosage);
        return form;
    }

    private void showValidationErrorAlert() {
        JOptionPane.showMessageDialog(this, "Validation error: Check selection and data format.", "Entry Error", JOptionPane.ERROR_MESSAGE);
    }
}
