<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <div class="min-h-screen relative">
        <!-- 背景效果 -->
        <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
        
        <!-- 顶部导航栏 -->
        <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
              <div class="flex items-center space-x-4">
                <button @click="$router.back()" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                </button>
                <h2 class="text-xl font-heading font-bold text-white">三方协同微调</h2>
              </div>
              <div class="flex items-center space-x-4">
                <div class="flex items-center space-x-2">
                  <div class="w-2 h-2 bg-accent-vibrant rounded-full animate-pulse"></div>
                  <span class="text-sm text-gray-400">实时同步中</span>
                </div>
                <button 
                  v-if="!isFinalized"
                  @click="finalizeDesign" 
                  class="neon-button px-4 py-2 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer"
                >
                  最终定稿
                </button>
                <button 
                  v-else
                  @click="goToOrderFlow" 
                  class="neon-button px-6 py-2 text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer flex items-center space-x-2"
                >
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                  </svg>
                  <span>立即下单</span>
                </button>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
          <!-- 三方数字协作平台说明 -->
          <div class="mb-8 glow-card rounded-xl p-6 bg-gradient-to-r from-accent-primary/10 via-accent-cta/10 to-accent-vibrant/10 border border-accent-primary/30">
            <!-- AI生成信息提示 -->
            <div v-if="route.query.source === 'ai'" class="mb-4 p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
              <div class="flex items-start space-x-3">
                <div class="flex-shrink-0 w-10 h-10 bg-accent-vibrant/20 rounded-lg flex items-center justify-center">
                  <svg class="w-5 h-5 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                  </svg>
                </div>
                <div class="flex-1">
                  <h4 class="text-sm font-semibold text-white mb-1">AI智能打版已完成</h4>
                  <p class="text-xs text-gray-300 mb-2">当前展示的是AI生成的刺绣图案，您可以在此基础上进行三方协同微调</p>
                  <div class="flex items-center space-x-4 text-xs text-gray-400">
                    <span v-if="route.query.stitchCount" class="flex items-center space-x-1">
                      <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
                      </svg>
                      <span>预估针数: {{ route.query.stitchCount }}</span>
                    </span>
                    <span v-if="route.query.colorCount" class="flex items-center space-x-1">
                      <svg class="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 21a4 4 0 01-4-4V5a2 2 0 012-2h4a2 2 0 012 2v12a4 4 0 01-4 4zm0 0h12a2 2 0 002-2v-4a2 2 0 00-2-2h-2.343M11 7.343l1.657-1.657a2 2 0 012.828 0l2.829 2.829a2 2 0 010 2.828l-8.486 8.485M7 17h.01" />
                      </svg>
                      <span>颜色数: {{ route.query.colorCount }}</span>
                    </span>
                  </div>
                </div>
              </div>
            </div>
            
            <div class="flex items-start space-x-4">
              <div class="flex-shrink-0 w-14 h-14 bg-accent-primary/20 rounded-xl flex items-center justify-center">
                <svg class="w-7 h-7 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
              </div>
              <div class="flex-1">
                <h3 class="text-xl font-heading font-bold mb-2 gradient-text">三方数字协作平台</h3>
                <p class="text-sm text-gray-300 mb-4">采用"AI初版+人工精修"工作机制，兼顾自动化效率与艺术审美，提升生产一致性与交付良品率，显著缩短交付周期</p>
                <div class="grid md:grid-cols-3 gap-4 mb-4">
                  <div class="p-4 bg-accent-primary/10 rounded-lg border border-accent-primary/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <div class="w-8 h-8 bg-accent-primary/20 rounded-lg flex items-center justify-center">
                        <span class="text-accent-primary font-bold text-lg">设</span>
                      </div>
                      <h4 class="text-white font-semibold">设计师</h4>
                    </div>
                    <p class="text-xs text-gray-400">负责艺术美感调整，确保设计符合审美要求</p>
                  </div>
                  <div class="p-4 bg-accent-cta/10 rounded-lg border border-accent-cta/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <div class="w-8 h-8 bg-accent-cta/20 rounded-lg flex items-center justify-center">
                        <span class="text-accent-cta font-bold text-lg">版</span>
                      </div>
                      <h4 class="text-white font-semibold">版师</h4>
                    </div>
                    <p class="text-xs text-gray-400">在Wilcom中精修，负责版型完整性和技术细节</p>
                  </div>
                  <div class="p-4 bg-accent-vibrant/10 rounded-lg border border-accent-vibrant/30">
                    <div class="flex items-center space-x-2 mb-2">
                      <div class="w-8 h-8 bg-accent-vibrant/20 rounded-lg flex items-center justify-center">
                        <span class="text-accent-vibrant font-bold text-lg">工</span>
                      </div>
                      <h4 class="text-white font-semibold">工艺师</h4>
                    </div>
                    <p class="text-xs text-gray-400">负责生产可行性，确保工艺参数符合生产要求</p>
                  </div>
                </div>
                <div class="pt-4 border-t border-accent-primary/20">
                  <p class="text-xs text-gray-400">
                    <span class="text-accent-primary font-semibold">工作流程：</span>
                    <span class="text-gray-300">AI生成初版</span> → 
                    <span class="text-accent-primary">设计师调整美感</span> → 
                    <span class="text-accent-cta">版师在Wilcom中精修</span> → 
                    <span class="text-accent-vibrant">工艺师优化参数</span> → 
                    <span class="text-white font-semibold">最终定稿</span>
                  </p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="grid lg:grid-cols-4 gap-6">
            <!-- 左侧：角色切换 -->
            <div class="lg:col-span-1">
              <div class="glow-card rounded-xl p-6 sticky top-24">
                <h3 class="text-lg font-heading font-semibold mb-4 text-white">角色选择</h3>
                <div class="space-y-3">
                  <button 
                    @click="switchRole('designer')"
                    :class="[
                      'w-full px-4 py-3 glow-card hover:bg-white/10 rounded-lg text-left transition-all duration-300 cursor-pointer',
                      currentRole === 'designer' ? 'border-2 border-accent-primary' : ''
                    ]"
                  >
                    <div class="flex items-center space-x-3">
                      <div class="w-10 h-10 bg-accent-primary/20 rounded-lg flex items-center justify-center">
                        <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
                        </svg>
                      </div>
                      <div>
                        <p class="font-medium text-white">设计师</p>
                        <p class="text-xs text-gray-400">艺术美感调整</p>
                      </div>
                    </div>
                  </button>
                  
                  <button 
                    @click="switchRole('pattern-maker')"
                    :class="[
                      'w-full px-4 py-3 glow-card hover:bg-white/10 rounded-lg text-left transition-all duration-300 cursor-pointer',
                      currentRole === 'pattern-maker' ? 'border-2 border-accent-cta' : ''
                    ]"
                  >
                    <div class="flex items-center space-x-3">
                      <div class="w-10 h-10 bg-accent-cta/20 rounded-lg flex items-center justify-center">
                        <svg class="w-5 h-5 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                        </svg>
                      </div>
                      <div>
                        <p class="font-medium text-white">版师</p>
                        <p class="text-xs text-gray-400">版型完整性</p>
                      </div>
                    </div>
                  </button>
                  
                  <button 
                    @click="switchRole('craftsman')"
                    :class="[
                      'w-full px-4 py-3 glow-card hover:bg-white/10 rounded-lg text-left transition-all duration-300 cursor-pointer',
                      currentRole === 'craftsman' ? 'border-2 border-accent-vibrant' : ''
                    ]"
                  >
                    <div class="flex items-center space-x-3">
                      <div class="w-10 h-10 bg-accent-vibrant/20 rounded-lg flex items-center justify-center">
                        <svg class="w-5 h-5 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                        </svg>
                      </div>
                      <div>
                        <p class="font-medium text-white">工艺师</p>
                        <p class="text-xs text-gray-400">生产可行性</p>
                      </div>
                    </div>
                  </button>
                </div>
              </div>
            </div>
            
            <!-- 中间：画布区域 -->
            <div class="lg:col-span-2">
              <div class="glow-card rounded-xl p-6">
                <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden">
                  <!-- 预览图片 -->
                  <img 
                    v-if="showPreviewImage"
                    :src="previewImageUrl" 
                    class="absolute inset-0 w-full h-full object-contain rounded-xl z-0" 
                    alt="协同预览"
                    @load="imageLoaded = true"
                    @error="handlePreviewImageError"
                  >
                  <!-- Canvas画布层 -->
                  <canvas ref="canvasRef" class="w-full h-full cursor-crosshair relative z-10" :style="{ opacity: showPreviewImage ? 0.7 : 1 }"></canvas>
                  <!-- 图片加载失败时的提示 -->
                  <div v-if="showPreviewImage && !imageLoaded" class="absolute inset-0 flex items-center justify-center bg-black/50 rounded-xl z-20">
                    <div class="text-center">
                      <p class="text-white font-medium mb-2">预览图片加载失败</p>
                      <p class="text-xs text-gray-400">请检查图片路径: {{ previewImageUrl }}</p>
                    </div>
                  </div>
                  <!-- 节点编辑提示 -->
                  <div v-if="showNodeHint" class="absolute top-4 left-4 bg-black/80 text-white px-4 py-2 rounded-lg text-sm z-30">
                    点击节点进行拖拽调整
                  </div>
                </div>
                
                <!-- 工具栏 -->
                <div class="mt-6 flex flex-wrap gap-3">
                  <button 
                    @click="toggleLayer('outline')"
                    :class="[
                      'px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                      activeLayers.outline ? 'border-2 border-accent-primary' : ''
                    ]"
                  >
                    <span class="flex items-center space-x-2">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                      </svg>
                      <span>轮廓</span>
                    </span>
                  </button>
                  <button 
                    @click="toggleLayer('stitch')"
                    :class="[
                      'px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                      activeLayers.stitch ? 'border-2 border-accent-primary' : ''
                    ]"
                  >
                    <span class="flex items-center space-x-2">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                      <span>针迹</span>
                    </span>
                  </button>
                  <button 
                    @click="toggleLayer('path')"
                    :class="[
                      'px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer',
                      activeLayers.path ? 'border-2 border-accent-primary' : ''
                    ]"
                  >
                    <span class="flex items-center space-x-2">
                      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
                      </svg>
                      <span>路径</span>
                    </span>
                  </button>
                  <button @click="resetView" class="px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer">
                    重置视图
                  </button>
                </div>
              </div>
            </div>
            
            <!-- 右侧：参数调整面板 -->
            <div class="lg:col-span-1">
              <div class="glow-card rounded-xl p-6 sticky top-24">
                <h3 class="text-lg font-heading font-semibold mb-4 text-white">{{ roleTitles[currentRole] }}</h3>
                
                <!-- 设计师面板 -->
                <div v-if="currentRole === 'designer'" class="space-y-4">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">颜色调整</label>
                    <input v-model="designerParams.color" type="color" class="w-full h-10 rounded-lg cursor-pointer">
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">亮度</label>
                    <input v-model.number="designerParams.brightness" type="range" min="0" max="100" class="w-full">
                    <span class="text-xs text-gray-500">{{ designerParams.brightness }}</span>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">对比度</label>
                    <input v-model.number="designerParams.contrast" type="range" min="0" max="100" class="w-full">
                    <span class="text-xs text-gray-500">{{ designerParams.contrast }}</span>
                  </div>
                </div>
                
                <!-- 版师面板 -->
                <div v-if="currentRole === 'pattern-maker'" class="space-y-4">
                  <!-- Wilcom专业版集成 -->
                  <div class="p-5 bg-gradient-to-br from-accent-cta/20 via-accent-primary/20 to-accent-vibrant/20 rounded-xl border-2 border-accent-cta/50 mb-4 relative overflow-hidden">
                    <div class="absolute top-0 right-0 w-20 h-20 bg-accent-cta/10 rounded-full blur-2xl"></div>
                    <div class="absolute bottom-0 left-0 w-16 h-16 bg-accent-primary/10 rounded-full blur-xl"></div>
                    
                    <div class="relative z-10">
                      <div class="flex items-center space-x-2 mb-3">
                        <div class="w-10 h-10 bg-accent-cta/30 rounded-lg flex items-center justify-center">
                          <svg class="w-6 h-6 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                          </svg>
                        </div>
                        <div>
                          <h4 class="text-base font-heading font-bold text-white">Wilcom专业版</h4>
                          <p class="text-xs text-gray-400">传统刺绣软件</p>
                        </div>
                      </div>
                      <p class="text-sm text-gray-300 mb-4 leading-relaxed">DST文件已自动同步，可在Wilcom中进行专业级精细调整，支持针法优化、路径精修等高级功能</p>
                      <button @click="openInWilcom" class="w-full px-4 py-3 neon-button text-white rounded-lg text-sm font-semibold transition-all duration-300 cursor-pointer flex items-center justify-center space-x-2 shadow-lg hover:shadow-xl">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14" />
                        </svg>
                        <span>在Wilcom中打开</span>
                      </button>
                      <div class="mt-4 pt-4 border-t border-accent-cta/30 space-y-2">
                        <div class="flex items-center space-x-2 text-xs">
                          <svg class="w-4 h-4 text-accent-cta flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                          </svg>
                          <span class="text-gray-300">自动同步DST文件到Wilcom</span>
                        </div>
                        <div class="flex items-center space-x-2 text-xs">
                          <svg class="w-4 h-4 text-accent-cta flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                          </svg>
                          <span class="text-gray-300">实时协作状态同步</span>
                        </div>
                        <div class="flex items-center space-x-2 text-xs">
                          <svg class="w-4 h-4 text-accent-cta flex-shrink-0" fill="currentColor" viewBox="0 0 20 20">
                            <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                          </svg>
                          <span class="text-gray-300">修改后自动重新上传到平台</span>
                        </div>
                      </div>
                    </div>
                  </div>
                  
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">针法类型</label>
                    <select v-model="patternMakerParams.stitchType" class="w-full glow-card rounded-lg px-3 py-2 text-white bg-transparent text-sm focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>平针</option>
                      <option>回针</option>
                      <option>锁边</option>
                      <option>填充</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">针迹密度</label>
                    <input v-model.number="patternMakerParams.stitchDensity" type="range" min="1" max="10" class="w-full">
                    <span class="text-xs text-gray-500">{{ patternMakerParams.stitchDensity }}</span>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">路径连接</label>
                    <select v-model="patternMakerParams.pathConnection" class="w-full glow-card rounded-lg px-3 py-2 text-white bg-transparent text-sm focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>自动连接</option>
                      <option>最短路径</option>
                      <option>顺序连接</option>
                    </select>
                  </div>
                </div>
                
                <!-- 工艺师面板 -->
                <div v-if="currentRole === 'craftsman'" class="space-y-4">
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">面料类型</label>
                    <select v-model="craftsmanParams.fabricType" class="w-full glow-card rounded-lg px-3 py-2 text-white bg-transparent text-sm focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>棉布</option>
                      <option>丝绸</option>
                      <option>麻布</option>
                      <option>化纤</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">面料厚度</label>
                    <input v-model.number="craftsmanParams.fabricThickness" type="range" min="1" max="10" class="w-full">
                    <span class="text-xs text-gray-500">{{ craftsmanParams.fabricThickness }}</span>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">绣线直径</label>
                    <select v-model="craftsmanParams.threadDiameter" class="w-full glow-card rounded-lg px-3 py-2 text-white bg-transparent text-sm focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer">
                      <option>0.3mm</option>
                      <option>0.5mm</option>
                      <option>0.8mm</option>
                      <option>1.0mm</option>
                    </select>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">缩率补偿</label>
                    <button @click="openMaterialDatabase" class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
                      查看材料数据库
                    </button>
                  </div>
                  <div>
                    <label class="block text-sm text-gray-400 mb-2">绣线色卡</label>
                    <button @click="openColorDatabase" class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
                      查看色卡库
                    </button>
                  </div>
                </div>
                
                <!-- 实时预览按钮 -->
                <button @click="previewChanges" class="w-full mt-6 neon-button px-4 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                  实时预览调整
                </button>
                
                <!-- Wilcom状态显示 -->
                <div class="mt-4 pt-4 border-t border-accent-primary/20">
                  <div class="flex items-center justify-between text-xs">
                    <span class="text-gray-400">Wilcom状态:</span>
                    <span :class="[
                      'text-xs',
                      wilcomStatus === 'opened' ? 'text-accent-vibrant font-semibold' : 'text-gray-500'
                    ]">
                      {{ wilcomStatus === 'opened' ? '已打开' : '未打开' }}
                    </span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import { useWilcom } from '@/composables/useWilcom'

