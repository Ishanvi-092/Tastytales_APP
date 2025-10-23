/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import javax.swing.*;
import java.awt.*;
import main.db.DBConnection;
import java.sql.Connection;

public class RegisterPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JFrame parentFrame;

    public RegisterPanel(JFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Register", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        add(new JLabel("Username:"), gbc);
        usernameField = new JTextField(15);
        gbc.gridx = 1;
        add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        registerButton = new JButton("Register");
        add(registerButton, gbc);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            if(username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username and password.");
                return;
            }
            try(Connection conn = DBConnection.getConnection()) {
                var pstmt = conn.prepareStatement(
                        "INSERT INTO users (username, password) VALUES (?, ?)",
                        java.sql.Statement.RETURN_GENERATED_KEYS
                );
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                pstmt.executeUpdate();
                var rs = pstmt.getGeneratedKeys();
                int userId = -1;
                if(rs.next()) userId = rs.getInt(1);
                parentFrame.getContentPane().removeAll();
                parentFrame.getContentPane().add(new ProfileSetupFrame(parentFrame, userId));
                parentFrame.revalidate();
                parentFrame.repaint();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
