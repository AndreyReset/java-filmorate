package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.model.ErrorNotification;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ValidationErrorResponse {

    private final List<ErrorNotification> ErrorNotifications;

}
