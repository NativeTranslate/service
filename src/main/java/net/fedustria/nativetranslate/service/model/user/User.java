package net.fedustria.nativetranslate.service.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente) Created on: 9/12/2024 2:46 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * Entity class representing a User. This class is mapped to the "fds_users" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_users")
public class User {

    /**
     * The unique identifier for the user. This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The username of the user. This value must be unique.
     */
    @Column(unique = true)
    private String username;

    /**
     * The bio of the user. This value is stored as a large object (LOB) in the database.
     */
    @Lob
    @Column(columnDefinition = "TEXT DEFAULT 'Hello, I am using NativeTranslate!'")
    private String bio;

    /**
     * The email of the user. This value must be unique.
     */
    @Column(unique = true)
    private String email;

    /**
     * The password of the user. This value is ignored during JSON serialization.
     */
    @JsonIgnore
    private String password;

    /**
     * The role of the user.
     */
    private String role;

    /**
     * The avatar of the user
     */
    private String avatar;

    /**
     * The country of the user.
     */
    private String country;

    /**
     * The gender of the user.
     */
    private String gender;

    /**
     * The date of birth of the user.
     */
    private long dateOfBirth;

    /**
     * The phone number of the user.
     */
    private String phoneNumber;

    /**
     * The location of the user
     */
    private String location;

    /**
     * The count of translated words of the user.
     */
    @Column(name = "translated_words")
    private long translatedWords;
}