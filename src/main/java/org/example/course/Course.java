package org.example.course;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.List;

/**
 * A class containing the information for a Course.
 */
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "Course ID")
    private String courseNumber;

    @CsvBindByName(column = "Course Title")
    private String courseTitle;

    @CsvBindByName(column = "Credits")
    private float credits;

    @ElementCollection(targetClass = CoreCode.class, fetch = FetchType.EAGER)
    @CsvBindAndSplitByName(column = "Core Code", elementType = CoreCode.class, splitOn = ",")
    @JoinTable(
            name = "course_core",
            joinColumns = @JoinColumn(name = "course_id")
    )
    @Column(name = "core_code")
    @Enumerated(EnumType.STRING)
    private List<CoreCode> coreCodes;

    @CsvBindByName(column = "Subject")
    private String subject;

    /**
     * Constructs a Course with the specified parameters.
     *
     * @param id           the unique identifier for the course
     * @param courseNumber the course number
     * @param courseTitle  the title of the course
     * @param credits      the number of credits for the course
     * @param coreCodes    the list of core codes associated with the course
     * @param subject      the subject area of the course
     */
    public Course(Long id, String courseNumber, String courseTitle, float credits, List<CoreCode> coreCodes, String subject) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.coreCodes = coreCodes;
        this.subject = subject;
    }

    /**
     * Default constructor for the Course class.
     * This constructor is used by JPA to create instances of the Course entity.
     */
    public Course() {
        // Default constructor for JPA
        this.coreCodes = List.of();
    }

    /**
     * Gets the unique identifier for the course.
     *
     * @return the unique identifier for the course
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the course.
     *
     * @param id the unique identifier to set for the course
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the course number.
     *
     * @return the course number
     */
    public String getCourseNumber() {
        return courseNumber;
    }

    /**
     * Sets the course number.
     *
     * @param courseNumber the course number to set
     */
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    /**
     * Gets the title of the course.
     *
     * @return the title of the course
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * Sets the title of the course.
     *
     * @param courseTitle the title to set for the course
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * Gets the number of credits for the course.
     *
     * @return the number of credits
     */
    public float getCredits() {
        return credits;
    }

    /**
     * Sets the number of credits for the course.
     *
     * @param credits the number of credits to set for the course
     */
    public void setCredits(float credits) {
        this.credits = credits;
    }

    /**
     * Gets the list of core codes associated with the course.
     *
     * @return the list of core codes
     */
    public List<CoreCode> getCoreCodes() {
        return coreCodes;
    }

    /**
     * Sets the list of core codes associated with the course.
     *
     * @param coreCodes the list of core codes to set for the course
     */
    public void setCoreCodes(List<CoreCode> coreCodes) {
        this.coreCodes = coreCodes;
    }

    /**
     * Gets the subject area of the course.
     *
     * @return the subject area
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject area of the course.
     *
     * @param subject the subject area to set for the course
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
