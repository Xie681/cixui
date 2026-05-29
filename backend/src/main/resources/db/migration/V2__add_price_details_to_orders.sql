-- 添加价格明细字段到订单表
ALTER TABLE `orders`
ADD COLUMN `base_price` DECIMAL(10,2) COMMENT '基础价格' AFTER `urgent_factor`,
ADD COLUMN `stitch_fee` DECIMAL(10,2) COMMENT '针数费用' AFTER `base_price`,
ADD COLUMN `color_fee` DECIMAL(10,2) COMMENT '色数费用' AFTER `stitch_fee`,
ADD COLUMN `fabric_fee` DECIMAL(10,2) COMMENT '面料费用' AFTER `color_fee`,
ADD COLUMN `thread_fee` DECIMAL(10,2) COMMENT '线材费用' AFTER `fabric_fee`,
ADD COLUMN `urgent_fee` DECIMAL(10,2) COMMENT '加急费用' AFTER `thread_fee`;
