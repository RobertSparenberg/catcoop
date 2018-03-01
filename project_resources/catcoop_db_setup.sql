CREATE DATABASE `catcoop` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE TABLE `images` (`id` INT NOT NULL AUTO_INCREMENT, `data` BLOB NOT NULL, `timestamp` BIGINT NOT NULL, PRIMARY KEY (`id`));
CREATE TABLE `settings` (`id` INT NOT NULL AUTO_INCREMENT, `setting` VARCHAR(32)  NOT NULL, `value` VARCHAR(64) NOT NULL, PRIMARY KEY (`id`), UNIQUE (`setting`));
CREATE TABLE `access` (`id` INT NOT NULL AUTO_INCREMENT, `cat` VARCHAR(32) NOT NULL, `timestamp` BIGINT NOT NULL, PRIMARY KEY (`id`));
CREATE TABLE `access_images` (`access_fk` INT NOT NULL, `image_fk` INT NOT NULL, FOREIGN KEY (`access_fk`) REFERENCES `access`(`id`), FOREIGN KEY (`image_fk`) REFERENCES `images` (`id`));
