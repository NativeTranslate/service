package net.fedustria.nativetranslate.service.model.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long   id;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;

    private User() {}

    public static User create(final String username, final String email, final String password, final String role) {
        final User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.role = role;
        return user;
    }
}
