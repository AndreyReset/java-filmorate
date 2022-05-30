package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.valid.CustomException;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film, HttpServletResponse response) {
        switch (filmStorage.create(film)) {
            case 1:
                response.setStatus(201);
                break;
            case -1:
                response.setStatus(400);
                throw new CustomException("Фильм с названием " + film.getName() +
                        " и датой релиза " + film.getReleaseDate() + " уже добавлен");
        }
        return film;
    }

    public Film update(Film film, HttpServletResponse response) {
        switch (filmStorage.update(film)) {
            case 1:
                response.setStatus(200);
                break;
            case -1:
                response.setStatus(404);
                throw new CustomException("Фильм не найден");
        }
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.findAll();
    }

    public Optional<Film> getFilmById(Long id, HttpServletResponse response) {
        Optional<Film> film = filmStorage.findAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
        if (film.isEmpty()) {
            response.setStatus(404);
            throw new CustomException("Фильм не найден");
        }
        return film;
    }

    public Film addLike(Long idFilm, Long idUser, HttpServletResponse response) {
        for (Film film : filmStorage.findAll()) {
            if (film.getId().equals(idFilm)) {
                for (User user : userStorage.findAll()) {
                    if (user.getId().equals(idUser)) {
                        film.addLike(idUser);
                        response.setStatus(200);
                        return film;
                    }
                }
                response.setStatus(404);
                throw new CustomException("Пользователь не найден");
            }
        }
        response.setStatus(404);
        throw new CustomException("Фильм не найден");
    }

    public Film removeLike(Long idFilm, Long idUser, HttpServletResponse response) {
        for (Film film : filmStorage.findAll()) {
            if (film.getId().equals(idFilm)) {
                for (User user : userStorage.findAll()) {
                    if (user.getId().equals(idUser)) {
                        film.removeLike(idUser);
                        response.setStatus(200);
                        return film;
                    }
                }
                response.setStatus(404);
                throw new CustomException("Пользователь не найден");
            }
        }
        response.setStatus(404);
        throw new CustomException("Фильм не найден");
    }

    public List<Film> getPopularFilm(int count) {
        return filmStorage.findAll().stream()
                .sorted((o1, o2)->o2.getLikes().size()-o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
