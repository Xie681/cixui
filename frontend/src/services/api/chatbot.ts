import api from './index'
import { useUserStore } from '@/store/user'

export interface ChatMessageDTO {
  message: string
  sessionId?: string
  stream?: boolean
}

export interface ChatResponseVO {
  sessionId: string
  reply: string
  toolsUsed: string[]
  timestamp: string
  tokenUsage?: any
}

/**
 * 普通对话（非流式）
 */
export function chat(data: ChatMessageDTO): Promise<ChatResponseVO> {
  return api.post('/api/chatbot/chat', data)
}

/**
 * 流式对话
 * 使用 fetch 接收 SSE 流
 */
export function chatStream(
  data: ChatMessageDTO,
  onMessage: (token: string) => void,
  onComplete: (sessionId?: string) => void,
  onError: (error: Error) => void
): () => void {
  const userStore = useUserStore()
  const baseURL = import.meta.env.VITE_API_BASE_URL ?? ''
  
  // 构建URL和请求体
  const url = `${baseURL}/api/chatbot/chat/stream`
  
  // 使用 fetch 进行流式请求
  const controller = new AbortController()
  
  fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': userStore.token ? `Bearer ${userStore.token}` : ''
    },
    body: JSON.stringify(data),
    signal: controller.signal
  })
    .then(async (response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }
      
      // 从响应头中获取 sessionId
      const sessionId = response.headers.get('X-Session-Id')
      console.log('响应头中的所有 headers:', Array.from(response.headers.entries()))
      if (sessionId) {
        console.log('✅ 收到 sessionId:', sessionId)
      } else {
        console.warn('⚠️ 未收到 sessionId，请检查后端 CORS 配置是否暴露了 X-Session-Id')
      }
      
      const reader = response.body?.getReader()
      const decoder = new TextDecoder()
      
      if (!reader) {
        throw new Error('无法获取响应流')
      }
      
      let buffer = ''
      
      while (true) {
        const { done, value } = await reader.read()
        
        if (done) {
          onComplete(sessionId || undefined)
          break
        }
        
        // 解码数据块
        buffer += decoder.decode(value, { stream: true })
        
        // 按行分割处理 SSE 格式
        const lines = buffer.split('\n')
        buffer = lines.pop() || '' // 保留最后一个不完整的行
        
        for (const line of lines) {
          // SSE 格式: "data: 内容"
          if (line.startsWith('data:')) {
            const token = line.substring(5).trim() // 移除 "data:" 前缀
            if (token) {
              onMessage(token)
            }
          }
        }
      }
    })
    .catch((error) => {
      if (error.name !== 'AbortError') {
        onError(error)
      }
    })
  
  // 返回取消函数
  return () => controller.abort()
}

/**
 * 清除会话
 */
export function clearSession(sessionId: string): Promise<void> {
  return api.delete(`/api/chatbot/session/${sessionId}`)
}

export const chatbotApi = {
  chat,
  chatStream,
  clearSession
}
