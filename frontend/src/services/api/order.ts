import api from './index'

export interface OrderVO {
  id: number
  orderNo: string
  type: 'IMAGE' | 'TEXT'
  status: string
  statusDesc: string
  totalAmount: number
  paidAmount: number
  isUrgent: boolean
  createdAt: string
  paidAt?: string
  summary: string
  imageUrl?: string
  resultImageUrl?: string
  embroideryStyle?: 'SU' | 'XIANG' | 'SHU' | 'YUE'
  styleDesc?: string
  fabricType?: string
  threadType?: string
  width?: number
  height?: number
  quantity?: number
}

export interface OrderDetailVO {
  id: number
  orderNo: string
  type: 'IMAGE' | 'TEXT'
  typeDesc: string
  status: string
  statusDesc: string
  totalAmount: number
  paidAmount: number
  addressSnapshot: any
  remark?: string
  isUrgent: boolean
  urgentFactor: number
  createdAt: string
  paidAt?: string
  sourceImage?: string
  resultImageUrl?: string
  embroideryStyle?: 'SU' | 'XIANG' | 'SHU' | 'YUE'
  width?: number
  height?: number
  fabricType?: string
  threadType?: string
  stitchCount?: number
  colorCount?: number
  quantity?: number
  unitPrice: number
  basePrice: number
  stitchFee: number
  colorFee: number
  fabricFee: number
  threadFee: number
  urgentFee: number
  styleDesc?: string
}

export const orderApi = {
  // 获取订单列表
  listOrders(status?: string): Promise<OrderVO[]> {
    const url = status ? `/api/orders?status=${status}` : '/api/orders'
    return api.get(url)
  },
  
  // 获取订单详情（按ID）
  getOrderDetail(id: number): Promise<OrderDetailVO> {
    return api.get(`/api/orders/${id}`)
  },
  
  // 获取订单详情（按订单号）
  getOrderDetailByOrderNo(orderNo: string): Promise<OrderDetailVO> {
    return api.get(`/api/orders/no/${orderNo}`)
  },
  
  // 取消订单
  cancelOrder(id: number): Promise<void> {
    return api.post(`/api/orders/${id}/cancel`)
  }
}
