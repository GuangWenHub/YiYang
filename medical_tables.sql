-- 药品表
CREATE TABLE `medicine` (
  `medicine_id` bigint NOT NULL AUTO_INCREMENT COMMENT '药品ID',
  `medicine_name` varchar(255) NOT NULL COMMENT '药品名称',
  `price` decimal(10,2) NOT NULL COMMENT '药品价格',
  `description` text COMMENT '药品说明',
  `stock` int NOT NULL COMMENT '药品库存',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`medicine_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='药品表';

-- 医嘱表
CREATE TABLE `medical_order` (
  `order_id` bigint NOT NULL AUTO_INCREMENT COMMENT '医嘱ID',
  `elderly_id` bigint NOT NULL COMMENT '老人ID',
  `order_content` text NOT NULL COMMENT '医嘱内容',
  `medication_advice` text COMMENT '用药建议',
  `doctor_id` bigint NOT NULL COMMENT '医生ID',
  `order_date` datetime NOT NULL COMMENT '医嘱日期',
  `medication_id` bigint DEFAULT NULL COMMENT '用药表ID',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`order_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  CONSTRAINT `fk_medical_order_elderly` FOREIGN KEY (`elderly_id`) REFERENCES `elderly` (`elderly_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医嘱表';

-- 用药表
CREATE TABLE `medication_record` (
  `record_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用药记录ID',
  `order_id` bigint NOT NULL COMMENT '医嘱ID',
  `medicine_id` bigint NOT NULL COMMENT '药品ID',
  `scheduled_time` datetime NOT NULL COMMENT '计划用药时间',
  `actual_time` datetime DEFAULT NULL COMMENT '实际用药时间',
  `nurse_id` bigint NOT NULL COMMENT '分配护工ID',
  `image_url` varchar(500) DEFAULT NULL COMMENT '打卡图片URL',
  `status` char(1) DEFAULT '0' COMMENT '任务状态（0待执行 1已完成 2异常）',
  `remark` text COMMENT '备注',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  PRIMARY KEY (`record_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_medicine_id` (`medicine_id`),
  KEY `idx_nurse_id` (`nurse_id`),
  CONSTRAINT `fk_medication_record_order` FOREIGN KEY (`order_id`) REFERENCES `medical_order` (`order_id`),
  CONSTRAINT `fk_medication_record_medicine` FOREIGN KEY (`medicine_id`) REFERENCES `medicine` (`medicine_id`),
  CONSTRAINT `fk_medication_record_nurse` FOREIGN KEY (`nurse_id`) REFERENCES `sys_user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用药表';

-- 插入药品表数据
INSERT INTO `medicine` (`medicine_id`, `medicine_name`, `price`, `description`, `stock`, `status`, `create_by`, `create_time`) VALUES
(1001, '阿莫西林胶囊', 15.50, '抗生素类药物，用于治疗细菌感染', 100, '0', 'admin', NOW()),
(1002, '布洛芬缓释胶囊', 22.80, '非甾体抗炎药，用于缓解疼痛和发热', 80, '0', 'admin', NOW()),
(1003, '盐酸氨溴索口服溶液', 35.60, '祛痰药，用于稀释痰液', 60, '0', 'admin', NOW()),
(1004, '硝苯地平缓释片', 42.30, '降压药，用于治疗高血压', 45, '0', 'admin', NOW()),
(1005, '二甲双胍片', 18.90, '降糖药，用于治疗2型糖尿病', 70, '0', 'admin', NOW());

-- 插入医嘱表数据
INSERT INTO `medical_order` (`order_id`, `elderly_id`, `order_content`, `medication_advice`, `doctor_id`, `order_date`, `medication_id`, `create_by`, `create_time`) VALUES
(2001, 1, '患者有轻微感冒症状，建议服用抗生素和退烧药', '阿莫西林胶囊 每次1粒，每日3次，饭后服用；布洛芬缓释胶囊 每次1粒，每日2次，必要时服用', 101, NOW(), 3001,'admin', NOW()),
(2002, 2, '患者有咳嗽、痰多症状', '盐酸氨溴索口服溶液 每次10ml，每日3次，饭后服用', 102, NOW(), 3002,'admin', NOW()),
(2003, 3, '患者血压偏高', '硝苯地平缓释片 每次1片，每日2次，早晚各一次',103, NOW(), 3003,'admin', NOW());

-- 插入用药表数据
INSERT INTO `medication_record` (`record_id`, `order_id`, `medicine_id`, `scheduled_time`, `nurse_id`, `status`, `create_by`, `create_time`) VALUES
(3001, 2001, 1001, NOW(), 2, '0', 'admin', NOW()),
(3002, 2001, 1002, NOW(), 2, '0', 'admin', NOW()),
(3003, 2002, 1003, NOW(), 3, '0', 'admin', NOW()),
(3004, 2003, 1004, NOW(), 4, '0', 'admin', NOW());