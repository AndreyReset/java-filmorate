package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();
    private int id = 0;

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма");
        film.setId(id);
        films.add(film);
        id++;
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма");
        if (films.contains(film)) {
            log.info("Такой фильм имеется, обновляю");
            films.set(films.indexOf(film), film);
        } else {
            log.info("Такого фильма нет");
        }
        return film;
    }
}
