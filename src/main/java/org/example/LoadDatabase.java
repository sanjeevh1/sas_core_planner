package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing the database with core goals and courses.
 */
@Configuration
public class LoadDatabase {

    /**
     * Initializes the database with core goals and courses.
     * @param repository the CourseRepository instance for database operations.
     * @return a CommandLineRunner that initializes the database.
     */
    @Bean
    CommandLineRunner initDatabase(CourseRepository repository) { return (args) -> {
        repository.initializeTables();
        repository.loadCoreGoals();
        for(CoreCode code : CoreCode.values()) {
            String filePath = "src/main/resources/" + code.name() + ".csv";
            repository.loadCourses(filePath);
        }
    };}

}
