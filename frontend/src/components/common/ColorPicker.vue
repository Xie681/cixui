<template>
  <div class="glow-card rounded-xl p-6">
    <div class="flex items-center justify-between mb-4">
      <h4 class="text-lg font-heading font-semibold text-white">智能配色</h4>
      <button 
        @click="generateColorScheme"
        class="text-xs px-3 py-1 neon-button text-white rounded-lg transition-all duration-300 cursor-pointer"
      >
        AI 智能配色
      </button>
    </div>
    
    <div class="mb-4">
      <p class="text-sm text-gray-400 mb-3">选择颜色（可多选）：</p>
      <div class="flex flex-wrap gap-2">
        <button
          v-for="color in colorPalette"
          :key="color"
          @click="toggleColor(color)"
          :class="[
            'w-10 h-10 rounded-lg cursor-pointer border-2 transition-all duration-300',
            selectedColors.includes(color)
              ? 'border-white scale-110 shadow-lg'
              : 'border-transparent hover:border-white/50'
          ]"
          :style="{ backgroundColor: color }"
          :title="color"
        />
      </div>
    </div>
    
    <div v-if="aiColorSuggestions.length > 0" class="mt-4 p-3 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
      <p class="text-sm font-semibold text-gray-300 mb-2">AI推荐配色方案：</p>
      <div class="flex flex-wrap gap-2">
        <button
          v-for="color in aiColorSuggestions"
          :key="color"
          @click="selectAIColor(color)"
          class="w-10 h-10 rounded-lg cursor-pointer border-2 border-transparent hover:border-white transition-all duration-300"
          :style="{ backgroundColor: color }"
          :title="color"
        />
      </div>
    </div>
    
    <div v-if="selectedColors.length > 0" class="mt-4 p-3 bg-white/5 rounded-lg">
      <p class="text-sm text-gray-400 mb-2">已选择 {{ selectedColors.length }} 种颜色：</p>
      <div class="flex flex-wrap gap-2">
        <div
          v-for="color in selectedColors"
          :key="color"
          class="flex items-center gap-2 px-2 py-1 bg-black/30 rounded-lg"
        >
          <div 
            class="w-6 h-6 rounded border border-white/20"
            :style="{ backgroundColor: color }"
          />
          <span class="text-xs text-gray-300">{{ color }}</span>
          <button
            @click="removeColor(color)"
            class="text-gray-400 hover:text-white transition-colors duration-200"
          >
            <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>
    
    <button 
      @click="openColorLibrary"
      class="mt-4 w-full px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left text-gray-300 hover:text-white"
    >
      打开色卡库 →
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = defineProps<{
  colorPalette?: string[]
}>()

const emit = defineEmits<{
  (e: 'select', colors: string[]): void
  (e: 'generate'): void
}>()

const defaultColorPalette = [
  '#EF4444', '#3B82F6', '#10B981', '#8B5CF6',
  '#F59E0B', '#EC4899', '#06B6D4', '#F97316'
]

const colorPalette = computed(() => props.colorPalette || defaultColorPalette)
const selectedColors = ref<string[]>([])
const aiColorSuggestions = ref<string[]>([])

function toggleColor(color: string) {
  const index = selectedColors.value.indexOf(color)
  if (index > -1) {
    selectedColors.value.splice(index, 1)
  } else {
    selectedColors.value.push(color)
  }
  emit('select', selectedColors.value)
}

function removeColor(color: string) {
  const index = selectedColors.value.indexOf(color)
  if (index > -1) {
    selectedColors.value.splice(index, 1)
    emit('select', selectedColors.value)
  }
}

function selectAIColor(color: string) {
  if (!selectedColors.value.includes(color)) {
    selectedColors.value.push(color)
    emit('select', selectedColors.value)
  }
}

function generateColorScheme() {
  emit('generate')
  // TODO: 调用后端API生成配色方案
  // 目前使用模拟逻辑
  aiColorSuggestions.value = ['#DC2626', '#F59E0B', '#10B981', '#3B82F6']
}

function openColorLibrary() {
  alert('色卡库功能开发中...')
  // TODO: 打开色卡库弹窗
}
</script>
