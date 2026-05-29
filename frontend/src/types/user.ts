export interface UserProfileVO {
  id: number
  phone: string
  email: string
  nickname: string
  avatar: string
  role: 'USER' | 'DESIGNER' | 'ADMIN'
  createdAt: string
}
