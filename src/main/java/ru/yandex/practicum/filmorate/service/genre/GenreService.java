package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> findAll() {
        return genreDbStorage.findAll();
    }

    public Genre findGenreById(int id) {
        Genre genre = genreDbStorage.findGenreById(id);
        if (genre == null)
            throw new ObjNotFoundException("Жанр не найден");
        return genre;
    }
}