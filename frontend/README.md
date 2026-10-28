# 数字绣娘——AI刺绣个性化定制平台

> 让普通用户也能"绣出自己故事"的 AI 刺绣创作平台

基于 Vue 3 + Vite + TypeScript 的现代化前端工程化项目，融合传统刺绣工艺与 AI 技术，提供从设计到生产的全链路数字化解决方案。

## ✨ 项目特色

- 🎨 **AI 智能打版** - AI 自动生成刺绣图案，支持智能提示词生成、四大名绣风格选择、智能配色
- 🏛️ **高端定制** - 一比一复刻奢侈品、文物、龙袍、传统服装等珍贵艺术品
- 🤝 **三方协作** - 设计师-版师-工艺师三方数字协作平台，AI初版+人工精修
- 📚 **知识图谱** - 传承中华传统优秀文化，普及AI技术知识
- 🎯 **生产协同** - 连接实体刺绣机，实现从设计到生产的全链路数字化联动
- 💎 **精美UI** - 融合传统文化元素与AI科技风，玻璃态、霓虹光效、粒子动画

## 🚀 快速开始

### 环境要求

- Node.js >= 18.0.0
- npm >= 9.0.0 或 yarn >= 1.22.0

### 安装依赖

```bash
npm install
```

### 开发环境运行

```bash
npm run dev
```

访问 http://localhost:3000

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

### 一键启动脚本

**Windows:**
```bash
start.bat
```

**Linux/Mac:**
```bash
chmod +x start.sh
./start.sh
```

## 📁 项目结构

```
frontend/
├── src/
│   ├── assets/              # 静态资源
│   │   └── styles/          # 全局样式
│   │       └── tailwind.css # Tailwind CSS 配置
│   │
│   ├── components/          # 公共组件
│   │   ├── ai/              # AI相关组件
│   │   │   ├── AIPromptGenerator.vue    # AI提示词生成器
│   │   │   ├── ChatInterface.vue        # AI聊天界面
│   │   │   └── StyleSelector.vue       # 风格选择器
│   │   ├── collaboration/   # 协作相关组件
│   │   │   └── CanvasEditor.vue         # 画布编辑器
│   │   ├── common/          # 通用组件
│   │   │   ├── ColorPicker.vue          # 颜色选择器
│   │   │   ├── GlowCard.vue             # 发光卡片
│   │   │   ├── NeonButton.vue          # 霓虹按钮
│   │   │   └── ProgressBar.vue         # 进度条
│   │   ├── effects/         # 特效组件
│   │   │   ├── DataStream.vue           # 数据流效果
│   │   │   ├── NeedleThread.vue         # 针线装饰
│   │   │   ├── Particles.vue             # 粒子效果
│   │   │   └── TraditionalPattern.vue   # 传统花纹
│   │   ├── knowledge/       # 知识图谱组件
│   │   │   └── KnowledgeGraph.vue       # 知识图谱可视化
│   │   ├── layout/          # 布局组件
│   │   │   └── Navbar.vue               # 导航栏
│   │   └── upload/          # 上传组件
│   │       └── ImageUpload.vue          # 图片上传
│   │
│   ├── composables/         # 组合式函数
│   │   ├── useAIGeneration.ts          # AI生成逻辑
│   │   ├── useCanvas.ts                # 画布操作
│   │   ├── useChat.ts                  # 聊天功能
│   │   ├── usePageManager.ts           # 页面管理
│   │   ├── useParticles.ts             # 粒子效果
│   │   └── useWilcom.ts                # Wilcom集成
│   │
│   ├── router/              # 路由配置
│   │   ├── index.ts                    # 路由实例
│   │   └── routes.ts                   # 路由定义
│   │
│   ├── services/            # API服务层
│   │   └── api/             # API接口
│   │       ├── index.ts                # Axios实例和拦截器
│   │       ├── auth.ts                 # 认证接口
│   │       ├── user.ts                 # 用户接口
│   │       ├── file.ts                 # 文件上传接口
│   │       ├── customize.ts            # 定制服务接口
│   │       └── order.ts                # 订单接口
│   │
│   ├── store/               # 状态管理 (Pinia)
│   │   ├── user.ts                     # 用户状态
│   │   ├── cart.ts                     # 购物车状态
│   │   └── ai.ts                       # AI状态
│   │
│   ├── types/               # TypeScript类型定义
│   │   └── user.ts                     # 用户类型
│   │
│   ├── views/               # 页面组件
│   │   ├── Home.vue                    # 首页
│   │   ├── Gallery.vue                 # 风格画廊
│   │   ├── GalleryMore.vue              # 更多风格
│   │   ├── CreatorTools.vue             # 创作工具
│   │   ├── Cart.vue                    # 购物车
│   │   ├── Checkout.vue                 # 结算页
│   │   ├── PatternEditor.vue           # 图案编辑器
│   │   ├── ProductionCollaboration.vue  # 生产协同
│   │   ├── KnowledgeGraph.vue          # 知识图谱
│   │   ├── NotFound.vue                # 404页面
│   │   │
│   │   ├── ai/                         # AI相关页面
│   │   │   ├── AIGeneration.vue        # AI生成
│   │   │   └── Collaboration.vue       # 三方协作
│   │   │
│   │   ├── luxury/                     # 高端定制页面
│   │   │   ├── LuxuryIndex.vue         # 高端定制首页
│   │   │   ├── LuxuryReplica.vue       # 奢侈品复刻
│   │   │   ├── ArtifactReplica.vue     # 文物复刻
│   │   │   ├── DragonRobe.vue          # 龙袍定制
│   │   │   └── TraditionalClothing.vue # 传统服装定制
│   │   │
│   │   ├── order/                      # 订单相关页面
│   │   │   ├── OrderList.vue           # 订单列表
│   │   │   └── OrderDetail.vue         # 订单详情
│   │   │
│   │   └── admin/                      # 管理后台页面
│   │       ├── Dashboard.vue           # 仪表盘
│   │       ├── OrderManagement.vue     # 订单管理
│   │       ├── PatternLibrary.vue      # 花样库
│   │       └── DeviceManagement.vue    # 设备管理
│   │
│   ├── App.vue              # 根组件
│   └── main.ts              # 入口文件
│
├── public/                  # 公共静态资源
├── index.html              # HTML模板
├── vite.config.ts          # Vite配置
├── tailwind.config.js      # Tailwind配置
├── tsconfig.json           # TypeScript配置
├── package.json            # 项目依赖
│
├── README.md               # 项目说明（本文件）
├── API_DOCUMENTATION.md    # 前端API文档
├── API_QUICK_REFERENCE.md  # API快速参考
└── COMPLETE_API_DOCUMENTATION.md  # 完整前后端API文档
```

