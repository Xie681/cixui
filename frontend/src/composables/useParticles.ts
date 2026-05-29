import { onMounted, onUnmounted } from 'vue'

export function useParticles(containerId: string, particleCount: number = 30) {
  let particles: HTMLElement[] = []
  
  // 增加更多刺绣风格颜色：紫、青、绿、粉、金、红
  const colors = ['#8B5CF6', '#06B6D4', '#10B981', '#EC4899', '#F59E0B', '#EF4444']
  
  function createParticles() {
    const container = document.getElementById(containerId)
    if (!container) return
    
    // Ensure container has dimensions and positioning context if not already
    if (getComputedStyle(container).position === 'static') {
      container.style.position = 'relative'
    }
    
    // 清除现有粒子
    particles.forEach(p => p.remove())
    particles = []
    
    for (let i = 0; i < particleCount; i++) {
      const particle = document.createElement('div')
      particle.className = 'particle'
      
      // 使用分层随机分布，确保左右均衡
      // 将屏幕分为左、中、右三个区域，确保每个区域都有粒子
      let leftPos: number
      const section = i % 3
      if (section === 0) {
        // 左侧 0-33%
        leftPos = Math.random() * 33
      } else if (section === 1) {
        // 中间 33-66%
        leftPos = 33 + Math.random() * 33
      } else {
        // 右侧 66-100%
        leftPos = 66 + Math.random() * 34
      }
      
      particle.style.left = leftPos + '%'
      particle.style.top = Math.random() * 100 + '%' // 同时也随机垂直位置，防止聚集在顶部
      
      particle.style.animationDelay = Math.random() * 5 + 's' // 减少延迟，让粒子更快出现
      particle.style.animationDuration = (10 + Math.random() * 15) + 's' // 稍微放慢速度
      
      const color = colors[Math.floor(Math.random() * colors.length)]
      particle.style.background = color
      particle.style.boxShadow = `0 0 8px ${color}` // 增加光晕
      particle.style.width = (2 + Math.random() * 4) + 'px' // 随机大小
      particle.style.height = particle.style.width
      particle.style.borderRadius = '50%'
      particle.style.position = 'absolute'
      particle.style.opacity = (0.3 + Math.random() * 0.5).toString() // 随机透明度
      
      container.appendChild(particle)
      particles.push(particle)
    }
  }
  
  onMounted(() => {
    // 稍微延迟执行以确保DOM已渲染
    setTimeout(createParticles, 100)
    window.addEventListener('resize', createParticles)
  })
  
  onUnmounted(() => {
    particles.forEach(p => p.remove())
    window.removeEventListener('resize', createParticles)
  })
  
  return {
    createParticles
  }
}
