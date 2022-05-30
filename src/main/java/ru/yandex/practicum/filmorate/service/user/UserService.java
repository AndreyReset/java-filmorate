package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.valid.CustomException;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User create(User user, HttpServletResponse response) {
        switch (userStorage.create(user)) {
            case 1:
                response.setStatus(201);
                break;
            case -1:
                response.setStatus(400);
                throw new CustomException("Пользователь с электронным адресом " + user.getEmail() +
                                " уже зарегистрирован");
            case -2:
                response.setStatus(400);
                throw new CustomException("Пользователь с логином " + user.getLogin() +
                                " уже зарегистрирован");
        }
        return user;
    }

    public User update(User user, HttpServletResponse response) {
        switch (userStorage.update(user)) {
            case 1:
                response.setStatus(200);
                break;
            case -1:
                response.setStatus(404);
                throw new CustomException("Пользователь не найден");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public Optional<User> getUserById(Long id, HttpServletResponse response) {
        Optional<User> user = userStorage.findAll().stream()
                .filter(u -> Objects.equals(u.getId(), id)).findFirst();
        if (user.isEmpty()) {
            response.setStatus(404);
            throw new CustomException("Пользователь не найден");
        }
        return user;
    }

    public void addFriends(Long idUser1, Long idUser2, HttpServletResponse response) {
        User user1 = null;
        User user2 = null;
        if (!Objects.equals(idUser1, idUser2)) {
            for (User user : userStorage.findAll()) {
                if (user.getId().equals(idUser1)) {
                    user1 = user;
                }
                if (user.getId().equals(idUser2)) {
                    user2 = user;
                }
                if (user1 != null && user2 != null) {
                    user1.addFriend(user2.getId());
                    user2.addFriend(user1.getId());
                    response.setStatus(200);
                    break;
                }
            }
            if (user1 == null || user2 == null) {
                response.setStatus(404);
                throw new CustomException("Один из пользователей не найден");
            }
        } else {
            response.setStatus(400);
            throw new CustomException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public void removeFriends(Long idUser1, Long idUser2, HttpServletResponse response) {
        User user1 = null;
        User user2 = null;
        if (!Objects.equals(idUser1, idUser2)) {
            for (User user : userStorage.findAll()) {
                if (user.getId().equals(idUser1)) {
                    user1 = user;
                }
                if (user.getId().equals(idUser2)) {
                    user2 = user;
                }
                if (user1 != null && user2 != null) {
                    user1.removeFriend(idUser2);
                    user2.removeFriend(idUser1);
                    response.setStatus(200);
                    break;
                }
            }
            if (user1 == null || user2 == null) {
                response.setStatus(404);
                throw new CustomException("Один из пользователей не найден");
            }
        } else {
            response.setStatus(400);
            throw new CustomException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public List<User> getFriends(Long idUser, HttpServletResponse response) {
        Set<Long> friendsId;
        List<User> friends = new ArrayList<>();
        for (User user : userStorage.findAll()) {
            if (user.getId().equals(idUser)) {
                friendsId = user.getFriends();
                for (User user2 : userStorage.findAll()) {
                    for (Long id : friendsId) {
                        if (Objects.equals(user2.getId(), id)) {
                            friends.add(user2);
                        }
                    }
                }
                return friends;
            }
        }
        response.setStatus(404);
        throw new CustomException("Пользователь с id: " + idUser + " не найден");
    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2, HttpServletResponse response) {
        Set<Long> user1Friends = new HashSet<>();
        Set<Long> user2Friends = new HashSet<>();
        Set<Long> idMutualFriends = new HashSet<>();
        List<User> mutualFriends = new ArrayList<>();

        if (!Objects.equals(idUser1, idUser2)) {
            for (User user : userStorage.findAll()) {
                if (user.getId().equals(idUser1)) {
                    user1Friends = user.getFriends();
                }
                if (user.getId().equals(idUser2)) {
                    user2Friends = user.getFriends();
                }
                if (user1Friends.size() > 0 && user2Friends.size() > 0) break;
            }
            if (user1Friends.size() > 0 && user2Friends.size() > 0) {
                for (Long id : user1Friends) {
                    if (user2Friends.contains(id)) {
                        idMutualFriends.add(id);
                    }
                }
            }
            for (User user : userStorage.findAll()) {
                for (Long id : idMutualFriends) {
                    if (Objects.equals(id, user.getId())) {
                        mutualFriends.add(user);
                    }
                }
            }
            return mutualFriends;
        } else {
            response.setStatus(400);
            throw new CustomException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }
}
