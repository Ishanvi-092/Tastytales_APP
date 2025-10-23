import java.sql.Connection;
import java.sql.PreparedStatement;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ImportRecipesCSV {
    public static void main(String[] args) {
        String csvFile = "C:/Users/kavya/OneDrive/Documents/NetBeansProjects/Tastytales1/Tastytales/src/main/db/recipes.csv";

        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("Database connected successfully!");

            List<String> lines = Files.readAllLines(Paths.get(csvFile));
            String sql = "INSERT INTO recipes (cuisine_name, diet_type, name, cooking_prep_time, ingredients, allergens, recipe_text) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                // ✅ Split correctly even if commas exist inside quotes
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (data.length < 7) continue;

                // ✅ Remove quotes around text fields
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].replaceAll("^\"|\"$", "").trim();
                }

                pstmt.setString(1, data[0]); // cuisine_name
                pstmt.setString(2, data[1]); // diet_type
                pstmt.setString(3, data[2]); // name
                pstmt.setString(4, data[3]); // cooking_prep_time
                pstmt.setString(5, data[4]); // ingredients
                pstmt.setString(6, data[5]); // allergens
                pstmt.setString(7, data[6]); // recipe_text

                pstmt.addBatch();
            }

            pstmt.executeBatch();
            System.out.println("CSV data imported successfully!");

        } catch (Exception e) {
            System.out.println("Error importing CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Simple DBConnection helper so DBConnection.getConnection() resolves.
    // It uses the JDBC URL from the environment variable JDBC_URL if set,
    // otherwise falls back to a local SQLite file 'recipes.db'.
    static class DBConnection {
        public static Connection getConnection() throws java.sql.SQLException {
            String url = System.getenv("JDBC_URL");
            if (url == null || url.isEmpty()) {
                url = "jdbc:sqlite:recipes.db";
            }
            return java.sql.DriverManager.getConnection(url);
        }
    }
}
