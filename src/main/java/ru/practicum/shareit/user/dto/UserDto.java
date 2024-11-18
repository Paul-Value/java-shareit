package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.Marker;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    @Null
    private Long id;
    @NotBlank(groups = Marker.Create.class)
    private String name;
    @NotNull(groups = Marker.Create.class)
    @NotEmpty(groups = Marker.Create.class)
    @Email(groups = {Marker.Create.class, Marker.Update.class})
    private String email;
}
