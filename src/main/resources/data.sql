INSERT INTO mpa (mpa_name, mpa_description)
VALUES ('G','У фильма нет возрастных ограничений');

INSERT INTO mpa (mpa_name, mpa_description)
VALUES ('PG','Детям рекомендуется смотреть фильм с родителями');

INSERT INTO mpa (mpa_name, mpa_description)
VALUES ('PG-13','Детям до 13 лет просмотр не желателен');

INSERT INTO mpa (mpa_name, mpa_description)
VALUES ('R','Лицам до 17 лет просматривать фильм можно только в присутствии взрослого');

INSERT INTO mpa (mpa_name, mpa_description)
VALUES ('NC-17','Лицам до 18 лет просмотр запрещён');

INSERT INTO genre (genre_name) VALUES ('Комедия');
INSERT INTO genre (genre_name) VALUES ('Драма');
INSERT INTO genre (genre_name) VALUES ('Мультфильм');
INSERT INTO genre (genre_name) VALUES ('Триллер');
INSERT INTO genre (genre_name) VALUES ('Документальный');
INSERT INTO genre (genre_name) VALUES ('Боевик');

INSERT INTO friends_status (friends_status_name) VALUES ('Запрос на добавление');
INSERT INTO friends_status (friends_status_name) VALUES ('Дружба подтверждена');

--для теста
--INSERT INTO users (user_login, user_email, user_name, user_birthday) VALUES ('dolore', 'dolore@mail.ru', 'dolore', '1999-04-30');