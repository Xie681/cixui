# 刺绣定制系统 - 后端服务

## 项目介绍

刺绣定制系统是一个基于Spring Boot 3.5.6的现代化刺绣定制平台，支持图像定制、文字定制和模板定制三种模式，集成AI针法生成、实时生产追踪等功能。

**当前版本**：v1.3
**开发状态**：用户端核心功能已完成 ✅ | 文字定制功能已完成 ✅ | AI服务已集成 ✅ | 智能客服已集成 ✅
**最后更新**：2026-02-09

## 技术栈

### 后端框架
- **Spring Boot** 3.5.6 - 核心框架
- **LangChain4j** 0.36.2 - AI 集成框架
- **MyBatis-Plus** 3.5.5 - ORM框架
- **Spring Security** - 安全框架
- **JWT** - 用户认证

### 数据存储
- **MySQL** 8.0+ - 主数据库
- **Redis** 7.x - 缓存 + 会话管理
- **Redisson** 3.25.2 - 分布式锁
- **阿里云OSS** - 文件存储（已从MinIO迁移）

### 数据库迁移
- **Flyway** - 数据库版本管理和迁移工具

### 工具库
- **SpringDoc OpenAPI** 2.7.0 - API文档（Swagger UI）
- **Hutool** 5.8.24 - Java工具类
- **Lombok** - 简化代码
- **Fastjson2** 2.0.43 - JSON处理

## 项目结构

```
src/main/java/com/jinyue/cixiu/
├── common/                            # 公共模块
│   ├── config/                       # 配置类
│   │   ├── SecurityConfig.java            # Spring Security配置（含JWT过滤器）
│   │   ├── RedisConfig.java              # Redis配置
│   │   ├── OssConfig.java                # 阿里云OSS配置
│   │   ├── AsyncConfig.java              # 异步任务配置
│   │   ├── OpenApiConfig.java            # OpenAPI文档配置
│   │   ├── MyBatisPlusConfig.java        # MyBatis-Plus配置
│   │   ├── WebSocketConfig.java          # WebSocket配置
│   │   ├── CorsConfig.java               # 跨域配置
│   │   └── RestTemplateConfig.java       # RestTemplate配置
│   ├── constant/                     # 常量定义
│   ├── enums/                        # 枚举类
│   │   ├── UserRole.java                 # 用户角色
│   │   ├── OrderStatus.java              # 订单状态
│   │   ├── OrderType.java                # 订单类型
│   │   └── EmbroideryStyle.java          # 刺绣风格
│   ├── exception/                    # 异常处理
│   │   ├── BusinessException.java        # 业务异常
│   │   └── GlobalExceptionHandler.java   # 全局异常处理器
│   ├── result/                       # 统一响应
│   │   └── Result.java                   # 响应结构
│   ├── filter/                       # 过滤器
│   │   └── JwtAuthenticationFilter.java  # JWT认证过滤器
│   ├── utils/                        # 工具类
│   │   ├── JwtUtil.java                  # JWT工具
│   │   └── RedisUtil.java                # Redis工具
│   ├── service/                      # 公共服务
│   │   ├── FileStorageService.java       # 文件存储接口
│   │   └── impl/
│   │       └── AliyunOssFileStorageService.java  # 阿里云OSS实现
│   └── controller/                   # 公共控制器
│       ├── HealthController.java         # 健康检查
│       └── FileUploadController.java     # 文件上传
│
├── module/                           # 业务模块
│   ├── auth/                         # 认证模块 ✅
│   │   ├── controller/                   # 认证控制器
│   │   ├── service/                      # 认证服务
│   │   ├── dto/                          # 数据传输对象
│   │   └── vo/                           # 视图对象
│   ├── user/                         # 用户模块 ✅
│   │   ├── controller/                   # 用户控制器
│   │   ├── service/                      # 用户服务
│   │   ├── mapper/                       # 数据访问层
│   │   ├── entity/                       # 实体类
│   │   ├── dto/                          # 数据传输对象
│   │   └── vo/                           # 视图对象
│   ├── customize/                    # 定制模块 ✅
│   │   ├── controller/                   # 定制控制器
│   │   ├── service/                      # 定制服务（含报价引擎）
│   │   ├── dto/                          # 数据传输对象
│   │   └── vo/                           # 视图对象
│   ├── order/                        # 订单模块 ✅
│       ├── controller/                   # 订单控制器
│       ├── service/                      # 订单服务
│       ├── mapper/                       # 数据访问层
│       ├── entity/                       # 实体类
│       └── vo/                           # 视图对象
│   └── chatbot/                      # 智能客服模块 ✅
│       ├── controller/                   # 客服控制器
│       ├── service/                      # 客服服务
│       ├── mapper/                       # 数据访问层
│       ├── entity/                       # 实体类
│       ├── dto/                          # 数据传输对象
│       ├── vo/                           # 视图对象
│       └── tools/                        # AI工具函数
│
└── CixiuApplication.java             # 启动类
```

