package net.fedustria.nativetranslate.service.model.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi")
public interface UserMapper {
    @Mapping(target = "password", ignore = true)
    UserDTO toDTO(final User user);

    record UserDTO(long id, String username, String email, String role) {}
}
