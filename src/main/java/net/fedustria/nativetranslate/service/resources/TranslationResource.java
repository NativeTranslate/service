package net.fedustria.nativetranslate.service.resources;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import net.fedustria.nativetranslate.service.mymemory.TranslationAPI;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import static jakarta.ws.rs.core.Response.ok;

/**
 * REST resource class for handling translation-related operations.
 */
@Path("/api/translation")
@Produces(MediaType.APPLICATION_JSON)
public class TranslationResource extends APIResource {
    @RestClient
    private TranslationAPI translationAPI;

    @ConfigProperty(name = "mymemory.apikey")
    private String apiKey;
    @ConfigProperty(name = "mymemory.contact-email")
    private String contactEmail;

    /**
     * Translates the given text from the source language to the target language.
     *
     * @param request the translation request containing source text, source language, and target language
     * @return a Response containing the translation result
     */
    @POST
    @Path("/")
    public Response translate(final TranslationRequest request) {
        final String sourceTranslation = request.sourceTranslation();
        final String sourceLanguage = request.sourceLanguage();
        final String targetLanguage = request.targetLanguage();

        if (sourceLanguage == null) {
            return badRequest("sourceLanguage is required");
        }

        if (targetLanguage == null) {
            return badRequest("targetLanguage is required");
        }

        if (sourceTranslation == null) {
            return badRequest("sourceTranslation is required");
        }

        return ok(translationAPI.getTranslation(
                        sourceTranslation,
                        combine(sourceLanguage, targetLanguage),
                        apiKey,
                        contactEmail
                )
        ).build();
    }

    /**
     * Combines the source and target languages into a single string.
     *
     * @param sourceLanguage the source language
     * @param targetLanguage the target language
     * @return a combined string of source and target languages
     */
    private String combine(final String sourceLanguage, final String targetLanguage) {
        return sourceLanguage + "|" + targetLanguage;
    }

    /**
     * Record class for translation request.
     */
    public record TranslationRequest(String sourceTranslation, String sourceLanguage, String targetLanguage) {
    }
}