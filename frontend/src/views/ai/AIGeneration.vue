<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <div class="min-h-screen relative">
        <!-- 背景效果 -->
        <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
        <Particles container-id="particles-ai-page" :count="25" />
        
        <!-- 顶部导航栏 -->
        <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
              <div class="flex items-center space-x-4">
                <button @click="$router.back()" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                </button>
                <h2 class="text-xl font-heading font-bold text-white">AI 智能打版流程</h2>
              </div>
              <div class="flex items-center space-x-4">
                <span class="text-sm text-gray-400">步骤 <span>{{ currentStep }}</span> / 5</span>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <!-- 进度条 -->
          <div class="mb-8">
            <div class="flex items-center justify-between mb-4">
              <div class="flex-1 h-2 bg-white/10 rounded-full overflow-hidden">
                <div class="h-full bg-gradient-to-r from-accent-primary to-accent-cta transition-all duration-500" :style="{ width: progressPercentage + '%' }"></div>
              </div>
            </div>
            <div class="flex justify-between text-sm text-gray-400">
              <span>上传图稿</span>
              <span>语义分割</span>
              <span>针法匹配</span>
              <span>路径规划</span>
              <span>DST生成</span>
            </div>
          </div>
          
          <!-- 步骤1: 上传图稿 -->
          <div v-if="currentStep === 1" class="step-content">
            <div class="text-center mb-8">
              <h3 class="text-3xl font-heading font-bold mb-4 gradient-text">上传图稿或输入定制文字</h3>
              <p class="text-gray-300">支持图片上传或文字输入，AI将自动识别并转换为刺绣图案</p>
            </div>
            
            <div class="grid md:grid-cols-2 gap-8">
              <!-- 上传区域 -->
              <div class="glow-card rounded-2xl p-8">
                <div 
                  class="aspect-square border-2 border-dashed border-accent-primary/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-primary/60 transition-all duration-300"
                  @dragover.prevent="handleDragOver"
                  @dragleave.prevent="handleDragLeave"
                  @drop.prevent="handleDrop"
                  @click="triggerFileInput"
                >
                  <!-- 上传提示 -->
                  <div v-if="!uploadedImage" class="absolute inset-0 flex flex-col items-center justify-center z-10">
                    <svg class="w-16 h-16 mb-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                    </svg>
                    <p class="text-gray-300 mb-2">拖拽图片到此处</p>
                    <p class="text-sm text-gray-500">或点击选择文件</p>
                    <p class="text-xs text-gray-500 mt-2">支持 JPG、PNG 格式，最大 10MB</p>
                  </div>
                  <!-- 图片预览 -->
                  <div v-if="uploadedImage" class="absolute inset-0">
                    <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
                    <button @click="removePreview" class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300 cursor-pointer">
                      <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                      </svg>
                    </button>
                  </div>
                  <input ref="fileInput" type="file" class="hidden" accept="image/*" @change="handleFileSelect">
                </div>
              </div>
              
              <!-- 文字输入区域 -->
              <div class="glow-card rounded-2xl p-8">
                <h4 class="text-xl font-heading font-semibold mb-4 text-white">或输入定制文字</h4>
                <textarea 
                  v-model="textInput" 
                  rows="4" 
                  class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent resize-none mb-3" 
                  placeholder="输入您想要刺绣的文字内容..."
                ></textarea>
                
                <!-- AI智能提示词生成 -->
                <div class="mb-4 p-3 glow-card rounded-lg border border-accent-primary/30">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-sm font-semibold text-white flex items-center space-x-2">
                      <svg class="w-4 h-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                      </svg>
                      <span>AI 智能提示词生成</span>
                    </span>
                    <button @click="generateTextPrompt" class="text-xs px-3 py-1 neon-button text-white rounded-lg transition-all duration-300 cursor-pointer">
                      生成
                    </button>
                  </div>
                  <div v-if="textPromptResult" class="mt-2 p-2 bg-black/30 rounded text-xs text-gray-300">{{ textPromptResult }}</div>
                </div>
                
                <div class="mt-4 space-y-3">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">选择字体风格</label>
                    <select v-model="fontStyle" class="w-full glow-card rounded-lg px-4 py-2 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>传统书法</option>
                      <option>现代简约</option>
                      <option>艺术字体</option>
                      <option>英文花体</option>
                    </select>
                  </div>
                  
                  <!-- 四大名绣风格选择 -->
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">选择刺绣流派风格</label>
                    <div class="grid grid-cols-2 gap-2">
                      <button 
                        v-for="style in embroideryStyles" 
                        :key="style.id"
                        @click="selectEmbroideryStyle(style.id)"
                        :class="[
                          'px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left',
                          selectedEmbroideryStyle === style.id ? 'border-2 border-accent-primary' : ''
                        ]"
                      >
                        <div class="font-medium text-white">{{ style.name }}</div>
                        <div class="text-xs text-gray-400">{{ style.location }}</div>
                      </button>
                    </div>
                  </div>
                  
                  <!-- 智能颜色选择 -->
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">智能颜色选择</label>
                    <div class="flex items-center space-x-2 mb-2">
                      <button @click="openColorPicker" class="flex-1 px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
                        <span class="flex items-center space-x-2">
                          <svg class="w-4 h-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
                          </svg>
                          <span>打开色卡库</span>
                        </span>
                      </button>
                      <button @click="generateColorScheme" class="px-3 py-2 neon-button text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">
                        <span class="flex items-center space-x-1">
                          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                          </svg>
                          <span>AI配色</span>
                        </span>
                      </button>
                    </div>
                    <div class="flex flex-wrap gap-2">
                      <div 
                        v-for="color in colorPalette" 
                        :key="color"
                        :class="[
                          'w-10 h-10 rounded-lg cursor-pointer border-2 transition-all',
                          selectedColors.includes(color) ? 'border-white scale-110' : 'border-transparent hover:border-white'
                        ]"
                        :style="{ backgroundColor: color }"
                        @click="selectColor(color)"
                      ></div>
                    </div>
                    <div v-if="aiColorSuggestions.length > 0" class="hidden mt-2 p-2 bg-accent-primary/10 rounded text-xs text-gray-300">
                      <p class="font-semibold mb-1">AI推荐配色方案：</p>
                      <div class="flex flex-wrap gap-2 mt-2">
                        <div 
                          v-for="color in aiColorSuggestions" 
                          :key="color"
                          class="w-8 h-8 rounded-lg"
                          :style="{ backgroundColor: color }"
                          @click="selectColor(color)"
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="mt-8 text-center">
              <button @click="startAIGeneration" :disabled="!canStartGeneration" class="neon-button px-8 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed">
                开始 AI 智能打版
              </button>
            </div>
          </div>
          
          <!-- 步骤2-5: AI处理流程 -->
          <div v-if="currentStep > 1" class="step-content">
            <div class="grid md:grid-cols-2 gap-8">
              <!-- 左侧：处理进度 -->
              <div class="glow-card rounded-2xl p-8">
                <h3 class="text-2xl font-heading font-semibold mb-6 text-white">AI 处理进度</h3>
                
                <!-- 语义分割 -->
                <div class="mb-6">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-gray-300">语义分割</span>
                    <span class="text-sm text-gray-400">{{ processStatus.semantic }}</span>
                  </div>
                  <div class="w-full bg-white/10 rounded-full h-2 overflow-hidden">
                    <div class="h-full bg-accent-primary transition-all duration-500" :style="{ width: processProgress.semantic + '%' }"></div>
                  </div>
                </div>
                
                <!-- 针法匹配 -->
                <div class="mb-6">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-gray-300">针法匹配</span>
                    <span class="text-sm text-gray-400">{{ processStatus.stitch }}</span>
                  </div>
                  <div class="w-full bg-white/10 rounded-full h-2 overflow-hidden">
                    <div class="h-full bg-accent-cta transition-all duration-500" :style="{ width: processProgress.stitch + '%' }"></div>
                  </div>
                </div>
                
                <!-- 路径规划 -->
                <div class="mb-6">
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-gray-300">路径规划</span>
                    <span class="text-sm text-gray-400">{{ processStatus.path }}</span>
                  </div>
                  <div class="w-full bg-white/10 rounded-full h-2 overflow-hidden">
                    <div class="h-full bg-accent-vibrant transition-all duration-500" :style="{ width: processProgress.path + '%' }"></div>
                  </div>
                </div>
                
                <!-- DST文件生成 -->
                <div>
                  <div class="flex items-center justify-between mb-2">
                    <span class="text-gray-300">DST文件生成</span>
                    <span class="text-sm text-gray-400">{{ processStatus.dst }}</span>
                  </div>
                  <div class="w-full bg-white/10 rounded-full h-2 overflow-hidden">
                    <div class="h-full bg-accent-secondary transition-all duration-500" :style="{ width: processProgress.dst + '%' }"></div>
                  </div>
                </div>
              </div>
              
              <!-- 右侧：实时预览 -->
              <div class="glow-card rounded-2xl p-8">
                <h3 class="text-2xl font-heading font-semibold mb-6 text-white">实时预览</h3>
                <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl flex items-center justify-center relative overflow-hidden">
                  <div v-if="!previewReady" class="text-center w-full h-full flex flex-col items-center justify-center">
                    <div class="w-24 h-24 mx-auto mb-4 border-4 border-dashed border-accent-primary/30 rounded-lg flex items-center justify-center">
                      <svg class="w-12 h-12 text-accent-primary animate-pulse" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                    </div>
                    <p class="text-gray-400">AI 正在处理中...</p>
                  </div>
                  <div v-else class="w-full h-full relative">
                    <!-- 预览图片 -->
                    <img 
                      :src="previewImageUrl" 
                      class="w-full h-full object-contain rounded-xl" 
                      alt="AI生成预览"
                      @load="imageLoaded = true"
                      @error="handlePreviewImageError"
                    >
                    <!-- 图片加载失败时的提示 -->
                    <div v-if="!imageLoaded" class="absolute inset-0 flex items-center justify-center bg-black/50 rounded-xl">
                      <div class="text-center">
                        <p class="text-white font-medium mb-2">DST文件已生成</p>
                        <p class="text-xs text-gray-400">预览图片加载失败，请检查图片路径</p>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- DST文件操作按钮组 -->
                <div v-if="dstReady" class="mt-6 space-y-3">
                  <div class="text-center mb-4">
                    <p class="text-sm text-gray-400 mb-2">DST文件已生成，请选择下一步操作：</p>
                  </div>
                  <div class="flex flex-col sm:flex-row gap-3 justify-center">
                    <button @click="openInWilcom" class="px-6 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer flex items-center justify-center space-x-2 border-2 border-accent-cta/50 hover:border-accent-cta">
                      <svg class="w-5 h-5 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                      </svg>
                      <span>在Wilcom中打开</span>
                    </button>
                    <button @click="enterCollaborationMode" class="neon-button px-6 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                      进入协同微调
                    </button>
                  </div>
                  <div class="text-center mt-4">
                    <p class="text-xs text-gray-500">
                      💡 提示：可在Wilcom中精修后，再进入协同微调进行三方协作
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'
import { useWilcom } from '@/composables/useWilcom'
import { fileApi } from '@/services/api/file'
import { chatbotApi } from '@/services/api/chatbot'
import { customizeApi } from '@/services/api/customize'

