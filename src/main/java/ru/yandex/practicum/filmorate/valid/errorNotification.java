package ru.yandex.practicum.filmorate.valid;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class errorNotification {

    private final String fieldName;
    private final String message;
}
