<template>
  <div class="min-h-screen bg-black text-white relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
    <Particles container-id="particles-luxury-replica" :count="20" />
    
    <!-- 导航栏 -->
    <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center space-x-4">
            <button @click="goHome" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h2 class="text-xl font-heading font-bold text-white">奢侈品复刻定制</h2>
          </div>
        </div>
      </div>
    </nav>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="text-center mb-12">
        <h1 class="text-4xl md:text-5xl font-heading font-bold mb-4 gradient-text">奢侈品复刻</h1>
        <p class="text-xl text-gray-300 max-w-3xl mx-auto">一比一精准复刻国际奢侈品牌刺绣工艺，还原每一个细节</p>
      </div>
      
      <div class="grid md:grid-cols-2 gap-8 mb-12">
        <!-- 上传参考图片 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white">上传参考图片</h3>
          <div 
            class="aspect-square border-2 border-dashed border-accent-primary/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-primary/60 transition-all duration-300 mb-4"
            @click="() => imageUploadRef?.click()"
            @dragover.prevent="handleDragOver"
            @dragleave.prevent="handleDragLeave"
            @drop.prevent="handleDrop"
          >
            <div v-if="!uploadedImage" class="absolute inset-0 flex flex-col items-center justify-center z-10">
              <svg class="w-16 h-16 mb-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
              </svg>
              <p class="text-gray-300 mb-2">拖拽图片到此处</p>
              <p class="text-sm text-gray-500">或点击选择文件</p>
            </div>
            <div v-else class="absolute inset-0">
              <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
              <button @click="removePreview" class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300 cursor-pointer">
                <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <input ref="imageUploadRef" type="file" class="hidden" accept="image/*" @change="handleImageUpload" />
          </div>
          <div class="space-y-3">
            <div>
              <label class="block text-sm text-gray-400 mb-2">品牌选择</label>
              <select v-model="selectedBrand" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>爱马仕 (Hermès)</option>
                <option>香奈儿 (Chanel)</option>
                <option>迪奥 (Dior)</option>
                <option>古驰 (Gucci)</option>
                <option>其他品牌</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">复刻精度</label>
              <div class="flex items-center space-x-4">
                <input type="range" v-model="precision" min="80" max="100" @input="updatePreview" class="flex-1" />
                <span class="text-sm text-white font-semibold">{{ precision }}%</span>
              </div>
              <p class="text-xs text-gray-500 mt-1">高精度模式将更准确地还原细节</p>
            </div>
          </div>
        </div>
        
        <!-- AI分析结果与实时预览 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white flex items-center justify-between">
            <span>AI智能分析</span>
            <span class="text-xs text-accent-primary font-normal">所见即所绣</span>
          </h3>
          
          <!-- 实时预览区域 -->
          <div class="mb-6">
            <div class="aspect-square bg-gradient-to-br from-gray-900/50 to-gray-800/50 rounded-xl relative overflow-hidden border-2 border-accent-primary/30">
              <canvas ref="previewCanvasRef" class="w-full h-full"></canvas>
              <div v-if="!hasPreview" class="absolute inset-0 flex flex-col items-center justify-center">
                <svg class="w-20 h-20 mb-4 text-accent-primary/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p class="text-sm text-gray-500">实时预览区域</p>
                <p class="text-xs text-gray-600 mt-1">调整参数查看效果</p>
              </div>
            </div>
            <div class="mt-3 flex items-center justify-between text-xs text-gray-400">
              <span>预览模式：{{ previewModeText }}</span>
              <button @click="togglePreviewMode" class="text-accent-primary hover:text-accent-secondary transition-colors duration-300 cursor-pointer">
                切换视图
              </button>
            </div>
          </div>
          
          <div class="space-y-4">
            <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-white">图案识别</span>
                <span class="text-xs text-accent-primary">{{ analysisStatus.pattern }}</span>
              </div>
              <p class="text-sm text-gray-400">已识别出 <span>{{ identifiedElements }}</span> 个主要图案元素</p>
            </div>
            <div class="p-4 bg-accent-cta/10 rounded-lg border border-accent-cta/30">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-white">针法匹配</span>
                <span class="text-xs text-accent-cta">{{ analysisStatus.stitch }}</span>
              </div>
              <p class="text-sm text-gray-400">正在匹配最佳针法组合...</p>
            </div>
            <div class="p-4 bg-gray-800 rounded-lg border border-gray-700">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-gray-400">路径规划</span>
                <span class="text-xs text-gray-500">{{ analysisStatus.path }}</span>
              </div>
              <p class="text-sm text-gray-500">等待针法匹配完成</p>
            </div>
          </div>
          <button @click="startAnalysis" class="w-full mt-6 neon-button px-4 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            开始AI分析
          </button>
          
          <!-- AI智能提示词生成 -->
          <div class="mt-6">
            <h3 class="text-lg font-heading font-semibold mb-4 text-white flex items-center space-x-2">
              <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
              <span>AI智能提示词生成</span>
            </h3>
            <div class="space-y-4">
              <div>
                <label class="block text-sm text-gray-400 mb-2">输入您的想法或需求</label>
                <textarea v-model="promptInput" rows="3" placeholder="例如：我想要一个爱马仕风格的丝巾图案，包含花卉元素，色彩优雅..." class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 resize-none"></textarea>
              </div>
              <button @click="generatePrompt" class="w-full neon-button px-4 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                生成智能提示词
              </button>
              <div v-if="generatedPrompt" class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
                <div class="flex items-start justify-between mb-2">
                  <span class="text-sm font-semibold text-white">生成的提示词：</span>
                  <button @click="copyPrompt" class="text-xs text-accent-primary hover:text-accent-secondary transition-colors duration-300 cursor-pointer">复制</button>
                </div>
                <p class="text-sm text-gray-300 whitespace-pre-wrap">{{ generatedPrompt }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 工艺参数设置 -->
      <div class="glow-card rounded-xl p-6 mb-8">
        <h3 class="text-xl font-heading font-semibold mb-6 text-white">工艺参数设置</h3>
        <div class="grid md:grid-cols-3 gap-6">
          <div>
            <label class="block text-sm text-gray-400 mb-2">绣线类型</label>
            <select v-model="threadType" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
              <option>丝线</option>
              <option>棉线</option>
              <option>金银线</option>
              <option>混纺线</option>
            </select>
          </div>
          <div>
            <label class="block text-sm text-gray-400 mb-2">针迹密度</label>
            <input type="range" v-model="density" min="1" max="10" @input="updatePreview" class="w-full" />
            <p class="text-xs text-gray-500 mt-1">当前值: {{ density }}/10</p>
          </div>
          <div>
            <label class="block text-sm text-gray-400 mb-2">预计工时</label>
            <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
              <p class="text-2xl font-heading font-bold text-accent-primary">8-12</p>
              <p class="text-sm text-gray-400">小时</p>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 预览与提交 -->
      <div class="flex flex-col md:flex-row items-center justify-between gap-4">
        <div class="flex items-center space-x-4">
          <div class="p-4 glow-card rounded-lg">
            <p class="text-sm text-gray-400 mb-1">预估价格</p>
            <p class="text-2xl font-heading font-bold gradient-text">¥2,999</p>
          </div>
          <div class="p-4 glow-card rounded-lg">
            <p class="text-sm text-gray-400 mb-1">交付周期</p>
            <p class="text-xl font-heading font-semibold text-white">7-14天</p>
          </div>
        </div>
        <div class="flex items-center space-x-4">
          <button @click="previewDesign" class="px-6 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            预览效果
          </button>
          <button @click="submitOrder" class="px-8 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            提交定制订单
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import Particles from '@/components/effects/Particles.vue'

const router = useRouter()
const imageUploadRef = ref<HTMLInputElement>()
const previewCanvasRef = ref<HTMLCanvasElement>()

function goHome() {
  router.push('/')
}

onMounted(() => {
  window.scrollTo(0, 0)
})

const uploadedImage = ref<string>('')
const selectedBrand = ref('爱马仕 (Hermès)')
const precision = ref(95)
const threadType = ref('丝线')
const density = ref(7)
const promptInput = ref('')
const generatedPrompt = ref('')
const previewMode = ref<'stitch' | 'color' | 'final'>('final')
const hasPreview = ref(false)

const analysisStatus = ref({
  pattern: '待处理',
  stitch: '待处理',
  path: '待处理'
})

const identifiedElements = ref(0)

const previewModeText = computed(() => {
  const modes = {
    stitch: '针法预览',
    color: '颜色预览',
    final: '最终预览'
  }
  return modes[previewMode.value]
})

function handleImageUpload(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedImage.value = e.target?.result as string
      updatePreview()
    }
    reader.readAsDataURL(file)
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
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedImage.value = e.target?.result as string
      updatePreview()
    }
    reader.readAsDataURL(file)
  }
}

