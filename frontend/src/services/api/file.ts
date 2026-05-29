import api from './index'

export const fileApi = {
  // 上传图片
  uploadImage(file: File): Promise<{ url: string; fileName: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/image', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  // 上传头像
  uploadAvatar(file: File): Promise<{ url: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/avatar', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  },
  
  // 上传设计文件
  uploadDesign(file: File): Promise<{ url: string; fileName: string }> {
    const formData = new FormData()
    formData.append('file', file)
    return api.post('/api/file/upload/design', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}
