<template>
  <div class="min-h-screen bg-black text-white relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
    <Particles container-id="particles-dragon-robe" :count="20" />
    
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
            <h2 class="text-xl font-heading font-bold text-white">龙袍定制</h2>
          </div>
        </div>
      </div>
    </nav>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="text-center mb-12">
        <h1 class="text-4xl md:text-5xl font-heading font-bold mb-4 gradient-text">龙袍定制</h1>
        <p class="text-xl text-gray-300 max-w-3xl mx-auto">皇家工艺重现，九爪金龙、祥云纹样，尽显帝王风范</p>
      </div>
      
      <div class="grid md:grid-cols-2 gap-8 mb-12">
        <!-- 龙袍样式选择 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white">样式选择</h3>
          <div class="space-y-4">
            <div>
              <label class="block text-sm text-gray-400 mb-2">朝代形制</label>
              <select v-model="dynasty" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>明代龙袍</option>
                <option>清代龙袍</option>
                <option>唐代龙袍</option>
                <option>宋代龙袍</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">龙纹样式</label>
              <div class="grid grid-cols-3 gap-3">
                <div 
                  @click="selectPattern('nine-claw')"
                  :class="[
                    'aspect-square rounded-lg border-2 cursor-pointer transition-all duration-300 flex items-center justify-center',
                    selectedPattern === 'nine-claw' 
                      ? 'bg-gradient-to-br from-accent-vibrant/20 to-accent-vibrant/10 border-accent-vibrant/30' 
                      : 'bg-gradient-to-br from-accent-vibrant/20 to-accent-vibrant/10 border-gray-700 hover:border-accent-vibrant'
                  ]"
                >
                  <span :class="selectedPattern === 'nine-claw' ? 'text-xs text-white' : 'text-xs text-gray-400'">九爪金龙</span>
                </div>
                <div 
                  @click="selectPattern('five-claw')"
                  :class="[
                    'aspect-square rounded-lg border-2 cursor-pointer transition-all duration-300 flex items-center justify-center',
                    selectedPattern === 'five-claw' 
                      ? 'bg-gradient-to-br from-accent-primary/20 to-accent-primary/10 border-accent-primary/30' 
                      : 'bg-gradient-to-br from-accent-primary/20 to-accent-primary/10 border-gray-700 hover:border-accent-primary'
                  ]"
                >
                  <span :class="selectedPattern === 'five-claw' ? 'text-xs text-white' : 'text-xs text-gray-400'">五爪金龙</span>
                </div>
                <div 
                  @click="selectPattern('cloud-dragon')"
                  :class="[
                    'aspect-square rounded-lg border-2 cursor-pointer transition-all duration-300 flex items-center justify-center',
                    selectedPattern === 'cloud-dragon' 
                      ? 'bg-gradient-to-br from-accent-cta/20 to-accent-cta/10 border-accent-cta/30' 
                      : 'bg-gradient-to-br from-accent-cta/20 to-accent-cta/10 border-gray-700 hover:border-accent-cta'
                  ]"
                >
                  <span :class="selectedPattern === 'cloud-dragon' ? 'text-xs text-white' : 'text-xs text-gray-400'">祥云龙</span>
                </div>
              </div>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">尺寸规格</label>
              <div class="grid grid-cols-2 gap-3">
                <input v-model="chestSize" type="text" placeholder="胸围 (cm)" class="glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300" />
                <input v-model="length" type="text" placeholder="衣长 (cm)" class="glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300" />
              </div>
            </div>
          </div>
        </div>
        
        <!-- 工艺参数与实时预览 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white flex items-center justify-between">
            <span>工艺参数</span>
            <span class="text-xs text-accent-vibrant font-normal">所见即所绣</span>
          </h3>
          
          <!-- 实时预览区域 -->
          <div class="mb-6">
            <div class="aspect-square bg-gradient-to-br from-gray-900/50 to-gray-800/50 rounded-xl relative overflow-hidden border-2 border-accent-vibrant/30">
              <canvas ref="previewCanvasRef" class="w-full h-full"></canvas>
              <div v-if="!hasPreview" class="absolute inset-0 flex flex-col items-center justify-center">
                <svg class="w-20 h-20 mb-4 text-accent-vibrant/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <p class="text-sm text-gray-500">实时预览区域</p>
                <p class="text-xs text-gray-600 mt-1">调整参数查看效果</p>
              </div>
            </div>
            <div class="mt-3 flex items-center justify-between text-xs text-gray-400">
              <span>预览模式：{{ previewModeText }}</span>
              <button @click="togglePreviewMode" class="text-accent-vibrant hover:text-accent-primary transition-colors duration-300 cursor-pointer">
                切换视图
              </button>
            </div>
          </div>
          
          <div class="space-y-4">
            <div>
              <label class="block text-sm text-gray-400 mb-2">金线类型</label>
              <select v-model="goldThread" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>24K纯金线</option>
                <option>18K金线</option>
                <option>仿金线</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">银线类型</label>
              <select v-model="silverThread" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>999纯银线</option>
                <option>925银线</option>
                <option>仿银线</option>
              </select>
            </div>
            <div>
              <label class="block text-sm text-gray-400 mb-2">面料选择</label>
              <select v-model="fabric" @change="updatePreview" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                <option>真丝缎</option>
                <option>云锦</option>
                <option>蜀锦</option>
                <option>其他高级面料</option>
              </select>
            </div>
            <div class="p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
              <p class="text-sm text-gray-400 mb-2">工艺等级</p>
              <p class="text-lg font-heading font-semibold text-white">宫廷级</p>
              <p class="text-xs text-gray-500 mt-1">采用传统宫廷工艺标准，确保每个细节完美</p>
            </div>
            
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
                  <textarea v-model="promptInput" rows="3" placeholder="例如：我想要一件明代风格的龙袍，九爪金龙图案，配以祥云和海水纹样，使用金线银线..." class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 resize-none"></textarea>
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
      </div>
      
      <!-- 传统纹样库 -->
      <div class="glow-card rounded-xl p-6 mb-8">
        <h3 class="text-xl font-heading font-semibold mb-6 text-white">传统纹样库</h3>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div 
            v-for="pattern in traditionalPatterns" 
            :key="pattern.id"
            @click="selectTraditionalPattern(pattern.id)"
            class="aspect-square rounded-lg border cursor-pointer transition-all duration-300 flex flex-col items-center justify-center"
            :class="[
              selectedTraditionalPattern === pattern.id
                ? `bg-gradient-to-br from-${pattern.color}/20 to-${pattern.color}/10 border-${pattern.color}/30`
                : `bg-gradient-to-br from-${pattern.color}/20 to-${pattern.color}/10 border-gray-700 hover:border-${pattern.color}`
            ]"
          >
            <span class="text-2xl mb-2">{{ pattern.icon }}</span>
            <span :class="selectedTraditionalPattern === pattern.id ? 'text-xs text-white' : 'text-xs text-gray-400'">{{ pattern.name }}</span>
          </div>
        </div>
      </div>
      
      <div class="flex flex-col md:flex-row items-center justify-between gap-4">
        <div class="p-4 glow-card rounded-lg">
          <p class="text-sm text-gray-400 mb-1">预估价格</p>
          <p class="text-2xl font-heading font-bold gradient-text">¥9,999</p>
          <p class="text-xs text-gray-500 mt-1">交付周期: 30-45天</p>
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
const previewCanvasRef = ref<HTMLCanvasElement>()

