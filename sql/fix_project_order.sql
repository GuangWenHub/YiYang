-- 修复 project_order 表缺少 creator_id 字段的问题
USE ry_vue;

-- 添加 creator_id 列到 project_order 表
ALTER TABLE `project_order` 
ADD COLUMN `creator_id` bigint NOT NULL COMMENT '开单人用户 ID' AFTER `item_id`;

-- 为已存在的数据设置默认的 creator_id 值 (可以根据实际情况调整)
UPDATE `project_order` SET `creator_id` = 101 WHERE `creator_id` IS NULL;
