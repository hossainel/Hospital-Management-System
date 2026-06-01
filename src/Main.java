import Database.DatabaseEngine;
import View.HospitalDashboard;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        // Step 1: Execute verification scans on startup
        System.out.println("Validating local storage configurations...");
        DatabaseEngine.checkAndCreateFile("doctor");
        DatabaseEngine.checkAndCreateFile("patient");
        DatabaseEngine.checkAndCreateFile("appointment");
        DatabaseEngine.checkAndCreateFile("bill");
        DatabaseEngine.checkAndCreateFile("followup");
        DatabaseEngine.checkAndCreateFile("medicine");
        DatabaseEngine.checkAndCreateFile("prescription");
        System.out.println("System text database layer online.");

        // Step 2: Initialize UI rendering
        SwingUtilities.invokeLater(() -> {
            try {
                // Adopt standard native visual design presets
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

                HospitalDashboard dashboard = new HospitalDashboard();
                dashboard.setVisible(true);
            } catch (Exception e) {
                System.err.println("GUI Boot Interruption Error: " + e.getMessage());
            }
        });
    }
}
