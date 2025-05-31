package org.example;

import jakarta.persistence.*;

/**
 * Entity class for CoreCodes
 */
@Entity
@Table(name = "core_code")
public class CoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "code", unique = true, nullable = false)
    private String code;

    /**
     * Retrieves the unique identifier of the core code entity.
     * @return the unique identifier of the core code entity.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the core code entity.
     * @param id the unique identifier to set for the core code entity.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the code of the core code entity.
     * @return the code of the core code entity.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the code of the core code entity.
     * @param code the code to set for the core code entity.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
