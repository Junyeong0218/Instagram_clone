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
  `related_user_read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: non-read / 1: read',
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

-- 테이블 데이터 project_instagram.article_comment:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `article_comment` DISABLE KEYS */;
INSERT IGNORE INTO `article_comment` (`id`, `article_id`, `commented_user_id`, `contents`, `create_date`, `update_date`, `deleted_flag`, `deleted_date`, `related_flag`, `related_comment_id`) VALUES
	(1, 5, 1, 'ㅎㅇㅎㅇ', '2022-05-31 21:18:18', '2022-05-31 21:18:18', 0, NULL, 0, NULL),
	(2, 5, 1, 'ㅎㅇ', '2022-05-31 21:18:44', '2022-05-31 21:18:44', 1, '2022-05-31 21:21:12', 1, 1);
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
/*!40000 ALTER TABLE `article_comment_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_comment_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.article_media 구조 내보내기
DROP TABLE IF EXISTS `article_media`;
CREATE TABLE IF NOT EXISTS `article_media` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `media_type` varchar(5) NOT NULL,
  `media_name` varchar(100) NOT NULL DEFAULT '',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_media` (`article_id`),
  CONSTRAINT `article_id_for_media` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_media:~8 rows (대략적) 내보내기
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
INSERT IGNORE INTO `article_media` (`id`, `article_id`, `media_type`, `media_name`, `create_date`, `update_date`) VALUES
	(1, 5, 'image', 'media01.jpg', '2022-05-31 20:49:57', '2022-05-31 20:49:57'),
	(2, 5, 'image', 'media02.jpg', '2022-05-31 20:49:57', '2022-05-31 20:49:57'),
	(3, 5, 'image', 'media03.jpg', '2022-05-31 20:49:57', '2022-05-31 20:49:57'),
	(4, 5, 'image', 'media04.png', '2022-05-31 20:49:57', '2022-05-31 20:49:57'),
	(5, 6, 'image', 'media01.jpg', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(6, 6, 'image', 'media02.jpg', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(7, 6, 'image', 'media03.jpg', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(8, 6, 'image', 'media04.png', '2022-05-31 21:38:22', '2022-05-31 21:38:22');
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;

-- 테이블 project_instagram.article_mst 구조 내보내기
DROP TABLE IF EXISTS `article_mst`;
CREATE TABLE IF NOT EXISTS `article_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `feature` varchar(50) DEFAULT NULL COMMENT 'place_name / sound_included etc',
  `contents` varchar(2200) DEFAULT '',
  `is_stored` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: public / 1: private',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `deleted_flag` tinyint(1) NOT NULL DEFAULT 0,
  `deleted_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_article` (`user_id`),
  CONSTRAINT `user_id_for_article` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_mst:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `article_mst` DISABLE KEYS */;
