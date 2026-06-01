package View;

import javax.swing.*;
import java.awt.*;

public class HospitalDashboard extends JPanel {
    public HospitalDashboard() {
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Wire up all 7 structural feature panels
        tabbedPane.addTab("Doctors", new DoctorPanel());
        tabbedPane.addTab("Patients", new PatientPanel());
        tabbedPane.addTab("Appointments", new AppointmentPanel());
        tabbedPane.addTab("Bills", new BillPanel());
        tabbedPane.addTab("Follow-Ups", new FollowUpPanel());
        tabbedPane.addTab("Medicine", new MedicinePanel());
        tabbedPane.addTab("Prescriptions", new PrescriptionPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}