const router = useRouter()
const route = useRoute()
const { openInWilcom, wilcomStatus } = useWilcom()

const currentRole = ref<'designer' | 'pattern-maker' | 'craftsman'>('designer')
const showNodeHint = ref(false)
const canvasRef = ref<HTMLCanvasElement | null>(null)

// 从路由参数或localStorage获取AI生成的图片URL
const getPreviewImageUrl = () => {
  // 优先从路由query参数获取
  if (route.query.imageUrl) {
    return route.query.imageUrl as string
  }
  
  // 从localStorage获取（如果是从AI生成页面跳转过来）
  const storedImageUrl = localStorage.getItem('ai_generated_image_url')
  if (storedImageUrl) {
    return storedImageUrl
  }
  
  // 默认占位图片
  return '/images/collaboration-preview/result.jpg'
}

const previewImageUrl = ref(getPreviewImageUrl())
const showPreviewImage = ref(true) // 是否显示预览图片，设为false则只显示canvas
const imageLoaded = ref(false)

const roleTitles = {
  'designer': '设计师参数',
  'pattern-maker': '版师参数',
  'craftsman': '工艺师参数'
}

const activeLayers = ref({
  outline: true,
  stitch: true,
  path: true
})

const designerParams = ref({
  color: '#8B5CF6',
  brightness: 50,
  contrast: 50
})

