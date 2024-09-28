package net.fedustria.nativetranslate.service.model.organization;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Data Access Object (DAO) for the Organization entity.
 * Provides methods to perform CRUD operations on Organization entities.
 */
@ApplicationScoped
public class OrganizationDAO implements PanacheRepository<Organization> {
}