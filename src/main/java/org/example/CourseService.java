package org.example;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A service class that provides methods to search for courses based on core codes.
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Retrieves a list of getCourses based on the provided search criteria.
     * @param cores a list sets of core codes to search for.
     * @return a list of getCourses that match the search criteria, or null if the program fails to connect to the database.
     */
    public List<Course> getCourses(List<List<CoreCode>> cores) {
        QCourse course = QCourse.course;
        BooleanExpression orExpression = null;
        for(List<CoreCode> coreList : cores) {
            BooleanExpression andExpression = null;
            for(CoreCode code : coreList) {
                BooleanExpression expression = course.coreCodes.contains(code);
                if(andExpression == null) {
                    andExpression = expression;
                } else {
                    andExpression = andExpression.and(expression);
                }
            }
            if(orExpression == null) {
                orExpression = andExpression;
            } else {
                orExpression = orExpression.or(andExpression);
            }
        }
        if(orExpression == null) {
            return null; // No search criteria provided
        }
        return courseRepository.findAll(orExpression);
    }
}
