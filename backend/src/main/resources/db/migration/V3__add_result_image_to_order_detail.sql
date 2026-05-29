-- 添加结果图字段到订单详情表
-- 用于存储AI生成的刺绣效果图URL

ALTER TABLE `order_detail`
ADD COLUMN `result_image` VARCHAR(512) NULL COMMENT 'AI生成的结果图URL'
AFTER `source_image`;

-- 添加索引以提升查询性能
CREATE INDEX `idx_result_image` ON `order_detail` (`result_image`);
