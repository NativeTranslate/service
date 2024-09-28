package net.fedustria.nativetranslate.service.model.review;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.fedustria.nativetranslate.service.model.language.Language;
import net.fedustria.nativetranslate.service.model.project.Project;
import net.fedustria.nativetranslate.service.model.user.User;

@Getter
@Setter
@Entity
@Table(name = "fds_reviews")
public class Review {
    @Id
    @GeneratedValue
    private long        id;
    private EReviewType type;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project     project;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_id")
    private Language    language;
    @Column(name = "original_text")
    private String      originalText;
    @Column(name = "translated_text")
    private String      translatedText;
    @Column(name = "current_text")
    private String      currentText;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User        translator;
}
