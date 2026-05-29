import api from './index'

export interface CalculatePriceDTO {
  style: string
  fabric: string
  thread: string
  size: {
    width: number
    height: number
  }
  complexity: number
  quantity: number
}

export interface AiPreviewDTO {
  imageUrl?: string
  text?: string
  fontStyle?: string
  fontSize?: number
  color?: string
  embroideryStyle: 'SU' | 'XIANG' | 'SHU' | 'YUE'
}

export interface SubmitOrderDTO {
  imageUrl: string
  embroideryStyle: 'SU' | 'XIANG' | 'SHU' | 'YUE'
  width: number
  height: number
  fabricType: string
  threadType: string
  quantity: number
  addressId: number
  isUrgent?: boolean
  needRefinement?: boolean
  remark?: string
  designId?: number
  resultImageUrl?: string
  stitchCount?: number
  colorCount?: number
}

export interface PriceVO {
  basePrice: number
  materialPrice: number
  laborPrice: number
  totalPrice: number
  estimatedDays: number
}

export interface AiPreviewVO {
  resultImageUrl: string
  stitchCount: number
  colorCount: number
}

export interface StyleVO {
  id: number
  name: string
  description: string
  imageUrl: string
  price: number
}

export interface DesignChatDTO {
  message: string
  sessionId?: string
}

export interface DesignChatVO {
  sessionId: string
  reply: string
  designImageUrl?: string
  requirement?: {
    theme: string
    style: string
    colors: string
    size: string
    additionalDetails: string
  }
  estimatedPrice?: string
  estimatedTime?: string
  timestamp: string
}

export const customizeApi = {
  // 获取绣制风格列表
  getStyles(): Promise<StyleVO[]> {
    return api.get('/api/customize/styles')
  },
  
  // 获取面料列表
  getFabrics(): Promise<string[]> {
    return api.get('/api/customize/fabrics')
  },
  
  // 获取线材列表
  getThreads(): Promise<string[]> {
    return api.get('/api/customize/threads')
  },
  
  // 计算报价
  calculatePrice(data: CalculatePriceDTO): Promise<PriceVO> {
    return api.post('/api/customize/calculate-price', data)
  },
  
  // AI预览刺绣效果
  previewAiResult(data: AiPreviewDTO): Promise<AiPreviewVO> {
    return api.post('/api/customize/preview-ai-result', data)
  },
  
  // 提交图片定制订单
  submitOrder(data: SubmitOrderDTO): Promise<string> {
    return api.post('/api/customize/submit-order', data)
  },
  
  // 提交文字定制订单
  submitTextOrder(data: any): Promise<string> {
    return api.post('/api/customize/submit-text-order', data)
  },
  
  // 设计助手对话
  designChat(data: DesignChatDTO): Promise<DesignChatVO> {
    return api.post('/api/customize/design/chat', data)
  }
}
