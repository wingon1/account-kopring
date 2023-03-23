-- liquibase formatted sql

-- changeset jihansol:001
-- comment: create roles table

CREATE TABLE `roles` (
    `id`          bigint(20) NOT NULL AUTO_INCREMENT,
    `name`        varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '이름',
    `modified_at` datetime                            DEFAULT NULL,
    `created_at`  datetime                            DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='권한';


-- changeset jihansol:002
-- comment: add default roles

INSERT INTO `roles` (`name`, `modified_at`, `created_at`) VALUES ('ROLE_USER', '2023-03-06 10:41:38', '2023-03-06 10:41:38');
INSERT INTO `roles` (`name`, `modified_at`, `created_at`) VALUES ('ROLE_ADMIN', '2023-03-06 10:41:38', '2023-03-06 10:41:38');