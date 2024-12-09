package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto save(BookingCreateDto dto);

    BookingDto updateStatusBooking(long bookingId, long userId, boolean approved);

    BookingDto get(long bookingId, long userId);

    List<BookingDto> getAllByBooker(long bookerId, BookingState state);

    List<BookingDto> getAllByOwner(long ownerId, BookingState state);

    void exists(long bookingId);

    void exists(long bookingId, long userId);
}
