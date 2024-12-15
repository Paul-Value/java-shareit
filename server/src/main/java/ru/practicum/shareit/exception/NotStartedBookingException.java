package ru.practicum.shareit.exception;

public class NotStartedBookingException extends RuntimeException {
    public NotStartedBookingException(String message) {
        super(message);
    }
}
