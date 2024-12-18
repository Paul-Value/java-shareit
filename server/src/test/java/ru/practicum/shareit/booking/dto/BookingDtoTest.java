package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BookingDtoTest {
    private BookingDto dto = new BookingDto();
    private long itemId = 1;
    private LocalDateTime start = LocalDateTime.now();
    private LocalDateTime end = LocalDateTime.now().plusDays(1);
    private long bookerId = 1;
    private BookingStatus status = BookingStatus.WAITING;
    private ItemDto itemDto = new ItemDto();
    private String itemName = "item";
    private String description = "description";
    private long ownerId = 2L;
    private boolean available = true;
    private UserDto bookerDto = new UserDto();
    private String userName = "user";
    private String email = "user@email.com";
    private Booking model = new Booking();
    private long id = 1;

    @BeforeEach
    void setUp() {
        itemDto.setId(itemId);
        itemDto.setName(itemName);
        itemDto.setDescription(description);
        itemDto.setOwnerId(ownerId);
        itemDto.setAvailable(available);

        bookerDto.setId(bookerId);
        bookerDto.setName(userName);
        bookerDto.setEmail(email);

        model.setStart(start);
        model.setEnd(end);
        model.setItem(ItemMapper.dtoToModel(itemDto));
        model.setBooker(UserMapper.dtoToModel(bookerDto));
        model.setStatus(status);

        dto.setId(id);
        dto.setStatus(status);
        dto.setStart(start);
        dto.setEnd(end);
        dto.setItem(itemDto);
        dto.setBooker(bookerDto);
    }

    @Test
    void isValidDate() {
        assertTrue(dto.isValidDate());
    }
}