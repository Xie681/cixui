# API 快速参考指南

## 📦 导入方式

```typescript
import { authApi, userApi, fileApi, customizeApi } from '@/services/api'
```

---

## 🔐 认证接口

```typescript
// 注册
await authApi.register({ phone, password, code, nickname? })

// 密码登录
await authApi.login({ phone, password })

// 验证码登录
await authApi.codeLogin({ phone, code })

// 发送验证码
await authApi.sendCode({ phone })

// 刷新Token
await authApi.refreshToken(refreshToken)

// 登出
await authApi.logout()
```

---

## 👤 用户中心

```typescript
// 获取个人信息
const profile = await userApi.getProfile()

// 更新个人信息
await userApi.updateProfile({ nickname?, email?, avatar? })
```

---

## 📁 文件上传

```typescript
// 上传图片
const { url, fileName } = await fileApi.uploadImage(file)

// 上传头像
const { url } = await fileApi.uploadAvatar(file)

// 上传设计文件
const { url, fileName } = await fileApi.uploadDesign(file)
```

---

## 🎨 定制服务

```typescript
// 获取风格列表
const styles = await customizeApi.getStyles()

// 获取面料列表
const fabrics = await customizeApi.getFabrics()

// 获取线材列表
const threads = await customizeApi.getThreads()

// 计算报价
const price = await customizeApi.calculatePrice({
  style, fabric, thread, size, complexity, quantity
})

// AI预览
const preview = await customizeApi.previewAiResult({
  imageUrl?, text?, style, colors?, size
})

// 提交订单
const orderId = await customizeApi.submitOrder({
  imageUrl, style, fabric, thread, size, colors?, addressId, quantity, remark?
})
```

---

## 📝 类型定义

```typescript
// 用户信息
interface UserProfileVO {
  id: number
  phone: string
  email: string
  nickname: string
  avatar: string
  role: 'USER' | 'DESIGNER' | 'ADMIN'
  createdAt: string
}

// 登录响应
interface LoginVO {
  token: string
  refreshToken: string
  user: UserProfileVO
}

// 风格
interface StyleVO {
  id: number
  name: string
  description: string
  imageUrl: string
  price: number
}

// 报价
interface PriceVO {
  basePrice: number
  materialPrice: number
  laborPrice: number
  totalPrice: number
  estimatedDays: number
}

// AI预览
interface AiPreviewVO {
  previewImageUrl: string
  stitchCount: number
  estimatedTime: number
  suggestions: string[]
}
```

---

## ⚠️ 错误处理

```typescript
try {
  const result = await authApi.login({ phone, password })
} catch (error) {
  console.error('登录失败:', error.message)
}
```

---

## 🔧 配置

```env
# .env
VITE_API_BASE_URL=http://localhost:8080
```
