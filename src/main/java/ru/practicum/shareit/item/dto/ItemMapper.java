package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
@Qualifier("ItemMapper")
public interface ItemMapper {
    ItemDto toDto(Item item);

    @Mapping(target = "ownerId", source = "ownerId")
    Item toEntity(ItemDto dto, Long ownerId);
}
