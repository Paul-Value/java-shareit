package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
public class CommentResponseDto {
    private long id;
    private String text;
    private UserDto author;
    private ItemDto item;
    private LocalDateTime created;
    private String authorName;
}