/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.200
Source Server Version : 50173
Source Host           : 192.168.0.200:3306
Source Database       : medicinedb

Target Server Type    : MYSQL
Target Server Version : 50173
File Encoding         : 65001

Date: 2018-10-13 09:24:50
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_u_player
-- ----------------------------
DROP TABLE IF EXISTS `t_u_player`;
CREATE TABLE `t_u_player` (
  `openId` varchar(100) NOT NULL COMMENT '用户ID',
  `nickName` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `totalQuestions` int(11) DEFAULT NULL COMMENT '总答题数',
  `answerSuccess` int(11) DEFAULT NULL COMMENT '总正确数',
  `historyCatergorysStr` varchar(2550) DEFAULT NULL COMMENT '历史答题类型记录',
  `historyQuestionsStr` varchar(2550) DEFAULT NULL COMMENT '历史答题ID',
  PRIMARY KEY (`openId`),
  UNIQUE KEY `idx_opend_id` (`openId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
