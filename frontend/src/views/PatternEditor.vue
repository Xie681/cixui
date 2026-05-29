<template>
  <div class="min-h-screen bg-black text-white relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
    
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
            <h2 class="text-xl font-heading font-bold text-white">图案编辑器</h2>
          </div>
          <div class="flex items-center space-x-4">
            <button @click="savePattern" class="px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">保存</button>
            <button @click="exportPattern" class="px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">导出</button>
            <button @click="openOrderModal" class="neon-button px-4 py-2 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">立即下单</button>
          </div>
        </div>
      </div>
    </nav>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div class="grid lg:grid-cols-4 gap-6">
        <!-- 左侧：图层面板 -->
        <div class="lg:col-span-1">
          <div class="glow-card rounded-xl p-6 sticky top-24">
            <h3 class="text-lg font-heading font-semibold mb-4 text-white">图层</h3>
            <div class="space-y-2">
              <div 
                v-for="(layer, index) in layers" 
                :key="layer.id"
                @click="selectLayer(layer.id)"
                :class="[
                  'flex items-center justify-between p-2 glow-card rounded-lg cursor-pointer transition-all duration-300',
                  selectedLayerId === layer.id ? 'bg-white/10 border border-accent-primary' : 'hover:bg-white/5'
                ]"
              >
                <span class="text-sm text-white">图层 {{ index + 1 }}</span>
                <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
            </div>
            <button @click="addLayer" class="w-full mt-4 px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer">
              + 新建图层
            </button>
          </div>
        </div>
        
        <!-- 中间：画布区域 -->
        <div class="lg:col-span-2">
          <div class="glow-card rounded-xl p-6">
            <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden">
              <canvas 
                ref="canvasRef" 
                class="w-full h-full cursor-crosshair relative z-10"
                @mousedown="handleMouseDown"
                @mousemove="handleMouseMove"
                @mouseup="handleMouseUp"
                @mouseleave="handleMouseUp"
              ></canvas>
              <!-- 质感叠加层 -->
              <div v-if="embroideryEffect" class="absolute inset-0 z-20 pointer-events-none embroidery-texture-overlay"></div>
            </div>
            
            <!-- 工具栏 -->
            <div class="mt-6 flex flex-wrap gap-3">
              <button 
                @click="selectTool('pen')"
                :class="[
                  'px-4 py-2 glow-card rounded-lg text-sm transition-all duration-300 cursor-pointer flex items-center space-x-2',
                  currentTool === 'pen' ? 'bg-white/10 border border-accent-primary' : 'hover:bg-white/10'
                ]"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                </svg>
                <span>画笔</span>
              </button>
              <button 
                @click="selectTool('path')"
                :class="[
                  'px-4 py-2 glow-card rounded-lg text-sm transition-all duration-300 cursor-pointer flex items-center space-x-2',
                  currentTool === 'path' ? 'bg-white/10 border border-accent-primary' : 'hover:bg-white/10'
                ]"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
                </svg>
                <span>路径</span>
              </button>
              <button 
                @click="selectTool('fill')"
                :class="[
                  'px-4 py-2 glow-card rounded-lg text-sm transition-all duration-300 cursor-pointer flex items-center space-x-2',
                  currentTool === 'fill' ? 'bg-white/10 border border-accent-primary' : 'hover:bg-white/10'
                ]"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
                </svg>
                <span>填充</span>
              </button>
              <button 
                @click="selectTool('select')"
                :class="[
                  'px-4 py-2 glow-card rounded-lg text-sm transition-all duration-300 cursor-pointer flex items-center space-x-2',
                  currentTool === 'select' ? 'bg-white/10 border border-accent-primary' : 'hover:bg-white/10'
                ]"
              >
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 16l2.879-2.879m0 0a3 3 0 104.243-4.242 3 3 0 00-4.243 4.242zM21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span>选择</span>
              </button>
            </div>
          </div>
        </div>
        
        <!-- 右侧：属性面板 -->
        <div class="lg:col-span-1">
          <div class="glow-card rounded-xl p-6 sticky top-24">
            <h3 class="text-lg font-heading font-semibold mb-4 text-white">属性</h3>
            <div class="space-y-4">
              <div>
                <label class="block text-sm text-gray-400 mb-2">颜色</label>
                <input type="color" v-model="color" class="w-full h-10 rounded-lg cursor-pointer" />
              </div>
              <div>
                <label class="block text-sm text-gray-400 mb-2">笔刷大小</label>
                <input type="range" v-model="brushSize" min="1" max="50" class="w-full" />
                <span class="text-xs text-gray-500">{{ brushSize }}px</span>
              </div>
              <div>
                <label class="block text-sm text-gray-400 mb-2">不透明度</label>
                <input type="range" v-model="opacity" min="0" max="100" class="w-full" />
                <span class="text-xs text-gray-500">{{ opacity }}%</span>
              </div>
              <div>
                <label class="block text-sm text-gray-400 mb-2">实时预览</label>
                <label class="relative inline-flex items-center cursor-pointer">
                  <input type="checkbox" v-model="realTimePreview" class="sr-only peer" />
                  <div class="w-11 h-6 bg-gray-700 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-accent-primary/20 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-accent-primary"></div>
                </label>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 订单确认弹窗 -->
    <div v-if="showOrderModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm">
      <div class="bg-gray-900 border border-accent-primary/30 rounded-2xl w-full max-w-4xl max-h-[90vh] overflow-y-auto m-4 shadow-2xl relative">
        <div class="p-6 border-b border-white/10 flex justify-between items-center sticky top-0 bg-gray-900 z-10">
          <h3 class="text-2xl font-heading font-bold text-white">确认定制订单</h3>
          <button @click="showOrderModal = false" class="text-gray-400 hover:text-white transition-colors">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <div class="p-6 grid md:grid-cols-2 gap-8">
          <!-- 左侧：预览与参数 -->
          <div class="space-y-6">
            <div class="aspect-square bg-black/50 rounded-xl border border-white/10 flex items-center justify-center relative overflow-hidden">
              <div class="absolute inset-0 embroidery-pattern opacity-30"></div>
              <p class="text-gray-500">图案预览区域</p>
            </div>
            
            <div class="space-y-4">
              <div>
                <label class="block text-sm font-medium text-gray-300 mb-2">刺绣风格</label>
                <select v-model="orderParams.style" class="w-full bg-black/50 border border-white/10 rounded-lg px-4 py-2 text-white focus:border-accent-primary focus:ring-1 focus:ring-accent-primary outline-none">
                  <option value="suxiu">苏绣 (精细雅致)</option>
                  <option value="xiangxiu">湘绣 (色彩鲜艳)</option>
                  <option value="yuexiu">粤绣 (富丽堂皇)</option>
                  <option value="shuxiu">蜀绣 (严谨细腻)</option>
                </select>
              </div>
              
              <div class="grid grid-cols-2 gap-4">
                <div>
                  <label class="block text-sm font-medium text-gray-300 mb-2">面料材质</label>
                  <select v-model="orderParams.fabric" class="w-full bg-black/50 border border-white/10 rounded-lg px-4 py-2 text-white focus:border-accent-primary focus:ring-1 focus:ring-accent-primary outline-none">
                    <option value="silk">真丝</option>
                    <option value="cotton">棉麻</option>
                    <option value="satin">绸缎</option>
                    <option value="canvas">帆布</option>
                  </select>
                </div>
                <div>
                  <label class="block text-sm font-medium text-gray-300 mb-2">绣线类型</label>
                  <select v-model="orderParams.thread" class="w-full bg-black/50 border border-white/10 rounded-lg px-4 py-2 text-white focus:border-accent-primary focus:ring-1 focus:ring-accent-primary outline-none">
                    <option value="silk_thread">桑蚕丝线</option>
                    <option value="cotton_thread">棉线</option>
                    <option value="gold_thread">金银线</option>
                    <option value="nylon">尼龙线</option>
                  </select>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 右侧：尺寸与价格 -->
          <div class="space-y-6">
            <div class="bg-white/5 rounded-xl p-6 border border-white/10">
              <h4 class="text-lg font-semibold text-white mb-4">尺寸与数量</h4>
              
              <div class="grid grid-cols-2 gap-4 mb-4">
                <div>
                  <label class="block text-sm text-gray-400 mb-1">宽度 (cm)</label>
                  <input type="number" v-model="orderParams.size.width" min="5" max="100" class="w-full bg-black/50 border border-white/10 rounded-lg px-3 py-2 text-white" />
                </div>
                <div>
                  <label class="block text-sm text-gray-400 mb-1">高度 (cm)</label>
                  <input type="number" v-model="orderParams.size.height" min="5" max="100" class="w-full bg-black/50 border border-white/10 rounded-lg px-3 py-2 text-white" />
                </div>
              </div>
              
              <div class="mb-4">
                <label class="block text-sm text-gray-400 mb-1">数量</label>
                <div class="flex items-center space-x-4">
                  <button @click="orderParams.quantity > 1 && orderParams.quantity--" class="w-8 h-8 rounded-full bg-white/10 hover:bg-white/20 flex items-center justify-center text-white">-</button>
                  <span class="text-xl font-bold text-white">{{ orderParams.quantity }}</span>
                  <button @click="orderParams.quantity++" class="w-8 h-8 rounded-full bg-white/10 hover:bg-white/20 flex items-center justify-center text-white">+</button>
                </div>
              </div>
              
              <div class="mb-4">
                <label class="block text-sm text-gray-400 mb-2">工艺复杂度</label>
                <input type="range" v-model="orderParams.complexity" min="1" max="10" class="w-full accent-accent-primary" />
                <div class="flex justify-between text-xs text-gray-500 mt-1">
                  <span>简单</span>
                  <span>复杂</span>
                </div>
              </div>
            </div>
            
            <div class="bg-gradient-to-br from-accent-primary/20 to-accent-cta/20 rounded-xl p-6 border border-accent-primary/30">
              <div class="flex justify-between items-center mb-2">
                <span class="text-gray-300">预估单价</span>
                <span class="text-white">¥{{ Math.round(estimatedPrice / orderParams.quantity) }}</span>
              </div>
              <div class="flex justify-between items-center mb-4 pb-4 border-b border-white/10">
                <span class="text-gray-300">数量</span>
                <span class="text-white">x{{ orderParams.quantity }}</span>
              </div>
              <div class="flex justify-between items-end">
                <span class="text-lg font-bold text-white">总计</span>
                <div class="text-right">
                  <span class="text-3xl font-heading font-bold text-accent-primary">¥{{ estimatedPrice }}</span>
                  <p v-if="isCalculating" class="text-xs text-accent-cta animate-pulse">正在计算...</p>
                </div>
              </div>
            </div>
            
            <button @click="submitOrder" class="w-full neon-button py-4 text-white rounded-xl font-bold text-lg shadow-lg hover:shadow-accent-primary/50 transition-all duration-300">
              确认并支付
            </button>
            <p class="text-center text-xs text-gray-500">
              *最终价格可能根据实际图案复杂度略有调整
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { customizeApi } from '@/services/api/customize'