**标注说明**：✅ 表示该模块已完成开发

## 快速开始

### 1. 环境要求

- JDK 17+
- MySQL 8.0+
- Redis 7.x+
- Maven 3.6+

### 2. 配置数据库

修改 `src/main/resources/application.yml` 中的数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/cixiu?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: your_password
```

### 3. 配置Redis

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password  # 如果有密码
```

### 4. 创建数据库

```sql
CREATE DATABASE IF NOT EXISTS cixiu
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

### 5. 初始化数据库表

**方式一：使用 Flyway 自动迁移（推荐）**

项目已集成 Flyway，首次启动时会自动创建数据库表：
```bash
# 直接启动项目，Flyway会自动执行迁移脚本
mvn spring-boot:run
```

迁移脚本位置：`src/main/resources/db/migration/`

**方式二：手动执行SQL**

执行SQL脚本（如果需要手动创建）：
```bash
mysql -u root -p < src/main/resources/sql/schema.sql
```

或者参考 `docs/刺绣定制系统开发文档.md` 中的表结构手动创建。

### 6. 配置阿里云OSS

**重要**：项目使用阿里云OSS作为文件存储，需要先配置：

1. 注册阿里云账号并开通OSS服务
2. 创建Bucket（名称：cixiu 或自定义）
3. 获取AccessKey ID和Secret
4. 在 `application.yml` 中配置：

```yaml
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: 你的AccessKey-ID
    access-key-secret: 你的AccessKey-Secret
    bucket-name: cixiu
```

**详细配置指南**：`docs/阿里云OSS配置说明.md`

### 7. 配置阿里云百炼（智能客服）

**可选**：如需使用智能客服功能，需要配置阿里云百炼 API Key：

1. 访问 [阿里云百炼控制台](https://dashscope.console.aliyun.com/)
2. 注册/登录账号并创建应用
3. 获取 API Key
4. 在 `application-dev.yml` 中配置：

```yaml
langchain4j:
  dashscope:
    chat-model:
      api-key: your-dashscope-api-key-here
```

**详细配置指南**：`docs/LangChain4j 智能客服集成说明.md`

### 8. 启动项目

```bash
# 使用Maven启动
mvn spring-boot:run

# 或者使用IDE直接运行 CixiuApplication
```

### 8. 访问API文档

项目启动后，访问：
- **Swagger UI**：http://localhost:8080/swagger-ui.html（推荐）
- **OpenAPI JSON**：http://localhost:8080/v3/api-docs
- **健康检查**：http://localhost:8080/health

## 核心功能

### 用户端（已完成 ✅）

#### 1. 用户认证模块 ✅
- [x] 手机号/邮箱注册
- [x] 密码登录
- [x] 验证码登录
- [x] JWT Token认证
- [x] Token刷新机制
- [x] 登出功能
- [ ] 三方登录（预留，按需求跳过）

#### 2. 用户中心 ✅
- [x] 个人信息查看/修改
- [x] 收货地址管理（增删改查）
- [x] 设置默认地址
- [x] 历史订单查询

#### 3. 文件存储 ✅
- [x] 阿里云OSS集成
- [x] 图片上传（定制图片）
- [x] 头像上传
- [x] 设计文件上传
- [x] 自动分类存储
- [x] 支持CDN加速
- [x] 签名URL（私有文件访问）

#### 4. 定制下单 ✅
- [x] 绣制风格选择（苏绣/湘绣/蜀绣/粤绣）
- [x] 面料类型选择
- [x] 线材类型选择
- [x] 图像定制流程
- [x] 文字定制流程（支持文字转刺绣）
- [x] 实时报价计算
- [x] 提交订单
- [x] AI针法生成（已集成）
- [x] AI服务异步调用

#### 5. 报价引擎 ✅
- [x] 智能报价算法
  - 基础价 + 针数费 + 色数费 + 面料费 + 线材费
- [x] 加急订单加价（1.5倍）
- [x] 数量折扣计算
- [x] 报价明细展示

#### 6. 订单管理 ✅
- [x] 订单列表查询
- [x] 按状态筛选订单
- [x] 订单详情查看（包含文字定制详情）
- [x] 订单取消（待支付状态）
- [x] 11种订单状态流转
- [x] AI生成结果展示
- [ ] 支付功能（待对接）

#### 7. AI服务集成 ✅
- [x] AI服务接口对接
- [x] 图像转刺绣针法
- [x] 文字转刺绣针法
- [x] 异步任务处理
- [x] AI生成文件自动清理

#### 8. 智能客服 ✅（首页咨询助手）
- [x] 多轮对话（基于 LangChain4j + 阿里云百炼千问大模型）
- [x] 知识库问答
- [x] Tools 调用（订单查询、价格计算、风格推荐、图片分析）
- [x] 流式响应
- [x] 会话管理
- [x] 上下文记忆
- [x] 多模态支持（图片识别和分析）

#### 9. 设计助手 ✅（定制流程助手）
- [x] 需求理解和提取（从自然语言中提取结构化信息）
- [x] AI自动生成刺绣效果图（通义万相文生图）
- [x] 多轮对话优化设计
- [x] 价格和工期估算
- [x] 设计历史记录

### 设计师管理端（待开发）

- [ ] 设计师工作台
- [ ] AI可视化精修
- [ ] 三库管理（工艺/设备/材料）
- [ ] 产线联动
- [ ] 质检追溯

### 其他功能（待开发）

- [ ] 模板商城
- [ ] WebSocket实时推送
- [ ] 售后管理
- [ ] 生产追踪可视化

## 配置说明

### JWT配置

```yaml
jwt:
  secret: your-secret-key
  expiration: 86400000        # 24小时
  refresh-expiration: 604800000  # 7天
