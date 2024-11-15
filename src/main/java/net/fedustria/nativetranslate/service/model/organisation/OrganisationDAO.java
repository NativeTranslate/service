package net.fedustria.nativetranslate.service.model.organisation;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

@ApplicationScoped
public class OrganisationDAO implements PanacheRepository<Organisation> {
    public Optional<Organisation> findByName(final String name) {
        return find("name", name).firstResultOptional();
    }

    public Optional<Organisation> findById(final long id) {
        return findByIdOptional(id);
    }
}
