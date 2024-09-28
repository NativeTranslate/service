package net.fedustria.nativetranslate.service.model.project;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Data Access Object (DAO) for the Project entity.
 * Provides methods to perform CRUD operations on Project entities.
 */
@ApplicationScoped
public class ProjectDAO implements PanacheRepository<Project> {
}