package net.fedustria.nativetranslate.service.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.model.organization.Organization;
import net.fedustria.nativetranslate.service.model.organization.OrganizationDAO;
import net.fedustria.nativetranslate.service.utils.ImageGenerator;
import org.jboss.resteasy.reactive.PartType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
import static jakarta.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/25/2024 11:35 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * REST resource class for handling organization-related operations.
 */
@Path("/api/organizations")
@Produces(MediaType.APPLICATION_JSON)
public class OrganizationResource extends APIResource {
    private static final Logger LOG = LoggerFactory.getLogger(OrganizationResource.class);

    @Inject
    private OrganizationDAO organizationDAO;

    /**
     * Retrieves a list of all organizations.
     *
     * @return a Response containing the list of organizations
     */
    @GET
    @Path("/")
    public Response getOrganizations() {
        return ok(organizationDAO.listAll()).build();
    }

    /**
     * Creates a new organization.
     *
     * @param authHeader the authorization header containing the JWT token
     * @param request    the request containing organization details
     * @return a Response indicating the result of the creation operation
     */
    @POST
    @Path("/create")
    @Transactional
    public Response createOrganization(@HeaderParam("Authorization") final String authHeader, final OrganizationCreateRequest request) {
        return ensureLoggedIn(authHeader, (user) -> {
            if (request == null) {
                return status(Response.Status.BAD_REQUEST).entity("Request body is required").build();
            }

            if (request.name() == null || request.name().isEmpty()) {
                return status(Response.Status.BAD_REQUEST).entity("Organization name is required").build();
            }

            if (request.description() == null || request.description().isEmpty()) {
                return status(Response.Status.BAD_REQUEST).entity("Organization description is required").build();
            }

            try {
                final Organization organization = new Organization();
                organization.setName(request.name());
                organization.setDescription(request.description());
                organization.setMembers(List.of(user));
                organizationDAO.persist(organization);

                ImageGenerator.generateImage(getFileSystemPath() + "/organizations/" + organization.getId() + ".png", organization.getName().charAt(0));

                return ok(organization).build();
            } catch (final Exception e) {
                return status(INTERNAL_SERVER_ERROR).entity(errorResponse(e.getMessage())).build();
            }
        });
    }

    /**
     * Updates the image of an organization.
     *
     * @param id   the ID of the organization
     * @param file the image file to be uploaded
     * @return a Response indicating the result of the update operation
     */
    @POST
    @Path("/{id}/image")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateImage(@PathParam("id") final int id, @FormParam("image") @PartType(APPLICATION_OCTET_STREAM) final File file) {
        try {
            final byte[] bytes = Files.readAllBytes(file.toPath());
            saveFile("organizations/" + id + ".png", bytes);
        } catch (final IOException e) {
            LOG.error("Failed to read file", e);
            return status(INTERNAL_SERVER_ERROR).entity("Failed to read file").build();
        }
        return ok().build();
    }

    /**
     * Record class for organization creation request.
     */
    public record OrganizationCreateRequest(String name, String description) {
    }
}