const router = useRouter()

const currentStep = ref(1)
const progressPercentage = computed(() => currentStep.value * 20)
const uploadedImage = ref<string | null>(null)
const uploadedImageUrl = ref<string | null>(null) // 后端返回的图片URL
const isUploading = ref(false)
const textInput = ref('')
const fontStyle = ref('传统书法')
const selectedEmbroideryStyle = ref<string | null>(null)
const selectedColors = ref<string[]>([])
const textPromptResult = ref('')
const isGenerating = ref(false) // 添加生成状态
const aiColorSuggestions = ref<string[]>([])
const previewReady = ref(false)
const dstReady = ref(false)
// 预览图片路径 - 将您的生成结果图片放在 frontend/public/images/ai-preview/ 目录下
const previewImageUrl = ref('/images/ai-preview/result.jpg')
const imageLoaded = ref(true)

const fileInput = ref<HTMLInputElement | null>(null)

const embroideryStyles = [
  { id: 'suxiu', name: '苏绣', location: '江苏苏州' },
  { id: 'xiangxiu', name: '湘绣', location: '湖南长沙' },
  { id: 'yuexiu', name: '粤绣', location: '广东地区' },
  { id: 'shuxiu', name: '蜀绣', location: '四川成都' }
]

const colorPalette = [
  '#EF4444', '#3B82F6', '#10B981', '#8B5CF6',
  '#F59E0B', '#EC4899', '#06B6D4', '#F97316'
]

