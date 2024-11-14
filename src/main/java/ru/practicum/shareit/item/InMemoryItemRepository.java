package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private long itemId;

    @Override
    public Item save(Long ownerId, Item item) {
        log.debug("==> Save item {}", item);
        item.setId(generateId());
        items.put(item.getId(), item);
        log.debug("<== Item saved {}", item);
        return item;
    }

    @Override
    public Item update(Long ownerId, Item item) {
        log.debug("==> Update item {}", item);
        Item oldItem = items.get(item.getId());
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.debug("<== Item updated {}", oldItem);
        return oldItem;
    }

    @Override
    public Item get(Long ownerId, Long itemId) {
        log.debug("==> Get item {}", itemId);
        Item item = items.get(itemId);
        log.debug("<== Item received {}", item);
        return item;
    }

    @Override
    public List<Item> getAllForUser(Long ownerId) {
        log.debug("==> Get all items from user {}", ownerId);
        List<Item> itemsList = items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .toList();
        log.debug("<==  Items {} from user received {} ", items, ownerId);
        return itemsList;
    }

    @Override
    public List<Item> search(String text) {
        log.debug("==> Search items with text {}", text);
        if (text.isBlank()) {
            return List.of();
        }
        List<Item> result = items.values().stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        item.getName().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .toList();
        log.debug("<== Search for items {} with text {}", result, text);
        return result;
    }

    @Override
    public boolean isExist(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean isExistForUser(Long ownerId, Long itemId) {
        return items.get(itemId).getOwnerId().equals(ownerId);
    }

    private Long generateId() {
        return ++itemId;
    }
}