## 🔧 技术栈

### 核心框架
- **Vue 3.4+** - 渐进式 JavaScript 框架，使用 Composition API
- **Vite 5.1+** - 下一代前端构建工具，极速热更新
- **TypeScript 5.3+** - 类型安全的 JavaScript 超集

### 路由与状态
- **Vue Router 4.3+** - 官方路由管理器
- **Pinia 2.1+** - 新一代状态管理库

### UI 框架
- **Tailwind CSS 3.4+** - 实用优先的 CSS 框架
- **自定义主题** - 融合传统文化与AI科技的设计系统

### HTTP 客户端
- **Axios 1.6+** - 基于 Promise 的 HTTP 客户端

### 工具库
- **@vueuse/core 10.9+** - Vue Composition API 工具集合

### 开发工具
- **ESLint** - 代码质量检查
- **Prettier** - 代码格式化
- **vue-tsc** - Vue TypeScript 类型检查

## 📋 功能模块

### 1. 首页 (Home)
- Hero 区域展示
- AI 智能打版预览
- 风格画廊（支持四大名绣筛选）
- 创作工具入口
- 高端定制展示
- 知识图谱可视化
- 定制下单流程

### 2. AI 智能生成 (AIGeneration)
- 图片上传与预览
- AI 智能提示词生成
- 四大名绣风格选择（苏绣、湘绣、粤绣、蜀绣）
- 智能配色方案
- 实时预览效果
- DST 文件生成
- Wilcom 软件集成

### 3. 图案编辑器 (PatternEditor)
- 图层管理
- 画笔工具
- 路径编辑
- 颜色填充
- 实时预览

### 4. 三方协作 (Collaboration)
- 设计师面板（艺术美感调整）
- 版师面板（Wilcom 精修）
- 工艺师面板（生产可行性）
- 实时同步状态
- 工作流程可视化

### 5. 生产协同 (ProductionCollaboration)
- 设备远程控制
- 实时进度监控
- B 端生产管理
- 设备参数调整
- 生产任务管理

### 6. 高端定制
- **奢侈品复刻** - 一比一复刻国际奢侈品牌
- **文物复刻** - 文物数字化与复刻
- **龙袍定制** - 皇家工艺重现
- **传统服装定制** - 汉服、旗袍、少数民族服饰

### 7. 知识图谱 (KnowledgeGraph)
- 传统工艺知识
- AI 技术科普
- 文化传承
- 针法图谱
- 材料科学
- 历史脉络

### 8. 订单管理
- 订单列表（支持状态筛选）
- 订单详情
- 订单取消

### 9. 用户中心
- 个人信息管理
- 收货地址管理
- 订单查询