const router = useRouter()
const canvasRef = ref<HTMLCanvasElement>()

const layers = ref([{ id: 1, name: '图层 1' }])
const selectedLayerId = ref(1)
const currentTool = ref<'pen' | 'path' | 'fill' | 'select'>('pen')
const color = ref('#8B5CF6')
const brushSize = ref(5)
const opacity = ref(100)
const embroideryEffect = ref(false)

// Order State
const showOrderModal = ref(false)
const orderParams = ref({
  style: 'suxiu',
  fabric: 'silk',
  thread: 'silk_thread',
  size: { width: 20, height: 20 },
  quantity: 1,
  complexity: 5
})
const estimatedPrice = ref(0)
const isCalculating = ref(false)

const isDrawing = ref(false)
let lastX = 0
let lastY = 0

function initCanvas() {
  if (!canvasRef.value) return
  
  const canvas = canvasRef.value
  const container = canvas.parentElement
  if (!container) return
  
  canvas.width = container.clientWidth
  canvas.height = container.clientHeight
  
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 设置默认样式
  ctx.strokeStyle = color.value
  ctx.lineWidth = brushSize.value
  ctx.lineCap = 'round'
  ctx.lineJoin = 'round'
}

function handleMouseDown(event: MouseEvent) {
  if (!canvasRef.value) return
  
  isDrawing.value = true
  const rect = canvasRef.value.getBoundingClientRect()
  lastX = event.clientX - rect.left
  lastY = event.clientY - rect.top
  
  if (currentTool.value === 'pen') {
    const ctx = canvasRef.value.getContext('2d')
    if (ctx) {
      ctx.beginPath()
      ctx.moveTo(lastX, lastY)
    }
  }
}

