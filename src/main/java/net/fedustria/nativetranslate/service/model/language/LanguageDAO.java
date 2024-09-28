package net.fedustria.nativetranslate.service.model.language;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Data Access Object (DAO) for the Language entity.
 * Provides methods to perform CRUD operations on Language entities.
 */
@ApplicationScoped
public class LanguageDAO implements PanacheRepository<Language> {

    /**
     * Finds a Language entity by its unique identifier.
     *
     * @param id the unique identifier of the Language entity
     * @return an Optional containing the found Language entity, or an empty Optional if no entity is found
     */
    public Optional<Language> findByID(final long id) {
        return find("id", id).firstResultOptional();
    }
}