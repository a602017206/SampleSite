/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : sampleSite

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 08/03/2022 11:49:21
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
                                  `permission_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键自增',
                                  `permission_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限名称',
                                  `permission_desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限描述',
                                  PRIMARY KEY (`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` (`permission_id`, `permission_name`, `permission_desc`) VALUES (1, 'admin', '超级管理员');
COMMIT;

-- ----------------------------
-- Table structure for sys_permission_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_role`;
CREATE TABLE `sys_permission_role` (
                                       `inner_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
                                       `permission_id` bigint DEFAULT NULL COMMENT '权限id',
                                       `role_id` bigint DEFAULT NULL COMMENT '角色id',
                                       PRIMARY KEY (`inner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色和权限关联表';

-- ----------------------------
-- Records of sys_permission_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission_role` (`inner_id`, `permission_id`, `role_id`) VALUES (1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
                            `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键自增',
                            `role_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
                            `role_desc` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
                            PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` (`role_id`, `role_name`, `role_desc`) VALUES (1, 'admin', '超级管理员');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键自增',
                        `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名称',
                        `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户密码',
                        `email` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
                        `role_id` bigint DEFAULT NULL COMMENT '角色id',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户信息表';

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` (`user_id`, `name`, `password`, `email`, `role_id`, `create_time`) VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@example.com', 1, '2022-03-08 11:48:28');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
