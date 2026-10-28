# 快速开始指南

这是一个5分钟快速启动刺绣定制系统的指南。

## 前置要求

确保你的开发环境已安装：
- ✅ JDK 17+
- ✅ MySQL 8.0+
- ✅ Redis 7.x+
- ✅ Maven 3.6+

## 步骤1：克隆项目

```bash
git clone <repository-url>
cd cixiu/backend
```

## 步骤2：创建数据库

```sql
-- 连接MySQL
mysql -u root -p

-- 创建数据库
CREATE DATABASE IF NOT EXISTS cixiu
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

-- 退出MySQL
exit
```

## 步骤3：数据库迁移（Flyway）

数据库表结构由 Flyway 迁移文件维护，所在目录为 `src/main/resources/db/migration/`。
项目在 `application.yml` 中启用了 Flyway。启动前请先检查数据库现状与迁移版本，避免重复手动导入。

**安全提示：** `V7__insert_test_users.sql` 包含公开的测试账户。生产环境部署前必须隔离该测试数据，并核查真实数据库中的测试用户是否已停用。

## 步骤4：配置文件

编辑 `src/main/resources/application.yml`：

### 必须配置（核心功能）

```yaml
# 数据库配置
spring:
  datasource:
    username: root
    password: 你的MySQL密码

# Redis配置
spring:
  data:
    redis:
      password: 你的Redis密码（如果有）

# JWT密钥（生产环境请更换）
jwt:
  secret: 建议生成一个复杂的随机字符串
```

### 可选配置（文件上传功能需要）

```yaml
# 阿里云OSS配置
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: 你的AccessKey-ID
    access-key-secret: 你的AccessKey-Secret
    bucket-name: cixiu
```

> 💡 **提示**：如果暂时不配置OSS，系统可以正常运行，但文件上传功能会报错。配置方法详见：`docs/阿里云OSS配置说明.md`

## 步骤5：启动服务

### 启动Redis
```bash
redis-server
```

### 启动应用

**方式1：Maven命令**
```bash
mvn clean spring-boot:run
```

**方式2：IDE运行**
- 使用IDEA或Eclipse打开项目
- 运行 `CixiuApplication.java`

## 步骤6：验证启动

### 访问API文档
浏览器打开：http://localhost:8080/doc.html

### 测试健康检查
```bash
curl http://localhost:8080/api/health
```

如果返回类似下面的JSON，说明启动成功：
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "status": "UP",
    "message": "刺绣定制系统运行正常"
  }
}
```

## 快速测试API

### 1. 发送验证码
```bash
curl -X POST http://localhost:8080/api/auth/send-code \
  -H "Content-Type: application/json" \
  -d '{
    "receiver": "13800138000",
    "type": 1
  }'
```

### 2. 用户注册
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "123456",
    "code": "查看控制台输出的验证码",
    "nickname": "测试用户"
  }'
```

### 3. 用户登录
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "account": "13800138000",
    "password": "123456"
  }'
```

会返回包含 `accessToken` 的响应，复制这个token。

### 4. 查看个人信息（需要token）
```bash
curl http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer 你的accessToken"
```

## 常见问题

### Q1: 启动失败，提示"Access denied for user"
**A**: MySQL用户名或密码配置错误，检查 `application.yml` 中的数据库配置。

### Q2: 启动失败，提示"Connection refused"
**A**: Redis未启动，运行 `redis-server` 启动Redis。

### Q3: 验证码在哪里查看？
**A**: 当前验证码只记录在控制台日志中，查看启动的终端输出。

### Q4: 文件上传报错
**A**: 需要配置阿里云OSS，详见 `docs/阿里云OSS配置说明.md`。如果不需要文件上传功能，可以暂时忽略。

### Q5: API文档打不开
**A**: 确认项目已成功启动，访问 http://localhost:8080/doc.html

## 下一步

- 📖 阅读完整文档：`README.md`
- 🎯 查看功能说明：`docs/用户端模块开发完成说明.md`
- 🔧 配置阿里云OSS：`docs/阿里云OSS配置说明.md`
- 🚀 开始开发：参考 `docs/刺绣定制系统开发文档.md`

## 开发工具推荐

- **IDE**: IntelliJ IDEA / Eclipse
- **API测试**: Postman / Knife4j（内置）
- **数据库工具**: Navicat / DataGrip
- **Redis工具**: RedisInsight / Another Redis Desktop Manager

---

🎉 **恭喜！你已成功启动刺绣定制系统！**

如有问题，请查看项目文档或提交Issue。
