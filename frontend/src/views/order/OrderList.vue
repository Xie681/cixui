<template>
  <div class="min-h-screen bg-black text-gray-100 antialiased pt-16">
    <Navbar />
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <h1 class="text-4xl font-heading font-bold gradient-text mb-8 text-center">我的订单</h1>
      
      <!-- 筛选和搜索 -->
      <div class="mb-6 flex flex-wrap items-center gap-3">
        <select 
          v-model="selectedStatus"
          @change="handleStatusChange"
          class="glow-card rounded-lg px-4 py-2 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer"
        >
          <option value="all">全部状态</option>
          <option value="PENDING">待支付</option>
          <option value="PAID">已支付</option>
          <option value="PROCESSING">处理中</option>
          <option value="COMPLETED">已完成</option>
          <option value="CANCELLED">已取消</option>
        </select>
        <input 
          v-model="searchQuery"
          type="text" 
          placeholder="搜索订单号..." 
          class="flex-1 min-w-[200px] glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent"
        />
      </div>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-block w-8 h-8 border-4 border-accent-primary border-t-transparent rounded-full animate-spin"></div>
        <p class="text-gray-400 mt-4">加载中...</p>
      </div>
      
      <!-- 订单列表 -->
      <div v-else class="space-y-4">
        <div 
          v-for="order in filteredOrders" 
          :key="order.id"
          class="glow-card rounded-xl p-6 cursor-pointer hover:bg-white/5 transition-all duration-300"
          @click="viewOrderDetail(order.id)"
        >
          <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            <div class="flex-1">
              <div class="flex items-center gap-4 mb-3">
                <h3 class="text-lg font-heading font-semibold text-white">订单 #{{ order.orderNo }}</h3>
                <span 
                  :class="getStatusClass(order.status)"
                  class="px-3 py-1 rounded-full text-xs font-medium"
                >
                  {{ order.statusDesc }}
                </span>
                <span v-if="order.isUrgent" class="px-2 py-1 bg-red-500/20 text-red-400 rounded text-xs">
                  加急
                </span>
              </div>
              <div class="grid grid-cols-2 md:grid-cols-4 gap-4 text-sm text-gray-400">
                <div>
                  <span class="text-gray-500">下单时间：</span>
                  <span>{{ formatDate(order.createdAt) }}</span>
                </div>
                <div>
                  <span class="text-gray-500">订单类型：</span>
                  <span>{{ order.type === 'IMAGE' ? '图片定制' : '文字定制' }}</span>
                </div>
                <div>
                  <span class="text-gray-500">订单金额：</span>
                  <span class="text-white font-semibold">¥{{ order.totalAmount }}</span>
                </div>
                <div v-if="order.quantity">
                  <span class="text-gray-500">数量：</span>
                  <span>{{ order.quantity }} 件</span>
                </div>
              </div>
              <div v-if="order.summary" class="mt-2 text-sm text-gray-400">
                {{ order.summary }}
              </div>
            </div>
            <div class="flex items-center gap-3">
              <button 
                @click.stop="viewOrderDetail(order.id)"
                class="px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm transition-all duration-300 cursor-pointer"
              >
                查看详情
              </button>
              <button 
                v-if="order.status === 'PENDING' || order.status === 'PAID'"
                @click.stop="cancelOrder(order.id)"
                class="px-4 py-2 glow-card hover:bg-red-500/20 text-red-400 rounded-lg text-sm transition-all duration-300 cursor-pointer"
              >
                取消订单
              </button>
            </div>
          </div>
        </div>
        
        <div v-if="filteredOrders.length === 0" class="glow-card rounded-xl p-12 text-center">
          <svg class="w-16 h-16 mx-auto mb-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
          </svg>
          <p class="text-gray-400 mb-2">暂无订单</p>
          <RouterLink to="/gallery" class="text-accent-primary hover:text-accent-secondary transition-colors duration-200">
            去逛逛 →
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { orderApi } from '@/services/api/order'
import type { OrderVO } from '@/services/api/order'

const router = useRouter()

const selectedStatus = ref('all')
const searchQuery = ref('')
const orders = ref<OrderVO[]>([])
const loading = ref(false)

// 加载订单列表
async function loadOrders() {
  loading.value = true
  try {
    const status = selectedStatus.value === 'all' ? undefined : selectedStatus.value
    orders.value = await orderApi.listOrders(status)
  } catch (error) {
    console.error('加载订单列表失败:', error)
  } finally {
    loading.value = false
  }
}

const filteredOrders = computed(() => {
  let result = orders.value

  // 搜索筛选
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(order => 
      order.orderNo.toLowerCase().includes(query) ||
      order.summary?.toLowerCase().includes(query)
    )
  }

  return result
})

function getStatusClass(status: string) {
  const classes: Record<string, string> = {
    'PENDING': 'bg-yellow-500/20 text-yellow-400',
    'PAID': 'bg-blue-500/20 text-blue-400',
    'PROCESSING': 'bg-blue-500/20 text-blue-400',
    'COMPLETED': 'bg-green-500/20 text-green-400',
    'CANCELLED': 'bg-red-500/20 text-red-400'
  }
  return classes[status] || 'bg-gray-500/20 text-gray-400'
}

function formatDate(dateString: string) {
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function viewOrderDetail(orderId: number) {
  router.push(`/orders/${orderId}`)
}

async function cancelOrder(orderId: number) {
  if (!confirm('确定要取消这个订单吗？')) return
  
  try {
    await orderApi.cancelOrder(orderId)
    alert('订单已取消')
    await loadOrders()
  } catch (error) {
    console.error('取消订单失败:', error)
    alert('取消订单失败，请重试')
  }
}

// 监听状态变化，重新加载订单
function handleStatusChange() {
  loadOrders()
}

onMounted(() => {
  loadOrders()
})
</script>