const patternMakerParams = ref({
  stitchType: '平针',
  stitchDensity: 5,
  pathConnection: '自动连接'
})

const craftsmanParams = ref({
  fabricType: '棉布',
  fabricThickness: 5,
  threadDiameter: '0.5mm'
})

function switchRole(role: 'designer' | 'pattern-maker' | 'craftsman') {
  currentRole.value = role
}

function toggleLayer(layer: 'outline' | 'stitch' | 'path') {
  activeLayers.value[layer] = !activeLayers.value[layer]
}

function resetView() {
  // TODO: 重置画布视图
}

function previewChanges() {
  // TODO: 实时预览调整效果
  alert('正在实时预览调整效果...')
}

const isFinalized = ref(false)

function finalizeDesign() {
  // TODO: 最终定稿
  if (confirm('确定要最终定稿吗？定稿后可以进入下单流程。')) {
    isFinalized.value = true
    alert('设计已定稿！现在可以进入下单流程了。')
  }
}

function goToOrderFlow() {
  // 跳转到定制下单流程，传递定稿后的设计信息
  router.push({
    path: '/custom-order-flow',
    query: {
      source: 'collaboration',
      finalized: 'true',
      imageUrl: previewImageUrl.value
    }
  })
}

function openMaterialDatabase() {
  alert('打开材料数据库：\n- 面料缩率补偿曲线\n- 不同面料的工艺参数\n- 历史打样数据')
}

