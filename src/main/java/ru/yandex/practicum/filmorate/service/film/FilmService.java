package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        switch (filmStorage.create(film)) {
            case 1:
                return film;
            case -1:
                throw new BadRequestException("Фильм с названием " + film.getName() +
                        " и датой релиза " + film.getReleaseDate() + " уже добавлен");
        }
        return film;
    }

    public Film update(Film film) {
        switch (filmStorage.update(film)) {
            case 1:
                return film;
            case -1:
                throw new ObjNotFoundException("Фильм не найден");
        }
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Optional<Film> getFilmById(Long id) {
        Optional<Film> film = filmStorage.findAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
        if (film.isEmpty()) {
            throw new ObjNotFoundException("Фильм не найден");
        }
        return film;
    }

    public Film addLike(Long idFilm, Long idUser) {
        for (Film film : filmStorage.findAll()) {
            if (film.getId().equals(idFilm)) {
                for (User user : userStorage.findAll()) {
                    if (user.getId().equals(idUser)) {
                        film.addLike(idUser);
                        return film;
                    }
                }
                throw new ObjNotFoundException("Пользователь не найден");
            }
        }
        throw new ObjNotFoundException("Фильм не найден");
    }

    public Film removeLike(Long idFilm, Long idUser) {
        for (Film film : filmStorage.findAll()) {
            if (film.getId().equals(idFilm)) {
                for (User user : userStorage.findAll()) {
                    if (user.getId().equals(idUser)) {
                        film.removeLike(idUser);
                        return film;
                    }
                }
                throw new ObjNotFoundException("Пользователь не найден");
            }
        }
        throw new ObjNotFoundException("Фильм не найден");
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2)->o2.getLikes().size()-o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
