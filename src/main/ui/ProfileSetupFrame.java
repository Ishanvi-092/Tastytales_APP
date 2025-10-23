/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import main.db.DBConnection;

public class ProfileSetupFrame extends JPanel {

    public ProfileSetupFrame(JFrame parentFrame, int userId) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Profile Setup", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title);
        add(Box.createVerticalStrut(10));

        JLabel allergenLabel = new JLabel("Select Allergens:");
        add(allergenLabel);
        String[] allergens = {"Nuts","Milk","Gluten","Soy","Seafood"};
        ArrayList<JCheckBox> allergenChecks = new ArrayList<>();
        for(String a : allergens) { JCheckBox cb = new JCheckBox(a); allergenChecks.add(cb); add(cb); }

        add(Box.createVerticalStrut(10));

        JLabel cuisineLabel = new JLabel("Select Favorite Cuisines:");
        add(cuisineLabel);
        String[] cuisines = {"Indian","Mediterranean","American","Italian","French","Thai","Lebanese",
                "Japanese","Greek","Vietnamese","Spanish","Brazilian","Peruvian","Korean",
                "Mexican","Turkish","Moroccan","Ethiopian","Chinese"};
        ArrayList<JCheckBox> cuisineChecks = new ArrayList<>();
        for(String c : cuisines) { JCheckBox cb = new JCheckBox(c); cuisineChecks.add(cb); add(cb); }

        add(Box.createVerticalStrut(10));

        JLabel dietLabel = new JLabel("Select Diet Type:");
        add(dietLabel);
        JRadioButton veg = new JRadioButton("Vegetarian");
        JRadioButton nonVeg = new JRadioButton("Non-Vegetarian");
        JRadioButton vegan = new JRadioButton("Vegan");
        ButtonGroup dietGroup = new ButtonGroup();
        dietGroup.add(veg); dietGroup.add(nonVeg); dietGroup.add(vegan);
        add(veg); add(nonVeg); add(vegan);

        add(Box.createVerticalStrut(20));

        JButton submit = new JButton("Submit");
        add(submit);

        submit.addActionListener(e -> {
            List<String> selectedAllergens = new ArrayList<>();
            for(JCheckBox cb: allergenChecks) if(cb.isSelected()) selectedAllergens.add(cb.getText());

            List<String> selectedCuisines = new ArrayList<>();
            for(JCheckBox cb: cuisineChecks) if(cb.isSelected()) selectedCuisines.add(cb.getText());

            String diet = "";
            if(veg.isSelected()) diet="Vegetarian";
            else if(nonVeg.isSelected()) diet="Non-Vegetarian";
            else if(vegan.isSelected()) diet="Vegan";

            try(var conn = DBConnection.getConnection()) {
                var stmt = conn.createStatement();
                var rsCheck = stmt.executeQuery("SELECT * FROM profile WHERE user_id=" + userId);
                if(rsCheck.next()) {
                    var pstmt = conn.prepareStatement(
                        "UPDATE profile SET allergens=?, cuisines=?, diet=? WHERE user_id=?"
                    );
                    pstmt.setString(1, String.join(",", selectedAllergens));
                    pstmt.setString(2, String.join(",", selectedCuisines));
                    pstmt.setString(3, diet);
                    pstmt.setInt(4, userId);
                    pstmt.executeUpdate();
                } else {
                    var pstmt = conn.prepareStatement(
                        "INSERT INTO profile (user_id, allergens, cuisines, diet) VALUES (?, ?, ?, ?)"
                    );
                    pstmt.setInt(1, userId);
                    pstmt.setString(2, String.join(",", selectedAllergens));
                    pstmt.setString(3, String.join(",", selectedCuisines));
                    pstmt.setString(4, diet);
                    pstmt.executeUpdate();
                }

                parentFrame.getContentPane().removeAll();
                parentFrame.getContentPane().add(new DashboardFrame(userId));
                parentFrame.revalidate();
                parentFrame.repaint();
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }
}
