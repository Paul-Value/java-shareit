package ru.practicum.shareit.user.dto;

import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
@Qualifier("UserMapper")
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
