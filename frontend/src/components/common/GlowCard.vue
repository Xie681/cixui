<template>
  <div 
    class="glow-card rounded-xl p-6"
    :class="{ 'hover:scale-105': hover }"
  >
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  hover?: boolean
}>()
</script>
