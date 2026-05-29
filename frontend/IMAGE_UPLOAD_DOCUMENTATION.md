# 图片上传功能文档

本文档详细说明了项目中所有涉及图片上传/插入功能的代码位置，包括输入和输出的地址位置。

## 目录
1. [组件级图片上传](#组件级图片上传)
2. [页面级图片上传](#页面级图片上传)
3. [API服务](#api服务)
4. [文件输出位置](#文件输出位置)

---

## 组件级图片上传

### 1. ImageUpload.vue 组件
**文件位置**: `frontend/src/components/upload/ImageUpload.vue`

#### 输入位置
- **文件输入**: 第11-16行
  ```vue
  <input 
    ref="fileInput"
    type="file" 
    class="hidden" 
    :accept="accept"
    @change="handleFileChange"
  />
  ```
- **拖拽上传**: 第6-8行
  ```vue
  @dragover.prevent="handleDragOver"
  @dragleave.prevent="handleDragLeave"
  @drop.prevent="handleDrop"
  ```

#### 处理逻辑
- **文件选择处理**: 第74-80行
  ```typescript
  function handleFileChange(event: Event) {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
      handleFile(file)
    }
  }
  ```
- **文件处理**: 第82-89行
  ```typescript
  function handleFile(file: File) {
    const reader = new FileReader()
    reader.onload = (e) => {
      previewUrl.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
    emit('upload', file)
  }
  ```
- **拖拽处理**: 第107-113行
  ```typescript
  function handleDrop(event: DragEvent) {
    isDragging.value = false
    const file = event.dataTransfer?.files[0]
    if (file) {
      handleFile(file)
    }
  }
  ```

#### 输出
- **预览URL**: 通过 `previewUrl` ref 存储（第61行）
- **文件对象**: 通过 `upload` 事件发出（第88行）

---

## 页面级图片上传

### 2. AI智能打版流程 - AIGeneration.vue
**文件位置**: `frontend/src/views/ai/AIGeneration.vue`

#### 输入位置
- **文件输入**: 第81行
  ```vue
  <input ref="fileInput" type="file" class="hidden" accept="image/*" @change="handleFileSelect">
  ```
- **拖拽上传**: 第57-60行
  ```vue
  @dragover.prevent="handleDragOver"
  @dragleave.prevent="handleDragLeave"
  @drop.prevent="handleDrop"
  @click="triggerFileInput"
  ```

#### 处理逻辑
- **文件选择处理**: 第355-364行
  ```typescript
  function handleFileSelect(event: Event) {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```
- **拖拽处理**: 第375-384行
  ```typescript
  function handleDrop(event: DragEvent) {
    const file = event.dataTransfer?.files[0]
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```

#### 输出位置
- **预览显示**: 第73-74行
  ```vue
  <div v-if="uploadedImage" class="absolute inset-0">
    <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
  </div>
  ```
- **存储变量**: `uploadedImage` ref（第309行）
- **传递给下单流程**: 第483行
  ```typescript
  imageUrl: uploadedImage.value || '',
  ```

#### DST文件输出
- **DST文件路径**: 通过 `useWilcom` composable 生成
- **临时DST文件**: `/files/temp/dst/${Date.now()}.dst`（见 `useWilcom.ts` 第65行）
- **订单DST文件**: `/files/dst/${orderId}/design.dst`（见 `useWilcom.ts` 第63行）

---

### 3. 奢侈品复刻 - LuxuryReplica.vue
**文件位置**: `frontend/src/views/luxury/LuxuryReplica.vue`

#### 输入位置
- **文件输入**: 第55行
  ```vue
  <input ref="imageUploadRef" type="file" class="hidden" accept="image/*" @change="handleImageUpload" />
  ```
- **拖拽上传**: 第34-38行
  ```vue
  @dragover.prevent="handleDragOver"
  @dragleave.prevent="handleDragLeave"
  @drop.prevent="handleDrop"
  @click="() => imageUploadRef?.click()"
  ```

#### 处理逻辑
- **文件上传处理**: 第258-268行
  ```typescript
  function handleImageUpload(event: Event) {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```
- **拖拽处理**: 第280-289行
  ```typescript
  function handleDrop(event: DragEvent) {
    const file = event.dataTransfer?.files[0]
    if (file && file.type.startsWith('image/')) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```

#### 输出位置
- **预览显示**: 第47-48行
  ```vue
  <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
  ```
- **存储变量**: `uploadedImage` ref（第231行）

---

### 4. 文物复刻 - ArtifactReplica.vue
**文件位置**: `frontend/src/views/luxury/ArtifactReplica.vue`

#### 输入位置
- **文件输入**: 第79行
  ```vue
  <input ref="fileInputRef" type="file" class="hidden" accept="image/*,.obj" @change="handleFileUpload" />
  ```
- **支持格式**: 图片文件（image/*）和3D模型文件（.obj）

#### 处理逻辑
- **文件上传处理**: 第232-241行
  ```typescript
  function handleFileUpload(event: Event) {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```
- **拖拽处理**: 第245-254行
  ```typescript
  function handleDrop(event: DragEvent) {
    const file = event.dataTransfer?.files[0]
    if (file && (file.type.startsWith('image/') || file.name.endsWith('.obj'))) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedImage.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```

#### 输出位置
- **预览显示**: 第72行
  ```vue
  <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
  ```
- **存储变量**: `uploadedImage` ref（第211行）

---

### 5. 传统服装定制 - TraditionalClothing.vue
**文件位置**: `frontend/src/views/luxury/TraditionalClothing.vue`

#### 输入位置
- **图案上传输入**: 第133行
  ```vue
  <input ref="patternUploadRef" type="file" class="hidden" accept="image/*" @change="handlePatternUpload" />
  ```
- **拖拽上传**: 第110-118行

#### 处理逻辑
- **图案上传处理**: 第279-288行
  ```typescript
  function handlePatternUpload(event: Event) {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        uploadedPattern.value = e.target?.result as string
      }
      reader.readAsDataURL(file)
    }
  }
  ```
- **拖拽处理**: 第292-301行

#### 输出位置
- **预览显示**: 第126行
  ```vue
  <img :src="uploadedPattern" class="w-full h-full object-contain rounded-xl" alt="预览">
  ```
- **存储变量**: `uploadedPattern` ref（第244行）

---

## API服务

### 6. 文件上传API - file.ts
**文件位置**: `frontend/src/services/api/file.ts`

#### 图片上传接口
- **上传图片**: 第5-11行
  ```typescript
  uploadImage(file: File): Promise<{ url: string; fileName: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
  ```
  - **API端点**: `/api/file/upload/image`
  - **请求方法**: POST
  - **请求体**: FormData（包含文件）
  - **响应**: `{ url: string, fileName: string }`
  - **输出URL位置**: 响应中的 `url` 字段

#### 头像上传接口
- **上传头像**: 第14-20行
  ```typescript
  uploadAvatar(file: File): Promise<{ url: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
  ```
  - **API端点**: `/api/file/upload/avatar`
  - **输出URL位置**: 响应中的 `url` 字段

#### 设计文件上传接口
- **上传设计文件**: 第23-29行
  ```typescript
  uploadDesign(file: File): Promise<{ url: string; fileName: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/design', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
  ```
  - **API端点**: `/api/file/upload/design`
  - **输出URL位置**: 响应中的 `url` 和 `fileName` 字段

---

## 文件输出位置

### DST文件输出

#### useWilcom.ts Composables
**文件位置**: `frontend/src/composables/useWilcom.ts`

- **临时DST文件路径**: 第65行
  ```typescript
  return `/files/temp/dst/${Date.now()}.dst`
  ```
  - **用途**: 临时生成的DST文件
  - **位置**: `/files/temp/dst/` 目录下

- **订单DST文件路径**: 第63行
  ```typescript
  return `/files/dst/${orderId}/design.dst`
  ```
  - **用途**: 订单关联的DST文件
  - **位置**: `/files/dst/{orderId}/` 目录下

- **下载DST文件**: 第72-76行
  ```typescript
  function downloadDSTFile(filePath: string) {
    const link = document.createElement('a')
    link.download = filePath.split('/').pop() || 'design.dst'
    link.href = filePath
    link.click()
  }
  ```

#### AI Store - ai.ts
**文件位置**: `frontend/src/store/ai.ts`

- **DST文件URL存储**: 第11行
  ```typescript
  const dstFileUrl = ref<string | null>(null)
  ```
- **DST文件URL设置**: 第67行
  ```typescript
  dstFileUrl.value = '/downloads/design_pattern.dst'
  ```
  - **默认输出位置**: `/downloads/design_pattern.dst`

---

## 图片预览位置

### 所有上传组件的预览显示方式

1. **本地预览（Base64）**
   - 使用 `FileReader.readAsDataURL()` 读取文件
   - 将结果存储在 ref 变量中（如 `uploadedImage`, `previewUrl`）
   - 直接在 `<img>` 标签的 `src` 属性中使用

2. **服务器预览（URL）**
   - 通过 `fileApi.uploadImage()` 上传后获得URL
   - 使用返回的 `url` 字段作为图片源

---

## 使用示例

### 在组件中使用图片上传

```vue
<template>
  <ImageUpload
    aspect="square"
    accept="image/*"
    hint="拖拽图片到此处"
    sub-hint="或点击选择文件"
    @upload="handleUpload"
    @remove="handleRemove"
  />
</template>

<script setup lang="ts">
import ImageUpload from '@/components/upload/ImageUpload.vue'
import { fileApi } from '@/services/api'

async function handleUpload(file: File) {
  // 方式1: 本地预览
  const reader = new FileReader()
  reader.onload = (e) => {
    const previewUrl = e.target?.result as string
    // 使用 previewUrl 显示预览
  }
  reader.readAsDataURL(file)
  
  // 方式2: 上传到服务器
  try {
    const result = await fileApi.uploadImage(file)
    const imageUrl = result.url
    // 使用 imageUrl 显示预览或提交订单
  } catch (error) {
    console.error('上传失败:', error)
  }
}

function handleRemove() {
  // 清除预览
}
</script>
```

---

## 总结

### 输入位置汇总
1. **组件级**: `ImageUpload.vue` - 通用图片上传组件
2. **页面级**: 
   - `AIGeneration.vue` - AI智能打版
   - `LuxuryReplica.vue` - 奢侈品复刻
   - `ArtifactReplica.vue` - 文物复刻
   - `TraditionalClothing.vue` - 传统服装定制

### 输出位置汇总
1. **图片URL**: 
   - API响应中的 `url` 字段
   - 本地预览的 Base64 Data URL
2. **DST文件**: 
   - 临时文件: `/files/temp/dst/{timestamp}.dst`
   - 订单文件: `/files/dst/{orderId}/design.dst`
   - 下载文件: `/downloads/design_pattern.dst`

### API端点汇总
- `/api/file/upload/image` - 上传图片
- `/api/file/upload/avatar` - 上传头像
- `/api/file/upload/design` - 上传设计文件

---

**最后更新**: 2024年
**维护者**: 开发团队
