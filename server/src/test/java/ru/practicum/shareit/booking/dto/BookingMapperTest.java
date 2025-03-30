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

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
    private BookingDto dto = new BookingDto();
    private BookingCreateDto dtoCreate = new BookingCreateDto();
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
        dtoCreate.setItemId(itemId);
        dtoCreate.setStart(start);
        dtoCreate.setEnd(end);
        dtoCreate.setBookerId(bookerId);
        dtoCreate.setStatus(status);

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
    void dtoToModel() {
        Booking result = BookingMapper.dtoToModel(dtoCreate, itemDto, bookerDto);

        assertEquals(model.getStart(), result.getStart());
        assertEquals(model.getEnd(), result.getEnd());
        assertEquals(model.getItem(), result.getItem());
        assertEquals(model.getBooker().getName(), result.getBooker().getName());
        assertEquals(model.getStatus(), result.getStatus());
    }

    @Test
    void modelToDtoResponse() {
        model.setId(id);
        BookingDto result = BookingMapper.modelToResponseDto(model);

        assertEquals(dto, result);
    }
}