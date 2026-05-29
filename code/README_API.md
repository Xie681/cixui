# 刺绣AI服务 API 文档

## 概述

这是一个基于Flask的HTTP API服务，用于将用户上传的图片转换为刺绣图案。服务能够从阿里云OSS等云存储服务下载图片，并使用AI模型进行处理。

## 快速开始

### 1. 安装依赖

```bash
# 安装API服务依赖
pip install -r requirements_api.txt

# 或使用conda环境
conda env create -f environment.yml
conda activate pytorch-CycleGAN-and-pix2pix
pip install flask flask-cors requests
```

### 2. 启动服务

**Windows:**
```bash
start_api_server.bat
```

**Linux/Mac:**
```bash
chmod +x start_api_server.sh
./start_api_server.sh
```

**或直接运行:**
```bash
python api_server.py
```

服务将在 `http://localhost:8001` 启动

### 3. 测试服务

```bash
# 健康检查
curl http://localhost:8001/health
```

## API 接口

### 1. 健康检查

**端点:** `GET /health`

**响应:**
```json
{
  "status": "ok",
  "model_loaded": true,
  "message": "AI服务运行正常"
}
```

---

### 2. 图片转刺绣

**端点:** `POST /api/image-to-embroidery`

**请求参数:**
```json
{
  "image_url": "https://your-oss-bucket.oss-cn-beijing.aliyuncs.com/images/20250122/xxxxx.png",
  "style": "SU",
  "return_base64": false
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| image_url | string | 是 | 图片URL（支持阿里云OSS等） |
| style | string | 否 | 刺绣风格（SU/XIANG/SHU/YUE），默认SU |
| return_base64 | boolean | 否 | 是否返回base64编码的结果，默认false |

**响应:**
```json
{
  "success": true,
  "result_url": "/results/result_xxxxx.png",
  "result_base64": "base64编码数据（如果请求）",
  "stitch_count": 5000,
  "color_count": 8
}
```

**示例:**
```bash
curl -X POST http://localhost:8001/api/image-to-embroidery \
  -H "Content-Type: application/json" \
  -d '{
    "image_url": "https://jy-javaweb.oss-cn-beijing.aliyuncs.com/images/20250122/test.png",
    "style": "SU"
  }'
```

---

### 3. 文字转刺绣

**端点:** `POST /api/text-to-embroidery`

**请求参数:**
```json
{
  "text": "刺绣文字",
  "font": "Arial",
  "size": 48,
  "style": "SU"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| text | string | 是 | 要转换的文字内容 |
| font | string | 否 | 字体名称，默认Arial |
| size | integer | 否 | 字号大小，默认48 |
| style | string | 否 | 刺绣风格，默认SU |

**响应:**
```json
{
  "success": true,
  "result_url": "/results/text_result_xxxxx.png",
  "stitch_count": 800,
  "color_count": 1
}
```

---

### 4. 优化刺绣参数

**端点:** `POST /api/optimize`

**请求参数:**
```json
{
  "image_url": "https://...",
  "target_stitch_count": 5000
}
```

**响应:**
```json
{
  "success": true,
  "optimized_params": {
    "stitch_count": 5000,
    "color_count": 8,
    "recommended_size": "20x20cm"
  }
}
```

---

### 5. 获取结果文件

**端点:** `GET /results/{filename}`

直接返回处理后的图片文件。

**示例:**
```
http://localhost:8001/results/result_abc123.png
```

## 与后端集成

### Spring Boot 调用示例

在你的 Spring Boot 后端中，可以这样调用 AI 服务：

```java
@Service
public class AiServiceClient {

    @Value("${ai.service.url}")
    private String aiServiceUrl;

    private final RestTemplate restTemplate;

    public AiProcessResult processImage(String imageUrl, String style) {
        String url = aiServiceUrl + "/api/image-to-embroidery";

        Map<String, Object> request = new HashMap<>();
        request.put("image_url", imageUrl);
        request.put("style", style);

        ResponseEntity<AiProcessResult> response = restTemplate.postForEntity(
            url,
            request,
            AiProcessResult.class
        );

        return response.getBody();
    }
}
```

### 前端调用示例

```javascript
// frontend/src/api/customize.js
export function processImage(imageUrl, style = 'SU') {
  return request({
    url: '/api/customize/process-image',  // 通过后端代理
    method: 'post',
    data: {
      imageUrl,
      style
    }
  })
}
```

## 配置说明

### application-dev.yml 配置

确保后端配置中 AI 服务地址正确：

```yaml
ai:
  service:
    url: http://localhost:8001
    timeout: 30000
    image-to-embroidery: /api/image-to-embroidery
    text-to-embroidery: /api/text-to-embroidery
    optimize: /api/optimize
```

## 工作流程

```
1. 用户上传图片 → 前端
   ↓
2. 图片上传到阿里云OSS → 后端 FileUploadController
   ↓
3. 获得图片URL（如：https://jy-javaweb.oss-cn-beijing.aliyuncs.com/images/xxx.png）
   ↓
4. 后端调用 AI 服务 → POST /api/image-to-embroidery
   ↓
5. AI 服务下载图片（从OSS URL）
   ↓
6. AI 模型处理图片
   ↓
7. 返回处理结果（针数、颜色数、结果图URL）
   ↓
8. 后端保存到订单详情
   ↓
9. 前端展示结果
```

## 错误处理

### 错误响应格式

```json
{
  "success": false,
  "error": "错误描述信息"
}
```

### 常见错误

| HTTP状态码 | 错误原因 |
|-----------|---------|
| 400 | 缺少必填参数 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误（模型处理失败、网络错误等） |

## 性能优化建议

1. **图片预处理**: 上传前端先压缩图片到合理尺寸（建议不超过2MB）
2. **异步处理**: 对于大图片，考虑使用异步任务队列（Celery）
3. **缓存**: 对相同图片的处理结果进行缓存
4. **负载均衡**: 部署多个AI服务实例，使用Nginx负载均衡

## 日志

服务日志输出位置：
- 控制台输出：实时查看
- 日志文件：`logs/ai_service.log`（需配置）

## 依赖服务

- **PyTorch**: 深度学习框架
- **Flask**: Web 服务框架
- **阿里云 OSS**: 图片存储（通过HTTP下载）

## 模型说明

当前使用的模型：`AttnEmbGAN_st_10`
- 位置：`./checkpoints/AttnEmbGAN_st_10/`
- 类型：基于注意力机制的GAN模型
- 用途：图片转刺绣图案

## 常见问题

**Q: 服务启动失败，提示找不到模型？**
A: 如果 `./checkpoints/AttnEmbGAN_st_10` 不存在，服务会以降级模式运行。你需要先训练模型或下载预训练模型。

**Q: 下载OSS图片失败？**
A: 检查：
1. OSS Bucket 权限是否设置为公共读
2. URL 是否正确且可访问
3. 网络连接是否正常

**Q: 处理图片很慢？**
A: 建议：
1. 使用 GPU 版本的 PyTorch
2. 限制输入图片大小
3. 考虑使用异步任务队列

## 开发计划

- [ ] 完善 AI 模型集成
- [ ] 添加文字转图片功能
- [ ] 实现结果缓存机制
- [ ] 添加批量处理接口
- [ ] 支持更多刺绣风格
- [ ] 添加进度查询接口（异步处理）

## 技术支持

如有问题，请查看日志输出或联系开发团队。
