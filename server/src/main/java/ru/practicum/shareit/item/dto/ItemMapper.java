package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemFeedback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ItemMapper {
    public ItemDto modelToDto(Item model) {
        ItemDto dto = new ItemDto();
        dto.setId(model.getId());
        dto.setName(model.getName());
        dto.setDescription(model.getDescription());
        dto.setAvailable(model.getAvailable());
        dto.setOwnerId(model.getOwnerId());
        dto.setAvailable(model.getAvailable());
        dto.setRequestId(model.getRequestId());
        return dto;
    }

    public Item dtoToModel(ItemDto dto) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwnerId(dto.getOwnerId());
        item.setRequestId(dto.getRequestId());
        return item;
    }

    public Item dtoToModel(ItemUpdateDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public Item dtoToModel(ItemCreateDto dto) {
        Item item = new Item();
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        return item;
    }

    public ItemCommentsDtoResponse modelToDtoWithComments(Item model, List<CommentResponseDto> comments,
                                                                 Map<String, BookingDto> bookingDtoMap) {
        if (bookingDtoMap == null) {
            bookingDtoMap = new HashMap<>();
            bookingDtoMap.put("lastBooking", null);
            bookingDtoMap.put("nextBooking", null);
        }
        if (comments == null) {
            comments = List.of();
        }
        return ItemCommentsDtoResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .available(model.getAvailable())
                .ownerId(model.getOwnerId())
                .comments(comments)
                .lastBooking(bookingDtoMap.get("lastBooking"))
                .nextBooking(bookingDtoMap.get("nextBooking"))
                .requestId(model.getRequestId())
                .build();
    }

    public ItemFeedbackDto modelToDtoFeedback(ItemFeedback model) {
        return ItemFeedbackDto.builder()
                .id(model.getId())
                .name(model.getName())
                .ownerId(model.getOwnerId())
                .build();
    }
}