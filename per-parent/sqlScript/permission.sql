/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : permission

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-01-01 15:20:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_api_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_info`;
CREATE TABLE `tb_api_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority_type` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `handler_method` varchar(255) DEFAULT NULL,
  `patten_url` varchar(255) DEFAULT NULL,
  `request_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey` (`request_method`,`patten_url`,`handler_method`)
) ENGINE=InnoDB AUTO_INCREMENT=341 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info
-- ----------------------------

-- ----------------------------
-- Table structure for tb_api_info_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_info_permission`;
CREATE TABLE `tb_api_info_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey` (`api_info_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info_permission
-- ----------------------------

-- ----------------------------
-- Table structure for tb_api_info_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_info_role`;
CREATE TABLE `tb_api_info_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey` (`api_info_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info_role
-- ----------------------------

-- ----------------------------
-- Table structure for tb_menu
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu`;
CREATE TABLE `tb_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT NULL,
  `enable` bit(1) DEFAULT NULL,
  `href` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `is_parent` bit(1) DEFAULT NULL,
  `menu_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `sort_by` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_qu7i6egv06cdb05xfro21uq1f` (`href`,`menu_name`) USING BTREE,
  UNIQUE KEY `uniqueKey` (`menu_name`,`href`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_menu
-- ----------------------------
INSERT INTO `tb_menu` VALUES ('1', '2019-11-19 17:27:25', '', 'javascript:;', 'layui-icon layui-icon-user i', '', '用户管理', '0', '100');
INSERT INTO `tb_menu` VALUES ('2', '2019-11-02 17:35:50', '', '/page/user@list', 'el-icon-s-tools i', '\0', '用户列表', '1', '100');
INSERT INTO `tb_menu` VALUES ('3', '2019-11-02 17:36:13', '', '/page/role@list', 'el-icon-user-solid i', '\0', '角色列表', '1', '100');
INSERT INTO `tb_menu` VALUES ('4', '2019-11-02 17:36:39', '', '/page/permission@list', null, '\0', '权限列表', '1', '100');
INSERT INTO `tb_menu` VALUES ('5', '2019-11-02 17:36:54', '\0', 'javascript:;', 'layui-icon-chart-screen layui-icon i', '', '日志管理', '0', '98');
INSERT INTO `tb_menu` VALUES ('10', '2019-11-02 17:38:50', '', 'javascript:;', 'layui-icon-loading layui-icon i', '', '项目监控', '0', '97');
INSERT INTO `tb_menu` VALUES ('12', '2019-11-02 17:39:46', '', '/page/monitoring@mysql', 'el-icon-s-tools i', '\0', 'mysql', '10', '97');
INSERT INTO `tb_menu` VALUES ('13', '2019-11-02 17:39:49', '', '/page/monitoring@redis', null, '\0', 'redis', '10', '97');
INSERT INTO `tb_menu` VALUES ('14', '2019-11-02 17:39:53', '', '/page/monitoring@mongodb', null, '\0', 'mongodb', '10', '97');
INSERT INTO `tb_menu` VALUES ('20', '2019-11-08 18:19:09', '', 'javascript:;', 'layui-icon-face-smile-fine layui-icon i', '', '菜单管理', '0', '99');
INSERT INTO `tb_menu` VALUES ('21', '2019-11-08 18:20:54', '', '/page/menu@list', 'el-icon-phone i', '\0', '菜单列表', '20', '99');
INSERT INTO `tb_menu` VALUES ('22', '2019-11-14 08:58:50', '', '/page/log@matter', null, '\0', '操作日志', '5', '50');
INSERT INTO `tb_menu` VALUES ('24', '2019-11-14 09:48:45', '', '/page/log@fileLog', null, '\0', '文件日志', '5', '49');
INSERT INTO `tb_menu` VALUES ('25', '2019-11-18 08:59:22', '', '/page/log@taskLog', 'el-icon-warning-outline i', '\0', '任务日志', '5', '95');
INSERT INTO `tb_menu` VALUES ('26', '2019-11-18 09:12:00', '', 'javascript:;', 'el-icon-date i', '', '任务管理', '0', '100');
INSERT INTO `tb_menu` VALUES ('27', '2019-11-18 09:12:51', '', '/page/quartz@quartz', 'el-icon-phone-outline i', '\0', '任务调度', '26', '0');
INSERT INTO `tb_menu` VALUES ('28', '2019-11-21 18:52:14', '', '/page/sso@sso', 'el-icon-data-line i', '\0', '单点服务', '0', '96');
INSERT INTO `tb_menu` VALUES ('29', '2019-12-11 17:13:22', '', '/page/monitoring@sql', null, '\0', 'sql', '10', '0');
INSERT INTO `tb_menu` VALUES ('30', '2019-12-11 18:41:14', '', '/page/monitoring@system', null, '\0', '系统监控', '10', '0');
INSERT INTO `tb_menu` VALUES ('31', '2019-12-13 20:23:27', '', null, 'el-icon-warning-outline i', '', '远程监控', '0', '97');
INSERT INTO `tb_menu` VALUES ('34', '2019-12-13 20:24:27', '', '/page/monitoring@remote@redis', null, '\0', 'remoteRedis', '31', '0');
INSERT INTO `tb_menu` VALUES ('35', '2019-12-13 20:27:56', '', '/page/monitoring@remote@mysql', null, '\0', 'remoteMysql', '31', '0');
INSERT INTO `tb_menu` VALUES ('36', '2019-12-13 20:28:13', '', '/page/monitoring@remote@mongodb', null, '\0', 'remoteMongo', '31', '0');
INSERT INTO `tb_menu` VALUES ('37', '2019-12-15 15:36:12', '', '', 'el-icon-setting i', '', '接口授权', '0', '97');
INSERT INTO `tb_menu` VALUES ('38', '2019-12-21 13:13:17', '', '/page/oss@index', 'el-icon-s-grid i', '\0', 'oss', '0', '0');
INSERT INTO `tb_menu` VALUES ('40', '2019-12-24 17:33:20', '', '/page/monitoring@remote@sql', null, '\0', 'remotesql', '31', '0');
INSERT INTO `tb_menu` VALUES ('41', '2020-01-01 15:19:20', '', '/page/api@index', null, '\0', '本地控制', '37', '0');
INSERT INTO `tb_menu` VALUES ('42', '2020-01-01 15:19:36', '', '/page/api@remote@index', null, '\0', '远程控制', '37', '0');

-- ----------------------------
-- Table structure for tb_menu_and_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu_and_permission`;
CREATE TABLE `tb_menu_and_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mid` (`mid`,`pid`) USING HASH,
  UNIQUE KEY `uniqueKey` (`mid`,`pid`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_menu_and_permission
-- ----------------------------
INSERT INTO `tb_menu_and_permission` VALUES ('29', '3', '1');
INSERT INTO `tb_menu_and_permission` VALUES ('4', '4', '1');
INSERT INTO `tb_menu_and_permission` VALUES ('30', '10', '6');
INSERT INTO `tb_menu_and_permission` VALUES ('23', '22', '14');
INSERT INTO `tb_menu_and_permission` VALUES ('15', '24', '14');
INSERT INTO `tb_menu_and_permission` VALUES ('28', '25', '14');

-- ----------------------------
-- Table structure for tb_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_permission`;
CREATE TABLE `tb_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `permission_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logogram_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_l08ocenndykx73p6e6d8n1mq` (`permission_name`) USING HASH,
  UNIQUE KEY `UK_6tvygxe75b74p481uy9ihbj0i` (`logogram_name`),
  UNIQUE KEY `uniqueKey` (`permission_name`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_permission
-- ----------------------------
INSERT INTO `tb_permission` VALUES ('1', 'ADMIN_USER_QUERY', '用户管理员查询权限', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1344893384,1482228567&fm=26&gp=0.jpg');
INSERT INTO `tb_permission` VALUES ('2', 'ADMIN_USER_ADD', '用户管理员添加权限', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1344893384,1482228567&fm=26&gp=0.jpg');
INSERT INTO `tb_permission` VALUES ('3', 'ADMIN_USER_DELETE', '用户管理员删除权限', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1344893384,1482228567&fm=26&gp=0.jpg');
INSERT INTO `tb_permission` VALUES ('4', 'ADMIN_USER_UPDATE', '用户管理员更新权限', 'https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1344893384,1482228567&fm=26&gp=0.jpg');
INSERT INTO `tb_permission` VALUES ('5', 'ADMIN_MENU_ADD', '菜单管理员添加权限', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573133926485&di=2c5f7878b222662ba1a427dbd79c8170&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0173e757b32f630000018c1b8767fc.png');
INSERT INTO `tb_permission` VALUES ('6', 'ADMIN_MENU_QUERY', '菜单管理员查询权限', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573133926485&di=2c5f7878b222662ba1a427dbd79c8170&imgtype=0&src=http%3A%2F%2Fimg.zcool.cn%2Fcommunity%2F0173e757b32f630000018c1b8767fc.png');
INSERT INTO `tb_permission` VALUES ('7', 'ADMIN_MENU_DELETE', '菜单管理员删除权限', '/images/err.jpg');
INSERT INTO `tb_permission` VALUES ('13', 'ADMIN_MENU_UPDATE', '菜单管理员更新权限', '/images/err.jpg');
INSERT INTO `tb_permission` VALUES ('14', 'ADMIN_LOG_QUERY', '日志管理员查询权限', '/images/err.jpg');
INSERT INTO `tb_permission` VALUES ('15', 'ADMIN_LOG_UPDATE', '日志管理员更新权限', '/images/err.jpg');
INSERT INTO `tb_permission` VALUES ('16', 'ADMIN_LOG_DELETE', '日志管理员删除权限', '/images/err.jpg');
INSERT INTO `tb_permission` VALUES ('17', 'ADMIN_LOG_ADD', '日志管理员添加权限', '/images/err.jpg');

-- ----------------------------
-- Table structure for tb_remote_api_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_remote_api_info`;
CREATE TABLE `tb_remote_api_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authority_type` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `handler_method` varchar(255) DEFAULT NULL,
  `namespace` varchar(255) DEFAULT NULL,
  `patten_url` varchar(255) DEFAULT NULL,
  `request_method` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `handler_method` (`handler_method`,`namespace`,`patten_url`,`request_method`) USING BTREE,
  UNIQUE KEY `uniqueKey` (`namespace`,`request_method`,`patten_url`,`handler_method`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_remote_api_info
-- ----------------------------

-- ----------------------------
-- Table structure for tb_remote_api_info_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_remote_api_info_permission`;
CREATE TABLE `tb_remote_api_info_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey` (`api_info_id`,`permission_id`),
  KEY `api_info_id` (`api_info_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_remote_api_info_permission
-- ----------------------------

-- ----------------------------
-- Table structure for tb_remote_api_info_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_remote_api_info_role`;
CREATE TABLE `tb_remote_api_info_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueKey` (`api_info_id`,`role_id`),
  KEY `api_info_id` (`api_info_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_remote_api_info_role
-- ----------------------------

-- ----------------------------
-- Table structure for tb_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_role`;
CREATE TABLE `tb_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `icon` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logogram_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_c9lijtmr0x68iu1vxftbu2u33` (`role_name`) USING HASH,
  UNIQUE KEY `UK_hd61rla2c1h10cal5y1asih66` (`logogram_name`),
  UNIQUE KEY `uniqueKey` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_role
-- ----------------------------
INSERT INTO `tb_role` VALUES ('1', 'NORMAL', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572931313396&di=a650346f28f24456d28fda63cd07f0d1&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201501%2F19%2F20150119113454_TejN8.jpeg', '普通角色');
INSERT INTO `tb_role` VALUES ('3', 'USER_ADMIN', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572931586596&di=11912c216349305313e95df76e911260&imgtype=0&src=http%3A%2F%2Fimgfs.oppo.cn%2Fuploads%2Fthread%2Fattachment%2F2017%2F08%2F29%2F15040108092121.jpg', '用户管理员');
INSERT INTO `tb_role` VALUES ('999', 'ADMIN', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572931558991&di=2ba72f13a09db11932cec0635369e691&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn14%2F480%2Fw640h640%2F20180423%2Faa2c-fytnfyp9512257.jpg', '超级管理员');
INSERT INTO `tb_role` VALUES ('1006', 'MENU_ADMIN', 'http://www.lhstack.xyz:8888/image/M00/00/00/rBgwPV3D8CSAVsiKAAQQAGGfKYU164.gif', '菜单管理员');

-- ----------------------------
-- Table structure for tb_role_and_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_role_and_permission`;
CREATE TABLE `tb_role_and_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(3) unsigned DEFAULT NULL,
  `rid` int(3) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `pid` (`pid`,`rid`) USING HASH,
  UNIQUE KEY `uniqueKey` (`pid`,`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_role_and_permission
-- ----------------------------
INSERT INTO `tb_role_and_permission` VALUES ('47', '1', '3');
INSERT INTO `tb_role_and_permission` VALUES ('48', '2', '3');
INSERT INTO `tb_role_and_permission` VALUES ('49', '4', '3');
INSERT INTO `tb_role_and_permission` VALUES ('51', '5', '1006');
INSERT INTO `tb_role_and_permission` VALUES ('52', '6', '1006');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `icon` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `nick_name` varchar(12) COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `is_del` bit(1) DEFAULT b'0',
  `is_lock` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`,`create_time`) USING BTREE,
  UNIQUE KEY `email` (`email`) USING BTREE,
  UNIQUE KEY `nick_name` (`nick_name`) USING HASH,
  UNIQUE KEY `username` (`username`) USING HASH,
  UNIQUE KEY `idcreateTime` (`create_time`,`id`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', 'lhstack@foxmail.com', 'http://www.lhstack.xyz:8888/image/M00/00/00/rBgwPV3wuoSAVhR9AACgADj6cbA612.jpg', '超级管理员', '$2a$10$WXdV2SfjyDUg/c1HqiAufe50MYIvK28Ux0fDkO.QWSP18nQ7uapy2', '$2a$10$/UCvVlpxiw5hTG7MZp10heckA03aJAtoXCfPLCEJ2vczd/vHDn4RW', 'admin', '2019-10-21 16:56:23', '\0', '\0');

-- ----------------------------
-- Table structure for tb_user_and_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_and_role`;
CREATE TABLE `tb_user_and_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) unsigned NOT NULL,
  `rid` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid_rid` (`uid`,`rid`),
  UNIQUE KEY `uniqueKey` (`uid`,`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_user_and_role
-- ----------------------------
INSERT INTO `tb_user_and_role` VALUES ('1', '1', '999');
