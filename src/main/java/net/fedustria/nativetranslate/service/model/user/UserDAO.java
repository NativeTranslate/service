package net.fedustria.nativetranslate.service.model.user;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserDAO implements PanacheRepository<User> {
    public User findByEmail(final String email) {
        return find("email", email).firstResult();
    }
}
