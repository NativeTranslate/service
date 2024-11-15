package net.fedustria.nativetranslate.service.resources.auth;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.provider.UserProvider;

import static jakarta.ws.rs.core.Response.ok;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {
    @POST
    @Path("/register")
    @Transactional
    public Response register(final RegisterRequest request) {
        UserProvider.createUser(request.username(), request.email(), request.password(), "user");
        return ok().build();
    }

    public record RegisterRequest(String email, String username, String password) {}
}
