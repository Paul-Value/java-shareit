
package ru.practicum.shareit.booking.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {

    public static Booking dtoToModel(BookingCreateDto dto, ItemDto itemDto, User booker) {
        Booking model = new Booking();
        model.setStart(dto.getStart());
        model.setEnd(dto.getEnd());
        model.setItem(ItemMapper.dtoToModel(itemDto));
        model.setBooker(booker);
        model.setStatus(dto.getStatus());
        return model;
    }

    public static BookingDto modelToResponseDto(Booking model) {
        BookingDto dto = new BookingDto();
        dto.setId(model.getId());
        dto.setStart(model.getStart());
        dto.setEnd(model.getEnd());
        dto.setItemId(model.getItem().getId());
        dto.setBookerId(model.getBooker().getId());
        dto.setStatus(model.getStatus());
        return dto;
    }

    public static BookingShortDto modelToShortDto(Booking model) {
        BookingShortDto dto = new BookingShortDto();
        dto.setId(model.getId());
        dto.setStart(model.getStart());
        dto.setEnd(model.getEnd());
        return dto;
    }
}
