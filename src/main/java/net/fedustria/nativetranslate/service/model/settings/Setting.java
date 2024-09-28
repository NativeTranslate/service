package net.fedustria.nativetranslate.service.model.settings;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/25/2024 11:35 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.fedustria.nativetranslate.service.model.user.User;

/**
 * Entity class representing a Setting.
 * This class is mapped to the "fds_usersettings" table in the database.
 */
@Getter
@Setter
@Entity
@Table(name = "fds_usersettings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "key"})
})
public class Setting {

    /**
     * The unique identifier for the user.
     * This value is generated automatically.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The user associated with the setting.
     * This is a many-to-one relationship with the User entity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * The key of the setting.
     * This value must be unique.
     */
    private String setting;

    /**
     * The value of the setting.
     */
    private String value;
}