package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping("/mpa")
    public List<Mpa> mpaAll() {
        log.info("GET запрос на получение списка всех возрастных рейтингов");
        return mpaService.findAll();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaById(@PathVariable int id) {
        log.info("GET запрос на получение возрастного рейтинга с id = " + id);
        return mpaService.findMpaById(id);
    }
}