```

### 阿里云OSS配置（文件存储）

```yaml
aliyun:
  oss:
    endpoint: oss-cn-hangzhou.aliyuncs.com
    access-key-id: your-access-key-id
    access-key-secret: your-access-key-secret
    bucket-name: cixiu
    custom-domain:  # 可选，CDN域名
```

**配置说明**：详见 `docs/阿里云OSS配置说明.md`

### AI服务配置

```yaml
ai:
  service:
    timeout: 30000
    image-to-embroidery: /api/image-to-embroidery
    text-to-embroidery: /api/text-to-embroidery
    optimize: /api/optimize
```

### Spring AI 阿里云百炼配置（智能客服）

```yaml
langchain4j:
  dashscope:
    chat-model:
      api-key: your-dashscope-api-key-here  # 阿里云百炼 API Key
      model-name: qwen-plus  # 可选: qwen-turbo, qwen-plus, qwen-max
      temperature: 0.7
      max-tokens: 2000
      top-p: 0.8
```

**详细配置指南**：`docs/LangChain4j 智能客服集成说明.md`

### Flyway配置

```yaml
spring:
  flyway:
    enabled: true
    baseline-on-migrate: true
    baseline-version: 1
    locations: classpath:db/migration
    encoding: UTF-8
```

### Redisson配置（Redis分布式锁）

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
      database: 0
```

## API接口文档

已完成 **40+** 个API接口，涵盖以下模块：

### 认证相关（6个接口）
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 密码登录
- `POST /api/auth/code-login` - 验证码登录
- `POST /api/auth/send-code` - 发送验证码
- `POST /api/auth/refresh-token` - 刷新Token
- `POST /api/auth/logout` - 登出

### 用户中心（8个接口）
- `GET /api/user/profile` - 获取个人信息
- `PUT /api/user/profile` - 更新个人信息
- `GET /api/user/addresses` - 获取地址列表
- `POST /api/user/addresses` - 添加地址
- `PUT /api/user/addresses/{id}` - 更新地址
- `DELETE /api/user/addresses/{id}` - 删除地址
- `PUT /api/user/addresses/{id}/default` - 设置默认地址
- `GET /api/user/addresses/{id}` - 获取地址详情

### 文件上传（3个接口）
- `POST /api/file/upload/image` - 上传定制图片
- `POST /api/file/upload/avatar` - 上传头像
- `POST /api/file/upload/design` - 上传设计文件

### 定制下单（7个接口）
- `GET /api/customize/styles` - 获取绣制风格列表
- `GET /api/customize/fabrics` - 获取面料列表
- `GET /api/customize/threads` - 获取线材列表
- `POST /api/customize/calculate-price` - 计算报价（图像定制）
- `POST /api/customize/submit-order` - 提交定制订单（图像定制）
- `POST /api/customize/text/calculate-price` - 计算报价（文字定制）
- `POST /api/customize/text/submit-order` - 提交定制订单（文字定制）

