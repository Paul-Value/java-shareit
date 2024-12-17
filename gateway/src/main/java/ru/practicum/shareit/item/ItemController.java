package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDtoCreate;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.user.UserIdHttpHeader;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long ownerId,
                                         @RequestBody @Valid ItemCreateDto dto) {
        log.info("Create item:{}, owner:{}", dto, ownerId);
        return itemClient.create(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long ownerId,
                                         @PathVariable @Positive long itemId, @RequestBody @Valid ItemUpdateDto dto) {
        log.info("Update item:{}, item id:{}, owner:{}", dto, itemId, ownerId);
        return itemClient.update(ownerId, itemId, dto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByOwnerId(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER)
                                                   @Positive long ownerId) {
        log.info("Find all items by ownerId:{}", ownerId);
        return itemClient.findAllByOwnerId(ownerId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemWithComments(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER)
                                                       @Positive long userId, @PathVariable @Positive long itemId) {
        log.info("Find item with comments, itemId:{}, userId:{}", itemId, userId);
        return itemClient.findItemWithComments(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long ownerId,
                                         @RequestParam("text") String text) {
        log.info("Find all items by ownerId: {}, text: {}", ownerId, text);
        return itemClient.search(ownerId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long authorId,
                                                @PathVariable long itemId, @RequestBody @Valid CommentDtoCreate dto) {
        log.info("Create comment: {} for item: {}, authorId:{}", dto, itemId, authorId);
        return itemClient.createComment(authorId, itemId, dto);
    }
}
