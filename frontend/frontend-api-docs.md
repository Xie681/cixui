# 云绣定制平台 - 前端API接口文档

> 基于后端API文档设计，参考：[后端API文档](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)

## 📋 目录

- [基础说明](#基础说明)
- [认证管理](#认证管理)
- [用户中心](#用户中心)
- [定制管理](#定制管理)
- [订单管理](#订单管理)
- [文件管理](#文件管理)
- [Wilcom集成](#wilcom集成)
- [风格画廊](#风格画廊)
- [设备管理](#设备管理)

---

## 基础说明

### 基础URL

```
开发环境: http://localhost:8080
生产环境: https://api.yunxiu.com
```

### 统一响应格式

所有API响应遵循统一格式：

```typescript
interface Result<T> {
  code: number;        // 状态码：200成功，400客户端错误，500服务器错误
  message: string;     // 响应消息
  data: T;            // 响应数据
  timestamp: number;   // 时间戳
}
```

### 认证方式

大部分接口需要JWT Token认证，在请求头中携带：

```
Authorization: Bearer {token}
```

Token通过登录接口获取，存储在localStorage中。

### 错误处理

- `200`: 请求成功
- `400`: 客户端错误（参数错误、业务逻辑错误等）
- `401`: 未授权（Token过期或无效）
- `403`: 禁止访问（权限不足）
- `500`: 服务器错误

---

## 认证管理

### 1. 用户注册

**接口地址：** `POST /api/auth/register`

**请求参数：**

```typescript
interface RegisterDTO {
  phone: string;        // 手机号（必填）
  password: string;     // 密码（必填，6-20位）
  code: string;         // 验证码（必填）
  nickname?: string;    // 昵称（可选）
}
```

**响应数据：**

```typescript
interface LoginVO {
  token: string;        // 访问令牌
  refreshToken: string; // 刷新令牌
  user: UserProfileVO;  // 用户信息
}
```

**前端调用示例：**

```javascript
async function register(phone, password, code, nickname) {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({
      phone,
      password,
      code,
      nickname
    })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    // 保存token
    localStorage.setItem('token', result.data.token);
    localStorage.setItem('refreshToken', result.data.refreshToken);
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 2. 密码登录

**接口地址：** `POST /api/auth/login`

**请求参数：**

```typescript
interface LoginDTO {
  phone: string;     // 手机号
  password: string;  // 密码
}
```

**响应数据：** `LoginVO`

**前端调用示例：**

```javascript
async function login(phone, password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ phone, password })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    localStorage.setItem('token', result.data.token);
    localStorage.setItem('refreshToken', result.data.refreshToken);
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 3. 验证码登录

**接口地址：** `POST /api/auth/code-login`

**请求参数：**

```typescript
interface CodeLoginDTO {
  phone: string;  // 手机号
  code: string;   // 验证码
}
```

**响应数据：** `LoginVO`

---

### 4. 发送验证码

**接口地址：** `POST /api/auth/send-code`

**请求参数：**

```typescript
interface SendCodeDTO {
  phone: string;  // 手机号
}
```

**响应数据：** `Result<Void>`

**前端调用示例：**

```javascript
async function sendCode(phone) {
  const response = await fetch('http://localhost:8080/api/auth/send-code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ phone })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return true;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 5. 刷新Token

**接口地址：** `POST /api/auth/refresh-token`

**请求参数：**

```
refreshToken: string (Query参数)
```

**响应数据：** `LoginVO`

---

### 6. 登出

**接口地址：** `POST /api/auth/logout`

**请求头：** 需要Authorization

**响应数据：** `Result<Void>`

**前端调用示例：**

```javascript
async function logout() {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/auth/logout', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  if (result.code === 200) {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    return true;
  } else {
    throw new Error(result.message);
  }
}
```

---

## 用户中心

### 1. 获取个人信息

**接口地址：** `GET /api/user/profile`

**请求头：** 需要Authorization

**响应数据：**

```typescript
interface UserProfileVO {
  id: number;           // 用户ID
  phone: string;        // 手机号
  email: string;        // 邮箱
  nickname: string;     // 昵称
  avatar: string;       // 头像URL
  role: 'USER' | 'DESIGNER' | 'ADMIN';  // 角色
  createdAt: string;    // 创建时间 (ISO 8601格式)
}
```

**前端调用示例：**

```javascript
async function getProfile() {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/user/profile', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 2. 更新个人信息

**接口地址：** `PUT /api/user/profile`

**请求头：** 需要Authorization

**请求参数：**

```typescript
interface UpdateProfileDTO {
  nickname?: string;  // 昵称
  email?: string;     // 邮箱
  avatar?: string;    // 头像URL
}
```

**响应数据：** `Result<Void>`

---

### 3. 获取收货地址列表

**接口地址：** `GET /api/user/addresses`

**请求头：** 需要Authorization

**响应数据：**

```typescript
interface AddressVO {
  id: number;              // 地址ID
  receiverName: string;    // 收货人姓名
  receiverPhone: string;   // 收货人电话
  province: string;        // 省份
  city: string;           // 城市
  district: string;       // 区县
  detail: string;         // 详细地址
  isDefault: boolean;     // 是否默认地址
}
```

---

### 4. 获取地址详情

**接口地址：** `GET /api/user/addresses/{id}`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 地址ID

**响应数据：** `AddressVO`

---

### 5. 添加收货地址

**接口地址：** `POST /api/user/addresses`

**请求头：** 需要Authorization

**请求参数：**

```typescript
interface AddAddressDTO {
  receiverName: string;   // 收货人姓名（必填）
  receiverPhone: string;  // 收货人电话（必填）
  province: string;       // 省份（必填）
  city: string;          // 城市（必填）
  district: string;       // 区县（必填）
  detail: string;        // 详细地址（必填）
  isDefault?: boolean;   // 是否设为默认地址
}
```

**响应数据：** `Result<Long>` (返回地址ID)

---

### 6. 更新收货地址

**接口地址：** `PUT /api/user/addresses/{id}`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 地址ID

**请求参数：**

```typescript
interface UpdateAddressDTO {
  receiverName?: string;
  receiverPhone?: string;
  province?: string;
  city?: string;
  district?: string;
  detail?: string;
  isDefault?: boolean;
}
```

**响应数据：** `Result<Void>`

---

### 7. 删除收货地址

**接口地址：** `DELETE /api/user/addresses/{id}`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 地址ID

**响应数据：** `Result<Void>`

---

### 8. 设置默认地址

**接口地址：** `PUT /api/user/addresses/{id}/default`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 地址ID

**响应数据：** `Result<Void>`

---

## 定制管理

### 1. 获取绣制风格列表

**接口地址：** `GET /api/customize/styles`

**响应数据：**

```typescript
interface StyleVO {
  id: number;
  name: string;           // 风格名称（如：苏绣、湘绣、粤绣、蜀绣）
  description: string;    // 风格描述
  imageUrl: string;       // 风格图片URL
  price: number;          // 基础价格
}
```

**前端调用示例：**

```javascript
async function getStyles() {
  const response = await fetch('http://localhost:8080/api/customize/styles');
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 2. 获取面料列表

**接口地址：** `GET /api/customize/fabrics`

**响应数据：** `Result<List<String>>`

示例：`["棉布", "丝绸", "麻布", "真丝缎", "云锦", "蜀锦"]`

---

### 3. 获取线材列表

**接口地址：** `GET /api/customize/threads`

**响应数据：** `Result<List<String>>`

示例：`["丝线", "棉线", "金银线", "混纺线", "24K纯金线", "999纯银线"]`

---

### 4. 计算报价

**接口地址：** `POST /api/customize/calculate-price`

**请求参数：**

```typescript
interface CalculatePriceDTO {
  style: string;          // 绣制风格
  fabric: string;         // 面料类型
  thread: string;         // 线材类型
  size: {
    width: number;       // 宽度（cm）
    height: number;      // 高度（cm）
  };
  complexity: number;     // 复杂度（1-10）
  quantity: number;       // 数量
}
```

**响应数据：**

```typescript
interface PriceVO {
  basePrice: number;      // 基础价格
  materialPrice: number;  // 材料费用
  laborPrice: number;    // 人工费用
  totalPrice: number;     // 总价
  estimatedDays: number;  // 预计工期（天）
}
```

---

### 5. AI预览刺绣效果

**接口地址：** `POST /api/customize/preview-ai-result`

**请求参数：**

```typescript
interface AiPreviewDTO {
  imageUrl?: string;      // 图片URL（图片定制）
  text?: string;          // 文字内容（文字定制）
  style: string;          // 绣制风格
  colors?: string[];      // 颜色方案
  size: {
    width: number;
    height: number;
  };
}
```

**响应数据：**

```typescript
interface AiPreviewVO {
  previewImageUrl: string;  // 预览图片URL
  stitchCount: number;       // 针数估算
  estimatedTime: number;     // 预计工时（小时）
  suggestions: string[];      // AI优化建议
}
```

**前端调用示例：**

```javascript
async function previewAiResult(imageUrl, style, colors, size) {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/customize/preview-ai-result', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      imageUrl,
      style,
      colors,
      size
    })
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 6. 提交图片定制订单

**接口地址：** `POST /api/customize/submit-order`

**请求头：** 需要Authorization

**请求参数：**

```typescript
interface SubmitOrderDTO {
  imageUrl: string;       // 图片URL（必填）
  style: string;          // 绣制风格（必填）
  fabric: string;         // 面料类型（必填）
  thread: string;         // 线材类型（必填）
  size: {
    width: number;
    height: number;
  };
  colors?: string[];      // 颜色方案
  addressId: number;      // 收货地址ID（必填）
  quantity: number;       // 数量（必填，默认1）
  remark?: string;         // 备注
}
```

**响应数据：** `Result<String>` (返回订单号)

---

### 7. 提交文字定制订单

**接口地址：** `POST /api/customize/submit-text-order`

**请求头：** 需要Authorization

**请求参数：**

```typescript
interface TextSubmitOrderDTO {
  text: string;           // 文字内容（必填）
  font: string;           // 字体（必填）
  style: string;          // 绣制风格（必填）
  fabric: string;         // 面料类型（必填）
  thread: string;         // 线材类型（必填）
  size: {
    width: number;
    height: number;
  };
  colors?: string[];      // 颜色方案
  addressId: number;      // 收货地址ID（必填）
  quantity: number;       // 数量（必填，默认1）
  remark?: string;         // 备注
}
```

**响应数据：** `Result<String>` (返回订单号)

---

## 订单管理

### 1. 获取订单列表

**接口地址：** `GET /api/orders`

**请求头：** 需要Authorization

**查询参数：**
- `status`: 订单状态（可选），如：`PENDING`, `PAID`, `PROCESSING`, `COMPLETED`, `CANCELLED`

**响应数据：**

```typescript
interface OrderVO {
  id: number;
  orderNo: string;        // 订单号
  status: string;         // 订单状态
  totalPrice: number;     // 总价
  itemCount: number;      // 商品数量
  createdAt: string;     // 创建时间
  imageUrl?: string;      // 订单图片预览
}
```

**前端调用示例：**

```javascript
async function getOrders(status) {
  const token = localStorage.getItem('token');
  const url = status 
    ? `http://localhost:8080/api/orders?status=${status}`
    : 'http://localhost:8080/api/orders';
    
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 2. 获取订单详情（按ID）

**接口地址：** `GET /api/orders/{id}`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 订单ID

**响应数据：**

```typescript
interface OrderDetailVO {
  id: number;
  orderNo: string;
  status: string;
  totalPrice: number;
  itemPrice: number;      // 商品价格
  materialPrice: number;  // 材料费用
  laborPrice: number;     // 人工费用
  shippingPrice: number;   // 运费
  createdAt: string;
  paidAt?: string;
  completedAt?: string;
  address: AddressVO;      // 收货地址
  items: OrderItemVO[];    // 订单明细
  resultImageUrl?: string; // 完成后的绣品图片
}

interface OrderItemVO {
  id: number;
  imageUrl?: string;       // 定制图片
  text?: string;           // 定制文字
  style: string;           // 绣制风格
  fabric: string;          // 面料
  thread: string;          // 线材
  size: {
    width: number;
    height: number;
  };
  quantity: number;
  price: number;
}
```

---

### 3. 获取订单详情（按订单号）

**接口地址：** `GET /api/orders/no/{orderNo}`

**请求头：** 需要Authorization

**路径参数：**
- `orderNo`: 订单号

**响应数据：** `OrderDetailVO`

---

### 4. 取消订单

**接口地址：** `POST /api/orders/{id}/cancel`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 订单ID

**响应数据：** `Result<Void>`

**前端调用示例：**

```javascript
async function cancelOrder(orderId) {
  const token = localStorage.getItem('token');
  const response = await fetch(`http://localhost:8080/api/orders/${orderId}/cancel`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return true;
  } else {
    throw new Error(result.message);
  }
}
```

---

## 文件管理

### 1. 上传图片

**接口地址：** `POST /api/file/upload/image`

**请求参数：**
- `file`: MultipartFile（图片文件，最大10MB）

**响应数据：**

```typescript
{
  url: string;        // 文件URL
  fileName: string;  // 文件名
}
```

**前端调用示例：**

```javascript
async function uploadImage(file) {
  const token = localStorage.getItem('token');
  const formData = new FormData();
  formData.append('file', file);
  
  const response = await fetch('http://localhost:8080/api/file/upload/image', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    },
    body: formData
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

### 2. 上传头像

**接口地址：** `POST /api/file/upload/avatar`

**请求参数：**
- `file`: MultipartFile（图片文件，最大2MB）

**响应数据：**

```typescript
{
  url: string;  // 头像URL
}
```

---

### 3. 上传设计文件

**接口地址：** `POST /api/file/upload/design`

**请求参数：**
- `file`: MultipartFile（设计文件，最大50MB，支持DST、PES等格式）

**响应数据：**

```typescript
{
  url: string;        // 文件URL
  fileName: string;  // 文件名
}
```

---

## Wilcom集成

### 1. 检查Wilcom是否已安装

**接口地址：** `GET /api/wilcom/check`

**请求头：** 需要Authorization

**响应数据：**

```typescript
{
  installed: boolean;     // 是否已安装
  version?: string;       // 版本号（如果已安装）
  path?: string;          // 安装路径（如果已安装）
}
```

**前端调用示例：**

```javascript
async function checkWilcomInstallation() {
  const token = localStorage.getItem('token');
  const response = await fetch('http://localhost:8080/api/wilcom/check', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data.installed;
  } else {
    return false;
  }
}
```

---

### 2. 获取当前DST文件路径

**接口地址：** `GET /api/wilcom/current-dst`

**请求头：** 需要Authorization

**查询参数：**
- `orderId`: 订单ID（可选）
- `designId`: 设计ID（可选）

**响应数据：**

```typescript
{
  filePath: string;       // DST文件路径
  fileUrl: string;        // DST文件URL
  orderNo?: string;       // 关联订单号
}
```

---

### 3. 在Wilcom中打开文件

**接口地址：** `POST /api/wilcom/open`

**请求头：** 需要Authorization

**请求参数：**

```typescript
{
  filePath: string;       // DST文件路径
  orderId?: number;        // 订单ID（可选）
}
```

**响应数据：** `Result<Void>`

**说明：** 此接口会调用Wilcom的URL协议打开文件：`wilcom://open?file={filePath}`

---

### 4. 检查Wilcom文件更新

**接口地址：** `GET /api/wilcom/check-updates`

**请求头：** 需要Authorization

**查询参数：**
- `orderId`: 订单ID
- `lastModified`: 最后修改时间戳（可选）

**响应数据：**

```typescript
{
  updated: boolean;       // 是否有更新
  filePath?: string;      // 更新后的文件路径
  fileUrl?: string;       // 更新后的文件URL
  modifiedAt?: string;    // 修改时间
}
```

**前端轮询示例：**

```javascript
// 每5秒检查一次Wilcom文件更新
setInterval(async () => {
  const token = localStorage.getItem('token');
  const orderId = getCurrentOrderId(); // 获取当前订单ID
  
  const response = await fetch(
    `http://localhost:8080/api/wilcom/check-updates?orderId=${orderId}`,
    {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }
  );
  
  const result = await response.json();
  if (result.code === 200 && result.data.updated) {
    // 文件已更新，刷新预览
    updateDesignFromWilcom(result.data.fileUrl);
  }
}, 5000);
```

---

### 5. 同步Wilcom修改到平台

**接口地址：** `POST /api/wilcom/sync`

**请求头：** 需要Authorization

**请求参数：**

```typescript
{
  orderId: number;        // 订单ID
  filePath: string;       // Wilcom修改后的文件路径
}
```

**响应数据：** `Result<Void>`

---

## 风格画廊

### 1. 获取风格画廊列表

**接口地址：** `GET /api/gallery/styles`

**查询参数：**
- `category`: 分类（可选），如：`traditional`, `modern`, `abstract`, `floral`
- `page`: 页码（可选，默认1）
- `size`: 每页数量（可选，默认20）

**响应数据：**

```typescript
interface GalleryStyleVO {
  id: number;
  name: string;           // 风格名称
  category: string;       // 分类
  price: number;         // 价格
  image: string;          // 图片标识
  style: string;          // 刺绣风格（suxiu, xiangxiu, yuexiu, shuxiu）
  technique: string;      // 工艺技法
  description: string;    // 描述
}
```

**前端调用示例：**

```javascript
async function getGalleryStyles(category, page = 1, size = 20) {
  const params = new URLSearchParams({
    page: page.toString(),
    size: size.toString()
  });
  if (category) {
    params.append('category', category);
  }
  
  const response = await fetch(
    `http://localhost:8080/api/gallery/styles?${params.toString()}`
  );
  
  const result = await response.json();
  if (result.code === 200) {
    return result.data;
  } else {
    throw new Error(result.message);
  }
}
```

---

## 设备管理

### 1. 获取设备列表

**接口地址：** `GET /api/devices`

**请求头：** 需要Authorization（需要DESIGNER或ADMIN角色）

**响应数据：**

```typescript
interface DeviceVO {
  id: number;
  name: string;           // 设备名称（如：刺绣机 #001）
  status: 'ONLINE' | 'OFFLINE' | 'BUSY' | 'IDLE';  // 设备状态
  progress?: number;      // 当前任务进度（0-100）
  currentTask?: string;   // 当前任务名称
  estimatedTime?: number; // 预计完成时间（分钟）
}
```

---

### 2. 获取设备详情

**接口地址：** `GET /api/devices/{id}`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 设备ID

**响应数据：** `DeviceVO`

---

### 3. 远程控制设备

**接口地址：** `POST /api/devices/{id}/control`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 设备ID

**请求参数：**

```typescript
{
  action: 'start' | 'stop' | 'pause' | 'resume';  // 操作类型
  orderId?: number;  // 订单ID（start操作时需要）
}
```

**响应数据：** `Result<Void>`

---

### 4. 获取实时进度

**接口地址：** `GET /api/devices/{id}/progress`

**请求头：** 需要Authorization

**路径参数：**
- `id`: 设备ID

**响应数据：**

```typescript
{
  progress: number;       // 进度（0-100）
  currentStep: string;    // 当前步骤
  estimatedTime: number;  // 预计剩余时间（分钟）
}
```

---

## 前端工具函数

### 统一请求封装

```javascript
// api.js - 统一API请求封装
const API_BASE_URL = 'http://localhost:8080';

class ApiClient {
  constructor() {
    this.baseURL = API_BASE_URL;
  }
  
  // 获取Token
  getToken() {
    return localStorage.getItem('token');
  }
  
  // 统一请求方法
  async request(url, options = {}) {
    const token = this.getToken();
    const headers = {
      'Content-Type': 'application/json',
      ...options.headers
    };
    
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    
    const response = await fetch(`${this.baseURL}${url}`, {
      ...options,
      headers
    });
    
    const result = await response.json();
    
    // Token过期，尝试刷新
    if (result.code === 401) {
      const refreshed = await this.refreshToken();
      if (refreshed) {
        // 重试请求
        return this.request(url, options);
      } else {
        // 跳转到登录页
        window.location.href = '/login';
        throw new Error('请重新登录');
      }
    }
    
    if (result.code !== 200) {
      throw new Error(result.message || '请求失败');
    }
    
    return result.data;
  }
  
  // 刷新Token
  async refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      return false;
    }
    
    try {
      const response = await fetch(
        `${this.baseURL}/api/auth/refresh-token?refreshToken=${refreshToken}`,
        { method: 'POST' }
      );
      const result = await response.json();
      
      if (result.code === 200) {
        localStorage.setItem('token', result.data.token);
        localStorage.setItem('refreshToken', result.data.refreshToken);
        return true;
      }
    } catch (error) {
      console.error('刷新Token失败:', error);
    }
    
    return false;
  }
  
  // GET请求
  async get(url, params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const fullUrl = queryString ? `${url}?${queryString}` : url;
    return this.request(fullUrl, { method: 'GET' });
  }
  
  // POST请求
  async post(url, data = {}) {
    return this.request(url, {
      method: 'POST',
      body: JSON.stringify(data)
    });
  }
  
  // PUT请求
  async put(url, data = {}) {
    return this.request(url, {
      method: 'PUT',
      body: JSON.stringify(data)
    });
  }
  
  // DELETE请求
  async delete(url) {
    return this.request(url, { method: 'DELETE' });
  }
  
  // 文件上传
  async upload(url, file, fieldName = 'file') {
    const token = this.getToken();
    const formData = new FormData();
    formData.append(fieldName, file);
    
    const response = await fetch(`${this.baseURL}${url}`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      },
      body: formData
    });
    
    const result = await response.json();
    if (result.code !== 200) {
      throw new Error(result.message || '上传失败');
    }
    
    return result.data;
  }
}

