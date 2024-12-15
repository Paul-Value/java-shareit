package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserIdHttpHeader;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ItemRequestDto create(@RequestHeader(name = UserIdHttpHeader.USER_ID_HEADER) long requesterId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        itemRequestDto.setRequesterId(requesterId);
        return itemRequestService.create(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAllByRequester(@RequestHeader(name = UserIdHttpHeader.USER_ID_HEADER)
                                                   long requesterId) {
        return itemRequestService.findAllByRequester(requesterId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAll() {
        return itemRequestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto findById(@PathVariable("requestId") long requestId) {
        return itemRequestService.findById(requestId);
    }
}
