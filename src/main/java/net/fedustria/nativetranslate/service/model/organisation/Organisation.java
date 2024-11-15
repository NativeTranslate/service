package net.fedustria.nativetranslate.service.model.organisation;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "organisations")
@Getter
public class Organisation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    private String logo;

    private Organisation() {}

    public static Organisation create(final String name, final String description, final String logo) {
        final Organisation organisation = new Organisation();
        organisation.name = name;
        organisation.description = description;
        organisation.logo = logo;
        return organisation;
    }
}
