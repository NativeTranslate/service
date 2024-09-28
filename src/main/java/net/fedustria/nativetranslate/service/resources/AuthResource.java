package net.fedustria.nativetranslate.service.resources;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.mailer.Mailer;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.model.invitecode.InviteCodeDAO;
import net.fedustria.nativetranslate.service.model.passwordreset.PasswordReset;
import net.fedustria.nativetranslate.service.model.passwordreset.PasswordResetDAO;
import net.fedustria.nativetranslate.service.model.user.User;
import net.fedustria.nativetranslate.service.model.user.UserDAO;
import net.fedustria.nativetranslate.service.utils.EMailTemplate;
import net.fedustria.nativetranslate.service.utils.EmailBuilder;
import net.fedustria.nativetranslate.service.utils.ImageGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;

import static io.quarkus.elytron.security.common.BcryptUtil.bcryptHash;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

/**
 * REST resource class for handling authentication-related operations.
 */
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource extends APIResource {
    private static final Logger LOG = LoggerFactory.getLogger(AuthResource.class);

    @Inject
    private UserDAO          userDAO;
    @Inject
    private InviteCodeDAO    inviteCodeDAO;
    @Inject
    private PasswordResetDAO passwordResetDAO;
    @Inject
    private Mailer           mailer;

    /**
     * Handles user login.
     *
     * @param jwt          the JWT token from the Authorization header
     * @param loginRequest the login request containing email and password
     * @return a Response indicating the result of the login operation
     */
    @POST
    @Path("/login")
    public Response login(@HeaderParam("Authorization") final String jwt, final LoginRequest loginRequest) {
        if (jwtUtils.isLoggedIn(jwt)) {
            return ok(messageResponse("Already logged in")).build();
        }

        final var optUser = userDAO.findByEmail(loginRequest.email());
        if (optUser.isEmpty() || !BcryptUtil.matches(loginRequest.password(), optUser.get().getPassword())) {
            return status(NOT_FOUND).entity(errorResponse("Invalid credentials")).build();
        }

        try {
            return ok(new TokenResponse(jwtUtils.generateJwt(optUser.get()))).build();
        } catch (final Exception e) {
            LOG.debug("Error while logging in", e);
            return status(NOT_FOUND).entity(errorResponse("Error while logging in")).build();
        }
    }

    /**
     * Handles user registration.
     *
     * @param registerRequest the registration request containing email, username, password, and invite code
     * @return a Response indicating the result of the registration operation
     */
    @POST
    @Path("/register")
    @Transactional
    public Response register(final RegisterRequest registerRequest) {
        if (inviteCodeDAO.findByCode(registerRequest.inviteCode()).isEmpty()) {
            return status(NOT_FOUND).entity(errorResponse("Invite code not found")).build();
        }

        final Date date = new Date();

        try {
            final var user = new User();
            user.setEmail(registerRequest.email());
            user.setUsername(registerRequest.username());
            user.setPassword(bcryptHash(registerRequest.password()));
            user.setRole("user");
            userDAO.persist(user);

            ImageGenerator.generateImage(getFileSystemPath() + "users/" + user.getId() + ".png", user.getUsername().charAt(0));

            return ok(new TokenResponse(jwtUtils.generateJwt(user))).build();
        } catch (final Exception e) {
            LOG.debug("Error while registering", e);
            return status(NOT_FOUND).entity(errorResponse("Error while registering")).build();
        }
    }

    /**
     * Handles user logout.
     *
     * @param jwt the JWT token from the Authorization header
     * @return a Response indicating the result of the logout operation
     */
    @POST
    @Path("/logout")
    public Response logout(@HeaderParam("Authorization") final String jwt) {
        if (!jwtUtils.isLoggedIn(jwt)) {
            return status(NOT_FOUND).entity(errorResponse("Not logged in")).build();
        }

        return ok("Logged out").build();
    }

    /**
     * Validates if the user is logged in.
     *
     * @param jwt the JWT token from the Authorization header
     * @return a Response indicating the result of the validation
     */
    @POST
    @Path("/validate")
    public Response validate(@HeaderParam("Authorization") final String jwt) {
        if (!jwtUtils.isLoggedIn(jwt)) {
            return status(NOT_FOUND).entity(errorResponse("Not logged in")).build();
        }

        return ok(messageResponse("Logged in")).build();
    }

    @POST
    @Path("/reset-password")
    @Transactional
    public Response resetPassword(final ResetPasswordRequest resetPasswordRequest) {
        final var optUser = userDAO.findByEmail(resetPasswordRequest.email());

        if (optUser.isEmpty()) {
            return status(NOT_FOUND).entity(errorResponse("User not found")).build();
        }

        final String randomGeneratedCode = passwordResetDAO.generateToken();

        passwordResetDAO.deleteByEmail(resetPasswordRequest.email());

        final PasswordReset passwordReset = new PasswordReset();
        passwordReset.setEmail(resetPasswordRequest.email());
        passwordReset.setToken(randomGeneratedCode);
        passwordReset.setExpirationDate(new Date(System.currentTimeMillis() + 3600000));
        passwordResetDAO.persist(passwordReset);

        final Map<String, String> replacements = Map.of(
                "code", randomGeneratedCode,
                "url", "http://localhost:5173/reset-password"
        );

        try {
            new EmailBuilder(
                    resetPasswordRequest.email(),
                    EMailTemplate.PASSWORD_RESET,
                    "Password Reset on NativeTranslate",
                    replacements,
                    mailer
            ).send();

            return ok(messageResponse("Email sent")).build();
        } catch (final Exception e) {
            LOG.debug("Error while sending email", e);
            return status(INTERNAL_SERVER_ERROR).entity(errorResponse("Error while sending email")).build();
        }
    }

    @POST
    @Path("/reset-password-confirm")
    @Transactional
    public Response resetPasswordConfirm(final ResetPasswordConfirmRequest resetPasswordConfirmRequest) {
        final var passwordReset = passwordResetDAO.findByToken(resetPasswordConfirmRequest.token());

        if (passwordReset == null || passwordReset.getExpirationDate().before(new Date())) {
            return status(NOT_FOUND).entity(errorResponse("Invalid or expired token")).build();
        }

        final var user = userDAO.findByEmail(passwordReset.getEmail()).get();
        user.setPassword(bcryptHash(resetPasswordConfirmRequest.password()));
        userDAO.persist(user);

        passwordResetDAO.deleteByEmail(passwordReset.getEmail());

        return ok(messageResponse("Password reset successful")).build();
    }

    /**
     * Record class for token response.
     */
    public record TokenResponse(String token) {
    }

    /**
     * Record class for login request.
     */
    public record LoginRequest(String email, String password) {
    }

    /**
     * Record class for registration request.
     */
    public record RegisterRequest(String email, String username, String password, String inviteCode) {
    }

    /**
     * Record class for password reset request.
     */
    public record ResetPasswordRequest(String email) {
    }

    /**
     * Record class for password reset confirmation request.
     */
    public record ResetPasswordConfirmRequest(String token, String password) {

    }
}