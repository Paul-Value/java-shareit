package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService itemService;
    private final String itemHeader = "X-Sharer-User-Id";

    @PostMapping
    @Validated(Marker.Create.class)
    public ItemDto create(@RequestHeader(itemHeader) @NotNull @Positive Long ownerId,
                          @RequestBody @Valid ItemDto dto) {
        dto.setOwnerId(ownerId);
        return itemService.save(ownerId, dto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader(itemHeader) @NotNull @Positive Long ownerId,
                          @PathVariable @NotNull @Positive Long itemId,
                          @RequestBody @Valid ItemDto dto) {
        dto.setId(itemId);
        dto.setOwnerId(ownerId);
        return itemService.update(ownerId, dto);
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@RequestHeader(itemHeader) @NotNull @Positive Long ownerId,
                       @PathVariable @NotNull @Positive Long itemId) {
        return itemService.get(ownerId, itemId);
    }

    @GetMapping
    List<ItemDto> getAllForUser(@RequestHeader(itemHeader) @NotNull @Positive Long ownerId) {
        return itemService.getAllForUser(ownerId);
    }

    @GetMapping("/search")
    List<ItemDto> search(@RequestParam("text") String text) {
        return itemService.search(text);
    }
}
