<template>
  <div class="min-h-screen pt-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 class="text-4xl font-heading font-bold gradient-text mb-12 text-center">高端定制</h1>
      
      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <GlowCard 
          hover 
          class="cursor-pointer"
          @click="$router.push('/luxury/replica')"
        >
          <h3 class="text-2xl font-heading font-semibold text-white mb-4">奢侈品复刻</h3>
          <p class="text-gray-400 mb-4">一比一复刻奢侈品刺绣</p>
        </GlowCard>
        
        <GlowCard 
          hover 
          class="cursor-pointer"
          @click="$router.push('/luxury/artifact')"
        >
          <h3 class="text-2xl font-heading font-semibold text-white mb-4">文物复刻</h3>
          <p class="text-gray-400 mb-4">文物数字化复刻</p>
        </GlowCard>
        
        <GlowCard 
          hover 
          class="cursor-pointer"
          @click="$router.push('/luxury/dragon-robe')"
        >
          <h3 class="text-2xl font-heading font-semibold text-white mb-4">龙袍定制</h3>
          <p class="text-gray-400 mb-4">传统龙袍定制</p>
        </GlowCard>
        
        <GlowCard 
          hover 
          class="cursor-pointer"
          @click="$router.push('/luxury/traditional-clothing')"
        >
          <h3 class="text-2xl font-heading font-semibold text-white mb-4">传统服装定制</h3>
          <p class="text-gray-400 mb-4">汉服、旗袍、少数民族服饰</p>
        </GlowCard>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import GlowCard from '@/components/common/GlowCard.vue'
</script>
