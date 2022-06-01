package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/users")
    public List<User> findAll() {
        log.info("GET запрос на получение списка всех пользователей");
        return userService.getAllUsers();
    }

    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("POST запрос на создание пользователя");
        return userService.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@Valid @RequestBody User user) {
        log.info("PUT запрос на обновление пользователя");
        return userService.update(user);
    }

    @GetMapping("/users/{id}")
    public Optional<User> getUserById(@PathVariable Long id) {
        log.info("GET запрос на получение данных пользователя с id = " + id);
        return userService.getUserById(id);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable Long id,
                           @PathVariable Long friendId) {
        log.info("PUT запрос на добавление в друзья user1 : " + id + ", user2 : " + friendId);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable Long id,
                              @PathVariable Long friendId) {
        log.info("DELETE запрос на удаление из друзей user1 : " + id + ", user2 : " + friendId);
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("GET запрос на получение списка друзей у пользователя с id = " + id);
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable Long id,
                                       @PathVariable Long otherId) {
        log.info("GET запрос на получение списка друзей у пользователя с id = " + id);
        return userService.getMutualFriends(id, otherId);
    }
}
