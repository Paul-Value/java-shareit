package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemDto;

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
                          @RequestBody @Validated(Marker.Create.class) ItemDto dto) {
        dto.setOwnerId(ownerId);
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId,
                          @PathVariable @Positive Long itemId,
                          @RequestBody @Valid ItemDto dto) {
        dto.setId(itemId);
        dto.setOwnerId(ownerId);
        return itemService.update(ownerId, dto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId,
                       @PathVariable @Positive Long itemId) {
        return itemService.get(ownerId, itemId);
    }

    @GetMapping
    List<ItemDto> getAllForUser(@RequestHeader(USER_ID_HEADER) @Positive Long ownerId) {
        return itemService.getAllForUser(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }
}
