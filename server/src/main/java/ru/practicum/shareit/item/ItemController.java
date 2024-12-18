package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static ru.practicum.shareit.user.UserIdHttpHeader.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor

public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@RequestHeader(USER_ID_HEADER) Long ownerId,
                          @RequestBody ItemCreateDto dto) {
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) Long ownerId,
                          @PathVariable Long itemId,
                          @RequestBody ItemUpdateDto dto) {

        return itemService.update(ownerId, dto, itemId);
    }

    @GetMapping
    List<ItemCommentsDtoResponse> getAll(@RequestHeader(USER_ID_HEADER) Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(USER_ID_HEADER) long authorId,
                                            @PathVariable long itemId,
                                            @RequestBody CommentCreateDto dto) {
        return itemService.createComment(dto, authorId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentsDtoResponse getItemWithComments(@RequestHeader(USER_ID_HEADER) long userId,
                                                       @PathVariable long itemId) {
        return itemService.getWithComments(itemId, userId);
    }
}