function removePreview() {
  uploadedImage.value = ''
  hasPreview.value = false
  if (imageUploadRef.value) {
    imageUploadRef.value.value = ''
  }
}

function updatePreview() {
  if (!previewCanvasRef.value) return
  
  const canvas = previewCanvasRef.value
  const container = canvas.parentElement
  if (!container) return
  
  canvas.width = container.clientWidth
  canvas.height = container.clientHeight
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  hasPreview.value = true
  
  // 清空画布
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  // 绘制背景渐变
  const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
  gradient.addColorStop(0, 'rgba(139, 92, 246, 0.1)')
  gradient.addColorStop(1, 'rgba(6, 182, 212, 0.1)')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, canvas.width, canvas.height)
  
  // 根据预览模式绘制
  if (previewMode.value === 'stitch') {
    drawStitchPreview(ctx, canvas.width, canvas.height)
  } else if (previewMode.value === 'color') {
    drawColorPreview(ctx, canvas.width, canvas.height)
  } else {
    drawFinalPreview(ctx, canvas.width, canvas.height)
  }
}

function drawStitchPreview(ctx: CanvasRenderingContext2D, width: number, height: number) {
  const centerX = width / 2
  const centerY = height / 2
  const radius = Math.min(width, height) * 0.3
  
  ctx.strokeStyle = threadType.value === '金银线' ? '#FFD700' : '#8B5CF6'
  ctx.lineWidth = density.value / 2
  ctx.lineCap = 'round'
  
  // 绘制圆形图案
  for (let i = 0; i < 8; i++) {
    const angle = (Math.PI * 2 * i) / 8
    const x1 = centerX + Math.cos(angle) * radius * 0.5
    const y1 = centerY + Math.sin(angle) * radius * 0.5
    const x2 = centerX + Math.cos(angle) * radius
    const y2 = centerY + Math.sin(angle) * radius
    
    ctx.beginPath()
    ctx.moveTo(x1, y1)
    ctx.lineTo(x2, y2)
    ctx.stroke()
  }
  
  // 绘制中心图案
  ctx.beginPath()
  ctx.arc(centerX, centerY, radius * 0.3, 0, Math.PI * 2)
  ctx.stroke()
  
  // 绘制细节
  const detailCount = Math.floor((precision.value - 80) / 5)
  for (let i = 0; i < detailCount; i++) {
    const angle = (Math.PI * 2 * i) / detailCount
    const x = centerX + Math.cos(angle) * radius * 0.7
    const y = centerY + Math.sin(angle) * radius * 0.7
    ctx.beginPath()
    ctx.arc(x, y, 3, 0, Math.PI * 2)
    ctx.fill()
  }
}

