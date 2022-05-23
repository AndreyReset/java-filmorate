package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();
    private int id = 1;

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
    public Film update(@Valid @RequestBody Film film, HttpServletResponse response) {
        log.info("PUT запрос на обновление фильма");
        int i = 0;
        for (Film f : films) {
            if (f.getId().equals(film.getId())) {
                log.info("Такой фильм имеется, обновляю");
                films.set(i, film);
                response.setStatus(200);
                return film;
            }
            i += 1;
        }
        log.info("Такого фильма нет");
        response.setStatus(500);
        return film;
    }
}
