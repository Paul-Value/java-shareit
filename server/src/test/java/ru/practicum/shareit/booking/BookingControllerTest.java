package ru.practicum.shareit.booking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.exception.BookingAccessException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnavailableItemException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @__(@Autowired))
class BookingControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final String requestHeader = "X-Sharer-User-Id";
    @MockBean
    private BookingServiceImpl bookingService;
    private BookingCreateDto dtoCreate = new BookingCreateDto();
    private long itemId = 1;
    private long userId = 1;
    private long bookingId = 1;
    private String baseUri = "/bookings";

    @BeforeEach
    void setUp() {
        dtoCreate.setItemId(1L);
        dtoCreate.setStart(LocalDateTime.now().plusHours(1));
        dtoCreate.setEnd(LocalDateTime.now().plusHours(2));
    }

    ResultActions performMvc(MockHttpServletRequestBuilder builder) throws Exception {
        return mvc.perform(builder);
    }

    MockHttpServletRequestBuilder setRequestHeaders(MockHttpServletRequestBuilder builder) throws JsonProcessingException {
        return builder
                .content(mapper.writeValueAsString(dtoCreate))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .accept(MediaType.APPLICATION_JSON)
                .header(requestHeader, userId);
    }

    @Test
    void create() throws Exception {
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isCreated());
    }

    @Test
    void updateStatusBooking() throws Exception {
        performMvc(setRequestHeaders(patch(baseUri + "/" + bookingId + "?approved=true"))).andExpect(status().isOk());
    }

    @Test
    void getBooking() throws Exception {
        performMvc(setRequestHeaders(get(baseUri + "/" + bookingId))).andExpect(status().isOk());
    }

    @Test
    void getAllByBooker() throws Exception {
        performMvc(setRequestHeaders(get(baseUri))).andExpect(status().isOk());
    }

    @Test
    void getAllByOwner() throws Exception {
        performMvc(setRequestHeaders(get(baseUri + "/owner"))).andExpect(status().isOk());
    }

    @Test
    void createBookingUnavailableItem() throws Exception {
        when(bookingService.save(any(BookingCreateDto.class)))
                .thenThrow(new UnavailableItemException("Item is not available, id = " + itemId));
        performMvc(setRequestHeaders(post(baseUri))).andExpect(status().isBadRequest());
    }

    @Test
    void updateBookingApproveByWrongUser() throws Exception {
        when(bookingService.updateStatusBooking(bookingId, userId, true))
                .thenThrow(new BookingAccessException("User with id " + userId + " not owner of booking with id " + bookingId));
        performMvc(setRequestHeaders(patch(baseUri + "/" + bookingId + "?approved=true")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllByOwnerFromWrongUser() throws Exception {
        when(bookingService.getAllByOwner(anyLong(), any(BookingState.class)))
                .thenThrow(new NotFoundException("User not found with id: " + userId));
        performMvc(setRequestHeaders(get(baseUri + "/owner"))).andExpect(status().isNotFound());
    }
}