### 订单管理（4个接口）
- `GET /api/orders` - 获取订单列表（支持状态筛选）
- `GET /api/orders/{id}` - 获取订单详情（按ID）
- `GET /api/orders/no/{orderNo}` - 获取订单详情（按订单号）
- `POST /api/orders/{id}/cancel` - 取消订单

### 智能客服（4个接口）
- `POST /api/chatbot/chat` - 多轮对话（支持多模态）
- `POST /api/chatbot/chat/stream` - 流式对话（支持多模态）
- `POST /api/chatbot/knowledge/query` - 知识库问答
- `DELETE /api/chatbot/session/{sessionId}` - 清除会话

### 设计助手（2个接口）
- `POST /api/customize/design/chat` - 设计对话（需求理解+效果图生成）
- `DELETE /api/customize/design/session/{sessionId}` - 清除设计会话

### 健康检查（1个接口）
- `GET /health` - 系统健康检查

**完整API文档**：http://localhost:8080/swagger-ui.html

### 接口认证

除了认证接口和健康检查接口，其他接口均需要在Header中携带JWT Token：

```http
Authorization: Bearer {accessToken}
```

## 开发规范

### 统一响应格式

所有接口统一使用 `Result<T>` 作为响应格式：

```java
// 成功响应
return Result.success(data);

// 失败响应
return Result.error(500, "错误信息");
```

### 异常处理

使用 `BusinessException` 抛出业务异常：

```java
throw new BusinessException("用户不存在");
throw new BusinessException(404, "资源不存在");
```

### 参数校验

使用 `@Valid` 和 `@Validated` 进行参数校验：

```java
@PostMapping
public Result<Void> create(@Valid @RequestBody UserDTO dto) {
    // 业务逻辑
}
```

## 项目文档

### 主要文档
- **智能助手架构说明**：`docs/智能助手架构说明.md` - 智能客服和设计助手的职责划分
- **AI刺绣效果图生成指南**：`docs/AI刺绣效果图生成指南.md` - 需求理解和自动生成效果图功能
- **多模态智能客服指南**：`docs/多模态智能客服使用指南.md` - 图片识别和分析功能说明
- **智能客服集成说明**：`docs/LangChain4j 智能客服集成说明.md` - LangChain4j 智能客服配置和使用
- **智能客服测试用例**：`docs/智能客服测试用例.md` - 智能客服功能测试指南
- **开发完成说明**：`docs/用户端模块开发完成说明.md` - 用户端功能详细说明
- **系统开发文档**：`docs/刺绣定制系统开发文档.md` - 完整系统设计文档
- **OSS配置指南**：`docs/阿里云OSS配置说明.md` - 阿里云OSS配置详解
- **迁移说明**：`docs/文件存储迁移说明-MinIO到阿里云OSS.md` - 存储方案迁移记录

### API测试流程

完整的测试流程请参考：`docs/用户端模块开发完成说明.md` 中的"API测试流程"章节。

快速测试示例：
```bash
# 1. 发送验证码
curl -X POST http://localhost:8080/api/auth/send-code \
  -H "Content-Type: application/json" \
  -d '{"receiver": "13800138000", "type": 1}'

# 2. 用户注册
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "phone": "13800138000",
    "password": "123456",
    "code": "验证码",
    "nickname": "测试用户"
  }'

# 3. 用户登录
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"account": "13800138000", "password": "123456"}'
```

## 后续开发计划

### 近期计划
1. ✅ ~~用户端核心功能~~ （已完成）
2. ✅ ~~文件存储（OSS）~~ （已完成）
3. ✅ ~~对接AI服务（图像转针法）~~ （已完成）
4. ✅ ~~文字定制功能~~ （已完成）
5. ✅ ~~智能客服（LangChain4j + 阿里云百炼）~~ （已完成）
6. 🔄 实现支付功能
7. 🔄 开发WebSocket推送
8. 🔄 模板定制功能

### 中期计划
1. 设计师管理端模块
2. 模板商城功能
3. 生产管理系统
4. 质检追溯系统

### 长期计划
1. 售后管理完善
2. 数据分析报表
3. 移动端适配
4. 性能优化

## 注意事项

