package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserIdHttpHeader;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    @Autowired
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(name = UserIdHttpHeader.USER_ID_HEADER) long requesterId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequesterId(requesterId);
        log.info("Creating request: {}", itemRequestDto);
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequester(@RequestHeader(name = UserIdHttpHeader.USER_ID_HEADER)
                                                   long requesterId) {
        log.info("Find all by requesterId: {}", requesterId);
        return itemRequestService.findAllByRequester(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        log.info("Find all requests");
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable("requestId") long requestId) {
        log.info("Finding request by id: {}", requestId);
        return itemRequestService.findById(requestId);
    }
}
