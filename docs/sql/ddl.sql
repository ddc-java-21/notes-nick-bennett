-- Generated 2025-06-11 15:05:24-0600 for database version 1

CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`      INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `oauth_key`    TEXT                              NOT NULL,
    `display_name` TEXT                              NOT NULL COLLATE NOCASE,
    `created`      INTEGER                           NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_oauth_key` ON `user` (`oauth_key`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_display_name` ON `user` (`display_name`);

CREATE TABLE IF NOT EXISTS `note`
(
    `note_id`     INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `title`       TEXT                              NOT NULL COLLATE NOCASE,
    `description` TEXT,
    `created`     INTEGER                           NOT NULL,
    `modified`    INTEGER                           NOT NULL,
    `user_id`     INTEGER                           NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `index_note_title` ON `note` (`title`);

CREATE INDEX IF NOT EXISTS `index_note_created` ON `note` (`created`);

CREATE INDEX IF NOT EXISTS `index_note_modified` ON `note` (`modified`);

CREATE INDEX IF NOT EXISTS `index_note_user_id` ON `note` (`user_id`);

CREATE TABLE IF NOT EXISTS `image`
(
    `image_id`  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
    `caption`   TEXT COLLATE NOCASE,
    `mime_type` TEXT,
    `uri`       TEXT                              NOT NULL,
    `created`   INTEGER                           NOT NULL,
    `note_id`   INTEGER                           NOT NULL,
    FOREIGN KEY (`note_id`) REFERENCES `note` (`note_id`) ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `index_image_mime_type` ON `image` (`mime_type`);

CREATE INDEX IF NOT EXISTS `index_image_created` ON `image` (`created`);

CREATE INDEX IF NOT EXISTS `index_image_note_id` ON `image` (`note_id`);