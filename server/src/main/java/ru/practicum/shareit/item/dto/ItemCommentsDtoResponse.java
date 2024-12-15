package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Data
@Builder
public class ItemCommentsDtoResponse {
    private Long id;
    private String name;
    private String description;
    private Long ownerId;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentResponseDto> comments;
    private Long requestId;
}