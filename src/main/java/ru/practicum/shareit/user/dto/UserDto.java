package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

@Data
public class UserDto {
    @Null
    private Long id;
    @NotNull
    private String name;
    @NotNull
    @Email
    private String email;
}
