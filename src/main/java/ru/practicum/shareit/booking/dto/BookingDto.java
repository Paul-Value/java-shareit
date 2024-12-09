package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.AssertTrue;
import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
    private Long bookerId;
    private BookingStatus status;

    @AssertTrue
    public boolean isValidDate() {
        return start.isBefore(end);
    }
}