const processProgress = ref({
  semantic: 0,
  stitch: 0,
  path: 0,
  dst: 0
})

const processStatus = ref({
  semantic: '等待中...',
  stitch: '等待中...',
  path: '等待中...',
  dst: '等待中...'
})

const canStartGeneration = computed(() => {
  return uploadedImage.value !== null || textInput.value.trim() !== ''
})

function triggerFileInput() {
  fileInput.value?.click()
}

function handleFileSelect(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    uploadImageToServer(file)
  }
}

function handleDragOver(event: DragEvent) {
  event.preventDefault()
}

function handleDragLeave(event: DragEvent) {
  event.preventDefault()
}

function handleDrop(event: DragEvent) {
  event.preventDefault()
  const file = event.dataTransfer?.files[0]
  if (file && file.type.startsWith('image/')) {
    uploadImageToServer(file)
  }
}

async function uploadImageToServer(file: File) {
  isUploading.value = true
  try {
    // 先显示本地预览
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedImage.value = e.target?.result as string
    }
    reader.readAsDataURL(file)
    
    // 上传到服务器
    const response = await fileApi.uploadImage(file)
    uploadedImageUrl.value = response.url
    console.log('图片上传成功:', response.url)
  } catch (error: any) {
    console.error('图片上传失败:', error)
    
    // 检查是否是网络错误（可能由浏览器扩展引起）
    const errorMessage = error?.message || ''
    if (errorMessage.includes('fetch') || errorMessage.includes('network')) {
      alert('图片上传失败。\n\n可能原因：\n1. 网络连接问题\n2. 浏览器扩展干扰（如广告拦截器）\n\n建议：\n- 尝试禁用浏览器扩展\n- 或使用无痕模式（Ctrl+Shift+N）')
    } else {
      alert('图片上传失败，请重试')
    }
    
    uploadedImage.value = null
  } finally {
    isUploading.value = false
  }
}

