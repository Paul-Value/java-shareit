package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody @Valid UserCreateDto userCreateDto) {
        log.info("Create user: {}", userCreateDto);
        return userClient.create(userCreateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@NotNull @Positive @PathVariable(name = "id") Long id,
                                         @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return userClient.update(id, userUpdateDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "id") @NotNull @Positive Long id) {
        return userClient.findById(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(name = "id") @NotNull @Positive Long id) {
        userClient.delete(id);
    }
}
