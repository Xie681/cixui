import { ref } from 'vue'
import { chatbotApi } from '@/services/api/chatbot'

export interface ChatMessage {
  role: 'user' | 'assistant'
  content: string
  timestamp?: number
}

export function useChat() {
  const messages = ref<ChatMessage[]>([])
  const isTyping = ref(false)
  const sessionId = ref<string>()
  
  async function sendMessage(content: string) {
    if (!content.trim()) return
    
    // 添加用户消息
    messages.value.push({
      role: 'user',
      content: content.trim(),
      timestamp: Date.now()
    })
    
    // 显示打字指示器
    isTyping.value = true
    
    try {
      // 创建一个空的助手消息用于流式填充
      const assistantMessageIndex = messages.value.length
      messages.value.push({
        role: 'assistant',
        content: '',
        timestamp: Date.now()
      })
      
      // 调用流式API
      console.log('📤 发送消息，当前 sessionId:', sessionId.value || '无（首次对话）')
      chatbotApi.chatStream(
        {
          message: content.trim(),
          sessionId: sessionId.value,
          stream: true
        },
        // onMessage: 接收每个token
        (token: string) => {
          messages.value[assistantMessageIndex].content += token
        },
        // onComplete: 流式完成，保存 sessionId
        (newSessionId?: string) => {
          if (newSessionId) {
            if (!sessionId.value) {
              sessionId.value = newSessionId
              console.log('💾 保存 sessionId:', newSessionId)
            } else {
              console.log('✅ 使用已有 sessionId:', sessionId.value)
            }
          } else {
            console.warn('⚠️ 未收到 sessionId')
          }
          isTyping.value = false
        },
        // onError: 错误处理
        (error: Error) => {
          console.error('❌ 流式对话失败:', error)
          messages.value[assistantMessageIndex].content = '抱歉，我遇到了一些问题，请稍后再试。'
          isTyping.value = false
        }
      )
    } catch (error) {
      console.error('发送消息失败:', error)
      messages.value.push({
        role: 'assistant',
        content: '抱歉，我遇到了一些问题，请稍后再试。',
        timestamp: Date.now()
      })
      isTyping.value = false
    }
  }
  
  function clearMessages() {
    console.log('🗑️ 清除对话，sessionId:', sessionId.value)
    messages.value = []
    if (sessionId.value) {
      chatbotApi.clearSession(sessionId.value).catch(console.error)
    }
    sessionId.value = undefined
  }
  
  return {
    messages,
    isTyping,
    sendMessage,
    clearMessages
  }
}
