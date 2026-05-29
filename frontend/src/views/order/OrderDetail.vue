<template>
  <div class="min-h-screen bg-black text-gray-100 antialiased pt-16">
    <Navbar />
    
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <button 
        @click="router.back()"
        class="mb-6 flex items-center gap-2 text-gray-400 hover:text-white transition-colors duration-200 cursor-pointer"
      >
        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
        </svg>
        返回订单列表
      </button>
      
      <h1 class="text-4xl font-heading font-bold gradient-text mb-8">订单详情</h1>
      
      <!-- 加载状态 -->
      <div v-if="loading" class="text-center py-12">
        <div class="inline-block w-8 h-8 border-4 border-accent-primary border-t-transparent rounded-full animate-spin"></div>
        <p class="text-gray-400 mt-4">加载中...</p>
      </div>
      
      <div v-else-if="order" class="space-y-6">
        <!-- 订单信息卡片 -->
        <div class="glow-card rounded-xl p-6">
          <div class="flex items-center justify-between mb-6">
            <h2 class="text-2xl font-heading font-semibold text-white">订单 #{{ order.orderNo }}</h2>
            <div class="flex items-center gap-2">
              <span 
                :class="getStatusClass(order.status)"
                class="px-4 py-2 rounded-full text-sm font-medium"
              >
                {{ order.statusDesc }}
              </span>
              <span v-if="order.isUrgent" class="px-3 py-1 bg-red-500/20 text-red-400 rounded-full text-sm">
                加急
              </span>
            </div>
          </div>
          
          <div class="grid md:grid-cols-2 gap-6 text-sm">
            <div>
              <p class="text-gray-500 mb-1">下单时间</p>
              <p class="text-white">{{ formatDate(order.createdAt) }}</p>
            </div>
            <div v-if="order.paidAt">
              <p class="text-gray-500 mb-1">支付时间</p>
              <p class="text-white">{{ formatDate(order.paidAt) }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1">订单类型</p>
              <p class="text-white">{{ order.typeDesc }}</p>
            </div>
            <div>
              <p class="text-gray-500 mb-1">订单金额</p>
              <p class="text-white text-xl font-semibold gradient-text">¥{{ order.totalAmount }}</p>
            </div>
            <div v-if="order.addressSnapshot" class="md:col-span-2">
              <p class="text-gray-500 mb-1">收货信息</p>
              <p class="text-white">
                {{ order.addressSnapshot.name }} {{ order.addressSnapshot.phone }}
              </p>
              <p class="text-gray-400 text-sm mt-1">
                {{ order.addressSnapshot.province }}{{ order.addressSnapshot.city }}{{ order.addressSnapshot.district }}{{ order.addressSnapshot.detail }}
              </p>
            </div>
            <div v-if="order.remark" class="md:col-span-2">
              <p class="text-gray-500 mb-1">备注</p>
              <p class="text-white">{{ order.remark }}</p>
            </div>
          </div>
        </div>
        
        <!-- 定制详情 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white">定制详情</h3>
          <div class="grid md:grid-cols-2 gap-6">
            <div v-if="order.sourceImage">
              <p class="text-gray-500 mb-2">原图</p>
              <img :src="order.sourceImage" class="w-full rounded-lg" alt="原图">
            </div>
            <div v-if="order.resultImageUrl">
              <p class="text-gray-500 mb-2">效果图</p>
              <img :src="order.resultImageUrl" class="w-full rounded-lg" alt="效果图">
            </div>
          </div>
          
          <div class="grid md:grid-cols-3 gap-4 mt-6 text-sm">
            <div v-if="order.embroideryStyle">
              <p class="text-gray-500 mb-1">刺绣风格</p>
              <p class="text-white">{{ order.styleDesc }}</p>
            </div>
            <div v-if="order.width && order.height">
              <p class="text-gray-500 mb-1">尺寸</p>
              <p class="text-white">{{ order.width }} × {{ order.height }} cm</p>
            </div>
            <div v-if="order.quantity">
              <p class="text-gray-500 mb-1">数量</p>
              <p class="text-white">{{ order.quantity }} 件</p>
            </div>
            <div v-if="order.fabricType">
              <p class="text-gray-500 mb-1">面料</p>
              <p class="text-white">{{ order.fabricType }}</p>
            </div>
            <div v-if="order.threadType">
              <p class="text-gray-500 mb-1">绣线</p>
              <p class="text-white">{{ order.threadType }}</p>
            </div>
            <div v-if="order.stitchCount">
              <p class="text-gray-500 mb-1">针数</p>
              <p class="text-white">{{ order.stitchCount }}</p>
            </div>
            <div v-if="order.colorCount">
              <p class="text-gray-500 mb-1">颜色数</p>
              <p class="text-white">{{ order.colorCount }}</p>
            </div>
          </div>
        </div>
        
        <!-- 费用明细 -->
        <div class="glow-card rounded-xl p-6">
          <h3 class="text-xl font-heading font-semibold mb-4 text-white">费用明细</h3>
          <div class="space-y-3">
            <div class="flex justify-between items-center">
              <span class="text-gray-400">基础价格</span>
              <span class="text-white">¥{{ order.basePrice }}</span>
            </div>
            <div v-if="order.stitchFee > 0" class="flex justify-between items-center">
              <span class="text-gray-400">针数费用</span>
              <span class="text-white">¥{{ order.stitchFee }}</span>
            </div>
            <div v-if="order.colorFee > 0" class="flex justify-between items-center">
              <span class="text-gray-400">颜色费用</span>
              <span class="text-white">¥{{ order.colorFee }}</span>
            </div>
            <div v-if="order.fabricFee > 0" class="flex justify-between items-center">
              <span class="text-gray-400">面料费用</span>
              <span class="text-white">¥{{ order.fabricFee }}</span>
            </div>
            <div v-if="order.threadFee > 0" class="flex justify-between items-center">
              <span class="text-gray-400">绣线费用</span>
              <span class="text-white">¥{{ order.threadFee }}</span>
            </div>
            <div v-if="order.urgentFee > 0" class="flex justify-between items-center">
              <span class="text-gray-400">加急费用</span>
              <span class="text-red-400">¥{{ order.urgentFee }}</span>
            </div>
            <div class="flex justify-between items-center pt-4 border-t border-white/10">
              <span class="text-white font-semibold text-lg">总计</span>
              <span class="text-xl font-heading font-bold gradient-text">¥{{ order.totalAmount }}</span>
            </div>
          </div>
        </div>
        
        <!-- 操作按钮 -->
        <div class="flex gap-4">
          <button 
            v-if="order.status === 'PENDING' || order.status === 'PAID'"
            @click="cancelOrder(order.id)"
            class="px-6 py-3 glow-card hover:bg-red-500/20 text-red-400 rounded-lg font-medium transition-all duration-300 cursor-pointer"
          >
            取消订单
          </button>
          <button 
            v-if="order.status === 'COMPLETED'"
            @click="downloadDST(order.id)"
            class="px-6 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer"
          >
            下载 DST 文件
          </button>
        </div>
      </div>
      
      <div v-else class="glow-card rounded-xl p-12 text-center">
        <p class="text-gray-400 mb-4">订单不存在</p>
        <button 
          @click="router.push('/orders')"
          class="px-6 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer"
        >
          返回订单列表
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { orderApi } from '@/services/api/order'
import type { OrderDetailVO } from '@/services/api/order'

const router = useRouter()
const route = useRoute()
const order = ref<OrderDetailVO | null>(null)
const loading = ref(false)

// 加载订单详情
async function loadOrderDetail() {
  loading.value = true
  try {
    const orderId = parseInt(route.params.id as string)
    order.value = await orderApi.getOrderDetail(orderId)
  } catch (error) {
    console.error('加载订单详情失败:', error)
    order.value = null
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadOrderDetail()
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

async function cancelOrder(orderId: number) {
  if (!confirm('确定要取消这个订单吗？')) return
  
  try {
    await orderApi.cancelOrder(orderId)
    alert('订单已取消')
    await loadOrderDetail()
  } catch (error) {
    console.error('取消订单失败:', error)
    alert('取消订单失败，请重试')
  }
}

function downloadDST(orderId: number) {
  alert('DST 文件下载功能开发中...')
  // TODO: 实现 DST 文件下载
}
</script>
