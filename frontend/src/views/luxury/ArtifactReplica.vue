<template>
  <div class="min-h-screen bg-black text-white relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
    <Particles container-id="particles-artifact-replica" :count="20" />
    
    <!-- 导航栏 -->
    <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex items-center justify-between h-16">
          <div class="flex items-center space-x-4">
            <button @click="$router.back()" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
              <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
              </svg>
            </button>
            <h2 class="text-xl font-heading font-bold text-white">文物复刻定制</h2>
          </div>
        </div>
      </div>
    </nav>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="text-center mb-12">
        <h1 class="text-4xl md:text-5xl font-heading font-bold mb-4 gradient-text">文物复刻</h1>
        <p class="text-xl text-gray-300 max-w-3xl mx-auto">数字化保护与传承，让千年文物以刺绣形式重现光彩</p>
      </div>
      
      <div class="grid md:grid-cols-2 gap-8 mb-12">
        <!-- 文物信息 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white">文物信息</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm text-gray-400 mb-2">文物名称</label>
              <input v-model="artifactName" type="text" placeholder="请输入文物名称" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300" />
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">所属朝代</label>
              <select v-model="dynasty" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>商周</option>
                <option>秦汉</option>
                <option>唐宋</option>
                <option>明清</option>
                <option>其他</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">文物类型</label>
              <select v-model="artifactType" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>纺织品</option>
                <option>书画</option>
                <option>器物纹样</option>
                <option>建筑装饰</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">上传文物图片或3D扫描文件</label>
              <div 
                class="aspect-video border-2 border-dashed border-accent-cta/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-cta/60 transition-all duration-300"
                @click="() => fileInputRef?.click()"
                @dragover.prevent
                @drop.prevent="handleDrop"
              >
                <div v-if="!uploadedImage" class="absolute inset-0 flex flex-col items-center justify-center">
                  <svg class="w-12 h-12 mb-2 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <p class="text-sm text-gray-400">支持 JPG、PNG、OBJ 格式</p>
                </div>
                <div v-else class="absolute inset-0">
                  <img :src="uploadedImage" class="w-full h-full object-contain rounded-xl" alt="预览">
                  <button @click="removePreview" class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300 cursor-pointer">
                    <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                </div>
                <input ref="fileInputRef" type="file" class="hidden" accept="image/*,.obj" @change="handleFileUpload" />
              </div>
            </div>
          </div>
        </div>
        
        <!-- 数字化处理与实时预览 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white flex items-center justify-between">
            <span>数字化处理</span>
            <span class="text-xs text-accent-primary font-normal">所见即所绣</span>
          </h3>
          
          <!-- 实时预览区域 -->
          <div class="mb-6">
            <div class="aspect-square bg-gradient-to-br from-gray-900/50 to-gray-800/50 rounded-xl relative overflow-hidden border-2 border-accent-cta/30">
              <canvas ref="previewCanvasRef" class="w-full h-full"></canvas>
              <div v-if="!hasPreview" class="absolute inset-0 flex flex-col items-center justify-center">
                <svg class="w-20 h-20 mb-4 text-accent-cta/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p class="text-sm text-gray-500">实时预览区域</p>
                <p class="text-xs text-gray-600 mt-1">调整参数查看效果</p>
              </div>
            </div>
            <div class="mt-3 flex items-center justify-between text-xs text-gray-400">
              <span>预览模式：{{ previewModeText }}</span>
              <button @click="togglePreviewMode" class="text-accent-cta hover:text-accent-primary transition-colors duration-300 cursor-pointer">
                切换视图
              </button>
            </div>
          </div>
          
          <div class="space-y-4">
            <div class="p-4 bg-accent-cta/10 rounded-lg border border-accent-cta/30">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-white">3D扫描分析</span>
                <span class="text-xs text-accent-cta">{{ processingStatus.scan }}</span>
              </div>
              <p class="text-sm text-gray-400">将提取文物的立体结构和细节纹理</p>
            </div>
            <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-white">图案提取</span>
                <span class="text-xs text-accent-primary">{{ processingStatus.extract }}</span>
              </div>
              <p class="text-sm text-gray-400">AI将自动识别并提取可刺绣的图案元素</p>
            </div>
            <div class="p-4 bg-gray-800 rounded-lg border border-gray-700">
              <div class="flex items-center justify-between mb-2">
                <span class="text-sm font-semibold text-gray-400">工艺还原</span>
                <span class="text-xs text-gray-500">{{ processingStatus.restore }}</span>
              </div>
              <p class="text-sm text-gray-500">匹配传统工艺技法，还原历史风貌</p>
            </div>
          </div>
          <button @click="startProcessing" class="w-full mt-6 neon-button px-4 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            开始数字化处理
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
                <textarea v-model="promptInput" rows="3" placeholder="例如：我想要复刻一件唐代的纺织品文物，包含花卉和鸟类的图案，色彩丰富..." class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 resize-none"></textarea>
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
      
      <!-- 保护说明 -->
      <div class="glow-card rounded-xl p-6 mb-8">
        <h3 class="text-xl font-heading font-semibold mb-4 text-white">文物保护说明</h3>
        <div class="space-y-3 text-sm text-gray-400">
          <p>• 本服务仅用于文化传承和教育目的，不涉及文物实体的商业交易</p>
          <p>• 所有数字化数据将严格保密，仅用于刺绣图案生成</p>
          <p>• 复刻作品将标注"复刻品"标识，避免与文物混淆</p>
          <p>• 建议与相关博物馆或文物保护机构合作，确保复刻的准确性</p>
        </div>
      </div>
      
      <div class="flex flex-col md:flex-row items-center justify-between gap-4">
        <div class="p-4 glow-card rounded-lg">
          <p class="text-sm text-gray-400 mb-1">预估价格</p>
          <p class="text-2xl font-heading font-bold gradient-text">¥4,999</p>
        </div>
        <button @click="submitOrder" class="px-8 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
          提交复刻申请
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
const fileInputRef = ref<HTMLInputElement>()
const previewCanvasRef = ref<HTMLCanvasElement>()