## 🔌 API 接口

### 环境配置

创建 `.env` 文件：

```env
# API 基础地址
VITE_API_BASE_URL=http://localhost:8080
```

### API 服务

所有 API 服务位于 `src/services/api/` 目录：

```typescript
import { authApi, userApi, fileApi, customizeApi, orderApi } from '@/services/api'

// 认证
await authApi.login({ account, password })
await authApi.register({ phone, password, code })

// 用户
await userApi.getProfile()
await userApi.updateProfile({ nickname, email })

// 文件上传
await fileApi.uploadImage(file)
await fileApi.uploadAvatar(file)

// 定制服务
await customizeApi.getStyles()
await customizeApi.previewAiResult({ imageUrl, embroideryStyle })
await customizeApi.submitOrder({ ... })

// 订单
await orderApi.listOrders(status)
await orderApi.getOrderDetail(id)
```

### API 文档

- 📖 [完整API文档](./COMPLETE_API_DOCUMENTATION.md) - 前后端完整接口文档
- 📝 [前端API文档](./API_DOCUMENTATION.md) - 前端API使用指南
- ⚡ [快速参考](./API_QUICK_REFERENCE.md) - API快速查询

## 🎨 样式系统

### 主题色彩

```typescript
// tailwind.config.js
colors: {
  accent: {
    primary: '#8B5CF6',    // 紫色（主色）
    cta: '#06B6D4',       // 青色（行动色）
    vibrant: '#10B981',   // 绿色（强调色）
    secondary: '#A78BFA', // 浅紫色
  }
}
```

### 自定义样式类

- `glass-dark` - 玻璃态深色背景
- `glow-card` - 发光卡片效果
- `neon-button` - 霓虹按钮
- `gradient-text` - 渐变文字
- `neon-border` - 霓虹边框
- `embroidery-pattern` - 刺绣纹理背景

### 动画效果

- `float` - 浮动动画
- `pulse-glow` - 脉冲光晕
- `data-stream` - 数据流效果
- `particles` - 粒子动画

## 🛠️ 开发指南

### 代码规范

#### 组件命名
- 使用 PascalCase：`UserProfile.vue`
- 页面组件放在 `views/` 目录
- 公共组件放在 `components/` 目录

#### 文件组织
```typescript
// 组件结构
<script setup lang="ts">
// 1. 导入
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

// 2. 类型定义
interface Props {
  // ...
}

// 3. Props & Emits
const props = defineProps<Props>()
const emit = defineEmits<{}>()

// 4. 组合式函数
const router = useRouter()

// 5. 响应式数据
const count = ref(0)

// 6. 计算属性
const doubleCount = computed(() => count.value * 2)

// 7. 方法
function handleClick() {
  // ...
}

// 8. 生命周期
onMounted(() => {
  // ...
})
</script>
```

#### 提交规范

使用 [Conventional Commits](https://www.conventionalcommits.org/) 规范：

```
feat: 添加新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整（不影响功能）
refactor: 代码重构
perf: 性能优化
test: 测试相关
chore: 构建/工具相关
```

### 开发命令

```bash
# 开发
npm run dev

# 构建
npm run build

# 预览
npm run preview

# 代码检查
npm run lint

# 代码格式化
npm run format
```

### 调试技巧

1. **Vue DevTools** - 安装浏览器扩展
2. **TypeScript 类型检查** - `npm run build` 会进行类型检查
3. **网络请求** - 使用浏览器开发者工具的 Network 面板
4. **状态管理** - 使用 Pinia DevTools

## 🚢 部署

### 构建生产版本

```bash
npm run build
```

构建产物位于 `dist/` 目录。

### 环境变量

生产环境需要配置：

```env
VITE_API_BASE_URL=https://api.yourdomain.com
```

### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name yourdomain.com;
    root /path/to/dist;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 📚 相关文档

- [完整API文档](./COMPLETE_API_DOCUMENTATION.md) - 前后端完整接口文档
- [前端API文档](./API_DOCUMENTATION.md) - 前端API使用指南
- [API快速参考](./API_QUICK_REFERENCE.md) - API快速查询

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

项目源代码及相关资源的授权范围正在核实，请勿将仓库公开访问视为已经获得全部内容的开源使用授权。

## 👥 团队

- **项目名称**: 绣生 (Xiù·Shēng)
- **项目类型**: AI 刺绣定制平台
- **技术栈**: Vue 3 + Vite + TypeScript

## 🔗 相关链接

- [后端API文档](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)
- [Vue 3 文档](https://vuejs.org/)
- [Vite 文档](https://vitejs.dev/)
- [Tailwind CSS 文档](https://tailwindcss.com/)

---

**绣生** - 让传统工艺在数字时代焕发新活力 ✨