function removePreview() {
  uploadedImage.value = null
  uploadedImageUrl.value = null
  if (fileInput.value) {
    fileInput.value.value = ''
  }
}

function handlePreviewImageError() {
  imageLoaded.value = false
  console.warn('预览图片加载失败，请检查图片路径:', previewImageUrl.value)
}

function generateTextPrompt() {
  if (!textInput.value.trim()) {
    alert('请先输入文字内容')
    return
  }
  
  isGenerating.value = true
  textPromptResult.value = ''
  
  // 构建提示词请求
  const styleInfo = selectedEmbroideryStyle.value 
    ? embroideryStyles.find(s => s.id === selectedEmbroideryStyle.value)?.name 
    : '通用'
  
  const colorInfo = selectedColors.value.length > 0 
    ? `使用颜色：${selectedColors.value.join('、')}` 
    : '自动配色'
  
  const prompt = `请为刺绣设计生成专业的AI绘图提示词。
用户需求：
- 文字内容：${textInput.value}
- 字体风格：${fontStyle.value}
- 刺绣风格：${styleInfo}
- ${colorInfo}

请生成一个详细的AI绘图提示词，要求：
1. 描述刺绣图案的整体布局和构图
2. 说明文字的字体风格和排列方式
3. 描述刺绣的针法、线条和纹理特点
4. 说明色彩搭配和视觉效果
5. 强调刺绣工艺的细节和质感
6. 适合用于AI图像生成工具（如Stable Diffusion、Midjourney等）

请直接输出提示词内容，不需要额外说明。`

  // 调用后端API
  chatbotApi.chat({
    message: prompt,
    stream: false
  })
    .then(response => {
      textPromptResult.value = response.reply
      isGenerating.value = false
    })
    .catch(error => {
      console.error('生成提示词失败:', error)
      alert('生成失败，请稍后重试')
      isGenerating.value = false
    })
}

