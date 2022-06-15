package ru.yandex.practicum.filmorate.exception;

public class ObjNotFoundException extends RuntimeException {
    public ObjNotFoundException(String message) {
        super(message);
    }
}
