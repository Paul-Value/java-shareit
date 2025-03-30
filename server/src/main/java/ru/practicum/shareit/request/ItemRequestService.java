package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findAllByRequester(long requesterId);

    List<ItemRequestDto> findAll();

    ItemRequestDto findById(long requestId);
}
