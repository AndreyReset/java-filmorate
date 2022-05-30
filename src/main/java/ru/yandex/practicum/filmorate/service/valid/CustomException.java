package ru.yandex.practicum.filmorate.service.valid;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
