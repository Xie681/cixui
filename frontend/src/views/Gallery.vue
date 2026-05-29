<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
    <!-- Style Gallery Section -->
    <section class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-15"></div>
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-gallery" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 传统花纹装饰 -->
      <TraditionalPattern />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/4 left-1/4 w-72 h-72 bg-accent-vibrant/15 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-1/4 right-1/4 w-96 h-96 bg-accent-primary/20 rounded-full blur-3xl float" style="animation-delay: 3s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">风格画廊</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-2xl mx-auto">
            探索丰富的刺绣风格库，从传统到现代，从简约到繁复
          </p>
        </div>
        
        <!-- Filter Tabs -->
        <div class="flex flex-wrap justify-center gap-4 mb-8">
          <button 
            @click="filterGallery('all')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-200 cursor-pointer',
              activeFilter === 'all' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            全部
          </button>
          <button 
            @click="filterGallery('traditional')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'traditional' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            传统
          </button>
          <button 
            @click="filterGallery('modern')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'modern' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            现代
          </button>
          <button 
            @click="filterGallery('abstract')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'abstract' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            抽象
          </button>
          <button 
            @click="filterGallery('floral')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'floral' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            花卉
          </button>
        </div>
        
        <!-- 四大名绣筛选 -->
        <div class="mb-8">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white text-center">按刺绣流派筛选</h3>
          <div class="flex flex-wrap justify-center gap-3">
            <button 
              @click="filterByEmbroideryStyle('suxiu')" 
              :class="[
                'px-4 py-2 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                selectedEmbroideryStyle === 'suxiu'
                  ? 'neon-button text-white'
                  : 'glow-card hover:bg-white/10 text-gray-300'
              ]"
            >
              <span class="flex items-center space-x-2">
                <span>苏绣</span>
                <span class="text-xs text-gray-400">苏州</span>
              </span>
            </button>
            <button 
              @click="filterByEmbroideryStyle('xiangxiu')" 
              :class="[
                'px-4 py-2 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                selectedEmbroideryStyle === 'xiangxiu'
                  ? 'neon-button text-white'
                  : 'glow-card hover:bg-white/10 text-gray-300'
              ]"
            >
              <span class="flex items-center space-x-2">
                <span>湘绣</span>
                <span class="text-xs text-gray-400">长沙</span>
              </span>
            </button>
            <button 
              @click="filterByEmbroideryStyle('yuexiu')" 
              :class="[
                'px-4 py-2 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                selectedEmbroideryStyle === 'yuexiu'
                  ? 'neon-button text-white'
                  : 'glow-card hover:bg-white/10 text-gray-300'
              ]"
            >
              <span class="flex items-center space-x-2">
                <span>粤绣</span>
                <span class="text-xs text-gray-400">广东</span>
              </span>
            </button>
            <button 
              @click="filterByEmbroideryStyle('shuxiu')" 
              :class="[
                'px-4 py-2 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                selectedEmbroideryStyle === 'shuxiu'
                  ? 'neon-button text-white'
                  : 'glow-card hover:bg-white/10 text-gray-300'
              ]"
            >
              <span class="flex items-center space-x-2">
                <span>蜀绣</span>
                <span class="text-xs text-gray-400">成都</span>
              </span>
            </button>
          </div>
        </div>
        
        <!-- 四大名绣展示区域 -->
        <div class="mb-12">
          <h3 class="text-2xl font-heading font-bold mb-6 text-center gradient-text">四大名绣流派</h3>
          <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            <!-- 苏绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20 rounded-lg mb-4 relative overflow-hidden">
                <div class="absolute inset-0 flex items-center justify-center">
                  <svg class="w-20 h-20 text-accent-primary/40" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                  </svg>
                </div>
                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-primary/80 rounded text-xs text-white font-semibold">双面绣</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">苏绣</h4>
              <p class="text-sm text-gray-400 mb-2">江苏苏州</p>
              <p class="text-xs text-gray-500 leading-relaxed">以"平、齐、细、密"著称，图案秀丽，色彩文雅。双面绣猫更是其代表作，神态逼真，毛色光泽。</p>
              <button @click="selectEmbroideryStyle('suxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 湘绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-cta/20 to-accent-vibrant/20 rounded-lg mb-4 relative overflow-hidden">
                <div class="absolute inset-0 flex items-center justify-center">
                  <svg class="w-20 h-20 text-accent-cta/40" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z"/>
                  </svg>
                </div>
                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-cta/80 rounded text-xs text-white font-semibold">鬅毛针</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">湘绣</h4>
              <p class="text-sm text-gray-400 mb-2">湖南长沙</p>
              <p class="text-xs text-gray-500 leading-relaxed">形象生动，色彩鲜艳，尤以"鬅毛针"绣出的猛虎闻名天下。质感强烈，威风凛凛，极具表现力。</p>
              <button @click="selectEmbroideryStyle('xiangxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 粤绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-vibrant/20 to-accent-primary/20 rounded-lg mb-4 relative overflow-hidden">
                <div class="absolute inset-0 flex items-center justify-center">
                  <svg class="w-20 h-20 text-accent-vibrant/40" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z"/>
                  </svg>
                </div>
                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-vibrant/80 rounded text-xs text-white font-semibold">浮雕效果</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">粤绣</h4>
              <p class="text-sm text-gray-400 mb-2">广东地区</p>
              <p class="text-xs text-gray-500 leading-relaxed">构图繁而不乱，色彩富丽堂皇。善用金银线垫高刺绣，产生浮雕般的立体效果，龙凤题材最为经典。</p>
              <button @click="selectEmbroideryStyle('yuexiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 蜀绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20 rounded-lg mb-4 relative overflow-hidden">
                <div class="absolute inset-0 flex items-center justify-center">
                  <svg class="w-20 h-20 text-accent-secondary/40" fill="currentColor" viewBox="0 0 24 24">
                    <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                  </svg>
                </div>
                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-secondary/80 rounded text-xs text-white font-semibold">晕针技法</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">蜀绣</h4>
              <p class="text-sm text-gray-400 mb-2">四川成都</p>
              <p class="text-xs text-gray-500 leading-relaxed">针法严谨，片线光亮。色彩明丽清秀，擅长表现鲤鱼和熊猫的灵动与憨态，具有浓郁的地方特色。</p>
              <button @click="selectEmbroideryStyle('shuxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
          </div>
        </div>
        
        <!-- Gallery Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          <div 
            v-for="item in galleryItems" 
            :key="item.id"
            class="glow-card rounded-xl overflow-hidden transition-all duration-300 cursor-pointer group hover:scale-105"
          >
            <div :class="['aspect-square relative overflow-hidden', item.gradient]">
              <div class="absolute inset-0 flex items-center justify-center">
                <svg class="w-24 h-24" :class="item.iconColor" fill="currentColor" viewBox="0 0 24 24">
                  <path :d="item.iconPath"/>
                </svg>
              </div>
              <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center">
                <button @click="addToCart(item)" class="px-4 py-2 bg-accent-primary text-white rounded-lg font-medium hover:bg-accent-secondary transition-colors duration-200 cursor-pointer">加入购物车</button>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-heading font-semibold mb-1">{{ item.name }}</h3>
              <p class="text-sm text-gray-400">{{ item.description }}</p>
              <div class="mt-3 flex items-center justify-between">
                <span class="text-accent-primary font-semibold">¥{{ item.price }}</span>
                <button class="text-gray-400 hover:text-white transition-colors duration-200 cursor-pointer">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="text-center mt-12">
          <button @click="$router.push('/tools')" class="px-8 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            查看更多风格
          </button>
        </div>
      </div>
    </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'
