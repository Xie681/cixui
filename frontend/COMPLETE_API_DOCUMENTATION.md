# 前后端完整接口文档

> 基于后端代码和前端代码生成的完整API接口文档  
> 后端API文档: [https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)

## 📋 目录

- [基础信息](#基础信息)
- [统一响应格式](#统一响应格式)
- [认证管理](#认证管理)
- [用户中心](#用户中心)
- [文件管理](#文件管理)
- [定制管理](#定制管理)
- [订单管理](#订单管理)
- [健康检查](#健康检查)
- [枚举类型](#枚举类型)
- [前端API服务](#前端api服务)
- [错误码说明](#错误码说明)

---

## 基础信息

### API 基础地址

- **开发环境**: `http://localhost:8080`
- **生产环境**: 根据环境变量配置

### 请求头

所有需要认证的接口都需要在请求头中携带Token：

```
Authorization: Bearer {token}
```

### Content-Type

- **JSON请求**: `application/json`
- **文件上传**: `multipart/form-data`

---

## 统一响应格式

所有接口返回统一格式：

```typescript
interface Result<T> {
  code: number        // 响应码: 200成功, 其他失败
  message: string     // 响应消息
  data: T            // 响应数据
  timestamp: number   // 时间戳(毫秒)
}
```

**前端处理**: 响应拦截器会自动提取 `data` 字段，直接使用返回的数据。

---

## 认证管理

### 1. 用户注册

**接口路径**: `POST /api/auth/register`

**请求参数**:

```typescript
interface RegisterDTO {
  phone?: string      // 手机号（手机号或邮箱必填其一）
  email?: string      // 邮箱（手机号或邮箱必填其一）
  password: string    // 密码（6-20位）
  code: string        // 验证码
  nickname?: string   // 昵称（可选，最大50字符）
}
```

**验证规则**:
- `phone`: 格式 `^1[3-9]\\d{9}$`
- `email`: 标准邮箱格式
- `password`: 6-20位字符
- `code`: 必填

**响应数据**:

```typescript
interface LoginVO {
  token: string
  refreshToken: string
  user: UserProfileVO
}
```

**前端实现**:

```typescript
// frontend/src/services/api/auth.ts
await authApi.register({
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
  account: string   // 账号（手机号或邮箱）
  password: string  // 密码
}
```

**响应数据**: `LoginVO`

**前端实现**:

```typescript
await authApi.login({
  account: '13800138000',
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

**前端实现**:

```typescript
await authApi.codeLogin({
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

**前端实现**:

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

**前端实现**:

```typescript
await authApi.refreshToken(refreshToken)
```

---

### 6. 登出

**接口路径**: `POST /api/auth/logout`

**请求头**: 需要认证

**响应数据**: `void`

**前端实现**:

```typescript
await authApi.logout()
```

---

## 用户中心

### 1. 获取个人信息

**接口路径**: `GET /api/user/profile`

**请求头**: 需要认证

**响应数据**:

```typescript
interface UserProfileVO {
  id: number
  phone: string
  email: string
  nickname: string
  avatar: string
  role: 'USER' | 'DESIGNER' | 'ADMIN'
  createdAt: string  // ISO 8601格式
}
```

**前端实现**:

```typescript
// frontend/src/services/api/user.ts
const profile = await userApi.getProfile()
```

---

### 2. 更新个人信息

**接口路径**: `PUT /api/user/profile`

**请求头**: 需要认证

**请求参数**:

```typescript
interface UpdateProfileDTO {
  nickname?: string  // 昵称（可选）
  email?: string    // 邮箱（可选）
  avatar?: string   // 头像URL（可选）
}
```

**响应数据**: `void`

**前端实现**:

```typescript
await userApi.updateProfile({
  nickname: '新昵称',
  email: 'new@example.com'
})
```

---

### 3. 获取收货地址列表

**接口路径**: `GET /api/user/addresses`

**请求头**: 需要认证

**响应数据**:

```typescript
interface AddressVO {
  id: number
  name: string           // 收货人姓名
  phone: string          // 手机号
  province: string       // 省
  city: string          // 市
  district: string       // 区
  detail: string         // 详细地址
  fullAddress: string    // 完整地址（省市区+详细地址）
  isDefault: boolean     // 是否默认地址
  createdAt: string      // ISO 8601格式
}[]
```

**前端实现**: 需要添加

```typescript
// 建议添加到 frontend/src/services/api/user.ts
const addresses = await userApi.listAddresses()
```

---

### 4. 获取地址详情

**接口路径**: `GET /api/user/addresses/{id}`

**请求头**: 需要认证

**路径参数**: `id` (地址ID)

**响应数据**: `AddressVO`

**前端实现**: 需要添加

---

### 5. 添加收货地址

**接口路径**: `POST /api/user/addresses`

**请求头**: 需要认证

**请求参数**:

```typescript
interface AddAddressDTO {
  name: string        // 收货人姓名（必填，最大50字符）
  phone: string       // 手机号（必填，格式：^1[3-9]\\d{9}$）
  province: string    // 省（必填）
  city: string        // 市（必填）
  district: string    // 区（必填）
  detail: string      // 详细地址（必填，最大255字符）
  isDefault?: boolean // 是否默认地址（可选）
}
```

**响应数据**: `number` (地址ID)

**前端实现**: 需要添加

---

### 6. 更新收货地址

**接口路径**: `PUT /api/user/addresses/{id}`

**请求头**: 需要认证

**路径参数**: `id` (地址ID)

**请求参数**:

```typescript
interface UpdateAddressDTO {
  name?: string
  phone?: string
  province?: string
  city?: string
  district?: string
  detail?: string
  isDefault?: boolean
}
```

**响应数据**: `void`

**前端实现**: 需要添加

---

### 7. 删除收货地址

**接口路径**: `DELETE /api/user/addresses/{id}`

**请求头**: 需要认证

**路径参数**: `id` (地址ID)

**响应数据**: `void`

**前端实现**: 需要添加

---

### 8. 设置默认地址

**接口路径**: `PUT /api/user/addresses/{id}/default`

**请求头**: 需要认证

**路径参数**: `id` (地址ID)

**响应数据**: `void`

**前端实现**: 需要添加

---

## 文件管理

### 1. 上传图片

**接口路径**: `POST /api/file/upload/image`

**Content-Type**: `multipart/form-data`

**请求参数**: `file` (文件)

**限制**:
- 文件类型: 图片文件 (`image/*`)
- 文件大小: 最大 10MB

**响应数据**:

```typescript
{
  url: string      // 图片URL
  fileName: string // 文件名
}
```

**前端实现**:

```typescript
// frontend/src/services/api/file.ts
const result = await fileApi.uploadImage(file)
```

---

### 2. 上传头像

**接口路径**: `POST /api/file/upload/avatar`

**Content-Type**: `multipart/form-data`

**请求参数**: `file` (文件)

**限制**:
- 文件类型: 图片文件 (`image/*`)
- 文件大小: 最大 2MB

**响应数据**:

```typescript
{
  url: string  // 头像URL
}
```

**前端实现**:

```typescript
const result = await fileApi.uploadAvatar(file)
```

---

### 3. 上传设计文件

**接口路径**: `POST /api/file/upload/design`

**Content-Type**: `multipart/form-data`

**请求参数**: `file` (文件)

**限制**:
- 文件大小: 最大 50MB

**响应数据**:

```typescript
{
  url: string      // 文件URL
  fileName: string // 文件名
}
```

**前端实现**:

```typescript
const result = await fileApi.uploadDesign(file)
```

---

## 定制管理

### 1. 获取绣制风格列表

**接口路径**: `GET /api/customize/styles`

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

**前端实现**:

```typescript
// frontend/src/services/api/customize.ts
const styles = await customizeApi.getStyles()
```

---

### 2. 获取面料列表

**接口路径**: `GET /api/customize/fabrics`

**响应数据**: `string[]`

**前端实现**:

```typescript
const fabrics = await customizeApi.getFabrics()
```

---

### 3. 获取线材列表

**接口路径**: `GET /api/customize/threads`

**响应数据**: `string[]`

**前端实现**:

```typescript
const threads = await customizeApi.getThreads()
```

---

### 4. 计算报价

**接口路径**: `POST /api/customize/calculate-price`

**请求参数**:

```typescript
interface CalculatePriceDTO {
  type: 'IMAGE' | 'TEXT'              // 定制类型
  embroideryStyle?: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格
  stitchCount: number                 // 针数（必填，>=1）
  colorCount: number                  // 色数（必填，>=1）
  trimCount: number                   // 剪线次数（必填，>=0）
  width: number                       // 宽度(cm)（必填，>=1.0）
  height: number                      // 高度(cm)（必填，>=1.0）
  fabricType: string                  // 面料类型（必填）
  threadType: string                  // 线材类型（必填）
  quantity: number                    // 数量（必填，>=1）
  isUrgent?: boolean                  // 是否加急（可选）
}
```

**响应数据**:

```typescript
interface PriceVO {
  basePrice: number      // 基础价
  stitchFee: number      // 针数费用
  colorFee: number       // 色数费用
  fabricFee: number      // 面料费用
  threadFee: number      // 线材费用
  subtotal: number       // 小计
  urgentFactor: number   // 加急系数
  urgentFee: number      // 加急费用
  totalAmount: number    // 总价
  description: string    // 明细说明
}
```

**前端实现**:

```typescript
const price = await customizeApi.calculatePrice({
  type: 'IMAGE',
  embroideryStyle: 'SU',
  stitchCount: 1000,
  colorCount: 5,
  trimCount: 10,
  width: 20,
  height: 20,
  fabricType: '真丝',
  threadType: '丝线',
  quantity: 1,
  isUrgent: false
})
```

---

### 5. AI预览刺绣效果

**接口路径**: `POST /api/customize/preview-ai-result`

**请求参数**:

```typescript
interface AiPreviewDTO {
  imageUrl?: string        // 图片URL（图片定制时必填）
  text?: string           // 文字内容（文字定制时必填）
  fontStyle?: string      // 字体样式（文字定制时使用）
  fontSize?: number       // 字体大小（文字定制时使用）
  color?: string          // 颜色（文字定制时使用）
  embroideryStyle: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格（必填）
}
```

**响应数据**:

```typescript
interface AiPreviewVO {
  resultImageUrl: string  // AI生成的结果图URL
  stitchCount: number     // 针数
  colorCount: number      // 色数
}
```

**前端实现**:

```typescript
const preview = await customizeApi.previewAiResult({
  imageUrl: 'https://example.com/image.jpg',
  embroideryStyle: 'SU'
})
```

---

### 6. 提交图片定制订单

**接口路径**: `POST /api/customize/submit-order`

**请求头**: 需要认证

**请求参数**:

```typescript
interface SubmitOrderDTO {
  imageUrl: string                    // 图片URL（必填）
  embroideryStyle: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格（必填）
  width: number                       // 宽度(cm)（必填，1-200）
  height: number                      // 高度(cm)（必填，1-200）
  fabricType: string                  // 面料类型（必填）
  threadType: string                  // 线材类型（必填）
  quantity: number                    // 数量（必填，>=1）
  isUrgent?: boolean                  // 是否加急（可选）
  remark?: string                     // 备注（可选）
  addressId: number                   // 收货地址ID（必填）
  designId?: number                   // AI生成的设计稿ID（可选）
  resultImageUrl?: string             // AI预览的结果图URL（可选）
  stitchCount?: number                // AI返回的针数（可选）
  colorCount?: number                 // AI返回的色数（可选）
}
```

**响应数据**: `string` (订单号)

**前端实现**:

```typescript
const orderNo = await customizeApi.submitOrder({
  imageUrl: 'https://example.com/image.jpg',
  embroideryStyle: 'SU',
  width: 20,
  height: 20,
  fabricType: '真丝',
  threadType: '丝线',
  quantity: 1,
  addressId: 1,
  remark: '请仔细制作'
})
```

---

### 7. 提交文字定制订单

**接口路径**: `POST /api/customize/submit-text-order`

**请求头**: 需要认证

**请求参数**:

```typescript
interface TextSubmitOrderDTO {
  text: string                        // 文字内容（必填）
  fontStyle?: string                  // 字体样式（可选）
  fontSize?: number                   // 字体大小（可选）
  color?: string                      // 颜色（可选）
  embroideryStyle: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格（必填）
  width: number                       // 宽度(cm)（必填）
  height: number                      // 高度(cm)（必填）
  fabricType: string                  // 面料类型（必填）
  threadType: string                  // 线材类型（必填）
  quantity: number                    // 数量（必填）
  isUrgent?: boolean                  // 是否加急（可选）
  remark?: string                     // 备注（可选）
  addressId: number                   // 收货地址ID（必填）
}
```

**响应数据**: `string` (订单号)

**前端实现**:

```typescript
const orderNo = await customizeApi.submitTextOrder({
  text: '绣生',
  embroideryStyle: 'SU',
  width: 10,
  height: 10,
  fabricType: '真丝',
  threadType: '丝线',
  quantity: 1,
  addressId: 1
})
```

---

## 订单管理

### 1. 获取订单列表

**接口路径**: `GET /api/orders?status={status}`

**请求头**: 需要认证

**查询参数**:
- `status` (可选): 订单状态，如 `PENDING_PAYMENT`, `DESIGNING`, `COMPLETED` 等

**响应数据**:

```typescript
interface OrderVO {
  id: number
  orderNo: string                    // 订单号
  type: 'IMAGE' | 'TEXT'            // 定制类型
  status: OrderStatus                // 订单状态
  statusDesc: string                 // 状态描述
  totalAmount: number                // 总金额
  paidAmount: number                 // 实付金额
  isUrgent: boolean                  // 是否加急
  createdAt: string                  // 创建时间
  paidAt?: string                    // 支付时间
  summary: string                    // 订单详情（简要信息）
  imageUrl?: string                  // 原始图片URL
  resultImageUrl?: string            // AI生成的结果图URL
  embroideryStyle?: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格
  styleDesc?: string                 // 绣制风格描述
  fabricType?: string                // 面料类型
  threadType?: string                // 线材类型
  width?: number                     // 宽度(cm)
  height?: number                    // 高度(cm)
  quantity?: number                  // 数量
}[]
```

**前端实现**: 需要添加

```typescript
// 建议添加到 frontend/src/services/api/order.ts
const orders = await orderApi.listOrders(status)
```

---

### 2. 获取订单详情（按ID）

**接口路径**: `GET /api/orders/{id}`

**请求头**: 需要认证

**路径参数**: `id` (订单ID)

**响应数据**:

```typescript
interface OrderDetailVO {
  id: number
  orderNo: string                    // 订单号
  type: 'IMAGE' | 'TEXT'            // 定制类型
  typeDesc: string                   // 定制类型描述
  status: OrderStatus                // 订单状态
  statusDesc: string                 // 状态描述
  totalAmount: number                // 总金额
  paidAmount: number                 // 实付金额
  addressSnapshot: object           // 收货地址快照
  remark?: string                    // 备注
  isUrgent: boolean                  // 是否加急
  urgentFactor: number               // 加急系数
  createdAt: string                  // 创建时间
  paidAt?: string                    // 支付时间
  sourceImage?: string               // 原始图片URL
  resultImageUrl?: string            // AI生成的结果图URL
  embroideryStyle?: 'SU' | 'XIANG' | 'SHU' | 'YUE'  // 绣制风格
  width?: number                     // 宽度(cm)
  height?: number                    // 高度(cm)
  fabricType?: string                // 面料类型
  threadType?: string                // 线材类型
  stitchCount?: number               // 针数
  colorCount?: number                // 色数
  quantity?: number                  // 数量
  unitPrice: number                  // 单价
  basePrice: number                   // 基础价格
  stitchFee: number                   // 针数费用
  colorFee: number                    // 色数费用
  fabricFee: number                   // 面料费用
  threadFee: number                   // 线材费用
  urgentFee: number                   // 加急费用
  styleDesc?: string                  // 绣制风格描述
}
```

**前端实现**: 需要添加

---

### 3. 获取订单详情（按订单号）

**接口路径**: `GET /api/orders/no/{orderNo}`

**请求头**: 需要认证

**路径参数**: `orderNo` (订单号)

**响应数据**: `OrderDetailVO`

**前端实现**: 需要添加

---

### 4. 取消订单

**接口路径**: `POST /api/orders/{id}/cancel`

**请求头**: 需要认证

**路径参数**: `id` (订单ID)

**响应数据**: `void`

**前端实现**: 需要添加

---

## 健康检查

### 健康检查

**接口路径**: `GET /api/health`

**响应数据**:

```typescript
{
  status: 'UP'
  timestamp: string  // ISO 8601格式
  message: '刺绣定制系统运行正常'
}
```

---

## 枚举类型

### 订单状态 (OrderStatus)

```typescript
enum OrderStatus {
  PENDING_PAYMENT = 'PENDING_PAYMENT'    // 待支付
  DESIGNING = 'DESIGNING'                // 设计中
  SAMPLING = 'SAMPLING'                  // 打样中
  REVIEWING = 'REVIEWING'                // 复审中
  SCHEDULING = 'SCHEDULING'              // 排产中
  EMBROIDERING = 'EMBROIDERING'          // 绣制中
  QUALITY_CHECK = 'QUALITY_CHECK'        // 质检中
  PENDING_SHIP = 'PENDING_SHIP'          // 待发货
  SHIPPED = 'SHIPPED'                    // 已发货
  COMPLETED = 'COMPLETED'                // 已完成
  CANCELLED = 'CANCELLED'                // 已取消
}
```

### 定制类型 (OrderType)

```typescript
enum OrderType {
  IMAGE = 'IMAGE'  // 图片定制
  TEXT = 'TEXT'    // 文字定制
}
```

### 刺绣风格 (EmbroideryStyle)

```typescript
enum EmbroideryStyle {
  SU = 'SU'        // 苏绣
  XIANG = 'XIANG'  // 湘绣
  SHU = 'SHU'      // 蜀绣
  YUE = 'YUE'      // 粤绣
}
```

### 用户角色 (UserRole)

```typescript
enum UserRole {
  USER = 'USER'           // 普通用户
  DESIGNER = 'DESIGNER'   // 设计师
  ADMIN = 'ADMIN'        // 管理员
}
```

---

## 前端API服务

### 已实现的API服务

#### 认证服务 (`frontend/src/services/api/auth.ts`)

```typescript
import { authApi } from '@/services/api'

// 注册
await authApi.register(data)

// 密码登录
await authApi.login(data)

// 验证码登录
await authApi.codeLogin(data)

// 发送验证码
await authApi.sendCode(data)

// 刷新Token
await authApi.refreshToken(refreshToken)

// 登出
await authApi.logout()
```

#### 用户服务 (`frontend/src/services/api/user.ts`)

```typescript
import { userApi } from '@/services/api'

// 获取个人信息
await userApi.getProfile()

// 更新个人信息
await userApi.updateProfile(data)
```

#### 文件服务 (`frontend/src/services/api/file.ts`)

```typescript
import { fileApi } from '@/services/api'

// 上传图片
await fileApi.uploadImage(file)

// 上传头像
await fileApi.uploadAvatar(file)

// 上传设计文件
await fileApi.uploadDesign(file)
```

#### 定制服务 (`frontend/src/services/api/customize.ts`)

```typescript
import { customizeApi } from '@/services/api'

// 获取风格列表
await customizeApi.getStyles()

// 获取面料列表
await customizeApi.getFabrics()

// 获取线材列表
await customizeApi.getThreads()

// 计算报价
await customizeApi.calculatePrice(data)

// AI预览
await customizeApi.previewAiResult(data)

// 提交图片订单
await customizeApi.submitOrder(data)

// 提交文字订单
await customizeApi.submitTextOrder(data)
```

### 需要添加的API服务

#### 订单服务 (`frontend/src/services/api/order.ts`)

建议创建以下服务：

```typescript
import api from './index'

export interface OrderVO {
  id: number
  orderNo: string
  type: 'IMAGE' | 'TEXT'
  status: string
  statusDesc: string
  totalAmount: number
  paidAmount: number
  isUrgent: boolean
  createdAt: string
  paidAt?: string
  summary: string
  imageUrl?: string
  resultImageUrl?: string
  embroideryStyle?: string
  styleDesc?: string
  fabricType?: string
  threadType?: string
  width?: number
  height?: number
  quantity?: number
}

export interface OrderDetailVO {
  // ... 完整定义见上方
}

export const orderApi = {
  // 获取订单列表
  listOrders(status?: string): Promise<OrderVO[]> {
    const url = status ? `/api/orders?status=${status}` : '/api/orders'
    return api.get(url)
  },
  
  // 获取订单详情（按ID）
  getOrderDetail(id: number): Promise<OrderDetailVO> {
    return api.get(`/api/orders/${id}`)
  },
  
  // 获取订单详情（按订单号）
  getOrderDetailByOrderNo(orderNo: string): Promise<OrderDetailVO> {
    return api.get(`/api/orders/no/${orderNo}`)
  },
  
  // 取消订单
  cancelOrder(id: number): Promise<void> {
    return api.post(`/api/orders/${id}/cancel`)
  }
}
```

#### 地址服务扩展 (`frontend/src/services/api/user.ts`)

建议添加以下方法：

```typescript
export interface AddressVO {
  id: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  fullAddress: string
  isDefault: boolean
  createdAt: string
}

export interface AddAddressDTO {
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  isDefault?: boolean
}

export interface UpdateAddressDTO {
  name?: string
  phone?: string
  province?: string
  city?: string
  district?: string
  detail?: string
  isDefault?: boolean
}

export const userApi = {
  // ... 现有方法
  
  // 获取收货地址列表
  listAddresses(): Promise<AddressVO[]> {
    return api.get('/api/user/addresses')
  },
  
  // 获取地址详情
  getAddress(id: number): Promise<AddressVO> {
    return api.get(`/api/user/addresses/${id}`)
  },
  
  // 添加收货地址
  addAddress(data: AddAddressDTO): Promise<number> {
    return api.post('/api/user/addresses', data)
  },
  
  // 更新收货地址
  updateAddress(id: number, data: UpdateAddressDTO): Promise<void> {
    return api.put(`/api/user/addresses/${id}`, data)
  },
  
  // 删除收货地址
  deleteAddress(id: number): Promise<void> {
    return api.delete(`/api/user/addresses/${id}`)
  },
  
  // 设置默认地址
  setDefaultAddress(id: number): Promise<void> {
    return api.put(`/api/user/addresses/${id}/default`)
  }
}
```

---

## 错误码说明

### HTTP状态码

- `200`: 请求成功
- `400`: 请求参数错误
- `401`: 未授权（Token过期或无效）
- `403`: 禁止访问（权限不足）
- `404`: 资源不存在
- `500`: 服务器内部错误

### 业务错误码

响应中的 `code` 字段：

- `200`: 操作成功
- `400`: 参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源不存在
- `500`: 服务器错误

### 前端错误处理

```typescript
try {
  const result = await authApi.login({ account, password })
  // 处理成功
} catch (error) {
  // 错误信息在 error.message 中
  console.error('登录失败:', error.message)
  // 显示错误提示
}
```

**注意**: 401错误会自动处理（清除token并跳转登录页），其他错误需要手动捕获。

---

## 更新日志

### v1.0.0 (2024-01-01)
- 初始版本
- 实现认证、用户中心、文件上传、定制服务、订单管理接口
- 完整的类型定义和使用示例

---

## 参考文档

- [后端API文档 (Apifox)](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)
- [前端API文档](./API_DOCUMENTATION.md)
- [前端快速参考](./API_QUICK_REFERENCE.md)
