package org.example;

import jakarta.persistence.*;

import java.util.List;

/**
 * Represents a user in the system.
 */
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private List<Course> courses;
}
