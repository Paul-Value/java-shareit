
package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.user.UserIdHttpHeader;


@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long userId,
                                         @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.create(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable @Positive long bookingId,
                                         @RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER)
                                         @Positive long userId, @RequestParam boolean approved) {
        log.info("Updating status booking: {}, userId: {}, approved: {}", bookingId, userId, approved);
        return bookingClient.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findById(@RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER) @Positive long userId,
                                           @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByBooker(@RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER)
                                                  @Positive long bookerId,
                                                  @RequestParam(defaultValue = "ALL") String stateParam) {
        log.info("Getting bookings by bookerId: {}, state: {}", bookerId, stateParam);
        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
        return bookingClient.findAllByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllByOwner(@RequestHeader(value = UserIdHttpHeader.USER_ID_HEADER)
                                                 @Positive long ownerId,
                                                 @RequestParam(defaultValue = "ALL") BookingState state) {
        log.info("Getting bookings by ownerId: {}, state: {}", ownerId, state);
        return bookingClient.findAllByOwner(ownerId, state);
    }
}
