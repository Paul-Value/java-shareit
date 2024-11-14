package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto save(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto get(Long id);

    List<UserDto> getAll();

    void delete(Long id);

    void isExists(Long id);

    void isExists(String email);
}
