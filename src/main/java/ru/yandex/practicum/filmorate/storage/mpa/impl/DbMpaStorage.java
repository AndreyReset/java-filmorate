package ru.yandex.practicum.filmorate.storage.mpa.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class DbMpaStorage implements MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT* FROM mpa");
        List<Mpa> mpa = new ArrayList<>();
        while (mpaRows.next()) {
            mpa.add(createNewObjMpa(mpaRows));
        }
        return mpa;
    }

    @Override
    public Mpa findMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT* FROM mpa WHERE mpa_id=?", id);
        if (mpaRows.next()) {
            return createNewObjMpa(mpaRows);
        }
        return null;
    }

    private Mpa createNewObjMpa(SqlRowSet mpaRows) {
        Mpa mpa = new Mpa();
        mpa.setId(mpaRows.getInt("mpa_id"));
        mpa.setName(mpaRows.getString("mpa_name"));
        mpa.setDescription(mpaRows.getString("mpa_description"));
        return mpa;
    }
}