function selectEmbroideryStyle(styleId: string) {
  selectedEmbroideryStyle.value = selectedEmbroideryStyle.value === styleId ? null : styleId
}

function selectColor(color: string) {
  const index = selectedColors.value.indexOf(color)
  if (index > -1) {
    selectedColors.value.splice(index, 1)
  } else {
    selectedColors.value.push(color)
  }
}

function openColorPicker() {
  // TODO: 打开色卡库
  alert('色卡库功能开发中...')
}

function generateColorScheme() {
  // TODO: 调用AI生成配色方案
  aiColorSuggestions.value = ['#DC2626', '#F59E0B', '#10B981', '#3B82F6']
}

async function startAIGeneration() {
  if (!canStartGeneration.value) return
  
  currentStep.value = 2
  
  try {
    // 重置状态
    processStatus.value.semantic = '准备中...'
    processStatus.value.stitch = '等待中...'
    processStatus.value.path = '等待中...'
    processStatus.value.dst = '等待中...'
    
    processProgress.value.semantic = 0
    processProgress.value.stitch = 0
    processProgress.value.path = 0
    processProgress.value.dst = 0
    
    // 构建AI预览请求参数
    const aiPreviewData: any = {
      embroideryStyle: selectedEmbroideryStyle.value || 'SU'
    }
    
    // 判断是图片定制还是文字定制
    if (uploadedImageUrl.value) {
      // 图片定制
      aiPreviewData.imageUrl = uploadedImageUrl.value
      console.log('🖼️ 开始图片转刺绣AI处理:', aiPreviewData)
    } else if (textInput.value.trim()) {
      // 文字定制
      aiPreviewData.text = textInput.value.trim()
      aiPreviewData.fontStyle = fontStyle.value
      aiPreviewData.fontSize = 48
      aiPreviewData.color = selectedColors.value.length > 0 ? selectedColors.value[0] : 'black'
      console.log('✏️ 开始文字转刺绣AI处理:', aiPreviewData)
    } else {
      throw new Error('请先上传图片或输入文字内容')
    }
    
    // 第1步：语义分割（模拟）
    processStatus.value.semantic = '处理中...'
    processProgress.value.semantic = 30
    await new Promise(resolve => setTimeout(resolve, 800))
    
    processProgress.value.semantic = 70
    await new Promise(resolve => setTimeout(resolve, 500))
    
    processProgress.value.semantic = 100
    processStatus.value.semantic = '完成'
    
    // 第2步：针法匹配
    processStatus.value.stitch = '处理中...'
    processProgress.value.stitch = 40
    await new Promise(resolve => setTimeout(resolve, 600))
    
    processProgress.value.stitch = 100
    processStatus.value.stitch = '完成'
    
    // 第3步：路径规划
    processStatus.value.path = '处理中...'
    processProgress.value.path = 50
    await new Promise(resolve => setTimeout(resolve, 700))
    
    processProgress.value.path = 100
    processStatus.value.path = '完成'
    
    // 第4步：调用真实的AI服务生成刺绣图案
    processStatus.value.dst = 'AI生成中...'
    processProgress.value.dst = 20
    
    console.log('🤖 调用AI预览服务...')
    const aiResult = await customizeApi.previewAiResult(aiPreviewData)
    
    processProgress.value.dst = 80
    console.log('✅ AI处理完成:', aiResult)
    
    // 更新预览结果
    previewImageUrl.value = aiResult.resultImageUrl
    
    // 保存AI结果供后续订单使用
    window.aiGenerationResult = {
      resultImageUrl: aiResult.resultImageUrl,
      stitchCount: aiResult.stitchCount,
      colorCount: aiResult.colorCount,
      originalImageUrl: uploadedImageUrl.value,
      textContent: textInput.value.trim(),
      embroideryStyle: selectedEmbroideryStyle.value,
      fontStyle: fontStyle.value,
      selectedColors: selectedColors.value
    }
    
    processProgress.value.dst = 100
    processStatus.value.dst = '完成'
    
    // 完成所有步骤
    currentStep.value = 5
    previewReady.value = true
    dstReady.value = true
    imageLoaded.value = true
    
    console.log('🎉 AI智能打版完成！')
    
  } catch (error: any) {
    console.error('❌ AI处理失败:', error)
    
    // 错误处理
    processStatus.value.dst = '失败'
    processStatus.value.semantic = '失败'
    processStatus.value.stitch = '失败'
    processStatus.value.path = '失败'
    
    const errorMessage = error?.message || error?.error || '未知错误'
    alert(`AI处理失败：${errorMessage}\n\n可能原因：\n1. AI服务未启动\n2. 网络连接问题\n3. 图片格式不支持\n\n请检查后重试`)
  }
}

