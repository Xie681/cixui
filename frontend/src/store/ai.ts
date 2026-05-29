import { defineStore } from 'pinia'
import { ref } from 'vue'
import { customizeApi } from '@/services/api'

export const useAIStore = defineStore('ai', () => {
  // AI分析状态
  const isAnalyzing = ref(false)
  const analysisProgress = ref(0)
  const currentStep = ref(1)
  const analysisResult = ref<any>(null)
  const dstFileUrl = ref<string | null>(null)
  
  // 分析步骤状态
  const semanticStatus = ref('等待中...')
  const stitchStatus = ref('等待中...')
  const pathStatus = ref('等待中...')
  const dstStatus = ref('等待中...')
  
  // 开始AI分析
  async function startAnalysis(imageUrl: string, style: string) {
    isAnalyzing.value = true
    currentStep.value = 1
    
    try {
      // 调用AI预览接口
      const result = await customizeApi.previewAiResult({
        imageUrl,
        style,
        size: { width: 20, height: 20 }
      })
      
      analysisResult.value = result
      
      // 模拟处理流程（实际应该用WebSocket）
      simulateProcess()
    } catch (error) {
      console.error('AI分析失败:', error)
      isAnalyzing.value = false
    }
  }
  
  // 模拟处理流程
  function simulateProcess() {
    // 语义分割
    setTimeout(() => {
      currentStep.value = 2
      analysisProgress.value = 20
      semanticStatus.value = '完成'
      
      // 针法匹配
      setTimeout(() => {
        currentStep.value = 3
        analysisProgress.value = 40
        stitchStatus.value = '完成'
        
        // 路径规划
        setTimeout(() => {
          currentStep.value = 4
          analysisProgress.value = 60
          pathStatus.value = '完成'
          
          // DST生成
          setTimeout(() => {
            currentStep.value = 5
            analysisProgress.value = 100
            dstStatus.value = '完成'
            dstFileUrl.value = '/downloads/design_pattern.dst'
            isAnalyzing.value = false
          }, 1500)
        }, 1500)
      }, 1500)
    }, 1500)
  }
  
  return {
    isAnalyzing,
    analysisProgress,
    currentStep,
    analysisResult,
    dstFileUrl,
    generatedImage,
    semanticStatus,
    stitchStatus,
    pathStatus,
    dstStatus,
    startAnalysis
  }
})
