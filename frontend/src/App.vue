<template>
  <div id="app" class="min-h-screen bg-black text-gray-100 antialiased embroidery-pattern">
    <!-- 路由视图 -->
    <RouterView />
  </div>
</template>

<script setup lang="ts">
import { RouterView } from 'vue-router'
</script>

<style>
/* 刺绣主题背景 */
.embroidery-pattern {
  background-image: 
    radial-gradient(circle at 20% 50%, rgba(139, 92, 246, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 80% 80%, rgba(6, 182, 212, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 40% 20%, rgba(16, 185, 129, 0.1) 0%, transparent 50%);
  background-size: 200px 200px, 300px 300px, 250px 250px;
  background-position: 0 0, 100px 100px, 50px 50px;
  animation: patternMove 20s ease-in-out infinite;
}

@keyframes patternMove {
  0%, 100% { background-position: 0 0, 100px 100px, 50px 50px; }
  50% { background-position: 100px 100px, 200px 200px, 150px 150px; }
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: transparent;
}

::-webkit-scrollbar-thumb {
  background: rgba(139, 92, 246, 0.3);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: rgba(139, 92, 246, 0.5);
}
</style>
