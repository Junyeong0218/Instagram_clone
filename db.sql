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

-- 테이블 project_instagram.article_media 구조 내보내기
CREATE TABLE IF NOT EXISTS `article_media` (
  `id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `media_name` varchar(50) NOT NULL DEFAULT '',
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `article_id_for_media` (`article_id`),
  CONSTRAINT `article_id_for_media` FOREIGN KEY (`article_id`) REFERENCES `article_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_media:~0 rows (대략적) 내보내기
DELETE FROM `article_media`;
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_media` ENABLE KEYS */;

-- 테이블 project_instagram.article_mst 구조 내보내기
CREATE TABLE IF NOT EXISTS `article_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `place_name` varchar(50) NOT NULL,
  `media_type` varchar(5) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  `is_stored` tinyint(1) NOT NULL DEFAULT 0,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_article` (`user_id`),
  CONSTRAINT `user_id_for_article` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_mst:~0 rows (대략적) 내보내기
DELETE FROM `article_mst`;
/*!40000 ALTER TABLE `article_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `article_mst` ENABLE KEYS */;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.follow_mst:~0 rows (대략적) 내보내기
DELETE FROM `follow_mst`;
/*!40000 ALTER TABLE `follow_mst` DISABLE KEYS */;
/*!40000 ALTER TABLE `follow_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_mst 구조 내보내기
CREATE TABLE IF NOT EXISTS `user_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL DEFAULT '',
  `password` varchar(50) NOT NULL DEFAULT '',
  `name` varchar(20) NOT NULL DEFAULT '',
  `email` varchar(30) NOT NULL DEFAULT '',
  `has_profile_image` tinyint(1) unsigned zerofill NOT NULL DEFAULT 0,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_mst:~12 rows (대략적) 내보내기
DELETE FROM `user_mst`;
/*!40000 ALTER TABLE `user_mst` DISABLE KEYS */;
INSERT INTO `user_mst` (`id`, `username`, `password`, `name`, `email`, `has_profile_image`, `create_date`, `update_date`) VALUES
	(1, 'hippo2003', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 10:49:44', '2022-04-11 10:49:44'),
	(2, 'hippo2004', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:29', '2022-04-11 11:07:29'),
	(3, 'hippo2005', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:31', '2022-04-11 11:07:31'),
	(4, 'hippo2006', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:32', '2022-04-11 11:07:32'),
	(5, 'hippo2007', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:33', '2022-04-11 11:07:33'),
	(6, 'hippo2008', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:35', '2022-04-11 11:07:35'),
	(7, 'hippo2009', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:36', '2022-04-11 11:07:36'),
	(8, 'hippo2010', 'wns12358', '박준영', 'hippo2003@naver.com', 0, '2022-04-11 11:07:38', '2022-04-11 11:07:38'),
	(11, 'hippo2013', 'wns12358', '박준영', '01035947113', 0, '2022-04-13 14:37:38', '2022-04-13 14:37:38'),
	(12, 'hippo2014', 'wns12358', '박준영', '01035947114', 0, '2022-04-13 14:39:03', '2022-04-13 14:39:03'),
	(13, 'hippo2015', 'wns12358', '박준영', '01035947111', 0, '2022-04-13 15:37:47', '2022-04-13 15:37:47');
/*!40000 ALTER TABLE `user_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_profile_image 구조 내보내기
CREATE TABLE IF NOT EXISTS `user_profile_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `file_name` varchar(50) NOT NULL,
  `create_date` datetime NOT NULL,
  `update_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id_for_profile_image` (`user_id`),
  CONSTRAINT `user_id_for_profile_image` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_profile_image:~0 rows (대략적) 내보내기
DELETE FROM `user_profile_image`;
/*!40000 ALTER TABLE `user_profile_image` DISABLE KEYS */;
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

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
