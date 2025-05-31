package org.example;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * A class containing the information for a Course.
 */
@Entity
public class Course {

    private @Id
    @GeneratedValue Long id;

    @CsvBindByName(column = "Course ID")
    private String courseNumber;

    @CsvBindByName(column = "Course Title")
    private String courseTitle;

    @CsvBindByName(column = "Credits")
    private float credits;

    @CsvBindAndSplitByName(column = "Core Code", elementType = CoreCode.class, splitOn = ",")
    private List<CoreCode> coreCodes;

    @CsvBindByName(column = "Subject")
    private String subject;


    /**
     * Default constructor for the Course class.
     */
    public Course() {
        // Default constructor
    }
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
        this.courseTitle = title;
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
    public String getCourseTitle() {
        return courseTitle;
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

    /**
     * Sets the course number.
     * @param courseNumber the course number to set.
     */
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * Sets the course title.
     * @param courseTitle the course title to set.
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * Sets the credits for the course.
     * @param credits the number of credits to set.
     */
    public void setCredits(float credits) {
        this.credits = credits;
    }
    /**
     * Sets the core codes for the course.
     * @param coreString a comma-separated string of core codes.
     */
    public void setCoreCodes(String coreString) {
        String[] coreCodesArray = coreString.split(",");
        this.coreCodes = new ArrayList<>();
        for (String code : coreCodesArray) {
            this.coreCodes.add(CoreCode.valueOf(code));
        }
    }

    /**
     * Sets the subject of the course.
     * @param subject the subject to set.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Retrieves the unique identifier for the course.
     * @return the unique identifier for the course.
     */
    public Long getId() {
        return id;
    }

    /**
     * Checks if two Course objects are equal.
     * @param o the object to compare with this Course.
     * @return true o is a Course and all fields are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course course)) return false;

        if (Float.compare(course.credits, credits) != 0) return false;
        if (!courseNumber.equals(course.courseNumber)) return false;
        if (!courseTitle.equals(course.courseTitle)) return false;
        if (!coreCodes.equals(course.coreCodes)) return false;
        return subject.equals(course.subject);
    }
}
