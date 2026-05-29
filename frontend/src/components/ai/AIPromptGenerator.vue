<template>
  <div class="glow-card rounded-xl p-6">
    <div class="flex items-center justify-between mb-4">
      <h4 class="text-lg font-heading font-semibold text-white">AI 智能提示词生成</h4>
      <button 
        @click="generatePrompt"
        :disabled="!canGenerate"
        class="text-xs px-3 py-1 neon-button text-white rounded-lg transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
      >
        生成提示词
      </button>
    </div>
    
    <textarea 
      v-model="inputText"
      rows="4" 
      class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent resize-none mb-3"
      placeholder="例如：我想要一个传统中式花卉图案，颜色以红色和金色为主，适合刺绣在丝绸上..."
    />
    
    <div v-if="generatedPrompt" class="mt-4 p-4 glow-card rounded-lg bg-black/30">
      <div class="flex items-center justify-between mb-2">
        <span class="text-sm font-semibold text-gray-300">生成的提示词：</span>
        <button 
          @click="copyPrompt"
          class="text-xs px-2 py-1 glow-card hover:bg-white/10 rounded text-gray-400 hover:text-white transition-all duration-300 cursor-pointer"
        >
          复制
        </button>
      </div>
      <p class="text-sm text-gray-300 whitespace-pre-wrap">{{ generatedPrompt }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  selectedStyle?: { id: string; name: string; location: string } | null
  fontStyle?: string
}>()

const emit = defineEmits<{
  (e: 'generated', prompt: string): void
}>()

const inputText = ref('')
const generatedPrompt = ref('')
const isGenerating = ref(false)

const canGenerate = computed(() => {
  return inputText.value.trim().length > 0 && !isGenerating.value
})

async function generatePrompt() {
  if (!canGenerate.value) return
  
  isGenerating.value = true
  
  try {
    // TODO: 调用后端API生成提示词
    // 目前使用模拟逻辑
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    let prompt = `基于您的文字"${inputText.value}"，AI为您生成的专业刺绣提示词：\n\n`
    prompt += `【文字内容】${inputText.value}\n\n`
    
    if (props.fontStyle) {
      prompt += `【字体风格】${props.fontStyle}\n\n`
    }
    
    if (props.selectedStyle) {
      prompt += `【刺绣流派】${props.selectedStyle.name}（${props.selectedStyle.location}）\n\n`
    }
    
    prompt += `【设计建议】传统中式风格，适合刺绣工艺，色彩搭配和谐，图案层次丰富，适合${props.selectedStyle?.name || '通用'}刺绣风格`
    
    generatedPrompt.value = prompt
    emit('generated', prompt)
  } catch (error) {
    console.error('生成提示词失败:', error)
    alert('生成提示词失败，请重试')
  } finally {
    isGenerating.value = false
  }
}

function copyPrompt() {
  if (generatedPrompt.value) {
    navigator.clipboard.writeText(generatedPrompt.value).then(() => {
      alert('提示词已复制到剪贴板')
    }).catch(() => {
      alert('复制失败，请手动复制')
    })
  }
}
</script>
