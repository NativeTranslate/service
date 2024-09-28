package net.fedustria.nativetranslate.service.model.organization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.fedustria.nativetranslate.service.model.user.User;

import java.util.List;

/**
 * Represents an organization entity.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_organizations")
public class Organization {
    /**
     * The unique identifier for the organization.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The name of the organization.
     */
    @Column(unique = true)
    private String name;

    /**
     * The description of the organization.
     */
    private String description;

    /**
     * The list of members in the organization.
     */
    @ManyToMany
    @JoinTable(
            name = "xorganizationmembers",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"})
    )
    private List<User> members;

    /**
     * The list of managers in the organization.
     */
    @ManyToMany
    @JoinTable(
            name = "xorganizationmanagers",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"})
    )
    private List<User> managers;

    /**
     * The list of translators in the organization.
     */
    @ManyToMany
    @JoinTable(
            name = "xorganizationtranslators",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "user_id"})
    )
    private List<User> translators;

    /**
     * The timestamp when the organization was created.
     */
    private long created;
}