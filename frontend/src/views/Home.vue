<template>
  <div class="min-h-screen bg-black text-gray-100 antialiased">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- Hero Section -->
    <section class="relative min-h-screen flex items-center justify-center pt-16 overflow-hidden">
      <!-- Background gradient - 深黑到霓虹渐变 -->
      <div class="absolute inset-0 bg-gradient-to-br from-black via-purple-950/20 to-cyan-950/20"></div>
      
      <!-- 传统刺绣纹理背景 -->
      <div class="absolute inset-0 embroidery-pattern opacity-30"></div>
      
      
      <!-- AI粒子效果 -->
      <Particles container-id="particles-container" :count="30" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 传统花纹装饰 -->
      <TraditionalPattern />
      
      <!-- Animated background elements - 霓虹光晕 -->
      <div class="absolute inset-0 overflow-hidden">
        <div class="absolute top-20 left-10 w-96 h-96 bg-accent-primary/30 rounded-full blur-3xl float"></div>
        <div class="absolute bottom-20 right-10 w-[500px] h-[500px] bg-accent-cta/30 rounded-full blur-3xl float" style="animation-delay: 2s;"></div>
        <div class="absolute top-1/2 left-1/2 w-80 h-80 bg-accent-vibrant/20 rounded-full blur-3xl float" style="animation-delay: 4s; transform: translate(-50%, -50%);"></div>
      </div>
      
      <div class="relative z-10 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-20">
        <div class="text-center">
          <h1 class="text-6xl md:text-8xl font-heading font-bold mb-8">
            <span class="text-white">人机合一的</span>
            <br>
            <span class="gradient-text text-7xl md:text-9xl">数字绣娘定制平台</span>
          </h1>
          <p class="text-2xl md:text-3xl text-gray-200 mb-6 max-w-4xl mx-auto leading-relaxed">
            从图案设计、AI智能打版到驱动实体刺绣机完成绣制
          </p>
          <p class="text-xl md:text-2xl text-gray-300 mb-10 max-w-4xl mx-auto">
            全链路数字化联动，让创意触手可及
          </p>
          <!-- AI Powered Badge -->
          <div class="mb-8 flex justify-center">
            <div class="neon-border px-6 py-2 inline-flex items-center space-x-2">
              <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
              <span class="text-sm font-medium gradient-text">AI 驱动的艺术生成</span>
            </div>
          </div>
          
          <div class="flex flex-col sm:flex-row gap-4 justify-center items-center">
            <button @click="goToAIGeneration" class="neon-button px-8 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer relative overflow-hidden">
              <span class="relative z-10 flex items-center space-x-2">
                <span>立即体验 AI 生成</span>
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                </svg>
              </span>
            </button>
            <a href="#gallery" @click="scrollToSection('#gallery')" class="px-8 py-4 glow-card hover:bg-white/5 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer">
              浏览风格库
            </a>
          </div>
        </div>
        
        <!-- Hero Preview Image with Decorative Elements -->
        <div class="mt-20 max-w-5xl mx-auto relative">
          <!-- 装饰性针线图案 -->
          <div class="absolute -top-10 -left-10 w-32 h-32 opacity-20">
            <svg viewBox="0 0 100 100" class="w-full h-full">
              <path d="M20,50 Q50,20 80,50 Q50,80 20,50" stroke="url(#threadGradient)" stroke-width="2" fill="none"/>
              <circle cx="20" cy="50" r="3" fill="#8B5CF6"/>
              <circle cx="80" cy="50" r="3" fill="#06B6D4"/>
              <defs>
                <linearGradient id="threadGradient" x1="0%" y1="0%" x2="100%" y2="0%">
                  <stop offset="0%" style="stop-color:#8B5CF6;stop-opacity:1" />
                  <stop offset="100%" style="stop-color:#06B6D4;stop-opacity:1" />
                </linearGradient>
              </defs>
            </svg>
          </div>
          
          <div class="glow-card rounded-2xl p-8 relative overflow-hidden">
            <!-- 内部发光边框 -->
            <div class="absolute inset-0 rounded-2xl" style="background: linear-gradient(135deg, rgba(139,92,246,0.2), rgba(6,182,212,0.2)); filter: blur(20px); opacity: 0.5;"></div>
            
            <!-- 前后对比展示区域 - 优化版本 -->
            <div class="bg-gradient-to-br from-gray-900 via-purple-900/20 to-cyan-900/20 rounded-2xl p-6 md:p-8 relative overflow-hidden border border-purple-500/30 shadow-2xl">
              <!-- 背景网格纹理 -->
              <div class="absolute inset-0 opacity-10" style="background-image: linear-gradient(rgba(139,92,246,0.1) 1px, transparent 1px), linear-gradient(90deg, rgba(139,92,246,0.1) 1px, transparent 1px); background-size: 40px 40px;"></div>
              
              <!-- 背景光效 -->
              <div class="absolute inset-0 overflow-hidden">
                <div class="absolute top-0 left-1/4 w-72 h-72 bg-purple-500/20 rounded-full blur-3xl"></div>
                <div class="absolute bottom-0 right-1/4 w-72 h-72 bg-cyan-500/20 rounded-full blur-3xl"></div>
              </div>
              
              <!-- 标题 -->
              <div class="text-center mb-10 relative z-10">
                <h3 class="text-3xl md:text-4xl font-heading font-bold mb-3 bg-gradient-to-r from-purple-400 via-pink-400 to-cyan-400 bg-clip-text text-transparent">
                  所见即所绣
                </h3>
                <p class="text-sm md:text-base text-gray-300 font-medium">AI智能打版，精准还原设计</p>
                <!-- 标题装饰线 -->
                <div class="mt-4 flex items-center justify-center gap-3">
                  <div class="h-px w-20 bg-gradient-to-r from-transparent via-purple-400 to-transparent"></div>
                  <div class="w-2 h-2 bg-purple-400 rounded-full"></div>
                  <div class="h-px w-20 bg-gradient-to-r from-transparent via-cyan-400 to-transparent"></div>
                </div>
              </div>
              
              <!-- 对比图片容器 -->
              <div class="flex flex-col md:flex-row items-center justify-center gap-8 md:gap-12 relative z-10">
                <!-- 原图 - 左侧 -->
                <div class="flex-1 max-w-md relative">
                  <div class="relative">
                    <!-- 标签 -->
                    <div class="absolute -top-5 left-1/2 transform -translate-x-1/2 z-20 bg-gradient-to-r from-purple-600 to-pink-600 text-white px-6 py-2 rounded-full text-sm font-bold shadow-lg">
                      <span class="flex items-center gap-2">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                        </svg>
                        原图设计
                      </span>
                    </div>
                    <!-- 图片容器 -->
                    <div class="bg-white rounded-xl p-5 shadow-xl border-2 border-purple-500/50 relative overflow-hidden">
                      <div class="absolute inset-0 bg-gradient-to-br from-purple-500/10 to-transparent pointer-events-none"></div>
                      <img 
                        src="/images/hero/original-design.jpg" 
                        class="w-full h-auto rounded-lg object-contain relative z-10" 
                        alt="原图设计"
                        @error="handleImageError"
                      >
                    </div>
                  </div>
                </div>
                
                <!-- 转换流程图标 - 中间（优化版） -->
                <div class="flex flex-col items-center justify-center relative z-20 my-8 md:my-0">
                  <!-- 统一的光效背景 -->
                  <div class="absolute inset-0 flex items-center justify-center">
                    <div class="w-32 h-32 bg-gradient-to-r from-purple-500/30 to-cyan-500/30 rounded-full blur-2xl"></div>
                  </div>
                  
                  <!-- AI转换图标容器 -->
                  <div class="relative flex flex-col items-center">
                    <!-- 外圈旋转光环 -->
                    <div class="absolute w-24 h-24 border-2 border-transparent border-t-purple-500 border-r-cyan-500 rounded-full animate-spin" style="animation-duration: 3s;"></div>
                    
                    <!-- 内圈旋转光环 -->
                    <div class="absolute w-20 h-20 border-2 border-transparent border-b-pink-500 border-l-purple-500 rounded-full animate-spin" style="animation-duration: 2s; animation-direction: reverse;"></div>
                    
                    <!-- AI图标主体 -->
                    <div class="relative bg-gradient-to-br from-purple-600 via-pink-500 to-cyan-500 rounded-full p-6 shadow-2xl w-16 h-16 flex items-center justify-center">
                      <div class="absolute inset-0 bg-gradient-to-br from-white/30 to-transparent rounded-full"></div>
                      <svg class="w-8 h-8 text-white relative z-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M13 10V3L4 14h7v7l9-11h-7z" />
                      </svg>
                    </div>
                    
                    <!-- 转换文字 -->
                    <div class="text-center mt-4">
                      <p class="text-sm font-bold text-white mb-2">AI智能转换</p>
                      <div class="flex items-center justify-center gap-1.5">
                        <div class="w-1.5 h-1.5 bg-purple-400 rounded-full"></div>
                        <div class="w-1.5 h-1.5 bg-pink-400 rounded-full"></div>
                        <div class="w-1.5 h-1.5 bg-cyan-400 rounded-full"></div>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 连接线（移动端隐藏） -->
                  <div class="hidden md:block absolute left-1/2 top-1/2 transform -translate-x-1/2 -translate-y-1/2 w-full h-0.5 -z-10">
                    <div class="relative h-full">
                      <div class="absolute inset-0 bg-gradient-to-r from-purple-500/60 via-pink-500/60 to-cyan-500/60"></div>
                      <!-- 流动光点 -->
                      <div class="absolute top-0 w-12 h-full bg-white/80 rounded-full animate-slide-right" style="animation-duration: 2s; animation-iteration-count: infinite;"></div>
                    </div>
                  </div>
                </div>
                
                <!-- 刺绣结果 - 右侧 -->
                <div class="flex-1 max-w-md relative">
                  <div class="relative">
                    <!-- 标签 -->
                    <div class="absolute -top-5 right-1/2 transform translate-x-1/2 z-20 bg-gradient-to-r from-cyan-600 to-green-500 text-white px-6 py-2 rounded-full text-sm font-bold shadow-lg">
                      <span class="flex items-center gap-2">
                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                        </svg>
                        AI刺绣结果
                      </span>
                    </div>
                    <!-- 图片容器 -->
                    <div class="bg-white rounded-xl p-5 shadow-xl border-2 border-cyan-500/50 relative overflow-hidden">
                      <div class="absolute inset-0 bg-gradient-to-br from-cyan-500/10 to-transparent pointer-events-none"></div>
                      <img 
                        src="/images/hero/ai-embroidery-result.jpg" 
                        class="w-full h-auto rounded-lg object-contain relative z-10" 
                        alt="AI刺绣结果"
                        @error="handleImageError"
                      >
                      <!-- 刺绣纹理效果 -->
                      <div class="absolute inset-0 opacity-5 pointer-events-none" style="background-image: repeating-linear-gradient(45deg, transparent, transparent 2px, rgba(6,182,212,0.1) 2px, rgba(6,182,212,0.1) 4px);"></div>
                    </div>
                  </div>
                </div>
              </div>
              
              <!-- 底部说明 -->
              <div class="mt-10 text-center relative z-10">
                <div class="inline-flex items-center gap-3 px-6 py-3 bg-gradient-to-r from-purple-500/20 via-pink-500/20 to-cyan-500/20 rounded-xl border border-purple-400/30 backdrop-blur-sm">
                  <svg class="w-5 h-5 text-purple-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                  </svg>
                  <span class="text-sm font-semibold text-gray-200">传统工艺 × 人工智能</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 右下角装饰 -->
          <div class="absolute -bottom-5 -right-5 w-24 h-24 opacity-20">
            <svg viewBox="0 0 100 100" class="w-full h-full">
              <path d="M50,10 L60,40 L90,50 L60,60 L50,90 L40,60 L10,50 L40,40 Z" stroke="url(#decorGradient)" stroke-width="2" fill="none"/>
              <defs>
                <linearGradient id="decorGradient" x1="0%" y1="0%" x2="100%" y2="100%">
                  <stop offset="0%" style="stop-color:#8B5CF6;stop-opacity:1" />
                  <stop offset="50%" style="stop-color:#06B6D4;stop-opacity:1" />
                  <stop offset="100%" style="stop-color:#10B981;stop-opacity:1" />
                </linearGradient>
              </defs>
            </svg>
          </div>
        </div>
      </div>
    </section>
    
    <!-- AI Generation Preview Section -->
    <section id="features" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
      <div class="absolute inset-0" style="background: linear-gradient(180deg, transparent 0%, rgba(139,92,246,0.05) 50%, transparent 100%);"></div>
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-features" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-10 right-20 w-64 h-64 bg-accent-primary/20 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-10 left-20 w-80 h-80 bg-accent-cta/20 rounded-full blur-3xl float" style="animation-delay: 2s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">AI 智能生成</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-2xl mx-auto">
            上传图片或文字，选择刺绣针法和风格，AI 大模型为您生成专业刺绣图案
          </p>
        </div>
        
        <div class="grid md:grid-cols-2 gap-8 items-center">
          <div class="space-y-6">
            <div class="glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 hover:scale-105 relative overflow-hidden group">
              <!-- 刺绣针线装饰 -->
              <div class="absolute top-2 right-2 w-16 h-16 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <path d="M30,50 L50,30 L70,50 L50,70 Z" stroke="#8B5CF6" stroke-width="2" fill="none"/>
                  <circle cx="50" cy="50" r="3" fill="#8B5CF6"/>
                </svg>
              </div>
              <div class="flex items-start space-x-4 relative z-10">
                <div class="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-lg flex items-center justify-center relative">
                  <!-- 刺绣针图标 -->
                  <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                  </svg>
                  <!-- 针线装饰 -->
                  <div class="absolute -top-1 -right-1 w-3 h-3 bg-accent-primary rounded-full animate-ping"></div>
                </div>
                <div>
                  <h3 class="text-xl font-heading font-semibold mb-2 flex items-center">
                    <span>图片上传</span>
                    <span class="ml-2 text-xs px-2 py-0.5 bg-accent-primary/20 text-accent-primary rounded-full">AI识别</span>
                  </h3>
                  <p class="text-gray-300">支持 JPG、PNG 等多种格式，AI 自动识别图案并转换为刺绣样式</p>
                </div>
              </div>
            </div>
            
            <div class="glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 hover:scale-105 relative overflow-hidden group">
              <!-- 刺绣路径装饰 -->
              <div class="absolute top-2 right-2 w-20 h-20 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <path d="M20,20 Q50,50 80,20 Q50,50 80,80 Q50,50 20,80 Q50,50 20,20" stroke="#06B6D4" stroke-width="2" fill="none"/>
                </svg>
              </div>
              <div class="flex items-start space-x-4 relative z-10">
                <div class="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-lg flex items-center justify-center relative">
                  <svg class="w-6 h-6 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                  </svg>
                  <div class="absolute -bottom-1 -left-1 w-2 h-2 bg-accent-cta rounded-full animate-pulse"></div>
                </div>
                <div>
                  <h3 class="text-xl font-heading font-semibold mb-2 flex items-center">
                    <span>智能打版</span>
                    <span class="ml-2 text-xs px-2 py-0.5 bg-accent-cta/20 text-accent-cta rounded-full">路径优化</span>
                  </h3>
                  <p class="text-gray-300">AI 大模型分析图案特征，自动生成最优刺绣路径和针法组合</p>
                </div>
              </div>
            </div>
            
            <div class="glow-card rounded-xl p-6 cursor-pointer transition-all duration-300 hover:scale-105 relative overflow-hidden group">
              <!-- 实时预览装饰 -->
              <div class="absolute top-2 right-2 w-18 h-18 opacity-10 group-hover:opacity-20 transition-opacity">
                <svg viewBox="0 0 100 100" class="w-full h-full">
                  <rect x="20" y="20" width="60" height="60" stroke="#10B981" stroke-width="2" fill="none"/>
                  <circle cx="50" cy="50" r="15" stroke="#10B981" stroke-width="2" fill="none"/>
                  <circle cx="50" cy="50" r="5" fill="#10B981"/>
                </svg>
              </div>
              <div class="flex items-start space-x-4 relative z-10">
                <div class="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-lg flex items-center justify-center relative">
                  <svg class="w-6 h-6 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                  </svg>
                  <div class="absolute inset-0 border-2 border-accent-vibrant/30 rounded-lg animate-ping"></div>
                </div>
                <div>
                  <h3 class="text-xl font-heading font-semibold mb-2 flex items-center">
                    <span>实时预览</span>
                    <span class="ml-2 text-xs px-2 py-0.5 bg-accent-vibrant/20 text-accent-vibrant rounded-full">实时</span>
                  </h3>
                  <p class="text-gray-300">生成过程中实时查看效果，支持调整参数和风格，所见即所得</p>
                </div>
              </div>
            </div>
          </div>
          
          <div class="glow-card rounded-2xl p-8">
            <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl flex items-center justify-center relative overflow-hidden">
              <div class="absolute inset-0 flex items-center justify-center">
                <div class="w-64 h-64 border-2 border-dashed border-accent-primary/30 rounded-xl flex items-center justify-center">
                  <div class="text-center">
                    <svg class="w-16 h-16 mx-auto mb-4 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M7 16a4 4 0 01-.88-7.903A5 5 0 1115.9 6L16 6a5 5 0 011 9.9M15 13l-3-3m0 0l-3 3m3-3v12" />
                    </svg>
                    <p class="text-gray-400 mb-2">拖拽上传图片</p>
                    <p class="text-sm text-gray-500">或点击选择文件</p>
                  </div>
                </div>
              </div>
            </div>
            <div class="mt-6 space-y-3">
              <div class="flex items-center justify-between text-sm">
                <span class="text-gray-400">生成进度</span>
                <span class="text-accent-primary">65%</span>
              </div>
              <div class="w-full bg-white/5 rounded-full h-2 overflow-hidden">
                <div class="h-full bg-gradient-to-r from-accent-primary to-accent-cta rounded-full transition-all duration-300" style="width: 65%;"></div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- Style Gallery Section -->
    <section id="gallery" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-15"></div>
      <TraditionalPattern />
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-gallery" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/4 left-1/4 w-72 h-72 bg-accent-vibrant/15 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-1/4 right-1/4 w-96 h-96 bg-accent-primary/20 rounded-full blur-3xl float" style="animation-delay: 3s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">风格画廊</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-2xl mx-auto">
            探索丰富的刺绣风格库，从传统到现代，从简约到繁复
          </p>
        </div>
        
        <!-- Filter Tabs -->
        <div class="flex flex-wrap justify-center gap-4 mb-8">
          <button 
            @click="filterGallery('all')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-200 cursor-pointer',
              activeFilter === 'all' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            全部
          </button>
          <button 
            @click="filterGallery('traditional')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'traditional' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            传统
          </button>
          <button 
            @click="filterGallery('modern')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'modern' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            现代
          </button>
          <button 
            @click="filterGallery('abstract')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'abstract' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            抽象
          </button>
          <button 
            @click="filterGallery('floral')" 
            :class="[
              'px-6 py-2 rounded-lg font-medium transition-all duration-300 cursor-pointer',
              activeFilter === 'floral' 
                ? 'neon-button text-white' 
                : 'glow-card hover:bg-white/10 text-gray-300'
            ]"
          >
            花卉
          </button>
        </div>
        
        <!-- 四大名绣展示区域 -->
        <div class="mb-12">
          <h3 class="text-2xl font-heading font-bold mb-6 text-center gradient-text">四大名绣流派</h3>
          <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6">
            <!-- 苏绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20 rounded-lg mb-4 relative overflow-hidden">
                <img src="/images/gallery/suxiu.png" class="w-full h-full object-cover" alt="苏绣">

                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-primary/80 rounded text-xs text-white font-semibold">双面绣</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">苏绣</h4>
              <p class="text-sm text-gray-400 mb-2">江苏苏州</p>
              <p class="text-xs text-gray-500 leading-relaxed">以"平、齐、细、密"著称，图案秀丽，色彩文雅。双面绣猫更是其代表作，神态逼真，毛色光泽。</p>
              <button @click="selectEmbroideryStyle('suxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 湘绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-cta/20 to-accent-vibrant/20 rounded-lg mb-4 relative overflow-hidden">
                <img src="/images/gallery/xiangxiu.png" class="w-full h-full object-cover" alt="湘绣">
                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-cta/80 rounded text-xs text-white font-semibold">鬅毛针</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">湘绣</h4>
              <p class="text-sm text-gray-400 mb-2">湖南长沙</p>
              <p class="text-xs text-gray-500 leading-relaxed">形象生动，色彩鲜艳，尤以"鬅毛针"绣出的猛虎闻名天下。质感强烈，威风凛凛，极具表现力。</p>
              <button @click="selectEmbroideryStyle('xiangxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 粤绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-vibrant/20 to-accent-primary/20 rounded-lg mb-4 relative overflow-hidden">
                <img src="/images/gallery/yuexiu.png" class="w-full h-full object-cover" alt="粤绣">

                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-vibrant/80 rounded text-xs text-white font-semibold">浮雕效果</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">粤绣</h4>
              <p class="text-sm text-gray-400 mb-2">广东地区</p>
              <p class="text-xs text-gray-500 leading-relaxed">构图繁而不乱，色彩富丽堂皇。善用金银线垫高刺绣，产生浮雕般的立体效果，龙凤题材最为经典。</p>
              <button @click="selectEmbroideryStyle('yuexiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
            
            <!-- 蜀绣 -->
            <div class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
              <div class="aspect-square bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20 rounded-lg mb-4 relative overflow-hidden">
                <img src="/images/gallery/shuxiu.png" class="w-full h-full object-cover" alt="蜀绣">

                <div class="absolute top-2 right-2 px-2 py-1 bg-accent-secondary/80 rounded text-xs text-white font-semibold">晕针技法</div>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white mb-1">蜀绣</h4>
              <p class="text-sm text-gray-400 mb-2">四川成都</p>
              <p class="text-xs text-gray-500 leading-relaxed">针法严谨，片线光亮。色彩明丽清秀，擅长表现鲤鱼和熊猫的灵动与憨态，具有浓郁的地方特色。</p>
              <button @click="selectEmbroideryStyle('shuxiu')" class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                查看风格
              </button>
            </div>
          </div>
        </div>
        
        <!-- Gallery Grid -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          <div 
            v-for="item in displayedGalleryItems" 
            :key="item.id"
            class="glow-card rounded-xl overflow-hidden transition-all duration-300 cursor-pointer group hover:scale-105"
          >
            <div :class="['aspect-square relative overflow-hidden', item.gradient]">
              <img
                  v-if="item.imageUrl"
                  :src="item.imageUrl"
                  class="w-full h-full object-cover"
                  :alt="item.name"
              >
              <div v-else class="absolute inset-0 flex items-center justify-center">
                <svg class="w-24 h-24" :class="item.iconColor" fill="currentColor" viewBox="0 0 24 24">
                  <path :d="item.iconPath"/>
                </svg>
              </div>
              <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity duration-200 flex items-center justify-center">
                <button @click="addToCart(item)" class="px-4 py-2 bg-accent-primary text-white rounded-lg font-medium hover:bg-accent-secondary transition-colors duration-200 cursor-pointer">加入购物车</button>
              </div>
            </div>
            <div class="p-4">
              <h3 class="font-heading font-semibold mb-1">{{ item.name }}</h3>
              <p class="text-sm text-gray-400">{{ item.description }}</p>
              <div class="mt-3 flex items-center justify-between">
                <span class="text-accent-primary font-semibold">¥{{ item.price }}</span>
                <button class="text-gray-400 hover:text-white transition-colors duration-200 cursor-pointer">
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                  </svg>
                </button>
              </div>
            </div>
          </div>
        </div>
        
        <div class="text-center mt-12">
          <button @click="router.push('/gallery-more')" class="px-8 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
            查看更多风格
          </button>
        </div>
      </div>
    </section>
    
    <!-- Creator Tools Section -->
    <section id="tools" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0" style="background: radial-gradient(ellipse at center, rgba(6,182,212,0.1) 0%, transparent 70%);"></div>
      <div class="absolute inset-0 embroidery-pattern opacity-10"></div>
      <TraditionalPattern />
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-tools" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/2 left-1/3 w-80 h-80 bg-accent-cta/20 rounded-full blur-3xl float" style="animation-delay: 1.5s;"></div>
      <div class="absolute bottom-1/3 right-1/3 w-64 h-64 bg-accent-vibrant/15 rounded-full blur-3xl float" style="animation-delay: 4s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">创作者工具</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-2xl mx-auto">
            专业级创作工具，让您的创意从设计到生产无缝衔接
          </p>
        </div>
        
        <div class="grid md:grid-cols-3 gap-8">
          <!-- Tool 1: 图案编辑器 -->
          <div @click="goToEditor" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
            <!-- 装饰性刺绣图案 -->
            <div class="absolute top-4 right-4 w-20 h-20 opacity-10 group-hover:opacity-20 transition-opacity">
              <svg viewBox="0 0 100 100" class="w-full h-full">
                <path d="M50,10 Q70,30 50,50 Q30,30 50,10" stroke="#8B5CF6" stroke-width="2" fill="none"/>
                <path d="M50,50 Q70,70 50,90 Q30,70 50,50" stroke="#06B6D4" stroke-width="2" fill="none"/>
              </svg>
            </div>
            <div class="w-16 h-16 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
              <svg class="w-8 h-8 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
              </svg>
            </div>
            <h3 class="text-2xl font-heading font-semibold mb-3 text-white">图案编辑器</h3>
            <p class="text-gray-300 mb-4">强大的在线编辑工具，支持图层、路径调整、颜色填充等专业功能</p>
            <ul class="space-y-2 text-sm text-gray-400">
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                多图层编辑
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                矢量路径调整
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                实时预览
              </li>
            </ul>
          </div>
          
          <!-- Tool 2: AI 智能体 -->
          <div @click="goToAIGeneration" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
            <!-- AI神经网络装饰 -->
            <div class="absolute top-4 right-4 w-24 h-24 opacity-10 group-hover:opacity-20 transition-opacity">
              <svg viewBox="0 0 100 100" class="w-full h-full">
                <circle cx="30" cy="30" r="3" fill="#06B6D4"/>
                <circle cx="70" cy="30" r="3" fill="#10B981"/>
                <circle cx="50" cy="70" r="3" fill="#8B5CF6"/>
                <line x1="30" y1="30" x2="50" y2="70" stroke="#06B6D4" stroke-width="1"/>
                <line x1="70" y1="30" x2="50" y2="70" stroke="#10B981" stroke-width="1"/>
                <line x1="30" y1="30" x2="70" y2="30" stroke="#8B5CF6" stroke-width="1"/>
              </svg>
            </div>
            <div class="w-16 h-16 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
              <svg class="w-8 h-8 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
              </svg>
            </div>
            <h3 class="text-2xl font-heading font-semibold mb-3 text-white">AI 智能体</h3>
            <p class="text-gray-300 mb-4">接入先进的 AI 刺绣大模型，智能分析、优化和生成刺绣方案</p>
            <ul class="space-y-2 text-sm text-gray-400">
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                智能路径规划
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                针法自动优化
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                材料用量估算
              </li>
            </ul>
          </div>
          
          <!-- Tool 3: 生产协同 -->
          <div @click="goToCollaboration" class="glow-card rounded-xl p-8 transition-all duration-300 cursor-pointer hover:scale-105 relative overflow-hidden group">
            <!-- 机械齿轮装饰 -->
            <div class="absolute top-4 right-4 w-20 h-20 opacity-10 group-hover:opacity-20 transition-opacity">
              <svg viewBox="0 0 100 100" class="w-full h-full">
                <circle cx="50" cy="50" r="30" stroke="#10B981" stroke-width="2" fill="none"/>
                <circle cx="50" cy="50" r="10" fill="#10B981"/>
                <rect x="45" y="20" width="10" height="15" fill="#10B981"/>
                <rect x="45" y="65" width="10" height="15" fill="#10B981"/>
                <rect x="20" y="45" width="15" height="10" fill="#10B981"/>
                <rect x="65" y="45" width="15" height="10" fill="#10B981"/>
              </svg>
            </div>
            <div class="w-16 h-16 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-xl flex items-center justify-center mb-6 relative z-10">
              <svg class="w-8 h-8 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
              </svg>
            </div>
            <h3 class="text-2xl font-heading font-semibold mb-3 text-white">生产协同</h3>
            <p class="text-gray-300 mb-4">连接实体刺绣机，实现从设计到生产的全链路数字化联动</p>
            <ul class="space-y-2 text-sm text-gray-400">
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                设备远程控制
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                实时进度监控
              </li>
              <li class="flex items-center">
                <svg class="w-4 h-4 mr-2 text-accent-primary" fill="currentColor" viewBox="0 0 20 20">
                  <path fill-rule="evenodd" d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z" clip-rule="evenodd" />
                </svg>
                B端生产管理
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 高端刺绣定制板块 -->
    <section id="luxury-custom" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
      <TraditionalPattern />
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-luxury" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/4 right-1/4 w-96 h-96 bg-accent-primary/20 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-1/4 left-1/4 w-80 h-80 bg-accent-vibrant/15 rounded-full blur-3xl float" style="animation-delay: 3s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">高端刺绣定制</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-3xl mx-auto">
            一比一复刻奢侈品、文物、龙袍、传统服装等珍贵艺术品<br>
            让普通用户也能成为专业设计师，传承千年工艺之美
          </p>
        </div>
        
        <!-- 四大定制类别 -->
        <div class="grid md:grid-cols-2 lg:grid-cols-4 gap-6 mb-12">
          <!-- 奢侈品复刻 -->
          <div @click="openLuxuryCustom('replica')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group relative overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-accent-primary/10 to-accent-secondary/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
            <div class="relative z-10">
              <div class="w-16 h-16 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-xl flex items-center justify-center mb-4">
                <svg class="w-8 h-8 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
                </svg>
              </div>
              <h3 class="text-xl font-heading font-semibold mb-2 text-white">奢侈品复刻</h3>
              <p class="text-sm text-gray-400 mb-4">一比一精准复刻国际奢侈品牌刺绣工艺，还原每一个细节</p>
              <ul class="space-y-1 text-xs text-gray-500">
                <li>• 爱马仕、香奈儿等品牌复刻</li>
                <li>• 高精度图案识别</li>
                <li>• 专业级工艺还原</li>
              </ul>
              <button class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                开始定制
              </button>
            </div>
          </div>
          
          <!-- 文物复刻 -->
          <div @click="openLuxuryCustom('artifact')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group relative overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-accent-cta/10 to-accent-vibrant/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
            <div class="relative z-10">
              <div class="w-16 h-16 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-xl flex items-center justify-center mb-4">
                <svg class="w-8 h-8 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
              </div>
              <h3 class="text-xl font-heading font-semibold mb-2 text-white">文物复刻</h3>
              <p class="text-sm text-gray-400 mb-4">数字化保护与传承，让千年文物以刺绣形式重现光彩</p>
              <ul class="space-y-1 text-xs text-gray-500">
                <li>• 博物馆级精度还原</li>
                <li>• 3D扫描技术</li>
                <li>• 传统工艺数字化</li>
              </ul>
              <button class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                开始定制
              </button>
            </div>
          </div>
          
          <!-- 龙袍定制 -->
          <div @click="openLuxuryCustom('dragon-robe')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group relative overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-accent-vibrant/10 to-accent-primary/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
            <div class="relative z-10">
              <div class="w-16 h-16 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-xl flex items-center justify-center mb-4">
                <svg class="w-8 h-8 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4M7.835 4.697a3.42 3.42 0 001.946-.806 3.42 3.42 0 014.438 0 3.42 3.42 0 001.946.806 3.42 3.42 0 013.138 3.138 3.42 3.42 0 00.806 1.946 3.42 3.42 0 010 4.438 3.42 3.42 0 00-.806 1.946 3.42 3.42 0 01-3.138 3.138 3.42 3.42 0 00-1.946.806 3.42 3.42 0 01-4.438 0 3.42 3.42 0 00-1.946-.806 3.42 3.42 0 01-3.138-3.138 3.42 3.42 0 00-.806-1.946 3.42 3.42 0 010-4.438 3.42 3.42 0 00.806-1.946 3.42 3.42 0 013.138-3.138z" />
                </svg>
              </div>
              <h3 class="text-xl font-heading font-semibold mb-2 text-white">龙袍定制</h3>
              <p class="text-sm text-gray-400 mb-4">皇家工艺重现，九爪金龙、祥云纹样，尽显帝王风范</p>
              <ul class="space-y-1 text-xs text-gray-500">
                <li>• 宫廷级工艺标准</li>
                <li>• 金线银线搭配</li>
                <li>• 传统纹样数据库</li>
              </ul>
              <button class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                开始定制
              </button>
            </div>
          </div>
          
          <!-- 传统服装定制 -->
          <div @click="openLuxuryCustom('traditional-clothing')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group relative overflow-hidden">
            <div class="absolute inset-0 bg-gradient-to-br from-accent-secondary/10 to-accent-cta/10 opacity-0 group-hover:opacity-100 transition-opacity duration-300"></div>
            <div class="relative z-10">
              <div class="w-16 h-16 bg-gradient-to-br from-accent-secondary/30 to-accent-secondary/10 rounded-xl flex items-center justify-center mb-4">
                <svg class="w-8 h-8 text-accent-secondary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </div>
              <h3 class="text-xl font-heading font-semibold mb-2 text-white">传统服装定制</h3>
              <p class="text-sm text-gray-400 mb-4">传统服饰刺绣工艺，让古典美学在现代焕发新生</p>
              <ul class="space-y-1 text-xs text-gray-500">
                <li>• 汉服、旗袍、少数民族服饰</li>
                <li>• 各朝代形制还原</li>
                <li>• 传统纹样库与个性化定制</li>
              </ul>
              <button class="mt-4 w-full px-4 py-2 neon-button text-white rounded-lg text-sm font-medium transition-all duration-300 cursor-pointer">
                开始定制
              </button>
            </div>
          </div>
        </div>
        
        <!-- 核心优势 -->
        <div class="grid md:grid-cols-3 gap-6">
          <div class="glow-card rounded-xl p-6">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-accent-primary/20 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">AI智能识别</h4>
            </div>
            <p class="text-sm text-gray-400">深度学习算法精准识别图案细节，自动提取刺绣要素，确保一比一还原</p>
          </div>
          
          <div class="glow-card rounded-xl p-6">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-accent-cta/20 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">专业工艺师团队</h4>
            </div>
            <p class="text-sm text-gray-400">资深工艺师全程指导，确保每个细节都符合传统工艺标准</p>
          </div>
          
          <div class="glow-card rounded-xl p-6">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-accent-vibrant/20 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">人人都是设计师</h4>
            </div>
            <p class="text-sm text-gray-400">零基础也能创作，AI辅助设计让每个人都能实现自己的艺术梦想</p>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 刺绣GPT板块 -->
    <section id="knowledge-graph" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-15"></div>
      <TraditionalPattern />
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-knowledge" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/3 left-1/3 w-96 h-96 bg-accent-cta/20 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-1/3 right-1/3 w-80 h-80 bg-accent-primary/15 rounded-full blur-3xl float" style="animation-delay: 4s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 relative z-10">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-6">
            <span class="gradient-text">EmbroideryGPT · 刺绣智能中枢</span>
          </h2>
          <p class="text-xl md:text-2xl text-gray-300 max-w-4xl mx-auto leading-relaxed mb-4">
            基于结构化刺绣知识，融合 RAG 智能生成技术<br>
            让 AI 理解工艺、纹样与文化，推动传统刺绣数字化创新
          </p>
          <!-- RAG技术标识 -->
          <div class="inline-flex items-center gap-2 px-4 py-2 bg-gradient-to-r from-purple-500/20 via-pink-500/20 to-cyan-500/20 rounded-full border border-purple-400/30 mt-4">
            <svg class="w-5 h-5 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
            </svg>
            <span class="text-sm font-semibold text-purple-300">进入 EmbroideryGPT</span>
          </div>
        </div>
        
        <!-- 知识图谱可视化区域 -->
        <div class="mb-12">
          <div class="glow-card rounded-2xl p-8 relative overflow-hidden">
            <!-- 背景装饰 -->
            <div class="absolute inset-0 opacity-10">
              <div class="absolute top-0 left-0 w-64 h-64 bg-purple-500 rounded-full blur-3xl"></div>
              <div class="absolute bottom-0 right-0 w-64 h-64 bg-cyan-500 rounded-full blur-3xl"></div>
            </div>
            
            <div class="relative z-10">
              <h3 class="text-2xl md:text-3xl font-heading font-semibold mb-6 text-white text-center">
                <span class="bg-gradient-to-r from-purple-400 via-pink-400 to-cyan-400 bg-clip-text text-transparent">刺绣知识图谱</span>
              </h3>
              <KnowledgeGraph @node-click="openKnowledgeDetail" />
            </div>
          </div>
        </div>
        
        <!-- 知识卡片网格 -->
        <div class="grid md:grid-cols-2 lg:grid-cols-3 gap-6 mb-12">
          <!-- 传统工艺知识 -->
          <div @click="openKnowledgeDetail('tradition')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-primary/30 to-accent-primary/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6.253v13m0-13C10.832 5.477 9.246 5 7.5 5S4.168 5.477 3 6.253v13C4.168 18.477 5.754 18 7.5 18s3.332.477 4.5 1.253m0-13C13.168 5.477 14.754 5 16.5 5c1.747 0 3.332.477 4.5 1.253v13C19.832 18.477 18.247 18 16.5 18c-1.746 0-3.332.477-4.5 1.253" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">传统工艺</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">了解四大名绣的历史渊源、技法特点和传承脉络</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>探索传统工艺</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
          
          <!-- AI技术科普 -->
          <div @click="openKnowledgeDetail('ai')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-cta/30 to-accent-cta/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">AI技术</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">深度学习、计算机视觉如何赋能传统刺绣工艺</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>了解AI技术</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
          
          <!-- 文化传承 -->
          <div @click="openKnowledgeDetail('culture')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-vibrant/30 to-accent-vibrant/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">文化传承</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">探索刺绣背后的文化内涵、历史故事和艺术价值</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>探索文化传承</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
          
          <!-- 针法图谱 -->
          <div @click="openKnowledgeDetail('stitch')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-secondary/30 to-accent-secondary/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-secondary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">针法图谱</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">系统学习各种刺绣针法，从基础到高级技法</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>学习针法</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
          
          <!-- 材料科学 -->
          <div @click="openKnowledgeDetail('material')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-primary/30 to-accent-cta/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">材料科学</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">了解不同面料、绣线的特性及适用场景</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>了解材料</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
          
          <!-- 历史脉络 -->
          <div @click="openKnowledgeDetail('history')" class="glow-card rounded-xl p-6 cursor-pointer hover:scale-105 transition-all duration-300 group">
            <div class="flex items-center space-x-3 mb-4">
              <div class="w-12 h-12 bg-gradient-to-br from-accent-vibrant/30 to-accent-primary/10 rounded-lg flex items-center justify-center">
                <svg class="w-6 h-6 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
              </div>
              <h4 class="text-lg font-heading font-semibold text-white">历史脉络</h4>
            </div>
            <p class="text-sm text-gray-400 mb-4">追溯刺绣艺术的发展历程，从古至今的演变</p>
            <button class="w-full px-4 py-2 glow-card hover:bg-white/10 rounded-lg text-sm transition-all duration-300 cursor-pointer text-left">
              <span class="flex items-center justify-between">
                <span>探索历史</span>
                <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </span>
            </button>
          </div>
        </div>
        
        <!-- 知识详情展示区域 -->
        <div v-if="knowledgeDetail" id="knowledge-detail" class="mt-12">
          <div class="glow-card rounded-2xl p-8">
            <div class="flex items-center justify-between mb-6">
              <h3 class="text-2xl font-heading font-semibold text-white">{{ knowledgeDetail.title }}</h3>
              <button @click="closeKnowledgeDetail" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            </div>
            <div class="prose prose-invert max-w-none" v-html="knowledgeDetail.content"></div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- Minting/Order Flow Section -->
    <section id="order" class="py-20 bg-black relative overflow-hidden">
      <!-- 统一背景效果 -->
      <div class="absolute inset-0 embroidery-pattern opacity-15"></div>
      <TraditionalPattern />
      
      <!-- 动态粒子效果 -->
      <Particles container-id="particles-order" :count="20" />
      
      <!-- 数据流效果 -->
      <DataStream />
      
      <!-- 针线装饰 -->
      <NeedleThread />
      
      <!-- 霓虹光晕 -->
      <div class="absolute top-1/4 right-1/4 w-96 h-96 bg-accent-primary/20 rounded-full blur-3xl float"></div>
      <div class="absolute bottom-1/4 left-1/4 w-72 h-72 bg-accent-cta/15 rounded-full blur-3xl float" style="animation-delay: 2.5s;"></div>
      
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="text-center mb-16">
          <h2 class="text-4xl md:text-5xl font-heading font-bold mb-4">
            <span class="gradient-text">定制下单流程</span>
          </h2>
          <p class="text-xl text-gray-300 max-w-2xl mx-auto">
            简单四步，完成您的专属刺绣定制
          </p>
        </div>
        
        <!-- Steps -->
        <div class="grid md:grid-cols-4 gap-8 mb-16">
          <!-- Step 1 -->
          <div class="text-center">
            <div class="w-20 h-20 mx-auto mb-6 bg-accent-primary/20 rounded-full flex items-center justify-center relative">
              <span class="text-3xl font-heading font-bold text-accent-primary">1</span>
              <div class="absolute -right-10 top-1/2 transform -translate-y-1/2 hidden md:block">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2">选择或生成</h3>
            <p class="text-gray-400">从风格库选择或AI生成专属图案</p>
          </div>
          
          <!-- Step 2 -->
          <div class="text-center">
            <div class="w-20 h-20 mx-auto mb-6 bg-accent-cta/20 rounded-full flex items-center justify-center relative">
              <span class="text-3xl font-heading font-bold text-accent-cta">2</span>
              <div class="absolute -right-10 top-1/2 transform -translate-y-1/2 hidden md:block">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2">定制参数</h3>
            <p class="text-gray-400">选择尺寸、颜色、材质等参数</p>
          </div>
          
          <!-- Step 3 -->
          <div class="text-center">
            <div class="w-20 h-20 mx-auto mb-6 bg-accent-vibrant/20 rounded-full flex items-center justify-center relative">
              <span class="text-3xl font-heading font-bold text-accent-vibrant">3</span>
              <div class="absolute -right-10 top-1/2 transform -translate-y-1/2 hidden md:block">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2">确认下单</h3>
            <p class="text-gray-400">预览效果并完成支付</p>
          </div>
          
          <!-- Step 4 -->
          <div class="text-center">
            <div class="w-20 h-20 mx-auto mb-6 bg-accent-secondary/20 rounded-full flex items-center justify-center">
              <span class="text-3xl font-heading font-bold text-accent-secondary">4</span>
            </div>
            <h3 class="text-xl font-heading font-semibold mb-2">生产交付</h3>
            <p class="text-gray-400">AI打版后自动生产并寄送</p>
          </div>
        </div>
        
        <!-- Order Form Preview -->
        <div class="max-w-3xl mx-auto">
          <div class="glow-card rounded-2xl p-8">
            <h3 class="text-2xl font-heading font-semibold mb-6">开始定制</h3>
            
            <div class="space-y-6">
              <div>
                <label class="block text-sm font-medium text-gray-300 mb-2">选择图案来源</label>
                <div class="grid grid-cols-2 gap-4">
                  <button @click="scrollToSection('#gallery')" class="px-4 py-3 glow-card hover:bg-white/10 rounded-lg text-left transition-all duration-300 cursor-pointer">
                    <div class="flex items-center space-x-3">
                      <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                      <span class="font-medium">从风格库选择</span>
                    </div>
                  </button>
                  <button @click="goToAIGeneration" class="px-4 py-3 bg-accent-primary/20 border border-accent-primary/50 rounded-lg text-left transition-all duration-200 cursor-pointer">
                    <div class="flex items-center space-x-3">
                      <svg class="w-6 h-6 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                      </svg>
                      <span class="font-medium">AI 生成图案</span>
                    </div>
                  </button>
                </div>
              </div>
              
              <div class="flex items-center justify-between pt-4 border-t border-white/10">
                <div>
                  <p class="text-sm text-gray-400">预估价格</p>
                  <p class="text-2xl font-heading font-bold text-accent-primary">¥299</p>
                </div>
                <RouterLink to="/custom-order-flow" class="neon-button px-8 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer flex items-center space-x-2">
                  <span>开始定制</span>
                  <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 7l5 5m0 0l-5 5m5-5H6" />
                  </svg>
                </RouterLink>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>
    
    <!-- 浮动购物车按钮 -->
    <div class="fixed bottom-24 right-4 md:bottom-28 md:right-6 z-50">
      <RouterLink to="/cart" class="relative w-14 h-14 bg-accent-vibrant hover:bg-accent-cta text-white rounded-full shadow-lg hover:shadow-xl transition-all duration-200 cursor-pointer flex items-center justify-center pulse-glow" style="animation: floatCart 3s ease-in-out infinite; box-shadow: 0 0 20px rgba(16, 185, 129, 0.5), 0 0 40px rgba(16, 185, 129, 0.3);">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
        </svg>
        <span v-if="cartCount > 0" class="absolute -top-1 -right-1 w-6 h-6 bg-accent-primary rounded-full text-xs flex items-center justify-center font-bold border-2 border-black" style="animation: pulseCart 2s ease-in-out infinite;">{{ cartCount }}</span>
      </RouterLink>
    </div>
    
    <!-- AI聊天助手 -->
    <ChatInterface />
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { RouterLink, useRouter } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'
import DataStream from '@/components/effects/DataStream.vue'
import NeedleThread from '@/components/effects/NeedleThread.vue'
import TraditionalPattern from '@/components/effects/TraditionalPattern.vue'
import ChatInterface from '@/components/ai/ChatInterface.vue'
import KnowledgeGraph from '@/components/knowledge/KnowledgeGraph.vue'
import { useCartStore } from '@/store/cart'
import { usePageManager } from '@/composables/usePageManager'

