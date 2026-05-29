<template>
  <button 
    class="neon-button px-6 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer"
    :class="sizeClass"
    @click="$emit('click')"
  >
    <slot />
  </button>
</template>

<script setup lang="ts">
import { computed } from 'vue'

const props = withDefaults(defineProps<{
  size?: 'sm' | 'md' | 'lg'
}>(), {
  size: 'md'
})

defineEmits<{
  (e: 'click'): void
}>()

const sizeClass = computed(() => {
  const sizes = {
    sm: 'px-4 py-2 text-sm',
    md: 'px-6 py-3 text-base',
    lg: 'px-8 py-4 text-lg'
  }
  return sizes[props.size]
})
</script>
