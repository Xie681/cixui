# 前端 API 接口文档

## 📋 目录

- [基础配置](#基础配置)
- [API 结构](#api-结构)
- [认证接口](#认证接口)
- [用户中心](#用户中心)
- [文件上传](#文件上传)
- [定制服务](#定制服务)
- [类型定义](#类型定义)
- [使用示例](#使用示例)
- [错误处理](#错误处理)

---

## 基础配置

### API 基础地址

```typescript
baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
timeout: 30000
```

### 请求拦截器

所有请求会自动添加 `Authorization` 头（如果用户已登录）：

```typescript
headers: {
  'Authorization': `Bearer ${token}`
}
```

### 响应拦截器

统一处理响应格式：

```typescript
// 成功响应 (code === 200)
return data

// Token过期 (code === 401)
// 自动清除token并跳转登录页

// 其他错误
return Promise.reject(new Error(message || '请求失败'))
```

---

## API 结构

### 文件组织

```
src/services/api/
├── index.ts          # Axios实例和拦截器
├── auth.ts          # 认证相关接口
├── user.ts          # 用户中心接口
├── file.ts          # 文件上传接口
└── customize.ts     # 定制服务接口
```

### 统一响应格式

所有接口返回统一格式：

```typescript
interface Result<T> {
  code: number        // 200: 成功, 其他: 失败
  message: string     // 错误信息
  data: T            // 响应数据
  timestamp: number   // 时间戳
}
```

**注意**：响应拦截器会自动提取 `data` 字段，所以实际使用时直接获取 `data` 的值。

---

## 认证接口

### 1. 用户注册

**接口路径**: `POST /api/auth/register`

**请求参数**:

```typescript
interface RegisterDTO {
  phone: string      // 手机号
  password: string  // 密码
  code: string      // 验证码
  nickname?: string // 昵称（可选）
}
```

**响应数据**:

```typescript
interface LoginVO {
  token: string
  refreshToken: string
  user: UserProfileVO
}
```

**使用示例**:

```typescript
import { authApi } from '@/services/api'

const result = await authApi.register({
  phone: '13800138000',
  password: '123456',
  code: '123456',
  nickname: '用户昵称'
})
```

---

### 2. 密码登录

**接口路径**: `POST /api/auth/login`

**请求参数**:

```typescript
interface LoginDTO {
  phone: string     // 手机号
  password: string  // 密码
}
```

**响应数据**: `LoginVO`

**使用示例**:

```typescript
const result = await authApi.login({
  phone: '13800138000',
  password: '123456'
})
```

---

### 3. 验证码登录

**接口路径**: `POST /api/auth/code-login`

**请求参数**:

```typescript
interface CodeLoginDTO {
  phone: string  // 手机号
  code: string   // 验证码
}
```

**响应数据**: `LoginVO`

**使用示例**:

```typescript
const result = await authApi.codeLogin({
  phone: '13800138000',
  code: '123456'
})
```

---

### 4. 发送验证码

**接口路径**: `POST /api/auth/send-code`

**请求参数**:

```typescript
interface SendCodeDTO {
  phone: string  // 手机号
}
```

**响应数据**: `void`

**使用示例**:

```typescript
await authApi.sendCode({
  phone: '13800138000'
})
```

---

### 5. 刷新Token

**接口路径**: `POST /api/auth/refresh-token?refreshToken={refreshToken}`

**请求参数**: `refreshToken` (查询参数)

**响应数据**: `LoginVO`

**使用示例**:

```typescript
const result = await authApi.refreshToken(refreshToken)
```

---

### 6. 登出

**接口路径**: `POST /api/auth/logout`

**请求参数**: 无

**响应数据**: `void`

**使用示例**:

```typescript
await authApi.logout()
```

---

## 用户中心

### 1. 获取个人信息

**接口路径**: `GET /api/user/profile`

**请求参数**: 无

**响应数据**:

```typescript
interface UserProfileVO {
  id: number
  phone: string
  email: string
  nickname: string
  avatar: string
  role: 'USER' | 'DESIGNER' | 'ADMIN'
  createdAt: string  // ISO 8601 格式
}
```

**使用示例**:

```typescript
import { userApi } from '@/services/api'

const profile = await userApi.getProfile()
console.log(profile.nickname)
```

---

### 2. 更新个人信息

**接口路径**: `PUT /api/user/profile`

**请求参数**:

```typescript
interface UpdateProfileDTO {
  nickname?: string  // 昵称（可选）
  email?: string    // 邮箱（可选）
  avatar?: string   // 头像URL（可选）
}
```

**响应数据**: `void`

**使用示例**:

```typescript
await userApi.updateProfile({
  nickname: '新昵称',
  email: 'new@example.com'
})
```

---

## 文件上传

### 1. 上传图片

**接口路径**: `POST /api/file/upload/image`

**请求参数**: `FormData`

```typescript
formData.append('file', File)
```

**响应数据**:

```typescript
{
  url: string      // 图片URL
  fileName: string // 文件名
}
```

**使用示例**:

```typescript
import { fileApi } from '@/services/api'

const file = event.target.files[0]
const result = await fileApi.uploadImage(file)
console.log(result.url)
```

---

### 2. 上传头像

**接口路径**: `POST /api/file/upload/avatar`

**请求参数**: `FormData`

**响应数据**:

```typescript
{
  url: string  // 头像URL
}
```

**使用示例**:

```typescript
const result = await fileApi.uploadAvatar(file)
```

---

### 3. 上传设计文件

**接口路径**: `POST /api/file/upload/design`

**请求参数**: `FormData`

**响应数据**:

```typescript
{
  url: string      // 文件URL
  fileName: string // 文件名
}
```

**使用示例**:

```typescript
const result = await fileApi.uploadDesign(file)
```

---

## 定制服务

### 1. 获取绣制风格列表

**接口路径**: `GET /api/customize/styles`

**请求参数**: 无

**响应数据**:

```typescript
interface StyleVO {
  id: number
  name: string
  description: string
  imageUrl: string
  price: number
}[]
```

**使用示例**:

```typescript
import { customizeApi } from '@/services/api'

const styles = await customizeApi.getStyles()
```

---

### 2. 获取面料列表

**接口路径**: `GET /api/customize/fabrics`

**请求参数**: 无

**响应数据**: `string[]`

**使用示例**:

```typescript
const fabrics = await customizeApi.getFabrics()
```

---

### 3. 获取线材列表

**接口路径**: `GET /api/customize/threads`

**请求参数**: 无

**响应数据**: `string[]`

**使用示例**:

```typescript
const threads = await customizeApi.getThreads()
```

---

### 4. 计算报价

**接口路径**: `POST /api/customize/calculate-price`

**请求参数**:

```typescript
interface CalculatePriceDTO {
  style: string      // 风格ID
  fabric: string     // 面料
  thread: string    // 线材
  size: {
    width: number   // 宽度（cm）
    height: number   // 高度（cm）
  }
  complexity: number // 复杂度 (1-10)
  quantity: number  // 数量
}
```

**响应数据**:

```typescript
interface PriceVO {
  basePrice: number      // 基础价格
  materialPrice: number  // 材料价格
  laborPrice: number    // 人工价格
  totalPrice: number    // 总价
  estimatedDays: number // 预计天数
}
```

**使用示例**:

```typescript
const price = await customizeApi.calculatePrice({
  style: '1',
  fabric: '真丝',
  thread: '丝线',
  size: { width: 20, height: 20 },
  complexity: 5,
  quantity: 1
})
```

---

### 5. AI预览刺绣效果

**接口路径**: `POST /api/customize/preview-ai-result`

**请求参数**:

```typescript
interface AiPreviewDTO {
  imageUrl?: string  // 图片URL（可选）
  text?: string     // 文字内容（可选）
  style: string     // 风格ID
  colors?: string[] // 颜色数组（可选）
  size: {
    width: number
    height: number
  }
}
```

**响应数据**:

```typescript
interface AiPreviewVO {
  previewImageUrl: string  // 预览图URL
  stitchCount: number      // 针数
  estimatedTime: number    // 预计时间（分钟）
  suggestions: string[]    // 建议
}
```

**使用示例**:

```typescript
const preview = await customizeApi.previewAiResult({
  imageUrl: 'https://example.com/image.jpg',
  style: '1',
  size: { width: 20, height: 20 }
})
```

---

### 6. 提交图片定制订单

**接口路径**: `POST /api/customize/submit-order`

**请求参数**:

```typescript
interface SubmitOrderDTO {
  imageUrl: string   // 图片URL
  style: string     // 风格ID
  fabric: string    // 面料
  thread: string    // 线材
  size: {
    width: number
    height: number
  }
  colors?: string[] // 颜色数组（可选）
  addressId: number // 地址ID
  quantity: number  // 数量
  remark?: string   // 备注（可选）
}
```

**响应数据**: `string` (订单ID)

**使用示例**:

```typescript
const orderId = await customizeApi.submitOrder({
  imageUrl: 'https://example.com/image.jpg',
  style: '1',
  fabric: '真丝',
  thread: '丝线',
  size: { width: 20, height: 20 },
  addressId: 1,
  quantity: 1,
  remark: '请仔细制作'
})
```

---

### 7. 提交文字定制订单

**接口路径**: `POST /api/customize/submit-text-order`

**请求参数**: `any` (待后端定义)

**响应数据**: `string` (订单ID)

**使用示例**:

```typescript
const orderId = await customizeApi.submitTextOrder({
  // 待后端定义
})
```

---

## 类型定义

### 完整类型定义

```typescript
// 用户相关
interface UserProfileVO {
  id: number
  phone: string
  email: string
  nickname: string
  avatar: string
  role: 'USER' | 'DESIGNER' | 'ADMIN'
  createdAt: string
}

// 认证相关
interface LoginVO {
  token: string
  refreshToken: string
  user: UserProfileVO
}

// 定制相关
interface StyleVO {
  id: number
  name: string
  description: string
  imageUrl: string
  price: number
}

interface PriceVO {
  basePrice: number
  materialPrice: number
  laborPrice: number
  totalPrice: number
  estimatedDays: number
}

interface AiPreviewVO {
  previewImageUrl: string
  stitchCount: number
  estimatedTime: number
  suggestions: string[]
}
```

---

## 使用示例

### 在 Vue 组件中使用

```vue
<template>
  <div>
    <button @click="handleLogin">登录</button>
    <button @click="handleGetProfile">获取个人信息</button>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { authApi, userApi } from '@/services/api'
import { useUserStore } from '@/store/user'

const userStore = useUserStore()

async function handleLogin() {
  try {
    const result = await authApi.login({
      phone: '13800138000',
      password: '123456'
    })
    
    // 保存token和用户信息
    userStore.token = result.token
    userStore.refreshToken = result.refreshToken
    userStore.userInfo = result.user
    
    console.log('登录成功')
  } catch (error) {
    console.error('登录失败:', error)
  }
}

async function handleGetProfile() {
  try {
    const profile = await userApi.getProfile()
    console.log('用户信息:', profile)
  } catch (error) {
    console.error('获取用户信息失败:', error)
  }
}
</script>
```

### 在 Store 中使用

```typescript
import { defineStore } from 'pinia'
import { authApi, userApi } from '@/services/api'

export const useUserStore = defineStore('user', () => {
  const token = ref<string | null>(null)
  const userInfo = ref<UserProfileVO | null>(null)
  
  async function login(phone: string, password: string) {
    const result = await authApi.login({ phone, password })
    token.value = result.token
    userInfo.value = result.user
  }
  
  async function fetchProfile() {
    const profile = await userApi.getProfile()
    userInfo.value = profile
  }
  
  return { token, userInfo, login, fetchProfile }
})
```

### 在 Composables 中使用

```typescript
import { ref } from 'vue'
import { customizeApi } from '@/services/api'

export function useCustomize() {
  const styles = ref<StyleVO[]>([])
  const loading = ref(false)
  
  async function loadStyles() {
    loading.value = true
    try {
      styles.value = await customizeApi.getStyles()
    } catch (error) {
      console.error('加载风格失败:', error)
    } finally {
      loading.value = false
    }
  }
  
  return { styles, loading, loadStyles }
}
```

---

## 错误处理

### 统一错误处理

所有接口调用都应该使用 `try-catch` 处理错误：

```typescript
try {
  const result = await authApi.login({ phone, password })
  // 处理成功
} catch (error) {
  // 处理错误
  console.error('登录失败:', error.message)
  // 显示错误提示
}
```

### 错误类型

- **401**: Token过期，已自动跳转登录页
- **400**: 请求参数错误
- **500**: 服务器错误
- **网络错误**: 请求超时或网络不可用

### 响应拦截器处理

响应拦截器会自动处理以下情况：

1. **成功响应** (`code === 200`): 返回 `data` 字段
2. **Token过期** (`code === 401`): 自动清除token并跳转登录页
3. **其他错误**: 抛出错误，包含 `message` 信息

---

## 环境变量配置

在 `.env` 文件中配置 API 基础地址：

```env
VITE_API_BASE_URL=http://localhost:8080
```

生产环境：

```env
VITE_API_BASE_URL=https://api.example.com
```

---

## 注意事项

1. **Token管理**: Token会自动通过请求拦截器添加到请求头，无需手动处理
2. **响应格式**: 响应拦截器已自动提取 `data` 字段，直接使用返回的数据
3. **错误处理**: 401错误会自动处理，其他错误需要手动捕获
4. **文件上传**: 文件上传接口使用 `FormData`，会自动设置正确的 `Content-Type`
5. **类型安全**: 所有接口都有完整的 TypeScript 类型定义，建议启用严格模式

---

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本
- 实现认证、用户中心、文件上传、定制服务接口

---

## 参考文档

- [后端 API 文档](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)
- [Axios 文档](https://axios-http.com/)
- [Vue 3 文档](https://vuejs.org/)
