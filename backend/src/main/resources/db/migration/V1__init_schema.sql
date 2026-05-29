-- ==================== 用户相关表 ====================
-- 注意：数据库 cixiu 需要先手动创建或通过连接URL指定

-- 用户表
CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `phone` VARCHAR(20) UNIQUE COMMENT '手机号',
  `email` VARCHAR(100) UNIQUE COMMENT '邮箱',
  `password` VARCHAR(255) COMMENT '密码（BCrypt加密）',
  `nickname` VARCHAR(50) COMMENT '昵称',
  `avatar` VARCHAR(255) COMMENT '头像URL',
  `role` ENUM('USER', 'DESIGNER', 'ADMIN') DEFAULT 'USER',
  `status` TINYINT DEFAULT 1 COMMENT '1正常 0禁用',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_phone (`phone`),
  INDEX idx_email (`email`)
) COMMENT '用户表';

-- 第三方登录绑定表（预留）
CREATE TABLE `user_oauth` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `oauth_type` ENUM('WECHAT', 'QQ', 'ALIPAY') COMMENT '登录类型',
  `oauth_id` VARCHAR(100) COMMENT '第三方用户ID',
  `union_id` VARCHAR(100) COMMENT 'UnionID',
  `access_token` TEXT,
  `refresh_token` TEXT,
  `expires_at` DATETIME,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_oauth (`oauth_type`, `oauth_id`),
  INDEX idx_user_id (`user_id`)
) COMMENT '第三方登录';

-- 收货地址表
CREATE TABLE `user_address` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(50) COMMENT '收货人',
  `phone` VARCHAR(20) COMMENT '手机号',
  `province` VARCHAR(50),
  `city` VARCHAR(50),
  `district` VARCHAR(50),
  `detail` VARCHAR(255) COMMENT '详细地址',
  `is_default` TINYINT DEFAULT 0 COMMENT '是否默认',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_user_id (`user_id`)
) COMMENT '收货地址';

-- ==================== 订单相关表 ====================

-- 订单主表
CREATE TABLE `orders` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_no` VARCHAR(32) UNIQUE COMMENT '订单号',
  `user_id` BIGINT NOT NULL,
  `type` ENUM('IMAGE', 'TEXT', 'TEMPLATE') COMMENT '定制类型',
  `status` ENUM('PENDING_PAYMENT', 'DESIGNING', 'SAMPLING', 'REVIEWING', 'SCHEDULING', 'EMBROIDERING', 'QUALITY_CHECK', 'PENDING_SHIP', 'SHIPPED', 'COMPLETED', 'CANCELLED') DEFAULT 'PENDING_PAYMENT',
  `total_amount` DECIMAL(10,2) COMMENT '总金额',
  `paid_amount` DECIMAL(10,2) COMMENT '实付金额',
  `address_snapshot` JSON COMMENT '收货地址快照',
  `remark` TEXT COMMENT '备注',
  `is_urgent` TINYINT DEFAULT 0 COMMENT '是否加急',
  `urgent_factor` DECIMAL(3,2) DEFAULT 1.00 COMMENT '加急系数',
  `paid_at` DATETIME COMMENT '支付时间',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order_no (`order_no`),
  INDEX idx_user_id (`user_id`),
  INDEX idx_status (`status`)
) COMMENT '订单表';

-- 订单详情表
CREATE TABLE `order_detail` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `design_id` BIGINT COMMENT '设计稿ID',
  `template_id` BIGINT COMMENT '模板ID',
  `source_image` VARCHAR(255) COMMENT '原始图片URL',
  `embroidery_style` ENUM('SU', 'XIANG', 'SHU', 'YUE') COMMENT '绣制风格',
  `width` DECIMAL(8,2) COMMENT '宽度(cm)',
  `height` DECIMAL(8,2) COMMENT '高度(cm)',
  `fabric_type` VARCHAR(50) COMMENT '面料类型',
  `thread_type` VARCHAR(50) COMMENT '线材类型',
  `stitch_count` INT COMMENT '针数',
  `color_count` INT COMMENT '色数',
  `trim_count` INT COMMENT '剪线次数',
  `quantity` INT DEFAULT 1 COMMENT '数量',
  `unit_price` DECIMAL(10,2) COMMENT '单价',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`),
  INDEX idx_design_id (`design_id`)
) COMMENT '订单详情';

