package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.valid.After;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {

    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание должно быть менее 200 символов")
    private String description;

    @After(value = "1895-12-27", message = "Дата релиза не может быть раньше 28 декабря 1895 годя")
    private LocalDate releaseDate;

    @Min(value = 0, message = "Длительность фильма не может быть отрицательным значением")
    private int duration;
}
