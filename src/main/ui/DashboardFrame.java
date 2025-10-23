/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import main.db.DBConnection;

public class DashboardFrame extends JPanel {

    public DashboardFrame(int userId) {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Tastytales Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        List<Recipe> recipesToShow = new ArrayList<>();

        try(var conn = DBConnection.getConnection()) {
            var stmt = conn.createStatement();
            ResultSet profileRs = stmt.executeQuery("SELECT * FROM profile WHERE user_id=" + userId);
            String diet = "";
            String[] selectedCuisines = new String[0];
            String[] selectedAllergens = new String[0];

            if(profileRs.next()) {
                diet = profileRs.getString("diet");
                String cuisinesStr = profileRs.getString("cuisines");
                if(cuisinesStr != null && !cuisinesStr.isEmpty()) selectedCuisines = cuisinesStr.split(",");
                String allergensStr = profileRs.getString("allergens");
                if(allergensStr != null && !allergensStr.isEmpty()) selectedAllergens = allergensStr.split(",");
            }

            ResultSet rs = stmt.executeQuery("SELECT * FROM recipes");
            while(rs.next()) {
                String rDiet = rs.getString("diet_type");
                String rCuisine = rs.getString("cuisine_name");
                String rAllergens = rs.getString("allergens") != null ? rs.getString("allergens") : "";

                boolean dietMatch = diet.isEmpty() || rDiet.equalsIgnoreCase(diet);

                boolean cuisineMatch = selectedCuisines.length == 0; 
                for(String c : selectedCuisines) {
                    if(rCuisine.equalsIgnoreCase(c.trim())) { cuisineMatch = true; break; }
                }

                boolean allergenMatch = true; 
                for(String a : selectedAllergens) {
                    if(!a.trim().isEmpty() && rAllergens.contains(a.trim())) {
                        allergenMatch = false;
                        break;
                    }
                }

                if(dietMatch && cuisineMatch && allergenMatch) {
                    recipesToShow.add(new Recipe(
                            rs.getString("name"),
                            rCuisine,
                            rDiet,
                            rs.getString("cooking_prep_time"),
                            rs.getString("ingredients"),  // fetch ingredients
                            rs.getString("recipe_text")
                    ));
                }
            }

        } catch(Exception e) {
            JOptionPane.showMessageDialog(this, "Error fetching recipes: " + e.getMessage());
        }

        JPanel recipesPanel = new JPanel();
        recipesPanel.setLayout(new GridLayout(0, 2, 10, 10));
        for(Recipe r : recipesToShow) addRecipe(recipesPanel, r);

        JScrollPane scrollPane = new JScrollPane(recipesPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void addRecipe(JPanel panel, Recipe r) {
        JPanel recipe = new JPanel(new BorderLayout());
        recipe.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        recipe.setPreferredSize(new Dimension(200, 250));

        JTextArea info = new JTextArea(
                r.name + "\n" +
                r.cuisine + " | " + r.diet + "\n" +
                "Time: " + r.time + "\n\n" +
                "Ingredients:\n" + r.ingredients + "\n\n" +
                r.steps
        );
        info.setEditable(false);
        info.setLineWrap(true);
        info.setWrapStyleWord(true);
        recipe.add(info, BorderLayout.CENTER);
        panel.add(recipe);
    }

    static class Recipe {
        String name, cuisine, diet, time, ingredients, steps;
        Recipe(String name, String cuisine, String diet, String time, String ingredients, String steps) {
            this.name = name;
            this.cuisine = cuisine;
            this.diet = diet;
            this.time = time;
            this.ingredients = ingredients;
            this.steps = steps;
        }
    }
}
