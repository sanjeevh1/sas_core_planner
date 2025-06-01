package org.example;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;

import java.util.List;

/**
 * A class containing the information for a Course.
 */
@Entity
public class Course {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CsvBindByName(column = "Course ID")
    private String courseNumber;

    @CsvBindByName(column = "Course Title")
    private String courseTitle;

    @CsvBindByName(column = "Credits")
    private float credits;

    @ElementCollection(targetClass = CoreCode.class)
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
     * @param coreCodes a list of Core Codes.
     */
    public void setCoreCodes(List<CoreCode> coreCodes) {
        this.coreCodes = coreCodes;
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

        return id.equals(course.id);
    }
}
