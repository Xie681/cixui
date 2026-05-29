import api from './index'

// 登录请求DTO - 匹配后端LoginDTO
export interface LoginDTO {
  account: string  // 后端接收account字段（手机号或邮箱）
  password: string
}

export interface RegisterDTO {
  phone?: string
  email?: string
  password: string
  code: string
  nickname?: string
}

export interface CodeLoginDTO {
  account: string  // 后端接收account字段
  code: string
}

export interface SendCodeDTO {
  receiver: string  // 后端接收receiver字段
  type: number      // 1-短信，2-邮箱
}

// 登录响应VO - 匹配后端LoginVO
export interface LoginVO {
  accessToken: string    // 后端返回accessToken
  refreshToken: string
  tokenType: string
  expiresIn: number
  userId: number
  nickname: string
  avatar?: string
  role: string
}

export const authApi = {
  // 用户注册
  register(data: RegisterDTO): Promise<LoginVO> {
    return api.post('/api/auth/register', data)
  },
  
  // 密码登录
  login(data: LoginDTO): Promise<LoginVO> {
    return api.post('/api/auth/login', data)
  },
  
  // 验证码登录
  codeLogin(data: CodeLoginDTO): Promise<LoginVO> {
    return api.post('/api/auth/code-login', data)
  },
  
  // 发送验证码
  sendCode(data: SendCodeDTO): Promise<void> {
    return api.post('/api/auth/send-code', data)
  },
  
  // 刷新Token
  refreshToken(refreshToken: string): Promise<LoginVO> {
    return api.post(`/api/auth/refresh-token?refreshToken=${refreshToken}`)
  },
  
  // 登出
  logout(): Promise<void> {
    return api.post('/api/auth/logout')
  }
}
