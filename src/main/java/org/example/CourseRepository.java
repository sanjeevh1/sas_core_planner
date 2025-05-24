package org.example;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * A repository class for handling database operations related to courses.
 */
@Repository
public class CourseRepository {

    private static final Logger logger = LoggerFactory.getLogger(CourseRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    public List<Course> getCourses(List<String> tokens) {
        if(!validateTokens(tokens)) {
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
                course_number CHAR(10) NOT NULL UNIQUE,
                course_title VARCHAR(255),
                credits FLOAT,
                core_codes VARCHAR(255),
                subject VARCHAR(255)
            );
        """;
        jdbcTemplate.execute(courseTable);
        String coreTable = """
            CREATE TABLE IF NOT EXISTS core_goal (
                code CHAR(3) PRIMARY KEY NOT NULL,
                goal VARCHAR(255)
            );
        """;
        jdbcTemplate.execute(coreTable);
        String courseCore = """
            CREATE TABLE IF NOT EXISTS course_core (
                course_id INT NOT NULL,
                core_code CHAR(3) NOT NULL,
                PRIMARY KEY (course_id, core_code),
                FOREIGN KEY (course_id) REFERENCES course(id),
                FOREIGN KEY (core_code) REFERENCES core_goal(code)
            );
        """;
        jdbcTemplate.execute(courseCore);
    }

    /**
     * Loads the core goals into the database.
     */
    public void loadCoreGoals() {
        String sql = "INSERT IGNORE INTO core_goal (code, goal) VALUES (?, ?)";
        for (CoreCode coreCode : CoreCode.values()) {
            jdbcTemplate.update(sql, coreCode.name(), coreCode.getGoal());
        }
    }

    /**
     * Loads courses from a CSV file into the database.
     * @param filePath the path to the CSV file containing course data.
     */
    public void loadCourses(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            List<Course> courses = new CsvToBeanBuilder<Course>(reader)
                    .withType(Course.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            for (Course course : courses) {
                insertCourse(course);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", filePath, e);
        } catch (IOException e) {
            logger.error("Error reading file: {}", filePath, e);
        }
    }

    /**
     * Inserts a course into the database.
     * @param course the course to be inserted.
     */
    private void insertCourse(Course course) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String insertCourse = "INSERT IGNORE INTO course (course_number, course_title, credits, core_codes, subject) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertCourse, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, course.getCourseNumber());
            ps.setString(2, course.getCourseTitle());
            ps.setFloat(3, course.getCredits());
            ps.setString(4, String.join(",", course.getCoreCodes().stream().map(CoreCode::name).toArray(String[]::new)));
            ps.setString(5, course.getSubject());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            int courseId = keyHolder.getKey().intValue();
            String coreQuery = "INSERT INTO course_core (course_id, core_code) VALUES (?, ?)";
            for (CoreCode coreCode : course.getCoreCodes()) {
                jdbcTemplate.update(coreQuery, courseId, coreCode.name());
            }
        }
    }
}