const router = useRouter()
const cartStore = useCartStore()
const { openPage } = usePageManager()

const cartCount = computed(() => cartStore.items.length)
const activeFilter = ref('all')
const selectedEmbroideryStyle = ref<string | null>(null)

interface GalleryItem {
  id: number
  name: string
  description: string
  price: number
  category: string
  embroideryStyle?: string
  gradient: string
  iconColor: string
  iconPath: string
  imageUrl?: string
}

const allGalleryItems: GalleryItem[] = [
  { id: 1, name: '传统花卉', description: '经典中式刺绣风格', price: 299, category: 'traditional', imageUrl: '/images/gallery/traditional-flower.png', gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-secondary/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 2, name: '现代几何', description: '简约线条艺术', price: 199, category: 'modern', imageUrl: '/images/gallery/jianyue.png',  gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-vibrant/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 3, name: '抽象艺术', description: '创意无限可能', price: 399, category: 'abstract', imageUrl: '/images/gallery/chouxiang.png',  gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-primary/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 4, name: '自然风景', description: '山水意境之美', price: 349, category: 'traditional', imageUrl: '/images/gallery/ziran.png',  gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-cta/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 5, name: '文字艺术', description: '个性化文字定制', price: 149, category: 'modern', imageUrl: '/images/gallery/wenzi.png',  gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-vibrant/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 6, name: '动物主题', description: '生动可爱', price: 279, category: 'traditional', imageUrl: '/images/gallery/dongwu.png',  embroideryStyle: 'xiangxiu', gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-primary/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 7, name: '旗袍刺绣', description: '优雅传统', price: 459, category: 'traditional',  imageUrl: '/images/gallery/qipao.png', embroideryStyle: 'suxiu', gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-secondary/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 8, name: '丝巾刺绣', description: '精致典雅', price: 329, category: 'traditional', imageUrl: '/images/gallery/sijin.png',  embroideryStyle: 'yuexiu', gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 9, name: '花卉图案', description: '精美花卉设计', price: 259, category: 'floral', imageUrl: '/images/gallery/traditional-flower.png',  embroideryStyle: 'suxiu', gradient: 'bg-gradient-to-br from-accent-primary/20 to-accent-vibrant/20', iconColor: 'text-accent-primary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' },
  { id: 10, name: '抽象线条', description: '现代抽象风格', price: 189, category: 'abstract', imageUrl: '/images/gallery/traditional-flower.png',  gradient: 'bg-gradient-to-br from-accent-cta/20 to-accent-secondary/20', iconColor: 'text-accent-cta/30', iconPath: 'M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z' },
  { id: 11, name: '龙凤呈祥', description: '传统吉祥图案', price: 599, category: 'traditional', imageUrl: '/images/gallery/traditional-flower.png',  embroideryStyle: 'yuexiu', gradient: 'bg-gradient-to-br from-accent-vibrant/20 to-accent-primary/20', iconColor: 'text-accent-vibrant/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z' },
  { id: 12, name: '熊猫主题', description: '可爱熊猫图案', price: 319, category: 'traditional', imageUrl: '/images/gallery/traditional-flower.png',  embroideryStyle: 'shuxiu', gradient: 'bg-gradient-to-br from-accent-secondary/20 to-accent-cta/20', iconColor: 'text-accent-secondary/30', iconPath: 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z' }
]

const galleryItems = ref<GalleryItem[]>(allGalleryItems)

// 只显示前8个风格
const displayedGalleryItems = computed(() => {
  return galleryItems.value.slice(0, 8)
})

function filterGallery(category: string) {
  activeFilter.value = category
  selectedEmbroideryStyle.value = null
  
  if (category === 'all') {
    galleryItems.value = allGalleryItems
  } else {
    galleryItems.value = allGalleryItems.filter(item => item.category === category)
  }
}

function filterByEmbroideryStyle(style: string) {
  selectedEmbroideryStyle.value = style
  activeFilter.value = 'all'
  galleryItems.value = allGalleryItems.filter(item => item.embroideryStyle === style)
}

function selectEmbroideryStyle(style: string) {
  filterByEmbroideryStyle(style)
}

function addToCart(item: GalleryItem) {
  cartStore.addItem({
    id: item.id.toString(),
    name: item.name,
    image: '',
    price: item.price
  })
}

function goToAIGeneration() {
  router.push('/ai-generation')
}

function goToEditor() {
  router.push('/pattern-editor')
}

function goToCollaboration() {
  router.push('/production')
}

function handleImageError(event: Event) {
  const img = event.target as HTMLImageElement
  console.warn('图片加载失败:', img.src)
  // 可以设置一个默认占位图
  // img.src = '/images/placeholder.jpg'
}

function scrollToSection(selector: string) {
  const element = document.querySelector(selector)
  if (element) {
    element.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }
}

function openLuxuryCustom(type: string) {
  router.push(`/luxury/${type}`)
}

const knowledgeDetail = ref<{ title: string; content: string } | null>(null)

const knowledgeData: Record<string, { title: string; content: string }> = {
  'tradition': {
    title: '传统工艺知识',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-2xl font-heading font-semibold text-white mb-4 flex items-center space-x-2">
            <span class="w-1 h-8 bg-gradient-to-b from-accent-primary to-accent-cta"></span>
            <span>四大名绣</span>
          </h4>
          <div class="grid md:grid-cols-2 gap-4 mb-6">
            <div class="p-5 glow-card rounded-lg border-l-4 border-accent-primary hover:scale-105 transition-all duration-300">
              <div class="flex items-center space-x-2 mb-2">
                <span class="text-2xl">🌸</span>
                <h5 class="font-semibold text-white text-lg">苏绣</h5>
              </div>
              <p class="text-sm text-gray-400 mb-2">起源于江苏苏州，以"平、齐、细、密"著称。双面绣是其绝技，正反两面图案一致，针法精细，色彩文雅。</p>
              <div class="flex flex-wrap gap-2 mt-3">
                <span class="px-2 py-1 bg-accent-primary/20 text-accent-primary text-xs rounded">双面绣</span>
                <span class="px-2 py-1 bg-accent-primary/20 text-accent-primary text-xs rounded">精细针法</span>
                <span class="px-2 py-1 bg-accent-primary/20 text-accent-primary text-xs rounded">色彩文雅</span>
              </div>
            </div>
            <div class="p-5 glow-card rounded-lg border-l-4 border-accent-cta hover:scale-105 transition-all duration-300">
              <div class="flex items-center space-x-2 mb-2">
                <span class="text-2xl">🦁</span>
                <h5 class="font-semibold text-white text-lg">湘绣</h5>
              </div>
              <p class="text-sm text-gray-400 mb-2">湖南长沙的传统工艺，独创"鬅毛针"技法，绣出的动物毛发栩栩如生，形象生动，色彩鲜艳。</p>
              <div class="flex flex-wrap gap-2 mt-3">
                <span class="px-2 py-1 bg-accent-cta/20 text-accent-cta text-xs rounded">鬅毛针</span>
                <span class="px-2 py-1 bg-accent-cta/20 text-accent-cta text-xs rounded">动物题材</span>
                <span class="px-2 py-1 bg-accent-cta/20 text-accent-cta text-xs rounded">色彩鲜艳</span>
              </div>
            </div>
            <div class="p-5 glow-card rounded-lg border-l-4 border-accent-vibrant hover:scale-105 transition-all duration-300">
              <div class="flex items-center space-x-2 mb-2">
                <span class="text-2xl">✨</span>
                <h5 class="font-semibold text-white text-lg">粤绣</h5>
              </div>
              <p class="text-sm text-gray-400 mb-2">广东地区的特色工艺，善用金银线，产生浮雕般的立体效果，构图繁而不乱，色彩富丽堂皇。</p>
              <div class="flex flex-wrap gap-2 mt-3">
                <span class="px-2 py-1 bg-accent-vibrant/20 text-accent-vibrant text-xs rounded">金银线</span>
                <span class="px-2 py-1 bg-accent-vibrant/20 text-accent-vibrant text-xs rounded">立体效果</span>
                <span class="px-2 py-1 bg-accent-vibrant/20 text-accent-vibrant text-xs rounded">富丽堂皇</span>
              </div>
            </div>
            <div class="p-5 glow-card rounded-lg border-l-4 border-accent-secondary hover:scale-105 transition-all duration-300">
              <div class="flex items-center space-x-2 mb-2">
                <span class="text-2xl">🐼</span>
                <h5 class="font-semibold text-white text-lg">蜀绣</h5>
              </div>
              <p class="text-sm text-gray-400 mb-2">四川成都的传统技艺，针法严谨，片线光亮，擅长表现鲤鱼和熊猫，具有浓郁的地方特色。</p>
              <div class="flex flex-wrap gap-2 mt-3">
                <span class="px-2 py-1 bg-accent-secondary/20 text-accent-secondary text-xs rounded">晕针技法</span>
                <span class="px-2 py-1 bg-accent-secondary/20 text-accent-secondary text-xs rounded">地方特色</span>
                <span class="px-2 py-1 bg-accent-secondary/20 text-accent-secondary text-xs rounded">片线光亮</span>
              </div>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-2xl font-heading font-semibold text-white mb-4 flex items-center space-x-2">
            <span class="w-1 h-8 bg-gradient-to-b from-accent-primary to-accent-cta"></span>
            <span>传统技法体系</span>
          </h4>
          <div class="grid md:grid-cols-3 gap-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-3 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span>基础针法</span>
              </h5>
              <ul class="space-y-2 text-sm text-gray-400">
                <li>• <strong class="text-white">平针</strong>：最基本的针法，用于轮廓和填充</li>
                <li>• <strong class="text-white">回针</strong>：用于线条和细节表现</li>
                <li>• <strong class="text-white">锁边</strong>：防止边缘脱线</li>
                <li>• <strong class="text-white">填充</strong>：大面积区域的绣制</li>
              </ul>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-3 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
                <span>高级技法</span>
              </h5>
              <ul class="space-y-2 text-sm text-gray-400">
                <li>• <strong class="text-white">双面绣</strong>：苏绣的绝技，正反两面一致</li>
                <li>• <strong class="text-white">鬅毛针</strong>：湘绣特色，表现动物毛发</li>
                <li>• <strong class="text-white">浮雕绣</strong>：粤绣特色，立体效果</li>
                <li>• <strong class="text-white">晕针</strong>：蜀绣特色，色彩渐变</li>
              </ul>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-3 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z" />
                </svg>
                <span>工艺特点</span>
              </h5>
              <ul class="space-y-2 text-sm text-gray-400">
                <li>• <strong class="text-white">精细度</strong>：每平方厘米可达数百针</li>
                <li>• <strong class="text-white">层次感</strong>：通过针法变化表现立体</li>
                <li>• <strong class="text-white">色彩</strong>：传统色卡与现代配色结合</li>
                <li>• <strong class="text-white">传承</strong>：千年工艺的现代演绎</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    `
  },
  'ai': {
    title: 'AI技术科普',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">AI如何赋能传统刺绣</h4>
          <div class="space-y-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
                <span>图像识别与语义分割</span>
              </h5>
              <p class="text-sm text-gray-400">深度学习模型能够自动识别图像中的图案元素，进行语义分割，将复杂图案分解为可刺绣的区域。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
                </svg>
                <span>智能路径规划</span>
              </h5>
              <p class="text-sm text-gray-400">AI算法自动计算最优刺绣路径，减少跳线次数，提高效率，降低绣线消耗。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-vibrant" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
                </svg>
                <span>针法智能匹配</span>
              </h5>
              <p class="text-sm text-gray-400">根据图案特征自动匹配最适合的针法组合，确保绣品质量和美观度。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2 flex items-center space-x-2">
                <svg class="w-5 h-5 text-accent-secondary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                </svg>
                <span>实时预览与优化</span>
              </h5>
              <p class="text-sm text-gray-400">AI实时生成预览效果，支持参数调整和优化建议，所见即所得。</p>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">技术原理</h4>
          <p class="text-sm text-gray-400 leading-relaxed mb-4">
            绣生平台的AI系统基于深度学习和计算机视觉技术，通过训练大量刺绣图案数据，学习传统工艺的规律和特点。
            系统能够理解图案的语义信息，自动识别适合的针法、颜色和路径，将传统工艺数字化、智能化。
            这不仅提高了生产效率，也让更多人能够参与到刺绣创作中来，实现传统工艺的传承与创新。
          </p>
          <div class="grid md:grid-cols-2 gap-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">深度学习模型</h5>
              <p class="text-sm text-gray-400">使用卷积神经网络(CNN)和生成对抗网络(GAN)进行图案识别和生成</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">计算机视觉</h5>
              <p class="text-sm text-gray-400">通过图像分割、边缘检测等技术提取刺绣要素</p>
            </div>
          </div>
        </div>
      </div>
    `
  },
  'culture': {
    title: '文化传承',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">刺绣的文化内涵</h4>
          <p class="text-sm text-gray-400 leading-relaxed mb-4">
            刺绣作为中华传统手工艺的重要组成部分，承载着深厚的文化内涵。从古代的宫廷御用到民间的日常装饰，
            刺绣不仅是技艺的展现，更是文化的传承。不同的图案、颜色、技法都蕴含着特定的文化寓意。
          </p>
          <div class="grid md:grid-cols-2 gap-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">吉祥寓意</h5>
              <p class="text-sm text-gray-400">龙凤呈祥、花开富贵、年年有余等传统图案，寄托着人们对美好生活的向往。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">身份象征</h5>
              <p class="text-sm text-gray-400">古代龙袍、官服上的刺绣图案，体现了等级制度和身份地位的象征意义。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">艺术价值</h5>
              <p class="text-sm text-gray-400">刺绣作品不仅是实用品，更是艺术品，体现了中华民族的审美情趣和艺术追求。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">情感表达</h5>
              <p class="text-sm text-gray-400">一针一线都蕴含着制作者的心血和情感，是情感交流的重要载体。</p>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">传承与创新</h4>
          <p class="text-sm text-gray-400 leading-relaxed">
            在数字化时代，我们通过AI技术将传统刺绣工艺与现代科技相结合，既保留了传统工艺的精髓，
            又赋予了新的生命力。让年轻一代能够通过数字化的方式学习和传承这门古老的艺术，
            让传统工艺在新时代焕发出新的光彩。
          </p>
        </div>
      </div>
    `
  },
  'stitch': {
    title: '针法图谱',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">基础针法</h4>
          <div class="grid md:grid-cols-2 gap-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">平针</h5>
              <p class="text-sm text-gray-400 mb-2">最基本的针法，针脚平行排列，用于轮廓和填充。</p>
              <p class="text-xs text-gray-500">特点：简单易学，适合大面积填充</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">回针</h5>
              <p class="text-sm text-gray-400 mb-2">针脚回退，形成连续的线条，用于细节表现。</p>
              <p class="text-xs text-gray-500">特点：线条流畅，适合轮廓</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">锁边</h5>
              <p class="text-sm text-gray-400 mb-2">防止边缘脱线，形成整齐的边缘效果。</p>
              <p class="text-xs text-gray-500">特点：保护边缘，美观实用</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">填充</h5>
              <p class="text-sm text-gray-400 mb-2">大面积区域的绣制方法，有多种填充模式。</p>
              <p class="text-xs text-gray-500">特点：效率高，覆盖面积大</p>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">高级技法</h4>
          <div class="space-y-3">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">双面绣（苏绣绝技）</h5>
              <p class="text-sm text-gray-400">正反两面图案完全一致，需要极高的技艺水平，是苏绣的代表性技法。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">鬅毛针（湘绣特色）</h5>
              <p class="text-sm text-gray-400">专门用于绣制动物毛发，能够表现出毛发的质感和层次，栩栩如生。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">浮雕效果（粤绣特色）</h5>
              <p class="text-sm text-gray-400">使用金银线垫高，产生立体浮雕效果，富丽堂皇。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">晕针技法（蜀绣特色）</h5>
              <p class="text-sm text-gray-400">通过颜色的渐变过渡，形成柔和的色彩效果，明丽清秀。</p>
            </div>
          </div>
        </div>
      </div>
    `
  },
  'material': {
    title: '材料科学',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">面料选择</h4>
          <div class="grid md:grid-cols-2 gap-4">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">丝绸</h5>
              <p class="text-sm text-gray-400 mb-2">最适合精细刺绣的面料，光泽好，质感佳。</p>
              <p class="text-xs text-gray-500">适用：高端定制、艺术品</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">棉布</h5>
              <p class="text-sm text-gray-400 mb-2">日常使用最广泛的面料，性价比高。</p>
              <p class="text-xs text-gray-500">适用：日常用品、服装</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">麻布</h5>
              <p class="text-sm text-gray-400 mb-2">天然材质，透气性好，适合夏季。</p>
              <p class="text-xs text-gray-500">适用：夏季服装、装饰品</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">化纤</h5>
              <p class="text-sm text-gray-400 mb-2">现代合成材料，耐用性强。</p>
              <p class="text-xs text-gray-500">适用：工业用途、批量生产</p>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">绣线知识</h4>
          <div class="space-y-3">
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">丝线</h5>
              <p class="text-sm text-gray-400">光泽度最高，适合精细作品，是传统刺绣的首选材料。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">棉线</h5>
              <p class="text-sm text-gray-400">性价比高，适合日常使用，颜色丰富。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">金银线</h5>
              <p class="text-sm text-gray-400">用于特殊效果，富丽堂皇，常用于龙袍、官服等。</p>
            </div>
            <div class="p-4 glow-card rounded-lg">
              <h5 class="font-semibold text-white mb-2">色卡系统</h5>
              <p class="text-sm text-gray-400">DMC、Anchor等国际标准色卡，确保颜色准确性和一致性。</p>
            </div>
          </div>
        </div>
      </div>
    `
  },
  'history': {
    title: '历史脉络',
    content: `
      <div class="space-y-6">
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">发展历程</h4>
          <div class="space-y-4">
            <div class="p-4 glow-card rounded-lg border-l-4 border-accent-primary">
              <h5 class="font-semibold text-white mb-2">远古时期</h5>
              <p class="text-sm text-gray-400">刺绣起源于新石器时代，最初用于装饰衣物，具有实用和装饰双重功能。</p>
            </div>
            <div class="p-4 glow-card rounded-lg border-l-4 border-accent-cta">
              <h5 class="font-semibold text-white mb-2">秦汉时期</h5>
              <p class="text-sm text-gray-400">刺绣工艺逐渐成熟，成为宫廷和贵族的重要装饰，出现了专门的绣工。</p>
            </div>
            <div class="p-4 glow-card rounded-lg border-l-4 border-accent-vibrant">
              <h5 class="font-semibold text-white mb-2">唐宋时期</h5>
              <p class="text-sm text-gray-400">刺绣艺术达到高峰，技法丰富，图案精美，四大名绣开始形成各自特色。</p>
            </div>
            <div class="p-4 glow-card rounded-lg border-l-4 border-accent-secondary">
              <h5 class="font-semibold text-white mb-2">明清时期</h5>
              <p class="text-sm text-gray-400">宫廷刺绣达到极致，龙袍、官服等成为艺术珍品，民间刺绣也蓬勃发展。</p>
            </div>
            <div class="p-4 glow-card rounded-lg border-l-4 border-accent-primary">
              <h5 class="font-semibold text-white mb-2">现代时期</h5>
              <p class="text-sm text-gray-400">传统工艺与现代科技相结合，AI技术为刺绣艺术注入新的活力，让更多人能够参与创作。</p>
            </div>
          </div>
        </div>
        <div>
          <h4 class="text-xl font-heading font-semibold text-white mb-3">文化意义</h4>
          <p class="text-sm text-gray-400 leading-relaxed">
            刺绣不仅是技艺，更是文化的载体。它记录了中华民族的审美变迁、生活方式和社会文化。
            从宫廷到民间，从实用到艺术，刺绣始终与人们的生活紧密相连。
            在新时代，我们通过数字化和AI技术，让这门古老的艺术焕发新的生命力，
            让更多人能够了解、学习和传承中华传统优秀文化。
          </p>
        </div>
      </div>
    `
  }
}

function openKnowledgeDetail(type: string) {
  const detail = knowledgeData[type]
  if (detail) {
    knowledgeDetail.value = detail
    // 滚动到知识详情区域
    setTimeout(() => {
      const element = document.getElementById('knowledge-detail')
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'start' })
      }
    }, 100)
  }
}

function closeKnowledgeDetail() {
  knowledgeDetail.value = null
}
</script>
