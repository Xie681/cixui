# AI生成预览图片说明

## 图片位置

请将您的AI生成结果图片放在此目录下：

**文件路径**: `frontend/public/images/ai-preview/result.jpg`

## 使用方法

1. 将您的生成结果图片命名为 `result.jpg`（或修改代码中的路径）
2. 将图片放在 `frontend/public/images/ai-preview/` 目录下
3. 如果使用其他文件名，请修改 `AIGeneration.vue` 中第317行的 `previewImageUrl` 变量

## 修改图片路径

在 `frontend/src/views/ai/AIGeneration.vue` 文件中：

**第317行**:
```typescript
const previewImageUrl = ref('/images/ai-preview/result.jpg')
```

修改为您想要的图片路径即可，例如：
- `/images/ai-preview/my-result.png`
- `/images/ai-preview/embroidery-preview.jpg`

## 图片要求

- **格式**: JPG、PNG 等常见图片格式
- **建议尺寸**: 800x800px 或更大（保持正方形比例）
- **文件大小**: 建议小于2MB，以优化加载速度

## 注意事项

- 图片路径是相对于 `public` 目录的
- 如果图片加载失败，会在预览区域显示提示信息
- 确保图片文件存在且路径正确
