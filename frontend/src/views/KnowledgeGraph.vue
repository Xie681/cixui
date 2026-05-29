<template>
  <div class="min-h-screen pt-16 bg-black relative overflow-hidden">
    <!-- 背景效果 -->
    <div class="absolute inset-0 embroidery-pattern opacity-15"></div>
    <div class="absolute top-1/3 left-1/3 w-96 h-96 bg-accent-cta/20 rounded-full blur-3xl float"></div>
    <div class="absolute bottom-1/3 right-1/3 w-80 h-80 bg-accent-primary/15 rounded-full blur-3xl float" style="animation-delay: 4s;"></div>
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12 relative z-10">
      <div class="text-center mb-12">
        <h1 class="text-4xl md:text-5xl font-heading font-bold mb-6">
          <span class="gradient-text">知识图谱</span>
        </h1>
        <p class="text-xl md:text-2xl text-gray-300 max-w-4xl mx-auto leading-relaxed mb-4">
          传承中华传统优秀文化，普及AI技术知识<br>
          让千年刺绣工艺在数字时代焕发新活力
        </p>
        <!-- RAG技术标识 -->
        <div class="inline-flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-purple-500/20 via-pink-500/20 to-cyan-500/20 rounded-full border border-purple-400/30 mt-4">
          <svg class="w-5 h-5 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
          </svg>
          <span class="text-sm font-semibold text-purple-300">EmbroideryGPT · 基于刺绣知识图谱的RAG</span>
        </div>
      </div>
      
      <GlowCard class="relative overflow-hidden">
        <!-- 背景装饰 -->
        <div class="absolute inset-0 opacity-10">
          <div class="absolute top-0 left-0 w-64 h-64 bg-purple-500 rounded-full blur-3xl"></div>
          <div class="absolute bottom-0 right-0 w-64 h-64 bg-cyan-500 rounded-full blur-3xl"></div>
        </div>
        
        <div class="relative z-10 text-center py-12">
          <h2 class="text-2xl md:text-3xl font-heading font-semibold mb-6 text-white">
            <span class="bg-gradient-to-r from-purple-400 via-pink-400 to-cyan-400 bg-clip-text text-transparent">刺绣知识图谱</span>
          </h2>
          <p class="text-gray-400 mb-8 max-w-2xl mx-auto">
            EmbroideryGPT基于刺绣知识图谱的RAG技术，整合传统刺绣工艺知识与现代AI技术，为您提供智能化的刺绣设计辅助。
          </p>
          <button @click="$router.push('/')" class="neon-button px-8 py-3 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer">
            返回首页探索知识图谱
          </button>
        </div>
      </GlowCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import GlowCard from '@/components/common/GlowCard.vue'
</script>
