package ru.yandex.practicum.filmorate.storage.genre.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DbGenreStorage implements GenreDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> findAll() {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT* FROM genre ORDER BY genre_id ASC");
        List<Genre> genres = new ArrayList<>();
        while (genreRows.next()) {
            genres.add(createNewObjGenre(genreRows));
        }
        return genres;
    }

    @Override
    public Genre findGenreById(int id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT* FROM genre WHERE genre_id=?", id);
        if (genreRows.next()) {
            return createNewObjGenre(genreRows);
        }
        return null;
    }

    private Genre createNewObjGenre(SqlRowSet genreRows) {
        Genre genre = new Genre();
        genre.setId(genreRows.getInt("genre_id"));
        genre.setName(genreRows.getString("genre_name"));
        return genre;
    }
}
