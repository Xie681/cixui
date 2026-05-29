import { ref } from 'vue'

export function useWilcom() {
  const wilcomStatus = ref<'opened' | 'closed'>('closed')

  function openInWilcom() {
    // 获取当前生成的DST文件路径
    const dstFilePath = getCurrentDSTFilePath()
    
    if (!dstFilePath) {
      alert('DST文件尚未生成，请等待AI处理完成')
      return
    }
    
    // 尝试通过多种方式打开Wilcom
    try {
      // 方案1: 使用自定义URL协议
      const wilcomUrl = `wilcom://open?file=${encodeURIComponent(dstFilePath)}`
      const protocolLink = document.createElement('a')
      protocolLink.href = wilcomUrl
      protocolLink.style.display = 'none'
      document.body.appendChild(protocolLink)
      protocolLink.click()
      document.body.removeChild(protocolLink)
      
      // 延迟检查是否成功打开
      setTimeout(() => {
        // 方案2: 通过后端API打开文件
        fetch('/api/wilcom/open', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            filePath: dstFilePath,
            orderId: getCurrentOrderId()
          })
        }).then(response => {
          if (response.ok) {
            wilcomStatus.value = 'opened'
            showNotification('Wilcom已打开，DST文件已同步', 'success')
          } else {
            throw new Error('无法通过API打开Wilcom')
          }
        }).catch(error => {
          console.error('打开Wilcom失败:', error)
          // 方案3: 下载文件，让用户手动打开
          downloadDSTFile(dstFilePath)
          showNotification('Wilcom未安装或无法自动打开，已为您下载DST文件', 'info')
        })
      }, 1000)
    } catch (error) {
      console.error('打开Wilcom失败:', error)
      // 降级方案：下载文件
      downloadDSTFile(dstFilePath)
      showNotification('请手动在Wilcom中打开下载的DST文件', 'info')
    }
  }

  function getCurrentDSTFilePath() {
    const orderId = getCurrentOrderId()
    if (orderId) {
      return `/files/dst/${orderId}/design.dst`
    }
    return `/files/temp/dst/${Date.now()}.dst`
  }

  function getCurrentOrderId() {
    return window.currentOrderId || localStorage.getItem('currentOrderId') || null
  }

  function downloadDSTFile(filePath: string) {
    const link = document.createElement('a')
    link.href = filePath
    link.download = `design_${Date.now()}.dst`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
  }

  function showNotification(message: string, type: 'success' | 'error' | 'info' = 'info') {
    const notification = document.createElement('div')
    notification.className = `fixed top-20 right-4 z-50 px-6 py-4 rounded-lg shadow-lg transition-all duration-300 ${
      type === 'success' ? 'bg-accent-vibrant/90' : 
      type === 'error' ? 'bg-red-500/90' : 
      'bg-accent-primary/90'
    } text-white flex items-center space-x-2`
    notification.innerHTML = `
      <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        ${type === 'success' ? '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />' : 
          type === 'error' ? '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />' :
          '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />'}
      </svg>
      <span>${message}</span>
    `
    document.body.appendChild(notification)
    
    setTimeout(() => {
      notification.style.opacity = '0'
      setTimeout(() => notification.remove(), 300)
    }, 3000)
  }

  return {
    wilcomStatus,
    openInWilcom
  }
}

// 扩展Window接口
declare global {
  interface Window {
    currentOrderId?: string | null
  }
}
