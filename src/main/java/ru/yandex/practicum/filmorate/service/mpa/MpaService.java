package ru.yandex.practicum.filmorate.service.mpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaDbStorage mpaDbStorage;

    public List<Mpa> findAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa findMpaById(int id) {
        Mpa mpa = mpaDbStorage.findMpaById(id);
        if (mpa == null)
            throw new ObjNotFoundException("Возрастной рейтинг не найден");
        return mpa;
    }
}
