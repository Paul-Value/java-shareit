package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    User update(User user);

    Optional<User> get(Long id);

    List<User> getAll();

    void delete(Long id);

    boolean isExist(Long id);

    boolean isExist(String email);
}
