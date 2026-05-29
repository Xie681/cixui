<template>
  <div class="smart-color-picker">
    <div class="color-section">
      <label class="block text-sm font-medium text-gray-300 mb-3">智能配色方案</label>
      
      <!-- AI推荐配色 -->
      <div v-if="aiSuggestions.length > 0" class="mb-4">
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs text-gray-400">AI智能推荐</span>
          <button @click="generateAIColors" class="text-xs text-accent-primary hover:text-accent-cta transition-colors cursor-pointer">
            <svg class="w-4 h-4 inline" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            重新生成
          </button>
        </div>
        <div class="flex flex-wrap gap-2 mb-3">
          <div 
            v-for="(color, index) in aiSuggestions" 
            :key="`ai-${index}`"
            :class="[
              'w-10 h-10 rounded-lg cursor-pointer border-2 transition-all relative group',
              selectedColors.includes(color) ? 'border-white scale-110' : 'border-transparent hover:border-white/50'
            ]"
            :style="{ backgroundColor: color }"
            @click="toggleColor(color)"
          >
            <div class="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity">
              <svg v-if="selectedColors.includes(color)" class="w-5 h-5 text-white" fill="currentColor" viewBox="0 0 20 20">
                <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
              </svg>
            </div>
          </div>
        </div>
      </div>
      
      <!-- 传统配色方案 -->
      <div class="mb-4">
        <span class="text-xs text-gray-400 block mb-2">传统配色方案</span>
        <div class="space-y-2">
          <div 
            v-for="scheme in traditionalSchemes" 
            :key="scheme.name"
            @click="applyScheme(scheme.colors)"
            class="flex items-center space-x-2 p-2 glow-card rounded-lg cursor-pointer hover:bg-white/5 transition-all"
          >
            <div class="flex space-x-1">
              <div 
                v-for="color in scheme.colors" 
                :key="color"
                class="w-6 h-6 rounded border border-white/20"
                :style="{ backgroundColor: color }"
              ></div>
            </div>
            <span class="text-xs text-gray-300 flex-1">{{ scheme.name }}</span>
          </div>
        </div>
      </div>
      
      <!-- 自定义颜色 -->
      <div>
        <div class="flex items-center justify-between mb-2">
          <span class="text-xs text-gray-400">自定义颜色</span>
          <button @click="addCustomColor" class="text-xs text-accent-primary hover:text-accent-cta transition-colors cursor-pointer">
            + 添加
          </button>
        </div>
        <div class="flex flex-wrap gap-2">
          <div 
            v-for="(color, index) in selectedColors" 
            :key="`selected-${index}`"
            :class="[
              'w-10 h-10 rounded-lg cursor-pointer border-2 transition-all relative group',
              'border-white scale-110'
            ]"
            :style="{ backgroundColor: color }"
            @click="removeColor(index)"
          >
            <div class="absolute inset-0 flex items-center justify-center bg-black/50 opacity-0 group-hover:opacity-100 transition-opacity rounded-lg">
              <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </div>
          </div>
          
          <!-- 颜色选择器 -->
          <div 
            v-if="showColorPicker"
            class="w-10 h-10 rounded-lg border-2 border-dashed border-accent-primary/50 cursor-pointer flex items-center justify-center hover:border-accent-primary transition-colors"
            @click="showColorPicker = false"
          >
            <input 
              v-model="customColor"
              type="color"
              @change="addColorFromPicker"
              class="w-full h-full opacity-0 cursor-pointer"
            />
            <svg class="w-5 h-5 text-accent-primary absolute pointer-events-none" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
            </svg>
          </div>
        </div>
      </div>
      
      <!-- 颜色分析 -->
      <div v-if="selectedColors.length > 0" class="mt-4 pt-4 border-t border-white/10">
        <div class="text-xs text-gray-400 mb-2">配色分析</div>
        <div class="space-y-1 text-xs">
          <div class="flex justify-between">
            <span class="text-gray-400">颜色数量:</span>
            <span class="text-white">{{ selectedColors.length }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-400">色系:</span>
            <span class="text-white">{{ colorSchemeType }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-gray-400">对比度:</span>
            <span class="text-white">{{ contrastLevel }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'

const props = defineProps<{
  modelValue: string[]
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string[]]
}>()

const selectedColors = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})

const aiSuggestions = ref<string[]>([])
const showColorPicker = ref(false)
const customColor = ref('#8B5CF6')

