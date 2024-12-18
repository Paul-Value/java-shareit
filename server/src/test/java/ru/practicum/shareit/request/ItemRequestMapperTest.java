package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemForRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {
    private Long id = 1L;
    private long requesterId = 1L;
    private String description = "description";
    private LocalDateTime created = LocalDateTime.now();
    private List<ItemForRequestDto> items = List.of();
    private ItemRequestDto dto = ItemRequestDto.builder()
            .id(id)
            .requesterId(requesterId)
            .description(description)
            .created(created)
            .items(items)
            .build();
    private ItemRequest model = new ItemRequest();

    @Test
    void dtoToModel() {
        model.setRequesterId(requesterId);
        model.setDescription(description);
        model.setCreated(created);
        ItemRequest result = ItemRequestMapper.dtoToModel(dto);

        assertEquals(requesterId, result.getRequesterId());
        assertEquals(description, result.getDescription());
        assertEquals(created, result.getCreated());
    }

    @Test
    void modelToDto() {
        model.setId(id);
        model.setRequesterId(requesterId);
        model.setDescription(description);
        model.setCreated(created);
        model.setItems(List.of());
        ItemRequestDto result = ItemRequestMapper.modelToDto(model);

        assertEquals(dto, result);
    }

}