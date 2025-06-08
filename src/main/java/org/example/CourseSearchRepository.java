package org.example;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A repository class for handling database operations related to courses.
 */
@Repository
public class CourseSearchRepository {

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
}
