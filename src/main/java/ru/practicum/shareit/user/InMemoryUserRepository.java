package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private long userId;

    @Override
    public User save(User user) {
        log.debug("==> Save user {}", user);
        user.setId(generateId());
        users.put(user.getId(), user);
        emails.add(user.getEmail());
        log.debug("<== User saved {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("==> Update user {}", user);
        User oldUser = users.get(user.getId());
        String name = user.getName();
        if (name != null) {
            oldUser.setName(name);
        }
        String email = user.getEmail();
        if (email != null) {
            oldUser.setEmail(email);
            emails.add(email);
        }
        log.debug("<== User updated {}", user);
        return oldUser;
    }

    @Override
    public User get(Long userId) {
        log.debug("==> Getting user id = {}", userId);
        User user = users.get(userId);
        log.debug("<== User received {}", user);
        return user;
    }

    @Override
    public List<User> getAll() {
        log.debug("==> Get all users");
        List<User> allUsers = users.values().stream()
                .toList();
        log.debug("<== Users received");
        return allUsers;
    }

    @Override
    public void delete(Long userId) {
        log.debug("==> Deleting user with id = {}", userId);
        User user = users.remove(userId);
        emails.remove(user.getEmail());
        log.debug("<== Deleted user with id = {}", userId);
    }

    @Override
    public boolean isExist(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean isExist(String email) {
        return emails.contains(email);
    }

    private long generateId() {
        return ++userId;
    }
}
