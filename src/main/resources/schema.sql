CREATE TABLE IF NOT EXISTS movie (
    film_id INTEGER auto_increment primary key,
    film_name VARCHAR(100),
    film_description VARCHAR(200),
    film_release_date DATE,
    film_duration INT,
    film_count_likes INT DEFAULT '0',
    mpa_id INT
);

CREATE TABLE IF NOT EXISTS likes (
    likes_film_id INTEGER   NOT NULL,
    likes_user_id INTEGER   NOT NULL,
    CONSTRAINT pk_likes PRIMARY KEY (
        likes_film_id,likes_user_id
     )
);

CREATE TABLE IF NOT EXISTS users (
    user_id integer auto_increment primary key,
    user_login VARCHAR(50)   NOT NULL,
    user_email VARCHAR(100)   NOT NULL,
    user_name VARCHAR(100)   NOT NULL,
    user_birthday DATE   NOT NULL,
    CONSTRAINT uc_users_login UNIQUE (
        user_login
    ),
    CONSTRAINT uc_users_email UNIQUE (
        user_email
    )
);

CREATE TABLE IF NOT EXISTS friends (
    friends_user_id INTEGER   NOT NULL,
    friends_friend_id INTEGER   NOT NULL,
    status_id INTEGER   NOT NULL,
    CONSTRAINT pk_friends PRIMARY KEY (
        friends_user_id,friends_friend_id
     )
);

CREATE TABLE IF NOT EXISTS friends_status (
    status_id INTEGER auto_increment primary key,
    friends_status_name VARCHAR(50)   NOT NULL,
    CONSTRAINT uc_friends_status_name UNIQUE (
        friends_status_name
    )
);

CREATE TABLE IF NOT EXISTS genres (
    film_id INTEGER   NOT NULL,
    genre_id INTEGER   NOT NULL,
    CONSTRAINT pk_genres PRIMARY KEY (
        film_id,genre_id
     )
);

CREATE TABLE IF NOT EXISTS genre (
    genre_id INTEGER auto_increment primary key,
    genre_name VARCHAR(100)   NOT NULL,
    CONSTRAINT uc_genre_name UNIQUE (
        genre_name
    )
);

CREATE TABLE IF NOT EXISTS mpa (
    mpa_id INTEGER auto_increment primary key,
    mpa_name VARCHAR(15)   NOT NULL,
    mpa_description VARCHAR(200),
    CONSTRAINT uc_mpa_name UNIQUE (
        mpa_name
    )
);

ALTER TABLE movie ADD CONSTRAINT IF NOT EXISTS fk_movie_mpa_id FOREIGN KEY(mpa_id)
REFERENCES mpa (mpa_id);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_film_id FOREIGN KEY(likes_film_id)
REFERENCES movie (film_id);

ALTER TABLE likes ADD CONSTRAINT IF NOT EXISTS fk_likes_user_id FOREIGN KEY(likes_user_id)
REFERENCES users (user_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_user_id FOREIGN KEY(friends_user_id)
REFERENCES users (user_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_friend_id FOREIGN KEY(friends_friend_id)
REFERENCES users (user_id);

ALTER TABLE friends ADD CONSTRAINT IF NOT EXISTS fk_friends_status_id FOREIGN KEY(status_id)
REFERENCES friends_status (status_id);

ALTER TABLE genres ADD CONSTRAINT IF NOT EXISTS fk_genres_film_id FOREIGN KEY(film_id)
REFERENCES movie (film_id);

ALTER TABLE genres ADD CONSTRAINT IF NOT EXISTS fk_genres_genre_id FOREIGN KEY(genre_id)
REFERENCES genre (genre_id);

