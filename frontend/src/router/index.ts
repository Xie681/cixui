import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { routes } from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes as RouteRecordRaw[]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 可以在这里添加认证检查
  next()
})

export default router