function goHome() {
  router.push('/')
}

onMounted(() => {
  window.scrollTo(0, 0)
  updatePreview()
})

const dynasty = ref('明代龙袍')
const selectedPattern = ref<'nine-claw' | 'five-claw' | 'cloud-dragon'>('nine-claw')
const chestSize = ref('')
const length = ref('')
const goldThread = ref('24K纯金线')
const silverThread = ref('999纯银线')
const fabric = ref('真丝缎')
const promptInput = ref('')
const generatedPrompt = ref('')
const previewMode = ref<'stitch' | 'color' | 'final'>('final')
const hasPreview = ref(false)
const selectedTraditionalPattern = ref<string | null>(null)

const traditionalPatterns = [
  { id: 'dragon', name: '龙纹', icon: '🐉', color: 'accent-vibrant' },
  { id: 'cloud', name: '祥云', icon: '☁️', color: 'accent-primary' },
  { id: 'wave', name: '海水', icon: '🌊', color: 'accent-cta' },
  { id: 'flower', name: '花卉', icon: '🌺', color: 'accent-secondary' },
]

const previewModeText = computed(() => {
  const modes = {
    stitch: '针法预览',
    color: '颜色预览',
    final: '最终预览'
  }
  return modes[previewMode.value]
})

function selectPattern(pattern: 'nine-claw' | 'five-claw' | 'cloud-dragon') {
  selectedPattern.value = pattern
  updatePreview()
}

