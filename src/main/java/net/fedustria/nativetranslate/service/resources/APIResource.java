package net.fedustria.nativetranslate.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import lombok.Getter;
import net.fedustria.nativetranslate.service.model.user.User;
import net.fedustria.nativetranslate.service.model.user.UserDAO;
import net.fedustria.nativetranslate.service.utils.JwtUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import static jakarta.ws.rs.core.Response.Status.BAD_REQUEST;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static jakarta.ws.rs.core.Response.status;

/**
 * Base class for API resources providing common functionality.
 */
public class APIResource {
    private static final Logger LOG = LoggerFactory.getLogger(APIResource.class);

    @Inject
    protected JwtUtils jwtUtils;
    @Inject
    private UserDAO userDAO;
    @ConfigProperty(name = "nativetranslate.public-path")
    @Getter
    private String fileSystemPath;

    /**
     * Ensures the user is logged in.
     *
     * @param authHeader the authorization header
     * @param func       the function to run if the user is logged in
     * @return the response
     */
    public Response ensureLoggedIn(final String authHeader, final Function<User, Response> func) {
        if (jwtUtils.isLoggedIn(authHeader)) {
            try {
                final User user = jwtUtils.getUserFromJwt(authHeader);
                return func.apply(user);
            } catch (final Exception e) {
                LOG.debug("Failed to execute function", e);
                return status(UNAUTHORIZED).entity(errorResponse(e.getMessage())).build();
            }
        }

        return status(UNAUTHORIZED).entity(errorResponse("You must be logged in to perform this action")).build();
    }

    /**
     * Creates a bad request response.
     *
     * @param message the message
     * @return the response
     */
    public Response badRequest(final String message) {
        return status(BAD_REQUEST).entity(errorResponse(message)).build();
    }

    /**
     * Creates a message response.
     *
     * @param message the message
     * @return the message response
     */
    public MessageResponse messageResponse(final String message) {
        return new MessageResponse(message);
    }

    /**
     * Creates an error response.
     *
     * @param error the error message
     * @return the error response
     */
    public ErrorResponse errorResponse(final String error) {
        return new ErrorResponse(error);
    }

    /**
     * Saves a file.
     *
     * @param name  the name of the file
     * @param bytes the bytes of the file
     * @throws IOException if an error occurs
     */
    public void saveFile(final String name, final byte[] bytes) throws IOException {
        Files.write(Path.of(fileSystemPath, name), bytes);
    }

    /**
     * Record class for error response.
     */
    public record ErrorResponse(String error) {
    }

    /**
     * Record class for message response.
     */
    public record MessageResponse(String message) {
    }
}