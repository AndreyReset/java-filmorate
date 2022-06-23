package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDbStorage {

    int create(Film film);

    int update(Film film);

    List<Film> findAll();

    Film findFilmById(int id);

    int addLike(int idFilm, int idUser);

    int removeLike(int idFilm, int idUser);
}
