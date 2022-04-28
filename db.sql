-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        10.6.5-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  11.3.0.6295
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- project_instagram 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `project_instagram` /*!40100 DEFAULT CHARACTER SET utf8mb3 */;
USE `project_instagram`;

-- 테이블 project_instagram.activity_logs 구조 내보내기
DROP TABLE IF EXISTS `activity_logs`;
CREATE TABLE IF NOT EXISTS `activity_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `related_user_id` int(11) NOT NULL,
  `activity_flag` varchar(20) NOT NULL,
  `activity_message` varchar(300) NOT NULL,
  `article_id` int(11) DEFAULT NULL,
  `comment_id` int(11) DEFAULT NULL,
  `follow_id` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_did_user` (`user_id`),
  KEY `user_id_for_related_user` (`related_user_id`),
  KEY `article_id_for_activity` (`article_id`),
  KEY `comment_id_for_activity` (`comment_id`),
  KEY `follow_id_for_activity` (`follow_id`),
  CONSTRAINT `article_id_for_activity` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `comment_id_for_activity` FOREIGN KEY (`comment_id`) REFERENCES `article_comment` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `follow_id_for_activity` FOREIGN KEY (`follow_id`) REFERENCES `follow_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_did_user` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_related_user` FOREIGN KEY (`related_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.activity_logs:~0 rows (대략적) 내보내기
DELETE FROM `activity_logs`;
/*!40000 ALTER TABLE `activity_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_logs` ENABLE KEYS */;

