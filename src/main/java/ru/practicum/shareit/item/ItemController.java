package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemCommentsDtoResponse;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

import static ru.practicum.shareit.user.UserIdHttpHeader.USER_ID_HEADER;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated

public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId,
                          @RequestBody @Valid ItemCreateDto dto) {
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId,
                          @PathVariable @Positive Long itemId,
                          @RequestBody @Valid ItemUpdateDto dto) {

        return itemService.update(ownerId, dto, itemId);
    }

    @GetMapping
    List<ItemCommentsDtoResponse> getAll(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId) {
        return itemService.getAllByOwnerId(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponseDto createComment(@RequestHeader(USER_ID_HEADER)
                                            @Positive long authorId,
                                            @PathVariable long itemId,
                                            @RequestBody
                                            @Valid CommentCreateDto dto) {
        return itemService.createComment(dto, authorId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemCommentsDtoResponse getItemWithComments(@RequestHeader(USER_ID_HEADER)
                                                       @Positive
                                                       long userId,
                                                       @PathVariable
                                                       @Positive
                                                       long itemId) {
        return itemService.getWithComments(itemId, userId);
    }
}