function openColorDatabase() {
  alert('打开色卡库：\n- 标准色线色卡\n- 自定义颜色\n- 历史配色方案')
}

function handlePreviewImageError() {
  imageLoaded.value = false
  console.warn('预览图片加载失败，请检查图片路径:', previewImageUrl.value)
}

function initCanvas() {
  if (!canvasRef.value) return
  
  const canvas = canvasRef.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  // 设置画布大小
  canvas.width = canvas.offsetWidth
  canvas.height = canvas.offsetHeight
  
  // 绘制示例内容
  ctx.strokeStyle = '#8B5CF6'
  ctx.lineWidth = 2
  ctx.beginPath()
  ctx.moveTo(50, 50)
  ctx.lineTo(canvas.width - 50, 50)
  ctx.lineTo(canvas.width - 50, canvas.height - 50)
  ctx.lineTo(50, canvas.height - 50)
  ctx.closePath()
  ctx.stroke()
}

onMounted(() => {
  initCanvas()
  
  // 如果有AI生成的图片URL，显示提示信息
  if (previewImageUrl.value && previewImageUrl.value !== '/images/collaboration-preview/result.jpg') {
    console.log('✅ 已加载AI生成的刺绣图案:', previewImageUrl.value)
    
    // 从query参数获取额外信息
    if (route.query.stitchCount || route.query.colorCount) {
      console.log('📊 刺绣参数:')
      console.log('   针数:', route.query.stitchCount)
      console.log('   颜色数:', route.query.colorCount)
    }
  }
})

onUnmounted(() => {
  // 清理资源
  // 注意：不清理localStorage中的图片URL，因为用户可能需要返回
})
</script>
