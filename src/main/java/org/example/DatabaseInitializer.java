package org.example;

import java.sql.*;

public class DatabaseInitializer {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            String password = System.getenv("SQL_PASSWORD");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", password);
            createTables();
            loadCoreGoals();
            connection.close();
            System.out.println("Database updated.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the course_info database if it does not exist.
     * Creates the course table if it does not exist.
     * @throws SQLException if a database access error occurs.
     */
    public static void createTables() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS course_info");
        statement.execute("USE course_info");
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS course (
                id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                course_number CHAR(10) NOT NULL UNIQUE,
                course_title VARCHAR(255),
                credits FLOAT,
                core_codes VARCHAR(255),
                subject VARCHAR(255)
            );
        """);
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS core_goal (
                code CHAR(3) PRIMARY KEY NOT NULL,
                goal VARCHAR(255)
            );
        """);
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS course_core (
                course_id INT NOT NULL,
                core_code CHAR(3) NOT NULL,
                PRIMARY KEY (course_id, core_code),
                FOREIGN KEY (course_id) REFERENCES course(id),
                FOREIGN KEY (core_code) REFERENCES core_goal(code)
            );
        """);
    }

    /**
     * Loads the core goals into the database.
     * @throws SQLException if a database access error occurs.
     */
    public static void loadCoreGoals() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO core_goal (code, goal) VALUES (?, ?)");
        for (CoreCode coreCode : CoreCode.values()) {
            preparedStatement.setString(1, coreCode.name());
            preparedStatement.setString(2, coreCode.getGoal());
            preparedStatement.executeUpdate();
        }
    }
}
