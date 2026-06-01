import Database.DatabaseEngine;
import View.*;
import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainContainer = new JPanel(cardLayout);

    public Main() {
        // Step 1: Window Setup
        setTitle("Hospital Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen

        // Step 2: Initialize UI Components
        HospitalDashboard dashboard = new HospitalDashboard();
        LoginPanel loginPanel = new LoginPanel(() -> cardLayout.show(mainContainer, "DASHBOARD"));

        // Step 3: Setup Layout Flow
        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(dashboard, "DASHBOARD");
        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN");
    }

    public static void main(String[] args) {
        // Step 4: Verification Scans
        System.out.println("Validating local storage configurations...");
        String[] files = {"doctor", "patient", "appointment", "bill", "followup", "medicine", "prescription"};
        for (String file : files) {
            DatabaseEngine.checkAndCreateFile(file);
        }
        System.out.println("System text database layer online.");

        // Step 5: Start UI
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Main().setVisible(true);
            } catch (Exception e) {
                System.err.println("GUI Boot Interruption Error: " + e.getMessage());
            }
        });
    }
}