### 开发环境
1. ⚠️ **验证码功能**：当前仅记录日志，需对接实际短信/邮件服务
2. ✅ **AI服务**：已对接实际AI服务，支持图像转刺绣和文字转刺绣
3. ✅ **智能客服**：已集成阿里云百炼千问大模型，需配置 API Key
4. ⚠️ **支付功能**：未实现，需对接支付宝/微信支付等
5. ✅ **三方登录**：已预留表结构，功能按需求跳过
6. ✅ **文件存储**：已配置阿里云OSS，需提供正确的AccessKey
7. ✅ **数据库迁移**：使用Flyway自动管理数据库版本

### 生产环境
1. 🔒 使用环境变量或配置中心存储敏感信息（AccessKey、数据库密码等）
2. 🔒 定期更换AccessKey和数据库密码
3. 📊 配置CDN加速降低OSS流量费用
4. 📊 监控系统运行状态和错误日志
5. 💾 定期备份数据库

## 开发规范

### 代码规范
- 使用Lombok简化代码
- 使用MyBatis-Plus简化数据库操作
- 统一使用`Result<T>`作为响应格式
- 使用`BusinessException`抛出业务异常
- 使用`@Valid`进行参数校验

### 提交规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建/工具链相关
```

## 性能指标

### 当前性能
- API响应时间：< 100ms（不含AI服务）
- 文件上传：支持最大10MB（图片）/ 50MB（设计文件）
- 并发支持：基于Redis + 数据库连接池

### 优化方向
- Redis缓存热点数据
- 数据库索引优化
- OSS配置CDN加速
- 异步处理耗时任务

## 技术支持

### 相关资源
- **Spring Boot官方文档**：https://spring.io/projects/spring-boot
- **MyBatis-Plus文档**：https://baomidou.com/
- **阿里云OSS文档**：https://help.aliyun.com/product/31815.html
- **SpringDoc OpenAPI文档**：https://springdoc.org/
- **Flyway文档**：https://flywaydb.org/documentation/
- **Redisson文档**：https://redisson.org/

### 问题反馈
- 项目Issues：提交到项目仓库
- 技术文档：查看 `docs/` 目录

## 版本历史

### v1.5 (2026-02-11)
- ✅ 集成阿里云通义万相文生图模型
- ✅ 实现AI自动生成刺绣效果图功能
- ✅ 新增需求提取工具（RequirementExtractionTool）
- ✅ 新增刺绣设计工具（EmbroideryDesignTool）
- ✅ 支持从自然语言描述自动生成效果图
- ✅ 自动估算价格和工期
- ✅ 支持多轮对话优化设计

### v1.4 (2026-02-11)
- ✅ 集成多模态功能（图片识别和分析）
- ✅ 支持阿里云百炼 qwen-vl-plus 视觉模型
- ✅ 新增图片分析工具（ImageAnalysisTool）
- ✅ 支持多图片对话和流式响应
- ✅ 自动切换文本/多模态模型

### v1.3 (2026-02-09)
- ✅ 集成 LangChain4j 框架（替代 Spring AI）
- ✅ 接入阿里云百炼千问大模型
- ✅ 实现智能客服多轮对话
- ✅ 实现知识库问答
- ✅ 实现 AI Tools 调用（订单查询、价格计算、风格推荐）
- ✅ 支持流式响应
- ✅ 实现会话管理和上下文记忆
- ✅ 新增 4 个智能客服 API 接口
- ✅ 新增聊天会话和消息表

### v1.2 (2025-10-24)
- ✅ 集成AI服务（图像转刺绣、文字转刺绣）
- ✅ 实现文字定制功能
- ✅ 新增异步任务处理
- ✅ 添加AI生成文件自动清理
- ✅ 集成Flyway数据库迁移工具
- ✅ 集成Redisson分布式锁
- ✅ 从Knife4j迁移到SpringDoc OpenAPI
- ✅ 优化前端下单用户体验
- ✅ 新增文字定制订单详情
- ✅ 新增32+个API接口

### v1.1 (2025-10-11)
- ✅ 文件存储从MinIO迁移到阿里云OSS
- ✅ 完善用户端核心功能
- ✅ 新增30+个API接口
- ✅ 完善项目文档

### v1.0 (2025-10-11)
- ✅ 项目初始化
- ✅ 用户认证模块
- ✅ 用户中心功能
- ✅ 定制下单功能
- ✅ 订单管理功能
- ✅ 报价引擎

## 许可证

Copyright © 2025 噤玥

## 联系方式

- **开发团队**：噤玥
- **项目版本**：v1.3
- **最后更新**：2026-02-09