import DataStream from '@/components/effects/DataStream.vue'
import NeedleThread from '@/components/effects/NeedleThread.vue'
import TraditionalPattern from '@/components/effects/TraditionalPattern.vue'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const cartStore = useCartStore()
const activeFilter = ref('all')

interface GalleryItem {
  id: number
  name: string
  description: string
  price: number
  category: string
  embroideryStyle?: string
  gradient: string
  iconColor: string
  iconPath: string
}

const allGalleryItems: GalleryItem[] = [
  { id: 1, name: '传统花卉', description: '经典中式刺绣风格', price: 299, category: 'traditional', gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 2, name: '现代几何', description: '简约线条艺术', price: 199, category: 'modern', gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-vibrant/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 3, name: '抽象艺术', description: '创意无限可能', price: 399, category: 'abstract', gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-primary/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 4, name: '自然风景', description: '山水意境之美', price: 349, category: 'traditional', gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-cta/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 5, name: '文字艺术', description: '个性化文字定制', price: 149, category: 'modern', gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-vibrant/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 6, name: '动物主题', description: '生动可爱', price: 279, category: 'traditional', embroideryStyle: 'xiangxiu', gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-primary/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 7, name: '旗袍刺绣', description: '优雅传统', price: 459, category: 'traditional', embroideryStyle: 'suxiu', gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-secondary/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 8, name: '丝巾刺绣', description: '精致典雅', price: 329, category: 'traditional', embroideryStyle: 'yuexiu', gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 9, name: '花卉图案', description: '精美花卉设计', price: 259, category: 'floral', embroideryStyle: 'suxiu', gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-vibrant/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 10, name: '抽象线条', description: '现代抽象风格', price: 189, category: 'abstract', gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-secondary/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 11, name: '龙凤呈祥', description: '传统吉祥图案', price: 599, category: 'traditional', embroideryStyle: 'yuexiu', gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-primary/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 12, name: '熊猫主题', description: '可爱熊猫图案', price: 319, category: 'traditional', embroideryStyle: 'shuxiu', gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' }
]

const galleryItems = ref<GalleryItem[]>(allGalleryItems)
const selectedEmbroideryStyle = ref<string | null>(null)

function filterGallery(category: string) {
  activeFilter.value = category
  selectedEmbroideryStyle.value = null
  
  if (category === 'all') {
    galleryItems.value = allGalleryItems
  } else {
    galleryItems.value = allGalleryItems.filter(item => item.category === category)
  }
}

function filterByEmbroideryStyle(style: string) {
  selectedEmbroideryStyle.value = style
  activeFilter.value = 'all'
  galleryItems.value = allGalleryItems.filter(item => item.embroideryStyle === style)
}

function selectEmbroideryStyle(style: string) {
  filterByEmbroideryStyle(style)
}

function addToCart(item: GalleryItem) {
  const cartStore = useCartStore()
  cartStore.addItem({
    id: item.id.toString(),
    name: item.name,
    image: '',
    price: item.price
  })
}

// Particles 组件会自动初始化粒子效果
</script>
