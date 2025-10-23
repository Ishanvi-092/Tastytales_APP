package main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabase {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            System.out.println("Connected to MySQL successfully.");

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS tastytales");
            System.out.println("Database 'tastytales' created or already exists.");

            stmt.execute("USE tastytales");

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) UNIQUE NOT NULL,
                    password VARCHAR(255) NOT NULL
                );
            """;

            String createProfileTable = """
                CREATE TABLE IF NOT EXISTS profile (
                    profile_id INT AUTO_INCREMENT PRIMARY KEY,
                    user_id INT,
                    allergens TEXT,
                    cuisines TEXT,
                    diet VARCHAR(50),
                    FOREIGN KEY (user_id) REFERENCES users(user_id)
                        ON DELETE CASCADE
                );
            """;

            String createRecipesTable = """
                CREATE TABLE IF NOT EXISTS recipes (
                    recipe_id INT AUTO_INCREMENT PRIMARY KEY,
                    cuisine_name VARCHAR(100),
                    diet_type VARCHAR(50),
                    name VARCHAR(100),
                    cooking_prep_time VARCHAR(100),
                    ingredients TEXT,
                    allergens TEXT,
                    recipe_text TEXT
                );
            """;

            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createProfileTable);
            stmt.executeUpdate(createRecipesTable);

            System.out.println("All tables created successfully inside 'tastytales' database.");

            conn.close();
            System.out.println("Database setup completed successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
