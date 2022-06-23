package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDbStorage {

    List<Genre> findAll();

    Genre findGenreById(int id);
}