const { openInWilcom } = useWilcom()

function enterCollaborationMode() {
  console.log('=== 准备进入协同微调模式 ===')
  
  // 检查是否完成了AI生成
  const aiResult = (window as any).aiGenerationResult
  if (!aiResult || !previewImageUrl.value) {
    alert('请先完成AI智能打版流程！')
    return
  }
  
  console.log('✅ AI生成结果:', aiResult)
  console.log('✅ 预览图片URL:', previewImageUrl.value)
  
  // 将AI生成的图片URL保存到localStorage，供协同页面使用
  localStorage.setItem('ai_generated_image_url', previewImageUrl.value)
  
  // 跳转到协同微调页面，携带图片URL
  router.push({
    path: '/collaboration',
    query: {
      imageUrl: previewImageUrl.value,
      source: 'ai',
      stitchCount: aiResult.stitchCount?.toString() || '',
      colorCount: aiResult.colorCount?.toString() || ''
    }
  })
}

function goToPatternEditor() {
  router.push('/pattern-editor')
}

function goToOrderFlow() {
  console.log('=== 准备跳转到订单流程 ===')
  
  // 检查是否完成了AI生成
  const aiResult = (window as any).aiGenerationResult
  if (!aiResult) {
    alert('请先完成AI智能打版流程！')
    return
  }
  
  console.log('✅ AI生成结果:', aiResult)
  console.log('✅ 准备跳转，携带参数：')
  console.log('   source: ai')
  console.log('   resultImageUrl:', aiResult.resultImageUrl)
  console.log('   originalImageUrl:', aiResult.originalImageUrl)
  console.log('   stitchCount:', aiResult.stitchCount)
  console.log('   colorCount:', aiResult.colorCount)
  console.log('   style:', aiResult.embroideryStyle)
  
  // 跳转到定制下单流程，携带AI生成的完整结果
  router.push({
    path: '/custom-order-flow',
    query: {
      source: 'ai',
      // AI生成的结果图（用于订单）
      resultImageUrl: aiResult.resultImageUrl,
      // 原始图片（用于展示）
      originalImageUrl: aiResult.originalImageUrl || '',
      // AI计算的参数
      stitchCount: aiResult.stitchCount?.toString() || '',
      colorCount: aiResult.colorCount?.toString() || '',
      // 用户选择的参数
      embroideryStyle: aiResult.embroideryStyle || '',
      textContent: aiResult.textContent || '',
      fontStyle: aiResult.fontStyle || '',
      selectedColors: aiResult.selectedColors?.join(',') || ''
    }
  })
}
</script>
