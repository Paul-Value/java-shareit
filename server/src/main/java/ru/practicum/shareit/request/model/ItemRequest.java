package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.item.model.ItemFeedback;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "request")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private long requesterId;
    @Column(nullable = false)
    private LocalDateTime created;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private List<ItemFeedback> items;
}
