package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto save(Long ownerId, ItemDto dto) {
        userService.isExists(ownerId);
        Item item = itemMapper.toEntity(dto, ownerId);
        return itemMapper.toDto(itemRepository.save(ownerId, item));
    }

    @Override
    public ItemDto update(Long ownerId, ItemDto dto) {
        if (!itemRepository.isExistForUser(ownerId, dto.getId())) {
            throw new NotFoundException("Item id = " + dto.getId() + " not found for user id = " + ownerId);
        }
        userService.isExists(ownerId);
        if (isExist(dto.getId())) {
            throw new NotFoundException("Item id = " + dto.getId() + " not found");
        }
        Item item = itemMapper.toEntity(dto, ownerId);
        return itemMapper.toDto(itemRepository.update(ownerId, item));
    }

    @Override
    public ItemDto get(Long ownerId, Long itemId) {
        userService.get(ownerId);
        Item item = itemRepository.get(ownerId, itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));
        //return itemMapper.modelToDto(item);
        return itemMapper.toDto(item);
    }

    @Override
    public List<ItemDto> getAllForUser(Long ownerId) {
        userService.isExists(ownerId);
        return itemRepository.getAllForUser(ownerId).stream()
                .map(itemMapper::toDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.search(text).stream()
                .map(itemMapper::toDto)
                .toList();
    }

    @Override
    public boolean isExist(Long itemId) {
        return !itemRepository.isExist(itemId);
    }
}
