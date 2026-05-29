<template>
  <div class="min-h-screen bg-black text-white relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
    <Particles container-id="particles-traditional-clothing" :count="20" />
    
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
            <h2 class="text-xl font-heading font-bold text-white">传统服装定制</h2>
          </div>
        </div>
      </div>
    </nav>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="text-center mb-12">
        <h1 class="text-4xl md:text-5xl font-heading font-bold mb-4 gradient-text">传统服装定制</h1>
        <p class="text-xl text-gray-300 max-w-3xl mx-auto">传统服饰刺绣工艺，让古典美学在现代焕发新生</p>
      </div>
      
      <!-- 服装类型选择 -->
      <div class="grid md:grid-cols-3 gap-6 mb-12">
        <div 
          @click="selectType('hanfu')"
          :class="[
            'glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 border-2',
            selectedType === 'hanfu' ? 'border-accent-primary/30 hover:border-accent-primary scale-105' : 'border-gray-700 hover:border-accent-primary'
          ]"
        >
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-full flex items-center justify-center mx-auto mb-4">
              <span class="text-4xl">👘</span>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2 text-white">汉服</h3>
            <p class="text-sm text-gray-400 mb-4">各朝代形制还原，传统纹样与现代设计结合</p>
            <button class="w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
              选择汉服
            </button>
          </div>
        </div>
        
        <div 
          @click="selectType('qipao')"
          :class="[
            'glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 border-2',
            selectedType === 'qipao' ? 'border-accent-cta/30 hover:border-accent-cta scale-105' : 'border-gray-700 hover:border-accent-cta'
          ]"
        >
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-full flex items-center justify-center mx-auto mb-4">
              <span class="text-4xl">🎽</span>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2 text-white">旗袍</h3>
            <p class="text-sm text-gray-400 mb-4">经典旗袍刺绣，展现东方女性优雅魅力</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
              选择旗袍
            </button>
          </div>
        </div>
        
        <div 
          @click="selectType('ethnic')"
          :class="[
            'glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 border-2',
            selectedType === 'ethnic' ? 'border-accent-vibrant/30 hover:border-accent-vibrant scale-105' : 'border-gray-700 hover:border-accent-vibrant'
          ]"
        >
          <div class="text-center">
            <div class="w-20 h-20 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-full flex items-center justify-center mx-auto mb-4">
              <span class="text-4xl">👗</span>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2 text-white">少数民族服饰</h3>
            <p class="text-sm text-gray-400 mb-4">各民族传统服饰，传承多元文化之美</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
              选择民族服饰
            </button>
          </div>
        </div>
      </div>
      
      <!-- 定制表单 -->
      <div class="grid md:grid-cols-2 gap-8 mb-8">
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-6 text-white">定制信息</h3>
          <div class="space-y-6">
            <div>
              <label class="block text-sm text-gray-400 mb-2">服装类型</label>
              <select v-model="clothingType" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>汉服 - 明制</option>
                <option>汉服 - 唐制</option>
                <option>汉服 - 宋制</option>
                <option>旗袍 - 传统款</option>
                <option>旗袍 - 改良款</option>
                <option>苗族服饰</option>
                <option>藏族服饰</option>
                <option>其他民族服饰</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">尺寸规格</label>
              <input v-model="size" type="text" placeholder="请输入您的尺寸" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300" />
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">刺绣图案</label>
              <div 
                class="aspect-video border-2 border-dashed border-accent-primary/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-primary/60 transition-all duration-300"
                @click="() => patternUploadRef?.click()"
                @dragover.prevent
                @drop.prevent="handleDrop"
              >
                <div v-if="!uploadedPattern" class="absolute inset-0 flex flex-col items-center justify-center">
                  <svg class="w-12 h-12 mb-2 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <p class="text-sm text-gray-400">上传图案或从纹样库选择</p>
                </div>
                <div v-else class="absolute inset-0">
                  <img :src="uploadedPattern" class="w-full h-full object-contain rounded-xl" alt="预览">
                  <button @click="removePattern" class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300 cursor-pointer">
                    <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
                <input ref="patternUploadRef" type="file" class="hidden" accept="image/*" @change="handlePatternUpload" />
              </div>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">传统纹样库</label>
              <div class="grid grid-cols-4 gap-2">
                <div 
                  v-for="(pattern, index) in patternLibrary" 
                  :key="index"
                  @click="selectPatternFromLibrary(pattern)"
                  class="aspect-square rounded border cursor-pointer transition-all duration-300"
                  :class="[
                    selectedPatternFromLibrary === pattern.id
                      ? `bg-gradient-to-br from-${pattern.color}/20 to-${pattern.color}/10 border-${pattern.color}`
                      : `bg-gradient-to-br from-${pattern.color}/20 to-${pattern.color}/10 border-gray-700 hover:border-${pattern.color}`
                  ]"
                ></div>
              </div>
              <button class="w-full mt-2 px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">
                浏览更多纹样
              </button>
            </div>
          </div>
        </div>
        
        <!-- AI智能提示词生成与实时预览 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white flex items-center justify-between">
            <span class="flex items-center space-x-2">
              <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
              <span>AI智能提示词生成</span>
            </span>
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
            <div>
              <label class="block text-sm text-gray-400 mb-2">输入您的想法或需求</label>
              <textarea v-model="promptInput" rows="3" placeholder="例如：我想要一件明制汉服，衣身绣有牡丹和凤凰图案，色彩以红色和金色为主..." @input="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 resize-none"></textarea>
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
      
      <div class="flex flex-col md:flex-row items-center justify-between gap-4 mt-8">
        <div class="p-4 glow-card rounded-lg">
          <p class="text-sm text-gray-400 mb-1">预估价格</p>
          <p class="text-2xl font-heading font-bold gradient-text">¥1,999</p>
          <p class="text-xs text-gray-500 mt-1">交付周期: 14-21天</p>
        </div>
        <button @click="submitOrder" class="px-8 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
          提交定制订单
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import Particles from '@/components/effects/Particles.vue'

