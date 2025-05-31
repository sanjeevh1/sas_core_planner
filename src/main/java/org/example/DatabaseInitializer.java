package org.example;

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
 * Configuration class for initializing the database with core goals and courses.
 */
@Configuration
public class DatabaseInitializer {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);
    private CourseSearchRepository repository;
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Initializes the database with core goals and courses.
     * @param repository the CourseRepository instance for database operations.
     * @return a CommandLineRunner that initializes the database.
     */
    @Bean
    public CommandLineRunner initDatabase(CourseSearchRepository repository) { return (filePaths) -> {
        this.repository = repository;
        repository.initializeTables();
        repository.loadCoreGoals();
        for(String path : filePaths) {
            loadCourses(path);
        }
    };}

    /**
     * Loads courses from a CSV file into the repository.
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
