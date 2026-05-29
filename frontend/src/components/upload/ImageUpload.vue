<template>
  <div 
    class="border-2 border-dashed border-accent-primary/30 rounded-xl relative overflow-hidden cursor-pointer hover:border-accent-primary/60 transition-all duration-300"
    :class="aspectClass"
    @click="triggerUpload"
    @dragover.prevent="handleDragOver"
    @dragleave.prevent="handleDragLeave"
    @drop.prevent="handleDrop"
  >
    <input 
      ref="fileInput"
      type="file" 
      class="hidden" 
      :accept="accept"
      @change="handleFileChange"
    />
    
    <div v-if="!previewUrl" class="absolute inset-0 flex flex-col items-center justify-center">
      <svg class="w-16 h-16 mb-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
      </svg>
      <p class="text-gray-300 mb-2">{{ hint }}</p>
      <p class="text-sm text-gray-500">{{ subHint }}</p>
    </div>
    
    <div v-else class="absolute inset-0">
      <img :src="previewUrl" class="w-full h-full object-contain rounded-xl" alt="预览">
      <button 
        @click.stop="removePreview"
        class="absolute top-2 right-2 w-8 h-8 bg-black/70 hover:bg-black/90 rounded-full flex items-center justify-center transition-all duration-300"
      >
        <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

const props = withDefaults(defineProps<{
  aspect?: 'square' | 'video' | 'auto'
  accept?: string
  hint?: string
  subHint?: string
}>(), {
  aspect: 'square',
  accept: 'image/*',
  hint: '拖拽图片到此处',
  subHint: '或点击选择文件'
})

const emit = defineEmits<{
  (e: 'upload', file: File): void
  (e: 'remove'): void
}>()

const fileInput = ref<HTMLInputElement>()
const previewUrl = ref<string>('')
const isDragging = ref(false)

const aspectClass = computed(() => {
  if (props.aspect === 'square') return 'aspect-square'
  if (props.aspect === 'video') return 'aspect-video'
  return ''
})

function triggerUpload() {
  fileInput.value?.click()
}

function handleFileChange(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  if (file) {
    handleFile(file)
  }
}

function handleFile(file: File) {
  const reader = new FileReader()
  reader.onload = (e) => {
    previewUrl.value = e.target?.result as string
  }
  reader.readAsDataURL(file)
  emit('upload', file)
}

function removePreview() {
  previewUrl.value = ''
  if (fileInput.value) {
    fileInput.value.value = ''
  }
  emit('remove')
}

function handleDragOver() {
  isDragging.value = true
}

function handleDragLeave() {
  isDragging.value = false
}

function handleDrop(event: DragEvent) {
  isDragging.value = false
  const file = event.dataTransfer?.files[0]
  if (file) {
    handleFile(file)
  }
}
</script>
