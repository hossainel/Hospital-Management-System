package View;

import Controller.AppointmentController;
import Database.DatabaseEngine;
import Models.Appointment;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;

public class AppointmentPanel extends BaseManagementPanel<Appointment> {
    private final AppointmentController controller = new AppointmentController();
    private HashMap<String, String> doctorMap;
    private HashMap<String, String> patientMap;

    public AppointmentPanel() {
        super(
                "Appointments Management",
                "appointment",
                new String[]{"Appt ID", "Patient Name", "Doctor Name", "Date", "Time"},
                new String[]{"ID", "Date", "PatientID", "DoctorID"}
        );
    }

    @Override
    protected ArrayList<String[]> fetchRawRows(String sortBy) {
        // Refresh lookup maps to ensure names are current
        doctorMap = DatabaseEngine.getNameLookupMap("doctor.txt");
        patientMap = DatabaseEngine.getNameLookupMap("patient.txt");
        return controller.getAppointments(sortBy);
    }

    @Override
    protected Appointment convertRowToModel(String[] row) {
        return Appointment.fromLineData(row);
    }

    @Override
    protected String[] convertModelToRow(Appointment appt) {
        String pName = patientMap.getOrDefault(String.valueOf(appt.getPatientId()), "Unknown");
        String dName = doctorMap.getOrDefault(String.valueOf(appt.getDoctorId()), "Unknown");

        return new String[] {
                String.valueOf(appt.getId()),
                pName,
                dName,
                appt.getDate(),
                appt.getTime()
        };
    }

    @Override
    protected boolean matchesSearchCriteria(Appointment appt, String query) {
        String pName = patientMap.getOrDefault(String.valueOf(appt.getPatientId()), "").toLowerCase();
        String dName = doctorMap.getOrDefault(String.valueOf(appt.getDoctorId()), "").toLowerCase();
        return appt.getDate().toLowerCase().contains(query) || pName.contains(query) || dName.contains(query);
    }

    @Override
    protected void openAddDialog() {
        SearchableComboBox pBox = new SearchableComboBox("patient.txt");
        SearchableComboBox dBox = new SearchableComboBox("doctor.txt");
        JTextField dateF = new JTextField("2026-06-01");
        JTextField timeF = new JTextField("10:00 AM");

        JPanel form = buildFormLayout(pBox, dBox, dateF, timeF);

        int result = JOptionPane.showConfirmDialog(this, form, "Book Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Appointment a = new Appointment(0, Integer.parseInt(pBox.getSelectedId()),
                        Integer.parseInt(dBox.getSelectedId()),
                        dateF.getText(), timeF.getText());
                controller.addAppointment(a);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Check your selections.");
            }
        }
    }

    @Override
    protected void openEditDialog(Appointment appt) {
        SearchableComboBox pBox = new SearchableComboBox("patient.txt");
        pBox.setSelectedById(String.valueOf(appt.getPatientId()));
        SearchableComboBox dBox = new SearchableComboBox("doctor.txt");
        dBox.setSelectedById(String.valueOf(appt.getDoctorId()));
        JTextField dateF = new JTextField(appt.getDate());
        JTextField timeF = new JTextField(appt.getTime());

        JPanel form = buildFormLayout(pBox, dBox, dateF, timeF);

        int result = JOptionPane.showConfirmDialog(this, form, "Edit Appointment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Appointment updated = new Appointment(appt.getId(), Integer.parseInt(pBox.getSelectedId()),
                        Integer.parseInt(dBox.getSelectedId()),
                        dateF.getText(), timeF.getText());
                controller.updateAppointment(updated);
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Update failed.");
            }
        }
    }

    private JPanel buildFormLayout(SearchableComboBox p, SearchableComboBox d, JTextField date, JTextField time) {
        JPanel f = new JPanel(new GridLayout(4, 2, 5, 5));
        f.add(new JLabel("Patient:")); f.add(p);
        f.add(new JLabel("Doctor:")); f.add(d);
        f.add(new JLabel("Date:")); f.add(date);
        f.add(new JLabel("Time:")); f.add(time);
        return f;
    }
}
