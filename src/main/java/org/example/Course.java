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
}
