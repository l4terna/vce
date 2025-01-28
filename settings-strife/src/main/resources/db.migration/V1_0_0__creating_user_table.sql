CREATE TABLE user
(
    id       VARCHAR(40) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    number   VARCHAR(12)  NOT NULL,
    password VARCHAR(40)  NOT NULL
);