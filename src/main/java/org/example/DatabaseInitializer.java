package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static Connection connection;
    public static void main(String[] args) {
        try {
            String password = System.getenv("SQL_PASSWORD");
            System.out.println(password + " is the password");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", password);
            System.out.println("Connected to the database!");
            initializeDatabase();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the course_info database if it does not exist.
     * Creates the course table if it does not exist.
     * @throws SQLException
     */
    public static void initializeDatabase() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS course_info");
        statement.execute("USE course_info");
        statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS course (
                    course_id CHAR(10) PRIMARY KEY NOT NULL,
                    course_title VARCHAR(255),
                    credits FLOAT,
                    core_code VARCHAR(255),
                    subject VARCHAR(255)
                );
        """);
    }
}
