package net.fedustria.nativetranslate.service.model.passwordreset;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/27/2024 6:18 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Represents an password reset entity.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_password_resets")
public class PasswordReset {

    /**
     * The unique identifier for the password reset.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The user's email.
     */
    private String email;

    /**
     * The reset token.
     */
    private String token;

    /**
     * The expiration date of the token.
     */
    private Date expirationDate;

}
