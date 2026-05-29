<template>
  <div class="fixed bottom-4 right-4 md:bottom-6 md:right-6 z-50">
    <!-- Chat Toggle Button -->
    <button 
      @click="toggleChat"
      class="w-14 h-14 bg-accent-primary hover:bg-accent-secondary text-white rounded-full shadow-lg hover:shadow-xl transition-all duration-200 cursor-pointer flex items-center justify-center pulse-glow"
    >
      <svg 
        v-if="!isOpen"
        class="w-6 h-6" 
        fill="none" 
        stroke="currentColor" 
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
      </svg>
      <svg 
        v-else
        class="w-6 h-6" 
        fill="none" 
        stroke="currentColor" 
        viewBox="0 0 24 24"
      >
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
      </svg>
    </button>
    
    <!-- Chat Window -->
    <div 
      v-if="isOpen"
      class="absolute bottom-16 right-0 w-[calc(100vw-2rem)] sm:w-80 md:w-96 h-[500px] max-h-[calc(100vh-8rem)] glass-dark rounded-2xl shadow-2xl border border-accent-primary/30 flex flex-col overflow-hidden"
    >
      <!-- Chat Header -->
      <div class="px-4 py-3 border-b border-white/10 flex items-center justify-between">
        <div class="flex items-center space-x-2">
          <div class="w-8 h-8 bg-gradient-to-br from-accent-primary to-accent-cta rounded-full flex items-center justify-center">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <div>
            <h3 class="font-heading font-semibold text-white">GPT-4o AI 助手</h3>
            <p class="text-xs text-gray-400">在线为您服务</p>
          </div>
        </div>
        <button 
          @click="clearChat"
          class="p-2 hover:bg-white/10 rounded-lg transition-colors duration-200 cursor-pointer"
          title="清除对话"
        >
          <svg class="w-5 h-5 text-gray-400 hover:text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        </button>
      </div>
      
      <!-- Chat Messages -->
      <div ref="messagesRef" class="flex-1 overflow-y-auto p-4 space-y-4">
        <div class="flex items-start space-x-2">
          <div class="w-8 h-8 bg-gradient-to-br from-accent-primary to-accent-cta rounded-full flex items-center justify-center flex-shrink-0">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <div class="flex-1 glow-card rounded-lg p-3">
            <p class="text-sm text-gray-300">您好！我是 GPT-4o AI 智能助手，可以帮助您：</p>
            <ul class="mt-2 text-xs text-gray-400 space-y-1 list-disc list-inside">
              <li>解答刺绣相关问题</li>
              <li>推荐合适的刺绣风格</li>
              <li>协助图案设计建议</li>
              <li>提供定制方案咨询</li>
            </ul>
          </div>
        </div>
        
        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="[
            'flex items-start space-x-2',
            message.role === 'user' ? 'justify-end' : ''
          ]"
        >
          <div
            v-if="message.role === 'assistant'"
            class="w-8 h-8 bg-gradient-to-br from-accent-primary to-accent-cta rounded-full flex items-center justify-center flex-shrink-0"
          >
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <div
            :class="[
              'flex-1 rounded-lg p-3 max-w-[80%]',
              message.role === 'user' 
                ? 'glass text-white ml-auto' 
                : 'glow-card text-gray-300'
            ]"
          >
            <p class="text-sm">{{ message.content }}</p>
          </div>
          <div
            v-if="message.role === 'user'"
            class="w-8 h-8 bg-gray-600 rounded-full flex items-center justify-center flex-shrink-0"
          >
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
        </div>
        
        <!-- Typing Indicator -->
        <div v-if="isTyping" class="flex items-start space-x-2">
          <div class="w-8 h-8 bg-gradient-to-br from-accent-primary to-accent-cta rounded-full flex items-center justify-center flex-shrink-0">
            <svg class="w-5 h-5 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
          </div>
          <div class="flex-1 glass rounded-lg p-3">
            <div class="flex space-x-1">
              <div class="w-2 h-2 bg-accent-primary rounded-full animate-bounce" style="animation-delay: 0s;"></div>
              <div class="w-2 h-2 bg-accent-primary rounded-full animate-bounce" style="animation-delay: 0.2s;"></div>
              <div class="w-2 h-2 bg-accent-primary rounded-full animate-bounce" style="animation-delay: 0.4s;"></div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Chat Input -->
      <div class="px-4 py-3 border-t border-white/10">
        <div class="flex items-center space-x-2">
          <input 
            v-model="inputMessage"
            @keypress.enter="sendMessage"
            type="text" 
            placeholder="输入您的问题..." 
            class="flex-1 glow-card rounded-lg px-4 py-2 text-sm text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent"
          />
          <button 
            @click="sendMessage"
            class="w-10 h-10 bg-accent-primary hover:bg-accent-secondary text-white rounded-lg transition-colors duration-200 cursor-pointer flex items-center justify-center"
          >
            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, nextTick, watch } from 'vue'
import { useChat } from '@/composables/useChat'

const { messages, isTyping, sendMessage: sendChatMessage, clearMessages } = useChat()

const isOpen = ref(false)
const inputMessage = ref('')
const messagesRef = ref<HTMLElement>()

function toggleChat() {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    nextTick(() => {
      scrollToBottom()
    })
  }
}

function sendMessage() {
  if (!inputMessage.value.trim()) return
  
  sendChatMessage(inputMessage.value)
  inputMessage.value = ''
  
  nextTick(() => {
    scrollToBottom()
  })
}

function clearChat() {
  if (confirm('确定要清除所有对话记录吗？')) {
    clearMessages()
  }
}

function scrollToBottom() {
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

watch(messages, () => {
  nextTick(() => {
    scrollToBottom()
  })
}, { deep: true })
</script>
