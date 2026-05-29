<template>
  <div :id="containerId" class="particles"></div>
</template>

<script setup lang="ts">
import { onMounted, onUnmounted } from 'vue'
import { useParticles } from '@/composables/useParticles'

interface Props {
  containerId: string
  count?: number
}

const props = withDefaults(defineProps<Props>(), {
  count: 30
})

useParticles(props.containerId, props.count)
</script>
