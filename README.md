# Tastytales_APP
🍽️ TastyTales
TastyTales is an intelligent recipe recommendation and management system that blends Java Swing, MySQL, and Machine Learning to deliver personalized recipe suggestions.
It allows users to register, set dietary preferences and allergies, and view curated recipes from a large dataset.

🚀 Features
👤 User Authentication — Secure registration and login system.
🍲 Recipe Dashboard — Displays recipes dynamically from the dataset.
🧠 Machine Learning Recommendations — Suggests recipes based on user preferences and similarity scoring.
⚙️ Preference Management — Users can specify their diet (e.g., Vegan, Keto) and allergens.
🧾 Data Persistence — Recipes, users, and preferences are stored in MySQL.
🎨 Interactive UI — Intuitive and responsive design built using Java Swing.
🧩 Tech Stack
Layer	Technology Used
Frontend (UI)	Java Swing
Backend Logic	Java (DAO + MVC architecture)
Database	MySQL
Data Source	recipes_10000_v2.csv
Machine Learning	Python (Pandas, Scikit-learn, Joblib)
Recommendation Model	Content-Based Filtering (cosine similarity based on ingredients, cuisine, and diet)
Development Tools	IntelliJ IDEA, MySQL Workbench, Jupyter Notebook, Google Colab
🧠 ML Workflow (Recommendation System)
Data Preprocessing

Loaded dataset (recipes_10000_v2.csv) containing features like cuisine, diet, ingredients, and allergens.
Cleaned and vectorized text fields using TF-IDF.
Modeling

Applied Cosine Similarity to find recipes similar to user preferences.
Generated ranked recommendations dynamically.
Integration with Java App

Exported the recommendation output as a .csv file or API endpoint.
Java retrieves and displays recommended recipes in the dashboard UI.
🗂️ Project Structure
TastyTales/ ├── src/ │ ├── main/ │ │ ├── dao/ # Database access classes (RecipeDAO, UserDAO, etc.) │ │ ├── db/ # Database connection setup │ │ ├── model/ # JavaBeans (Recipe, User, Preferences) │ │ ├── ui/ # Swing UI files (LoginFrame, DashboardFrame, etc.) │ │ ├── ml/ # (Optional) Python scripts for recommendation model │ │ └── recipes_10000_v2.csv │ └── Main.java # Entry point ├── README.md └── requirements.txt # Python dependencies for ML