function handleMouseMove(event: MouseEvent) {
  if (!isDrawing.value || !canvasRef.value) return
  
  const ctx = canvasRef.value.getContext('2d')
  if (!ctx) return
  
  const rect = canvasRef.value.getBoundingClientRect()
  const currentX = event.clientX - rect.left
  const currentY = event.clientY - rect.top
  
  if (currentTool.value === 'pen') {
    ctx.strokeStyle = color.value
    ctx.lineWidth = brushSize.value
    ctx.globalAlpha = opacity.value / 100
    ctx.lineTo(currentX, currentY)
    ctx.stroke()
    lastX = currentX
    lastY = currentY
  }
}

function handleMouseUp() {
  isDrawing.value = false
}

function selectTool(tool: 'pen' | 'path' | 'fill' | 'select') {
  currentTool.value = tool
}

function selectLayer(layerId: number) {
  selectedLayerId.value = layerId
}

function addLayer() {
  const newId = Math.max(...layers.value.map(l => l.id)) + 1
  layers.value.push({ id: newId, name: `图层 ${layers.value.length + 1}` })
  selectedLayerId.value = newId
}

function savePattern() {
  alert('图案已保存！')
}

function exportPattern() {
  if (!canvasRef.value) return
  
  const dataURL = canvasRef.value.toDataURL('image/png')
  const link = document.createElement('a')
  link.download = `pattern-${Date.now()}.png`
  link.href = dataURL
  link.click()
}

