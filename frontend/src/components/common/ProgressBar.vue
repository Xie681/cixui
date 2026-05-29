<template>
  <div class="w-full">
    <div class="flex items-center justify-between mb-2">
      <span class="text-sm text-gray-300">{{ label }}</span>
      <span class="text-sm text-gray-400">{{ status }}</span>
    </div>
    <div class="w-full bg-white/10 rounded-full h-2 overflow-hidden">
      <div 
        :class="[
          'h-full transition-all duration-500 rounded-full',
          colorClass
        ]"
        :style="{ width: `${percentage}%` }"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps<{
  percentage: number
  label: string
  status?: string
  color?: 'primary' | 'cta' | 'vibrant' | 'secondary'
}>()

const colorClass = computed(() => {
  const colors = {
    primary: 'bg-accent-primary',
    cta: 'bg-accent-cta',
    vibrant: 'bg-accent-vibrant',
    secondary: 'bg-accent-secondary'
  }
  return colors[props.color || 'primary']
})
</script>
