package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(value = "select b from Booking b where b.id = ?1 and (b.booker.id = ?2 or b.item.ownerId = ?2)")
    Optional<Booking> findByBookingId(@Param("bookingId") long bookingId, @Param("userId") long userId);

    List<Booking> findAllByBookerId(Long id);

    List<Booking> findAllByItemId(Long id);

    List<Booking> findAllByItemIdIn(List<Long> ids);

    List<Booking> findByBookerIdAndItemIdOrderByStart(long bookerId, long itemId);

    List<Booking> findAllByItemIdAndEndBeforeOrderByEndDesc(Long itemId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByEndDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfterOrderByEndDesc(long bookerId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStatusOrderByEndDesc(long bookerId, BookingStatus status);

    List<Booking> findAllByBookerIdOrderByEndDesc(long bookerId);

    @Query(value = "select b from Booking b where b.id = :bookerId and (:now between b.start and b.end) order by b.end desc ")
    List<Booking> findAllByBookerIdAndNowBetweenOrderByEndDesc(@Param("bookerId") long bookerId, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query(value = "update Booking b set b.status = ?3 where b.id = ?1 and b.item.ownerId = ?2")
    void updateBooking(@Param("bookingId") long bookingId, @Param("ownerId") long ownerId,
                       @Param("status") BookingStatus status);

    @Query(value = "select exists(select b.id from Booking b where b.id = ?1 and b.item.ownerId = ?2)")
    boolean existsByOwnerId(long bookingId, long ownerId);

    List<Booking> findAllByItemOwnerIdOrderByEndDesc(long ownerId);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByEndDesc(long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByEndDesc(long ownerId, LocalDateTime now);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByEndDesc(long ownerId, BookingStatus status);

    @Query(value = "select b from Booking b where b.item.id = ?1 and (?2 between b.start and b.end) order by b.end desc")
    List<Booking> findAllByItemOwnerIdAndNowBetweenOrderByEndDesc(@Param("ownerId") long ownerId, @Param("now") LocalDateTime now);
}
