<template>
  <nav class="fixed top-0 left-0 right-0 z-50 glass-dark border-b border-accent-primary/20">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-16">
        <div class="flex items-center space-x-2">
          <svg class="w-8 h-8 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
          </svg>
          <RouterLink to="/" class="text-xl font-heading font-bold text-white">数字绣娘</RouterLink>
        </div>
        
        <!-- Desktop Navigation -->
        <div class="hidden md:flex items-center space-x-8">
          <a href="#features" @click="scrollToSection('#features')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">功能特色</a>
          <a href="#gallery" @click="scrollToSection('#gallery')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">风格画廊</a>
          <a href="#tools" @click="scrollToSection('#tools')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">创作工具</a>
          <a href="#luxury-custom" @click="scrollToSection('#luxury-custom')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">高端定制</a>
          <a href="#knowledge-graph" @click="scrollToSection('#knowledge-graph')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">刺绣GPT</a>
          <a href="#order" @click="scrollToSection('#order')" class="text-gray-300 hover:text-white transition-colors duration-200 cursor-pointer">定制下单</a>
        </div>
        
        <div class="flex items-center space-x-4">
          <!-- 购物车按钮 -->
          <button @click="openCart" class="relative p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
            </svg>
            <span v-if="cartCount > 0" class="absolute -top-1 -right-1 w-5 h-5 bg-accent-primary rounded-full text-xs flex items-center justify-center font-semibold pulse-glow">{{ cartCount }}</span>
          </button>
          <button @click="startCreation" class="hidden sm:block px-4 py-2 bg-accent-primary hover:bg-accent-secondary text-white rounded-lg transition-colors duration-200 cursor-pointer font-medium">
            开始创作
          </button>
          <!-- Mobile Menu Button -->
          <button @click="toggleMobileMenu" class="md:hidden p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
            <svg v-if="!mobileMenuOpen" class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
            <svg v-else class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
          <!-- Desktop User Button -->
          <div class="hidden md:block relative">
            <button @click="toggleUserMenu" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
              <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
            </button>
            
            <!-- User Menu Dropdown -->
            <div 
              v-if="userMenuOpen"
              class="absolute top-full right-0 mt-2 w-64 glow-card rounded-xl p-4 shadow-2xl z-50"
            >
              <div v-if="!isLoggedIn" class="space-y-3">
                <h3 class="text-lg font-heading font-semibold text-white mb-4">登录</h3>
                
                <!-- 错误提示 -->
                <div v-if="loginError" class="text-red-400 text-sm bg-red-500/10 border border-red-500/20 rounded-lg px-3 py-2">
                  {{ loginError }}
                </div>
                
                <input 
                  v-model="loginUsername"
                  type="text" 
                  class="w-full glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                  placeholder="手机号/邮箱"
                  :disabled="isLoading"
                />
                <input 
                  v-model="loginPassword"
                  type="password" 
                  class="w-full glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                  placeholder="密码"
                  :disabled="isLoading"
                  @keyup.enter="handleLogin"
                />
                <button 
                  @click="handleLogin" 
                  :disabled="isLoading"
                  class="w-full neon-button px-4 py-2 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  {{ isLoading ? '登录中...' : '登录' }}
                </button>
                <div class="text-center">
                  <a href="#" class="text-sm text-gray-400 hover:text-white transition-colors duration-200 cursor-pointer">注册账号</a>
                </div>
              </div>
              
              <div v-else class="space-y-3">
                <div class="flex items-center space-x-3 pb-3 border-b border-white/10">
                  <div class="w-10 h-10 bg-gradient-to-br from-accent-primary to-accent-cta rounded-full flex items-center justify-center">
                    <span class="text-white font-semibold">{{ userInitial }}</span>
                  </div>
                  <div>
                    <p class="text-white font-semibold text-sm">{{ userName }}</p>
                    <p class="text-xs text-gray-400">{{ userEmail }}</p>
                  </div>
                </div>
                <RouterLink to="/profile" @click="closeUserMenu" class="block px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-white">
                  <span class="flex items-center space-x-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    <span>个人中心</span>
                  </span>
                </RouterLink>
                <RouterLink to="/cart" @click="closeUserMenu" class="block px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-white">
                  <span class="flex items-center space-x-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <span>购物车</span>
                    <span class="ml-auto px-2 py-0.5 bg-accent-primary rounded-full text-xs">{{ cartCount }}</span>
                  </span>
                </RouterLink>
                <RouterLink to="/orders" @click="closeUserMenu" class="block px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-white">
                  <span class="flex items-center space-x-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
                    </svg>
                    <span>订单管理</span>
                  </span>
                </RouterLink>
                <button @click="handleLogout" class="w-full px-3 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-white text-left">
                  <span class="flex items-center space-x-2">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                    </svg>
                    <span>退出登录</span>
                  </span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Mobile Menu -->
    <div v-if="mobileMenuOpen" class="md:hidden glass-dark border-t border-accent-primary/20">
      <div class="px-4 py-4 space-y-3">
        <a href="#features" @click="scrollToSection('#features'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">功能特色</a>
        <a href="#gallery" @click="scrollToSection('#gallery'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">风格画廊</a>
        <a href="#tools" @click="scrollToSection('#tools'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">创作工具</a>
        <a href="#luxury-custom" @click="scrollToSection('#luxury-custom'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">高端定制</a>
        <a href="#knowledge-graph" @click="scrollToSection('#knowledge-graph'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">刺绣GPT</a>
        <a href="#order" @click="scrollToSection('#order'); closeMobileMenu()" class="block px-4 py-2 text-gray-300 hover:text-white hover:bg-white/5 rounded-lg transition-colors duration-200 cursor-pointer">定制下单</a>
        <button @click="startCreation" class="w-full px-4 py-2 bg-accent-primary hover:bg-accent-secondary text-white rounded-lg transition-colors duration-200 cursor-pointer font-medium">
          开始创作
        </button>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import { useCartStore } from '@/store/cart'
