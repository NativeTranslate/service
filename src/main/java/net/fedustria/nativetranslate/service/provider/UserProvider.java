package net.fedustria.nativetranslate.service.provider;

import jakarta.enterprise.context.ApplicationScoped;
import net.fedustria.nativetranslate.service.model.user.User;
import net.fedustria.nativetranslate.service.model.user.UserDAO;

import static io.quarkus.elytron.security.common.BcryptUtil.bcryptHash;

@ApplicationScoped
public class UserProvider {
    private static final UserDAO userDAO = new UserDAO();

    public static void createUser(final String username, final String email, final String password, final String role) {
        userDAO.persist(User.create(username, email, bcryptHash(password), role));
    }
}
