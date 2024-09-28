package net.fedustria.nativetranslate.service.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.model.language.Language;
import net.fedustria.nativetranslate.service.model.language.LanguageDAO;
import net.fedustria.nativetranslate.service.model.project.Project;
import net.fedustria.nativetranslate.service.model.project.ProjectDAO;
import net.fedustria.nativetranslate.service.utils.ImageGenerator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;
import static java.util.function.Function.identity;

/**
 * REST resource class for handling project-related operations.
 */
@Path("/api/projects")
@Produces(MediaType.APPLICATION_JSON)
public class ProjectResource extends APIResource {
    @Inject
    private ProjectDAO projectDAO;
    @Inject
    private LanguageDAO languageDAO;

    /**
     * Retrieves a list of all projects.
     *
     * @return a list of all projects
     */
    @GET
    @Path("/")
    public List<Project> getProjects() {
        return projectDAO.listAll();
    }

    /**
     * Retrieves a project by its ID.
     *
     * @param id the ID of the project
     * @return the project with the specified ID
     */
    @GET
    @Path("/{id}")
    public Project getProject(@PathParam("id") final long id) {
        return projectDAO.findById(id);
    }

    /**
     * Creates a new project.
     *
     * @param jwt     the JWT token from the Authorization header
     * @param request the request containing project details
     * @return a Response indicating the result of the creation operation
     */
    @POST
    @Path("/create")
    @Transactional
    public Response createProject(@HeaderParam("Authorization") final String jwt, final ProjectCreateRequest request) {
        return ensureLoggedIn(jwt, (user) -> {
            if (request == null) {
                return badRequest("Request body is required");
            }

            if (request.name() == null || request.name().isEmpty()) {
                return badRequest("Project name is required");
            }

            if (request.identifier() == null || request.identifier().isEmpty()) {
                return badRequest("Project identifier is required");
            }

            if (request.description() == null || request.description().isEmpty()) {
                return badRequest("Project description is required");
            }

            if (request.sourceLanguage() <= 0) {
                return badRequest("Source language is required");
            }

            if (request.targetLanguages() == null || request.targetLanguages().isEmpty()) {
                return badRequest("Target languages are required");
            }

            final List<Language> languages = languageDAO.listAll();
            final Map<Long, Language> languageMap = languages.stream().collect(Collectors.toMap(Language::getId, identity()));

            if (!languageMap.containsKey(request.sourceLanguage())) {
                return badRequest("Invalid source language");
            }

            if (request.targetLanguages.stream().anyMatch(id -> !languageMap.containsKey(id))) {
                return badRequest("Invalid target language");
            }

            try {
                final Project project = new Project();
                project.setName(request.name());
                project.setIdentifier(request.identifier());
                project.setDescription(request.description());
                project.setPrivateProject(request.privateProject());
                project.setSourceLanguage(languageMap.get(request.sourceLanguage()));
                project.setTargetLanguages(request.targetLanguages().stream().map(languageMap::get).toList());
                project.setCreated(System.currentTimeMillis());
                projectDAO.persist(project);

                ImageGenerator.generateImage(getFileSystemPath() + "project/" + project.getId() + ".png", project.getName().charAt(0));

                return ok(messageResponse("Project created")).build();
            } catch (final Exception e) {
                return status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse(e.getMessage())).build();
            }
        });
    }

    /**
     * Record class for project creation request.
     */
    public record ProjectCreateRequest(
            String name, String identifier, String description, boolean privateProject, long sourceLanguage,
            List<Long> targetLanguages
    ) {
    }
}