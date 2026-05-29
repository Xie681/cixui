import type { RouteRecordRaw } from 'vue-router'

export const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'home',
    component: () => import('@/views/Home.vue'),
    meta: { title: '首页' }
  },
  {
    path: '/gallery',
    name: 'gallery',
    component: () => import('@/views/Gallery.vue'),
    meta: { title: '风格画廊' }
  },
  {
    path: '/gallery-more',
    name: 'gallery-more',
    component: () => import('@/views/GalleryMore.vue'),
    meta: { title: '更多风格' }
  },
  {
    path: '/tools',
    name: 'tools',
    component: () => import('@/views/CreatorTools.vue'),
    meta: { title: '创作工具' }
  },
  {
    path: '/ai-generation',
    name: 'ai-generation',
    component: () => import('@/views/ai/AIGeneration.vue'),
    meta: { title: 'AI智能打版' }
  },
  {
    path: '/collaboration',
    name: 'collaboration',
    component: () => import('@/views/ai/Collaboration.vue'),
    meta: { title: '三方协同' }
  },
  {
    path: '/production',
    name: 'production',
    component: () => import('@/views/ProductionCollaboration.vue'),
    meta: { title: '生产协同' }
  },
  {
    path: '/pattern-editor',
    name: 'pattern-editor',
    component: () => import('@/views/PatternEditor.vue'),
    meta: { title: '图案编辑器' }
  },
  {
    path: '/luxury',
    name: 'luxury',
    component: () => import('@/views/luxury/LuxuryIndex.vue'),
    meta: { title: '高端定制' }
  },
  {
    path: '/luxury/replica',
    name: 'luxury-replica',
    component: () => import('@/views/luxury/LuxuryReplica.vue'),
    meta: { title: '奢侈品复刻' }
  },
  {
    path: '/luxury/artifact',
    name: 'artifact-replica',
    component: () => import('@/views/luxury/ArtifactReplica.vue'),
    meta: { title: '文物复刻' }
  },
  {
    path: '/luxury/dragon-robe',
    name: 'dragon-robe',
    component: () => import('@/views/luxury/DragonRobe.vue'),
    meta: { title: '龙袍定制' }
  },
  {
    path: '/luxury/traditional-clothing',
    name: 'traditional-clothing',
    component: () => import('@/views/luxury/TraditionalClothing.vue'),
    meta: { title: '传统服装定制' }
  },
  {
    path: '/knowledge',
    name: 'knowledge',
    component: () => import('@/views/KnowledgeGraph.vue'),
    meta: { title: '知识图谱' }
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('@/views/Profile.vue'),
    meta: { title: '个人中心', requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'orders',
    component: () => import('@/views/order/OrderList.vue'),
    meta: { title: '我的订单' }
  },
  {
    path: '/cart',
    name: 'cart',
    component: () => import('@/views/Cart.vue'),
    meta: { title: '购物车' }
  },
  {
    path: '/checkout',
    name: 'checkout',
    component: () => import('@/views/Checkout.vue'),
    meta: { title: '确认下单' }
  },
  {
    path: '/custom-order-flow',
    name: 'custom-order-flow',
    component: () => import('@/views/CustomOrderFlow.vue'),
    meta: { title: '定制下单流程' }
  },
  {
    path: '/orders/:id',
    name: 'order-detail',
    component: () => import('@/views/order/OrderDetail.vue'),
    meta: { title: '订单详情' }
  },
  {
    path: '/admin',
    name: 'admin',
    redirect: '/admin/dashboard',
    meta: { title: '管理后台', requiresAuth: true }
  },
  {
    path: '/admin/dashboard',
    name: 'admin-dashboard',
    component: () => import('@/views/admin/Dashboard.vue'),
    meta: { title: '仪表盘', requiresAuth: true }
  },
  {
    path: '/admin/patterns',
    name: 'admin-patterns',
    component: () => import('@/views/admin/PatternLibrary.vue'),
    meta: { title: '花样库', requiresAuth: true }
  },
  {
    path: '/admin/orders',
    name: 'admin-orders',
    component: () => import('@/views/admin/OrderManagement.vue'),
    meta: { title: '订单管理', requiresAuth: true }
  },
  {
    path: '/admin/devices',
    name: 'admin-devices',
    component: () => import('@/views/admin/DeviceManagement.vue'),
    meta: { title: '设备管理', requiresAuth: true }
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'not-found',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面未找到' }
  }
]
