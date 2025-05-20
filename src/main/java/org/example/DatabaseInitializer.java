package org.example;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.io.FileReader;

public class DatabaseInitializer {
    private static Connection connection;

    public static void main(String[] args) {
        try {
            String password = System.getenv("SQL_PASSWORD");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", password);
            createTables();
            loadCoreGoals();
            loadCourses();
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

    /**
     * Loads the courses into the database.
     * @throws SQLException if a database access error occurs.
     */
    public static void loadCourses() throws SQLException {
        // Implement the logic to load courses from a CSV file or other source
        // and insert them into the course table.
        for (CoreCode code : CoreCode.values()) {
            String filePath = "src/main/resources/" + code.name() + ".csv";
            try (FileReader reader = new FileReader(filePath)) {
                List<Course> courses = new CsvToBeanBuilder<Course>(reader)
                        .withType(Course.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build()
                        .parse();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO course (course_number, course_title, credits, core_codes, subject) VALUES (?, ?, ?, ?, ?)");
                for (Course course : courses) {
                    preparedStatement.setString(1, course.getCourseNumber());
                    preparedStatement.setString(2, course.getTitle());
                    preparedStatement.setFloat(3, course.getCredits());
                    preparedStatement.setString(4, String.join(",", course.getCoreCodes().stream().map(CoreCode::name).toArray(String[]::new)));
                    preparedStatement.setString(5, course.getSubject());
                    preparedStatement.executeUpdate();
                }
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + filePath);
            } catch (IOException e) {
                System.err.println("File could not be opened: " + filePath);
            }
        }
    }
}
