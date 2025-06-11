package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the Spring Boot application.
 */
@SpringBootApplication
public class CourseApplication {

    private static final String[] COURSE_FILES = {
            "src/main/resources/AHo.csv",
            "src/main/resources/AHp.csv",
            "src/main/resources/AHq.csv",
            "src/main/resources/AHr.csv",
            "src/main/resources/CCD.csv",
            "src/main/resources/CCO.csv",
            "src/main/resources/HST.csv",
            "src/main/resources/NS.csv",
            "src/main/resources/QQ.csv",
            "src/main/resources/QR.csv",
            "src/main/resources/SCL.csv",
            "src/main/resources/WC.csv",
            "src/main/resources/WCd.csv",
            "src/main/resources/WCr.csv"
    };

    /**
     * Main method to run the Spring Boot application.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(CourseApplication.class, COURSE_FILES);
    }
}