const traditionalSchemes = [
  {
    name: '传统中国红',
    colors: ['#DC2626', '#F59E0B', '#FCD34D', '#FFFFFF']
  },
  {
    name: '青花瓷',
    colors: ['#1E40AF', '#3B82F6', '#60A5FA', '#DBEAFE']
  },
  {
    name: '水墨画',
    colors: ['#1F2937', '#4B5563', '#9CA3AF', '#E5E7EB']
  },
  {
    name: '花鸟画',
    colors: ['#10B981', '#34D399', '#F59E0B', '#FCD34D']
  },
  {
    name: '宫廷金',
    colors: ['#F59E0B', '#FCD34D', '#FDE047', '#FEF3C7']
  }
]

// 生成AI推荐配色
function generateAIColors() {
  // 模拟AI生成配色方案
  const colorPalettes = [
    ['#8B5CF6', '#06B6D4', '#10B981', '#F59E0B'],
    ['#EC4899', '#F472B6', '#FBCFE8', '#FDF2F8'],
    ['#3B82F6', '#60A5FA', '#93C5FD', '#DBEAFE'],
    ['#10B981', '#34D399', '#6EE7B7', '#D1FAE5'],
    ['#F59E0B', '#FBBF24', '#FCD34D', '#FEF3C7']
  ]
  
  const randomPalette = colorPalettes[Math.floor(Math.random() * colorPalettes.length)]
  aiSuggestions.value = randomPalette
}

function toggleColor(color: string) {
  const index = selectedColors.value.indexOf(color)
  if (index > -1) {
    selectedColors.value = selectedColors.value.filter(c => c !== color)
  } else {
    selectedColors.value = [...selectedColors.value, color]
  }
}

function removeColor(index: number) {
  selectedColors.value = selectedColors.value.filter((_, i) => i !== index)
}

function addCustomColor() {
  showColorPicker.value = true
}

function addColorFromPicker() {
  if (customColor.value && !selectedColors.value.includes(customColor.value)) {
    selectedColors.value = [...selectedColors.value, customColor.value]
  }
  showColorPicker.value = false
}

function applyScheme(colors: string[]) {
  selectedColors.value = [...colors]
}

// 计算色系类型
const colorSchemeType = computed(() => {
  if (selectedColors.value.length === 0) return '-'
  
  const hues = selectedColors.value.map(color => {
    const r = parseInt(color.slice(1, 3), 16)
    const g = parseInt(color.slice(3, 5), 16)
    const b = parseInt(color.slice(5, 7), 16)
    const max = Math.max(r, g, b)
    const min = Math.min(r, g, b)
    const diff = max - min
    
    if (diff === 0) return 'neutral'
    const hue = max === r ? ((g - b) / diff) % 6 : 
                max === g ? (b - r) / diff + 2 : 
                (r - g) / diff + 4
    return Math.round(hue * 60)
  })
  
  const avgHue = hues.reduce((a, b) => a + b, 0) / hues.length
  
  if (avgHue < 30 || avgHue > 330) return '红色系'
  if (avgHue < 90) return '黄色系'
  if (avgHue < 150) return '绿色系'
  if (avgHue < 210) return '青色系'
  if (avgHue < 270) return '蓝色系'
  if (avgHue < 330) return '紫色系'
  return '中性色'
})

// 计算对比度
const contrastLevel = computed(() => {
  if (selectedColors.value.length < 2) return '-'
  
  // 简化对比度计算
  const brightnesses = selectedColors.value.map(color => {
    const r = parseInt(color.slice(1, 3), 16)
    const g = parseInt(color.slice(3, 5), 16)
    const b = parseInt(color.slice(5, 7), 16)
    return (r * 299 + g * 587 + b * 114) / 1000
  })
  
  const maxBrightness = Math.max(...brightnesses)
  const minBrightness = Math.min(...brightnesses)
  const contrast = (maxBrightness - minBrightness) / 255
  
  if (contrast > 0.7) return '高'
  if (contrast > 0.4) return '中'
  return '低'
})

// 初始化时生成AI配色
watch(() => selectedColors.value.length, () => {
  if (selectedColors.value.length === 0 && aiSuggestions.value.length === 0) {
    generateAIColors()
  }
}, { immediate: true })

// 初始化
generateAIColors()
</script>

<style scoped>
.smart-color-picker {
  @apply w-full;
}

.color-section {
  @apply space-y-4;
}
</style>
