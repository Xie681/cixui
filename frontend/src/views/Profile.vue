<template>
  <div class="min-h-screen bg-black text-white">
    <Navbar />
    <div class="pt-16">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <h1 class="text-4xl font-heading font-bold gradient-text mb-8">个人中心</h1>
        
        <div class="grid md:grid-cols-4 gap-6">
          <!-- 左侧导航 -->
          <div class="md:col-span-1">
            <div class="glow-card rounded-xl p-4 space-y-2">
              <button
                v-for="tab in tabs"
                :key="tab.id"
                @click="activeTab = tab.id"
                :class="[
                  'w-full text-left px-4 py-3 rounded-lg transition-all duration-300 cursor-pointer',
                  activeTab === tab.id 
                    ? 'bg-accent-primary text-white' 
                    : 'hover:bg-white/10 text-gray-300'
                ]"
              >
                <span class="flex items-center space-x-2">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="tab.icon" />
                  </svg>
                  <span>{{ tab.name }}</span>
                </span>
              </button>
            </div>
          </div>
          
          <!-- 右侧内容 -->
          <div class="md:col-span-3">
            <!-- 个人信息 -->
            <div v-if="activeTab === 'info'" class="glow-card rounded-xl p-6">
              <h2 class="text-2xl font-heading font-semibold mb-6 text-white">个人信息</h2>
              
              <div class="space-y-6">
                <!-- 头像 -->
                <div class="flex items-center space-x-4">
                  <div class="relative group">
                    <div class="w-20 h-20 rounded-full bg-gradient-to-br from-accent-primary to-accent-cta flex items-center justify-center overflow-hidden">
                      <img v-if="editForm.avatar || userInfo?.avatar" :src="editForm.avatar || userInfo?.avatar" class="w-full h-full object-cover" alt="头像">
                      <svg v-else class="w-10 h-10 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                      </svg>
                    </div>
                    <label class="absolute inset-0 flex items-center justify-center bg-black/50 rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer">
                      <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 9a2 2 0 012-2h.93a2 2 0 001.664-.89l.812-1.22A2 2 0 0110.07 4h3.86a2 2 0 011.664.89l.812 1.22A2 2 0 0018.07 7H19a2 2 0 012 2v9a2 2 0 01-2 2H5a2 2 0 01-2-2V9z" />
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 13a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                      <input
                        ref="avatarInput"
                        type="file"
                        accept="image/*"
                        class="hidden"
                        @change="handleAvatarChange"
                      >
                    </label>
                  </div>
                  <div>
                    <p class="text-white font-semibold">{{ userInfo?.nickname || '未设置昵称' }}</p>
                    <p class="text-sm text-gray-400">ID: {{ userInfo?.id }}</p>
                    <p class="text-xs text-gray-500 mt-1">点击头像上传新头像</p>
                  </div>
                </div>
                
                <!-- 信息表单 -->
                <div class="grid gap-4">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">昵称</label>
                    <input
                      v-model="editForm.nickname"
                      type="text"
                      class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
                      placeholder="请输入昵称"
                    >
                  </div>
                  
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">手机号</label>
                    <input
                      :value="userInfo?.phone"
                      type="text"
                      disabled
                      class="w-full glow-card rounded-lg px-4 py-3 text-gray-500 bg-transparent cursor-not-allowed"
                    >
                  </div>
                  
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">邮箱</label>
                    <input
                      v-model="editForm.email"
                      type="email"
                      class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
                      placeholder="请输入邮箱"
                    >
                  </div>
                </div>
                
                <div class="flex justify-end">
                  <button @click="updateProfile" class="neon-button px-6 py-3 text-white rounded-lg transition-all duration-300 cursor-pointer">
                    保存修改
                  </button>
                </div>
              </div>
            </div>
            
            <!-- 收货地址 -->
            <div v-if="activeTab === 'address'" class="glow-card rounded-xl p-6">
              <div class="flex items-center justify-between mb-6">
                <h2 class="text-2xl font-heading font-semibold text-white">收货地址</h2>
                <button @click="showAddressForm = true" class="neon-button px-4 py-2 text-white rounded-lg transition-all duration-300 cursor-pointer">
                  添加地址
                </button>
              </div>
              
              <div v-if="addresses.length === 0" class="text-center py-12 text-gray-400">
                暂无收货地址
              </div>
              
              <div v-else class="space-y-4">
                <div
                  v-for="addr in addresses"
                  :key="addr.id"
                  class="glow-card rounded-lg p-4 hover:bg-white/5 transition-all duration-300"
                >
                  <div class="flex items-start justify-between">
                    <div class="flex-1">
                      <div class="flex items-center space-x-2 mb-2">
                        <span class="text-white font-semibold">{{ addr.name }}</span>
                        <span class="text-gray-400">{{ addr.phone }}</span>
                        <span v-if="addr.isDefault" class="px-2 py-0.5 bg-accent-primary rounded text-xs text-white">默认</span>
                      </div>
                      <p class="text-gray-300 text-sm">{{ addr.fullAddress }}</p>
                    </div>
                    <div class="flex items-center space-x-2">
                      <button @click="deleteAddress(addr.id)" class="text-gray-400 hover:text-red-400 transition-colors">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                        </svg>
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 我的订单 -->
            <div v-if="activeTab === 'orders'" class="glow-card rounded-xl p-6">
              <h2 class="text-2xl font-heading font-semibold mb-6 text-white">我的订单</h2>
              <div class="text-center py-12">
                <p class="text-gray-400 mb-4">查看您的所有订单</p>
                <RouterLink to="/orders" class="neon-button px-6 py-3 text-white rounded-lg inline-block transition-all duration-300 cursor-pointer">
                  前往订单列表
                </RouterLink>
              </div>
            </div>
            
            <!-- 账户设置 -->
            <div v-if="activeTab === 'settings'" class="glow-card rounded-xl p-6">
              <h2 class="text-2xl font-heading font-semibold mb-6 text-white">账户设置</h2>
              <div class="space-y-4">
                <div class="glow-card rounded-lg p-4 hover:bg-white/5 transition-all duration-300 cursor-pointer">
                  <div class="flex items-center justify-between">
                    <div>
                      <p class="text-white font-semibold">修改密码</p>
                      <p class="text-sm text-gray-400">定期修改密码，保护账户安全</p>
                    </div>
                    <svg class="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                    </svg>
                  </div>
                </div>
                
                <div class="glow-card rounded-lg p-4">
                  <div class="flex items-center justify-between">
                    <div>
                      <p class="text-white font-semibold">退出登录</p>
                      <p class="text-sm text-gray-400">退出当前账户</p>
                    </div>
                    <button @click="handleLogout" class="px-4 py-2 bg-red-500/20 hover:bg-red-500/30 text-red-400 rounded-lg transition-all duration-300 cursor-pointer">
                      退出
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 地址表单对话框 -->
    <div v-if="showAddressForm" class="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm">
      <div class="glow-card rounded-2xl p-6 w-full max-w-md mx-4 max-h-[90vh] overflow-y-auto">
        <div class="flex items-center justify-between mb-6">
          <h3 class="text-xl font-heading font-semibold text-white">添加收货地址</h3>
          <button @click="closeAddressForm" class="text-gray-400 hover:text-white transition-colors">
            <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
        
        <form @submit.prevent="handleAddAddress" class="space-y-4">
          <div>
            <label class="block text-sm text-gray-400 mb-2">收货人姓名 <span class="text-red-400">*</span></label>
            <input
              v-model="addressForm.name"
              type="text"
              required
              maxlength="50"
              class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
              placeholder="请输入收货人姓名"
            >
          </div>
          
          <div>
            <label class="block text-sm text-gray-400 mb-2">手机号 <span class="text-red-400">*</span></label>
            <input
              v-model="addressForm.phone"
              type="tel"
              required
              pattern="^1[3-9]\d{9}$"
              class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
              placeholder="请输入11位手机号"
            >
            <p class="text-xs text-gray-500 mt-1">请输入11位中国大陆手机号</p>
          </div>
          
          <div class="grid grid-cols-3 gap-3">
            <div>
              <label class="block text-sm text-gray-400 mb-2">省份 <span class="text-red-400">*</span></label>
              <input
                v-model="addressForm.province"
                type="text"
                required
                class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
                placeholder="省"
              >
            </div>
            
            <div>
              <label class="block text-sm text-gray-400 mb-2">城市 <span class="text-red-400">*</span></label>
              <input
                v-model="addressForm.city"
                type="text"
                required
                class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
                placeholder="市"
              >
            </div>
            
            <div>
              <label class="block text-sm text-gray-400 mb-2">区县 <span class="text-red-400">*</span></label>
              <input
                v-model="addressForm.district"
                type="text"
                required
                class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary"
                placeholder="区"
              >
            </div>
          </div>
          
          <div>
            <label class="block text-sm text-gray-400 mb-2">详细地址 <span class="text-red-400">*</span></label>
            <textarea
              v-model="addressForm.detail"
              required
              maxlength="255"
              rows="3"
              class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary resize-none"
              placeholder="请输入详细地址（街道、门牌号等）"
            ></textarea>
          </div>
          
          <div class="flex items-center space-x-2">
            <input
              v-model="addressForm.isDefault"
              type="checkbox"
              id="isDefault"
              class="w-4 h-4 rounded border-gray-600 bg-transparent text-accent-primary focus:ring-accent-primary focus:ring-offset-0"
            >
            <label for="isDefault" class="text-sm text-gray-300 cursor-pointer">设为默认地址</label>
          </div>
          
          <div class="flex items-center space-x-3 pt-4">
            <button
              type="button"
              @click="closeAddressForm"
              class="flex-1 px-4 py-3 glow-card hover:bg-white/10 text-white rounded-lg transition-all duration-300 cursor-pointer"
            >
              取消
            </button>
            <button
              type="submit"
              :disabled="isSubmitting"
              class="flex-1 neon-button px-4 py-3 text-white rounded-lg transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {{ isSubmitting ? '保存中...' : '保存' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { useUserStore } from '@/store/user'
