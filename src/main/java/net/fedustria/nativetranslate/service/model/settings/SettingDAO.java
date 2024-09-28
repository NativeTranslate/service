package net.fedustria.nativetranslate.service.model.settings;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import net.fedustria.nativetranslate.service.model.user.User;

import java.util.HashSet;
import java.util.Set;

/**
 * © 2024 Florian O. (https://github.com/Fedox-die-Ente) Created on: 9/25/2024 11:53 PM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

/**
 * Data Access Object (DAO) for managing settings.
 */
@ApplicationScoped
public class SettingDAO implements PanacheRepository<Setting> {

    /**
     * Finds settings associated with a specific user.
     *
     * @param user the user whose settings are to be retrieved
     * @return a set of settings associated with the user
     */
    public Set<Setting> findByUser(final User user) {
        return new HashSet<>(find("user", user).list());
    }

    /**
     * Update a setting for a specific user.
     *
     * @param user  the user whose setting is to be updated
     * @param key   the key of the setting to update
     * @param value the new value of the setting
     */
    public void updateSetting(final User user, final String key, final String value) {
        find("user = ?1 and setting = ?2", user, key).firstResultOptional()
                .ifPresentOrElse(setting -> {
                    setting.setValue(value);
                    persist(setting);
                }, () -> {
                    final Setting newSetting = new Setting();
                    newSetting.setUser(user);
                    newSetting.setSetting(key);
                    newSetting.setValue(value);
                    persist(newSetting);
                });
    }

}