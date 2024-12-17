package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCommentsDtoResponse;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto save(Long ownerId, ItemCreateDto dto);

    ItemDto update(Long ownerId, ItemUpdateDto dto, long itemId);

    ItemDto get(Long ownerId, Long itemId);

    List<ItemCommentsDtoResponse> getAllByOwnerId(Long ownerId);

    List<ItemDto> search(String text);

    boolean isExist(Long itemId);

    CommentResponseDto createComment(CommentCreateDto dto, long authorId, long itemId);

    ItemCommentsDtoResponse getWithComments(long itemId, long userId);
}
