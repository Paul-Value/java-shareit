package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import ru.practicum.shareit.Marker;

@Data
public class ItemDto {
    @Null(groups = Marker.Create.class)
    private Long id;
    @NotEmpty(groups = Marker.Create.class)
    private String name;
    @NotEmpty(groups = Marker.Create.class)
    private String description;
    private Long ownerId;
    @NotNull(groups = Marker.Create.class)
    private Boolean available;
}
