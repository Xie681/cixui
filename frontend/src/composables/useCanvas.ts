import { ref, onMounted, onUnmounted, type Ref } from 'vue'

export interface CanvasTool {
  id: string
  label: string
}

export function useCanvas(canvasRef: Ref<HTMLCanvasElement | undefined>) {
  const currentTool = ref('pen')
  const isDrawing = ref(false)
  const ctx = ref<CanvasRenderingContext2D | null>(null)
  
  const tools: CanvasTool[] = [
    { id: 'pen', label: '画笔' },
    { id: 'eraser', label: '橡皮' },
    { id: 'select', label: '选择' }
  ]
  
  let startX = 0
  let startY = 0
  
  onMounted(() => {
    if (canvasRef.value) {
      ctx.value = canvasRef.value.getContext('2d')
      canvasRef.value.width = canvasRef.value.offsetWidth
      canvasRef.value.height = canvasRef.value.offsetHeight
    }
  })
  
  function selectTool(toolId: string) {
    currentTool.value = toolId
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
    if (!ctx.value) return
    
    isDrawing.value = true
    const pos = getMousePos(e)
    startX = pos.x
    startY = pos.y
    
    if (currentTool.value === 'pen') {
      ctx.value.beginPath()
      ctx.value.moveTo(startX, startY)
    }
  }
  
  function handleMouseMove(e: MouseEvent) {
    if (!ctx.value || !isDrawing.value) return
    
    const pos = getMousePos(e)
    
    if (currentTool.value === 'pen') {
      ctx.value.lineTo(pos.x, pos.y)
      ctx.value.strokeStyle = '#8B5CF6'
      ctx.value.lineWidth = 3
      ctx.value.stroke()
    } else if (currentTool.value === 'eraser') {
      ctx.value.globalCompositeOperation = 'destination-out'
      ctx.value.beginPath()
      ctx.value.arc(pos.x, pos.y, 10, 0, Math.PI * 2)
      ctx.value.fill()
      ctx.value.globalCompositeOperation = 'source-over'
    }
  }
  
  function handleMouseUp() {
    isDrawing.value = false
  }
  
  function clear() {
    if (ctx.value && canvasRef.value) {
      ctx.value.clearRect(0, 0, canvasRef.value.width, canvasRef.value.height)
    }
  }
  
  function loadImage(imageUrl: string) {
    if (!ctx.value || !canvasRef.value) return
    
    const img = new Image()
    img.onload = () => {
      if (ctx.value && canvasRef.value) {
        ctx.value.drawImage(img, 0, 0, canvasRef.value.width, canvasRef.value.height)
      }
    }
    img.src = imageUrl
  }
  
  return {
    currentTool,
    isDrawing,
    tools,
    selectTool,
    handleMouseDown,
    handleMouseMove,
    handleMouseUp,
    clear,
    loadImage
  }
}