function selectTraditionalPattern(patternId: string) {
  selectedTraditionalPattern.value = selectedTraditionalPattern.value === patternId ? null : patternId
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
  gradient.addColorStop(0, 'rgba(16, 185, 129, 0.1)')
  gradient.addColorStop(1, 'rgba(139, 92, 246, 0.1)')
  ctx.fillStyle = gradient
  ctx.fillRect(0, 0, canvas.width, canvas.height)
  
  // 绘制龙纹预览
  const centerX = canvas.width / 2
  const centerY = canvas.height / 2
  
  if (previewMode.value === 'stitch') {
    // 绘制龙纹轮廓
    ctx.strokeStyle = goldThread.value.includes('24K') ? '#FFD700' : '#FFA500'
    ctx.lineWidth = 3
    ctx.lineCap = 'round'
    
    // 绘制龙身
    ctx.beginPath()
    ctx.moveTo(canvas.width * 0.2, centerY)
    ctx.quadraticCurveTo(centerX, canvas.height * 0.3, canvas.width * 0.8, centerY)
    ctx.quadraticCurveTo(centerX, canvas.height * 0.7, canvas.width * 0.2, centerY)
    ctx.stroke()
    
    // 绘制龙鳞（使用银线）
    ctx.strokeStyle = silverThread.value.includes('999') ? '#C0C0C0' : '#E0E0E0'
    ctx.lineWidth = 1
    for (let i = 0; i < 10; i++) {
      const x = canvas.width * 0.2 + (canvas.width * 0.6 * i) / 10
      const y = centerY + Math.sin(i * 0.5) * 20
      ctx.beginPath()
      ctx.arc(x, y, 5, 0, Math.PI * 2)
      ctx.stroke()
    }
  } else if (previewMode.value === 'color') {
    const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
    gradient.addColorStop(0, '#FFD700')
    gradient.addColorStop(0.5, '#C0C0C0')
    gradient.addColorStop(1, '#8B4513')
    ctx.fillStyle = gradient
    ctx.fillRect(canvas.width * 0.2, canvas.height * 0.2, canvas.width * 0.6, canvas.height * 0.6)
  } else {
    // 最终预览
    updatePreview()
    const gradient = ctx.createLinearGradient(0, 0, canvas.width, canvas.height)
    gradient.addColorStop(0, '#FFD700')
    gradient.addColorStop(0.5, '#C0C0C0')
    gradient.addColorStop(1, '#8B4513')
    ctx.fillStyle = gradient
    ctx.fillRect(canvas.width * 0.2, canvas.height * 0.2, canvas.width * 0.6, canvas.height * 0.6)
  }
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
  generatedPrompt.value = `【龙袍定制提示词】
朝代形制：${userInput.includes('明代') ? '明代龙袍' : userInput.includes('清代') ? '清代龙袍' : dynasty.value}
龙纹样式：${selectedPattern.value === 'nine-claw' ? '九爪金龙' : selectedPattern.value === 'five-claw' ? '五爪金龙' : '祥云龙'}
纹样组合：${userInput.includes('祥云') ? '祥云纹样' : ''}${userInput.includes('海水') ? ' + 海水纹样' : ''}
工艺要求：宫廷级工艺标准，使用${goldThread.value}和${silverThread.value}
面料选择：${fabric.value}

【AI优化建议】
- 建议使用24K纯金线和999纯银线，确保质感
- 采用传统宫廷工艺，每个细节完美还原
- 预计工时较长，确保工艺质量`
}

function copyPrompt() {
  if (generatedPrompt.value) {
    navigator.clipboard.writeText(generatedPrompt.value)
    alert('提示词已复制到剪贴板')
  }
}

function submitOrder() {
  alert(`龙袍定制订单已提交！订单号：DRG-${Date.now()}`)
  router.push('/orders')
}

watch([dynasty, selectedPattern, goldThread, silverThread, fabric, previewMode], () => {
  updatePreview()
})

onMounted(() => {
  updatePreview()
})
</script>
