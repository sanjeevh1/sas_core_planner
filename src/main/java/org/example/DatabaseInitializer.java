package org.example;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.io.FileReader;

public class DatabaseInitializer {
    private static Connection connection = null;
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/course_info";
    public static final String USER = "root";
    public static final String PASSWORD = System.getenv("SQL_PASSWORD");
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    public static void main(String[] args) {
        try {
            connection = DriverManager.getConnection(DEFAULT_URL, USER, PASSWORD);
            createTables();
            loadCoreGoals();
            loadCourses();
            logger.info("Database initialized successfully.");
        } catch (SQLException e) {
            logger.error("Database connection error: ", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: ", e);
                }
            }
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
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT IGNORE INTO course (course_number, course_title, credits, core_codes, subject) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                PreparedStatement corePreparedStatement = connection.prepareStatement("INSERT IGNORE INTO course_core (course_id, core_code) VALUES (?, ?)");
                for (Course course : courses) {
                    preparedStatement.setString(1, course.getCourseNumber());
                    preparedStatement.setString(2, course.getTitle());
                    preparedStatement.setFloat(3, course.getCredits());
                    preparedStatement.setString(4, String.join(",", course.getCoreCodes().stream().map(CoreCode::name).toArray(String[]::new)));
                    preparedStatement.setString(5, course.getSubject());
                    preparedStatement.executeUpdate();
                    ResultSet resultSet = preparedStatement.getGeneratedKeys();
                    if(resultSet.next()) {
                        int courseId = resultSet.getInt(1);
                        for (CoreCode coreCode : course.getCoreCodes()) {
                            corePreparedStatement.setInt(1, courseId);
                            corePreparedStatement.setString(2, coreCode.name());
                            corePreparedStatement.executeUpdate();
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                logger.error("File not found: {}", filePath, e);
            } catch (IOException e) {
                logger.error("Error reading file: {}", filePath, e);
            }
        }
    }
}
