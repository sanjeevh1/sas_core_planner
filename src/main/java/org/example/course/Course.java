package org.example.course;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A class containing the information for a Course.
 */
@Entity
@Getter
@Setter
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

}
