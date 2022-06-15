package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.ObjNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        switch (userStorage.create(user)) {
            case 1:
                return user;
            case -1:
                throw new BadRequestException("Пользователь с электронным адресом " + user.getEmail() +
                        " уже зарегистрирован");
            case -2:
                throw new BadRequestException("Пользователь с логином " + user.getLogin() +
                        " уже зарегистрирован");
        }
        return user;
    }

    public User update(User user) {
        switch (userStorage.update(user)) {
            case 1:
                return user;
            case -1:
                throw new ObjNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    public Optional<User> getUserById(Long id) {
        Optional<User> user = userStorage.findAll().stream()
                .filter(u -> Objects.equals(u.getId(), id)).findFirst();
        if (user.isEmpty()) {
            throw new ObjNotFoundException("Пользователь не найден");
        }
        return user;
    }

    public void addFriends(Long idUser1, Long idUser2) {
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
                    break;
                }
            }
            if (user1 == null || user2 == null) {
                throw new ObjNotFoundException("Один из пользователей не найден");
            }
        } else {
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public void removeFriends(Long idUser1, Long idUser2) {
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
                    break;
                }
            }
            if (user1 == null || user2 == null) {
                throw new ObjNotFoundException("Один из пользователей не найден");
            }
        } else {
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }

    public List<User> getFriends(Long idUser) {
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
        throw new ObjNotFoundException("Пользователь с id: " + idUser + " не найден");
    }

    public List<User> getMutualFriends(Long idUser1, Long idUser2) {
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
            throw new BadRequestException("Id пользователя и Id друга не могут быть одинаковыми");
        }
    }
}
