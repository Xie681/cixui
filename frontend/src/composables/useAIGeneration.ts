import { ref, computed } from 'vue'

export interface AIGenerationProgress {
  semantic: number
  stitch: number
  path: number
  dst: number
}

export interface AIGenerationStatus {
  semantic: string
  stitch: string
  path: string
  dst: string
}

export function useAIGeneration() {
  const isGenerating = ref(false)
  const progress = ref<AIGenerationProgress>({
    semantic: 0,
    stitch: 0,
    path: 0,
    dst: 0
  })
  
  const status = ref<AIGenerationStatus>({
    semantic: '等待中...',
    stitch: '等待中...',
    path: '等待中...',
    dst: '等待中...'
  })
  
  const previewReady = ref(false)
  const dstReady = ref(false)
  
  const overallProgress = computed(() => {
    return Math.round(
      (progress.value.semantic + 
       progress.value.stitch + 
       progress.value.path + 
       progress.value.dst) / 4
    )
  })
  
  async function startGeneration(params: {
    imageUrl?: string
    textPrompt?: string
    styleId?: string
    colors?: string[]
  }) {
    if (isGenerating.value) return
    
    isGenerating.value = true
    previewReady.value = false
    dstReady.value = false
    
    // 重置进度
    progress.value = {
      semantic: 0,
      stitch: 0,
      path: 0,
      dst: 0
    }
    
    status.value = {
      semantic: '处理中...',
      stitch: '等待中...',
      path: '等待中...',
      dst: '等待中...'
    }
    
    try {
      // TODO: 调用后端API
      // 目前使用模拟逻辑
      await simulateGeneration()
    } catch (error) {
      console.error('AI生成失败:', error)
      throw error
    } finally {
      isGenerating.value = false
    }
  }
  
  async function simulateGeneration() {
    // 语义分割
    status.value.semantic = '处理中...'
    for (let i = 0; i <= 100; i += 10) {
      await new Promise(resolve => setTimeout(resolve, 200))
      progress.value.semantic = i
    }
    status.value.semantic = '已完成'
    
    // 针法匹配
    status.value.stitch = '处理中...'
    for (let i = 0; i <= 100; i += 10) {
      await new Promise(resolve => setTimeout(resolve, 200))
      progress.value.stitch = i
    }
    status.value.stitch = '已完成'
    
    // 路径规划
    status.value.path = '处理中...'
    for (let i = 0; i <= 100; i += 10) {
      await new Promise(resolve => setTimeout(resolve, 200))
      progress.value.path = i
    }
    status.value.path = '已完成'
    
    // DST文件生成
    status.value.dst = '处理中...'
    for (let i = 0; i <= 100; i += 10) {
      await new Promise(resolve => setTimeout(resolve, 200))
      progress.value.dst = i
    }
    status.value.dst = '已完成'
    
    previewReady.value = true
    dstReady.value = true
  }
  
  function reset() {
    isGenerating.value = false
    progress.value = {
      semantic: 0,
      stitch: 0,
      path: 0,
      dst: 0
    }
    status.value = {
      semantic: '等待中...',
      stitch: '等待中...',
      path: '等待中...',
      dst: '等待中...'
    }
    previewReady.value = false
    dstReady.value = false
  }
  
  return {
    isGenerating,
    progress,
    status,
    previewReady,
    dstReady,
    overallProgress,
    startGeneration,
    reset
  }
}
