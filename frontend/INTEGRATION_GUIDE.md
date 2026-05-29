# AI 服务集成完成指南

## 🎉 集成已完成

AI 服务已成功集成到刺绣定制系统中，支持从阿里云 OSS 获取用户上传的图片，处理后将结果上传回 OSS。

---

## 📋 完成的修改

### Python AI 服务 (code/)

1. **`api_server.py`** - 修复模型加载 ✅
   - 使用 `argparse.Namespace` 直接构造参数，不依赖命令行
   - 配置 `AttenEmbGAN` 模型
   - 实现真实 AI 模型推理
   - 总是返回 base64 编码的结果图

2. **`cleanup_old_results.py`** - 自动清理脚本 ✅
   - 自动删除 7 天前的结果文件
   - 可设置为定时任务

### Spring Boot 后端 (backend/)

3. **`AiServiceClient.java`** - AI 服务客户端 ✅
   - 调用 Python AI 服务
   - 接收 base64 结果
   - 自动上传到阿里云 OSS
   - 返回 OSS URL

4. **`CustomizeServiceImpl.java`** - 定制服务实现 ✅
   - 集成 AI 服务调用
   - 使用真实 AI 返回的针数和颜色数
   - 保存结果图 URL 到订单

5. **`OrderDetail.java`** - 订单详情实体 ✅
   - 新增 `resultImage` 字段存储结果图 URL

6. **`V3__add_result_image_to_order_detail.sql`** - 数据库迁移 ✅
   - 添加 `result_image` 列
   - 创建索引

---

## 🚀 启动步骤

### 1. 启动 AI 服务

```bash
cd code
python api_server.py
```

**预期输出：**
```
==================================================
刺绣AI服务启动中...
==================================================
初始化AI模型...
✅ 模型加载成功: AttnEmbGAN_st_10
启动HTTP服务，监听端口: 8001
 * Running on http://0.0.0.0:8001
```

如果出现 GPU 相关警告但服务正常启动，可以忽略（会使用 CPU 运行）。

### 2. 启动 Spring Boot 后端

```bash
cd backend
mvn spring-boot:run
```

Flyway 会自动执行数据库迁移，添加 `result_image` 字段。

### 3. 启动前端（可选）

```bash
cd frontend
npm run dev
```

---

## 🔄 完整工作流程

```
1. 用户上传图片
   ↓
2. 前端 → 后端 /api/file/upload/image
   ↓
3. 后端上传到 OSS → 返回原图 URL
   ↓
4. 前端提交定制 → /api/customize/submit
   ↓
5. 后端 CustomizeService:
   - 调用 AI 服务 (http://localhost:8001/api/image-to-embroidery)
   - 传入原图 OSS URL
   ↓
6. AI 服务:
   - 从 OSS 下载原图
   - AI 模型处理
   - 返回 base64 结果 + 针数/颜色数
   ↓
7. 后端 AiServiceClient:
   - 解码 base64
   - 上传结果图到 OSS (designs/ 目录)
   - 获得结果图 URL
   ↓
8. 后端创建订单:
   - source_image: 原图 URL
   - result_image: 结果图 URL ✅ 新增
   - stitch_count: AI 返回的针数
   - color_count: AI 返回的颜色数
   ↓
9. 订单创建成功
```

---

## 🧪 测试方法

### 测试 1: AI 服务健康检查

```bash
curl http://localhost:8001/health
```

**期望响应：**
```json
{
  "status": "ok",
  "model_loaded": true,
  "message": "AI服务运行正常"
}
```

### 测试 2: 图片处理（使用公开测试图）

```bash
curl -X POST http://localhost:8001/api/image-to-embroidery \
  -H "Content-Type: application/json" \
  -d '{
    "image_url": "https://picsum.photos/200/200",
    "style": "SU"
  }'
```

**期望响应：**
```json
{
  "success": true,
  "result_base64": "iVBORw0KGgoAAAANSUhEUgAA...",
  "stitch_count": 5000,
  "color_count": 8,
  "original_filename": "result_xxx.png"
}
```

### 测试 3: 完整定制流程

1. 使用 Postman 或前端上传图片
2. 获得图片 URL（如 `https://jy-javaweb.oss-cn-beijing.aliyuncs.com/images/20250122/xxx.png`）
3. 提交定制订单
4. 检查数据库 `order_detail` 表的 `result_image` 字段