// 创建API客户端实例
const api = new ApiClient();

// 导出API方法
export const authApi = {
  register: (data) => api.post('/api/auth/register', data),
  login: (data) => api.post('/api/auth/login', data),
  codeLogin: (data) => api.post('/api/auth/code-login', data),
  sendCode: (data) => api.post('/api/auth/send-code', data),
  logout: () => api.post('/api/auth/logout')
};

export const userApi = {
  getProfile: () => api.get('/api/user/profile'),
  updateProfile: (data) => api.put('/api/user/profile', data),
  getAddresses: () => api.get('/api/user/addresses'),
  getAddress: (id) => api.get(`/api/user/addresses/${id}`),
  addAddress: (data) => api.post('/api/user/addresses', data),
  updateAddress: (id, data) => api.put(`/api/user/addresses/${id}`, data),
  deleteAddress: (id) => api.delete(`/api/user/addresses/${id}`),
  setDefaultAddress: (id) => api.put(`/api/user/addresses/${id}/default`)
};

export const customizeApi = {
  getStyles: () => api.get('/api/customize/styles'),
  getFabrics: () => api.get('/api/customize/fabrics'),
  getThreads: () => api.get('/api/customize/threads'),
  calculatePrice: (data) => api.post('/api/customize/calculate-price', data),
  previewAiResult: (data) => api.post('/api/customize/preview-ai-result', data),
  submitOrder: (data) => api.post('/api/customize/submit-order', data),
  submitTextOrder: (data) => api.post('/api/customize/submit-text-order', data)
};

