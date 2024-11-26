package net.fedustria.nativetranslate.service.model.notifications;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.Date;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 11/26/2024 4:53 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

@Entity
@Table(name = "notifications")
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long userId;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private Date date;

    private Notification() {
    }

    public static Notification create(final long userId, final String title, final String description, final Date date) {
        final Notification notification = new Notification();
        notification.userId = userId;
        notification.title = title;
        notification.description = description;
        notification.date = date;
        return notification;
    }

}
