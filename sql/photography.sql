/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50730
 Source Host           : localhost:3306
 Source Schema         : ssm_test

 Target Server Type    : MySQL
 Target Server Version : 50730
 File Encoding         : 65001

 Date: 04/04/2023 17:52:25
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for photography
-- ----------------------------
DROP TABLE IF EXISTS `photography`;
CREATE TABLE `photography`  (
  `photography_id` char(56) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `user_id` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `img_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stars` int(8) NULL DEFAULT NULL,
  `collects` int(8) NULL DEFAULT NULL,
  PRIMARY KEY (`photography_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of photography
-- ----------------------------
INSERT INTO `photography` VALUES ('1643165308338434050', '1638085473597394946', '2023-04-04 16:15:13', '20220115112943_IMG_1309-1680596103861.JPG', '婚宴吃的美食', '#美食', NULL, NULL);
INSERT INTO `photography` VALUES ('1643166635189727233', '1638085473597394946', '2023-04-04 16:20:29', 'IMG_20230117_004314-1680596424089.jpg', '帅气的伴郎', '#婚礼', NULL, NULL);
INSERT INTO `photography` VALUES ('1643167009355198465', '1638085473597394946', '2023-04-04 16:21:58', '20220115080115_IMG_0869-1680596514635.JPG', '抢红包的喜悦时刻', '#抢红包', NULL, NULL);
INSERT INTO `photography` VALUES ('1643167700714909697', '1638085473597394946', '2023-04-04 16:24:43', 'IMG_20230113_153541-1680596680794.jpg', '可爱兔子摆件', '#摆件', NULL, NULL);
INSERT INTO `photography` VALUES ('1643167887348854786', '1638085473597394946', '2023-04-04 16:25:28', 'IMG_20230114_150951-1680596719589.jpg', '喜庆自拍', '#喜庆', NULL, NULL);
INSERT INTO `photography` VALUES ('1643168195110105090', '1638085473597394946', '2023-04-04 16:26:41', '20220115084313_IMG_1013-1680596783742.JPG', '要红包，哈哈哈', '#红包', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