-- 订单状态流转记录表
CREATE TABLE `order_status_log` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `from_status` VARCHAR(50),
  `to_status` VARCHAR(50),
  `operator_id` BIGINT COMMENT '操作人ID',
  `operator_name` VARCHAR(50),
  `remark` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`)
) COMMENT '订单状态流转';

-- ==================== 设计相关表 ====================

-- 设计稿表
CREATE TABLE `design` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT,
  `designer_id` BIGINT COMMENT '设计师ID',
  `version` INT DEFAULT 1 COMMENT '版本号',
  `status` ENUM('AI_GENERATED', 'DESIGNING', 'PENDING_REVIEW', 'APPROVED', 'REJECTED') DEFAULT 'AI_GENERATED',
  `source_type` ENUM('IMAGE', 'TEXT', 'TEMPLATE') COMMENT '来源类型',
  `source_data` JSON COMMENT '原始数据',
  `ai_result` JSON COMMENT 'AI生成结果（针法数据）',
  `design_data` JSON COMMENT '设计师精修后的数据',
  `preview_image` VARCHAR(255) COMMENT '预览图',
  `stitch_count` INT,
  `color_count` INT,
  `trim_count` INT,
  `file_format` VARCHAR(20) COMMENT '输出格式（DST/PES）',
  `file_url` VARCHAR(255) COMMENT '设计文件URL',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`),
  INDEX idx_designer_id (`designer_id`)
) COMMENT '设计稿';

-- AI处理记录表
CREATE TABLE `ai_process_log` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `design_id` BIGINT,
  `request_data` JSON COMMENT '请求参数',
  `response_data` JSON COMMENT 'AI返回结果',
  `status` ENUM('PENDING', 'PROCESSING', 'SUCCESS', 'FAILED'),
  `error_msg` TEXT,
  `process_time` INT COMMENT '处理耗时(ms)',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_design_id (`design_id`)
) COMMENT 'AI处理日志';

-- ==================== 模板与素材表 ====================

-- 模板表
CREATE TABLE `template` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) COMMENT '模板名称',
  `category` VARCHAR(50) COMMENT '分类',
  `tags` JSON COMMENT '标签',
  `preview_image` VARCHAR(255),
  `thumbnail` VARCHAR(255),
  `design_data` JSON COMMENT '设计数据',
  `stitch_count` INT,
  `color_count` INT,
  `base_price` DECIMAL(10,2) COMMENT '基础价格',
  `is_customizable` TINYINT DEFAULT 1 COMMENT '是否可二次编辑',
  `sales_count` INT DEFAULT 0 COMMENT '销量',
  `status` TINYINT DEFAULT 1 COMMENT '1上架 0下架',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_category (`category`)
) COMMENT '模板库';

-- 字体库表
CREATE TABLE `font_library` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `font_name` VARCHAR(100),
  `font_family` VARCHAR(100),
  `font_file` VARCHAR(255) COMMENT '字体文件URL',
  `preview_image` VARCHAR(255),
  `style` VARCHAR(50) COMMENT '风格（楷书/隶书等）',
  `is_default` TINYINT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '字体库';

-- 排版模板表
CREATE TABLE `layout_template` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) COMMENT '模板名称（校徽/云肩）',
  `type` VARCHAR(50),
  `layout_config` JSON COMMENT '布局配置（位置/大小）',
  `preview_image` VARCHAR(255),
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '排版模板';

-- ==================== 材料库表 ====================

-- 材料库表
CREATE TABLE `material` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `type` ENUM('FABRIC', 'THREAD') COMMENT '材料类型',
  `name` VARCHAR(100),
  `brand` VARCHAR(50),
  `code` VARCHAR(50) COMMENT '材料编码',
  `color` VARCHAR(50),
  `color_code` VARCHAR(20) COMMENT '色号',
  `rgb_value` VARCHAR(20) COMMENT 'RGB值',
  `properties` JSON COMMENT '属性（缩率/张力等）',
  `unit_price` DECIMAL(10,2),
  `stock` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '材料库';