---

## 📊 数据库变更

新增字段：`order_detail.result_image`

```sql
-- 查看订单的原图和结果图
SELECT
    id,
    order_id,
    source_image,     -- 原图 URL
    result_image,     -- AI生成的结果图 URL
    stitch_count,
    color_count
FROM order_detail
WHERE order_id = ?;
```

---

## ⚙️ 配置说明

### AI 服务配置 (application-dev.yml)

```yaml
ai:
  service:
    url: http://localhost:8001
    timeout: 30000
```

### OSS 配置 (application-dev.yml)

```yaml
aliyun:
  oss:
    endpoint: oss-cn-beijing.aliyuncs.com
    access-key-id: YOUR_ACCESS_KEY
    access-key-secret: YOUR_SECRET_KEY
    bucket-name: jy-javaweb
```

---

## 🗂️ 文件存储结构

```
阿里云 OSS Bucket (jy-javaweb)
├── images/           # 用户上传的原图
│   └── 20250122/
│       └── abc123.png
├── designs/          # AI生成的结果图 ✅ 新增
│   └── 20250122/
│       └── result_abc123.png
└── avatars/          # 用户头像
```

---

## 🔧 定时清理任务

### Windows 计划任务

```batch
schtasks /create /tn "清理AI结果文件" /tr "python E:\Projects\Java projects\cixiu\code\cleanup_old_results.py" /sc daily /st 02:00
```

### Linux cron

```bash
# 编辑 crontab
crontab -e

# 添加以下行（每天凌晨2点执行）
0 2 * * * cd /path/to/cixiu/code && python cleanup_old_results.py
```

---

## 🐛 常见问题

### Q1: AI 服务启动失败，提示找不到模块

**解决方案：**
```bash
cd code
pip install flask flask-cors requests pillow torch torchvision
```

### Q2: 模型加载失败

**检查：**
- 确认 `code/checkpoints/AttnEmbGAN_st_10/` 目录存在
- 确认包含 `latest_net_Gen.pth` 文件

### Q3: AI 服务调用超时

**检查：**
1. AI 服务是否正常运行：`curl http://localhost:8001/health`
2. 图片是否过大（建议 < 2MB）
3. 网络连接是否正常

### Q4: 结果图没有保存到 OSS

**检查：**
1. OSS 配置是否正确
2. AccessKey 是否有写权限
3. 查看后端日志：`AI处理结果已上传至OSS: ...`

### Q5: 数据库字段不存在

**解决方案：**
```bash
# 重启后端，让 Flyway 执行迁移
mvn spring-boot:run
```

或手动执行：
```sql
ALTER TABLE order_detail
ADD COLUMN result_image VARCHAR(512) NULL COMMENT 'AI生成的结果图URL'
AFTER source_image;
```

---

## 📈 性能优化建议

1. **使用 GPU**
   - 在 `api_server.py` 的 `opt.gpu_ids` 中配置 GPU ID
   - 安装 CUDA 版本的 PyTorch

2. **图片压缩**
   - 前端上传前压缩图片到合理大小

3. **异步处理**
   - 对于大批量订单，考虑使用消息队列（如 RabbitMQ）

4. **结果缓存**
   - 对相同图片的处理结果进行缓存

---

## ✅ 验收清单

- [ ] AI 服务正常启动
- [ ] 健康检查接口返回正常
- [ ] 能从 OSS URL 下载图片
- [ ] AI 模型能正确处理图片
- [ ] 结果图成功上传到 OSS
- [ ] 订单详情包含 `result_image` URL
- [ ] 前端能显示原图和结果图
- [ ] 本地结果文件能自动清理

---

## 📞 技术支持

如有问题，请查看：
1. AI 服务日志（控制台输出）
2. Spring Boot 日志
3. 数据库 `order_detail` 表

生成的文件列表：
- `code/api_server.py` - 已修改
- `code/cleanup_old_results.py` - 新建
- `backend/.../AiServiceClient.java` - 新建
- `backend/.../CustomizeServiceImpl.java` - 已修改
- `backend/.../OrderDetail.java` - 已修改
- `backend/.../V3__add_result_image_to_order_detail.sql` - 新建
