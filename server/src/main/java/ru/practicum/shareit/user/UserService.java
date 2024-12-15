package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

public interface UserService {
    UserDto create(UserCreateDto userDto);

    UserDto update(UserUpdateDto userDto, Long userId);

    UserDto get(Long id);

    List<UserDto> getAll();

    void delete(Long id);

    void isExists(Long id);

    void isExists(String email);
}
