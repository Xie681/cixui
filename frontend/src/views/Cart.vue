<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <div class="min-h-screen relative">
        <!-- 背景效果 -->
        <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
        
        <!-- 顶部导航栏 -->
        <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
              <div class="flex items-center space-x-4">
                <button @click="$router.back()" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                </button>
                <h2 class="text-xl font-heading font-bold text-white">购物车</h2>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <!-- 购物车为空 -->
          <div v-if="cartItems.length === 0" class="text-center py-20">
            <svg class="w-24 h-24 mx-auto mb-4 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <p class="text-gray-400 text-lg mb-2">购物车是空的</p>
            <p class="text-gray-500 text-sm mb-6">快去挑选您喜欢的刺绣图案吧！</p>
            <button @click="$router.push('/gallery')" class="neon-button px-6 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
              浏览风格库
            </button>
          </div>
          
          <!-- 购物车列表 -->
          <div v-else class="space-y-4 mb-8">
            <div 
              v-for="item in cartItems" 
              :key="item.id"
              class="glow-card rounded-xl p-6 flex items-center space-x-6"
            >
              <div class="w-24 h-24 bg-gradient-to-br from-accent-primary/20 to-accent-cta/20 rounded-lg flex items-center justify-center flex-shrink-0">
                <svg class="w-12 h-12 text-accent-primary/40" fill="currentColor" viewBox="0 0 24 24">
                  <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                </svg>
              </div>
              <div class="flex-1">
                <h3 class="text-lg font-heading font-semibold text-white mb-1">{{ item.name }}</h3>
                <p class="text-sm text-gray-400 mb-2">{{ item.description || '精美刺绣图案' }}</p>
                <p class="text-accent-primary font-semibold">¥{{ item.price }}</p>
              </div>
              <div class="flex items-center space-x-4">
                <div class="flex items-center space-x-2">
                  <button @click="updateQuantity(item.id, item.quantity - 1)" class="w-8 h-8 glow-card hover:bg-white/10 rounded-lg flex items-center justify-center transition-all duration-300 cursor-pointer">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                    </svg>
                  </button>
                  <span class="w-12 text-center text-white font-medium">{{ item.quantity }}</span>
                  <button @click="updateQuantity(item.id, item.quantity + 1)" class="w-8 h-8 glow-card hover:bg-white/10 rounded-lg flex items-center justify-center transition-all duration-300 cursor-pointer">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                    </svg>
                  </button>
                </div>
                <button @click="removeItem(item.id)" class="p-2 glow-card hover:bg-red-500/20 rounded-lg transition-all duration-300 cursor-pointer">
                  <svg class="w-5 h-5 text-red-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
          
          <!-- 购物车汇总 -->
          <div v-if="cartItems.length > 0" class="glow-card rounded-xl p-6 sticky bottom-0">
            <div class="flex items-center justify-between mb-6">
              <span class="text-xl font-heading font-semibold text-white">总计</span>
              <span class="text-3xl font-heading font-bold gradient-text">¥{{ totalPrice }}</span>
            </div>
            <button @click="checkout" class="w-full neon-button px-6 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer">
              去结算
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const cartStore = useCartStore()

const cartItems = computed(() => cartStore.items)

const totalPrice = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

function updateQuantity(id: string, quantity: number) {
  if (quantity <= 0) {
    cartStore.removeItem(id)
  } else {
    cartStore.updateQuantity(id, quantity)
  }
}

function removeItem(id: string) {
  cartStore.removeItem(id)
}

function checkout() {
  router.push('/checkout')
}
</script>
