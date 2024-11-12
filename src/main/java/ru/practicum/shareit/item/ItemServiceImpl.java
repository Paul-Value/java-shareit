package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
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
        Item item = itemMapper.dtoToModel(dto);
        return itemMapper.modelToDto(itemRepository.save(ownerId, item));
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
        Item item = itemMapper.dtoToModel(dto);
        return itemMapper.modelToDto(itemRepository.update(ownerId, item));
    }

    @Override
    public ItemDto get(Long ownerId, Long itemId) {
        userService.isExists(ownerId);
        if (isExist(itemId)) {
            throw new NotFoundException("Item id = " + itemId + " not found");
        }
        return itemMapper.modelToDto(itemRepository.get(ownerId, itemId));
    }

    @Override
    public List<ItemDto> getAllForUser(Long ownerId) {
        userService.isExists(ownerId);
        return itemRepository.getAllForUser(ownerId).stream()
                .map(itemMapper::modelToDto)
                .toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(itemMapper::modelToDto)
                .toList();
    }

    @Override
    public boolean isExist(Long itemId) {
        return !itemRepository.isExist(itemId);
    }
}
