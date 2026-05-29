<template>
  <div 
    v-for="(stream, index) in streams" 
    :key="index"
    class="data-stream"
    :style="{
      left: stream.left,
      animationDelay: stream.delay + 's'
    }"
  ></div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'

interface Stream {
  left: string
  delay: number
}

const streams = ref<Stream[]>([])

onMounted(() => {
  streams.value = [
    { left: '10%', delay: 0 },
    { left: '30%', delay: 1 },
    { left: '50%', delay: 2 },
    { left: '70%', delay: 0.5 },
    { left: '90%', delay: 1.5 }
  ]
})
</script>
