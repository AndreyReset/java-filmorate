package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("GET запрос на получение списка всех фильмов");
        return filmService.getAllFilms();
    }
    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        log.info("POST запрос на добавление фильма");
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        log.info("PUT запрос на обновление фильма");
        return filmService.update(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("GET запрос на получение фильма с id = " + id);
        return filmService.getFilmById(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLike(@PathVariable int id,
                        @PathVariable int userId) {
        log.info("PUT запрос на лайк film : " + id + ", user : " + userId);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id,
                        @PathVariable int userId) {
        log.info("DELETE запрос на лайк film : " + id + ", user : " + userId);
        return filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required=true,defaultValue="10") int count) {
        log.info("GET запрос на получение популярных фильмов, список из " + count + " фильмов");
        return filmService.getPopularFilm(count);
    }
}
