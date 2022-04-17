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

-- 테이블 project_instagram.article_comment 구조 내보내기
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_comment:~1 rows (대략적) 내보내기
DELETE FROM `article_comment`;
/*!40000 ALTER TABLE `article_comment` DISABLE KEYS */;
INSERT INTO `article_comment` (`id`, `article_id`, `commented_user_id`, `contents`, `create_date`, `update_date`, `deleted_flag`, `deleted_date`, `related_flag`, `related_comment_id`) VALUES
	(1, 1, 1, 'asdfsadfdfsa', '2022-04-16 23:00:08', '2022-04-16 23:00:08', 0, NULL, 0, NULL);
/*!40000 ALTER TABLE `article_comment` ENABLE KEYS */;

-- 테이블 project_instagram.article_comment_reaction 구조 내보내기
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
	(0, 1, 'media01.webp', '2022-04-15 21:32:13', '2022-04-15 21:32:13');
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;

-- 테이블 project_instagram.article_mst 구조 내보내기
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

-- 테이블 데이터 project_instagram.article_mst:~1 rows (대략적) 내보내기
DELETE FROM `article_mst`;
/*!40000 ALTER TABLE `article_mst` DISABLE KEYS */;
INSERT INTO `article_mst` (`id`, `user_id`, `feature`, `media_type`, `contents`, `is_stored`, `create_date`, `update_date`) VALUES
	(1, 2, NULL, 'webp', 'test', 0, '2022-04-15 21:31:28', '2022-04-15 21:31:28');
/*!40000 ALTER TABLE `article_mst` ENABLE KEYS */;

-- 테이블 project_instagram.article_reaction 구조 내보내기
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_reaction:~1 rows (대략적) 내보내기
DELETE FROM `article_reaction`;
/*!40000 ALTER TABLE `article_reaction` DISABLE KEYS */;
INSERT INTO `article_reaction` (`id`, `article_id`, `like_user_id`, `create_date`, `update_date`) VALUES
	(2, 1, 1, '2022-04-16 21:34:35', '2022-04-16 21:34:35');
/*!40000 ALTER TABLE `article_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.follow_mst 구조 내보내기
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.follow_mst:~1 rows (대략적) 내보내기
DELETE FROM `follow_mst`;
/*!40000 ALTER TABLE `follow_mst` DISABLE KEYS */;
INSERT INTO `follow_mst` (`id`, `user_id`, `partner_user_id`, `follower_group`, `follow_date`, `update_date`) VALUES
	(1, 1, 2, 'COMMON', '2022-04-16 15:14:58', '2022-04-16 15:14:58');
