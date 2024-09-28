package net.fedustria.nativetranslate.service.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.model.organization.OrganizationDAO;
import net.fedustria.nativetranslate.service.model.project.ProjectDAO;
import net.fedustria.nativetranslate.service.model.user.UserDAO;

import static jakarta.ws.rs.core.Response.ok;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/25/2024 11:35 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * REST resource class for handling statistics-related operations.
 */
@Path("/api/stats")
public class StatsResource extends APIResource {
    @Inject
    private UserDAO userDAO;
    @Inject
    private OrganizationDAO organizationDAO;
    @Inject
    private ProjectDAO projectDAO;

    /**
     * Retrieves statistics about users, organizations, projects, and translations.
     *
     * @return a Response containing the statistics
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStats() {
        final var stats = new Stats(userDAO.listAll().size(), organizationDAO.listAll().size(), projectDAO.listAll().size(), 0);
        return ok(stats).build();
    }

    /**
     * Record class for statistics.
     */
    public record Stats(int users, int organizations, int projects, int translations) {
    }

}