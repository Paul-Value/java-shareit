package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemRepository implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Long>> userItemIndex = new LinkedHashMap<>();
    private long itemId;

    @Override
    public Item save(Long ownerId, Item item) {
        log.debug("==> Save item {}", item);
        item.setId(generateId());
        items.put(item.getId(), item);
        userItemIndex.computeIfAbsent(item.getOwnerId(), id -> new ArrayList<>()).add(item.getId());
        log.debug("<== Item saved {}", item);
        return item;
    }

    @Override
    public Item update(Long ownerId, Item item) {
        log.debug("==> Update item {}", item);
        Item oldItem = items.get(item.getId());
        if (item.getName() != null && !item.getName().isBlank()) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        log.debug("<== Item updated {}", oldItem);
        return oldItem;
    }

    @Override
    public Optional<Item> get(Long ownerId, Long itemId) {
        log.debug("==> Get item {}", itemId);
        Optional<Item> item = Optional.ofNullable(items.get(itemId));
        log.debug("<== Item received {}", item);
        return item;
    }

    @Override
    public List<Item> getAllForUser(Long ownerId) {
        log.debug("==> Get all items from user {}", ownerId);
        List<Item> itemsList = userItemIndex.get(ownerId).stream()
                .map(items::get)
                .collect(Collectors.toList());
        log.debug("<==  Items {} from user received {} ", itemsList, ownerId);
        return itemsList;
    }

    @Override
    public List<Item> search(String text) {
        log.debug("==> Search items with text {}", text);
        String textLowerCase = text.toLowerCase();
        List<Item> result = items.values().stream()
                .filter(item -> (item.getDescription().toLowerCase().contains(textLowerCase) ||
                        item.getName().toLowerCase().contains(textLowerCase))
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
