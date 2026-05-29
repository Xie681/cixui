-- 添加文字定制相关字段到 order_detail 表

ALTER TABLE `order_detail`
    ADD COLUMN `custom_text` VARCHAR(500) COMMENT '文字定制：文字内容' AFTER `unit_price`,
    ADD COLUMN `font_style` VARCHAR(50) COMMENT '文字定制：字体样式' AFTER `custom_text`,
    ADD COLUMN `font_size` INT COMMENT '文字定制：字号大小' AFTER `font_style`,
    ADD COLUMN `text_color` VARCHAR(50) COMMENT '文字定制：文字颜色' AFTER `font_size`;
