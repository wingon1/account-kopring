-- liquibase formatted sql

-- changeset jihansol:001
-- comment: create user_roles table

CREATE TABLE `user_roles` (
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(20) NOT NULL COMMENT '사용자 ID',
    `role_id`     bigint(20) NOT NULL COMMENT '권한 ID',
    `modified_at` datetime                            DEFAULT NULL,
    `created_at`  datetime                            DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='사용자 권한';