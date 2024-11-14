package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto save(Long ownerId, ItemDto dto);

    ItemDto update(Long ownerId, ItemDto dto);

    ItemDto get(Long ownerId, Long itemId);

    List<ItemDto> getAllForUser(Long ownerId);

    List<ItemDto> search(String text);

    boolean isExist(Long itemId);
}
