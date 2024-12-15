package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Marker;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserIdHttpHeader;

@Controller
@RequiredArgsConstructor
@RequestMapping("/request")
@Validated
@Slf4j
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Validated(Marker.Create.class)
    public ResponseEntity<Object> create(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long requesterId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Create request: {}", itemRequestDto);
        return itemRequestClient.create(requesterId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> findAllByRequester(@RequestHeader(UserIdHttpHeader.USER_ID_HEADER) @Positive long requesterId) {
        log.info("Find all requests by requester: {}", requesterId);
        return itemRequestClient.findAllByRequester(requesterId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll() {
        log.info("Find all requests");
        return itemRequestClient.findAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> findById(@PathVariable("requestId") @Positive long requestId) {
        log.info("Find request by id: {}", requestId);
        return itemRequestClient.findById(requestId);
    }
}
