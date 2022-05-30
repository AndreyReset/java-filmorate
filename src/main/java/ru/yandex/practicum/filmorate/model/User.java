package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private Long id;
    private Set<Long> friends = new HashSet<>();

    @NotBlank
    @Email(message = "E-mail не валиден")
    private String email;

    @Size(min = 4, message = "Логин должен содержать хотя бы 4 символа")
    @Pattern(regexp = "[a-zA-Z0-9_.]*",
            message = "Логин должен быть из латинских символов и/или цифр. " +
                    "Не должен содержать специальные знаки и пробелы")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    public boolean addFriend(Long id) {
        return friends.add(id);
    }

    public boolean removeFriend(Long id) {
        return friends.remove(id);
    }
}
