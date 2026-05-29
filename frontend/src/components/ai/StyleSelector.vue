<template>
  <div class="glow-card rounded-xl p-6">
    <h4 class="text-lg font-heading font-semibold mb-4 text-white">选择刺绣风格</h4>
    <p class="text-sm text-gray-400 mb-4">选择四大名绣流派之一，AI将根据所选风格生成图案</p>
    
    <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
      <button
        v-for="style in styles"
        :key="style.id"
        @click="selectStyle(style.id)"
        :class="[
          'p-4 rounded-xl transition-all duration-300 cursor-pointer border-2',
          selectedStyleId === style.id
            ? 'bg-accent-primary/20 border-accent-primary glow-card'
            : 'glow-card hover:bg-white/5 border-transparent hover:border-accent-primary/30'
        ]"
      >
        <div class="text-center">
          <div 
            :class="[
              'w-16 h-16 mx-auto mb-3 rounded-full flex items-center justify-center text-2xl font-bold transition-all duration-300',
              selectedStyleId === style.id
                ? 'bg-accent-primary/30 text-accent-primary scale-110'
                : 'bg-white/5 text-gray-400'
            ]"
          >
            {{ style.name.charAt(0) }}
          </div>
          <h5 class="font-semibold text-white mb-1">{{ style.name }}</h5>
          <p class="text-xs text-gray-400">{{ style.location }}</p>
        </div>
      </button>
    </div>
    
    <div v-if="selectedStyle" class="mt-4 p-3 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
      <p class="text-sm text-gray-300">
        <span class="font-semibold text-accent-primary">已选择：</span>
        {{ selectedStyle.name }}（{{ selectedStyle.location }}）
      </p>
      <p class="text-xs text-gray-400 mt-1">{{ selectedStyle.description }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'

interface EmbroideryStyle {
  id: string
  name: string
  location: string
  description: string
}

const props = defineProps<{
  styles?: EmbroideryStyle[]
}>()

const emit = defineEmits<{
  (e: 'select', styleId: string | null): void
}>()

const defaultStyles: EmbroideryStyle[] = [
  {
    id: 'suxiu',
    name: '苏绣',
    location: '江苏苏州',
    description: '以精细、雅洁著称，色彩清雅，针法细腻，适合表现花鸟、人物等题材'
  },
  {
    id: 'xiangxiu',
    name: '湘绣',
    location: '湖南长沙',
    description: '以写实见长，色彩鲜艳，层次分明，擅长表现山水、花鸟等自然题材'
  },
  {
    id: 'yuexiu',
    name: '粤绣',
    location: '广东地区',
    description: '色彩浓郁，构图饱满，装饰性强，适合表现吉祥图案和传统纹样'
  },
  {
    id: 'shuxiu',
    name: '蜀绣',
    location: '四川成都',
    description: '以细腻、平整著称，色彩丰富，针法多样，适合表现花鸟、虫鱼等题材'
  }
]

const styles = computed(() => props.styles || defaultStyles)
const selectedStyleId = ref<string | null>(null)

const selectedStyle = computed(() => {
  return selectedStyleId.value 
    ? styles.value.find(s => s.id === selectedStyleId.value) || null
    : null
})

function selectStyle(styleId: string) {
  if (selectedStyleId.value === styleId) {
    selectedStyleId.value = null
    emit('select', null)
  } else {
    selectedStyleId.value = styleId
    emit('select', styleId)
  }
}
</script>
