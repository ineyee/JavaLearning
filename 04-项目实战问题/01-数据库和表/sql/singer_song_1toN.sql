CREATE TABLE `singer`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `sex` varchar(100) NULL DEFAULT '',
  PRIMARY KEY (`id`)
);

CREATE TABLE `song`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `cover` varchar(100) NULL DEFAULT '',
  `singer_id` bigint NULL,
  PRIMARY KEY (`id`)
);

ALTER TABLE `song` ADD CONSTRAINT `fk_song_singer_1` FOREIGN KEY (`singer_id`) REFERENCES `singer` (`id`);

