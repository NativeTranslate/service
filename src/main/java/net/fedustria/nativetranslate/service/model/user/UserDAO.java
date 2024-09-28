package net.fedustria.nativetranslate.service.model.user;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente)
 * Created on: 9/12/2024 2:46 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * Data Access Object (DAO) for the User entity.
 * Provides methods to perform CRUD operations on User entities.
 */
@ApplicationScoped
public class UserDAO implements PanacheRepository<User> {

    /**
     * Finds a user by their username.
     *
     * @param username the username of the user to find
     * @return an Optional containing the found User, or an empty Optional if no user is found
     */
    public Optional<User> findByUsername(final String username) {
        return find("username", username).firstResultOptional();
    }

    /**
     * Finds a user by their email.
     *
     * @param email the email of the user to find
     * @return an Optional containing the found User, or an empty Optional if no user is found
     */
    public Optional<User> findByEmail(final String email) {
        return find("email", email).firstResultOptional();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return an Optional containing the found User, or an empty Optional if no user is found
     */
    public Optional<User> findByUserId(final long id) {
        return find("id", id).firstResultOptional();
    }

    /**
     * Updates a value of a user.
     *
     * @param user     the user to update
     * @param value    the value to update
     * @param newValue the new value
     */
    public void updateValue(final User user, final String value, final String newValue) {
        switch (value) {
            case "username":
                user.setUsername(newValue);
                break;
            case "gender":
                user.setGender(newValue);
                break;
            case "dateOfBirth":
                user.setDateOfBirth(Long.parseLong(newValue));
                break;
            case "phoneNumber":
                user.setPhoneNumber(newValue);
                break;
            case "location":
                user.setLocation(newValue);
                break;
            case "bio":
                user.setBio(newValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    /**
     * Checks if the given password matches the user's password.
     *
     * @param user     the user to check the password for
     * @param password the password to check
     * @return true if the password matches, false otherwise
     */
    public boolean checkPassword(final User user, final String password) {
        return BcryptUtil.matches(password, user.getPassword());
    }

    /**
     * Updates the password of a user.
     *
     * @param user     the user to update the password for
     * @param password the new password
     */
    public void updatePassword(final User user, final String password) {
        user.setPassword(BcryptUtil.bcryptHash(password));
    }
}