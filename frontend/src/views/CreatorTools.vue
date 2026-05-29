<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <!-- Creator Tools Section -->
      <section class="py-20 bg-black relative overflow-hidden">
        <!-- 统一背景效果 -->
        <div class="absolute inset-0" style="background: radial-gradient(ellipse at center, rgba(6,182,212,0.1) 0%, transparent 70%);"></div>
        <div class="absolute inset-0 embroidery-pattern opacity-10"></div>
        
        <!-- 传统花纹装饰 -->
        <TraditionalPattern />
        
        <!-- 动态粒子效果 -->
        <Particles container-id="particles-tools" :count="20" />
        
        <!-- 数据流效果 -->
        <DataStream />
        
        <!-- 针线装饰 -->
        <NeedleThread />
        
        <!-- 霓虹光晕 -->
        <div class="absolute top-1/2 left-1/3 w-80 h-80 bg-accent-cta/20 rounded-full blur-3xl float" style="animation-delay: 1.5s;"></div>
        <div class="absolute bottom-1/3 right-1/3 w-64 h-64 bg-accent-vibrant/15 rounded-full blur-3xl float" style="animation-delay: 4s;"></div>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
          <div class="text-center mb-16">
            <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
              <span class="gradient-text">创作者工具</span>
            </h2>
            <p class="text-xl text-gray-300 max-w-2xl mx-auto">
              专业级创作工具，让您的创意从设计到生产无缝衔接
            </p>
          </div>
          
          <div class="grid md:grid-cols-3 gap-8">
            <!-- Tool 1: 图案编辑器 -->
            <div @click="goToEditor" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
              <!-- 装饰性刺绣图案 -->
              <div class="absolute top-4 right-4 w-20 h-20 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <path d="M50,10 Q70,30 50,50 Q30,30 50,10" stroke="#8B5CF6" stroke-width="2" fill="none"/>
                  <path d="M50,50 Q70,70 50,90 Q30,70 50,50" stroke="#06B6D4" stroke-width="2" fill="none"/>
                </svg>
              </div>
              <div class="w-16 h-16 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
                <svg class="w-8 h-8 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                </svg>
              </div>
              <h3 class="text-2xl font-heading font-semibold mb-3 text-white">图案编辑器</h3>
              <p class="text-gray-300 mb-4">强大的在线编辑工具，支持图层、路径调整、颜色填充等专业功能</p>
              <ul class="space-y-2 text-sm text-gray-400">
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  多图层编辑
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  矢量路径调整
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  实时预览
                </li>
              </ul>
            </div>
            
            <!-- Tool 2: AI 智能体 -->
            <div @click="goToAIGeneration" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
              <!-- AI神经网络装饰 -->
              <div class="absolute top-4 right-4 w-24 h-24 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <circle cx="30" cy="30" r="3" fill="#06B6D4"/>
                  <circle cx="70" cy="30" r="3" fill="#10B981"/>
                  <circle cx="50" cy="70" r="3" fill="#8B5CF6"/>
                  <line x1="30" y1="30" x2="50" y2="70" stroke="#06B6D4" stroke-width="1"/>
                  <line x1="70" y1="30" x2="50" y2="70" stroke="#10B981" stroke-width="1"/>
                  <line x1="30" y1="30" x2="70" y2="30" stroke="#8B5CF6" stroke-width="1"/>
                </svg>
              </div>
              <div class="w-16 h-16 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
                <svg class="w-8 h-8 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
              </div>
              <h3 class="text-2xl font-heading font-semibold mb-3 text-white">AI 智能体</h3>
              <p class="text-gray-300 mb-4">接入先进的 AI 刺绣大模型，智能分析、优化和生成刺绣方案</p>
              <ul class="space-y-2 text-sm text-gray-400">
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  智能路径规划
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  针法自动优化
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  材料用量估算
                </li>
              </ul>
            </div>
            
            <!-- Tool 3: 生产协同 -->
            <div @click="goToCollaboration" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
              <!-- 机械齿轮装饰 -->
              <div class="absolute top-4 right-4 w-20 h-20 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <circle cx="50" cy="50" r="30" stroke="#10B981" stroke-width="2" fill="none"/>
                  <circle cx="50" cy="50" r="10" fill="#10B981"/>
                  <rect x="45" y="20" width="10" height="15" fill="#10B981"/>
                  <rect x="45" y="65" width="10" height="15" fill="#10B981"/>
                  <rect x="20" y="45" width="15" height="10" fill="#10B981"/>
                  <rect x="65" y="45" width="15" height="10" fill="#10B981"/>
                </svg>
              </div>
              <div class="w-16 h-16 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
                <svg class="w-8 h-8 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
              </div>
              <h3 class="text-2xl font-heading font-semibold mb-3 text-white">生产协同</h3>
              <p class="text-gray-300 mb-4">连接实体刺绣机，实现从设计到生产的全链路数字化联动</p>
              <ul class="space-y-2 text-sm text-gray-400">
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  设备远程控制
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  实时生产监控
                </li>
                <li class="flex items-center">
                  <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                  </svg>
                  三方数字协作
                </li>
              </ul>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'
import DataStream from '@/components/effects/DataStream.vue'
import NeedleThread from '@/components/effects/NeedleThread.vue'
import TraditionalPattern from '@/components/effects/TraditionalPattern.vue'

const router = useRouter()

function goToEditor() {
  // TODO: 跳转到图案编辑器页面
  router.push('/ai-generation')
}

function goToAIGeneration() {
  router.push('/ai-generation')
}

function goToCollaboration() {
  router.push('/collaboration')
}
</script>