import { userApi } from '@/services/api/user'
import type { AddressVO } from '@/services/api/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('info')
const showAddressForm = ref(false)
const addresses = ref<AddressVO[]>([])
const isSubmitting = ref(false)

// 地址表单
const addressForm = ref({
  name: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: '',
  isDefault: false
})

const tabs = [
  { id: 'info', name: '个人信息', icon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z' },
  { id: 'address', name: '收货地址', icon: 'M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z' },
  { id: 'orders', name: '我的订单', icon: 'M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2' },
  { id: 'settings', name: '账户设置', icon: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z' }
]

const userInfo = ref(userStore.userInfo)

const editForm = ref({
  nickname: userInfo.value?.nickname || '',
  email: userInfo.value?.email || '',
  avatar: userInfo.value?.avatar || ''
})

const avatarInput = ref<HTMLInputElement | null>(null)
const isUploadingAvatar = ref(false)

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    router.push('/')
    return
  }
  
  try {
    await userStore.fetchProfile()
    userInfo.value = userStore.userInfo
    editForm.value = {
      nickname: userInfo.value?.nickname || '',
      email: userInfo.value?.email || '',
      avatar: userInfo.value?.avatar || ''
    }
    
    await loadAddresses()
  } catch (error) {
    console.error('加载用户信息失败:', error)
  }
})

