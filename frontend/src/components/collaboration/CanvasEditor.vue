<template>
  <div class="glow-card rounded-xl p-6">
    <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden">
      <canvas 
        ref="canvasRef"
        class="w-full h-full cursor-crosshair"
        @mousedown="handleMouseDown"
        @mousemove="handleMouseMove"
        @mouseup="handleMouseUp"
        @mouseleave="handleMouseUp"
      />
      
      <!-- 节点编辑提示 -->
      <div 
        v-if="showNodeHint"
        class="absolute top-4 left-4 bg-black/80 text-white px-4 py-2 rounded-lg text-sm"
      >
        点击节点进行拖拽调整
      </div>
    </div>
    
    <!-- 工具栏 -->
    <div class="mt-6 flex flex-wrap gap-3">
      <button
        v-for="tool in tools"
        :key="tool.id"
        @click="selectTool(tool.id)"
        :class="[
          'px-4 py-2 glow-card rounded-lg text-sm transition-all duration-300 cursor-pointer',
          currentTool === tool.id ? 'bg-accent-primary/20 border-2 border-accent-primary' : 'hover:bg-white/10'
        ]"
      >
        <span class="flex items-center space-x-2">
          <component :is="tool.icon" class="w-4 h-4" />
          <span>{{ tool.label }}</span>
        </span>
      </button>
      <button
        @click="resetView"
        class="px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer"
      >
        重置视图
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'

const props = defineProps<{
  imageUrl?: string
}>()

const emit = defineEmits<{
  (e: 'change'): void
}>()

const canvasRef = ref<HTMLCanvasElement>()
const currentTool = ref('pen')
const isDrawing = ref(false)
const showNodeHint = ref(false)

const tools = [
  { id: 'pen', label: '画笔', icon: 'svg' },
  { id: 'eraser', label: '橡皮', icon: 'svg' },
  { id: 'select', label: '选择', icon: 'svg' }
]

let ctx: CanvasRenderingContext2D | null = null
let startX = 0
let startY = 0

onMounted(() => {
  if (canvasRef.value) {
    ctx = canvasRef.value.getContext('2d')
    canvasRef.value.width = canvasRef.value.offsetWidth
    canvasRef.value.height = canvasRef.value.offsetHeight
    
    if (props.imageUrl && ctx) {
      const img = new Image()
      img.onload = () => {
        if (ctx && canvasRef.value) {
          ctx.drawImage(img, 0, 0, canvasRef.value.width, canvasRef.value.height)
        }
      }
      img.src = props.imageUrl
    }
  }
})

function selectTool(toolId: string) {
  currentTool.value = toolId
  showNodeHint.value = toolId === 'select'
}

function getMousePos(e: MouseEvent) {
  if (!canvasRef.value) return { x: 0, y: 0 }
  const rect = canvasRef.value.getBoundingClientRect()
  return {
    x: e.clientX - rect.left,
    y: e.clientY - rect.top
  }
}

function handleMouseDown(e: MouseEvent) {
  if (!ctx) return
  
  isDrawing.value = true
  const pos = getMousePos(e)
  startX = pos.x
  startY = pos.y
  
  if (currentTool.value === 'pen') {
    ctx.beginPath()
    ctx.moveTo(startX, startY)
  }
}

function handleMouseMove(e: MouseEvent) {
  if (!ctx || !isDrawing.value) return
  
  const pos = getMousePos(e)
  
  if (currentTool.value === 'pen') {
    ctx.lineTo(pos.x, pos.y)
    ctx.strokeStyle = '#8B5CF6'
    ctx.lineWidth = 3
    ctx.stroke()
  } else if (currentTool.value === 'eraser') {
    ctx.globalCompositeOperation = 'destination-out'
    ctx.beginPath()
    ctx.arc(pos.x, pos.y, 10, 0, Math.PI * 2)
    ctx.fill()
    ctx.globalCompositeOperation = 'source-over'
  }
  
  emit('change')
}

function handleMouseUp() {
  isDrawing.value = false
}

function resetView() {
  if (ctx && canvasRef.value) {
    ctx.clearRect(0, 0, canvasRef.value.width, canvasRef.value.height)
    emit('change')
  }
}
</script>
