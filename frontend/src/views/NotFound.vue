<template>
  <div class="min-h-screen flex items-center justify-center">
    <div class="text-center">
      <h1 class="text-6xl font-heading font-bold gradient-text mb-4">404</h1>
      <p class="text-xl text-gray-300 mb-8">页面未找到</p>
      <RouterLink to="/" class="neon-button px-6 py-3 text-white rounded-lg font-medium">
        返回首页
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>
