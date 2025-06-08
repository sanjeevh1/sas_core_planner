package org.example;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CourseRepository courseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves a list of courses based on the provided search tokens.
     * @param tokens a list of core codes and boolean operators (AND, OR).
     * @return a list of courses that match the search criteria, or null if the program fails to connect to the database.
     */
    public List<Course> getCourses(List<List<CoreCode>> tokens) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCourse course = QCourse.course;
        BooleanExpression orExpression = null;
        for(List<CoreCode> tokenList : tokens) {
            BooleanExpression andExpression = null;
            for(CoreCode token : tokenList) {
                if(andExpression == null) {
                    andExpression = course.coreCodes.contains(token);
                } else {
                    andExpression = andExpression.and(course.coreCodes.contains(token));
                }
            }
            if(orExpression == null) {
                orExpression = andExpression;
            } else {
                orExpression = orExpression.or(andExpression);
            }
        }
        return queryFactory.selectFrom(course)
                .where(orExpression)
                .fetch();
    }

    /**
     * Generates a SQL query based on the provided tokens.
     * @return a SQL query string to obtain courses based on the given tokens.
     */
    private static String getQuery(List<String> tokens) {
        StringBuilder queryBuilder = new StringBuilder("SELECT DISTINCT course_id FROM course_core WHERE course_id IN (");
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
