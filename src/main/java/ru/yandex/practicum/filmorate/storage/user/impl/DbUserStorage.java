package ru.yandex.practicum.filmorate.storage.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.util.*;

@RequiredArgsConstructor
@Component
public class DbUserStorage implements UserDbStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public int create(User user) {
        if (user.getName().isEmpty()) user.setName(user.getLogin());
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users " +
                        "WHERE user_login = ? or user_email = ? LIMIT 1",
                        user.getLogin(), user.getEmail());
        if (!userRows.next()) {
            String sql = "INSERT INTO users (user_login, user_email, user_name, user_birthday) " +
                    "VALUES (?,?,?,?)";
            if (jdbcTemplate.update(sql,
                    user.getLogin(),
                    user.getEmail(),
                    user.getName(),
                    user.getBirthday()) == 1) {
                userRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM users " +
                        "WHERE user_login = ? or user_email = ? LIMIT 1",
                        user.getLogin(), user.getEmail());
                if (userRows.next()) {
                    user.setId(userRows.getInt("user_id"));
                    return 1;
                }
            }
        } else {
            if (Objects.equals(userRows.getString("user_email"), user.getEmail())) return -1;
            if (Objects.equals(userRows.getString("user_login"), user.getLogin())) return -2;
        }
        return 0;
    }

    @Override
    public int update(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users " +
                "WHERE user_id = ? LIMIT 1", user.getId());
        if (userRows.next()) {
            //если обновление имэйла
            if (!user.getEmail().equals(userRows.getString("user_email"))) {
                SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("SELECT * FROM users " +
                                "WHERE user_email = ? LIMIT 1", user.getEmail());
                if (userRows2.next()) {
                    if (Objects.equals(userRows2.getString("user_email"), user.getEmail())) return -2;
                }
            }
            //если обновление логина
            if (!user.getLogin().equals(userRows.getString("user_login"))) {
                SqlRowSet userRows2 = jdbcTemplate.queryForRowSet("SELECT * FROM users " +
                                "WHERE user_login = ? LIMIT 1", user.getLogin());
                if (userRows2.next()) {
                    if (Objects.equals(userRows2.getString("user_login"), user.getLogin())) return -3;
                }
            }

            String sql = "UPDATE users SET user_login=?, user_email=?, user_name=?,user_birthday=? WHERE user_id=?";
            if (jdbcTemplate.update(sql,
                    user.getLogin(),
                    user.getEmail(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId()) == 1) {
                return 1;
            }
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users");
        while (userRows.next()) {
            users.add(createNewObjUser(userRows));
        }
        return users;
    }

    @Override
    public User findUserById(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id=?", id);
        if (userRows.next()) {
            return createNewObjUser(userRows);
        }
        return null;
    }

    private User createNewObjUser(SqlRowSet userRows) {
        User user = new User();
        user.setId(userRows.getInt("user_id"));
        user.setLogin(userRows.getString("user_login"));
        user.setEmail(userRows.getString("user_email"));
        user.setName(userRows.getString("user_name"));
        user.setBirthday(Objects.requireNonNull(userRows.getDate("user_birthday")).toLocalDate());
        return user;
    }

    @Override
    public int addFriend(int userId, int friendId) {
        //проверка наличие пользователей в БД
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT user_id FROM users " +
                                    "WHERE user_id=? or user_id=? LIMIT 2",
                                    userId, friendId);
        if (userRows.last()) {
            if (userRows.getRow() != 2)
                return -1;
        } else {
            return -1;
        }
        //был ли ранее запрос на дружбу и не является ли запрос ответом
        userRows = jdbcTemplate.queryForRowSet("SELECT fr.*, fs.* FROM friends AS fr " +
                        "LEFT JOIN friends_status AS fs ON fr.status_id=fs.status_id " +
                        "WHERE (fr.friends_user_id=? and fr.friends_friend_id=?) or (fr.friends_user_id=? and fr.friends_friend_id=?) ",
                        userId, friendId, friendId, userId);

        if (userRows.next()) {
            if (userId == userRows.getInt("friends_user_id")
                    && friendId == userRows.getInt("friends_friend_id")) {
                //запрос на дружбу ранее был отправлен
                return 1;
            } else if (userId == userRows.getInt("friends_friend_id")
                    && friendId == userRows.getInt("friends_user_id")) {
                //запрос на дружбу является ответом на первый запрос дружбы, меняем статус дружбы
                if (jdbcTemplate.update("UPDATE friends SET status_id=2 " +
                        "WHERE friends_user_id=? and friends_friend_id=?", friendId, userId) == 1) {
                    return 1;
                }
            }
        } else { //если ранее запросов на дружбу не было
            if (jdbcTemplate.update("INSERT INTO friends (friends_user_id, friends_friend_id, status_id) " +
                    "VALUES (?,?,1) ", userId, friendId) == 1) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public int removeFriend(int userId, int friendId) {
        if (jdbcTemplate.update("DELETE FROM friends " +
                        "WHERE (friends_user_id=? and friends_friend_id=?) " +
                        "or (friends_user_id=? and friends_friend_id=?)",
                userId, friendId, friendId, userId) == 1) return 1;
        return -1;
    }

    @Override
    public List<User> getFriendsByUserId(int id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM friends AS fr " +
                "LEFT JOIN users AS us ON (fr.friends_friend_id=us.user_id and fr.friends_user_id=?) " +
                "or (fr.friends_user_id=us.user_id and fr.friends_friend_id=?)" +
                "WHERE fr.friends_user_id=? or fr.friends_friend_id=?", id, id, id, id);
        List<User> friends = new ArrayList<>();

        while (userRows.next()) {
            if (userRows.getInt("friends_user_id") == id ||
                    userRows.getInt("friends_friend_id") == id &&
                            userRows.getInt("status_id") == 2) {
                friends.add(createNewObjUser(userRows));
            }
        }
        return friends;
    }
}
