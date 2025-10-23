/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import javax.swing.*;
import com.formdev.flatlaf.FlatLightLaf;
import main.ui.LoginPanel;

public class TastytalesApp {
    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
            UIManager.put("Button.background", new java.awt.Color(187, 255, 200));
            UIManager.put("Button.arc", 12);
            UIManager.put("TextComponent.arc", 12);
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Panel.background", new java.awt.Color(245, 255, 247));
        } catch (Exception e) {
            System.err.println("FlatLaf failed to load. Using default look.");
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tastytales");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 600);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new java.awt.BorderLayout());

            LoginPanel loginPanel = new LoginPanel(frame);
            frame.add(loginPanel, java.awt.BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
