package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film, HttpServletResponse response) {
        log.info("POST запрос на добавление фильма");
        return filmService.create(film, response);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film, HttpServletResponse response) {
        log.info("PUT запрос на обновление фильма");
        return filmService.update(film, response);
    }

    @GetMapping("/films/{id}")
    public Optional<Film> getFilmById(@PathVariable Long id, HttpServletResponse response) {
        log.info("GET запрос на получение фильма с id = " + id);
        return filmService.getFilmById(id, response);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLike(@PathVariable Long id,
                        @PathVariable Long userId,
                        HttpServletResponse response) {
        log.info("PUT запрос на лайк film : " + id + ", user : " + userId);
        return filmService.addLike(id, userId, response);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable Long id,
                        @PathVariable Long userId,
                        HttpServletResponse response) {
        log.info("DELETE запрос на лайк film : " + id + ", user : " + userId);
        return filmService.removeLike(id, userId, response);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required=true,defaultValue="10") int count) {
        log.info("GET запрос на получение популярных фильмов, список из " + count + " фильмов");
        return filmService.getPopularFilm(count);
    }
}