function drawColorPreview(ctx: CanvasRenderingContext2D, width: number, height: number) {
  const colors: Record<string, string[]> = {
    '爱马仕': ['#FF6B6B', '#4ECDC4', '#FFE66D'],
    '香奈儿': ['#000000', '#FFFFFF', '#FFD700'],
    '默认': ['#8B5CF6', '#06B6D4', '#10B981']
  }
  
  const brand = selectedBrand.value.split(' ')[0]
  const palette = colors[brand] || colors['默认']
  const rectWidth = width / palette.length
  
  palette.forEach((color, index) => {
    ctx.fillStyle = color
    ctx.fillRect(index * rectWidth, 0, rectWidth, height)
  })
}

function drawFinalPreview(ctx: CanvasRenderingContext2D, width: number, height: number) {
  // 绘制最终预览效果
  const centerX = width / 2
  const centerY = height / 2
  const radius = Math.min(width, height) * 0.35
  
  // 绘制背景图案
  const gradient = ctx.createRadialGradient(centerX, centerY, 0, centerX, centerY, radius)
  gradient.addColorStop(0, 'rgba(139, 92, 246, 0.3)')
  gradient.addColorStop(1, 'rgba(6, 182, 212, 0.1)')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, width, height)
  
  // 绘制主要图案
  ctx.strokeStyle = threadType.value === '金银线' ? '#FFD700' : '#8B5CF6'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.arc(centerX, centerY, radius, 0, Math.PI * 2)
  ctx.stroke()
  
  // 绘制装饰元素
  for (let i = 0; i < 12; i++) {
    const angle = (Math.PI * 2 * i) / 12
    const x = centerX + Math.cos(angle) * radius * 0.7
    const y = centerY + Math.sin(angle) * radius * 0.7
    ctx.beginPath()
    ctx.arc(x, y, 5, 0, Math.PI * 2)
    ctx.fill()
  }
}

