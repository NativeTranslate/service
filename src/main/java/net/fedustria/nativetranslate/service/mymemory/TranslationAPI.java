package net.fedustria.nativetranslate.service.mymemory;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/12/2024 2:46 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * Interface for the Translation API.
 * This interface defines the endpoints for interacting with the translation service.
 */
@Path("/")
@RegisterRestClient(configKey = "translation-api")
public interface TranslationAPI {

    /**
     * Retrieves a translation for the given text and language pair.
     *
     * @param text         the text to be translated
     * @param langPair     the language pair in the format "source|target"
     * @param apiKey       the API key for authentication
     * @param contactEmail the contact email for the request
     * @return the translation result as a String
     */
    @GET
    @Path("/get")
    String getTranslation(
            @QueryParam("q") String text, @QueryParam("langpair") String langPair, @QueryParam("key") String apiKey,
            @QueryParam("de") String contactEmail
    );

    /**
     * Record representing the response from the translation API.
     *
     * @param matches a list of translation matches
     */
    record TranslationResponse(List<TranslationMatch> matches) {
    }

    /**
     * Record representing a single translation match.
     *
     * @param translation the translated text
     * @param match       the match percentage
     * @param quality     the quality of the translation
     * @param createdBy   the creator of the translation
     */
    record TranslationMatch(
            String translation, double match, String quality, @JsonProperty("created-by") String createdBy
    ) {
    }
}