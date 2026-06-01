package View;

import javax.swing.*;

public class HospitalDashboard extends JFrame {
    public HospitalDashboard() {
        setTitle("Hospital Management System - Consolidated Administration Console");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Wire up all 7 structural feature panels
        tabbedPane.addTab("Doctors", new DoctorPanel());
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Bills", new BillPanel());
        tabbedPane.addTab("Follow-Ups", new FollowUpPanel());
        tabbedPane.addTab("Medicine", new MedicinePanel());
        tabbedPane.addTab("Prescriptions", new PrescriptionPanel());

        add(tabbedPane);
    }
}
