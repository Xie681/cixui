<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <div class="min-h-screen relative">
        <!-- 背景效果 -->
        <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
        <Particles container-id="particles-production" :count="20" />
        
        <!-- 顶部导航栏 -->
        <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
              <div class="flex items-center space-x-4">
                <button @click="goHome" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                </button>
                <h2 class="text-xl font-heading font-bold text-white">生产协同</h2>
              </div>
              <div class="flex items-center space-x-4">
                <div class="flex items-center space-x-2">
                  <div class="w-2 h-2 bg-accent-vibrant rounded-full animate-pulse"></div>
                  <span class="text-sm text-gray-400">实时同步中</span>
                </div>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <!-- 生产协同说明卡片 -->
          <div class="mb-8 glow-card rounded-xl p-6 bg-gradient-to-r from-accent-vibrant/10 via-accent-primary/10 to-accent-cta/10 border border-accent-vibrant/30">
            <div class="flex items-start space-x-4">
              <div class="flex-shrink-0 w-14 h-14 bg-accent-vibrant/20 rounded-xl flex items-center justify-center">
                <svg class="w-7 h-7 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
                </svg>
              </div>
              <div class="flex-1">
                <h3 class="text-xl font-heading font-bold mb-2 gradient-text">生产协同平台</h3>
                <p class="text-sm text-gray-300 mb-4">连接实体刺绣机，实现从设计到生产的全链路数字化联动</p>
                <div class="grid md:grid-cols-3 gap-4">
                  <div class="p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <svg class="w-5 h-5 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
                      </svg>
                      <h4 class="text-white font-semibold">设备远程控制</h4>
                    </div>
                    <p class="text-xs text-gray-400">远程操控刺绣机，实时调整参数</p>
                  </div>
                  <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                      </svg>
                      <h4 class="text-white font-semibold">实时进度监控</h4>
                    </div>
                    <p class="text-xs text-gray-400">实时查看生产进度和状态</p>
                  </div>
                  <div class="p-4 bg-accent-cta/10 rounded-lg border border-accent-cta/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <svg class="w-5 h-5 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                      </svg>
                      <h4 class="text-white font-semibold">B端生产管理</h4>
                    </div>
                    <p class="text-xs text-gray-400">企业级生产管理和调度</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="grid lg:grid-cols-3 gap-6">
            <!-- 左侧：设备列表 -->
            <div class="lg:col-span-1">
              <div class="glow-card rounded-xl p-6 sticky top-24">
                <h3 class="text-lg font-heading font-semibold mb-4 text-white">设备列表</h3>
                <div class="space-y-3">
                  <div 
                    v-for="device in devices" 
                    :key="device.id"
                    @click="selectDevice(device.id)"
                    :class="[
                      'p-4 glow-card hover:bg-white/10 rounded-lg cursor-pointer transition-all duration-300',
                      selectedDeviceId === device.id ? 'border-2 border-accent-vibrant' : ''
                    ]"
                  >
                    <div class="flex items-center justify-between mb-2">
                      <h4 class="font-medium text-white">{{ device.name }}</h4>
                      <div :class="[
                        'w-2 h-2 rounded-full',
                        device.status === 'online' ? 'bg-accent-vibrant animate-pulse' : 'bg-gray-500'
                      ]"></div>
                    </div>
                    <p class="text-xs text-gray-400 mb-1">{{ device.model }}</p>
                    <p class="text-xs text-gray-500">{{ device.location }}</p>
                    <div v-if="device.status === 'working'" class="mt-2">
                      <div class="flex items-center justify-between text-xs text-gray-400 mb-1">
                        <span>进度</span>
                        <span>{{ device.progress }}%</span>
                      </div>
                      <div class="w-full bg-gray-700 rounded-full h-1.5">
                        <div 
                          class="bg-accent-vibrant h-1.5 rounded-full transition-all duration-300"
                          :style="{ width: device.progress + '%' }"
                        ></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 中间：实时监控 -->
            <div class="lg:col-span-2">
              <div class="glow-card rounded-xl p-6">
                <div class="flex items-center justify-between mb-6">
                  <h3 class="text-xl font-heading font-semibold text-white">实时监控</h3>
                  <div class="flex items-center space-x-2">
                    <div class="w-2 h-2 bg-accent-vibrant rounded-full animate-pulse"></div>
                    <span class="text-sm text-gray-400">实时同步</span>
                  </div>
                </div>
                
                <!-- 监控画面 -->
                <div class="aspect-video bg-gradient-to-br from-gray-900/50 to-gray-800/50 rounded-xl relative overflow-hidden border-2 border-accent-vibrant/30 mb-6">
                  <canvas ref="monitorCanvasRef" class="w-full h-full"></canvas>
                  <div v-if="!selectedDeviceId" class="absolute inset-0 flex flex-col items-center justify-center">
                    <svg class="w-20 h-20 mb-4 text-accent-vibrant/40" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 3v2m6-2v2M9 19v2m6-2v2M5 9H3m2 6H3m18-6h-2m2 6h-2M7 19h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2zM9 9h6v6H9V9z" />
                    </svg>
                    <p class="text-sm text-gray-500">请选择设备查看实时监控</p>
                  </div>
                  <div v-else class="absolute top-4 left-4 bg-black/80 text-white px-4 py-2 rounded-lg text-sm">
                    <div class="flex items-center space-x-2">
                      <div class="w-2 h-2 bg-accent-vibrant rounded-full animate-pulse"></div>
                      <span>设备 {{ selectedDevice?.name }} - {{ selectedDevice?.status === 'working' ? '工作中' : '待机中' }}</span>
                    </div>
                  </div>
                </div>
                
                <!-- 控制面板 -->
                <div v-if="selectedDeviceId" class="grid md:grid-cols-3 gap-4">
                  <div class="p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
                    <div class="flex items-center justify-between mb-2">
                      <span class="text-sm text-gray-400">当前速度</span>
                      <span class="text-lg font-semibold text-white">{{ deviceParams.speed }} rpm</span>
                    </div>
                    <input 
                      v-model.number="deviceParams.speed" 
                      type="range" 
                      min="100" 
                      max="1000" 
                      step="50"
                      @input="updateDeviceParams"
                      class="w-full"
                    />
                    <p class="text-xs text-gray-500 mt-1">调整刺绣机运行速度</p>
                  </div>
                  <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
                    <div class="flex items-center justify-between mb-2">
                      <span class="text-sm text-gray-400">针距</span>
                      <span class="text-lg font-semibold text-white">{{ deviceParams.stitchLength }}mm</span>
                    </div>
                    <input 
                      v-model.number="deviceParams.stitchLength" 
                      type="range" 
                      min="0.5" 
                      max="5" 
                      step="0.1"
                      @input="updateDeviceParams"
                      class="w-full"
                    />
                    <p class="text-xs text-gray-500 mt-1">控制针脚间距</p>
                  </div>
                  <div class="p-4 bg-accent-cta/10 rounded-lg border border-accent-cta/30">
                    <div class="flex items-center justify-between mb-2">
                      <span class="text-sm text-gray-400">张力</span>
                      <span class="text-lg font-semibold text-white">{{ deviceParams.tension }}</span>
                    </div>
                    <input 
                      v-model.number="deviceParams.tension" 
                      type="range" 
                      min="1" 
                      max="10" 
                      step="1"
                      @input="updateDeviceParams"
                      class="w-full"
                    />
                    <p class="text-xs text-gray-500 mt-1">调整绣线张力</p>
                  </div>
                </div>
                
                <!-- 设备连接状态 -->
                <div v-if="selectedDeviceId" class="mb-6 p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
                  <div class="flex items-center justify-between">
                    <div class="flex items-center space-x-3">
                      <div :class="[
                        'w-3 h-3 rounded-full',
                        selectedDevice?.status === 'online' || selectedDevice?.status === 'working' 
                          ? 'bg-accent-vibrant animate-pulse' 
                          : 'bg-gray-500'
                      ]"></div>
                      <div>
                        <p class="text-sm font-semibold text-white">连接状态</p>
                        <p class="text-xs text-gray-400">
                          {{ selectedDevice?.status === 'online' ? '设备在线' : 
                             selectedDevice?.status === 'working' ? '设备工作中' : 
                             selectedDevice?.status === 'offline' ? '设备离线' : '未知状态' }}
                        </p>
                      </div>
                    </div>
                    <button 
                      @click="connectDevice"
                      :disabled="selectedDevice?.status === 'online' || selectedDevice?.status === 'working'"
                      class="px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      {{ selectedDevice?.status === 'offline' ? '连接设备' : '已连接' }}
                    </button>
                  </div>
                </div>
                
                <!-- 操作按钮 -->
                <div v-if="selectedDeviceId" class="mt-6 flex flex-wrap gap-3">
                  <button 
                    @click="startDevice"
                    :disabled="selectedDevice?.status === 'working'"
                    class="px-6 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    启动设备
                  </button>
                  <button 
                    @click="pauseDevice"
                    :disabled="selectedDevice?.status !== 'working'"
                    class="px-6 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                  >
                    暂停
                  </button>
                  <button 
                    @click="stopDevice"
                    class="px-6 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer"
                  >
                    停止
                  </button>
                  <button 
                    @click="resetDevice"
                    class="px-6 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer"
                  >
                    重置
                  </button>
                </div>
              </div>
              
              <!-- 生产任务列表 -->
              <div class="mt-6 glow-card rounded-xl p-6">
                <h3 class="text-lg font-heading font-semibold mb-4 text-white">生产任务</h3>
                <div class="space-y-3">
                  <div 
                    v-for="task in productionTasks" 
                    :key="task.id"
                    class="p-4 bg-gray-800/50 rounded-lg border border-gray-700"
                  >
                    <div class="flex items-center justify-between mb-2">
                      <h4 class="font-medium text-white">{{ task.name }}</h4>
                      <span :class="[
                        'px-2 py-1 rounded text-xs',
                        task.status === 'completed' ? 'bg-accent-vibrant/20 text-accent-vibrant' :
                        task.status === 'processing' ? 'bg-accent-primary/20 text-accent-primary' :
                        'bg-gray-700 text-gray-400'
                      ]">
                        {{ task.status === 'completed' ? '已完成' : task.status === 'processing' ? '进行中' : '待处理' }}
                      </span>
                    </div>
                    <p class="text-sm text-gray-400 mb-2">{{ task.description }}</p>
                    <div v-if="task.status === 'processing'" class="mt-2">
                      <div class="flex items-center justify-between text-xs text-gray-400 mb-1">
                        <span>进度</span>
                        <span>{{ task.progress }}%</span>
                      </div>
                      <div class="w-full bg-gray-700 rounded-full h-1.5">
                        <div 
                          class="bg-accent-primary h-1.5 rounded-full transition-all duration-300"
                          :style="{ width: task.progress + '%' }"
                        ></div>
                      </div>
                    </div>
                    <div class="mt-2 flex items-center justify-between text-xs text-gray-500">
                      <span>设备: {{ task.deviceName }}</span>
                      <span>预计完成: {{ task.estimatedTime }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'

const router = useRouter()
const monitorCanvasRef = ref<HTMLCanvasElement | null>(null)
const selectedDeviceId = ref<number | null>(null)

function goHome() {
  router.push('/')
}

interface Device {
  id: number
  name: string
  model: string
  location: string
  status: 'online' | 'offline' | 'working'
  progress?: number
}

interface ProductionTask {
  id: number
  name: string
  description: string
  status: 'pending' | 'processing' | 'completed'
  progress?: number
  deviceName: string
  estimatedTime: string
}

const devices = ref<Device[]>([
  { id: 1, name: '刺绣机-001', model: 'Brother PR1000e', location: '生产车间A', status: 'working', progress: 65 },
  { id: 2, name: '刺绣机-002', model: 'Brother PR1000e', location: '生产车间A', status: 'online', progress: 0 },
  { id: 3, name: '刺绣机-003', model: 'Janome MB-4', location: '生产车间B', status: 'online', progress: 0 },
  { id: 4, name: '刺绣机-004', model: 'Janome MB-4', location: '生产车间B', status: 'offline', progress: 0 }
])

const productionTasks = ref<ProductionTask[]>([
  { id: 1, name: '订单#ORD-2024-001', description: '传统花卉图案刺绣', status: 'processing', progress: 65, deviceName: '刺绣机-001', estimatedTime: '2小时' },
  { id: 2, name: '订单#ORD-2024-002', description: '现代几何图案刺绣', status: 'pending', deviceName: '刺绣机-002', estimatedTime: '3小时' },
  { id: 3, name: '订单#ORD-2024-003', description: '抽象艺术图案刺绣', status: 'completed', progress: 100, deviceName: '刺绣机-003', estimatedTime: '已完成' }
])

const deviceParams = ref({
  speed: 500,
  stitchLength: 2.5,
  tension: 5
})

const selectedDevice = computed(() => {
  return devices.value.find(d => d.id === selectedDeviceId.value)
})

function selectDevice(deviceId: number) {
  selectedDeviceId.value = deviceId
  initMonitor()
}

function startDevice() {
  if (selectedDevice.value) {
    selectedDevice.value.status = 'working'
    selectedDevice.value.progress = 0
    // 模拟进度更新
    const interval = setInterval(() => {
      if (selectedDevice.value && selectedDevice.value.progress !== undefined) {
        selectedDevice.value.progress += 1
        if (selectedDevice.value.progress >= 100) {
          selectedDevice.value.progress = 100
          clearInterval(interval)
        }
      }
    }, 1000)
  }
}

function pauseDevice() {
  if (selectedDevice.value && selectedDevice.value.status === 'working') {
    // 暂停逻辑 - 保持状态但停止进度更新
    const currentProgress = selectedDevice.value.progress || 0
    selectedDevice.value.status = 'online'
    selectedDevice.value.progress = currentProgress
  }
}

function stopDevice() {
  if (selectedDevice.value) {
    selectedDevice.value.status = 'online'
    selectedDevice.value.progress = 0
    updateDeviceParams()
  }
}

function resetDevice() {
  if (selectedDevice.value) {
    selectedDevice.value.status = 'online'
    selectedDevice.value.progress = 0
    deviceParams.value = {
      speed: 500,
      stitchLength: 2.5,
      tension: 5
    }
    updateDeviceParams()
  }
}

function connectDevice() {
  if (selectedDevice.value && selectedDevice.value.status === 'offline') {
    selectedDevice.value.status = 'online'
    // 模拟连接过程
    setTimeout(() => {
      if (selectedDevice.value) {
        initMonitor()
      }
    }, 500)
  }
}

function updateDeviceParams() {
  // 实时同步参数到设备
  if (selectedDevice.value && selectedDevice.value.status !== 'offline') {
    // 这里可以调用API同步参数
    console.log('更新设备参数:', deviceParams.value)
    // 更新监控画面
    initMonitor()
  }
}

function initMonitor() {
  if (!monitorCanvasRef.value || !selectedDeviceId.value) return
  
  const canvas = monitorCanvasRef.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  canvas.width = canvas.offsetWidth
  canvas.height = canvas.offsetHeight
  
  // 绘制监控画面
  ctx.fillStyle = 'rgba(139, 92, 246, 0.1)'
  ctx.fillRect(0, 0, canvas.width, canvas.height)
  
  // 绘制网格
  ctx.strokeStyle = 'rgba(139, 92, 246, 0.2)'
  ctx.lineWidth = 1
  for (let i = 0; i < canvas.width; i += 50) {
    ctx.beginPath()
    ctx.moveTo(i, 0)
    ctx.lineTo(i, canvas.height)
    ctx.stroke()
  }
  for (let i = 0; i < canvas.height; i += 50) {
    ctx.beginPath()
    ctx.moveTo(0, i)
    ctx.lineTo(canvas.width, i)
    ctx.stroke()
  }
  
  // 绘制模拟的刺绣图案
  ctx.strokeStyle = '#8B5CF6'
  ctx.lineWidth = 2
  const centerX = canvas.width / 2
  const centerY = canvas.height / 2
  const radius = Math.min(canvas.width, canvas.height) * 0.3
  
  ctx.beginPath()
  ctx.arc(centerX, centerY, radius, 0, Math.PI * 2)
  ctx.stroke()
  
  // 绘制进度指示
  if (selectedDevice.value?.progress) {
    ctx.strokeStyle = '#10B981'
    ctx.lineWidth = 3
    ctx.beginPath()
    ctx.arc(centerX, centerY, radius + 10, -Math.PI / 2, -Math.PI / 2 + (selectedDevice.value.progress / 100) * Math.PI * 2)
    ctx.stroke()
  }
}

let monitorInterval: number | null = null

onMounted(() => {
  window.scrollTo(0, 0)
  
  // 模拟实时更新
  monitorInterval = window.setInterval(() => {
    if (selectedDeviceId.value) {
      initMonitor()
    }
  }, 1000)
})

onUnmounted(() => {
  if (monitorInterval) {
    clearInterval(monitorInterval)
  }
})
</script>
