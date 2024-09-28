package net.fedustria.nativetranslate.service.utils;

import lombok.Getter;

public enum EMailTemplate {
    PASSWORD_RESET("reset-password.html"),
    ;

    @Getter
    private final String fileName;

    EMailTemplate(final String fileName) {
        this.fileName = fileName;
    }
}
