<template>
  <div class="relative aspect-video bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl overflow-hidden">
    <svg ref="svgRef" class="w-full h-full" :viewBox="`0 0 ${width} ${height}`">
      <!-- 连线 -->
      <line
        v-for="(conn, index) in connections"
        :key="`line-${index}`"
        :x1="getNodePosition(conn.from).x"
        :y1="getNodePosition(conn.from).y"
        :x2="getNodePosition(conn.to).x"
        :y2="getNodePosition(conn.to).y"
        :stroke="getNodeColor(conn.from)"
        stroke-width="2"
        opacity="0.4"
        class="knowledge-line"
        :style="{ animation: `drawLine 1s ease-out ${index * 0.1}s both` }"
      />
      
      <!-- 节点光晕 -->
      <circle
        v-for="(node, index) in nodes"
        :key="`glow-${node.id}`"
        :cx="node.x"
        :cy="node.y"
        :r="getNodeRadius(node.type) + 5"
        :fill="node.color"
        opacity="0.2"
        class="knowledge-glow"
        :style="{ animation: `pulse 2s ease-in-out infinite ${index * 0.1}s` }"
      />
      
      <!-- 节点 -->
      <circle
        v-for="node in nodes"
        :key="`node-${node.id}`"
        :cx="node.x"
        :cy="node.y"
        :r="getNodeRadius(node.type)"
        :fill="node.color"
        opacity="0.9"
        class="cursor-pointer knowledge-node"
        :data-node-id="node.id"
        :style="{
          filter: `drop-shadow(0 0 10px ${node.color})`,
          transition: 'all 0.3s ease'
        }"
        @mouseenter="handleNodeHover(node, true)"
        @mouseleave="handleNodeHover(node, false)"
        @click="handleNodeClick(node)"
      />
      
      <!-- 节点标签 -->
      <text
        v-for="node in nodes"
        :key="`text-${node.id}`"
        :x="node.x"
        :y="node.y + getTextOffset(node.type)"
        text-anchor="middle"
        fill="#FFFFFF"
        :font-size="getFontSize(node.type)"
        font-weight="bold"
        class="cursor-pointer"
        @click="handleNodeClick(node)"
      >
        {{ node.label }}
      </text>
    </svg>
    
    <div v-if="showPlaceholder" class="absolute inset-0 flex items-center justify-center pointer-events-none">
      <div class="text-center">
        <svg class="w-24 h-24 mx-auto mb-4 text-accent-primary animate-pulse" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
        </svg>
        <p class="text-gray-400">点击节点或下方知识卡片探索更多</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'

interface KnowledgeNode {
  id: string
  x: number
  y: number
  label: string
  type: 'main' | 'category' | 'sub'
  color: string
}

interface Connection {
  from: string
  to: string
}

const props = defineProps<{
  onNodeClick?: (nodeId: string) => void
}>()

const svgRef = ref<SVGSVGElement>()
const width = ref(1200)
const height = ref(600)
const showPlaceholder = ref(false)

const nodes = ref<KnowledgeNode[]>([
  // 中心节点
  { id: 'center', x: 600, y: 300, label: '刺绣知识', type: 'main', color: '#8B5CF6' },
  
  // 第一层：主要分类
  { id: 'tradition', x: 400, y: 150, label: '传统工艺', type: 'category', color: '#8B5CF6' },
  { id: 'ai', x: 800, y: 150, label: 'AI技术', type: 'category', color: '#06B6D4' },
  { id: 'culture', x: 400, y: 450, label: '文化传承', type: 'category', color: '#10B981' },
  { id: 'stitch', x: 800, y: 450, label: '针法图谱', type: 'category', color: '#A78BFA' },
  { id: 'material', x: 600, y: 50, label: '材料科学', type: 'category', color: '#EC4899' },
  { id: 'history', x: 600, y: 550, label: '历史脉络', type: 'category', color: '#F59E0B' },
  
  // 第二层：传统工艺的子节点
  { id: 'suxiu', x: 250, y: 50, label: '苏绣', type: 'sub', color: '#8B5CF6' },
  { id: 'xiangxiu', x: 250, y: 250, label: '湘绣', type: 'sub', color: '#8B5CF6' },
  { id: 'yuexiu', x: 250, y: 350, label: '粤绣', type: 'sub', color: '#8B5CF6' },
  { id: 'shuxiu', x: 250, y: 550, label: '蜀绣', type: 'sub', color: '#8B5CF6' },
  
  // 第二层：AI技术的子节点
  { id: 'cv', x: 950, y: 50, label: '计算机视觉', type: 'sub', color: '#06B6D4' },
  { id: 'dl', x: 950, y: 250, label: '深度学习', type: 'sub', color: '#06B6D4' },
  { id: 'path', x: 950, y: 350, label: '路径规划', type: 'sub', color: '#06B6D4' },
  { id: 'gen', x: 950, y: 550, label: '生成式AI', type: 'sub', color: '#06B6D4' },
])

