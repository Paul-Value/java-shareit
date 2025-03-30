package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