INSERT IGNORE INTO `article_mst` (`id`, `user_id`, `feature`, `contents`, `is_stored`, `create_date`, `update_date`, `deleted_flag`, `deleted_date`) VALUES
	(5, 1, 'vcxz231vcxz231', '12314242123', 0, '2022-05-31 20:49:57', '2022-05-31 21:03:19', 0, NULL),
	(6, 1, '123123', '#text #test #if #methods aszzzz', 0, '2022-05-31 21:38:22', '2022-05-31 21:38:22', 0, NULL);
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
/*!40000 ALTER TABLE `article_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_image 구조 내보내기
DROP TABLE IF EXISTS `direct_message_image`;
CREATE TABLE IF NOT EXISTS `direct_message_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_name` varchar(100) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_image:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_image` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_mst 구조 내보내기
DROP TABLE IF EXISTS `direct_message_mst`;
CREATE TABLE IF NOT EXISTS `direct_message_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `contents` varchar(300) NOT NULL DEFAULT '',
  `is_image` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0: just_text / 1: image',
  `image_id` int(11) DEFAULT 0,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `delete_flag` tinyint(1) NOT NULL DEFAULT 0,
  `delete_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_direct_message` (`user_id`),
  KEY `image_id_for_direct_message` (`image_id`),
  KEY `room_id_for_direct_message` (`room_id`),
  CONSTRAINT `image_id_for_direct_message` FOREIGN KEY (`image_id`) REFERENCES `direct_message_image` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `room_id_for_direct_message` FOREIGN KEY (`room_id`) REFERENCES `direct_message_room_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_direct_message` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_mst:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_mst` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_reaction 구조 내보내기
DROP TABLE IF EXISTS `direct_message_reaction`;
CREATE TABLE IF NOT EXISTS `direct_message_reaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `direct_message_id` int(11) NOT NULL,
  `update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_direct_message_reaction` (`user_id`),
  KEY `direct_message_id_for_reaction` (`direct_message_id`),
  CONSTRAINT `direct_message_id_for_reaction` FOREIGN KEY (`direct_message_id`) REFERENCES `direct_message_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_direct_message_reaction` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_reaction:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_reaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_reaction` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_read_flags 구조 내보내기
DROP TABLE IF EXISTS `direct_message_read_flags`;
CREATE TABLE IF NOT EXISTS `direct_message_read_flags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `direct_message_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_message_read_flag` (`user_id`),
  KEY `direct_message_id_for_message_read_flag` (`direct_message_id`),
  CONSTRAINT `direct_message_id_for_message_read_flag` FOREIGN KEY (`direct_message_id`) REFERENCES `direct_message_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_message_read_flag` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_read_flags:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_read_flags` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_read_flags` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_room_entered_users 구조 내보내기
DROP TABLE IF EXISTS `direct_message_room_entered_users`;
CREATE TABLE IF NOT EXISTS `direct_message_room_entered_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `room_id_for_enter_flag` (`room_id`),
  KEY `user_id_for_enter_flag` (`user_id`),
  CONSTRAINT `room_id_for_enter_flag` FOREIGN KEY (`room_id`) REFERENCES `direct_message_room_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_enter_flag` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_room_entered_users:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_room_entered_users` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_room_entered_users` ENABLE KEYS */;

-- 테이블 project_instagram.direct_message_room_mst 구조 내보내기
DROP TABLE IF EXISTS `direct_message_room_mst`;
CREATE TABLE IF NOT EXISTS `direct_message_room_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `made_user_id` int(11) NOT NULL DEFAULT 0,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `made_user_id_for_dm_room_mst` (`made_user_id`),
  CONSTRAINT `made_user_id_for_dm_room_mst` FOREIGN KEY (`made_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.direct_message_room_mst:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `direct_message_room_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `direct_message_room_mst` ENABLE KEYS */;

-- 테이블 project_instagram.follow_mst 구조 내보내기
DROP TABLE IF EXISTS `follow_mst`;
CREATE TABLE IF NOT EXISTS `follow_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `partner_user_id` int(11) DEFAULT NULL,
  `followed_hash_tag_id` int(11) DEFAULT NULL,
  `follower_group` varchar(20) DEFAULT 'COMMON',
  `follow_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_follow` (`user_id`),
  KEY `partner_id_for_follow` (`partner_user_id`),
  KEY `followed_hash_tag_id_for_follow` (`followed_hash_tag_id`),
  CONSTRAINT `followed_hash_tag_id_for_follow` FOREIGN KEY (`followed_hash_tag_id`) REFERENCES `hash_tag_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `partner_id_for_follow` FOREIGN KEY (`partner_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_follow` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.follow_mst:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `follow_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `follow_mst` ENABLE KEYS */;

