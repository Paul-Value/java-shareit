package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select exists(select id from users where email = ?1)", nativeQuery = true)
    boolean existsByUserEmail(String email);
}
