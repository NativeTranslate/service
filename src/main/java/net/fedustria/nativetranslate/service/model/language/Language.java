package net.fedustria.nativetranslate.service.model.language;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity class representing a Language.
 * This class is mapped to the "fds_languages" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_languages")
public class Language {

    /**
     * The unique identifier for the language.
     * This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the language.
     * This value must be unique.
     */
    @Column(unique = true)
    private String name;

    /**
     * The code of the language.
     * This value must be unique.
     */
    @Column(unique = true)
    private String code;
}