async function handleAvatarChange(event: Event) {
  const target = event.target as HTMLInputElement
  const file = target.files?.[0]
  
  if (!file) return
  
  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    alert('请选择图片文件')
    return
  }
  
  // 验证文件大小（2MB）
  if (file.size > 2 * 1024 * 1024) {
    alert('图片大小不能超过2MB')
    return
  }
  
  isUploadingAvatar.value = true
  
  try {
    const result = await userApi.uploadAvatar(file)
    editForm.value.avatar = result.url
    alert('头像上传成功，请点击"保存修改"按钮保存')
  } catch (error) {
    console.error('上传头像失败:', error)
    alert('上传失败，请重试')
  } finally {
    isUploadingAvatar.value = false
    // 清空input，允许重复选择同一文件
    if (avatarInput.value) {
      avatarInput.value.value = ''
    }
  }
}

async function updateProfile() {
  try {
    await userApi.updateProfile(editForm.value)
    await userStore.fetchProfile()
    userInfo.value = userStore.userInfo
    editForm.value.avatar = userInfo.value?.avatar || ''
    alert('保存成功')
  } catch (error) {
    console.error('更新失败:', error)
    alert('保存失败，请重试')
  }
}

async function loadAddresses() {
  try {
    addresses.value = await userApi.listAddresses()
  } catch (error) {
    console.error('加载地址失败:', error)
  }
}

async function deleteAddress(id: number) {
  if (!confirm('确定要删除这个地址吗？')) return
  
  try {
    await userApi.deleteAddress(id)
    await loadAddresses()
  } catch (error) {
    console.error('删除地址失败:', error)
    alert('删除失败，请重试')
  }
}

function closeAddressForm() {
  showAddressForm.value = false
  // 重置表单
  addressForm.value = {
    name: '',
    phone: '',
    province: '',
    city: '',
    district: '',
    detail: '',
    isDefault: false
  }
}

async function handleAddAddress() {
  isSubmitting.value = true
  try {
    await userApi.addAddress(addressForm.value)
    alert('地址添加成功')
    closeAddressForm()
    await loadAddresses()
  } catch (error) {
    console.error('添加地址失败:', error)
    alert('添加地址失败，请重试')
  } finally {
    isSubmitting.value = false
  }
}

function handleLogout() {
  if (confirm('确定要退出登录吗？')) {
    userStore.logout()
    router.push('/')
  }
}
</script>
