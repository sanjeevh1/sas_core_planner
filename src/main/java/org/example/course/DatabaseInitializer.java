package org.example.course;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.List;

/**
 * Configuration class for initializing the database with core goals and getCourses.
 */
@Configuration
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Initializes the database with core goals and getCourses.
     * @return a CommandLineRunner that initializes the database.
     */
    @Bean
    public CommandLineRunner initDatabase() {
        return this;
    }

    /**
     * Runs the database initialization with the provided file paths.
     * @param args command line arguments (not used).
     * @throws Exception if an error occurs while reading the files.
     */
    @Override
    public void run(String... args) throws Exception {
        for (CoreCode coreCode : CoreCode.values()) {
            loadCourses("/" + coreCode.name() + ".csv");
        }
    }

    /**
     * Loads getCourses from a CSV file into the repository.
     * @param fileName the name of the CSV file containing course data.
     */
    private void loadCourses(String fileName) {
        InputStream stream = getClass().getResourceAsStream(fileName);
        if (stream == null) {
            logger.error("Resource not found: {}", fileName);
        } else {
            InputStreamReader reader = new InputStreamReader(stream);
            List<Course> courses = new CsvToBeanBuilder<Course>(reader)
                    .withType(Course.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            for (Course course : courses) {
                String courseNumber = course.getCourseNumber();
                if (!courseRepository.existsByCourseNumber(courseNumber)) {
                    courseRepository.save(course);
                }
            }
        }
    }

}
