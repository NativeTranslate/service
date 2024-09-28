package net.fedustria.nativetranslate.service.model.passwordreset;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Date;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/27/2024 6:42 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@ApplicationScoped
public class PasswordResetSchedularService {

    @Inject
    PasswordResetDAO passwordResetDAO;

    /**
     * Scheduled method to clean up expired password reset tokens.
     */
    @Scheduled(every = "1h")
    @Transactional
    public void cleanupExpiredTokens() {
        passwordResetDAO.deleteExpiredTokens(new Date());
    }

}
