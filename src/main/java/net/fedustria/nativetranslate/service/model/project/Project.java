package net.fedustria.nativetranslate.service.model.project;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.fedustria.nativetranslate.service.model.language.Language;
import net.fedustria.nativetranslate.service.model.user.User;

import java.util.List;

/**
 * Entity class representing a Project. This class is mapped to the "fds_projects" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_projects")
public class Project {

    /**
     * The unique identifier for the project. This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the project.
     */
    private String name;

    /**
     * The unique identifier for the project.
     */
    @Column(unique = true)
    private String identifier;

    /**
     * The description of the project. This value is stored as a large object (LOB) in the database.
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    /**
     * Indicates whether the project is private.
     */
    @Column(name = "private")
    private boolean privateProject;

    /**
     * The source language of the project. This is a many-to-one relationship with the Language entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_language_id")
    private Language sourceLanguage;

    /**
     * The target languages of the project. This is a many-to-many relationship with the Language entity.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "xprojectlanguage",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "language_id"})
    )
    private List<Language> targetLanguages;

    /**
     * The managers of the project. This is a many-to-many relationship with the User entity.
     */
    @ManyToMany
    @JoinTable(
            name = "xprojectmanager",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"})
    )
    private List<User> managers;

    /**
     * The members of the project. This is a many-to-many relationship with the User entity.
     */
    @ManyToMany
    @JoinTable(
            name = "xprojectmembers",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "user_id"})
    )
    private List<User> members;

    /**
     * The timestamp when the project was created.
     */
    private long created;

    /**
     * The timestamp of the last activity in the project.
     */
    @Column(name = "last_activity")
    private long lastActivity;
}