package org.example.course;

import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
     * @param filePaths the paths to the CSV files containing course data.
     * @throws Exception if an error occurs while reading the files.
     */
    public void run(String... filePaths) throws Exception {
        for (String filePath : filePaths) {
            loadCourses(filePath);
        }
    }

    /**
     * Loads getCourses from a CSV file into the repository.
     * @param filePath the path to the CSV file containing course data.
     */
    private void loadCourses(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            List<Course> courses = new CsvToBeanBuilder<Course>(reader)
                    .withType(Course.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();
            courseRepository.saveAll(courses);
        } catch (FileNotFoundException e) {
            logger.error("File not found: {}", filePath, e);
        } catch (IOException e) {
            logger.error("Error reading file: {}", filePath, e);
        }
    }

}