const router = useRouter()
const patternUploadRef = ref<HTMLInputElement>()
const previewCanvasRef = ref<HTMLCanvasElement>()

function goHome() {
  router.push('/')
}

onMounted(() => {
  window.scrollTo(0, 0)
  updatePreview()
})

const selectedType = ref<'hanfu' | 'qipao' | 'ethnic' | null>(null)
const clothingType = ref('汉服 - 明制')
const size = ref('')
const uploadedPattern = ref<string>('')
const promptInput = ref('')
const generatedPrompt = ref('')
const previewMode = ref<'stitch' | 'color' | 'final'>('final')
const hasPreview = ref(false)
const selectedPatternFromLibrary = ref<string | null>(null)

const patternLibrary = [
  { id: 'pattern1', color: 'accent-primary' },
  { id: 'pattern2', color: 'accent-cta' },
  { id: 'pattern3', color: 'accent-vibrant' },
  { id: 'pattern4', color: 'accent-secondary' },
]

const previewModeText = computed(() => {
  const modes = {
    stitch: '针法预览',
    color: '颜色预览',
    final: '最终预览'
  }
  return modes[previewMode.value]
})

function selectType(type: 'hanfu' | 'qipao' | 'ethnic') {
  selectedType.value = selectedType.value === type ? null : type
  if (type === 'hanfu') {
    clothingType.value = '汉服 - 明制'
  } else if (type === 'qipao') {
    clothingType.value = '旗袍 - 传统款'
  } else if (type === 'ethnic') {
    clothingType.value = '苗族服饰'
  }
  updatePreview()
}

function handlePatternUpload(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedPattern.value = e.target?.result as string
      updatePreview()
    }
    reader.readAsDataURL(file)
  }
}

function handleDrop(event: DragEvent) {
  const file = event.dataTransfer?.files[0]
  if (file && file.type.startsWith('image/')) {
    const reader = new FileReader()
    reader.onload = (e) => {
      uploadedPattern.value = e.target?.result as string
      updatePreview()
    }
    reader.readAsDataURL(file)
  }
}

function removePattern() {
  uploadedPattern.value = ''
  if (patternUploadRef.value) {
    patternUploadRef.value.value = ''
  }
}

