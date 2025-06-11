package org.example;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import jakarta.transaction.Transactional;
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
     * @return a list of getCourses that match the search criteria.
     */
    @Transactional
    public List<Course> getCourses(List<List<CoreCode>> cores) {
        BooleanExpression expression = getExpression(cores);
        return courseRepository.findAll(expression);
    }

    /**
     * Constructs a BooleanExpression that combines multiple CoreCode lists with OR logic.
     * @param cores a list of lists of CoreCode objects to combine
     * @return a BooleanExpression that represents the OR condition for the provided CoreCode lists
     */
    private BooleanExpression getExpression(List<List<CoreCode>> cores) {
        BooleanExpression orExpression = Expressions.FALSE;
        for(List<CoreCode> coreList : cores) {
            BooleanExpression andExpression = getAndExpression(coreList);
            if(orExpression == null) {
                orExpression = andExpression;
            } else {
                orExpression = orExpression.or(andExpression);
            }
        }
        return orExpression;
    }

    /**
     * Constructs a BooleanExpression that combines multiple CoreCode conditions with AND logic.
     * @param coreList a list of CoreCode objects to combine
     * @return a BooleanExpression that represents the AND condition for the provided CoreCode list
     */
    private BooleanExpression getAndExpression(List<CoreCode> coreList) {
        BooleanExpression andExpression = Expressions.TRUE;
        for(CoreCode code : coreList) {
            BooleanExpression expression = QCourse.course.coreCodes.contains(code);
            andExpression = andExpression.and(expression);
        }
        return andExpression;
    }
}
