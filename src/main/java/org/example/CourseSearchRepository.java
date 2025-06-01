package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A repository class for handling database operations related to courses.
 */
@Repository
public class CourseSearchRepository {

    private static final Logger logger = LoggerFactory.getLogger(CourseSearchRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    public List<Course> getCourses(List<String> tokens) {
        if(!validateTokens(tokens)) {
            logger.error("Invalid tokens provided: {}", tokens);
            return null;
        }
        String query = getQuery(tokens);
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Course.class));
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
     * Initializes the database tables required for the course repository.
     */
    public void initializeTables() {
        String courseTable = """
            CREATE TABLE IF NOT EXISTS course (
                id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                course_number CHAR(10) NOT NULL,
                course_title VARCHAR(255),
                credits FLOAT,
                subject VARCHAR(255)
            );
        """;
        jdbcTemplate.execute(courseTable);
        String courseCore = """
            CREATE TABLE IF NOT EXISTS course_core (
                course_id INT NOT NULL,
                core_code CHAR(3) NOT NULL,
                PRIMARY KEY (course_id, core_code),
                FOREIGN KEY (course_id) REFERENCES course(id)
            );
        """;
        jdbcTemplate.execute(courseCore);
        String user = """      
                CREATE TABLE IF NOT EXISTS user (
                id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                username VARCHAR(50) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL
            );
        """;
        jdbcTemplate.execute(user);
        String userCourse = """
            CREATE TABLE IF NOT EXISTS user_course (
                user_id INT NOT NULL,
                course_id INT NOT NULL,
                PRIMARY KEY (user_id, course_id),
                FOREIGN KEY (user_id) REFERENCES user(id),
                FOREIGN KEY (course_id) REFERENCES course(id)
            );
        """;
        jdbcTemplate.execute(userCourse);
    }
}
