package net.fedustria.nativetranslate.service.resources.organisation;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.provider.OrganisationProvider;

@Path("/api/v1/organisations")
public class OrganisationResource {
    @POST
    @Path("/create")
    @Transactional
    public Response createOrganisation(final CreateOrganisationRequest request) {
        try {
            OrganisationProvider.createOrganisation(request.name(), request.description(), request.logo());
            return Response.ok().build();
        } catch (final Exception e) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getOrganisation(final String id) {
        try {
            return Response.ok(OrganisationProvider.getOrganisationById(Long.parseLong(id))).build();
        } catch (final NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Id isn't a number").build();
        } catch (final Exception e) {
            return Response.serverError().build();
        }
    }

    public record CreateOrganisationRequest(String name, String description, String logo) {}
}
