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
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.activity_logs:~25 rows (대략적) 내보내기
DELETE FROM `activity_logs`;
/*!40000 ALTER TABLE `activity_logs` DISABLE KEYS */;
INSERT INTO `activity_logs` (`id`, `user_id`, `related_user_id`, `activity_flag`, `activity_message`, `article_id`, `comment_id`, `follow_id`, `create_date`, `update_date`) VALUES
	(1, 1, 4, 'follow', '님이 회원님을 팔로우하기 시작했습니다.', NULL, NULL, 1, '2022-04-28 14:22:28', '2022-04-28 14:22:28'),
	(2, 1, 4, 'article_reaction', '님이 회원님의 게시글을 좋아합니다.', 1, NULL, NULL, '2022-04-28 14:24:53', '2022-04-28 14:24:53'),
	(3, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 1, 15, NULL, '2022-04-28 17:44:48', '2022-04-28 17:44:48'),
	(4, 1, 1, 'comment_reaction', '님이 회원님이 남긴 댓글을 좋아합니다.', 1, 15, NULL, '2022-04-28 17:46:35', '2022-04-28 17:46:35'),
	(5, 1, 1, 'comment_reaction', '님이 회원님이 남긴 댓글을 좋아합니다.', 1, 15, NULL, '2022-04-28 17:48:49', '2022-04-28 17:48:49'),
	(6, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 1, 16, NULL, '2022-04-30 14:19:46', '2022-04-30 14:19:46'),
	(7, 1, 1, 'comment_reaction', '님이 회원님이 남긴 댓글을 좋아합니다.', 1, 16, NULL, '2022-04-30 14:19:51', '2022-04-30 14:19:51'),
	(8, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 1, 17, NULL, '2022-04-30 14:21:20', '2022-04-30 14:21:20'),
	(9, 1, 4, 'article_reaction', '님이 회원님의 게시글을 좋아합니다.', 1, NULL, NULL, '2022-04-30 14:21:30', '2022-04-30 14:21:30'),
	(10, 4, 1, 'follow', '님이 회원님을 팔로우하기 시작했습니다.', NULL, NULL, 2, '2022-05-02 10:13:41', '2022-05-02 10:13:41'),
	(11, 4, 1, 'article_reaction', '님이 회원님의 게시글을 좋아합니다.', 20, NULL, NULL, '2022-05-02 12:30:31', '2022-05-02 12:30:31'),
	(12, 4, 1, 'comment', '님이 게시글에 댓글을 달았습니다.', 20, 18, NULL, '2022-05-02 12:32:39', '2022-05-02 12:32:39'),
	(13, 1, 4, 'article_reaction', '님이 회원님의 게시글을 좋아합니다.', 21, NULL, NULL, '2022-05-02 17:20:10', '2022-05-02 17:20:10'),
	(14, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 21, 23, NULL, '2022-05-02 17:47:18', '2022-05-02 17:47:18'),
	(15, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 21, 24, NULL, '2022-05-02 18:09:12', '2022-05-02 18:09:12'),
	(16, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 21, 25, NULL, '2022-05-02 18:10:50', '2022-05-02 18:10:50'),
	(17, 1, 4, 'article_reaction', '님이 회원님의 게시글을 좋아합니다.', 1, NULL, NULL, '2022-05-02 18:18:36', '2022-05-02 18:18:36'),
	(18, 1, 4, 'comment_tag', '님이 댓글에서 회원님을 언급했습니다.', 21, 26, NULL, '2022-05-02 18:19:59', '2022-05-02 18:19:59'),
	(19, 1, 4, 'comment', '님이 게시글에 댓글을 달았습니다.', 21, 26, NULL, '2022-05-02 18:19:59', '2022-05-02 18:19:59'),
	(20, 4, 1, 'article_tag', '님이 게시글에서 회원님을 언급했습니다.', 38, NULL, NULL, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(21, 1, 4, 'article_tag', '님이 게시글에서 회원님을 언급했습니다.', 39, NULL, NULL, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(22, 4, 1, 'comment_tag', '님이 댓글에서 회원님을 언급했습니다.', 39, 28, NULL, '2022-05-03 15:38:15', '2022-05-03 15:38:15'),
	(23, 4, 1, 'comment', '님이 게시글에 댓글을 달았습니다.', 39, 28, NULL, '2022-05-03 15:38:15', '2022-05-03 15:38:15'),
	(24, 1, 4, 'article_tag', '님이 게시글에서 회원님을 언급했습니다.', 40, NULL, NULL, '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(25, 4, 4, 'comment_reaction', '님이 회원님이 남긴 댓글을 좋아합니다.', 39, 28, NULL, '2022-05-03 16:32:31', '2022-05-03 16:32:31');
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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_comment:~9 rows (대략적) 내보내기
DELETE FROM `article_comment`;
/*!40000 ALTER TABLE `article_comment` DISABLE KEYS */;
INSERT INTO `article_comment` (`id`, `article_id`, `commented_user_id`, `contents`, `create_date`, `update_date`, `deleted_flag`, `deleted_date`, `related_flag`, `related_comment_id`) VALUES
	(15, 1, 1, 'asdfsadfsd', '2022-04-28 17:44:48', '2022-04-28 17:44:48', 0, NULL, 0, NULL),
	(16, 1, 1, ' @hippo2003 asdfsadacvasv', '2022-04-30 14:19:46', '2022-04-30 14:19:46', 0, NULL, 1, 15),
	(17, 1, 1, '하이영', '2022-04-30 14:21:20', '2022-04-30 14:21:20', 0, NULL, 0, NULL),
	(18, 20, 4, 'asdasdfsafd', '2022-05-02 12:32:39', '2022-05-02 12:32:39', 0, NULL, 0, NULL),
	(23, 21, 1, '@hippo2004 adfnlkjsdlvnsvd', '2022-05-02 17:47:18', '2022-05-02 17:47:18', 0, NULL, 0, NULL),
	(24, 21, 1, '@hippo2004', '2022-05-02 18:09:12', '2022-05-02 18:09:12', 0, NULL, 0, NULL),
	(25, 21, 1, '@hippo2004 adfbsjdflkvafdg', '2022-05-02 18:10:50', '2022-05-02 18:10:50', 0, NULL, 0, NULL),
	(26, 21, 1, '@hippo2004 afdjsdhfkvnalkdfnvlan;g', '2022-05-02 18:19:59', '2022-05-02 18:19:59', 0, NULL, 0, NULL),
	(28, 39, 4, '@hippo2003 ㅋㅋㅋ 보러 ㄱ?', '2022-05-03 15:38:15', '2022-05-03 15:38:15', 0, NULL, 0, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_comment_reaction:~3 rows (대략적) 내보내기
DELETE FROM `article_comment_reaction`;
/*!40000 ALTER TABLE `article_comment_reaction` DISABLE KEYS */;
INSERT INTO `article_comment_reaction` (`id`, `article_comment_id`, `like_user_id`, `create_date`, `update_date`) VALUES
	(3, 15, 1, '2022-04-28 17:48:49', '2022-04-28 17:48:49'),
	(4, 16, 1, '2022-04-30 14:19:51', '2022-04-30 14:19:51'),
	(7, 28, 4, '2022-05-03 16:32:31', '2022-05-03 16:32:31');
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
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_media:~39 rows (대략적) 내보내기
DELETE FROM `article_media`;
/*!40000 ALTER TABLE `article_media` DISABLE KEYS */;
INSERT INTO `article_media` (`id`, `article_id`, `media_type`, `media_name`, `create_date`, `update_date`) VALUES
	(1, 1, 'image', 'media01.webp', '2022-04-15 21:32:13', '2022-04-15 21:32:13'),
	(50, 19, 'image', 'media01.png', '2022-05-01 17:34:31', '2022-05-01 17:34:31'),
	(51, 19, 'image', 'media02.png', '2022-05-01 17:34:31', '2022-05-01 17:34:31'),
	(52, 20, 'image', 'media01.jpeg', '2022-05-01 17:35:26', '2022-05-01 17:35:26'),
	(53, 20, 'image', 'media02.jpeg', '2022-05-01 17:35:26', '2022-05-01 17:35:26'),
	(54, 20, 'image', 'media03.jpeg', '2022-05-01 17:35:26', '2022-05-01 17:35:26'),
	(55, 21, 'image', 'media01.jpeg', '2022-05-02 12:34:05', '2022-05-02 12:34:05'),
	(56, 21, 'image', 'media02.png', '2022-05-02 12:34:05', '2022-05-02 12:34:05'),
	(57, 21, 'image', 'media03.jpeg', '2022-05-02 12:34:05', '2022-05-02 12:34:05'),
	(58, 21, 'image', 'media04.png', '2022-05-02 12:34:05', '2022-05-02 12:34:05'),
	(59, 34, 'image', 'media01.jpeg', '2022-05-02 17:56:13', '2022-05-02 17:56:13'),
	(60, 34, 'image', 'media02.jpeg', '2022-05-02 17:56:13', '2022-05-02 17:56:13'),
	(61, 34, 'image', 'media03.jpeg', '2022-05-02 17:56:13', '2022-05-02 17:56:13'),
	(62, 34, 'image', 'media04.png', '2022-05-02 17:56:13', '2022-05-02 17:56:13'),
	(63, 35, 'image', 'media01.png', '2022-05-02 18:01:12', '2022-05-02 18:01:12'),
	(64, 35, 'image', 'media02.png', '2022-05-02 18:01:12', '2022-05-02 18:01:12'),
	(65, 35, 'image', 'media03.png', '2022-05-02 18:01:12', '2022-05-02 18:01:12'),
	(66, 35, 'image', 'media04.png', '2022-05-02 18:01:12', '2022-05-02 18:01:12'),
	(67, 36, 'image', 'media01.png', '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(68, 36, 'image', 'media02.png', '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(69, 36, 'image', 'media03.png', '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(70, 36, 'image', 'media04.png', '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(71, 37, 'image', 'media01.jpeg', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(72, 37, 'image', 'media02.png', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(73, 37, 'image', 'media03.png', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(74, 37, 'image', 'media04.png', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(75, 38, 'image', 'media01.jpeg', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(76, 38, 'image', 'media02.jpeg', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(77, 38, 'image', 'media03.jpeg', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(78, 38, 'image', 'media04.png', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(79, 39, 'image', 'media01.jpeg', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(80, 39, 'image', 'media02.png', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(81, 39, 'image', 'media03.jpeg', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(82, 39, 'image', 'media04.jpeg', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(83, 39, 'image', 'media05.jpeg', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(84, 40, 'image', 'media01.jpeg', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(85, 40, 'image', 'media02.jpeg', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(86, 40, 'image', 'media03.png', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(87, 40, 'image', 'media04.jpeg', '2022-05-03 15:50:57', '2022-05-03 15:50:57');
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
  PRIMARY KEY (`id`),
  KEY `user_id_for_article` (`user_id`),
  CONSTRAINT `user_id_for_article` FOREIGN KEY (`user_id`) REFERENCES `user_mst` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_mst:~11 rows (대략적) 내보내기
DELETE FROM `article_mst`;
/*!40000 ALTER TABLE `article_mst` DISABLE KEYS */;
INSERT INTO `article_mst` (`id`, `user_id`, `feature`, `contents`, `is_stored`, `create_date`, `update_date`) VALUES
	(1, 4, NULL, 'test', 0, '2022-04-15 21:31:28', '2022-04-15 21:31:28'),
	(19, 1, 'vczxcvadacw', 'asdfasdfasdf', 0, '2022-05-01 17:34:31', '2022-05-01 17:34:31'),
	(20, 1, 'adfbvfdagar', 'adfgadvvc', 0, '2022-05-01 17:35:26', '2022-05-01 17:35:26'),
	(21, 4, 'adffdabdafdv', 'adsfadsgdvad', 0, '2022-05-02 12:34:05', '2022-05-02 12:34:05'),
	(34, 1, 'HKJANDKGNADF', '#asdf #qrwer #zcxv fdangkjadflkjvn', 0, '2022-05-02 17:56:13', '2022-05-02 17:56:13'),
	(35, 1, 'dfgadfgadfg', '#gkgk #하하 #ㅁㄴㅇㄹ #ㅁㅇㅍㅊ asdgadfadfgasd', 0, '2022-05-02 18:01:12', '2022-05-02 18:01:12'),
	(36, 1, 'asdfasfdasdf', '#asdf #erhoig #jfdnvk #fbsfda adfgdsgdsadf', 0, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(37, 1, 'asdfasfadsf', 'asdfasdf #dfgjdhf #fdhav #jkcS #gfnbkjnb sadfdsfgfdgdagfasdf', 0, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(38, 4, '학원', '@hippo2003 #제대로 #되려나 #??? ', 0, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(39, 1, '학원에스', '@hippo2004 #공기살인 #영화 #포스터 #재밌나 #??? ', 0, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(40, 1, 'asdfasdf', '@hippo2004 #아아 #언제 #끝나지 #db... ', 0, '2022-05-03 15:50:57', '2022-05-03 15:50:57');
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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.article_reaction:~3 rows (대략적) 내보내기
DELETE FROM `article_reaction`;
/*!40000 ALTER TABLE `article_reaction` DISABLE KEYS */;
INSERT INTO `article_reaction` (`id`, `article_id`, `like_user_id`, `create_date`, `update_date`) VALUES
	(3, 20, 4, '2022-05-02 12:30:31', '2022-05-02 12:30:31'),
	(4, 21, 1, '2022-05-02 17:20:10', '2022-05-02 17:20:10'),
	(5, 1, 1, '2022-05-02 18:18:36', '2022-05-02 18:18:36');
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.follow_mst:~2 rows (대략적) 내보내기
DELETE FROM `follow_mst`;
/*!40000 ALTER TABLE `follow_mst` DISABLE KEYS */;
INSERT INTO `follow_mst` (`id`, `user_id`, `partner_user_id`, `follower_group`, `follow_date`, `update_date`) VALUES
	(1, 1, 4, 'COMMON', '2022-04-28 14:22:28', '2022-04-28 14:22:28'),
	(2, 4, 1, 'COMMON', '2022-05-02 10:13:41', '2022-05-02 10:13:41');
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
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.hash_tag_logs:~23 rows (대략적) 내보내기
DELETE FROM `hash_tag_logs`;
/*!40000 ALTER TABLE `hash_tag_logs` DISABLE KEYS */;
INSERT INTO `hash_tag_logs` (`id`, `article_id`, `hash_tag_id`, `create_date`, `update_date`) VALUES
	(1, 36, 1, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(2, 36, 1, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(3, 36, 1, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(4, 36, 1, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(5, 36, 1, '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(6, 37, 2, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(7, 37, 3, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(8, 37, 4, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(9, 37, 5, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(10, 37, 6, '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(11, 38, 7, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(12, 38, 8, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(13, 38, 9, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(14, 38, 10, '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(15, 39, 7, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(16, 39, 11, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(17, 39, 12, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(18, 39, 13, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(19, 39, 14, '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(20, 40, 15, '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(21, 40, 16, '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(22, 40, 17, '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(23, 40, 18, '2022-05-03 15:50:57', '2022-05-03 15:50:57');
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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.hash_tag_mst:~18 rows (대략적) 내보내기
DELETE FROM `hash_tag_mst`;
/*!40000 ALTER TABLE `hash_tag_mst` DISABLE KEYS */;
INSERT INTO `hash_tag_mst` (`id`, `tag_name`, `create_date`, `update_date`) VALUES
	(1, 'fbsfda', '2022-05-02 18:21:01', '2022-05-02 18:21:01'),
	(2, 'gfnbkjnb', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(3, 'jkcS', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(4, 'fdhav', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(5, 'dfgjdhf', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(6, 'asdfasdf', '2022-05-02 18:26:07', '2022-05-02 18:26:07'),
	(7, '???', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(8, '되려나', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(9, '제대로', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(10, '@hippo2003', '2022-05-03 11:29:25', '2022-05-03 11:29:25'),
	(11, '재밌나', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(12, '포스터', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(13, '영화', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(14, '공기살인', '2022-05-03 15:32:46', '2022-05-03 15:32:46'),
	(15, 'db...', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(16, '끝나지', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(17, '언제', '2022-05-03 15:50:57', '2022-05-03 15:50:57'),
	(18, '아아', '2022-05-03 15:50:57', '2022-05-03 15:50:57');
/*!40000 ALTER TABLE `hash_tag_mst` ENABLE KEYS */;

-- 테이블 project_instagram.user_mst 구조 내보내기
DROP TABLE IF EXISTS `user_mst`;
CREATE TABLE IF NOT EXISTS `user_mst` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(16) NOT NULL,
  `password` varchar(100) NOT NULL,
  `name` varchar(20) NOT NULL,
  `email` varchar(30) DEFAULT '',
  `phone` varchar(20) DEFAULT '',
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_mst:~2 rows (대략적) 내보내기
DELETE FROM `user_mst`;
/*!40000 ALTER TABLE `user_mst` DISABLE KEYS */;
INSERT INTO `user_mst` (`id`, `username`, `password`, `name`, `email`, `phone`, `website`, `description`, `gender`, `has_profile_image`, `last_username_update_date`, `create_date`, `update_date`, `disable_flag`, `disable_date`) VALUES
	(1, 'hippo2003', '$2a$10$iCr/.BDIKq7FZwotVUKO6ucfhOcqmC4xRe8noLj9c8ZVqNFoZjlKG', '박준영', 'hippo2003@naver.com', NULL, '', '', 3, 0, '2022-04-28 12:59:58', '2022-04-28 12:59:58', '2022-04-28 12:59:58', 0, NULL),
	(4, 'hippo2004', '$2a$10$ONuSCW3E/6AZ3ZazIkFoE.RbSTCxTZCoBuy1eV.hwo6NdIZMTNCDO', '박준영', 'hippo2004@naver.com', '', '', '', 3, 0, '2022-04-28 14:22:20', '2022-04-28 14:22:20', '2022-04-28 14:22:20', 0, NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 project_instagram.user_profile_image:~2 rows (대략적) 내보내기
DELETE FROM `user_profile_image`;
/*!40000 ALTER TABLE `user_profile_image` DISABLE KEYS */;
INSERT INTO `user_profile_image` (`id`, `user_id`, `file_name`, `create_date`, `update_date`) VALUES
	(2, 1, NULL, '2022-04-28 12:59:58', '2022-04-28 12:59:58'),
	(3, 4, NULL, '2022-04-28 14:22:20', '2022-04-28 14:22:20');
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
