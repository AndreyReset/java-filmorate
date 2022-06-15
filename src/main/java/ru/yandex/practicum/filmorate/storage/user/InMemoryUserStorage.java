package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new ArrayList<>();
    private Long id = 1L;

    @Override
    public int create(User user) {
        if (user.getName().isEmpty()) user.setName(user.getLogin());
        for (User u : users) {
            if (u.getEmail().equals(user.getEmail())) {
                return -1;
            }
            if (u.getLogin().equals(user.getLogin())) {
                return -2;
            }
        }
        user.setId(id);
        users.add(user);
        id++;
        return 1;
    }

    @Override
    public int update(User user) {
        int i = 0;
        for (User u : users) {
            if (u.getId().equals(user.getId())) {
                users.set(i, user);
                return 1;
            }
            i += 1;
        }
        return -1;
    }

    @Override
    public List<User> findAll() {
        return users;
    }
}
