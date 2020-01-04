/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : permission

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-12-18 22:16:37
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
  UNIQUE KEY `handler_method` (`handler_method`,`patten_url`,`request_method`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info
-- ----------------------------
INSERT INTO `tb_api_info` VALUES ('1', '2', '', 'com.lhstack.controller.log.SysLogController.findAll(page,size,search)---------->org.springframework.http.ResponseEntity', '/log/matter/list', 'POST');
INSERT INTO `tb_api_info` VALUES ('2', '2', '', 'com.lhstack.controller.log.SysLogController.deleteById(id)---------->org.springframework.http.ResponseEntity', '/log/delete/matter/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('3', '2', '', 'com.lhstack.controller.log.SysLogController.findChildFile(fileLogInfo)---------->reactor.core.publisher.Mono', '/log/file/childFile', 'POST');
INSERT INTO `tb_api_info` VALUES ('4', '2', '', 'com.lhstack.controller.log.SysLogController.findChildFile(fullPath,response)---------->reactor.core.publisher.Mono', '/log/file/download', 'GET');
INSERT INTO `tb_api_info` VALUES ('5', '2', '', 'com.lhstack.controller.log.SysLogController.deleteTaskByIds(ids)---------->org.springframework.http.ResponseEntity', '/log/delete/tasks/{ids}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('6', '2', '', 'com.lhstack.controller.log.SysLogController.findRoot()---------->org.springframework.http.ResponseEntity', '/log/file/root', 'GET');
INSERT INTO `tb_api_info` VALUES ('7', '2', '', 'com.lhstack.controller.log.SysLogController.deleteByIds(ids)---------->org.springframework.http.ResponseEntity', '/log/delete/matters/{ids}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('8', '2', '', 'com.lhstack.controller.log.SysLogController.findTaskLogList(page,size,examples)---------->org.springframework.http.ResponseEntity', '/log/task/list', 'POST');
INSERT INTO `tb_api_info` VALUES ('9', '2', '', 'com.lhstack.controller.log.SysLogController.deleteTaskById(id)---------->org.springframework.http.ResponseEntity', '/log/delete/task/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('10', '2', '', 'com.lhstack.controller.log.SysLogController.findChildDirector(fileLogInfo)---------->org.springframework.http.ResponseEntity', '/log/file/childDirector', 'POST');
INSERT INTO `tb_api_info` VALUES ('11', '3', 'state', 'com.lhstack.controller.monitoring.MongoMonitoringController.state()---------->org.springframework.http.ResponseEntity', '/monitoring/mongo/state', 'GET');
INSERT INTO `tb_api_info` VALUES ('12', '3', 'getStats', 'com.lhstack.controller.monitoring.RedisMonitoringController.getStats()---------->org.springframework.http.ResponseEntity', '/monitoring/redis/state', 'GET');
INSERT INTO `tb_api_info` VALUES ('13', '3', 'list', 'com.lhstack.controller.monitoring.SqlMonitoringController.list(page,size)---------->org.springframework.http.ResponseEntity', '/monitoring/mysql/list/{page}/{size}', 'GET');
INSERT INTO `tb_api_info` VALUES ('14', '3', 'list', 'com.lhstack.controller.monitoring.SqlMonitoringController.list()---------->org.springframework.http.ResponseEntity', '/monitoring/mysql/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('15', '3', 'state', 'com.lhstack.controller.monitoring.SqlMonitoringController.state()---------->org.springframework.http.ResponseEntity', '/monitoring/mysql/state', 'GET');
INSERT INTO `tb_api_info` VALUES ('16', '3', 'getState', 'com.lhstack.controller.monitoring.SystemMonitoringController.getState()---------->org.springframework.http.ResponseEntity', '/monitoring/system/state', 'GET');
INSERT INTO `tb_api_info` VALUES ('17', '3', 'update', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.update(id,mongoConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/update/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('18', '3', 'list', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.list()---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('19', '3', 'save', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.save(mongoConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/save', 'POST');
INSERT INTO `tb_api_info` VALUES ('20', '3', 'state', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.state(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/state/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('21', '3', 'del', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.del(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('22', '3', 'findById', 'com.lhstack.controller.monitoring.remote.MongoConnectionInfoController.findById(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mongo/find/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('23', '3', 'update', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.update(id,mysqlConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/update/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('24', '3', 'list', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.list()---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('25', '3', 'save', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.save(mysqlConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/save', 'POST');
INSERT INTO `tb_api_info` VALUES ('26', '3', 'state', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.state(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/state/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('27', '3', 'del', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.del(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('28', '3', 'findById', 'com.lhstack.controller.monitoring.remote.MysqlConnectionInfoController.findById(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/mysql/find/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('29', '3', 'update', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.update(id,redisConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/update/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('30', '3', 'list', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.list()---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('31', '3', 'save', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.save(redisConnectionInfo)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/save', 'POST');
INSERT INTO `tb_api_info` VALUES ('32', '3', 'state', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.state(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/state/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('33', '3', 'del', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.del(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('34', '3', 'findById', 'com.lhstack.controller.monitoring.remote.RedisConnectionInfoController.findById(id)---------->org.springframework.http.ResponseEntity', '/remote/monitoring/redis/find/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('35', '2', '', 'com.lhstack.controller.permission.MenuController.findByPreAllMenu()---------->org.springframework.http.ResponseEntity', '/menu/preList', 'GET');
INSERT INTO `tb_api_info` VALUES ('36', '3', 'findByAllMenu', 'com.lhstack.controller.permission.MenuController.findByAllMenu()---------->org.springframework.http.ResponseEntity', '/menu/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('37', '2', '', 'com.lhstack.controller.permission.MenuController.addMenu(menu,pids)---------->org.springframework.http.ResponseEntity', '/menu/add', 'POST');
INSERT INTO `tb_api_info` VALUES ('38', '2', '', 'com.lhstack.controller.permission.MenuController.updateMenu(menu,pids)---------->org.springframework.http.ResponseEntity', '/menu/update', 'POST');
INSERT INTO `tb_api_info` VALUES ('39', '2', '', 'com.lhstack.controller.permission.MenuController.deleteMenu(id)---------->org.springframework.http.ResponseEntity', '/menu/delete/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('40', '2', '', 'com.lhstack.controller.permission.MenuController.findByParentAllMenu()---------->org.springframework.http.ResponseEntity', '/menu/plist', 'GET');
INSERT INTO `tb_api_info` VALUES ('41', '2', '', 'com.lhstack.controller.permission.MenuController.findAllIgnorePermission()---------->org.springframework.http.ResponseEntity', '/menu/pmsList', 'GET');
INSERT INTO `tb_api_info` VALUES ('42', '2', '', 'com.lhstack.controller.permission.MenuController.findByMidPermission(mid)---------->org.springframework.http.ResponseEntity', '/menu/pms/{mid}', 'GET');
INSERT INTO `tb_api_info` VALUES ('43', '2', '', 'com.lhstack.controller.permission.PermissionController.add(permission)---------->org.springframework.http.ResponseEntity', '/permission/add', 'POST');
INSERT INTO `tb_api_info` VALUES ('44', '2', '', 'com.lhstack.controller.permission.PermissionController.add(id)---------->org.springframework.http.ResponseEntity', '/permission/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('45', '2', '', 'com.lhstack.controller.permission.PermissionController.update(permission)---------->org.springframework.http.ResponseEntity', '/permission/update', 'POST');
INSERT INTO `tb_api_info` VALUES ('46', '2', '', 'com.lhstack.controller.permission.PermissionController.allList()---------->org.springframework.http.ResponseEntity', '/permission/all', 'GET');
INSERT INTO `tb_api_info` VALUES ('47', '2', '', 'com.lhstack.controller.permission.PermissionController.findAllPermission(page,size)---------->org.springframework.http.ResponseEntity', '/permission/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('48', '2', '', 'com.lhstack.controller.permission.RoleController.all()---------->org.springframework.http.ResponseEntity', '/role/all', 'GET');
INSERT INTO `tb_api_info` VALUES ('49', '2', '', 'com.lhstack.controller.permission.RoleController.add(id)---------->org.springframework.http.ResponseEntity', '/role/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('50', '2', '', 'com.lhstack.controller.permission.RoleController.add(pids,role)---------->org.springframework.http.ResponseEntity', '/role/add', 'POST');
INSERT INTO `tb_api_info` VALUES ('51', '2', '', 'com.lhstack.controller.permission.RoleController.update(pids,role)---------->org.springframework.http.ResponseEntity', '/role/update', 'POST');
INSERT INTO `tb_api_info` VALUES ('52', '2', '', 'com.lhstack.controller.permission.RoleController.findAll(page,size)---------->org.springframework.http.ResponseEntity', '/role/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('53', '2', '', 'com.lhstack.controller.permission.UserController.del(id)---------->reactor.core.publisher.Mono', '/user/del/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('54', '2', '', 'com.lhstack.controller.permission.UserController.findAll(examples)---------->reactor.core.publisher.Mono', '/user/page', 'POST');
INSERT INTO `tb_api_info` VALUES ('55', '2', '', 'com.lhstack.controller.permission.UserController.addUser(user,rids)---------->reactor.core.publisher.Mono', '/user/add', 'POST');
INSERT INTO `tb_api_info` VALUES ('56', '2', '', 'com.lhstack.controller.permission.UserController.delAll(ids)---------->reactor.core.publisher.Mono', '/user/del/all/{ids}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('57', '2', '', 'com.lhstack.controller.permission.UserController.findByRoles()---------->reactor.core.publisher.Mono', '/user/roles', 'GET');
INSERT INTO `tb_api_info` VALUES ('58', '2', '', 'com.lhstack.controller.permission.UserController.updateUser(user,rids)---------->reactor.core.publisher.Mono', '/user/update', 'POST');
INSERT INTO `tb_api_info` VALUES ('59', '2', '', 'com.lhstack.controller.permission.UserController.findByUidRole(uid)---------->reactor.core.publisher.Mono', '/user/role/{uid}', 'GET');
INSERT INTO `tb_api_info` VALUES ('60', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.list(page,size)---------->org.springframework.http.ResponseEntity', '/authority/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('61', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.listPermission(apiInfoId)---------->org.springframework.http.ResponseEntity', '/authority/permissions', 'GET');
INSERT INTO `tb_api_info` VALUES ('62', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.updatePermission(permissionIds,apiId)---------->org.springframework.http.ResponseEntity', '/authority/permissions', 'POST');
INSERT INTO `tb_api_info` VALUES ('63', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.listRoles(apiInfoId)---------->org.springframework.http.ResponseEntity', '/authority/roles', 'GET');
INSERT INTO `tb_api_info` VALUES ('64', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.updateRoles(roleIds,apiId)---------->org.springframework.http.ResponseEntity', '/authority/roles', 'POST');
INSERT INTO `tb_api_info` VALUES ('65', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.updateAuthorityType(type,id,description)---------->org.springframework.http.ResponseEntity', '/authority/authorityType/{authorityType}/{apiId}', 'POST');
INSERT INTO `tb_api_info` VALUES ('66', '2', '', 'com.lhstack.controller.quartz.SchedulerController.add(taskEntity)---------->org.springframework.http.ResponseEntity', '/quartz/add', 'POST');
INSERT INTO `tb_api_info` VALUES ('67', '2', '', 'com.lhstack.controller.quartz.SchedulerController.findAll(page,size)---------->org.springframework.http.ResponseEntity', '/quartz/list', 'GET');
INSERT INTO `tb_api_info` VALUES ('68', '2', '', 'com.lhstack.controller.quartz.SchedulerController.deleteById(id)---------->org.springframework.http.ResponseEntity', '/quartz/delete/{id}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('69', '2', '', 'com.lhstack.controller.quartz.SchedulerController.pauseAll()---------->org.springframework.http.ResponseEntity', '/quartz/pauseAll', 'PUT');
INSERT INTO `tb_api_info` VALUES ('70', '2', '', 'com.lhstack.controller.quartz.SchedulerController.deleteByIds(ids)---------->org.springframework.http.ResponseEntity', '/quartz/deletes/{ids}', 'DELETE');
INSERT INTO `tb_api_info` VALUES ('71', '2', '', 'com.lhstack.controller.quartz.SchedulerController.runOnce(id)---------->org.springframework.http.ResponseEntity', '/quartz/runOnce/{id}', 'GET');
INSERT INTO `tb_api_info` VALUES ('72', '2', '', 'com.lhstack.controller.quartz.SchedulerController.updateTask(id,taskEntity)---------->org.springframework.http.ResponseEntity', '/quartz/update/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('73', '2', '', 'com.lhstack.controller.quartz.SchedulerController.pauseTask(id)---------->org.springframework.http.ResponseEntity', '/quartz/pause/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('74', '2', '', 'com.lhstack.controller.quartz.SchedulerController.resumeTask(id)---------->org.springframework.http.ResponseEntity', '/quartz/resume/{id}', 'PUT');
INSERT INTO `tb_api_info` VALUES ('75', '2', '', 'com.lhstack.controller.quartz.SchedulerController.runAll()---------->org.springframework.http.ResponseEntity', '/quartz/runAll', 'PUT');
INSERT INTO `tb_api_info` VALUES ('135', '0', '', 'com.lhstack.controller.permission.api.ApiAuthorityController.list(page,size,apiInfo)---------->org.springframework.http.ResponseEntity', '/authority/list', 'POST');

-- ----------------------------
-- Table structure for tb_api_info_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_info_permission`;
CREATE TABLE `tb_api_info_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `api_info_id` (`api_info_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info_permission
-- ----------------------------
INSERT INTO `tb_api_info_permission` VALUES ('1', '1', '14');
INSERT INTO `tb_api_info_permission` VALUES ('2', '2', '16');
INSERT INTO `tb_api_info_permission` VALUES ('3', '3', '14');
INSERT INTO `tb_api_info_permission` VALUES ('4', '4', '14');
INSERT INTO `tb_api_info_permission` VALUES ('5', '5', '16');
INSERT INTO `tb_api_info_permission` VALUES ('6', '6', '14');
INSERT INTO `tb_api_info_permission` VALUES ('7', '7', '16');
INSERT INTO `tb_api_info_permission` VALUES ('8', '8', '14');
INSERT INTO `tb_api_info_permission` VALUES ('9', '9', '16');
INSERT INTO `tb_api_info_permission` VALUES ('10', '10', '14');
INSERT INTO `tb_api_info_permission` VALUES ('11', '35', '6');
INSERT INTO `tb_api_info_permission` VALUES ('12', '37', '5');
INSERT INTO `tb_api_info_permission` VALUES ('13', '38', '13');
INSERT INTO `tb_api_info_permission` VALUES ('14', '39', '7');
INSERT INTO `tb_api_info_permission` VALUES ('15', '40', '6');
INSERT INTO `tb_api_info_permission` VALUES ('16', '41', '6');
INSERT INTO `tb_api_info_permission` VALUES ('17', '42', '6');
INSERT INTO `tb_api_info_permission` VALUES ('18', '43', '2');
INSERT INTO `tb_api_info_permission` VALUES ('19', '44', '3');
INSERT INTO `tb_api_info_permission` VALUES ('20', '45', '4');
INSERT INTO `tb_api_info_permission` VALUES ('21', '46', '1');
INSERT INTO `tb_api_info_permission` VALUES ('22', '47', '1');
INSERT INTO `tb_api_info_permission` VALUES ('23', '48', '1');
INSERT INTO `tb_api_info_permission` VALUES ('24', '49', '2');
INSERT INTO `tb_api_info_permission` VALUES ('25', '50', '2');
INSERT INTO `tb_api_info_permission` VALUES ('26', '51', '4');
INSERT INTO `tb_api_info_permission` VALUES ('27', '52', '1');
INSERT INTO `tb_api_info_permission` VALUES ('28', '53', '3');
INSERT INTO `tb_api_info_permission` VALUES ('29', '54', '1');
INSERT INTO `tb_api_info_permission` VALUES ('30', '55', '2');
INSERT INTO `tb_api_info_permission` VALUES ('31', '56', '3');
INSERT INTO `tb_api_info_permission` VALUES ('32', '57', '1');
INSERT INTO `tb_api_info_permission` VALUES ('33', '58', '4');
INSERT INTO `tb_api_info_permission` VALUES ('34', '59', '1');

-- ----------------------------
-- Table structure for tb_api_info_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_api_info_role`;
CREATE TABLE `tb_api_info_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_info_id` bigint(20) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `api_info_id` (`api_info_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_api_info_role
-- ----------------------------
INSERT INTO `tb_api_info_role` VALUES ('1', '1', '999');
INSERT INTO `tb_api_info_role` VALUES ('2', '2', '999');
INSERT INTO `tb_api_info_role` VALUES ('3', '3', '999');
INSERT INTO `tb_api_info_role` VALUES ('4', '4', '999');
INSERT INTO `tb_api_info_role` VALUES ('5', '5', '999');
INSERT INTO `tb_api_info_role` VALUES ('6', '6', '999');
INSERT INTO `tb_api_info_role` VALUES ('7', '7', '999');
INSERT INTO `tb_api_info_role` VALUES ('8', '8', '999');
INSERT INTO `tb_api_info_role` VALUES ('9', '9', '999');
INSERT INTO `tb_api_info_role` VALUES ('10', '10', '999');
INSERT INTO `tb_api_info_role` VALUES ('11', '35', '999');
INSERT INTO `tb_api_info_role` VALUES ('12', '37', '999');
INSERT INTO `tb_api_info_role` VALUES ('13', '38', '999');
INSERT INTO `tb_api_info_role` VALUES ('14', '39', '999');
INSERT INTO `tb_api_info_role` VALUES ('15', '40', '999');
INSERT INTO `tb_api_info_role` VALUES ('16', '41', '999');
INSERT INTO `tb_api_info_role` VALUES ('17', '42', '999');
INSERT INTO `tb_api_info_role` VALUES ('18', '43', '999');
INSERT INTO `tb_api_info_role` VALUES ('19', '44', '999');
INSERT INTO `tb_api_info_role` VALUES ('20', '45', '999');
INSERT INTO `tb_api_info_role` VALUES ('21', '46', '999');
INSERT INTO `tb_api_info_role` VALUES ('22', '47', '999');
INSERT INTO `tb_api_info_role` VALUES ('23', '48', '999');
INSERT INTO `tb_api_info_role` VALUES ('24', '49', '999');
INSERT INTO `tb_api_info_role` VALUES ('25', '50', '999');
INSERT INTO `tb_api_info_role` VALUES ('26', '51', '999');
INSERT INTO `tb_api_info_role` VALUES ('27', '52', '999');
INSERT INTO `tb_api_info_role` VALUES ('28', '53', '999');
INSERT INTO `tb_api_info_role` VALUES ('29', '54', '999');
INSERT INTO `tb_api_info_role` VALUES ('30', '55', '999');
INSERT INTO `tb_api_info_role` VALUES ('31', '56', '999');
INSERT INTO `tb_api_info_role` VALUES ('32', '57', '999');
INSERT INTO `tb_api_info_role` VALUES ('33', '58', '999');
INSERT INTO `tb_api_info_role` VALUES ('34', '59', '999');
INSERT INTO `tb_api_info_role` VALUES ('35', '60', '999');
INSERT INTO `tb_api_info_role` VALUES ('36', '61', '999');
INSERT INTO `tb_api_info_role` VALUES ('37', '62', '999');
INSERT INTO `tb_api_info_role` VALUES ('38', '63', '999');
INSERT INTO `tb_api_info_role` VALUES ('39', '64', '999');
INSERT INTO `tb_api_info_role` VALUES ('40', '65', '999');
INSERT INTO `tb_api_info_role` VALUES ('41', '66', '999');
INSERT INTO `tb_api_info_role` VALUES ('42', '67', '999');
INSERT INTO `tb_api_info_role` VALUES ('43', '68', '999');
INSERT INTO `tb_api_info_role` VALUES ('44', '69', '999');
INSERT INTO `tb_api_info_role` VALUES ('45', '70', '999');
INSERT INTO `tb_api_info_role` VALUES ('46', '71', '999');
INSERT INTO `tb_api_info_role` VALUES ('47', '72', '999');
INSERT INTO `tb_api_info_role` VALUES ('48', '73', '999');
INSERT INTO `tb_api_info_role` VALUES ('49', '74', '999');
INSERT INTO `tb_api_info_role` VALUES ('50', '75', '999');
INSERT INTO `tb_api_info_role` VALUES ('51', '135', '999');

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
  UNIQUE KEY `UK_qu7i6egv06cdb05xfro21uq1f` (`menu_name`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

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
INSERT INTO `tb_menu` VALUES ('37', '2019-12-15 15:36:12', '', '/page/api@index', 'el-icon-setting i', '\0', '接口授权', '0', '97');

-- ----------------------------
-- Table structure for tb_menu_and_permission
-- ----------------------------
DROP TABLE IF EXISTS `tb_menu_and_permission`;
CREATE TABLE `tb_menu_and_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mid` (`mid`,`pid`) USING HASH
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
  UNIQUE KEY `UK_6tvygxe75b74p481uy9ihbj0i` (`logogram_name`)
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
  UNIQUE KEY `UK_hd61rla2c1h10cal5y1asih66` (`logogram_name`)
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
  UNIQUE KEY `pid` (`pid`,`rid`) USING HASH
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
  UNIQUE KEY `username` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', '1104375192@qq.com', 'http://www.lhstack.xyz:8888/image/M00/00/00/rBgwPV3wuoSAVhR9AACgADj6cbA612.jpg', '超级管理员', '$2a$10$XuJ1.G8qf6MWgJyzl52mBuoVqGK7CQlWjwsalfcwQM4d0Ng/gYaj6', '$2a$10$lVd0pHRO7uR7ARLJRDOKVO.JxSxpK24tlHz8CZTO8ty7DLIA2G64C', 'admin', '2019-10-21 16:56:23', '\0', '\0');
INSERT INTO `tb_user` VALUES ('14', '1005767018@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin14', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin14', '2019-10-22 10:25:29', '\0', '\0');
INSERT INTO `tb_user` VALUES ('15', '1005767019@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin15', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin15', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('16', '1005767020@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin16', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin16', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('17', '1005767021@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin17', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin17', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('18', '1005767022@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin18', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin18', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('19', '1005767023@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin19', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin19', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('21', '1005767025@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin21', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin21', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('22', '1005767026@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin22', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin22', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('24', '1005767028@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin24', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin24', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('25', '1005767029@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin25', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin25', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('26', '1005767030@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin26', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin26', '2019-10-22 10:25:29', '\0', '\0');
INSERT INTO `tb_user` VALUES ('27', '1005767031@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin27', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin27', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('28', '1005767032@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin28', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin28', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('29', '1005767033@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin29', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin29', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('30', '1005767034@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin30', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin30', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('31', '1005767035@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin31', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin31', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('32', '1005767036@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin32', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin32', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('33', '1005767037@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin33', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin33', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('34', '1005767038@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin355', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin34', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('35', '1005767039@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin35', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin35', '2019-10-22 10:25:29', '\0', '\0');
INSERT INTO `tb_user` VALUES ('36', '1005767055@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin36', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin36', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('37', '1005767040@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin37', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin37', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('38', '1005767041@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin38', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin38', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('39', '1005767042@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin39', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin39', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('40', '1005767043@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin40', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin40', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('41', '1005767044@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin41', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin41', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('42', '1005767045@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin42', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin42', '2019-10-22 10:25:29', '\0', '\0');
INSERT INTO `tb_user` VALUES ('43', '1005767046@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin43', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin43', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('44', '1005767047@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin44', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin44', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('45', '1005767048@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin45', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin45', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('46', '1005767049@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin46', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin46', '2019-10-22 10:25:29', '\0', '');
INSERT INTO `tb_user` VALUES ('47', '1005767050@qq.com', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1572411597&di=e248b06cbdb721e30c3ef7fed70aa165&imgtype=jpg&er=1&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201508%2F26%2F20150826221548_x3SAJ.jpeg', 'admin47', '$2a$10$e3SUxP/3I/yBF9Xq.2vmz.t0xrgbzPGKZEh5/9tDauhAWpEkTv51i', '$2a$10$3WvGU.JeWbypGx.Do/xtP.z327KzmVEq.Y3eb4TNAItt33.o/H/zS', 'admin47', '2019-10-22 10:25:29', '', '');
INSERT INTO `tb_user` VALUES ('75', '1005767007@qq.com', '/images/err.jpg', 'lhstack', '$2a$10$U9yT3vXeviCnTiMH2Bl.wO98PsmLQiOG7NyHcQpG8GPqcwhMWWbMW', '$2a$10$2wv9RxHh55TfO.UwCLxzOuWwuWvsLb.hAZ4rmAKfeHVoW1Zo6aMg6', 'lhstack', '2019-10-31 17:57:14', '\0', '\0');
INSERT INTO `tb_user` VALUES ('76', '100@qq.com', '/images/err.jpg', 'MenuAdmin', '$2a$10$n8785gFTSDj.Y2LjJ3B8FOrrvPtBaHJG2KJrGyOpEvq.bzwWPur3q', '$2a$10$zcSWSrF8Oe80AyEwV1lGf.lx8yoSGxxrE3al3fGIDenaeO7EXqEk6', 'MenuAdmin', '2019-11-12 11:55:57', '\0', '\0');

-- ----------------------------
-- Table structure for tb_user_and_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_and_role`;
CREATE TABLE `tb_user_and_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) unsigned NOT NULL,
  `rid` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid_rid` (`uid`,`rid`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of tb_user_and_role
-- ----------------------------
INSERT INTO `tb_user_and_role` VALUES ('1', '1', '999');
INSERT INTO `tb_user_and_role` VALUES ('33', '14', '1');
INSERT INTO `tb_user_and_role` VALUES ('5', '75', '3');
INSERT INTO `tb_user_and_role` VALUES ('28', '76', '1006');
