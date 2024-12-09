package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public BookingDto save(BookingCreateDto dto) {
        ItemDto itemDto = itemService.get(dto.getBookerId(), dto.getItemId());
        if (!itemDto.getAvailable()) {
            throw new UnavailableItemException(String.format("Item with id %d not available", itemDto.getId()));
        }
        User booker = userMapper.toEntity(userService.get(dto.getBookerId()));
        Booking booking = BookingMapper.dtoToModel(dto, itemDto, booker);
        return BookingMapper.modelToResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto updateStatusBooking(long bookingId, long userId, boolean approved) {
        exists(bookingId, userId);
        userService.isExists(userId);
        BookingStatus bookingStatus;
        if (approved) {
            bookingStatus = BookingStatus.APPROVED;
        } else {
            bookingStatus = BookingStatus.REJECTED;
        }
        bookingRepository.updateBooking(bookingId, userId, bookingStatus);
        return BookingMapper.modelToResponseDto(bookingRepository.findById(bookingId).get());
    }

    @Override
    public BookingDto get(long bookingId, long userId) {
        exists(bookingId);
        Booking booking = bookingRepository.findByBookingId(bookingId, userId)
                .orElseThrow(() -> new BookingAccessException("Access denied for user with id = " + userId));
        return BookingMapper.modelToResponseDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBooker(long bookerId, BookingState state) {
        userService.isExists(bookerId);
        List<Booking> modelList;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST -> modelList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(bookerId, now);
            case FUTURE -> modelList = bookingRepository.findAllByBookerIdAndStartAfterOrderByEndDesc(bookerId, now);
            case WAITING -> modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId,
                    BookingStatus.WAITING);
            case REJECTED -> modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(bookerId,
                    BookingStatus.REJECTED);
            case CURRENT -> modelList = bookingRepository.findAllByBookerIdAndNowBetweenOrderByEndDesc(bookerId, now);
            default -> modelList = bookingRepository.findAllByBookerIdOrderByEndDesc(bookerId);
        }
        return modelList.stream()
                .map(BookingMapper::modelToResponseDto)
                .toList();
    }

    @Override
    public List<BookingDto> getAllByOwner(long ownerId, BookingState state) {
        userService.isExists(ownerId);
        List<Booking> modelList;
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case PAST -> modelList = bookingRepository.findAllByBookerIdAndEndBeforeOrderByEndDesc(ownerId, now);
            case FUTURE -> modelList = bookingRepository.findAllByBookerIdAndStartAfterOrderByEndDesc(ownerId, now);
            case WAITING -> modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(ownerId,
                    BookingStatus.WAITING);
            case REJECTED -> modelList = bookingRepository.findAllByBookerIdAndStatusOrderByEndDesc(ownerId,
                    BookingStatus.REJECTED);
            case CURRENT -> modelList = bookingRepository.findAllByBookerIdAndNowBetweenOrderByEndDesc(ownerId, now);
            default -> modelList = bookingRepository.findAllByBookerIdOrderByEndDesc(ownerId);
        }
        return modelList.stream()
                .map(BookingMapper::modelToResponseDto)
                .toList();
    }

    @Override
    public void exists(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException(String.format("Booking with id %d not found", bookingId));
        }

    }

    @Override
    public void exists(long bookingId, long userId) {
        if (!bookingRepository.existsByOwnerId(bookingId, userId)) {
            throw new BookingAccessException(String.format("User with id %d not owner of booking with id %d",
                    bookingId, userId));
        }
    }
}
