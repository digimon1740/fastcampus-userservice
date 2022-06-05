-- 초기 세팅
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id         bigint NOT NULL AUTO_INCREMENT,
    email   varchar(100),
    username   varchar(50),
    password   varchar(500),
    created_at timestamp default NOW(),
    primary key (id)
);
