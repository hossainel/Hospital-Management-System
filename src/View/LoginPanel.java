package View;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JTextField userField = new JTextField(15);
    private final JPasswordField passField = new JPasswordField(15);
    private final JButton loginBtn = new JButton("Login");

    public LoginPanel(Runnable onLoginSuccess) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // UI Components
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Username:"), gbc);
        gbc.gridx = 1; add(userField, gbc);
        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; add(passField, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(loginBtn, gbc);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            // Authentication check against your specific requirements
            if (username.equals("admin") && password.equals("1234")) {
                // Clear fields on successful entry for security
                userField.setText("");
                passField.setText("");
                onLoginSuccess.run();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Invalid username or password. Please try again.",
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
                passField.setText(""); // Clear password field on error
            }
        });
    }
}