const connections = ref<Connection[]>([
  // 中心到主要分类
  { from: 'center', to: 'tradition' },
  { from: 'center', to: 'ai' },
  { from: 'center', to: 'culture' },
  { from: 'center', to: 'stitch' },
  { from: 'center', to: 'material' },
  { from: 'center', to: 'history' },
  
  // 传统工艺到四大名绣
  { from: 'tradition', to: 'suxiu' },
  { from: 'tradition', to: 'xiangxiu' },
  { from: 'tradition', to: 'yuexiu' },
  { from: 'tradition', to: 'shuxiu' },
  
  // AI技术到子技术
  { from: 'ai', to: 'cv' },
  { from: 'ai', to: 'dl' },
  { from: 'ai', to: 'path' },
  { from: 'ai', to: 'gen' },
  
  // 交叉连接
  { from: 'cv', to: 'stitch' },
  { from: 'dl', to: 'tradition' },
  { from: 'culture', to: 'history' },
])

function getNodeRadius(type: string): number {
  switch (type) {
    case 'main': return 40
    case 'category': return 30
    case 'sub': return 20
    default: return 20
  }
}

function getTextOffset(type: string): number {
  switch (type) {
    case 'main': return 60
    case 'category': return 45
    case 'sub': return 30
    default: return 30
  }
}

function getFontSize(type: string): string {
  switch (type) {
    case 'main': return '18'
    case 'category': return '14'
    case 'sub': return '11'
    default: return '11'
  }
}

function getNodePosition(nodeId: string): { x: number; y: number } {
  const node = nodes.value.find(n => n.id === nodeId)
  return node ? { x: node.x, y: node.y } : { x: 0, y: 0 }
}

function getNodeColor(nodeId: string): string {
  const node = nodes.value.find(n => n.id === nodeId)
  return node ? node.color : '#8B5CF6'
}

function handleNodeHover(node: KnowledgeNode, isHover: boolean) {
  // 可以通过修改节点样式来实现hover效果
  const circle = svgRef.value?.querySelector(`circle[data-node-id="${node.id}"]`)
  if (circle && isHover) {
    circle.setAttribute('r', String(getNodeRadius(node.type) + 5))
    circle.setAttribute('opacity', '1')
  } else if (circle) {
    circle.setAttribute('r', String(getNodeRadius(node.type)))
    circle.setAttribute('opacity', '0.9')
  }
}

function handleNodeClick(node: KnowledgeNode) {
  let detailType = node.id
  if (['suxiu', 'xiangxiu', 'yuexiu', 'shuxiu'].includes(node.id)) {
    detailType = 'tradition'
  } else if (['cv', 'dl', 'path', 'gen'].includes(node.id)) {
    detailType = 'ai'
  }
  
  if (props.onNodeClick) {
    props.onNodeClick(detailType)
  }
}

onMounted(() => {
  // 添加CSS动画样式
  if (!document.getElementById('knowledge-graph-styles')) {
    const style = document.createElement('style')
    style.id = 'knowledge-graph-styles'
    style.textContent = `
      @keyframes drawLine {
        from {
          stroke-dasharray: 1000;
          stroke-dashoffset: 1000;
        }
        to {
          stroke-dashoffset: 0;
        }
      }
      @keyframes pulse {
        0%, 100% { opacity: 0.2; transform: scale(1); }
        50% { opacity: 0.4; transform: scale(1.1); }
      }
      .knowledge-node:hover {
        filter: drop-shadow(0 0 20px currentColor) !important;
      }
    `
    document.head.appendChild(style)
  }
  
  // 初始化时隐藏placeholder
  setTimeout(() => {
    showPlaceholder.value = false
  }, 100)
})
</script>
