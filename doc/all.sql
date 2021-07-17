drop table if exists `demo`;
create table `demo` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='ceshi';

insert into `demo` (id, name) values (1, 'ceshi');



-- ----------------------------
-- Table structure for ebook
-- ----------------------------
DROP TABLE IF EXISTS `ebook`;
CREATE TABLE `ebook`  (
                          `id` bigint(20) NOT NULL COMMENT 'id',
                          `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
                          `category1_id` bigint(20) NULL DEFAULT NULL COMMENT '分类1',
                          `category2_id` bigint(20) NULL DEFAULT NULL COMMENT '分类2',
                          `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
                          `cover` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
                          `doc_count` int(11) NULL DEFAULT NULL COMMENT '文档数',
                          `view_count` int(11) NULL DEFAULT NULL COMMENT '阅读数',
                          `vote_count` int(11) NULL DEFAULT NULL COMMENT '点赞数',
                          PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '电子书' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ebook
-- ----------------------------
INSERT INTO `ebook` VALUES (1, 'SpringBoot 入门教程', NULL, NULL, '零基础入门 Java 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook` VALUES (2, 'Vue 入门教程', NULL, NULL, '零基础入门 Vue 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook` VALUES (3, 'Python 入门教程', NULL, NULL, '零基础入门 Python 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook` VALUES (4, 'MySQL 入门教程', NULL, NULL, '零基础入门 MySQL 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);
INSERT INTO `ebook` VALUES (5, 'Oracle 入门教程', NULL, NULL, '零基础入门 Oracle 开发，企业级应用开发最佳首选框架', NULL, NULL, NULL, NULL);