/*!40000 ALTER TABLE `follow_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_mst 구조 내보내기
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
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_mst:~11 rows (대략적) 내보내기
DELETE FROM `user_mst`;
/*!40000 ALTER TABLE `user_mst` DISABLE KEYS */;
INSERT INTO `user_mst` (`id`, `username`, `password`, `name`, `email`, `phone`, `website`, `description`, `gender`, `has_profile_image`, `last_username_update_date`, `create_date`, `update_date`, `disable_flag`, `disable_date`) VALUES
	(1, 'hippo2003', 'wns12358', '박준영', 'hippo2003@naver.com', '01035947111', '', '', 0, 1, '2022-04-11 10:49:44', '2022-04-11 10:49:44', '2022-04-16 12:26:13', 0, NULL),
	(2, 'hippo2004', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:29', '2022-04-11 11:07:29', '2022-04-11 11:07:29', 0, NULL),
	(3, 'hippo2005', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:31', '2022-04-11 11:07:31', '2022-04-11 11:07:31', 0, NULL),
	(4, 'hippo2006', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:32', '2022-04-11 11:07:32', '2022-04-11 11:07:32', 0, NULL),
	(5, 'hippo2007', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:33', '2022-04-11 11:07:33', '2022-04-11 11:07:33', 0, NULL),
	(6, 'hippo2008', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:35', '2022-04-11 11:07:35', '2022-04-11 11:07:35', 0, NULL),
	(7, 'hippo2009', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-11 11:07:36', '2022-04-11 11:07:36', '2022-04-11 11:07:36', 0, NULL),
	(8, 'hippo2010', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-13 14:37:00', '2022-04-13 14:37:00', '2022-04-11 11:07:38', 0, NULL),
	(11, 'hippo2013', 'wns12358', '박준영', '', '01035947113', '', '', 3, 0, '2022-04-13 14:37:38', '2022-04-13 14:37:38', '2022-04-13 14:37:38', 0, NULL),
	(12, 'hippo2014', 'wns12358', '박준영', '', '01035947114', '', '', 3, 0, '2022-04-13 14:39:03', '2022-04-13 14:39:03', '2022-04-13 14:39:03', 0, NULL),
	(13, 'hippo2015', 'wns12358', '박준영', '', '01035947111', '', '', 3, 0, '2022-04-13 15:37:47', '2022-04-13 15:37:47', '2022-04-13 15:37:47', 0, NULL),
	(14, 'hippo2016', 'wns12358', '박준영', 'hippo2003@naver.com', '', '', '', 3, 0, '2022-04-15 16:03:18', '2022-04-15 16:03:18', '2022-04-15 16:03:18', 0, NULL),
	(15, 'hippo2017', 'wns12358', '박준영', '', '01035947111', '', '', 3, 0, '2022-04-15 16:03:52', '2022-04-15 16:03:52', '2022-04-15 16:03:52', 0, NULL),
	(16, 'hippo2018', 'wns12358', '박준영', '', '01035947111', '', '', 3, 0, '2022-04-15 16:15:53', '2022-04-15 16:15:53', '2022-04-15 16:15:53', 0, NULL);
/*!40000 ALTER TABLE `user_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_profile_image 구조 내보내기
CREATE TABLE IF NOT EXISTS `user_profile_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_name` varchar(50) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_profile_image` (`user_id`),
  CONSTRAINT `user_id_for_profile_image` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_profile_image:~12 rows (대략적) 내보내기
DELETE FROM `user_profile_image`;
/*!40000 ALTER TABLE `user_profile_image` DISABLE KEYS */;
INSERT INTO `user_profile_image` (`id`, `user_id`, `file_name`, `create_date`, `update_date`) VALUES
	(1, 16, NULL, '2022-04-15 16:15:53', '2022-04-15 16:15:53'),
	(2, 1, 'hippo2003.ico', '2022-04-15 16:17:38', '2022-04-16 12:26:13'),
	(3, 2, NULL, '2022-04-15 16:17:41', '2022-04-15 16:17:41'),
	(4, 3, NULL, '2022-04-15 16:17:45', '2022-04-15 16:17:45'),
	(5, 4, NULL, '2022-04-15 16:17:47', '2022-04-15 16:17:47'),
	(6, 5, NULL, '2022-04-15 16:17:50', '2022-04-15 16:17:50'),
	(7, 6, NULL, '2022-04-15 16:17:53', '2022-04-15 16:17:53'),
	(8, 7, NULL, '2022-04-15 16:17:55', '2022-04-15 16:17:55'),
	(9, 8, NULL, '2022-04-15 16:17:57', '2022-04-15 16:17:57'),
	(10, 11, NULL, '2022-04-15 16:18:13', '2022-04-15 16:18:13'),
	(11, 12, NULL, '2022-04-15 16:18:15', '2022-04-15 16:18:15'),
	(12, 13, NULL, '2022-04-15 16:18:18', '2022-04-15 16:18:18'),
	(13, 14, NULL, '2022-04-15 16:18:20', '2022-04-15 16:18:20'),
	(14, 15, NULL, '2022-04-15 16:18:23', '2022-04-15 16:18:23');
/*!40000 ALTER TABLE `user_profile_image` ENABLE KEYS */;

-- 테이블 project_instagram.user_story_mst 구조 내보내기
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

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