function openOrderModal() {
  showOrderModal.value = true
  calculatePrice()
}

async function calculatePrice() {
  isCalculating.value = true
  try {
    const result = await customizeApi.calculatePrice({
      style: orderParams.value.style,
      fabric: orderParams.value.fabric,
      thread: orderParams.value.thread,
      size: orderParams.value.size,
      complexity: orderParams.value.complexity,
      quantity: orderParams.value.quantity
    })
    estimatedPrice.value = result.totalPrice
  } catch (error) {
    console.error('Failed to calculate price:', error)
    // Fallback price for demo
    estimatedPrice.value = 299 * orderParams.value.quantity
  } finally {
    isCalculating.value = false
  }
}

async function submitOrder() {
  try {
    const dataURL = canvasRef.value?.toDataURL('image/png') || ''
    
    // In a real app, we would upload the image first and get a URL
    // For now we simulate success
    
    // const orderId = await customizeApi.submitOrder({
    //   imageUrl: 'uploaded-image-url',
    //   ...orderParams.value,
    //   addressId: 1 // Default address
    // })
    
    alert('订单提交成功！即将跳转到订单详情页...')
    showOrderModal.value = false
    router.push('/orders')
  } catch (error) {
    console.error('Failed to submit order:', error)
    alert('下单失败，请重试')
  }
}

watch(orderParams, () => {
  if (showOrderModal.value) {
    calculatePrice()
  }
}, { deep: true })

watch([color, brushSize, opacity], () => {
  if (canvasRef.value) {
    const ctx = canvasRef.value.getContext('2d')
    if (ctx) {
      ctx.strokeStyle = color.value
      ctx.lineWidth = brushSize.value
      ctx.globalAlpha = opacity.value / 100
    }
  }
})

onMounted(() => {
  initCanvas()
  window.addEventListener('resize', initCanvas)
})
</script>
