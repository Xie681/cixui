<template>
  <div 
    v-for="(pattern, index) in patterns" 
    :key="index"
    class="traditional-pattern"
    :style="{
      top: pattern.top,
      left: pattern.left,
      bottom: pattern.bottom,
      right: pattern.right,
      animationDelay: pattern.delay + 's',
      transform: pattern.transform
    }"
  ></div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Pattern {
  top?: string
  left?: string
  bottom?: string
  right?: string
  delay: number
  transform?: string
}

const patterns = ref<Pattern[]>([])

onMounted(() => {
  patterns.value = [
    { top: '10px', left: '10px', delay: 0 },
    { bottom: '20px', right: '20px', delay: -15 },
    { top: '50%', left: '25%', delay: -10, transform: 'scale(0.8)' }
  ]
})
</script>
