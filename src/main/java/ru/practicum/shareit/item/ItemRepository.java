package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item save(Long ownerId, Item item);

    Item update(Long ownerId, Item item);

    Optional<Item> get(Long ownerId, Long itemId);

    List<Item> getAllForUser(Long ownerId);

    List<Item> search(String text);

    boolean isExist(Long itemId);

    boolean isExistForUser(Long ownerId, Long itemId);
}