function togglePreviewMode() {
  const modes: Array<'stitch' | 'color' | 'final'> = ['stitch', 'color', 'final']
  const currentIndex = modes.indexOf(previewMode.value)
  previewMode.value = modes[(currentIndex + 1) % modes.length]
  updatePreview()
}

function startAnalysis() {
  analysisStatus.value.pattern = '进行中...'
  setTimeout(() => {
    analysisStatus.value.pattern = '已完成'
    identifiedElements.value = 12
    
    analysisStatus.value.stitch = '进行中...'
    setTimeout(() => {
      analysisStatus.value.stitch = '已完成'
      
      analysisStatus.value.path = '进行中...'
      setTimeout(() => {
        analysisStatus.value.path = '已完成'
        updatePreview()
      }, 2000)
    }, 2000)
  }, 2000)
}

function generatePrompt() {
  if (!promptInput.value.trim()) {
    alert('请输入您的想法或需求')
    return
  }
  
  // 模拟AI生成提示词
  generatedPrompt.value = `基于您的需求"${promptInput.value}"，AI生成的智能提示词：

【风格定位】${selectedBrand.value}经典风格
【图案元素】花卉、几何、品牌标识
【色彩方案】优雅、经典、高级感
【工艺要求】${threadType.value}，针迹密度${density.value}/10，复刻精度${precision.value}%
【特殊要求】${promptInput.value}

建议：采用${selectedBrand.value}的传统工艺技法，确保每个细节都完美还原。`
}

function copyPrompt() {
  if (generatedPrompt.value) {
    navigator.clipboard.writeText(generatedPrompt.value)
    alert('提示词已复制到剪贴板')
  }
}

function previewDesign() {
  router.push('/preview')
}

function submitOrder() {
  alert(`订单提交成功！订单号：LUX-${Date.now()}`)
  router.push('/orders')
}

watch([selectedBrand, precision, threadType, density, previewMode], () => {
  updatePreview()
})

onMounted(() => {
  updatePreview()
})
</script>
