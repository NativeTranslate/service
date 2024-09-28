package net.fedustria.nativetranslate.service.model.passwordreset;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/27/2024 6:20 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@ApplicationScoped
public class PasswordResetDAO implements PanacheRepository<PasswordReset> {

    /**
     * Finds a password reset entity by the given token.
     *
     * @param token the token to search for
     * @return the password reset entity if found, null otherwise
     */
    public PasswordReset findByToken(String token) {
        return find("token", token).firstResult();
    }

    /**
     * Finds a password reset entity by the given email.
     *
     * @param email the email to search for
     * @return the password reset entity if found, null otherwise
     */
    public PasswordReset findByEmail(String email) {
        return find("email", email).firstResult();
    }

    /**
     * Deletes a password reset entity by the given email.
     *
     * @param email the email to search for
     */
    public void deleteByEmail(String email) {
        delete("email", email);
    }

    /**
     * Generates a new password reset token.
     * The token is a random 6-character alphanumeric string.
     *
     * @return the generated toke
     */
    public String generateToken() {
        String generatedToken;

        generatedToken = RandomStringUtils.randomAlphanumeric(6);

        return generatedToken;
    }

    /**
     * Deletes password reset entries that have expired.
     *
     * @param now the current date and time
     */
    public void deleteExpiredTokens(Date now) {
        delete("expirationDate < ?1", now);
    }

}
