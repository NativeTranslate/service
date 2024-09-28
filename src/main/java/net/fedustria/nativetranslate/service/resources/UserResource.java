package net.fedustria.nativetranslate.service.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.model.settings.SettingDAO;
import net.fedustria.nativetranslate.service.model.user.UserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

/**
 * REST resource class for handling user-related operations.
 */
@Path("/api/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource extends APIResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserDAO userDAO;
    @Inject
    private SettingDAO settingDAO;

    /**
     * Retrieves a list of all users.
     *
     * @return a Response containing the list of users
     */
    @GET
    @Path("/")
    public Response getUsers() {
        return Response.ok().entity(userDAO.listAll()).build();
    }

    /**
     * Retrieves the current authenticated user.
     *
     * @param jwt the JWT token from the Authorization header
     * @return a Response containing the current authenticated user
     */
    @GET
    @Path("/me")
    public Response getMe(@HeaderParam("Authorization") final String jwt) {
        return ensureLoggedIn(jwt, (user) -> Response.ok().entity(user).build());
    }

    /**
     * Retrieves the settings for the current authenticated user.
     *
     * @param jwt the JWT token from the Authorization header
     * @return a Response containing the user's settings
     */
    @GET
    @Path("/me/settings")
    public Response getSettings(@HeaderParam("Authorization") final String jwt) {
        return ensureLoggedIn(jwt, (user) -> {
            try {
                return ok(settingDAO.findByUser(user)).build();
            } catch (final Exception e) {
                LOG.debug("Failed to execute function", e);
                return status(INTERNAL_SERVER_ERROR).entity(errorResponse(e.getMessage())).build();
            }
        });
    }

    /**
     * Updates the settings for the current authenticated user.
     *
     * @param jwt     the JWT token from the Authorization header
     * @param request the request containing the settings to update
     * @return a Response indicating the result of the update operation
     */
    @POST
    @Path("/me/settings")
    @Transactional
    public Response updateSettings(@HeaderParam("Authorization") final String jwt, final SettingSetRequest request) {
        return ensureLoggedIn(jwt, (user) -> {
            if (request == null) {
                return badRequest("Request body is required");
            }

            if (request.key() == null || request.key().isEmpty()) {
                return badRequest("Setting key is required");
            }

            if (request.value() == null || request.value().isEmpty()) {
                return badRequest("Setting value is required");
            }

            try {
                settingDAO.updateSetting(user, request.key(), request.value());
                return ok(messageResponse("Settings updated successfully")).build();
            } catch (final Exception e) {
                LOG.debug("Failed to execute function", e);
                return status(UNAUTHORIZED).entity(errorResponse(e.getMessage())).build();
            }
        });
    }

    /**
     * Updates the account settings for the current authenticated user.
     *
     * @param jwt      the JWT token from the Authorization header
     * @param settings the settings to update
     * @return a Response indicating the result of the update operation
     */
    @POST
    @Path("/me/account-settings")
    @Transactional
    public Response updateAccountSettings(@HeaderParam("Authorization") final String jwt, final Map<String, String> settings) {
        return ensureLoggedIn(jwt, (user) -> {
            if (settings == null || settings.isEmpty()) {
                return badRequest("Request body is required");
            }

            for (Map.Entry<String, String> entry : settings.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (key.equals("email") || key.equals("password") || key.equals("role") || key.equals("id")) {
                    return badRequest("Cannot update account settings");
                }

                userDAO.updateValue(user, key, value);
            }

            return ok(messageResponse("Account settings updated successfully")).build();
        });
    }

    /**
     * Resets the password for the current authenticated user.
     *
     * @param jwt                  the JWT token from the Authorization header
     * @param resetPasswordRequest the request containing the email of the user
     */
    @POST
    @Path("/me/reset-password")
    @Transactional
    public Response resetPassword(@HeaderParam("Authorization") final String jwt, final ResetPasswordRequest resetPasswordRequest) {
        return ensureLoggedIn(jwt, (user) -> {
            if (resetPasswordRequest == null) {
                return badRequest("Request body is required");
            }

            if (resetPasswordRequest.currentPassword() == null || resetPasswordRequest.currentPassword().isEmpty()) {
                return badRequest("Current password is required");
            }

            if (resetPasswordRequest.newPassword() == null || resetPasswordRequest.newPassword().isEmpty()) {
                return badRequest("New password is required");
            }

            if (!userDAO.checkPassword(user, resetPasswordRequest.currentPassword())) {
                return status(UNAUTHORIZED).entity(errorResponse("Invalid current password")).build();
            }

            userDAO.updatePassword(user, resetPasswordRequest.newPassword());
            return ok(messageResponse("Password reset successfully")).build();
        });
    }

    /**
     * Record class for project creation request.
     */
    public record SettingSetRequest(String key, String value) {
    }

    /**
     * Record class for password reset request.
     */
    public record ResetPasswordRequest(String currentPassword, String newPassword) {
    }
}