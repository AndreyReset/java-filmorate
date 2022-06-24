package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {

    private int id;

    @NotBlank
    @Email(message = "E-mail не валиден")
    private String email;

    @Size(min = 4, message = "Логин должен содержать хотя бы 4 символа")
    @Pattern(regexp = "[a-zA-Z0-9_.]*",
            message = "Логин должен быть из латинских символов и/или цифр. " +
                    "Не должен содержать специальные знаки и пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;
}
