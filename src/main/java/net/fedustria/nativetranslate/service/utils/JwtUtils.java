package net.fedustria.nativetranslate.service.utils;

import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.json.JsonNumber;
import net.fedustria.nativetranslate.service.model.user.User;
import net.fedustria.nativetranslate.service.model.user.UserDAO;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;

/**
 * Utility class for handling JWT operations such as parsing, verification, and generation.
 */
@Singleton
public class JwtUtils {
    @ConfigProperty(name = "nativetranslate.jwt.secret")
    private String jwtSecret;

    @Inject
    private JWTParser jwtParser;

    @Inject
    private UserDAO userDAO;

    /**
     * Retrieves the user from the JWT token.
     *
     * @param authHeader the authorization header containing the JWT token
     * @return the user associated with the JWT token
     * @throws Exception if the JWT token is invalid or the user is not found
     */
    public User getUserFromJwt(final String authHeader) throws Exception {
        final var claims = jwtParser.verify(authHeader.substring(7), jwtSecret);
        final var userId = ((JsonNumber) claims.getClaim("id")).longValue();
        return userDAO.findByUserId(userId).orElseThrow();
    }

    /**
     * Checks if the user is logged in by verifying the JWT token.
     *
     * @param authHeader the authorization header containing the JWT token
     * @return true if the user is logged in, false otherwise
     */
    public boolean isLoggedIn(final String authHeader) {
        if (authHeader == null) {
            return false;
        }

        try {
            jwtParser.verify(authHeader.substring(7), jwtSecret);
            return true;
        } catch (final ParseException e) {
            return false;
        }
    }

    /**
     * Generates a JWT token for the specified user.
     *
     * @param user the user for whom the JWT token is to be generated
     * @return the generated JWT token
     */
    public String generateJwt(final User user) {
        return Jwt
                .upn(user.getUsername())
                .groups(new HashSet<>(Collections.singletonList(user.getRole())))
                .claim("id", user.getId())
                .expiresAt(Instant.now().plus(6, ChronoUnit.HOURS))
                .signWithSecret(jwtSecret);
    }
}