-- ==================== 生产相关表 ====================

-- 生产任务表
CREATE TABLE `production_task` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `design_id` BIGINT NOT NULL,
  `device_id` BIGINT COMMENT '设备ID',
  `factory_id` BIGINT COMMENT '工厂ID',
  `priority` TINYINT DEFAULT 0 COMMENT '优先级',
  `status` ENUM('PENDING', 'QUEUE', 'PROCESSING', 'PAUSED', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
  `progress` DECIMAL(5,2) DEFAULT 0.00 COMMENT '进度百分比',
  `current_stitch` INT DEFAULT 0 COMMENT '当前针数',
  `total_stitch` INT COMMENT '总针数',
  `estimated_time` INT COMMENT '预计耗时(分钟)',
  `actual_start_time` DATETIME,
  `actual_end_time` DATETIME,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`),
  INDEX idx_device_id (`device_id`)
) COMMENT '生产任务';

-- 设备表
CREATE TABLE `device` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `device_no` VARCHAR(50) UNIQUE COMMENT '设备编号',
  `device_name` VARCHAR(100),
  `brand` VARCHAR(50) COMMENT '品牌',
  `model` VARCHAR(50) COMMENT '型号',
  `factory_id` BIGINT,
  `frame_width` DECIMAL(8,2) COMMENT '绣框宽度(cm)',
  `frame_height` DECIMAL(8,2) COMMENT '绣框高度(cm)',
  `max_speed` INT COMMENT '最高转速(rpm)',
  `protocol` VARCHAR(50) COMMENT '联机协议',
  `supported_formats` JSON COMMENT '支持的文件格式',
  `status` ENUM('ONLINE', 'OFFLINE', 'WORKING', 'MAINTENANCE') DEFAULT 'OFFLINE',
  `current_task_id` BIGINT COMMENT '当前任务ID',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_factory_id (`factory_id`)
) COMMENT '设备表';

-- 工厂表
CREATE TABLE `factory` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100),
  `code` VARCHAR(50) UNIQUE,
  `address` VARCHAR(255),
  `contact_name` VARCHAR(50),
  `contact_phone` VARCHAR(20),
  `capacity` INT COMMENT '日产能',
  `status` TINYINT DEFAULT 1,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '工厂表';

-- ==================== 三库表 ====================

-- 工艺知识库表
CREATE TABLE `craft_knowledge` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(100) COMMENT '知识点名称',
  `type` ENUM('STITCH_TEMPLATE', 'ELEMENT_STRATEGY', 'QC_STANDARD') COMMENT '类型',
  `category` VARCHAR(50),
  `content` JSON COMMENT '具体内容',
  `parameters` JSON COMMENT '参数配置',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '工艺知识库';

-- ==================== 质检与售后表 ====================

-- 质检表
CREATE TABLE `quality_check` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `task_id` BIGINT,
  `inspector_id` BIGINT COMMENT '质检员ID',
  `result` ENUM('PASS', 'REJECT', 'REWORK') COMMENT '质检结果',
  `defect_types` JSON COMMENT '缺陷类型列表',
  `images` JSON COMMENT '质检照片',
  `remark` TEXT,
  `checked_at` DATETIME,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`)
) COMMENT '质检记录';

-- 售后工单表
CREATE TABLE `after_sales` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `type` ENUM('REFUND', 'RETURN', 'EXCHANGE', 'REPAIR') COMMENT '售后类型',
  `reason` VARCHAR(255),
  `description` TEXT,
  `images` JSON COMMENT '问题图片',
  `status` ENUM('PENDING', 'PROCESSING', 'APPROVED', 'REJECTED', 'COMPLETED') DEFAULT 'PENDING',
  `handler_id` BIGINT COMMENT '处理人ID',
  `handle_result` TEXT,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_order_id (`order_id`),
  INDEX idx_user_id (`user_id`)
) COMMENT '售后工单';
