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

    public Course(Long id, String courseNumber, String courseTitle, float credits, List<CoreCode> coreCodes, String subject) {
        this.id = id;
        this.courseNumber = courseNumber;
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.coreCodes = coreCodes;
        this.subject = subject;
    }
    public Course() {
        // Default constructor for JPA
        this.coreCodes = List.of();
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }
    public String getCourseTitle() {
        return courseTitle;
    }
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }
    public float getCredits() {
        return credits;
    }
    public void setCredits(float credits) {
        this.credits = credits;
    }
    public List<CoreCode> getCoreCodes() {
        return coreCodes;
    }
    public void setCoreCodes(List<CoreCode> coreCodes) {
        this.coreCodes = coreCodes;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

}