-- 테이블 project_instagram.article_comment 구조 내보내기
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE IF NOT EXISTS `article_comment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `commented_user_id` int(11) NOT NULL,
  `contents` varchar(300) NOT NULL DEFAULT '',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0,
  `deleted_date` datetime DEFAULT NULL,
  `related_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: not-related / 1: related',
  `related_comment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_comment` (`article_id`),
  KEY `commented_user_id` (`commented_user_id`),
  KEY `related_comment_id` (`related_comment_id`),
  CONSTRAINT `article_id_for_comment` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `commented_user_id` FOREIGN KEY (`commented_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `related_comment_id` FOREIGN KEY (`related_comment_id`) REFERENCES `article_comment` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_comment:~0 rows (대략적) 내보내기
DELETE FROM `article_comment`;
/*!40000 ALTER TABLE `article_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_comment` ENABLE KEYS */;

-- 테이블 project_instagram.article_comment_reaction 구조 내보내기
DROP TABLE IF EXISTS `article_comment_reaction`;
CREATE TABLE IF NOT EXISTS `article_comment_reaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_comment_id` int(11) NOT NULL,
  `like_user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_comment_id_for_reaction` (`article_comment_id`),
  KEY `like_user_id_for_comment_reaction` (`like_user_id`),
  CONSTRAINT `article_comment_id_for_reaction` FOREIGN KEY (`article_comment_id`) REFERENCES `article_comment` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `like_user_id_for_comment_reaction` FOREIGN KEY (`like_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_comment_reaction:~0 rows (대략적) 내보내기
DELETE FROM `article_comment_reaction`;
/*!40000 ALTER TABLE `article_comment_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_comment_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.article_media 구조 내보내기
DROP TABLE IF EXISTS `article_media`;
CREATE TABLE IF NOT EXISTS `article_media` (
  `id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `media_name` varchar(100) NOT NULL DEFAULT '',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_media` (`article_id`),
  CONSTRAINT `article_id_for_media` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_media:~0 rows (대략적) 내보내기
DELETE FROM `article_media`;
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
INSERT INTO `article_media` (`id`, `article_id`, `media_name`, `create_date`, `update_date`) VALUES
	(1, 1, 'media01.webp', '2022-04-15 21:32:13', '2022-04-15 21:32:13');
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;

-- 테이블 project_instagram.article_mst 구조 내보내기
DROP TABLE IF EXISTS `article_mst`;
CREATE TABLE IF NOT EXISTS `article_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `feature` varchar(50) DEFAULT NULL COMMENT 'place_name / sound_included etc',
  `media_type` varchar(5) NOT NULL,
  `contents` varchar(300) NOT NULL DEFAULT '',
  `is_stored` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: public / 1: private',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_article` (`user_id`),
  CONSTRAINT `user_id_for_article` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_mst:~0 rows (대략적) 내보내기
DELETE FROM `article_mst`;
/*!40000 ALTER TABLE `article_mst` DISABLE KEYS */;
INSERT INTO `article_mst` (`id`, `user_id`, `feature`, `media_type`, `contents`, `is_stored`, `create_date`, `update_date`) VALUES
	(1, 2, NULL, 'webp', 'test', 0, '2022-04-15 21:31:28', '2022-04-15 21:31:28');
/*!40000 ALTER TABLE `article_mst` ENABLE KEYS */;

-- 테이블 project_instagram.article_reaction 구조 내보내기
DROP TABLE IF EXISTS `article_reaction`;
CREATE TABLE IF NOT EXISTS `article_reaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `like_user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_like` (`article_id`),
  KEY `like_user_id` (`like_user_id`),
  CONSTRAINT `article_id_for_like` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `like_user_id` FOREIGN KEY (`like_user_id`) REFERENCES `user_mst` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_reaction:~0 rows (대략적) 내보내기
DELETE FROM `article_reaction`;
/*!40000 ALTER TABLE `article_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.follow_mst 구조 내보내기
DROP TABLE IF EXISTS `follow_mst`;
CREATE TABLE IF NOT EXISTS `follow_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `partner_user_id` int(11) NOT NULL,
  `follower_group` varchar(20) NOT NULL DEFAULT 'COMMON',
  `follow_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_follow` (`user_id`),
  KEY `partner_id_for_follow` (`partner_user_id`),
  CONSTRAINT `partner_id_for_follow` FOREIGN KEY (`partner_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_follow` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.follow_mst:~0 rows (대략적) 내보내기
DELETE FROM `follow_mst`;
/*!40000 ALTER TABLE `follow_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `follow_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_mst 구조 내보내기
DROP TABLE IF EXISTS `user_mst`;
CREATE TABLE IF NOT EXISTS `user_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(50) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL DEFAULT '',
  `phone` varchar(20) NOT NULL DEFAULT '',
  `website` varchar(50) DEFAULT '',
  `description` varchar(200) DEFAULT '',
  `gender` tinyint(1) NOT NULL DEFAULT 3 COMMENT '0: male / 1: female / 2: both / 3: not-to-know',
  `has_profile_image` tinyint(1) unsigned zerofill NOT NULL DEFAULT 0 COMMENT '0: none / 1: user has a profile image',
  `last_username_update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `disable_flag` tinyint(1) unsigned zerofill NOT NULL DEFAULT 0 COMMENT '0: active / 1: disabled',
  `disable_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb3;

-- 테이블 project_instagram.user_profile_image 구조 내보내기
DROP TABLE IF EXISTS `user_profile_image`;
CREATE TABLE IF NOT EXISTS `user_profile_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_name` varchar(50) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_profile_image` (`user_id`),
  CONSTRAINT `user_id_for_profile_image` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_profile_image:~1 rows (대략적) 내보내기
DELETE FROM `user_profile_image`;
/*!40000 ALTER TABLE `user_profile_image` DISABLE KEYS */;
INSERT INTO `user_profile_image` (`id`, `user_id`, `file_name`, `create_date`, `update_date`) VALUES
	(1, 1, NULL, '2022-04-28 12:34:57', '2022-04-28 12:34:57');
/*!40000 ALTER TABLE `user_profile_image` ENABLE KEYS */;

-- 테이블 project_instagram.user_story_mst 구조 내보내기
DROP TABLE IF EXISTS `user_story_mst`;
CREATE TABLE IF NOT EXISTS `user_story_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `media_type` varchar(5) NOT NULL,
  `file_name` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_story` (`user_id`),
  CONSTRAINT `user_id_for_story` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_story_mst:~0 rows (대략적) 내보내기
DELETE FROM `user_story_mst`;
/*!40000 ALTER TABLE `user_story_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_story_mst` ENABLE KEYS */;

-- 트리거 project_instagram.default_profile_image_insert 구조 내보내기
DROP TRIGGER IF EXISTS `default_profile_image_insert`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `default_profile_image_insert` AFTER INSERT ON `user_mst` FOR EACH ROW BEGIN
	INSERT INTO 
		user_profile_image
	VALUES(
		0, 
		NEW.id, 
		NULL, 
		NOW(), 
		NOW()
	);
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.insert_article_reaction_log 구조 내보내기
DROP TRIGGER IF EXISTS `insert_article_reaction_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insert_article_reaction_log` AFTER INSERT ON `article_reaction` FOR EACH ROW BEGIN
	DECLARE article_user_id INT;
	
	SELECT user_id INTO article_user_id FROM article_mst WHERE id = NEW.article_id;
	
	INSERT INTO activity_logs
	SET 
		user_id = NEW.like_user_id,
		related_user_id = article_user_id,
		activity_flag = "article_reaction",
		activity_message = "님이 회원님의 게시글을 좋아합니다.",
		article_id = NEW.article_id,
		create_date = NEW.create_date,
		update_date = NEW.update_date;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.insert_comment_log 구조 내보내기
DROP TRIGGER IF EXISTS `insert_comment_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insert_comment_log` AFTER INSERT ON `article_comment` FOR EACH ROW BEGIN
	DECLARE article_user_id INT;
	DECLARE tag_user_id INT;
	DECLARE tag_username VARCHAR(20);
	DECLARE i INT DEFAULT -1;
	SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(contents,'@',i),' ',1) INTO tag_username FROM article_comment WHERE id = NEW.id;
	SELECT user_id INTO article_user_id FROM article_mst WHERE id = NEW.article_id;
	
	while (tag_username IS NOT NULL && tag_username != "") DO
		SELECT id INTO tag_user_id FROM user_mst WHERE username = tag_username;
		if tag_user_id != NEW.commented_user_id then
			INSERT INTO activity_logs
			SET 
				user_id = NEW.commented_user_id,
				related_user_id = tag_user_id,
				activity_flag = "comment_tag",
				activity_message = "님이 댓글에서 회원님을 언급했습니다.",
				article_id = NEW.article_id,
				comment_id = NEW.id,
				create_date = NEW.create_date,
				update_date = NEW.update_date;
		END if;	
		SET i = i - 1;
		SELECT SUBSTRING_INDEX(SUBSTRING_INDEX(contents,'@',i),' ',1) INTO tag_username FROM article_comment WHERE id = NEW.id;
	END while;
	
	if article_user_id != NEW.commented_user_id then
		INSERT INTO activity_logs
		set
			user_id = NEW.commented_user_id,
			related_user_id = article_user_id,
			activity_flag = "comment",
			activity_message = "님이 게시글에 댓글을 달았습니다.",
			article_id = NEW.article_id,
			comment_id = NEW.id,
			create_date = NEW.create_date,
			update_date = NEW.update_date;
	END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.insert_comment_reaction_log 구조 내보내기
DROP TRIGGER IF EXISTS `insert_comment_reaction_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insert_comment_reaction_log` AFTER INSERT ON `article_comment_reaction` FOR EACH ROW BEGIN
	DECLARE article_id INT;
	DECLARE comment_user_id INT;
	
	SELECT article_id, commented_user_id INTO article_id, comment_user_id 
	FROM article_comment WHERE id = NEW.article_comment_id;
	
	INSERT INTO acrivity_logs
	SET 
		user_id = NEW.like_user_id,
		related_user_id = comment_user_id,
		activity_flag = "comment_reaction",
		activity_message = "님이 회원님이 남긴 댓글을 좋아합니다.",
		article_id = article_id,
		comment_id = NEW.article_comment_id,
		create_date = NEW.create_date,
		update_date = NEW.update_date;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.insert_follow_log 구조 내보내기
DROP TRIGGER IF EXISTS `insert_follow_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insert_follow_log` AFTER INSERT ON `follow_mst` FOR EACH ROW BEGIN
	DECLARE username VARCHAR(20);
	
	SELECT username INTO username FROM user_mst WHERE id = NEW.user_id;

	INSERT INTO activity_logs
	SET 
		user_id = NEW.user_id,
		related_user_id = NEW.partner_user_id,
		activity_flag = "follow",
		activity_message = "님이 회원님을 팔로우하기 시작했습니다.",
		follow_id = NEW.id,
		create_date = NEW.follow_date,
		update_date = NEW.update_date;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
