package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto save(@RequestBody @Validated(Marker.Create.class) UserDto userDto) {
        return userService.save(userDto);
    }

    @PatchMapping("/{id}")
    UserDto update(@Positive @PathVariable Long id,
                   @RequestBody @Valid UserDto userDto) {
        userDto.setId(id);
        return userService.update(userDto);
    }

    @GetMapping
    List<UserDto> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    UserDto get(@Positive @PathVariable Long id) {
        return userService.get(id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable @Positive Long id) {
        userService.delete(id);
    }
}
