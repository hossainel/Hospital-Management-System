// File: src/View/BillPanel.java
package View;

import Controller.BillController;
import Database.DatabaseEngine;
import Models.Bill;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class BillPanel extends BaseManagementPanel<Bill> {
    private final BillController controller = new BillController();
    private HashMap<String, String> patientMap;

    public BillPanel() {
        super(
                "Billing Ledger Administration",
                "bill",
                new String[]{"Bill ID", "Patient Name", "Amount Due ($)", "Payment Status"},
                new String[]{"ID", "Amount", "Status", "PatientID"}
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        // FIX: Ensure file extensions match throughout the lookup process
        patientMap = DatabaseEngine.getNameLookupMap("patient.txt");
        return controller.getBills(sortBy);
    }

    @Override
    protected Bill convertRowToModel(String[] row) {
        return Bill.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Bill b) {
        String patientName = patientMap.getOrDefault(String.valueOf(b.getPatientId()), "Unknown Patient (ID: " + b.getPatientId() + ")");

        return new String[] {
                String.valueOf(b.getId()),
                patientName,
                String.format("%.2f", b.getAmount()), // FIX: Clean currency formatting
                b.getStatus()
        };
    }

    @Override
    protected boolean matchesSearchCriteria(Bill b, String query) {
        String pName = patientMap.getOrDefault(String.valueOf(b.getPatientId()), "").toLowerCase();

        return b.getStatus().toLowerCase().contains(query) ||
                String.valueOf(b.getId()).contains(query) ||
                pName.contains(query);
    }

    @Override
    protected void openAddDialog() {
        SearchableComboBox patientDropdown = new SearchableComboBox("patient.txt");
        JTextField amtF = new JTextField();
        JComboBox<String> statF = new JComboBox<>(new String[]{"UNPAID", "PAID", "OVERDUE"});

        JPanel form = buildFormLayout(patientDropdown, amtF, statF);

        int result = JOptionPane.showConfirmDialog(this, form, "Emit New Bill (ID Auto-Assigned)", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedPatientId = patientDropdown.getSelectedId();
                if (selectedPatientId.isEmpty()) {
                    throw new IllegalArgumentException("Target patient selection mapping incomplete.");
                }

                Bill b = new Bill(
                        0,
                        Integer.parseInt(selectedPatientId),
                        Double.parseDouble(amtF.getText().trim()),
                        statF.getSelectedItem().toString()
                );
                controller.addBill(b);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    @Override
    protected void openEditDialog(Bill existingBill) {
        SearchableComboBox patientDropdown = new SearchableComboBox("patient.txt");
        JTextField amtF = new JTextField(String.valueOf(existingBill.getAmount()));
        JComboBox<String> statF = new JComboBox<>(new String[]{"UNPAID", "PAID", "OVERDUE"});

        patientDropdown.setSelectedById(String.valueOf(existingBill.getPatientId()));
        statF.setSelectedItem(existingBill.getStatus());

        JPanel form = buildFormLayout(patientDropdown, amtF, statF);

        int result = JOptionPane.showConfirmDialog(this, form, "Modify Invoice Statement ID: " + existingBill.getId(), JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedPatientId = patientDropdown.getSelectedId();
                if (selectedPatientId.isEmpty()) {
                    throw new IllegalArgumentException("Target patient selection parameter required.");
                }

                Bill updated = new Bill(
                        existingBill.getId(),
                        Integer.parseInt(selectedPatientId),
                        Double.parseDouble(amtF.getText().trim()),
                        statF.getSelectedItem().toString()
                );
                controller.updateBill(updated);
                loadData();
            } catch (Exception ex) {
                showValidationErrorAlert();
            }
        }
    }

    private JPanel buildFormLayout(SearchableComboBox patientBox, JTextField amount, JComboBox<String> status) {
        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Select Account Patient:")); form.add(patientBox);
        form.add(new JLabel("Statement Net Balance Due ($):")); form.add(amount);
        form.add(new JLabel("Current Invoice Account Status:")); form.add(status);
        return form;
    }

    private void showValidationErrorAlert() {
        JOptionPane.showMessageDialog(this, "Validation Error: Please verify numerical entries.", "Data Formatting Failure", JOptionPane.ERROR_MESSAGE);
    }
}
