package View;

import Controller.FollowUpController;
import Database.DatabaseEngine;
import Models.FollowUp;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class FollowUpPanel extends BaseManagementPanel<FollowUp> {
    private final FollowUpController controller = new FollowUpController();
    private HashMap<String, String> patientMap;

    public FollowUpPanel() {
        super(
                "Patient Follow-Up Schedules",
                "followup",
                new String[]{"FollowUp ID", "Patient Name", "Next Visit Date", "Progress Notes"}, // Human-Readable Column Names
                new String[]{"ID", "Date", "PatientID"}                                          // Sort Filter Criteria Options
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        // Re-compile real-time structural reference patient mappings before data loading operations
        patientMap = DatabaseEngine.getNameLookupMap("patient.txt");
        return controller.getFollowUps(sortBy);
    }

    @Override
    protected FollowUp convertRowToModel(String[] row) {
        return FollowUp.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(FollowUp f) {
        // Intercept and resolve plain numerical patient ID variables down to real human names
        String patientName = patientMap.getOrDefault(String.valueOf(f.getPatientId()), "Unknown Patient (ID: " + f.getPatientId() + ")");

        return new String[] {
                String.valueOf(f.getId()),
                patientName,
                f.getNextDate(),
                f.getNotes()
        };
    }

    @Override
    protected boolean matchesSearchCriteria(FollowUp f, String query) {
        // Search matches against notes, visit dates, or resolved clear name values seamlessly
        String pName = patientMap.getOrDefault(String.valueOf(f.getPatientId()), "").toLowerCase();

        return f.getNotes().toLowerCase().contains(query) ||
                f.getNextDate().toLowerCase().contains(query) ||
                pName.contains(query);
    }

    @Override
    protected void openAddDialog() {
        // Implements dynamic searchable dropdown components
        SearchableComboBox patientDropdown = new SearchableComboBox("patient.txt");
        JTextField dateF = new JTextField("2026-06-15");
        JTextField notesF = new JTextField();

        JPanel form = buildFormLayout(patientDropdown, dateF, notesF);

        int result = JOptionPane.showConfirmDialog(this, form, "Plan Follow-Up Schedule (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedPatientId = patientDropdown.getSelectedId();
                if (selectedPatientId.isEmpty()) {
                    throw new IllegalArgumentException("Target patient selection mapping incomplete.");
                }

                // Temporary ID parameter set to 0; managed directly on append by controller layer
                FollowUp f = new FollowUp(
                        0,
                        Integer.parseInt(selectedPatientId),
                        dateF.getText().trim(),
                        notesF.getText().trim()
                );
                controller.addFollowUp(f);
                loadData(); // Trigger structural parent table view refresh
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    @Override
    protected void openEditDialog(FollowUp existingFollowUp) {
        SearchableComboBox patientDropdown = new SearchableComboBox("patient.txt");
        JTextField dateF = new JTextField(existingFollowUp.getNextDate());
        JTextField notesF = new JTextField(existingFollowUp.getNotes());

        // Focus state settings match selected model configuration anchors
        patientDropdown.setSelectedById(String.valueOf(existingFollowUp.getPatientId()));

        JPanel form = buildFormLayout(patientDropdown, dateF, notesF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Follow-Up Tracking Entry ID: " + existingFollowUp.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedPatientId = patientDropdown.getSelectedId();
                if (selectedPatientId.isEmpty()) {
                    throw new IllegalArgumentException("Target patient selection parameter required.");
                }

                FollowUp updated = new FollowUp(
                        existingFollowUp.getId(), // Keep permanent primary unique key completely intact
                        Integer.parseInt(selectedPatientId),
                        dateF.getText().trim(),
                        notesF.getText().trim()
                );
                controller.updateFollowUp(updated);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    // --- Helper User Interface Matrix Layout Mapping ---
    private JPanel buildFormLayout(SearchableComboBox patientBox, JTextField date, JTextField notes) {
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Select Return Patient:")); form.add(patientBox);
        form.add(new JLabel("Scheduled Return Date:")); form.add(date);
        form.add(new JLabel("Clinical Progress Notes:")); form.add(notes);
        return form;
    }

    private void showValidationErrorAlert() {
        JOptionPane.showMessageDialog(this, "Validation Error: Please verify text entries and confirm target relations match.", "Data Formatting Failure", JOptionPane.ERROR_MESSAGE);
    }
}
