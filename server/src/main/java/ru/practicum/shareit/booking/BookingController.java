package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.user.UserIdHttpHeader;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto create(@RequestBody BookingCreateDto bookingCreateDto,
                             @RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) long bookerId) {
        bookingCreateDto.setBookerId(bookerId);
        return bookingService.save(bookingCreateDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatusBooking(@PathVariable long bookingId,
                                          @RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) long userId,
                                          @RequestParam boolean approved) {
        return bookingService.updateStatusBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId,
                                 @RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getAllByBooker(@RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) long bookerId,
                                           @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllByOwner(@RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) long ownerId,
                                          @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getAllByOwner(ownerId, state);
    }
}