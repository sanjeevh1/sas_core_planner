package org.example;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;

import java.util.List;

/**
 * A class containing the information for a Course.
 */
public class Course {

    @CsvBindByName(column = "Course ID")
    private String courseNumber;

    @CsvBindByName(column = "Course Title")
    private String title;

    @CsvBindByName
    private float credits;

    @CsvBindAndSplitByName(column = "Core Code", elementType = CoreCode.class, splitOn = ",")
    private List<CoreCode> coreCodes;

    @CsvBindByName
    private String subject;

    /**
     * Constructor for the Course class.
     * @param courseNumber the course number.
     * @param title the course title.
     * @param credits the number of credits for the course.
     * @param coreCodes a list of core codes satisfied by the course.
     * @param subject the subject of the course.
     */
    public Course(String courseNumber, String title, float credits, List<CoreCode> coreCodes, String subject) {
        this.courseNumber = courseNumber;
        this.title = title;
        this.credits = credits;
        this.coreCodes = coreCodes;
        this.subject = subject;
    }

    /**
     * Retrieves the course number.
     * @return the course number.
     */
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * Retrieves the course title.
     * @return the course title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves the credits for the course.
     * @return the number of credits for the course.
     */
    public float getCredits() {
        return credits;
    }

    /**
     * Retrieves the core codes for the course.
     * @return a list of core codes satisfied by the course.
     */
    public List<CoreCode> getCoreCodes() {
        return coreCodes;
    }

    /**
     * Retrieves the subject of the course.
     * @return the subject of the course.
     */
    public String getSubject() {
        return subject;
    }
}
