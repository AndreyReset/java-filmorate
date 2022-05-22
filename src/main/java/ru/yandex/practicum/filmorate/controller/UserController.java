package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class UserController {

    private List<User> users = new ArrayList<>();
    private int id = 0;

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
    public User update(@Valid @RequestBody User user) {
        log.info("PUT запрос на обновление пользователя");
        if (users.contains(user)) {
            log.info("Такой пользователь имеется, обновляю");
            if (user.getName().isEmpty()) user.setName(user.getLogin());
            users.set(users.indexOf(user), user);
        } else {
            log.info("Такого пользователя нет");
        }
        return user;
    }
}