export const orderApi = {
  getOrders: (status) => api.get('/api/orders', { status }),
  getOrderDetail: (id) => api.get(`/api/orders/${id}`),
  getOrderDetailByNo: (orderNo) => api.get(`/api/orders/no/${orderNo}`),
  cancelOrder: (id) => api.post(`/api/orders/${id}/cancel`)
};

export const fileApi = {
  uploadImage: (file) => api.upload('/api/file/upload/image', file),
  uploadAvatar: (file) => api.upload('/api/file/upload/avatar', file),
  uploadDesign: (file) => api.upload('/api/file/upload/design', file)
};

export const wilcomApi = {
  checkInstallation: () => api.get('/api/wilcom/check'),
  getCurrentDST: (params) => api.get('/api/wilcom/current-dst', params),
  openInWilcom: (data) => api.post('/api/wilcom/open', data),
  checkUpdates: (params) => api.get('/api/wilcom/check-updates', params),
  sync: (data) => api.post('/api/wilcom/sync', data)
};

export const galleryApi = {
  getStyles: (params) => api.get('/api/gallery/styles', params)
};

export const deviceApi = {
  getDevices: () => api.get('/api/devices'),
  getDevice: (id) => api.get(`/api/devices/${id}`),
  controlDevice: (id, data) => api.post(`/api/devices/${id}/control`, data),
  getProgress: (id) => api.get(`/api/devices/${id}/progress`)
};
```

---

## 使用示例

### 在HTML中使用

```html
<script type="module">
  import { authApi, userApi, customizeApi } from './api.js';
  
  // 登录
  async function handleLogin() {
    try {
      const result = await authApi.login({
        phone: '13800138000',
        password: '123456'
      });
      console.log('登录成功:', result);
    } catch (error) {
      console.error('登录失败:', error);
    }
  }
  
  // 获取个人信息
  async function loadProfile() {
    try {
      const profile = await userApi.getProfile();
      console.log('用户信息:', profile);
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  }
  
  // 获取风格列表
  async function loadStyles() {
    try {
      const styles = await customizeApi.getStyles();
      console.log('风格列表:', styles);
    } catch (error) {
      console.error('获取风格列表失败:', error);
    }
  }
</script>
```

---

## 注意事项

1. **Token管理**：Token存储在localStorage中，每次请求自动携带
2. **Token刷新**：Token过期时自动尝试刷新，刷新失败则跳转登录页
3. **错误处理**：统一使用try-catch处理错误，显示友好的错误提示
4. **文件上传**：使用FormData格式，注意文件大小限制
5. **请求拦截**：可以在ApiClient中添加请求/响应拦截器
6. **Loading状态**：建议在请求时显示Loading状态，提升用户体验

---

## 更新日志

- **v1.0.0** (2024-03-20)
  - 初始版本
  - 包含认证、用户、定制、订单、文件管理等基础接口
  - 添加Wilcom集成接口
  - 添加设备管理接口

---

## 参考文档

- [后端API文档](https://s.apifox.cn/ca4267a3-fab6-474a-a3ce-b50950dd2c70)
- [OpenAPI规范](https://swagger.io/specification/)
