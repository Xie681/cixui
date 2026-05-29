-- ==================== 测试用户数据 ====================
-- 密码统一为: Test123456
-- BCrypt 加密后的密码: $2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW

-- 1. 普通用户
INSERT INTO `user` (`phone`, `email`, `password`, `nickname`, `avatar`, `role`, `status`, `created_at`, `updated_at`) VALUES
('13800138001', 'user1@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '测试用户1', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user1', 'USER', 1, NOW(), NOW()),
('13800138002', 'user2@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '测试用户2', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user2', 'USER', 1, NOW(), NOW()),
('13800138003', 'user3@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '测试用户3', 'https://api.dicebear.com/7.x/avataaars/svg?seed=user3', 'USER', 1, NOW(), NOW());

-- 2. 设计师用户
INSERT INTO `user` (`phone`, `email`, `password`, `nickname`, `avatar`, `role`, `status`, `created_at`, `updated_at`) VALUES
('13900139001', 'designer1@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '设计师-张三', 'https://api.dicebear.com/7.x/avataaars/svg?seed=designer1', 'DESIGNER', 1, NOW(), NOW()),
('13900139002', 'designer2@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '设计师-李四', 'https://api.dicebear.com/7.x/avataaars/svg?seed=designer2', 'DESIGNER', 1, NOW(), NOW());

-- 3. 管理员用户
INSERT INTO `user` (`phone`, `email`, `password`, `nickname`, `avatar`, `role`, `status`, `created_at`, `updated_at`) VALUES
('13700137000', 'admin@test.com', '$2a$10$1MGN3iZhl.vrBVSbLzkJAu2dU2oLUjM0TkRfRwNuY5caKKz.36FPW', '系统管理员', 'https://api.dicebear.com/7.x/avataaars/svg?seed=admin', 'ADMIN', 1, NOW(), NOW());

-- 4. 测试收货地址（为用户1添加）
INSERT INTO `user_address` (`user_id`, `name`, `phone`, `province`, `city`, `district`, `detail`, `is_default`, `created_at`) VALUES
(1, '张三', '13800138001', '北京市', '朝阳区', '望京街道', '望京SOHO T3座 1001室', 1, NOW()),
(1, '张三', '13800138001', '上海市', '浦东新区', '陆家嘴街道', '世纪大道88号 金茂大厦 2001室', 0, NOW());

-- 5. 测试收货地址（为用户2添加）
INSERT INTO `user_address` (`user_id`, `name`, `phone`, `province`, `city`, `district`, `detail`, `is_default`, `created_at`) VALUES
(2, '李四', '13800138002', '广东省', '深圳市', '南山区', '科技园南区 腾讯大厦 3001室', 1, NOW());

-- 6. 测试收货地址（为用户3添加）
INSERT INTO `user_address` (`user_id`, `name`, `phone`, `province`, `city`, `district`, `detail`, `is_default`, `created_at`) VALUES
(3, '王五', '13800138003', '浙江省', '杭州市', '西湖区', '文三路 阿里巴巴西溪园区 4001室', 1, NOW());

-- ==================== 测试数据说明 ====================
-- 
-- 所有测试账号密码: Test123456
-- 
-- 账号列表:
-- 1. 普通用户
--    - 手机: 13800138001, 邮箱: user1@test.com, 昵称: 测试用户1
--    - 手机: 13800138002, 邮箱: user2@test.com, 昵称: 测试用户2
--    - 手机: 13800138003, 邮箱: user3@test.com, 昵称: 测试用户3
-- 
-- 2. 设计师
--    - 手机: 13900139001, 邮箱: designer1@test.com, 昵称: 设计师-张三
--    - 手机: 13900139002, 邮箱: designer2@test.com, 昵称: 设计师-李四
-- 
-- 3. 管理员
--    - 手机: 13700137000, 邮箱: admin@test.com, 昵称: 系统管理员
-- 
-- 头像使用 DiceBear API 生成的随机头像
-- 
