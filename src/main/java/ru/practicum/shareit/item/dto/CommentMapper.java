package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.dto.UserMapper;

@UtilityClass
public class CommentMapper {
    UserMapper userMapper;

    public static CommentResponseDto modelToDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthor(userMapper.toDto(comment.getAuthor()));
        dto.setItem(ItemMapper.modelToDto(comment.getItem()));
        dto.setCreated(comment.getCreated());
        dto.setAuthorName(dto.getAuthor().getName());
        return dto;
    }

    public static CommentShortDto modelToShortDto(Comment comment) {
        CommentShortDto dto = new CommentShortDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        return dto;
    }

    public Comment dtoToModel(CommentCreateDto dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        comment.setCreated(dto.getCreated());
        return comment;
    }
}