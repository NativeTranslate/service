package net.fedustria.nativetranslate.service.model.invitecode;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * Data Access Object (DAO) for the InviteCode entity.
 * Provides methods to perform CRUD operations on InviteCode entities.
 */
@ApplicationScoped
public class InviteCodeDAO implements PanacheRepository<InviteCode> {

    /**
     * Finds an InviteCode entity by its code.
     *
     * @param code the code of the InviteCode entity
     * @return an Optional containing the found InviteCode entity, or an empty Optional if no entity is found
     */
    public Optional<InviteCode> findByCode(final String code) {
        return find("code", code).firstResultOptional();
    }
}