function selectPatternFromLibrary(pattern: { id: string; color: string }) {
  selectedPatternFromLibrary.value = selectedPatternFromLibrary.value === pattern.id ? null : pattern.id
  updatePreview()
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
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
  gradient.addColorStop(0, 'rgba(139, 92, 246, 0.1)')
  gradient.addColorStop(1, 'rgba(167, 139, 250, 0.1)')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, canvas.width, canvas.height)
  
  // 绘制预览效果
  const centerX = canvas.width / 2
  const centerY = canvas.height / 2
  
  if (previewMode.value === 'stitch') {
    ctx.strokeStyle = promptInput.value.includes('红色') ? '#FF6B6B' : promptInput.value.includes('金色') ? '#FFD700' : '#8B5CF6'
    ctx.lineWidth = 2
    
    if (clothingType.value.includes('汉服')) {
      // 绘制花卉图案
      for (let i = 0; i < 4; i++) {
        const angle = (Math.PI * 2 * i) / 4
        const x = centerX + Math.cos(angle) * canvas.width * 0.2
        const y = centerY + Math.sin(angle) * canvas.height * 0.2
        drawFlowerPattern(ctx, x, y, 15)
      }
    } else if (clothingType.value.includes('旗袍')) {
      // 绘制几何图案
      ctx.beginPath()
      ctx.moveTo(centerX, canvas.height * 0.2)
      ctx.lineTo(canvas.width * 0.3, centerY)
      ctx.lineTo(centerX, canvas.height * 0.8)
      ctx.lineTo(canvas.width * 0.7, centerY)
      ctx.closePath()
      ctx.stroke()
    }
  } else if (previewMode.value === 'color') {
    const colors: string[] = []
    if (promptInput.value.includes('红色')) colors.push('#FF6B6B')
    if (promptInput.value.includes('金色')) colors.push('#FFD700')
    if (promptInput.value.includes('蓝色')) colors.push('#4ECDC4')
    if (colors.length === 0) colors.push('#8B5CF6', '#A78BFA')
    
    const rectHeight = canvas.height / colors.length
    colors.forEach((color, index) => {
      ctx.fillStyle = color
      ctx.fillRect(canvas.width * 0.2, index * rectHeight, canvas.width * 0.6, rectHeight)
    })
  } else {
    // 最终预览
    updatePreview()
  }
}

function drawFlowerPattern(ctx: CanvasRenderingContext2D, x: number, y: number, size: number) {
  ctx.beginPath()
  for (let i = 0; i < 6; i++) {
    const angle = (Math.PI * 2 * i) / 6
    const px = x + Math.cos(angle) * size
    const py = y + Math.sin(angle) * size
    if (i === 0) ctx.moveTo(px, py)
    else ctx.lineTo(px, py)
  }
  ctx.closePath()
  ctx.stroke()
  ctx.beginPath()
  ctx.arc(x, y, size * 0.5, 0, Math.PI * 2)
  ctx.fill()
}

function togglePreviewMode() {
  const modes: Array<'stitch' | 'color' | 'final'> = ['stitch', 'color', 'final']
  const currentIndex = modes.indexOf(previewMode.value)
  previewMode.value = modes[(currentIndex + 1) % modes.length]
  updatePreview()
}

function generatePrompt() {
  if (!promptInput.value.trim()) {
    alert('请输入您的想法或需求')
    return
  }
  
  const userInput = promptInput.value.trim()
  generatedPrompt.value = `【传统服装定制提示词】
服装类型：${clothingType.value}
形制风格：${userInput.includes('明制') ? '明制' : userInput.includes('唐制') ? '唐制' : userInput.includes('宋制') ? '宋制' : '传统形制'}
图案元素：${userInput.includes('牡丹') ? '牡丹' : userInput.includes('凤凰') ? '凤凰' : '传统图案'}
色彩方案：${userInput.includes('红色') ? '红色' : ''}${userInput.includes('金色') ? ' + 金色' : ''}
工艺要求：传统刺绣工艺，传承古典美学

【AI优化建议】
- 建议从传统纹样库选择图案，确保文化准确性
- 使用传统针法，如苏绣、湘绣等
- 色彩搭配遵循传统配色规律`
}

function copyPrompt() {
  if (generatedPrompt.value) {
    navigator.clipboard.writeText(generatedPrompt.value)
    alert('提示词已复制到剪贴板')
  }
}

function submitOrder() {
  alert(`传统服装定制订单已提交！订单号：TRD-${Date.now()}`)
  router.push('/orders')
}

watch([clothingType, promptInput, previewMode], () => {
  updatePreview()
})

onMounted(() => {
  updatePreview()
})
</script>
