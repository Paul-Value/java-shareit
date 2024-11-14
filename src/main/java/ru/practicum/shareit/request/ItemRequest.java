package ru.practicum.shareit.request;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
public class ItemRequest {
    private int id;
    private String name;
    private String description;
}
