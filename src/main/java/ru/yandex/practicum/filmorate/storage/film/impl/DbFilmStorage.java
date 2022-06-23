package ru.yandex.practicum.filmorate.storage.film.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.util.*;

@RequiredArgsConstructor
@Component
public class DbFilmStorage implements FilmDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(Film film) {
        if (film.getMpa() == null) return -3;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM movie " +
                "WHERE film_name = ? and film_release_date = ? LIMIT 1",
                film.getName(), film.getReleaseDate());
        if (!filmRows.next()) {
            if (jdbcTemplate.update("INSERT INTO movie " +
                    "(film_name, film_description, film_release_date, film_duration, film_count_likes, mpa_id) " +
                    "values (?,?,?,?,?,?)",
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    0,
                    film.getMpa().getId()) == 1) {
                SqlRowSet filmRows2 = jdbcTemplate.queryForRowSet(
                        "SELECT mov.*, mpa.* " +
                        "FROM movie AS mov " +
                        "LEFT JOIN mpa ON mov.mpa_id=mpa.mpa_id " +
                        "WHERE mov.film_name = ? and mov.film_release_date = ? LIMIT 1",
                        film.getName(), film.getReleaseDate());
                if (filmRows2.next()) {
                    film.setId(filmRows2.getInt("film_id"));
                    film.getMpa().setName(filmRows2.getString("mpa_name"));
                    film.getMpa().setDescription(filmRows2.getString("mpa_description"));
                    if (film.getGenres() != null) {
                        for (Genre g : film.getGenres()) {
                            jdbcTemplate.update("INSERT INTO genres (film_id, genre_id) values (?,?)",
                                    film.getId(), g.getId());
                        }
                    }
                    return 1;
                }
            }
            return -2;
        } else {
            return -1;
        }
    }

    @Override
    public int update(Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM movie " +
                "WHERE film_id = ? LIMIT 1", film.getId());
        if (filmRows.next()) {
            String sql = "UPDATE movie SET film_name=?, film_description=?, film_release_date=?, " +
                    "film_duration=?, mpa_id=? WHERE film_id=?";
            jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
            //обновление жанров
            //получаем данные о жанрах из БД
            List<Genre> genresFromDB = genresFromDb(film.getId());
            //если на обнавление подали нулевой список жанров или пустой
            if (film.getGenres() == null || film.getGenres().isEmpty()) {
                //удаляем из БД инфо о жанрах
                removeGenresFromDb(film.getId(), genresFromDB);
            } else {
                //формируем уникальный сет жанров из объекта обновления
                Set<Genre> genresFromObj = new HashSet<>(film.getGenres());
                //формируем список жанров для обновы в БД
                List<Genre> toUpdateGenre = new ArrayList<>();
                for (Genre g : genresFromObj) {
                    if (!genresFromDB.remove(g)) toUpdateGenre.add(g);
                }
                //удаляем из БД неактуальные жанры и добавляем новые
                removeGenresFromDb(film.getId(), genresFromDB);
                insertGenresToDb(film.getId(), toUpdateGenre);
                //получаем данные о жанрах из БД
                genresFromDB = genresFromDb(film.getId());
                //устанавливаем данные в объект для ответа
                film.setGenres(genresFromDB);
            }
            return 1;
        } else { //если фильм не найден
            return -1;
        }
    }

    private List<Genre> genresFromDb(int filmId) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT * FROM genres " +
                "WHERE film_id = ?", filmId);
        List<Genre> genresFromDB = new ArrayList<>();
        while(genreRows.next()) {
            Genre genre = new Genre();
            genre.setId(genreRows.getInt("genre_id"));
            genresFromDB.add(genre);
        }
        return genresFromDB;
    }

    private void removeGenresFromDb(int filmId, List<Genre> genresFromDB) {
        if (!genresFromDB.isEmpty()) {
            for (Genre g : genresFromDB) {
                String sql = "DELETE FROM genres WHERE film_id=? and genre_id=?";
                jdbcTemplate.update(sql, filmId, g.getId());
            }
        }
    }

    private void insertGenresToDb(int filmId, List<Genre> toUpdateGenre) {
        if (!toUpdateGenre.isEmpty()) {
            for (Genre g : toUpdateGenre) {
                String sql = "INSERT INTO genres (film_id, genre_id) VALUES (?,?)";
                jdbcTemplate.update(sql, filmId, g.getId());
            }
        }
    }

    @Override
    public List<Film> findAll() {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT mov.*, mpa.* FROM movie AS mov " +
                "LEFT JOIN mpa ON mov.mpa_id=mpa.mpa_id");
        List<Film> list = new ArrayList<>();
        while (filmRows.next()) {
            list.add(createNewObjFilm(filmRows));
        }
        return list;
    }

    @Override
    public Film findFilmById(int id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT mov.*, mpa.* FROM movie AS mov " +
                "LEFT JOIN mpa ON mov.mpa_id=mpa.mpa_id " +
                "WHERE mov.film_id=? LIMIT 1", id);
        if (filmRows.next()) {
            return createNewObjFilm(filmRows);
        }
        return null;
    }

    private Film createNewObjFilm(SqlRowSet filmRows) {
        Film film = new Film();
        film.setId(filmRows.getInt("film_id"));
        film.setName(filmRows.getString("film_name"));
        film.setDescription(filmRows.getString("film_description"));
        film.setReleaseDate(Objects.requireNonNull(filmRows.getDate("film_release_date")).toLocalDate());
        film.setDuration(filmRows.getInt("film_duration"));
        film.setCountLikes(filmRows.getInt("film_count_likes"));

        Mpa mpa = new Mpa();
        mpa.setId(filmRows.getInt("mpa_id"));
        mpa.setName(filmRows.getString("mpa_name"));
        mpa.setDescription(filmRows.getString("mpa_description"));
        film.setMpa(mpa);

        List<Genre> genres = new ArrayList<>();
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet("SELECT gs.*, ge.* FROM genres AS gs " +
                        "LEFT JOIN genre AS ge ON gs.genre_id=ge.genre_id " +
                        "WHERE gs.film_id=? ",
                film.getId());
        while (genresRows.next()) {
            Genre genre = new Genre();
            genre.setId(genresRows.getInt("genre_id"));
            genre.setName(genresRows.getString("genre_name"));
            genres.add(genre);
        }
        if (genres.isEmpty()) genres = null;
        film.setGenres(genres);

        return film;
    }

    @Override
    public int addLike(int idFilm, int idUser) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes " +
                        "WHERE likes_film_id=? and likes_user_id=?",
                        idFilm, idUser);
        if (!likesRows.next()) {
            String sql = "INSERT INTO likes (likes_film_id, likes_user_id) VALUES (?,?)";
            jdbcTemplate.update(sql, idFilm, idUser);
            sql = "UPDATE movie SET film_count_likes=film_count_likes+1 WHERE film_id=?";
            jdbcTemplate.update(sql, idFilm);
            return 1;
        }
        return 0;
    }

    @Override
    public int removeLike(int idFilm, int idUser) {
        SqlRowSet likesRows = jdbcTemplate.queryForRowSet("SELECT * FROM likes " +
                        "WHERE likes_film_id=? and likes_user_id=?",
                        idFilm, idUser);
        if (likesRows.next()) {
            String sql = "DELETE FROM likes WHERE likes_film_id=? and likes_user_id=?";
            jdbcTemplate.update(sql, idFilm, idUser);
            sql = "UPDATE movie SET film_count_likes=film_count_likes-1 WHERE film_id=?";
            jdbcTemplate.update(sql, idFilm);
            return 1;
        }
        return 0;
    }
}