-- 테이블 project_instagram.hash_tag_logs 구조 내보내기
DROP TABLE IF EXISTS `hash_tag_logs`;
CREATE TABLE IF NOT EXISTS `hash_tag_logs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `article_id` int(11) NOT NULL,
  `hash_tag_id` int(11) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_hash_tag_logs` (`article_id`),
  KEY `hash_tag_id_for_hash_tag_logs` (`hash_tag_id`),
  CONSTRAINT `article_id_for_hash_tag_logs` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `hash_tag_id_for_hash_tag_logs` FOREIGN KEY (`hash_tag_id`) REFERENCES `hash_tag_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.hash_tag_logs:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `hash_tag_logs` DISABLE KEYS */;
INSERT IGNORE INTO `hash_tag_logs` (`id`, `article_id`, `hash_tag_id`, `create_date`, `update_date`) VALUES
	(1, 6, 1, '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(2, 6, 2, '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(3, 6, 3, '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(4, 6, 4, '2022-05-31 21:38:22', '2022-05-31 21:38:22');
/*!40000 ALTER TABLE `hash_tag_logs` ENABLE KEYS */;

-- 테이블 project_instagram.hash_tag_mst 구조 내보내기
DROP TABLE IF EXISTS `hash_tag_mst`;
CREATE TABLE IF NOT EXISTS `hash_tag_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(20) NOT NULL DEFAULT '',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.hash_tag_mst:~4 rows (대략적) 내보내기
/*!40000 ALTER TABLE `hash_tag_mst` DISABLE KEYS */;
INSERT IGNORE INTO `hash_tag_mst` (`id`, `tag_name`, `create_date`, `update_date`) VALUES
	(1, 'methods', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(2, 'if', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(3, 'test', '2022-05-31 21:38:22', '2022-05-31 21:38:22'),
	(4, 'text', '2022-05-31 21:38:22', '2022-05-31 21:38:22');
/*!40000 ALTER TABLE `hash_tag_mst` ENABLE KEYS */;

-- 테이블 project_instagram.latest_search_records 구조 내보내기
DROP TABLE IF EXISTS `latest_search_records`;
CREATE TABLE IF NOT EXISTS `latest_search_records` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `searched_user_id` int(11) DEFAULT NULL,
  `hash_tag_id` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `hash_tag_id_for_latest_result` (`hash_tag_id`),
  KEY `searched_user_id_for_latest_result` (`searched_user_id`),
  KEY `user_id_for_latest_result` (`user_id`),
  CONSTRAINT `hash_tag_id_for_latest_result` FOREIGN KEY (`hash_tag_id`) REFERENCES `hash_tag_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `searched_user_id_for_latest_result` FOREIGN KEY (`searched_user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_id_for_latest_result` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.latest_search_records:~0 rows (대략적) 내보내기
/*!40000 ALTER TABLE `latest_search_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `latest_search_records` ENABLE KEYS */;

-- 테이블 project_instagram.user_auth_token 구조 내보내기
DROP TABLE IF EXISTS `user_auth_token`;
CREATE TABLE IF NOT EXISTS `user_auth_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `jwt_token` varchar(300) DEFAULT NULL,
  `secret_key` varchar(100) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `secret_key` (`secret_key`),
  UNIQUE KEY `user_id` (`user_id`),
  CONSTRAINT `user_id_for_jwt_token` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_auth_token:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user_auth_token` DISABLE KEYS */;
INSERT IGNORE INTO `user_auth_token` (`id`, `user_id`, `jwt_token`, `secret_key`, `create_date`, `update_date`) VALUES
	(1, 1, 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhdXRoVXNlciIsIm5hbWUiOiLrsJXspIDsmIEiLCJpZCI6MSwiZXhwIjoxNjU0MDAxMzY3LCJ1c2VybmFtZSI6ImhpcHBvMjAwMyJ9.9UcmtULlPVtZeUiwj5E-_7sRdESjE1KNVrCDe81F2-Fz8hJbPpWRQo4touDyPCCmefYuOALOoSTDDLLpF17kpg', 'af6b1946-1b9f-4410-a5e0-4c12ec762918', '2022-05-31 12:30:28', '2022-05-31 12:30:28'),
	(2, 2, NULL, 'f39c7cfa-6303-424d-b220-51ccd64da1c4', '2022-05-31 21:28:51', '2022-05-31 21:28:51');
/*!40000 ALTER TABLE `user_auth_token` ENABLE KEYS */;

-- 테이블 project_instagram.user_detail 구조 내보내기
DROP TABLE IF EXISTS `user_detail`;
CREATE TABLE IF NOT EXISTS `user_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(16) NOT NULL DEFAULT '',
  `name` varchar(20) NOT NULL DEFAULT '',
  `email` varchar(30) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `website` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `gender` tinyint(1) NOT NULL DEFAULT 3,
  `has_profile_image` tinyint(1) unsigned zerofill NOT NULL DEFAULT 0,
  `last_username_update_date` datetime NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  CONSTRAINT `user_id_for_user_detail` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_detail:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user_detail` DISABLE KEYS */;
INSERT IGNORE INTO `user_detail` (`id`, `user_id`, `username`, `name`, `email`, `phone`, `website`, `description`, `gender`, `has_profile_image`, `last_username_update_date`, `create_date`, `update_date`) VALUES
	(1, 1, 'hippo2003', '박준영', 'hippo2003@naver.com', NULL, NULL, '', 3, 0, '2022-05-31 12:30:28', '2022-05-31 12:30:28', '2022-05-31 13:10:27'),
	(2, 2, 'hippo2004', '박준일', 'hippo2004@naver.com', NULL, NULL, NULL, 3, 0, '2022-05-31 21:28:51', '2022-05-31 21:28:51', '2022-05-31 21:28:51');
/*!40000 ALTER TABLE `user_detail` ENABLE KEYS */;

-- 테이블 project_instagram.user_mst 구조 내보내기
DROP TABLE IF EXISTS `user_mst`;
CREATE TABLE IF NOT EXISTS `user_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `oauth_username` varchar(100) DEFAULT NULL,
  `provider` varchar(20) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  `disable_flag` tinyint(1) unsigned zerofill NOT NULL DEFAULT 0 COMMENT '0: active / 1: disabled',
  `disable_date` datetime DEFAULT NULL,
  `role` varchar(50) NOT NULL DEFAULT 'ROLE_USER' COMMENT 'ROLE_USER, ROLE_ADMIN, ROLE_DEV',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_mst:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user_mst` DISABLE KEYS */;
INSERT IGNORE INTO `user_mst` (`id`, `username`, `password`, `name`, `email`, `phone`, `oauth_username`, `provider`, `create_date`, `update_date`, `disable_flag`, `disable_date`, `role`) VALUES
	(1, 'hippo2003', '$2a$10$tmCecxfY3eBUwvMv6LwDX.wU5t6nH.nlzlAMPWaGzRCqJFSxKobZi', '박준영', 'hippo2003@naver.com', NULL, NULL, NULL, '2022-05-31 12:30:28', '2022-05-31 13:10:27', 0, NULL, 'ROLE_USER'),
	(2, 'hippo2004', '$2a$10$X731KA.SaqZgZfg4FXTGRusCHf9/Kiba.x4o77qfbSWUqJkScYTd.', '박준일', 'hippo2004@naver.com', NULL, NULL, NULL, '2022-05-31 21:28:51', '2022-05-31 21:28:51', 0, NULL, 'ROLE_USER');
/*!40000 ALTER TABLE `user_mst` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_profile_image:~2 rows (대략적) 내보내기
/*!40000 ALTER TABLE `user_profile_image` DISABLE KEYS */;
INSERT IGNORE INTO `user_profile_image` (`id`, `user_id`, `file_name`, `create_date`, `update_date`) VALUES
	(1, 1, NULL, '2022-05-31 12:30:28', '2022-05-31 12:30:28'),
	(2, 2, NULL, '2022-05-31 21:28:51', '2022-05-31 21:28:51');
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
/*!40000 ALTER TABLE `user_story_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_story_mst` ENABLE KEYS */;

-- 트리거 project_instagram.delete_article_log_and_relates 구조 내보내기
DROP TRIGGER IF EXISTS `delete_article_log_and_relates`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `delete_article_log_and_relates` BEFORE UPDATE ON `article_mst` FOR EACH ROW BEGIN
	if OLD.deleted_flag != NEW.deleted_flag then
		DELETE FROM article_media WHERE article_id = OLD.id;
		DELETE FROM article_reaction WHERE article_id = OLD.id;
	
		UPDATE article_comment SET deleted_flag = TRUE, deleted_date = NOW() WHERE article_id = OLD.id;
		DELETE FROM article_comment_reaction WHERE article_comment_id IN(SELECT id FROM article_comment WHERE article_id = OLD.id AND deleted_flag = TRUE);
		DELETE FROM activity_logs WHERE article_id = OLD.id;
	END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.delete_article_reaction 구조 내보내기
DROP TRIGGER IF EXISTS `delete_article_reaction`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `delete_article_reaction` BEFORE DELETE ON `article_reaction` FOR EACH ROW BEGIN
	DELETE from
		activity_logs 
	WHERE 
		activity_logs.user_id = OLD.like_user_id AND activity_logs.article_id = OLD.article_id AND activity_logs.activity_flag = "article_reaction";
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.delete_comment_log_and_relates 구조 내보내기
DROP TRIGGER IF EXISTS `delete_comment_log_and_relates`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `delete_comment_log_and_relates` BEFORE DELETE ON `article_comment` FOR EACH ROW BEGIN
	UPDATE article_comment SET deleted_flag = TRUE, deleted_date = NOW() WHERE related_comment_id = OLD.id;
	DELETE FROM article_comment_reaction WHERE article_comment_id = OLD.id;
	DELETE FROM activity_logs WHERE comment_id = OLD.id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.delete_comment_reaction_log 구조 내보내기
DROP TRIGGER IF EXISTS `delete_comment_reaction_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `delete_comment_reaction_log` BEFORE DELETE ON `article_comment_reaction` FOR EACH ROW BEGIN
	DELETE FROM 
		activity_logs
	WHERE 
		user_id = OLD.like_user_id AND comment_id = OLD.article_comment_id AND activity_flag = "comment_reaction";
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.delete_follow_log_for_user 구조 내보내기
DROP TRIGGER IF EXISTS `delete_follow_log_for_user`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `delete_follow_log_for_user` BEFORE DELETE ON `follow_mst` FOR EACH ROW BEGIN
	DELETE FROM
		activity_logs
	WHERE 
		follow_id = OLD.id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.insert_article_log 구조 내보내기
DROP TRIGGER IF EXISTS `insert_article_log`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `insert_article_log` AFTER INSERT ON `article_mst` FOR EACH ROW BEGIN
	DECLARE tag_user_id INT;
	DECLARE tag_username VARCHAR(2200);
	DECLARE hash_id INT;
	DECLARE hash_tag_name VARCHAR(20);
	DECLARE origin_contents VARCHAR(2200) DEFAULT NEW.contents;
	DECLARE substr_contents VARCHAR(2200);
	DECLARE i INT DEFAULT -1;
	SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
	
	while (substr_contents != origin_contents) DO
		SELECT SUBSTRING_INDEX(substr_contents, ' ', 1) INTO tag_username;
		SELECT id INTO tag_user_id FROM user_mst WHERE username = tag_username;
		if tag_user_id IS NOT NULL AND tag_user_id != NEW.user_id then
			INSERT INTO activity_logs
			SET
				user_id = NEW.user_id,
				related_user_id = tag_user_id,
				activity_flag = "article_tag",
				activity_message = "님이 게시글에서 회원님을 언급했습니다.",
				article_id = NEW.id,
				create_date = NEW.create_date,
				update_date = NEW.update_date;
		END if;
		
		SET i = i - 1;
		SET tag_user_id = NULL;
		SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
	END while;
	
	SET i = -1;
	SELECT SUBSTRING_INDEX(origin_contents,'#',i) INTO substr_contents LIMIT 1;
	
	while (substr_contents != origin_contents) DO
		SELECT SUBSTRING_INDEX(substr_contents, ' ', 1) INTO hash_tag_name;
		SELECT id INTO hash_id FROM hash_tag_mst WHERE tag_name = hash_tag_name;
	
		if hash_id IS NULL || hash_id = 0 || hash_id = "" then
			INSERT INTO hash_tag_mst
			SET 
				tag_name = hash_tag_name,
				create_date = NEW.create_date,
				update_date = NEW.update_date;
			
			SELECT id INTO hash_id FROM hash_tag_mst WHERE tag_name = hash_tag_name;
		END if;
		
		INSERT INTO hash_tag_logs
		SET 
			article_id = NEW.id,
			hash_tag_id = hash_id,
			create_date = NEW.create_date,
			update_date = NEW.update_date;

		SET hash_id = NULL;
		SET i = i - 1;
		SELECT SUBSTRING_INDEX(origin_contents,'#',i) INTO substr_contents LIMIT 1;
	END while;
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
	DECLARE origin_contents VARCHAR(300) DEFAULT NEW.contents;
	DECLARE substr_contents VARCHAR(300);
	DECLARE i INT DEFAULT -1;
	SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
	SELECT user_id INTO article_user_id FROM article_mst WHERE id = NEW.article_id;
	
	while (substr_contents != origin_contents) DO
		SELECT SUBSTRING_INDEX(substr_contents, ' ', 1) INTO tag_username;
		SELECT id INTO tag_user_id FROM user_mst WHERE username = tag_username;
		if tag_user_id IS NOT NULL && tag_user_id != NEW.commented_user_id then
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
		SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
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
	DECLARE commented_article_id INT;
	DECLARE comment_user_id INT;
	
	SELECT article_id, commented_user_id INTO commented_article_id, comment_user_id 
	FROM article_comment WHERE id = NEW.article_comment_id;
	
	INSERT INTO activity_logs
	SET 
		user_id = NEW.like_user_id,
		related_user_id = comment_user_id,
		activity_flag = "comment_reaction",
		activity_message = "님이 회원님이 남긴 댓글을 좋아합니다.",
		article_id = commented_article_id,
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
	if NEW.partner_user_id != NULL then 
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
	END if;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.update_article_tags 구조 내보내기
DROP TRIGGER IF EXISTS `update_article_tags`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `update_article_tags` AFTER UPDATE ON `article_mst` FOR EACH ROW BEGIN
	DECLARE tag_user_id INT;
	DECLARE tag_username VARCHAR(2200);
	DECLARE hash_id INT;
	DECLARE hash_tag_name VARCHAR(20);
	DECLARE origin_contents VARCHAR(2200) DEFAULT OLD.contents;
	DECLARE substr_contents VARCHAR(2200);
	DECLARE i INT DEFAULT -1;
	SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
	
	DELETE FROM activity_logs WHERE article_id = NEW.id;
	
	while (substr_contents != origin_contents) DO
		SELECT SUBSTRING_INDEX(substr_contents, ' ', 1) INTO tag_username;
		SELECT id INTO tag_user_id FROM user_mst WHERE username = tag_username;
		if tag_user_id IS NOT NULL AND tag_user_id != OLD.user_id then
			INSERT INTO activity_logs
			SET
				user_id = NEW.user_id,
				related_user_id = tag_user_id,
				activity_flag = "article_tag",
				activity_message = "님이 게시글에서 회원님을 언급했습니다.",
				article_id = NEW.id,
				create_date = NEW.create_date,
				update_date = NEW.update_date;
		END if;
		
		SET i = i - 1;
		SET tag_user_id = NULL;
		SELECT SUBSTRING_INDEX(origin_contents,'@',i) INTO substr_contents LIMIT 1;
	END while;
	
	DELETE FROM hash_tag_logs WHERE article_id = NEW.id;
	
	SET i = -1;
	SELECT SUBSTRING_INDEX(origin_contents,'#',i) INTO substr_contents LIMIT 1;
	
	while (substr_contents != origin_contents) DO
		SELECT SUBSTRING_INDEX(substr_contents, ' ', 1) INTO hash_tag_name;
		SELECT id INTO hash_id FROM hash_tag_mst WHERE tag_name = hash_tag_name;
	
		if hash_id IS NULL || hash_id = 0 || hash_id = "" then
			INSERT INTO hash_tag_mst
			SET 
				tag_name = hash_tag_name,
				create_date = NEW.create_date,
				update_date = NEW.update_date;
			
			SELECT id INTO hash_id FROM hash_tag_mst WHERE tag_name = hash_tag_name;
		END if;
		
		INSERT INTO hash_tag_logs
		SET 
			article_id = NEW.id,
			hash_tag_id = hash_id,
			create_date = NEW.create_date,
			update_date = NEW.update_date;

		SET hash_id = NULL;
		SET i = i - 1;
		SELECT SUBSTRING_INDEX(origin_contents,'#',i) INTO substr_contents LIMIT 1;
	END while;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.user_detail_after_update 구조 내보내기
DROP TRIGGER IF EXISTS `user_detail_after_update`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `user_detail_after_update` AFTER UPDATE ON `user_detail` FOR EACH ROW BEGIN
	UPDATE 
		user_mst
	SET 
		username = NEW.username,
		`name` = NEW.`name`,
		email = NEW.email,
		phone = NEW.phone,
		update_date = NEW.update_date
	WHERE 
		id = NEW.id;
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

-- 트리거 project_instagram.user_mst_insert_after_default_set 구조 내보내기
DROP TRIGGER IF EXISTS `user_mst_insert_after_default_set`;
SET @OLDTMP_SQL_MODE=@@SQL_MODE, SQL_MODE='STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';
DELIMITER //
CREATE TRIGGER `user_mst_insert_after_default_set` AFTER INSERT ON `user_mst` FOR EACH ROW BEGIN
	INSERT INTO 
		user_profile_image
	VALUES(
		0, 
		NEW.id, 
		NULL, 
		NOW(), 
		NOW()
	);
	
	INSERT INTO 
		user_detail
	VALUES(
		0,
		NEW.id,
		NEW.username,
		NEW.`name`,
		NEW.email,
		NEW.phone,
		NULL,
		NULL,
		3,
		0,
		NEW.create_date,
		NEW.create_date,
		NEW.update_date
	);
	
	INSERT INTO 
		user_auth_token
	VALUES(
		0,
		NEW.id,
		NULL,
		NEW.username,
		NEW.create_date,
		NEW.update_date
	);
		
END//
DELIMITER ;
SET SQL_MODE=@OLDTMP_SQL_MODE;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
