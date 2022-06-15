package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    int create(Film film);

    int update(Film film);

    List<Film> findAll();
}
