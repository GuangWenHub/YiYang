-- 生活照料记录表
CREATE TABLE `care_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `nurse_id` bigint NOT NULL COMMENT '护工ID',
  `care_type` varchar(50) NOT NULL COMMENT '照料类型',
  `care_content` text NOT NULL COMMENT '照料内容',
  `care_time` datetime NOT NULL COMMENT '照料时间',
  `status` char(1) DEFAULT '0' COMMENT '状态（0待执行 1已完成 2异常）',
  `image_url` varchar(500) DEFAULT NULL COMMENT '打卡图片URL',
  `remark` text COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`record_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_nurse_id` (`nurse_id`),
  CONSTRAINT `fk_care_record_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly` (`elderly_id`),
  CONSTRAINT `fk_care_record_nurse` FOREIGN KEY (`nurse_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='生活照料记录表';

-- 活动陪伴记录表
CREATE TABLE `activity_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `nurse_id` bigint NOT NULL COMMENT '护工ID',
  `activity_type` varchar(50) NOT NULL COMMENT '活动类型',
  `activity_content` text NOT NULL COMMENT '活动内容',
  `activity_time` datetime NOT NULL COMMENT '活动时间',
  `duration` int NOT NULL COMMENT '活动时长（分钟）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0待执行 1已完成 2异常）',
  `image_url` varchar(500) DEFAULT NULL COMMENT '打卡图片URL',
  `remark` text COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`record_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_nurse_id` (`nurse_id`),
  CONSTRAINT `fk_activity_record_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly` (`elderly_id`),
  CONSTRAINT `fk_activity_record_nurse` FOREIGN KEY (`nurse_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动陪伴记录表';

-- 插入生活照料记录数据
INSERT INTO `care_record` (`record_id`, `elderly_id`, `nurse_id`, `care_type`, `care_content`, `care_time`, `status`, `create_by`, `create_time`) VALUES
(4001, 1, 2, '0', '协助老人进食，确保营养均衡', NOW(), '0', 'admin', NOW()),
(4002, 2, 3, '1', '协助老人起床、穿衣、洗漱', NOW(), '0', 'admin', NOW()),
(4003, 3, 4, '2', '协助老人洗澡、理发', NOW(), '0', 'admin', NOW()),
(4004, 1, 2, '3', '协助老人进行康复训练', NOW(), '0', 'admin', NOW()),
(4005, 2, 3, '4', '与老人聊天，缓解孤独感', NOW(), '0', 'admin', NOW());

-- 插入活动陪伴记录数据
INSERT INTO `activity_record` (`record_id`, `elderly_id`, `nurse_id`, `activity_type`, `activity_content`, `activity_time`, `duration`, `status`, `create_by`, `create_time`) VALUES
(5001, 1, 2, '0', '陪老人在花园散步', NOW(), 30, '0', 'admin', NOW()),
(5002, 2, 3, '1', '陪老人下棋、打牌', NOW(), 60, '0', 'admin', NOW()),
(5003, 3, 4, '2', '陪老人阅读、听音乐', NOW(), 45, '0', 'admin', NOW()),
(5004, 1, 2, '3', '陪老人参加社区活动', NOW(), 90, '0', 'admin', NOW()),
(5005, 2, 3, '4', '陪老人做手工、拼图', NOW(), 50, '0', 'admin', NOW());

-- 插入生活照料类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(101, '生活照料类型', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '生活照料类型字典');

-- 插入生活照料类型字典数据
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(1001, 1, '饮食照料', '0', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '饮食照料'),
(1002, 2, '起居照料', '1', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '起居照料'),
(1003, 3, '个人卫生', '2', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '个人卫生'),
(1004, 4, '康复护理', '3', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '康复护理'),
(1005, 5, '情绪疏导', '4', 'care_type', '0', 'admin', NOW(), 'admin', NOW(), '情绪疏导');

-- 插入活动类型字典
INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(102, '活动类型', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '活动类型字典');

-- 插入活动类型字典数据
INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES
(2001, 1, '户外活动', '0', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '户外活动'),
(2002, 2, '室内活动', '1', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '室内活动'),
(2003, 3, '文化活动', '2', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '文化活动'),
(2004, 4, '社交活动', '3', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '社交活动'),
(2005, 5, '益智活动', '4', 'activity_type', '0', 'admin', NOW(), 'admin', NOW(), '益智活动');