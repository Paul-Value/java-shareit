package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.ItemFeedbackDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@UtilityClass
public class ItemRequestMapper {

    public ItemRequest dtoToModel(ItemRequestDto dto) {
        ItemRequest model = new ItemRequest();
        model.setDescription(dto.getDescription());
        model.setCreated(dto.getCreated());
        model.setRequesterId(dto.getRequesterId());
        return model;
    }

    public ItemRequestDto modelToDto(ItemRequest model) {
        List<ItemFeedbackDto> listItemFeedbackDto = null;
        if (model.getItems() != null) {
            listItemFeedbackDto = model.getItems().stream()
                    .map(ItemMapper::modelToDtoFeedback)
                    .toList();
        }
        return ItemRequestDto.builder()
                .id(model.getId())
                .description(model.getDescription())
                .created(model.getCreated())
                .items(listItemFeedbackDto)
                .requesterId(model.getRequesterId())
                .build();
    }
}

