package org.example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    @GetMapping("/courses")
    public List<Course> courses(@RequestParam("tokens") List<String> tokens) {
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM course WHERE course.id in (");
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
        String query = queryBuilder.toString();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DatabaseInitializer.DB_URL, DatabaseInitializer.USER, DatabaseInitializer.PASSWORD);
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            List<Course> courses = new ArrayList<>();
            while (resultSet.next()) {
                String courseNumber = resultSet.getString("course_number");
                String title = resultSet.getString("course_title");
                float credits = resultSet.getFloat("credits");
                String[] coreCodes = resultSet.getString("core_codes").split(",");
                List<CoreCode> cores = new ArrayList<>();
                for (String coreCode: coreCodes) {
                    cores.add(CoreCode.valueOf(coreCode));
                }
                String subject = resultSet.getString("subject");
                Course course = new Course(courseNumber, title, credits, cores, subject);
                courses.add(course);
            }
            return courses;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
