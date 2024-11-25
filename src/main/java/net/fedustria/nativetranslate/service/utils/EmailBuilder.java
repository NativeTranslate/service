package net.fedustria.nativetranslate.service.utils;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import jakarta.inject.Inject;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente) Created on: 9/27/2024 5:05 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */
public class EmailBuilder {
    private static final Map<String, String> templateContents = new HashMap<>();
    @Getter
    private final String receiver;
    @Getter
    private final String fileName;
    @Getter
    private final String subject;
    @Getter
    private final Map<String, String> replacements;
    @Inject
    private final Mailer mailer;

    public EmailBuilder(final String receiver, final EMailTemplate template, final String subject, final Map<String, String> replacements, final Mailer mailer) {
        this.receiver = receiver;
        this.fileName = template.getFileName();
        this.subject = subject;
        this.replacements = replacements;
        this.mailer = mailer;
    }

    private String minifyHtml(final String html) {
        return html.replaceAll("\\s+", " ")
                .replaceAll("> <", "><");
    }

    private File getFile() {
        if (fileName == null) {
            throw new IllegalArgumentException("File name must not be null");
        }

        final var classLoader = getClass().getClassLoader();
        final var resource = classLoader.getResource("email-templates/" + fileName);

        if (resource == null) {
            throw new IllegalArgumentException("Template file not found in classpath: " + fileName);
        }

        final File file = new File(resource.getFile());

        if (!file.exists()) {
            throw new IllegalArgumentException("File does not exist: " + file.getAbsolutePath());
        }

        return file;
    }

    private String fileToString() throws IOException {
        if (!templateContents.containsKey(fileName)) {
            final File file = getFile();
            final String htmlContent = minifyHtml(Files.readString(file.toPath()));
            templateContents.put(fileName, htmlContent);
        }

        String htmlContent = templateContents.get(fileName);

        if (!replacements.isEmpty()) {
            for (final Map.Entry<String, String> entry : replacements.entrySet()) {
                htmlContent = htmlContent.replace("%" + entry.getKey() + "%", entry.getValue());
            }
        }

        return htmlContent;
    }

    public void send() throws IOException {
        final String htmlContent = fileToString();

        final Mail mail = Mail.withHtml(
                receiver,
                subject,
                htmlContent
        );

        mailer.send(mail);
    }

}
