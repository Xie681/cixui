import { ref, onMounted, onUnmounted } from 'vue'

export function usePageManager() {
  const activePage = ref<string | null>(null)
  
  function openPage(pageId: string) {
    activePage.value = pageId
    document.body.style.overflow = 'hidden'
    
    // 触发自定义事件，让组件可以监听
    window.dispatchEvent(new CustomEvent('pageOpened', { detail: pageId }))
  }
  
  function closePage(pageId: string) {
    if (activePage.value === pageId) {
      activePage.value = null
      document.body.style.overflow = ''
      
      // 触发自定义事件
      window.dispatchEvent(new CustomEvent('pageClosed', { detail: pageId }))
    }
  }
  
  function isPageOpen(pageId: string): boolean {
    return activePage.value === pageId
  }
  
  // 监听全局openPage事件
  onMounted(() => {
    const handleOpenPage = (event: Event) => {
      const customEvent = event as CustomEvent<string>
      openPage(customEvent.detail)
    }
    
    window.addEventListener('openPage', handleOpenPage as EventListener)
    
    onUnmounted(() => {
      window.removeEventListener('openPage', handleOpenPage as EventListener)
    })
  })
  
  return {
    activePage,
    openPage,
    closePage,
    isPageOpen
  }
}
