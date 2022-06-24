package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    public Film create(Film film) {
        switch (filmDbStorage.create(film)) {
            case 1:
                return film;
            case -1:
                throw new BadRequestException("Фильм с названием " + film.getName() +
                        " и датой релиза " + film.getReleaseDate() + " уже добавлен");
        }
        return film;
    }

    public Film update(Film film) {
        switch (filmDbStorage.update(film)) {
            case 1:
                return film;
            case -1:
                throw new ObjNotFoundException("Фильм не найден");
        }
        return film;
    }

    public List<Film> getAllFilms() {
        return filmDbStorage.findAll();
    }

    public Film getFilmById(int id) {
        Film film = filmDbStorage.findFilmById(id);
        if (film == null)
            throw new ObjNotFoundException("Фильм не найден");
        return film;
    }

    public Film addLike(int idFilm, int idUser) {
        Film film = getFilmById(idFilm);
        User user = userDbStorage.findUserById(idUser);

        if (user != null) {
            film.setCountLikes(film.getCountLikes() + filmDbStorage.addLike(idFilm, idUser));
            return film;
        }
        throw new ObjNotFoundException("Пользователь не найден");
    }

    public Film removeLike(int idFilm, int idUser) {
        Film film = getFilmById(idFilm);
        User user = userDbStorage.findUserById(idUser);

        if (user != null) {
            film.setCountLikes(film.getCountLikes() - filmDbStorage.addLike(idFilm, idUser));
            return film;
        }
        throw new ObjNotFoundException("Пользователь не найден");
    }

    public List<Film> getPopularFilm(int count) {
        return filmDbStorage.findAll().stream()
                .sorted((o1, o2)->o2.getCountLikes()-o1.getCountLikes())
                .limit(count)
                .collect(Collectors.toList());
    }
}
