package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new ArrayList<>();
    private Long id = 1L;

    @Override
    public int create(Film film) {
        if (films.stream()
                .filter(f -> f.getName().equals(film.getName()))
                .noneMatch(f -> f.getReleaseDate().isEqual(film.getReleaseDate()))) {
            film.setId(id);
            films.add(film);
            id++;
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public int update(Film film) {
        int i = 0;
        for (Film f : films) {
            if (f.getId().equals(film.getId())) {
                films.set(i, film);
                return 1;
            }
            i += 1;
        }
        return -1;
    }

    @Override
    public List<Film> findAll() {
        return films;
    }
}
