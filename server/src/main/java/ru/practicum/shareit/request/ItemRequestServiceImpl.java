package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto) {
        log.debug("Create ItemRequest, ItemRequestDto: {}", itemRequestDto);
        ItemRequest itemRequest = ItemRequestMapper.dtoToModel(itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());
        ItemRequestDto result = ItemRequestMapper.modelToDto(itemRequestRepository.save(itemRequest));
        log.debug("ItemRequest created: {}", result);
        return result;
    }

    @Override
    public List<ItemRequestDto> findAllByRequester(long requesterId) {
        log.debug("Find all item's requests by requester, Requestor: {}", requesterId);
        List<ItemRequest> listItemRequests = itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(requesterId);
        log.debug("Find all item's requests by requester, List of models {}", listItemRequests);
        List<ItemRequestDto> result = listItemRequests.stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
        log.debug("Find all item's requests by requester, List of dtos: {}", result);
        return result;
    }

    @Override
    public List<ItemRequestDto> findAll() {
        log.debug("Find all item's requests");
        List<ItemRequest> listItemRequests = itemRequestRepository.findAllOrderByCreatedDesc();
        log.debug("Find all item's requests, List of models: {}", listItemRequests);
        List<ItemRequestDto> result = listItemRequests.stream()
                .map(ItemRequestMapper::modelToDto)
                .toList();
        log.debug("Find all item's requests, List of dtos: {}", result);
        return result;
    }

    @Override
    public ItemRequestDto findById(long requestId) {
        log.debug("Find item's request by id: {}", requestId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Not found Item's request by id: " + requestId));
        log.debug("Finding item's request model: {}", itemRequest);
        ItemRequestDto result = ItemRequestMapper.modelToDto(itemRequest);
        log.debug("Finding item's request dto: {}", result);
        return result;
    }
}
