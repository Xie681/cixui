# 阿里云OSS配置说明

## 一、阿里云OSS准备工作

### 1. 注册阿里云账号
访问 [阿里云官网](https://www.aliyun.com/) 注册账号并完成实名认证。

### 2. 开通OSS服务
1. 登录阿里云控制台
2. 搜索并进入"对象存储OSS"
3. 点击"立即开通"开通OSS服务

### 3. 创建Bucket
1. 进入OSS管理控制台
2. 点击"创建Bucket"
3. 配置Bucket信息：
   - **Bucket名称**：cixiu（全局唯一，可自定义）
   - **地域**：选择离你最近的地域（如：华东1-杭州）
   - **存储类型**：标准存储
   - **读写权限**：公共读（如果需要直接访问图片）或私有（通过签名URL访问）
   - **服务器端加密**：根据需要选择
4. 点击"确定"创建Bucket

### 4. 获取AccessKey
1. 点击右上角头像 → "AccessKey管理"
2. 如果是子账户，建议创建RAM子账户并授予OSS权限
3. 创建AccessKey，记录：
   - **AccessKey ID**
   - **AccessKey Secret**（只显示一次，请妥善保存）

⚠️ **安全提示**：不要将AccessKey泄露给他人，建议使用RAM子账户并设置最小权限。

---

## 二、项目配置

### 1. Maven依赖
已在 `pom.xml` 中添加阿里云OSS依赖：

```xml
<!-- 阿里云OSS -->
<dependency>
    <groupId>com.aliyun.oss</groupId>
    <artifactId>aliyun-sdk-oss</artifactId>
    <version>3.17.4</version>
</dependency>
```

### 2. 配置文件
在 `application.yml` 中配置OSS参数：

```yaml
aliyun:
  oss:
    # OSS服务地域节点
    # 华东1（杭州）：oss-cn-hangzhou.aliyuncs.com
    # 华东2（上海）：oss-cn-shanghai.aliyuncs.com
    # 华北2（北京）：oss-cn-beijing.aliyuncs.com
    # 华南1（深圳）：oss-cn-shenzhen.aliyuncs.com
    endpoint: oss-cn-hangzhou.aliyuncs.com

    # AccessKey ID（从阿里云控制台获取）
    access-key-id: your-access-key-id

    # AccessKey Secret（从阿里云控制台获取）
    access-key-secret: your-access-key-secret

    # Bucket名称
    bucket-name: cixiu

    # 自定义域名（可选，如果配置了CDN加速域名）
    # 例如：https://cdn.yourdomain.com
    custom-domain:
```

### 3. 安全配置建议
生产环境建议将敏感信息（AccessKey等）配置到环境变量或配置中心：

#### 方式一：环境变量
```bash
export ALIYUN_OSS_ACCESS_KEY_ID=your-access-key-id
export ALIYUN_OSS_ACCESS_KEY_SECRET=your-access-key-secret
```

然后在配置文件中引用：
```yaml
aliyun:
  oss:
    access-key-id: ${ALIYUN_OSS_ACCESS_KEY_ID}
    access-key-secret: ${ALIYUN_OSS_ACCESS_KEY_SECRET}
```

#### 方式二：配置文件分离
创建 `application-prod.yml` 用于生产环境，不提交到Git。

---

## 三、功能特性

### 1. 文件上传
```java
@Autowired
private FileStorageService fileStorageService;

// 上传MultipartFile
String fileUrl = fileStorageService.uploadFile(file, "images");

// 上传InputStream
String fileUrl = fileStorageService.uploadFile(
    inputStream,
    fileName,
    "images",
    "image/jpeg"
);
```

### 2. 文件删除
```java
fileStorageService.deleteFile(fileUrl);
```

### 3. 文件访问URL
上传成功后返回的URL格式：
- **公共读Bucket**：`https://{bucket}.{endpoint}/{objectName}`
- **自定义域名**：`https://cdn.yourdomain.com/{objectName}`

### 4. 签名URL（私有文件访问）
如果Bucket设置为私有，可以生成临时签名URL：
```java
@Autowired
private AliyunOssFileStorageService ossService;

// 生成1小时有效期的签名URL
String signedUrl = ossService.generatePresignedUrl(objectName, 3600);
```

---

## 四、OSS存储结构

项目文件按照以下目录结构存储：

```
cixiu (bucket)
├── images/              # 定制图片
│   ├── 20250111/
│   │   ├── uuid1.jpg
│   │   └── uuid2.png
│   └── 20250112/
├── avatars/             # 用户头像
│   ├── 20250111/
│   └── 20250112/
└── designs/             # 设计文件
    ├── 20250111/
    └── 20250112/
```

每个文件都会：
1. 按日期（yyyyMMdd）分目录
2. 使用UUID生成唯一文件名
3. 保留原始文件扩展名

---

## 五、常见问题

### 1. AccessDenied错误
- **原因**：AccessKey权限不足或配置错误
- **解决**：
  1. 检查AccessKey是否正确
  2. 确认RAM账户是否有OSS权限
  3. 检查Bucket权限策略

### 2. NoSuchBucket错误
- **原因**：Bucket不存在或名称错误
- **解决**：
  1. 检查Bucket名称是否正确
  2. 确认Bucket所在地域与endpoint一致

### 3. InvalidAccessKeyId错误
- **原因**：AccessKey ID不正确
- **解决**：重新从阿里云控制台获取AccessKey

### 4. 跨域问题
如果前端直传OSS，需要配置CORS：
1. 进入OSS控制台 → Bucket列表 → 选择Bucket
2. 进入"权限管理" → "跨域设置"
3. 添加CORS规则：
   - **来源**：* 或指定域名
   - **允许Methods**：GET、POST、PUT、DELETE、HEAD
   - **允许Headers**：*
   - **暴露Headers**：ETag、x-oss-request-id

---

## 六、性能优化建议

### 1. CDN加速
为了提升文件访问速度，建议配置阿里云CDN：
1. 开通阿里云CDN服务
2. 添加加速域名，源站选择OSS域名
3. 配置HTTPS证书（可使用免费证书）
4. 在项目配置中设置 `custom-domain` 为CDN域名

### 2. 图片处理
阿里云OSS支持图片处理服务（收费），可以：
- 压缩图片
- 生成缩略图
- 添加水印
- 格式转换

示例URL：
```
https://cixiu.oss-cn-hangzhou.aliyuncs.com/image.jpg?x-oss-process=image/resize,w_800
```

### 3. 传输加速
如果有跨地域上传需求，可以开通传输加速：
1. 进入Bucket管理 → "传输管理" → "传输加速"
2. 开通传输加速
3. 使用加速域名上传文件

---

## 七、费用说明

### 阿里云OSS计费项
1. **存储费用**：按存储容量和时长计费
   - 标准存储：约0.12元/GB/月
2. **流量费用**：
   - 外网流出流量：约0.50元/GB
   - CDN回源流量：约0.15元/GB
3. **请求费用**：
   - PUT请求：0.01元/万次
   - GET请求：0.01元/万次

### 节省费用建议
1. 使用CDN加速，降低OSS外网流出费用
2. 定期清理不需要的文件
3. 对访问频率低的文件使用低频存储

---

## 八、对比MinIO的优势

| 特性 | 阿里云OSS | MinIO |
|-----|----------|-------|
| 部署维护 | 无需维护，开箱即用 | 需要自行部署和维护 |
| 可靠性 | 99.9999999999%（12个9） | 依赖底层存储 |
| 扩展性 | 无限扩展 | 受硬件限制 |
| 性能 | 全球CDN加速 | 依赖带宽 |
| 成本 | 按量付费，小规模低成本 | 需要服务器成本 |
| 安全性 | 企业级安全防护 | 需要自行配置 |

---

## 九、迁移注意事项

从MinIO迁移到阿里云OSS的注意事项：

1. **代码兼容**：已通过 `FileStorageService` 接口抽象，无需修改业务代码
2. **URL格式变化**：OSS的URL格式不同，需要更新数据库中的历史URL（如有）
3. **访问权限**：确认Bucket的读写权限配置正确
4. **批量迁移**：如果有历史文件，需要编写脚本批量迁移

---

## 十、测试验证

### 1. 启动应用
确保配置正确后启动应用：
```bash
mvn spring-boot:run
```

### 2. 测试文件上传
```bash
curl -X POST http://localhost:8080/api/file/upload/image \
  -H "Authorization: Bearer {token}" \
  -F "file=@test.jpg"
```

### 3. 验证文件访问
使用返回的URL在浏览器中访问，确认文件可以正常显示。

---

**配置完成！** 🎉

如有问题，请参考：
- [阿里云OSS官方文档](https://help.aliyun.com/product/31815.html)
- [OSS Java SDK文档](https://help.aliyun.com/document_detail/32008.html)