import { useUserStore } from '@/store/user'
import { onClickOutside } from '@vueuse/core'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const mobileMenuOpen = ref(false)
const userMenuOpen = ref(false)
const loginUsername = ref('')
const loginPassword = ref('')
const loginError = ref('')
const isLoading = ref(false)

// 从userStore获取登录状态和用户信息
const isLoggedIn = computed(() => userStore.isLoggedIn)
const userName = computed(() => userStore.userInfo?.nickname || '用户')
const userEmail = computed(() => userStore.userInfo?.email || userStore.userInfo?.phone || '')
const cartCount = computed(() => cartStore.items.length)
const userInitial = computed(() => userName.value.charAt(0).toUpperCase())

// 组件挂载时检查登录状态
onMounted(async () => {
  if (userStore.isLoggedIn && !userStore.userInfo) {
    try {
      await userStore.fetchProfile()
    } catch (error) {
      console.error('获取用户信息失败:', error)
    }
  }
})

const toggleMobileMenu = () => {
  mobileMenuOpen.value = !mobileMenuOpen.value
}

const closeMobileMenu = () => {
  mobileMenuOpen.value = false
}

const openCart = () => {
  // 打开购物车全屏页面
  const event = new CustomEvent('openPage', { detail: 'cart-page' })
  window.dispatchEvent(event)
}

const scrollToSection = (selector: string) => {
  const element = document.querySelector(selector)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

const startCreation = () => {
  router.push('/ai-generation')
  closeMobileMenu()
}

const toggleUserMenu = () => {
  userMenuOpen.value = !userMenuOpen.value
}

const closeUserMenu = () => {
  userMenuOpen.value = false
}

// 真实的登录逻辑 - 调用后端接口
const handleLogin = async () => {
  if (!loginUsername.value || !loginPassword.value) {
    loginError.value = '请输入账号和密码'
    return
  }

  isLoading.value = true
  loginError.value = ''

  try {
    // 调用userStore的login方法
    await userStore.login(loginUsername.value, loginPassword.value)
    // 登录成功
    closeUserMenu()
    loginUsername.value = ''
    loginPassword.value = ''
    console.log('登录成功')
  } catch (error: any) {
    // 登录失败，显示错误信息
    loginError.value = error.message || '登录失败，请检查账号和密码'
    console.error('登录失败:', error)
  } finally {
    isLoading.value = false
  }
}

const handleLogout = async () => {
  try {
    await userStore.logout()
    closeUserMenu()
    router.push('/')
  } catch (error) {
    console.error('登出失败:', error)
  }
}

// 点击外部关闭菜单
const userMenuRef = ref<HTMLElement>()
onClickOutside(userMenuRef, () => {
  userMenuOpen.value = false
})
</script>
