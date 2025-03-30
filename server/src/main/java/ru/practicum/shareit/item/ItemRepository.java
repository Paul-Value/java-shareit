package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(long userId);

    @Query("select i from Item i where (lower(i.name) like lower(concat('%', ?1, '%')) or lower(i.description) " +
            "like lower(concat('%', ?1, '%'))) and i.available = true")
    List<Item> searchContaining(@Param("text") String text);

    @Query(nativeQuery = true, value = "SELECT EXISTS (SELECT id FROM items WHERE owner_id = ?1 AND id = ?2)")
    boolean existsItemForUser(Long ownerId, Long itemId);
}
