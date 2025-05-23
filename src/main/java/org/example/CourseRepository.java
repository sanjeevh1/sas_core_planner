package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * A repository class for handling database operations related to courses.
 */
@Repository
public class CourseRepository {

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
}
