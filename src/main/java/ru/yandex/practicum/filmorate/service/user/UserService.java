package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userDbStorage;

    public User create(User user) {
        switch (userDbStorage.create(user)) {
            case 1:
                return user;
            case -1:
                throw new BadRequestException("Пользователь с электронным адресом " + user.getEmail() +
                        " уже зарегистрирован");
            case -2:
                throw new BadRequestException("Пользователь с логином " + user.getLogin() +
                        " уже зарегистрирован");
            case 0:
                throw new BadRequestException("Ошибка добавлния данных в БД");
        }
        return user;
    }

    public User update(User user) {
        switch (userDbStorage.update(user)) {
            case 1:
                return user;
            case -1:
                throw new ObjNotFoundException("Пользователь для обновления не найден");
            case -2:
                throw new BadRequestException("Пользователь с электронным адресом " + user.getEmail() +
                        " уже зарегистрирован");
            case -3:
                throw new BadRequestException("Пользователь с логином " + user.getLogin() +
                        " уже зарегистрирован");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userDbStorage.findAll();
    }

    public User getUserById(int id) {
        User user = userDbStorage.findUserById(id);
        if (user == null) {
            throw new ObjNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public void addFriends(int idUser1, int idUser2) {
        if (idUser1 != idUser2) {
            int resAddFriend = userDbStorage.addFriend(idUser1, idUser2);
            if (resAddFriend == -1) throw new ObjNotFoundException("Один из пользователей не найден");
        } else {
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public void removeFriends(int idUser1, int idUser2) {
        if (idUser1 != idUser2) {
            userDbStorage.removeFriend(idUser1, idUser2);
        } else {
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public List<User> getFriends(int idUser) {
        return userDbStorage.getFriendsByUserId(idUser);
    }

    public List<User> getMutualFriends(int idUser1, int idUser2) {
        List<User> user1Friends = getFriends(idUser1);
        List<User> user2Friends = getFriends(idUser2);
        List<User> mutualFriends = new ArrayList<>();

        if (idUser1 != idUser2) {
            for (User u1 : user1Friends) {
                if (user2Friends.remove(u1)) mutualFriends.add(u1);
                if (user2Friends.isEmpty()) break;
            }
            return mutualFriends;
        } else {
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }
}
