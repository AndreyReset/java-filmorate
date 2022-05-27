package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private List<User> users = new ArrayList<>();
    private int id = 1;

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("GET запрос на получение списка всех пользователей");
        return users;
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("POST запрос на создание пользователя");
        if (user.getName().isEmpty()) user.setName(user.getLogin());
        user.setId(id);
        users.add(user);
        id++;
        return user;
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user, HttpServletResponse response) {
        log.info("PUT запрос на обновление пользователя");
        int i = 0;
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                log.info("Такой пользователь имеется, обновляю");
                users.set(i, user);
                response.setStatus(200);
                return user;
            }
            i += 1;
        }
        log.info("Такого пользователя нет");
        response.setStatus(500);
        return user;
    }
}
