package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A controller class for handling course-related requests.
 */
@RestController
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private ResultSet resultSet;
    private final CourseRepository courseRepository;

    /**
     * Constructor for CourseController.
     * @param courseRepository the CourseRepository instance for database operations.
     */
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    @GetMapping("/courses")
    public List<Course> courses(@RequestParam("tokens") List<String> tokens) {
        if(!validateTokens(tokens)) {
            logger.error("Invalid tokens provided: {}", tokens);
            return null;
        }
        String query = getQuery(tokens);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(LoadDatabase.DB_URL, LoadDatabase.USER, LoadDatabase.PASSWORD);
            resultSet = connection.createStatement().executeQuery(query);
            return getCourses();
        } catch (SQLException e) {
            logger.error("Error executing SQL query: {}", query, e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("Error closing database connection", e);
                }
            }
        }
        return null;
    }

    /**
     * Generates a SQL query based on the provided tokens.
     * @return a SQL query string to obtain courses based on the given tokens.
     */
    private static String getQuery(List<String> tokens) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM course WHERE course.id IN (");
        for (String token: tokens) {
            if (token.equals("OR")) {
                queryBuilder.append(" UNION ");
            } else if (token.equals("AND")) {
                queryBuilder.append(" INTERSECT ");
            } else {
                queryBuilder.append("(SELECT course_id FROM course_core WHERE core_code = '").append(token).append("')");
            }
        }
        queryBuilder.append(");");
        return queryBuilder.toString();
    }

    /**
     * Retrieves a list of courses from the result set.
     * @return a list of Course objects containing course information.
     * @throws SQLException if a database access error occurs.
     */
    private List<Course> getCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        while (resultSet.next()) {
            Course course = getNextCourse();
            courses.add(course);
        }
        return courses;
    }

    /**
     * Retrieves the next course from the result set.
     * @return a Course object containing course information.
     * @throws SQLException if a database access error occurs.
     */
    private Course getNextCourse() throws SQLException {
        String courseNumber = resultSet.getString("course_number");
        String title = resultSet.getString("course_title");
        float credits = resultSet.getFloat("credits");
        String[] coreCodes = resultSet.getString("core_codes").split(",");
        List<CoreCode> cores = new ArrayList<>();
        for (String coreCode: coreCodes) {
            cores.add(CoreCode.valueOf(coreCode));
        }
        String subject = resultSet.getString("subject");
        return new Course(courseNumber, title, credits, cores, subject);
    }

    /**
     * Validates the provided tokens to ensure they are in the correct format.
     * @param tokens a list of tokens to validate.
     * @return true if the tokens are valid, false otherwise.
     */
    private static boolean validateTokens(List<String> tokens) {
        for (int i = 0; i < tokens.size(); i++) {
            if((i % 2 == 0)) {
                boolean isCode = false;
                for(CoreCode code : CoreCode.values()) {
                    if (tokens.get(i).equals(code.name())) {
                        isCode = true;
                        break;
                    }
                }
                if(!isCode) {
                    return false;
                }
            } else if(!tokens.get(i).equals("AND") && !tokens.get(i).equals("OR")) {
                return false;
            }
        }
        return true;
    }
}
