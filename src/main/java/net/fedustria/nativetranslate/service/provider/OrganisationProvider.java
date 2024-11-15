package net.fedustria.nativetranslate.service.provider;

import net.fedustria.nativetranslate.service.model.organisation.Organisation;
import net.fedustria.nativetranslate.service.model.organisation.OrganisationDAO;

import java.util.Optional;

public class OrganisationProvider {
    private static final OrganisationDAO organisationDAO = new OrganisationDAO();

    public static Optional<Organisation> getOrganisationByName(final String name) {
        return organisationDAO.findByName(name);
    }

    public static Optional<Organisation> getOrganisationById(final long id) {
        return organisationDAO.findById(id);
    }

    public static void createOrganisation(final String name, final String description, final String logo) {
        organisationDAO.persist(Organisation.create(name, description, logo));
    }
}