function goHome() {
  router.push('/')
}

onMounted(() => {
  window.scrollTo(0, 0)
})

const artifactName = ref('')
const dynasty = ref('唐宋')
const artifactType = ref('纺织品')
const uploadedImage = ref<string>('')
const promptInput = ref('')
const generatedPrompt = ref('')
const previewMode = ref<'stitch' | 'color' | 'final'>('final')
const hasPreview = ref(false)

const processingStatus = ref({
  scan: '待处理',
  extract: '待处理',
  restore: '待处理'
})

const previewModeText = computed(() => {
  const modes = {
    stitch: '针法预览',
    color: '颜色预览',
    final: '最终预览'
  }
  return modes[previewMode.value]
})

function handleFileUpload(event: Event) {
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

function handleDrop(event: DragEvent) {
  const file = event.dataTransfer?.files[0]
  if (file && (file.type.startsWith('image/') || file.name.endsWith('.obj'))) {
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
  if (fileInputRef.value) {
    fileInputRef.value.value = ''
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
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
  gradient.addColorStop(0, 'rgba(6, 182, 212, 0.1)')
  gradient.addColorStop(1, 'rgba(16, 185, 129, 0.1)')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, canvas.width, canvas.height)
  
  // 绘制预览效果
  const centerX = canvas.width / 2
  const centerY = canvas.height / 2
  const radius = Math.min(canvas.width, canvas.height) * 0.3
  
  ctx.strokeStyle = '#06B6D4'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.arc(centerX, centerY, radius, 0, Math.PI * 2)
  ctx.stroke()
}

function togglePreviewMode() {
  const modes: Array<'stitch' | 'color' | 'final'> = ['stitch', 'color', 'final']
  const currentIndex = modes.indexOf(previewMode.value)
  previewMode.value = modes[(currentIndex + 1) % modes.length]
  updatePreview()
}

function startProcessing() {
  processingStatus.value.scan = '进行中...'
  setTimeout(() => {
    processingStatus.value.scan = '已完成'
    updatePreview()
    
    processingStatus.value.extract = '进行中...'
    setTimeout(() => {
      processingStatus.value.extract = '已完成'
      updatePreview()
      
      processingStatus.value.restore = '进行中...'
      setTimeout(() => {
        processingStatus.value.restore = '已完成'
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
  
  generatedPrompt.value = `基于您的需求"${promptInput.value}"，AI生成的智能提示词：

【文物信息】${artifactName.value || '未命名文物'} - ${dynasty.value} - ${artifactType.value}
【复刻要求】${promptInput.value}
【工艺建议】采用${dynasty.value}时期的传统刺绣工艺，确保历史风貌的准确还原
【色彩方案】根据文物原貌，采用传统色彩搭配
【特殊说明】数字化保护与传承，确保文化价值的准确传递`
}

function copyPrompt() {
  if (generatedPrompt.value) {
    navigator.clipboard.writeText(generatedPrompt.value)
    alert('提示词已复制到剪贴板')
  }
}

function submitOrder() {
  alert(`复刻申请已提交！订单号：ART-${Date.now()}`)
  router.push('/orders')
}

watch([dynasty, artifactType, previewMode], () => {
  updatePreview()
})

onMounted(() => {
  updatePreview()
})
</script>
