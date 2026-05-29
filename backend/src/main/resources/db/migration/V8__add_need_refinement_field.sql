-- ==================== 添加图案精修字段 ====================

-- 为订单详情表添加"是否需要图案精修"字段
ALTER TABLE `order_detail` 
ADD COLUMN `need_refinement` TINYINT DEFAULT 0 COMMENT '是否需要图案精修（0否 1是）' AFTER `text_color`;

-- 添加索引以便查询需要精修的订单
CREATE INDEX idx_need_refinement ON `order_detail` (`need_refinement`);

-- ==================== 字段说明 ====================
-- 
-- need_refinement: 是否需要图案精修
--   0 - 不需要精修，直接使用AI生成的结果
--   1 - 需要精修，由人工设计师进行精细调整
-- 
-- 业务逻辑：
-- 1. 用户在提交订单时可以选择是否需要图案精修
-- 2. 需要精修的订单会进入设计师工作台，由设计师进行人工调整
-- 3. 不需要精修的订单直接使用AI生成的结果进入生产流程
-- 4. 精修服务可能会产生额外费用和延长交付时间
-- 
