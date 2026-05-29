# 图片插入指南

本文档详细说明了整个前端系统中所有可以插入展示图片的位置，以及如何插入图片。

---

## 📋 目录

1. [首页风格画廊](#1-首页风格画廊)
2. [风格画廊页面](#2-风格画廊页面)
3. [更多风格页面](#3-更多风格页面)
4. [首页Hero预览区域](#4-首页hero预览区域)
5. [AI生成页面](#5-ai生成页面)
6. [定制下单流程页面](#6-定制下单流程页面)
7. [三方协同微调页面](#7-三方协同微调页面)
8. [高端定制页面](#8-高端定制页面)
9. [DST文件预览路径](#9-dst文件预览路径)

---

## 1. 首页风格画廊

### 位置
**文件**: `frontend/src/views/Home.vue`

### 图片展示位置

#### 1.1 四大名绣流派卡片（第377-445行）

**代码位置**: 第377-445行

**当前状态**: 使用SVG图标占位符

**插入方式**:
```vue
<!-- 苏绣卡片 -->
<div class="aspect-square bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20 rounded-lg mb-4 relative overflow-hidden">
  <!-- 替换这里的SVG为图片 -->
  <img src="/images/gallery/suxiu.jpg" class="w-full h-full object-cover" alt="苏绣">
  <div class="absolute top-2 right-2 px-2 py-1 bg-accent-primary/80 rounded text-xs text-white font-semibold">双面绣</div>
</div>
```

**图片路径建议**: `frontend/public/images/gallery/suxiu.jpg`

**需要修改的卡片**:
- 苏绣 (第377行)
- 湘绣 (第395行)
- 粤绣 (第413行)
- 蜀绣 (第431行)

#### 1.2 风格画廊网格（第456-478行）

**代码位置**: 第456-478行

**当前状态**: 使用SVG图标占位符，通过 `galleryItems` 数组数据控制

**插入方式**:
修改 `galleryItems` 数组（第1164-1175行），为每个项目添加 `imageUrl` 属性：

```typescript
const galleryItems = [
  { 
    id: 1, 
    name: '传统花卉', 
    description: '经典中式刺绣风格', 
    price: 299,
    imageUrl: '/images/gallery/traditional-flower.jpg', // 添加这行
    gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20',
    iconColor: 'text-accent-primary/30',
    iconPath: '...'
  },
  // ... 其他项目
]
```

然后在模板中修改（第456-461行）：
```vue
<div :class="['aspect-square relative overflow-hidden', item.gradient]">
  <!-- 如果有图片URL，显示图片；否则显示SVG图标 -->
  <img 
    v-if="item.imageUrl" 
    :src="item.imageUrl" 
    class="w-full h-full object-cover" 
    :alt="item.name"
  >
  <div v-else class="absolute inset-0 flex items-center justify-center">
    <svg class="w-24 h-24" :class="item.iconColor" fill="currentColor" viewBox="0 0 24 24">
      <path :d="item.iconPath"/>
    </svg>
  </div>
  <!-- ... 其他内容 -->
</div>
```

**图片路径建议**: `frontend/public/images/gallery/{item-name}.jpg`

---

## 2. 风格画廊页面

### 位置
**文件**: `frontend/src/views/Gallery.vue`

### 图片展示位置

#### 2.1 四大名绣流派卡片（第163-232行）

**代码位置**: 第163-232行

**插入方式**: 与首页相同，替换SVG为图片

**需要修改的卡片**:
- 苏绣 (第164行)
- 湘绣 (第182行)
- 粤绣 (第200行)
- 蜀绣 (第218行)

#### 2.2 风格画廊网格（第243-265行）

**代码位置**: 第243-265行

**插入方式**: 与首页相同，需要修改 `galleryItems` 数组并更新模板

---

## 3. 更多风格页面

### 位置
**文件**: `frontend/src/views/GalleryMore.vue`

### 图片展示位置

#### 3.1 风格画廊网格（第165-187行）

**代码位置**: 第165-187行

**当前状态**: 使用SVG图标占位符

**插入方式**: 
修改 `allGalleryItems` 数组（第221-238行），添加 `imageUrl` 属性，然后更新模板（第165-170行）：

```vue
<div :class="['aspect-square relative overflow-hidden', item.gradient]">
  <img 
    v-if="item.imageUrl" 
    :src="item.imageUrl" 
    class="w-full h-full object-cover" 
    :alt="item.name"
  >
  <div v-else class="absolute inset-0 flex items-center justify-center">
    <svg class="w-24 h-24" :class="item.iconColor" fill="currentColor" viewBox="0 0 24 24">
      <path :d="item.iconPath"/>
    </svg>
  </div>
  <!-- ... 其他内容 -->
</div>
```

**图片路径建议**: `frontend/public/images/gallery/{item-name}.jpg`

---

## 4. 首页Hero预览区域

### 位置
**文件**: `frontend/src/views/Home.vue`

### 图片展示位置

#### 4.1 Hero预览图片（第93-123行）

**代码位置**: 第93-123行

**当前状态**: 使用装饰性网格背景和SVG图标

**插入方式**:
在第93行的 `aspect-video` div 中添加图片：

```vue
<div class="aspect-video bg-gradient-to-br from-accent-primary/20 via-accent-cta/20 to-accent-vibrant/20 rounded-xl flex items-center justify-center relative overflow-hidden">
  <!-- 添加预览图片 -->
  <img src="/images/hero/preview.jpg" class="w-full h-full object-cover rounded-xl" alt="AI刺绣预览">
  
  <!-- 保留装饰性元素（可选） -->
  <div class="absolute inset-0" style="background-image: linear-gradient(rgba(139,92,246,0.1) 1px, transparent 1px), linear-gradient(90deg, rgba(139,92,246,0.1) 1px, transparent 1px); background-size: 50px 50px;"></div>
  
  <!-- 或者移除装饰性元素，只显示图片 -->
</div>
```

**图片路径建议**: `frontend/public/images/hero/preview.jpg`

---

## 5. AI生成页面

### 位置
**文件**: `frontend/src/views/ai/AIGeneration.vue`

### 图片展示位置

#### 5.1 图片上传区域（第54-82行）

**代码位置**: 第54-82行

**当前状态**: 支持拖拽上传和点击选择

**功能**: 已实现图片上传功能，用户上传的图片会显示在这里

**预览路径**: 上传后通过 `uploadedImage` ref 存储（Base64或URL）

#### 5.2 实时预览区域（第253-265行）

**代码位置**: 第253-265行

**当前状态**: 显示"AI 正在处理中..."或"DST文件已生成"文字

**插入方式**: 
修改第262-264行，添加DST文件预览图片：

```vue
<div v-else class="w-full h-full flex items-center justify-center relative">
  <!-- 添加DST文件预览图片 -->
  <img 
    v-if="dstPreviewImage" 
    :src="dstPreviewImage" 
    class="w-full h-full object-contain rounded-xl" 
    alt="DST预览"
  >
  <p v-else class="text-white font-medium">DST文件已生成</p>
</div>
```

**图片路径建议**: 
- 默认预览图片: `frontend/public/images/dst/preview-default.jpg`
- 动态DST预览: 从后端API获取或使用 `dstFileUrl`（见第9节）

**代码修改示例**:
```typescript
// 在 script 部分添加
const dstPreviewImage = ref<string | null>(null)

// 当DST文件生成后，设置预览图片
watch(dstReady, (ready) => {
  if (ready) {
    // 方式1: 使用默认预览图片
    dstPreviewImage.value = '/images/dst/preview-default.jpg'
    
    // 方式2: 从后端获取DST预览图
    // dstPreviewImage.value = await getDSTPreview(dstFileUrl.value)
  }
})
```

---

## 6. 定制下单流程页面

### 位置
**文件**: `frontend/src/views/CustomOrderFlow.vue`

### 图片展示位置

#### 6.1 步骤1 - 风格选择卡片（第174行）

**代码位置**: 第174行

**当前状态**: 使用SVG图标占位符

**插入方式**:
修改 `availableStyles` 数组，添加 `imageUrl` 属性，然后更新模板：

```vue
<div class="aspect-square bg-gradient-to-br from-accent-primary/20 to-accent-cta/20 rounded-lg mb-2 flex items-center justify-center">
  <img 
    v-if="style.imageUrl" 
    :src="style.imageUrl" 
    class="w-full h-full object-cover rounded-lg" 
    :alt="style.name"
  >
  <svg v-else class="w-12 h-12 text-accent-primary/40" fill="currentColor" viewBox="0 0 24 24">
    <!-- SVG图标 -->
  </svg>
</div>
```

**图片路径建议**: `frontend/public/images/styles/{style-name}.jpg`

#### 6.2 步骤2 - 实时预览（第326-328行）

**代码位置**: 第326-328行

**当前状态**: 使用Canvas绘制预览

**插入方式**: 
如果需要显示静态预览图片，可以在Canvas下方添加：

```vue
<div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden mb-4">
  <!-- Canvas预览 -->
  <canvas ref="previewCanvas" class="w-full h-full"></canvas>
  
  <!-- 或者使用静态图片 -->
  <img 
    v-if="previewImage" 
    :src="previewImage" 
    class="w-full h-full object-contain" 
    alt="定制预览"
  >
</div>
```

**图片路径建议**: `frontend/public/images/custom/preview.jpg`

---

## 7. 三方协同微调页面

### 位置
**文件**: `frontend/src/views/ai/Collaboration.vue`

### 图片展示位置

#### 7.1 实时预览区域（第176行）

**代码位置**: 第176行

**当前状态**: 使用渐变背景占位符

**插入方式**:
```vue
<div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden">
  <img 
    v-if="previewImage" 
    :src="previewImage" 
    class="w-full h-full object-contain rounded-xl" 
    alt="协同预览"
  >
  <div v-else class="absolute inset-0 flex items-center justify-center">
    <p class="text-gray-400">等待预览...</p>
  </div>
</div>
```

**图片路径建议**: `frontend/public/images/collaboration/preview.jpg`

---

## 8. 高端定制页面

### 位置
**文件**: 
- `frontend/src/views/luxury/LuxuryReplica.vue`
- `frontend/src/views/luxury/ArtifactReplica.vue`
- `frontend/src/views/luxury/DragonRobe.vue`
- `frontend/src/views/luxury/TraditionalClothing.vue`

### 图片展示位置

#### 8.1 图片上传区域

**代码位置**: 各文件中的上传区域（约第40-56行）

**当前状态**: 已实现图片上传功能

**功能**: 用户上传的图片会显示在这里

**预览路径**: 通过 `uploadedImage` ref 存储

#### 8.2 示例图片展示

**插入方式**: 在上传区域下方添加示例图片展示：

```vue
<!-- 示例图片展示 -->
<div class="mt-6">
  <h4 class="text-lg font-semibold mb-4 text-white">示例作品</h4>
  <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
    <img 
      v-for="(example, index) in exampleImages" 
      :key="index"
      :src="example" 
      class="w-full aspect-square object-cover rounded-lg cursor-pointer hover:scale-105 transition-transform"
      @click="selectExample(example)"
      alt="示例作品"
    >
  </div>
</div>
```

**图片路径建议**: `frontend/public/images/luxury/{page-name}/examples/{index}.jpg`

---

## 9. DST文件预览路径

### DST文件存储位置

#### 9.1 临时DST文件

**文件**: `frontend/src/composables/useWilcom.ts`

**代码位置**: 第65行

**路径**: `/files/temp/dst/${Date.now()}.dst`

**说明**: AI生成过程中临时存储的DST文件

**实际存储位置**: 
- 开发环境: `frontend/public/files/temp/dst/`
- 生产环境: 后端服务器 `/files/temp/dst/` 目录

#### 9.2 订单DST文件

**文件**: `frontend/src/composables/useWilcom.ts`

**代码位置**: 第63行

**路径**: `/files/dst/${orderId}/design.dst`

**说明**: 订单关联的最终DST文件

**实际存储位置**: 
- 开发环境: `frontend/public/files/dst/{orderId}/`
- 生产环境: 后端服务器 `/files/dst/{orderId}/` 目录

#### 9.3 DST预览图片路径

**文件**: `frontend/src/views/ai/AIGeneration.vue`

**代码位置**: 第253-265行（实时预览区域）

**当前状态**: 仅显示文字提示

**插入方式**:

1. **使用默认预览图片**:
```typescript
// 在 script 部分
const dstPreviewImage = ref<string | null>('/images/dst/preview-default.jpg')

// 在模板中
<img 
  v-if="dstReady && dstPreviewImage" 
  :src="dstPreviewImage" 
  class="w-full h-full object-contain rounded-xl" 
  alt="DST预览"
>
```

2. **从后端API获取预览图**:
```typescript
// 当DST文件生成后
watch(dstReady, async (ready) => {
  if (ready && dstFileUrl.value) {
    try {
      // 调用后端API获取DST预览图
      const response = await fetch(`/api/dst/preview?file=${encodeURIComponent(dstFileUrl.value)}`)
      const blob = await response.blob()
      dstPreviewImage.value = URL.createObjectURL(blob)
    } catch (error) {
      console.error('获取DST预览失败:', error)
      // 使用默认图片
      dstPreviewImage.value = '/images/dst/preview-default.jpg'
    }
  }
})
```

3. **预先放置预览图片**:
   - 将预览图片放在: `frontend/public/images/dst/preview-default.jpg`
   - 或使用订单ID: `frontend/public/images/dst/{orderId}/preview.jpg`

**推荐方案**: 
在 `frontend/public/images/dst/` 目录下放置一个默认的DST预览图片，当DST文件生成后显示该图片。

**文件路径**: `frontend/public/images/dst/preview-default.jpg`

---

## 📝 图片插入步骤总结

### 快速插入步骤

1. **准备图片文件**
   - 将图片文件放在 `frontend/public/images/` 对应目录下
   - 建议使用 JPG 或 PNG 格式
   - 建议尺寸: 风格卡片 800x800px，预览图 1200x1200px

2. **修改代码**
   - 找到对应的代码位置（参考上面的位置说明）
   - 添加 `imageUrl` 属性到数据数组（如 `galleryItems`）
   - 在模板中添加 `<img>` 标签替换SVG图标

3. **测试显示**
   - 启动开发服务器: `npm run dev`
   - 检查图片是否正确显示
   - 检查响应式布局是否正常

### 图片命名规范

建议使用以下命名规范：

- 风格画廊: `{style-name}.jpg` (如: `suxiu.jpg`, `traditional-flower.jpg`)
- Hero预览: `preview.jpg`
- DST预览: `preview-default.jpg` 或 `{orderId}/preview.jpg`
- 高端定制示例: `{page-name}/examples/{index}.jpg`

### 图片优化建议

1. **压缩图片**: 使用工具压缩图片以减少加载时间
2. **WebP格式**: 考虑使用WebP格式以获得更好的压缩比
3. **懒加载**: 对于大量图片，考虑使用懒加载
4. **CDN**: 生产环境建议使用CDN存储图片

---

## 🔧 技术说明

### 图片路径类型

1. **静态资源路径**: `/images/xxx.jpg` (放在 `public/images/` 目录)
2. **动态URL**: 从后端API获取的图片URL
3. **Base64**: 用户上传后本地预览使用的Base64编码

### 图片显示方式

1. **object-cover**: 填充整个容器，可能裁剪图片
2. **object-contain**: 完整显示图片，可能留白
3. **object-fill**: 拉伸填充（不推荐）

### 响应式处理

所有图片容器都使用了 `aspect-square` 或 `aspect-video` 类，确保在不同屏幕尺寸下保持正确的宽高比。

---

## 📞 需要帮助？

如果在插入图片过程中遇到问题，请检查：

1. 图片路径是否正确
2. 图片文件是否存在
3. 浏览器控制台是否有错误信息
4. 图片格式是否支持（建议JPG/PNG）

---

**最后更新**: 2024年
**文档版本**: 1.0
