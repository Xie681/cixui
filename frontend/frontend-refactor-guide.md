# 云绣定制平台 - 前端工程化重构指南

> 从静态HTML到Vue 3工程化项目的完整迁移方案

---

## 📋 目录

1. [技术栈选择](#1-技术栈选择)
2. [项目结构设计](#2-项目结构设计)
3. [页面拆分方案](#3-页面拆分方案)
4. [前后端对接设计](#4-前后端对接设计)
5. [状态管理设计](#5-状态管理设计)
6. [迁移步骤](#6-迁移步骤)

---

## 1. 技术栈选择

### 1.1 推荐方案：Vue 3 + Vite

**选择理由：**
- ✅ **学习曲线平缓**：Vue 3的模板语法接近HTML，迁移成本低
- ✅ **组合式API灵活**：适合复杂业务逻辑（AI分析、协同编辑）
- ✅ **生态成熟**：Pinia状态管理、Vue Router路由、Element Plus组件库
- ✅ **性能优秀**：Vue 3的响应式系统性能优于Vue 2
- ✅ **TypeScript友好**：渐进式支持，可以逐步迁移

**技术栈清单：**
```json
{
  "框架": "Vue 3.4+ (Composition API)",
  "构建工具": "Vite 5+",
  "路由": "Vue Router 4+",
  "状态管理": "Pinia 2+",
  "UI框架": "Element Plus (可选，主要用于后台)",
  "样式方案": "Tailwind CSS 3+",
  "HTTP客户端": "Axios",
  "类型检查": "TypeScript (可选，建议)",
  "代码规范": "ESLint + Prettier"
}
```

### 1.2 为什么不选React？

虽然React也很优秀，但考虑到：
- 项目已有大量HTML模板，Vue的模板语法迁移成本更低
- Vue的组合式API在处理复杂业务时更直观
- 团队可能对Vue更熟悉

---

## 2. 项目结构设计

### 2.1 完整目录结构

```
cixiu-frontend/
├── public/                          # 静态资源
│   ├── favicon.ico
│   └── logo.png
├── src/
│   ├── assets/                     # 资源文件
│   │   ├── images/                 # 图片
│   │   ├── styles/                 # 全局样式
│   │   │   ├── tailwind.css       # Tailwind入口
│   │   │   ├── variables.css      # CSS变量
│   │   │   └── animations.css     # 动画
│   │   └── fonts/                  # 字体
│   │
│   ├── components/                  # 公共组件
│   │   ├── common/                 # 通用组件
│   │   │   ├── AppNavbar.vue      # 导航栏
│   │   │   ├── AppFooter.vue      # 页脚
│   │   │   ├── GlowCard.vue       # 发光卡片
│   │   │   ├── NeonButton.vue     # 霓虹按钮
│   │   │   ├── LoadingSpinner.vue # 加载动画
│   │   │   └── EmptyState.vue     # 空状态
│   │   │
│   │   ├── upload/                 # 上传相关
│   │   │   ├── ImageUpload.vue    # 图片上传
│   │   │   ├── FileUpload.vue     # 文件上传
│   │   │   └── DragDropZone.vue   # 拖拽上传
│   │   │
│   │   ├── gallery/                # 画廊相关
│   │   │   ├── StyleCard.vue      # 风格卡片
│   │   │   ├── StyleGrid.vue      # 风格网格
│   │   │   └── FilterTabs.vue     # 筛选标签
│   │   │
│   │   ├── ai/                     # AI相关
│   │   │   ├── AIPreview.vue      # AI预览
│   │   │   ├── ProcessProgress.vue # 处理进度
│   │   │   └── PromptGenerator.vue # 提示词生成器
│   │   │
│   │   ├── collaboration/          # 协同相关
│   │   │   ├── RoleSelector.vue   # 角色选择器
│   │   │   ├── ParameterPanel.vue # 参数面板
│   │   │   └── CanvasEditor.vue   # 画布编辑器
│   │   │
│   │   └── preview/                # 预览相关
│   │       ├── StitchPreview.vue  # 针法预览
│   │       ├── ColorPreview.vue   # 颜色预览
│   │       └── FinalPreview.vue  # 最终预览
│   │
│   ├── views/                      # 页面级组件
│   │   ├── Home.vue                # 首页
│   │   ├── Gallery.vue             # 风格画廊
│   │   ├── CreatorTools.vue        # 创作工具
│   │   │
│   │   ├── ai/                     # AI相关页面
│   │   │   ├── AIGeneration.vue   # AI智能打版
│   │   │   └── Collaboration.vue   # 三方协同
│   │   │
│   │   ├── luxury/                 # 高端定制页面
│   │   │   ├── LuxuryIndex.vue    # 高端定制首页
│   │   │   ├── LuxuryReplica.vue  # 奢侈品复刻
│   │   │   ├── ArtifactReplica.vue # 文物复刻
│   │   │   ├── DragonRobe.vue     # 龙袍定制
│   │   │   └── TraditionalClothing.vue # 传统服装
│   │   │
│   │   ├── knowledge/              # 知识图谱
│   │   │   └── KnowledgeGraph.vue
│   │   │
│   │   ├── order/                  # 订单相关
│   │   │   ├── OrderList.vue      # 订单列表
│   │   │   └── OrderDetail.vue    # 订单详情
│   │   │
│   │   └── admin/                  # 管理后台
│   │       ├── Dashboard.vue       # 仪表盘
│   │       ├── PatternLibrary.vue # 花样库
│   │       ├── PatternList.vue    # 花样列表
│   │       ├── OrderManagement.vue # 订单管理
│   │       └── DeviceManagement.vue # 设备管理
│   │
│   ├── router/                     # 路由配置
│   │   ├── index.ts               # 路由入口
│   │   ├── routes.ts              # 路由定义
│   │   └── guards.ts              # 路由守卫
│   │
│   ├── store/                      # 状态管理 (Pinia)
│   │   ├── index.ts               # Store入口
│   │   ├── user.ts                # 用户状态
│   │   ├── order.ts               # 订单状态
│   │   ├── gallery.ts             # 画廊状态
│   │   ├── ai.ts                  # AI分析状态
│   │   └── collaboration.ts       # 协同状态
│   │
│   ├── services/                   # API服务层
│   │   ├── api/                   # API接口
│   │   │   ├── index.ts          # Axios实例
│   │   │   ├── auth.ts           # 认证接口
│   │   │   ├── user.ts           # 用户接口
│   │   │   ├── customize.ts      # 定制接口
│   │   │   ├── order.ts           # 订单接口
│   │   │   ├── file.ts            # 文件接口
│   │   │   ├── wilcom.ts          # Wilcom接口
│   │   │   └── gallery.ts         # 画廊接口
│   │   │
│   │   └── websocket/             # WebSocket服务
│   │       ├── index.ts
│   │       └── ai-analysis.ts    # AI分析WebSocket
│   │
│   ├── utils/                      # 工具函数
│   │   ├── request.ts             # 请求封装
│   │   ├── storage.ts             # 本地存储
│   │   ├── format.ts              # 格式化工具
│   │   ├── canvas.ts              # Canvas工具
│   │   └── constants.ts          # 常量定义
│   │
│   ├── composables/                # 组合式函数
│   │   ├── useAuth.ts             # 认证逻辑
│   │   ├── useUpload.ts           # 上传逻辑
│   │   ├── usePreview.ts          # 预览逻辑
│   │   ├── useWilcom.ts           # Wilcom集成
│   │   └── useWebSocket.ts        # WebSocket逻辑
│   │
│   ├── types/                      # TypeScript类型
│   │   ├── api.ts                 # API类型
│   │   ├── user.ts                # 用户类型
│   │   ├── order.ts               # 订单类型
│   │   └── gallery.ts             # 画廊类型
│   │
│   ├── App.vue                     # 根组件
│   └── main.ts                     # 入口文件
│
├── .env                            # 环境变量
├── .env.development                # 开发环境
├── .env.production                 # 生产环境
├── .gitignore
├── index.html                      # HTML模板
├── package.json                    # 依赖配置
├── tsconfig.json                   # TypeScript配置
├── vite.config.ts                  # Vite配置
├── tailwind.config.js              # Tailwind配置
└── README.md                       # 项目说明
```

---

## 3. 页面拆分方案

### 3.1 原有页面分析

#### 📄 ai-embroidery.html → 拆分为：

1. **首页 (Home.vue)**
   - Hero区域
   - 功能特色展示
   - 风格画廊预览

2. **风格画廊 (Gallery.vue)**
   - 风格筛选
   - 风格网格展示
   - 四大名绣详情

3. **创作工具 (CreatorTools.vue)**
   - 三个工具卡片入口

4. **AI智能打版 (AIGeneration.vue)**
   - 图片/文字上传
   - AI处理流程
   - DST文件生成
   - Wilcom集成

5. **三方协同 (Collaboration.vue)**
   - 角色切换
   - 画布编辑
   - 参数调整

6. **知识图谱 (KnowledgeGraph.vue)**
   - 知识节点展示
   - 交互式图谱

7. **订单页面 (OrderList.vue / OrderDetail.vue)**
   - 订单列表
   - 订单详情

#### 📄 luxury-custom-pages.html → 拆分为：

1. **高端定制首页 (LuxuryIndex.vue)**
   - 四个定制卡片入口

2. **奢侈品复刻 (LuxuryReplica.vue)**
   - 图片上传
   - AI分析
   - 实时预览
   - 参数设置

3. **文物复刻 (ArtifactReplica.vue)**
   - 文物信息表单
   - 数字化处理
   - 实时预览

4. **龙袍定制 (DragonRobe.vue)**
   - 样式选择
   - 工艺参数
   - 实时预览

5. **传统服装定制 (TraditionalClothing.vue)**
   - 服装类型选择
   - 定制表单
   - 实时预览

#### 📄 admin-dashboard.html → 拆分为：

1. **管理后台首页 (Dashboard.vue)**
   - 数据统计
   - 快捷操作

2. **花样库 (PatternLibrary.vue)**
   - 花样网格展示

3. **花样列表 (PatternList.vue)**
   - 表格展示
   - 搜索筛选

4. **订单管理 (OrderManagement.vue)**
   - 订单列表
   - 状态筛选

5. **设备管理 (DeviceManagement.vue)**
   - 设备列表
   - 实时监控

---

## 4. 前后端对接设计

### 4.1 API服务层组织

**services/api/index.ts** - Axios实例配置

```typescript
import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig } from 'axios'
import { useUserStore } from '@/store/user'
import router from '@/router'

// 创建axios实例
const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    const { code, data, message } = response.data
    if (code === 200) {
      return data
    } else if (code === 401) {
      // Token过期，跳转登录
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
      return Promise.reject(new Error('登录已过期'))
    } else {
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default api
```

**services/api/auth.ts** - 认证接口

```typescript
import api from './index'
import type { LoginDTO, RegisterDTO, CodeLoginDTO, SendCodeDTO } from '@/types/api'

export const authApi = {
  // 用户注册
  register(data: RegisterDTO) {
    return api.post('/api/auth/register', data)
  },
  
  // 密码登录
  login(data: LoginDTO) {
    return api.post('/api/auth/login', data)
  },
  
  // 验证码登录
  codeLogin(data: CodeLoginDTO) {
    return api.post('/api/auth/code-login', data)
  },
  
  // 发送验证码
  sendCode(data: SendCodeDTO) {
    return api.post('/api/auth/send-code', data)
  },
  
  // 刷新Token
  refreshToken(refreshToken: string) {
    return api.post(`/api/auth/refresh-token?refreshToken=${refreshToken}`)
  },
  
  // 登出
  logout() {
    return api.post('/api/auth/logout')
  }
}
```

**services/api/customize.ts** - 定制接口

```typescript
import api from './index'
import type { CalculatePriceDTO, AiPreviewDTO, SubmitOrderDTO } from '@/types/api'

export const customizeApi = {
  // 获取绣制风格列表
  getStyles() {
    return api.get('/api/customize/styles')
  },
  
  // 获取面料列表
  getFabrics() {
    return api.get('/api/customize/fabrics')
  },
  
  // 获取线材列表
  getThreads() {
    return api.get('/api/customize/threads')
  },
  
  // 计算报价
  calculatePrice(data: CalculatePriceDTO) {
    return api.post('/api/customize/calculate-price', data)
  },
  
  // AI预览刺绣效果
  previewAiResult(data: AiPreviewDTO) {
    return api.post('/api/customize/preview-ai-result', data)
  },
  
  // 提交图片定制订单
  submitOrder(data: SubmitOrderDTO) {
    return api.post('/api/customize/submit-order', data)
  },
  
  // 提交文字定制订单
  submitTextOrder(data: any) {
    return api.post('/api/customize/submit-text-order', data)
  }
}
```

**services/api/file.ts** - 文件接口

```typescript
import api from './index'

export const fileApi = {
  // 上传图片
  uploadImage(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  // 上传头像
  uploadAvatar(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  // 上传设计文件
  uploadDesign(file: File) {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/design', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}
```

**services/api/wilcom.ts** - Wilcom接口

```typescript
import api from './index'

export const wilcomApi = {
  // 检查Wilcom是否已安装
  checkInstallation() {
    return api.get('/api/wilcom/check')
  },
  
  // 获取当前DST文件路径
  getCurrentDST(params?: { orderId?: number; designId?: number }) {
    return api.get('/api/wilcom/current-dst', params)
  },
  
  // 在Wilcom中打开文件
  openInWilcom(data: { filePath: string; orderId?: number }) {
    return api.post('/api/wilcom/open', data)
  },
  
  // 检查Wilcom文件更新
  checkUpdates(params: { orderId: number; lastModified?: number }) {
    return api.get('/api/wilcom/check-updates', params)
  },
  
  // 同步Wilcom修改到平台
  sync(data: { orderId: number; filePath: string }) {
    return api.post('/api/wilcom/sync', data)
  }
}
```

---

## 5. 状态管理设计

### 5.1 Pinia Store设计

**store/user.ts** - 用户状态

```typescript
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, userApi } from '@/services/api'
import type { UserProfileVO } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const userInfo = ref<UserProfileVO | null>(null)
  const isLoggedIn = computed(() => !!token.value)
  
  // 登录
  async function login(phone: string, password: string) {
    const result = await authApi.login({ phone, password })
    token.value = result.token
    refreshToken.value = result.refreshToken
    userInfo.value = result.user
    localStorage.setItem('token', result.token)
    localStorage.setItem('refreshToken', result.refreshToken)
  }
  
  // 登出
  function logout() {
    token.value = null
    refreshToken.value = null
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }
  
  // 获取用户信息
  async function fetchProfile() {
    const profile = await userApi.getProfile()
    userInfo.value = profile
  }
  
  return {
    token,
    refreshToken,
    userInfo,
    isLoggedIn,
    login,
    logout,
    fetchProfile
  }
})
```

**store/ai.ts** - AI分析状态

```typescript
import { defineStore } from 'defineStore'
import { ref } from 'vue'
import { customizeApi } from '@/services/api'

export const useAIStore = defineStore('ai', () => {
  // AI分析状态
  const isAnalyzing = ref(false)
  const analysisProgress = ref(0)
  const currentStep = ref(1)
  const analysisResult = ref<any>(null)
  const dstFileUrl = ref<string | null>(null)
  
  // 分析步骤状态
  const semanticStatus = ref('等待中...')
  const stitchStatus = ref('等待中...')
  const pathStatus = ref('等待中...')
  const dstStatus = ref('等待中...')
  
  // 开始AI分析
  async function startAnalysis(imageUrl: string, style: string) {
    isAnalyzing.value = true
    currentStep.value = 1
    
    try {
      // 调用AI预览接口
      const result = await customizeApi.previewAiResult({
        imageUrl,
        style,
        size: { width: 20, height: 20 }
      })
      
      analysisResult.value = result
      
      // 模拟处理流程（实际应该用WebSocket）
      simulateProcess()
    } catch (error) {
      console.error('AI分析失败:', error)
      isAnalyzing.value = false
    }
  }
  
  // 模拟处理流程
  function simulateProcess() {
    // 语义分割
    setTimeout(() => {
      currentStep.value = 2
      analysisProgress.value = 20
      semanticStatus.value = '完成'
      
      // 针法匹配
      setTimeout(() => {
        currentStep.value = 3
        analysisProgress.value = 40
        stitchStatus.value = '完成'
        
        // 路径规划
        setTimeout(() => {
          currentStep.value = 4
          analysisProgress.value = 60
          pathStatus.value = '完成'
          
          // DST生成
          setTimeout(() => {
            currentStep.value = 5
            analysisProgress.value = 100
            dstStatus.value = '完成'
            dstFileUrl.value = '/downloads/design_pattern.dst'
            isAnalyzing.value = false
          }, 1500)
        }, 1500)
      }, 1500)
    }, 1500)
  }
  
  return {
    isAnalyzing,
    analysisProgress,
    currentStep,
    analysisResult,
    dstFileUrl,
    semanticStatus,
    stitchStatus,
    pathStatus,
    dstStatus,
    startAnalysis
  }
})
```

**store/order.ts** - 订单状态

```typescript
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { orderApi } from '@/services/api'
import type { OrderVO, OrderDetailVO } from '@/types/order'

export const useOrderStore = defineStore('order', () => {
  const orders = ref<OrderVO[]>([])
  const currentOrder = ref<OrderDetailVO | null>(null)
  const loading = ref(false)
  
  // 获取订单列表
  async function fetchOrders(status?: string) {
    loading.value = true
    try {
      orders.value = await orderApi.getOrders(status)
    } finally {
      loading.value = false
    }
  }
  
  // 获取订单详情
  async function fetchOrderDetail(id: number) {
    loading.value = true
    try {
      currentOrder.value = await orderApi.getOrderDetail(id)
    } finally {
      loading.value = false
    }
  }
  
  return {
    orders,
    currentOrder,
    loading,
    fetchOrders,
    fetchOrderDetail
  }
})
```

---

## 6. 迁移步骤

### Step 1: 初始化Vue 3项目

```bash
# 使用Vite创建Vue项目
npm create vue@latest cixiu-frontend

# 选择配置：
# ✅ TypeScript
# ✅ Router
# ✅ Pinia
# ✅ ESLint
# ✅ Prettier

cd cixiu-frontend
npm install

# 安装Tailwind CSS
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p

# 安装其他依赖
npm install axios
npm install @vueuse/core  # Vue工具库
```

### Step 2: 配置Tailwind CSS

**tailwind.config.js**

```javascript
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        heading: ['Poppins', 'sans-serif'],
        body: ['Open Sans', 'sans-serif'],
      },
      colors: {
        canvas: {
          neutral: '#0A0A0F',
          dark: '#000000',
          light: '#1A1A2E',
        },
        accent: {
          primary: '#8B5CF6',
          secondary: '#A78BFA',
          cta: '#06B6D4',
          vibrant: '#10B981',
          neon: '#EC4899',
        }
      }
    }
  },
  plugins: [],
}
```

### Step 3: 迁移全局样式

将原有HTML中的`<style>`标签内容提取到：
- `src/assets/styles/tailwind.css` - Tailwind入口
- `src/assets/styles/variables.css` - CSS变量
- `src/assets/styles/animations.css` - 动画

### Step 4: 创建基础组件

先创建最常用的组件：
1. `GlowCard.vue` - 发光卡片
2. `NeonButton.vue` - 霓虹按钮
3. `AppNavbar.vue` - 导航栏

### Step 5: 拆分页面

按优先级拆分：
1. 首页 (Home.vue)
2. 风格画廊 (Gallery.vue)
3. AI智能打版 (AIGeneration.vue)

### Step 6: 配置路由

在`router/routes.ts`中定义路由

### Step 7: 接入API

创建API服务层，替换模拟数据

### Step 8: 添加状态管理

使用Pinia管理全局状态

---

## 7. 关键组件设计示例

### 7.1 GlowCard组件

```vue
<template>
  <div 
    class="glow-card rounded-xl p-6"
    :class="{ 'hover:scale-105': hover }"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  hover?: boolean
}>()
</script>

<style scoped>
.glow-card {
  position: relative;
  background: rgba(0, 0, 0, 0.4);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(139, 92, 246, 0.3);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.3),
    inset 0 1px 0 rgba(255, 255, 255, 0.1),
    0 0 0 1px rgba(139, 92, 246, 0.1);
  transition: all 0.3s;
}

.glow-card:hover {
  border-color: rgba(139, 92, 246, 0.6);
  box-shadow: 
    0 8px 32px rgba(139, 92, 246, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.2),
    0 0 20px rgba(139, 92, 246, 0.3);
}
</style>
```

### 7.2 ImageUpload组件

```vue
<template>
  <div 
    class="border-2 border-dashed border-accent-primary/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-primary/60 transition-all duration-300"
    :class="aspectClass"
    @click="triggerUpload"
    @dragover.prevent="handleDragOver"
    @dragleave.prevent="handleDragLeave"
    @drop.prevent="handleDrop"
  >
    <input 
      ref="fileInput"
      type="file" 
      class="hidden" 
      :accept="accept"
      @change="handleFileChange"
    />
    
    <div v-if="!previewUrl" class="absolute inset-0 flex flex-col items-center justify-center">
      <svg class="w-16 h-16 mb-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
      </svg>
      <p class="text-gray-300 mb-2">{{ hint }}</p>
      <p class="text-sm text-gray-500">{{ subHint }}</p>
    </div>
    
    <div v-else class="absolute inset-0">
      <img :src="previewUrl" class="w-full h-full object-contain rounded-xl" alt="预览">
      <button 
        @click.stop="removePreview"
        class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300"
      >
        <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = withDefaults(defineProps<{
  aspect?: 'square' | 'video' | 'auto'
  accept?: string
  hint?: string
  subHint?: string
}>(), {
  aspect: 'square',
  accept: 'image/*',
  hint: '拖拽图片到此处',
  subHint: '或点击选择文件'
})

const emit = defineEmits<{
  (e: 'upload', file: File): void
  (e: 'remove'): void
}>()

const fileInput = ref<HTMLInputElement>()
const previewUrl = ref<string>('')
const isDragging = ref(false)

const aspectClass = computed(() => {
  if (props.aspect === 'square') return 'aspect-square'
  if (props.aspect === 'video') return 'aspect-video'
  return ''
})

function triggerUpload() {
  fileInput.value?.click()
}

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    handleFile(file)
  }
}

function handleFile(file: File) {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target?.result as string
  }
  reader.readAsDataURL(file)
  emit('upload', file)
}

function removePreview() {
  previewUrl.value = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  emit('remove')
}

function handleDragOver() {
  isDragging.value = true
}

function handleDragLeave() {
  isDragging.value = false
}

function handleDrop(event: DragEvent) {
  isDragging.value = false
  const file = event.dataTransfer?.files[0]
  if (file) {
    handleFile(file)
  }
}
</script>
```

---

## 8. 下一步行动

1. ✅ 创建Vue 3项目骨架
2. ✅ 配置Tailwind CSS
3. ✅ 创建基础组件库
4. ✅ 拆分页面组件
5. ✅ 配置路由
6. ✅ 接入API服务
7. ✅ 添加状态管理
8. ✅ 测试和优化

---

## 9. 注意事项

1. **渐进式迁移**：不要一次性迁移所有页面，先迁移核心功能
2. **保持样式一致**：确保Tailwind配置与原有样式一致
3. **API对接**：先使用Mock数据，再逐步对接真实API
4. **性能优化**：使用Vue 3的`<Suspense>`处理异步组件
5. **类型安全**：逐步添加TypeScript类型定义

---

## 10. 参考资源

- [Vue 3 官方文档](https://cn.vuejs.org/)
- [Vite 官方文档](https://cn.vitejs.dev/)
- [Pinia 官方文档](https://pinia.vuejs.org/zh/)
- [Vue Router 官方文档](https://router.vuejs.org/zh/)
- [Tailwind CSS 官方文档](https://tailwindcss.com/docs)
