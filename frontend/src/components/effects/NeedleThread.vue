<template>
  <div 
    v-for="(thread, index) in threads" 
    :key="index"
    class="needle-thread"
    :style="{
      top: thread.top,
      left: thread.left,
      right: thread.right,
      animationDelay: thread.delay + 's',
      transform: thread.transform
    }"
  ></div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Thread {
  top: string
  left?: string
  right?: string
  delay: number
  transform?: string
}

const threads = ref<Thread[]>([])

onMounted(() => {
  threads.value = [
    { top: '20%', left: '15%', delay: 0 },
    { top: '60%', right: '20%', delay: 2, transform: 'scaleX(-1)' }
  ]
})
</script>
