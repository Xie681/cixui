import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, userApi } from '@/services/api'
import type { UserProfileVO } from '@/types/user'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))
  const userInfo = ref<UserProfileVO | null>(null)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  
  // 登录 - 参数改为account匹配后端
  async function login(account: string, password: string) {
    const result = await authApi.login({ account, password })
    // 保存后端返回的accessToken
    token.value = result.accessToken
    refreshToken.value = result.refreshToken
    // 构建用户信息对象
    userInfo.value = {
      id: result.userId,
      nickname: result.nickname,
      avatar: result.avatar,
      role: result.role
    } as UserProfileVO
    // 保存到localStorage
    localStorage.setItem('token', result.accessToken)
    localStorage.setItem('refreshToken', result.refreshToken)
    return result
  }
  
  // 登出
  function logout() {
    token.value = null
    refreshToken.value = null
    userInfo.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('refreshToken')
  }
  
  // 获取用户信息
  async function fetchProfile() {
    const profile = await userApi.getProfile()
    userInfo.value = profile
  }
  
  return {
    token,
    refreshToken,
    userInfo,
    isLoggedIn,
    login,
    logout,
    fetchProfile
  }
})
