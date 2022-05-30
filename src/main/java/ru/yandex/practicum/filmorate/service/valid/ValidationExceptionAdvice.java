package ru.yandex.practicum.filmorate.service.valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.*;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ValidationExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e
    ) {
        final List<errorNotification> errorNotifications = e.getConstraintViolations().stream()
                .map(
                        violation -> new errorNotification(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        return new ValidationErrorResponse(errorNotifications);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e
    ) {
        final List<errorNotification> errorNotifications = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new errorNotification(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.info(e.getMessage());
        return new ValidationErrorResponse(errorNotifications);
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ValidationErrorResponse CustomException(
            CustomException e
    ) {
        final List<errorNotification> errorNotifications = List.of(
                new errorNotification("", e.getMessage()));
        log.info(e.getMessage());
        return new ValidationErrorResponse(errorNotifications);
    }
}

