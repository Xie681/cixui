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
                <h2 class="text-xl font-heading font-bold text-white">确认下单</h2>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <div class="grid md:grid-cols-3 gap-8">
            <!-- 左侧：订单详情 -->
            <div class="md:col-span-2 space-y-6">
              <!-- 订单信息 -->
              <div class="glow-card rounded-xl p-6">
                <h3 class="text-xl font-heading font-semibold mb-4 text-white">订单信息</h3>
                <div class="space-y-4">
                  <div 
                    v-for="item in cartItems" 
                    :key="item.id"
                    class="flex items-center space-x-4 p-4 glow-card rounded-lg"
                  >
                    <div class="w-16 h-16 bg-gradient-to-br from-accent-primary/20 to-accent-cta/20 rounded-lg flex items-center justify-center flex-shrink-0">
                      <svg class="w-8 h-8 text-accent-primary/40" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                      </svg>
                    </div>
                    <div class="flex-1">
                      <h4 class="font-semibold text-white">{{ item.name }}</h4>
                      <p class="text-sm text-gray-400">数量: {{ item.quantity }}</p>
                    </div>
                    <div class="text-right">
                      <p class="text-accent-primary font-semibold">¥{{ item.price * item.quantity }}</p>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 定制参数 -->
              <div class="glow-card rounded-xl p-6">
                <h3 class="text-xl font-heading font-semibold mb-4 text-white">定制参数</h3>
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">选择尺寸</label>
                    <select v-model="orderParams.size" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>10cm × 10cm</option>
                      <option>15cm × 15cm</option>
                      <option>20cm × 20cm</option>
                      <option>自定义尺寸</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">选择材质</label>
                    <select v-model="orderParams.material" class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>棉布</option>
                      <option>丝绸</option>
                      <option>麻布</option>
                      <option>其他</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">选择颜色</label>
                    <div class="flex flex-wrap gap-3">
                      <div 
                        v-for="color in colorOptions" 
                        :key="color"
                        :class="[
                          'w-12 h-12 rounded-lg cursor-pointer border-2 transition-all',
                          orderParams.color === color ? 'border-white scale-110' : 'border-transparent hover:border-white'
                        ]"
                        :style="{ backgroundColor: color }"
                        @click="orderParams.color = color"
                      ></div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 收货信息 -->
              <div class="glow-card rounded-xl p-6">
                <h3 class="text-xl font-heading font-semibold mb-4 text-white">收货信息</h3>
                <div class="space-y-4">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">收货人姓名</label>
                    <input 
                      v-model="shippingInfo.name" 
                      type="text" 
                      class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent" 
                      placeholder="请输入姓名"
                    >
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">联系电话</label>
                    <input 
                      v-model="shippingInfo.phone" 
                      type="tel" 
                      class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent" 
                      placeholder="请输入手机号"
                    >
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">收货地址</label>
                    <textarea 
                      v-model="shippingInfo.address" 
                      rows="3" 
                      class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent resize-none" 
                      placeholder="请输入详细地址"
                    ></textarea>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 右侧：订单汇总 -->
            <div class="md:col-span-1">
              <div class="glow-card rounded-xl p-6 sticky top-24">
                <h3 class="text-lg font-heading font-semibold mb-4 text-white">订单汇总</h3>
                <div class="space-y-3 mb-6">
                  <div class="flex justify-between text-sm">
                    <span class="text-gray-400">商品总额</span>
                    <span class="text-white">¥{{ subtotal }}</span>
                  </div>
                  <div class="flex justify-between text-sm">
                    <span class="text-gray-400">运费</span>
                    <span class="text-white">¥{{ shippingFee }}</span>
                  </div>
                  <div class="border-t border-white/10 pt-3 flex justify-between">
                    <span class="text-white font-semibold">总计</span>
                    <span class="text-xl font-heading font-bold gradient-text">¥{{ total }}</span>
                  </div>
                </div>
                <button @click="submitOrder" :disabled="!canSubmit" class="w-full neon-button px-6 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed">
                  提交订单
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { useCartStore } from '@/store/cart'

const router = useRouter()
const cartStore = useCartStore()

const cartItems = computed(() => cartStore.items)

const orderParams = ref({
  size: '10cm × 10cm',
  material: '棉布',
  color: '#EF4444'
})

const shippingInfo = ref({
  name: '',
  phone: '',
  address: ''
})

const colorOptions = ['#EF4444', '#3B82F6', '#10B981', '#8B5CF6']

const subtotal = computed(() => {
  return cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
})

const shippingFee = ref(15)

const total = computed(() => {
  return subtotal.value + shippingFee.value
})

const canSubmit = computed(() => {
  return shippingInfo.value.name && shippingInfo.value.phone && shippingInfo.value.address && cartItems.value.length > 0
})

function submitOrder() {
  if (!canSubmit.value) return
  
  // TODO: 调用API提交订单
  alert('订单提交成功！')
  cartStore.clearCart()
  router.push('/orders')
}
</script>
