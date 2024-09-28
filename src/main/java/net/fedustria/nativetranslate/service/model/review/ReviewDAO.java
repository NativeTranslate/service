package net.fedustria.nativetranslate.service.model.review;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import net.fedustria.nativetranslate.service.model.project.Project;

import java.util.List;

@ApplicationScoped
public class ReviewDAO implements PanacheRepository<Review> {
    public List<Review> findByProject(final Project project) {
        return list("project", project);
    }
}
