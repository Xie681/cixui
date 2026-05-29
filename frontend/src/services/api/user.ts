import api from './index'
import type { UserProfileVO } from '@/types/user'

export interface UpdateProfileDTO {
  nickname?: string
  email?: string
  avatar?: string
}

export interface AddressVO {
  id: number
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  fullAddress: string
  isDefault: boolean
  createdAt: string
}

export interface AddAddressDTO {
  name: string
  phone: string
  province: string
  city: string
  district: string
  detail: string
  isDefault?: boolean
}

export interface UpdateAddressDTO {
  name?: string
  phone?: string
  province?: string
  city?: string
  district?: string
  detail?: string
  isDefault?: boolean
}

export const userApi = {
  // 获取个人信息
  getProfile(): Promise<UserProfileVO> {
    return api.get('/api/user/profile')
  },
  
  // 更新个人信息
  updateProfile(data: UpdateProfileDTO): Promise<void> {
    return api.put('/api/user/profile', data)
  },
  
  // 上传头像
  uploadAvatar(file: File): Promise<{ url: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/avatar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  },
  
  // 获取收货地址列表
  listAddresses(): Promise<AddressVO[]> {
    return api.get('/api/user/addresses')
  },
  
  // 获取地址详情
  getAddress(id: number): Promise<AddressVO> {
    return api.get(`/api/user/addresses/${id}`)
  },
  
  // 添加收货地址
  addAddress(data: AddAddressDTO): Promise<number> {
    return api.post('/api/user/addresses', data)
  },
  
  // 更新收货地址
  updateAddress(id: number, data: UpdateAddressDTO): Promise<void> {
    return api.put(`/api/user/addresses/${id}`, data)
  },
  
  // 删除收货地址
  deleteAddress(id: number): Promise<void> {
    return api.delete(`/api/user/addresses/${id}`)
  },
  
  // 设置默认地址
  setDefaultAddress(id: number): Promise<void> {
    return api.put(`/api/user/addresses/${id}/default`)
  }
}
