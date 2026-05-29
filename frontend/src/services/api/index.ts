import axios, { type AxiosInstance, type AxiosRequestConfig } from 'axios'
import { useUserStore } from '@/store/user'
import router from '@/router'

// 创建axios实例
const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL ?? '',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    const { code, data, message } = response.data
    if (code === 200) {
      return data
    } else if (code === 401) {
      // Token过期，跳转登录
      const userStore = useUserStore()
      userStore.logout()
      router.push('/login')
      return Promise.reject(new Error('登录已过期'))
    } else {
      return Promise.reject(new Error(message || '请求失败'))
    }
  },
  (error) => {
    return Promise.reject(error)
  }
)

export default api

// 导出所有API服务
export { authApi } from './auth'
export { userApi } from './user'
export { fileApi } from './file'
export { customizeApi } from './customize'
export { orderApi } from './order'
export { chatbotApi } from './chatbot'

// 暂时注释掉，因为generated目录缺少必要的文件
// export * as openapi from './generated/client'

// 导出类型（按需导入）
// import type { LoginDTO, RegisterDTO } from '@/services/api/auth'
// import type { UserProfileVO, AddressVO } from '@/services/api/user'
// import type { OrderVO, OrderDetailVO } from '@/services/api/order'
