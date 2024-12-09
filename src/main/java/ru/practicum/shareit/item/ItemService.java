package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {
    ItemDto save(Long ownerId, ItemCreateDto dto);

    ItemDto update(Long ownerId, ItemUpdateDto dto, long itemId);

    ItemDto get(Long ownerId, Long itemId);

    List<ItemCommentsDtoResponse> getAllByOwnerId(Long ownerId);

    List<ItemDto> search(String text);

    void isExist(Long itemId);

    CommentResponseDto createComment(CommentCreateDto dto, long authorId, long itemId);

    ItemCommentsDtoResponse getWithComments(long itemId, long userId);
}
