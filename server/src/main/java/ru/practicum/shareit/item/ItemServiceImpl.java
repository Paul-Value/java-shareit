package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.NotValidException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    //private final UserService userService;

    @Override
    @Transactional
    public ItemDto save(Long ownerId, ItemCreateDto dto) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User with id: " + ownerId + " not found");
        }
        Item item = ItemMapper.dtoToModel(dto);
        item.setOwnerId(userRepository.findById(ownerId).get().getId());
        return ItemMapper.modelToDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long ownerId, ItemUpdateDto dto, long itemId) {
        if (!itemRepository.existsItemForUser(ownerId,itemId)) {
            throw new NotFoundException("Item id = " + itemId + " not found for user id = " + ownerId);
        }
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User with id: " + ownerId + " not found");
        }
        isExist(itemId);
        Item item = ItemMapper.dtoToModel(dto);
        Item itemFromRep = itemRepository.findById(itemId).get();
        item.setOwnerId(ownerId);
        item.setId(itemId);
        if (dto.getName() != null) {
            item.setName(dto.getName());
        } else {
            item.setName(itemFromRep.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        } else {
            item.setDescription(itemFromRep.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        } else {
            item.setAvailable(itemFromRep.getAvailable());
        }
        return ItemMapper.modelToDto(itemRepository.save(item));
    }

    @Override
    public ItemDto get(Long ownerId, Long itemId) {
        userRepository.findById(ownerId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Item not found with id: " + itemId));
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now);
        Map<String, BookingDto> bookingDtoMap = getLastAndNextBookings(bookings, now);
        ItemDto itemDto = ItemMapper.modelToDto(item);
        itemDto.setLastBooking(bookingDtoMap.get("lastBooking"));
        itemDto.setNextBooking(bookingDtoMap.get("nextBooking"));
        return itemDto;
    }

    @Override
    public List<ItemCommentsDtoResponse> getAllByOwnerId(Long ownerId) {
        if (!userRepository.existsById(ownerId)) {
            throw new NotFoundException("User with id: " + ownerId + " not found");
        }
        LocalDateTime now = LocalDateTime.now();
        List<Item> items = itemRepository.findAllByOwnerId(ownerId);
        List<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(ownerId, now);
        Map<Item, List<Booking>> itemBookingsMap = new HashMap<>();

        for (Booking booking: bookings) {
            Item item = booking.getItem();
            if (!itemBookingsMap.containsKey(item)) {
                List<Booking> newItemBookings = new ArrayList<>();
                newItemBookings.add(booking);
                itemBookingsMap.put(item, newItemBookings);
            }
            itemBookingsMap.get(item).add(booking);
        }

        Map<Item, Map<String, BookingDto>> itemsByLastAndNextBookingDto = new HashMap<>();

        for (Map.Entry<Item, List<Booking>> entry : itemBookingsMap.entrySet()) {
            Item item = entry.getKey();
            Map<String, BookingDto> lastAndNextBooking = getLastAndNextBookings(entry.getValue(), now);
            itemsByLastAndNextBookingDto.put(item, lastAndNextBooking);
        }

        List<Comment> comments = commentRepository.findAllByItemOwnerId(ownerId);
        Map<Item, List<CommentResponseDto>> commentsByItemId = new HashMap<>();
        for (Comment comment : comments) {
            Item item = comment.getItem();
            CommentResponseDto commentDtoResponse = CommentMapper.modelToDto(comment);
            List<CommentResponseDto> list = commentsByItemId.get(item);
            if (list == null) {
                list = new ArrayList<>();
                list.add(commentDtoResponse);
                commentsByItemId.put(item, list);
            }
            list.add(commentDtoResponse);
        }
        List<ItemCommentsDtoResponse> result = new ArrayList<>();
        for (Item item : items) {
            ItemCommentsDtoResponse itemCommentsDto = ItemMapper.modelToDtoWithComments(item,
                    commentsByItemId.get(item), itemsByLastAndNextBookingDto.get(item));
            result.add(itemCommentsDto);
        }
        return result;
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemRepository.searchContaining(text).stream()
                .map(ItemMapper::modelToDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponseDto createComment(CommentCreateDto dto, long userId, long itemId) {
        bookingRepository.findByBookerIdAndItemIdOrderByStart(userId, itemId).stream()
                .filter(booking -> booking.getEnd().isBefore(dto.getCreated()))
                .findFirst()
                .orElseThrow(() -> new NotValidException("Fail to create comment"));

        Comment comment = CommentMapper.dtoToModel(dto);
        User user = userRepository.findById(userId).orElse(null);
        comment.setAuthor(user);
        Item item = itemRepository.findById(itemId).orElse(null);
        comment.setItem(item);
        return CommentMapper.modelToDto(commentRepository.save(comment));
    }

    @Override
    public ItemCommentsDtoResponse getWithComments(long itemId, long userId) {
        isExist(itemId);
        LocalDateTime now = LocalDateTime.now();
        Item item = itemRepository.findById(itemId).get();
        List<CommentResponseDto> commentDtos = commentRepository.findAllByItemIdOrderById(itemId).stream()
                .map(CommentMapper::modelToDto)
                .toList();
        Map<String, BookingDto> bookingDtoMap = null;
        if (item.getOwnerId().equals(userId)) {
            List<Booking> list = bookingRepository.findAllByItemIdAndEndBeforeOrderByEndDesc(itemId, now);
            bookingDtoMap = getLastAndNextBookings(list, now);
        }
        return ItemMapper.modelToDtoWithComments(item, commentDtos, bookingDtoMap);
    }

    @Override
    public boolean isExist(Long itemId) {
        boolean exists = itemRepository.existsById(itemId);
        if (!exists) {
            throw new NotFoundException("Item not found with id " + itemId);
        }
        return true;
    }

    public Map<String, BookingDto> getLastAndNextBookings(List<Booking> bookings, LocalDateTime now) {
        Map<String, BookingDto> bookingDtoMap = new HashMap<>();
        Booking lastBookingModel = null;
        LocalDateTime last = LocalDateTime.MIN;
        for (Booking booking : bookings) {
            if (booking.getEnd().isAfter(last)) {
                last = booking.getEnd();
                lastBookingModel = booking;
            }
        }

        Booking nextBookingModel = null;
        long min = Long.MAX_VALUE;
        for (Booking booking : bookings) {
            long next = (booking.getStart().toEpochSecond(ZoneOffset.UTC) - now.toEpochSecond(ZoneOffset.UTC));
            if (next < min) {
                min = next;
                nextBookingModel = booking;
            }
        }
        BookingDto nextBooking = null;
        if (nextBookingModel != null) {
            nextBooking = BookingMapper.modelToResponseDto(nextBookingModel);
        }
        BookingDto lastBooking = null;
        if (lastBookingModel != null) {
            lastBooking = BookingMapper.modelToResponseDto(lastBookingModel);
        }
        bookingDtoMap.put("lastBooking", lastBooking);
        bookingDtoMap.put("nextBooking", nextBooking);
        return bookingDtoMap;
    }
}
