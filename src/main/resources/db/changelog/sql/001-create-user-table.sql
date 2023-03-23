-- liquibase formatted sql

-- changeset jihansol:001
-- comment: create user table

CREATE TABLE `user` (
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `email`       varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '이메일',
    `password`    varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '비밀번호',
    `name`        varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '이름',
    `phone`       varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '전화번호',
    `modified_at` datetime                            DEFAULT NULL,
    `created_at`  datetime                            DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='사용자';