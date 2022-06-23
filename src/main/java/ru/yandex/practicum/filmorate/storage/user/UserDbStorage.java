package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDbStorage {

    int create(User user);

    int update(User user);

    List<User> findAll();

    User findUserById(int id);

    int addFriend(int userId, int friendId);

    int removeFriend(int userId, int friendId);

    List<User> getFriendsByUserId(int id);
}
