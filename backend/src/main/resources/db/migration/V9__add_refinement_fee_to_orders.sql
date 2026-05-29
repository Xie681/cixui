-- ==================== 为订单表添加精修费用字段 ====================

-- 为订单主表添加精修费用字段
ALTER TABLE `orders` 
ADD COLUMN `refinement_fee` DECIMAL(10,2) DEFAULT 0.00 COMMENT '精修费用' AFTER `urgent_fee`;

-- ==================== 字段说明 ====================
-- 
-- refinement_fee: 精修费用
--   记录订单中的图案精修服务费用
--   如果订单详情中 need_refinement = 1，则此字段会有值
--   默认为 0.00（不需要精修）
-- 
