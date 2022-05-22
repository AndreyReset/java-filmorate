package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.valid.After;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

@Data
public class Film {

    private Integer id;

    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @Size(max = 200, message = "Описание должно быть менее 200 символов")
    private String description;

    @After(value = "1895-12-27", message = "Дата релиза не может быть раньше 28 декабря 1895 годя")
    private LocalDate releaseDate;

    @DurationMin(message = "Длительность фильма не может быть отрицательным значением")
    private Duration duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return id.equals(film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
