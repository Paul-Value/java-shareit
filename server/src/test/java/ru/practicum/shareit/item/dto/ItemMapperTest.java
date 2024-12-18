package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemForRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    private Item model = new Item(1L, "afafqe", "ascacq", 1L, true, 1L);
    private ItemDto dto = new ItemDto(1L, "afafqe", "ascacq", 1L, true, null, null, 1L);

    @Test
    void modelToDto() {
        ItemDto result = ItemMapper.modelToDto(model);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void dtoToModel() {
        Item result = ItemMapper.dtoToModel(dto);

        assertNotNull(result);
        assertEquals(model, result);
    }

    @Test
    void modelToDtoWithComments() {
        UserDto userDto = new UserDto();
                userDto.setId(1L);
                userDto.setName("afqvv");
                userDto.setEmail("afqvv@gmail.com");

        CommentResponseDto commentDtoResponse = CommentResponseDto.builder()
                .id(1L)
                .text("fefqq")
                .author(userDto)
                .item(dto)
                .created(LocalDateTime.now())
                .authorName(userDto.getName())
                .build();
        List<CommentResponseDto> listCommentDtoResponses = List.of(commentDtoResponse);
        Map<String, BookingDto> bookingDtoMap = new HashMap<>();
        bookingDtoMap.put("lastBooking", null);
        bookingDtoMap.put("nextBooking", null);
        ItemCommentsDtoResponse result = ItemMapper.modelToDtoWithComments(model, listCommentDtoResponses, bookingDtoMap);

        assertNotNull(result);
        assertEquals(result.getId(), model.getId());
        assertEquals(result.getName(), model.getName());
        assertEquals(result.getDescription(), model.getDescription());
        assertEquals(result.getAvailable(), model.getAvailable());
        assertEquals(result.getOwnerId(), model.getOwnerId());
        assertEquals(result.getComments(), listCommentDtoResponses);
        assertEquals(result.getLastBooking(), bookingDtoMap.get("lastBooking"));
        assertEquals(result.getNextBooking(), bookingDtoMap.get("nextBooking"));
        assertEquals(result.getRequestId(), model.getRequestId());
    }

    @Test
    void modelFeedbackToDtoFeedback() {
        ItemForRequestDto forRequestDto = ItemForRequestDto.builder()
                .id(model.getId())
                .name(model.getName())
                .ownerId(model.getOwnerId())
                .build();
        ItemForRequest forRequestModel = ItemForRequest.builder()
                .id(model.getId())
                .name(model.getName())
                .ownerId(model.getOwnerId())
                .build();

        ItemForRequestDto result = ItemMapper.modelToDtoForRequest(forRequestModel);

        assertNotNull(result);
        assertEquals(forRequestDto, result);
    }
}