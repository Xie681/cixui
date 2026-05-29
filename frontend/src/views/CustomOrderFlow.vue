<template>
  <div class="min-h-screen bg-black text-gray-100">
    <Navbar />
    <div class="pt-16">
      <div class="min-h-screen relative">
        <!-- 背景效果 -->
        <div class="absolute inset-0 embroidery-pattern opacity-20"></div>
        <Particles container-id="particles-order-flow" :count="25" />
        
        <!-- 顶部导航栏 -->
        <nav class="sticky top-0 z-10 glass-dark border-b border-accent-primary/20">
          <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex items-center justify-between h-16">
              <div class="flex items-center space-x-4">
                <button @click="goHome" class="p-2 glow-card rounded-lg hover:bg-white/10 transition-colors duration-300 cursor-pointer">
                  <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 19l-7-7m0 0l7-7m-7 7h18" />
                  </svg>
                </button>
                <h2 class="text-xl font-heading font-bold text-white">定制下单流程</h2>
              </div>
              <div class="flex items-center space-x-4">
                <span class="text-sm text-gray-400">步骤 {{ currentStep }} / 4</span>
              </div>
            </div>
          </div>
        </nav>
        
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
          <!-- 步骤指示器 -->
          <div class="mb-12">
            <div class="flex items-center justify-between mb-8">
              <!-- Step 1 -->
              <div class="flex-1 flex flex-col items-center">
                <div 
                  :class="[
                    'w-20 h-20 rounded-full flex items-center justify-center mb-4 transition-all duration-300',
                    currentStep >= 1 ? 'bg-accent-primary/20 border-2 border-accent-primary' : 'bg-white/5 border-2 border-white/10'
                  ]"
                >
                  <span :class="['text-3xl font-heading font-bold', currentStep >= 1 ? 'text-accent-primary' : 'text-gray-500']">1</span>
                </div>
                <h3 class="text-lg font-heading font-semibold mb-2 text-white">选择或生成</h3>
                <p class="text-sm text-gray-400 text-center">从风格库选择或AI生成专属图案</p>
              </div>
              
              <!-- Arrow -->
              <div class="flex-1 flex items-center justify-center px-4">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
              
              <!-- Step 2 -->
              <div class="flex-1 flex flex-col items-center">
                <div 
                  :class="[
                    'w-20 h-20 rounded-full flex items-center justify-center mb-4 transition-all duration-300',
                    currentStep >= 2 ? 'bg-accent-cta/20 border-2 border-accent-cta' : 'bg-white/5 border-2 border-white/10'
                  ]"
                >
                  <span :class="['text-3xl font-heading font-bold', currentStep >= 2 ? 'text-accent-cta' : 'text-gray-500']">2</span>
                </div>
                <h3 class="text-lg font-heading font-semibold mb-2 text-white">定制参数</h3>
                <p class="text-sm text-gray-400 text-center">选择尺寸、颜色、材质等参数</p>
              </div>
              
              <!-- Arrow -->
              <div class="flex-1 flex items-center justify-center px-4">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
              
              <!-- Step 3 -->
              <div class="flex-1 flex flex-col items-center">
                <div 
                  :class="[
                    'w-20 h-20 rounded-full flex items-center justify-center mb-4 transition-all duration-300',
                    currentStep >= 3 ? 'bg-accent-vibrant/20 border-2 border-accent-vibrant' : 'bg-white/5 border-2 border-white/10'
                  ]"
                >
                  <span :class="['text-3xl font-heading font-bold', currentStep >= 3 ? 'text-accent-vibrant' : 'text-gray-500']">3</span>
                </div>
                <h3 class="text-lg font-heading font-semibold mb-2 text-white">确认下单</h3>
                <p class="text-sm text-gray-400 text-center">预览效果并完成支付</p>
              </div>
              
              <!-- Arrow -->
              <div class="flex-1 flex items-center justify-center px-4">
                <svg class="w-8 h-8 text-accent-primary/50" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
                </svg>
              </div>
              
              <!-- Step 4 -->
              <div class="flex-1 flex flex-col items-center">
                <div 
                  :class="[
                    'w-20 h-20 rounded-full flex items-center justify-center mb-4 transition-all duration-300',
                    currentStep >= 4 ? 'bg-accent-secondary/20 border-2 border-accent-secondary' : 'bg-white/5 border-2 border-white/10'
                  ]"
                >
                  <span :class="['text-3xl font-heading font-bold', currentStep >= 4 ? 'text-accent-secondary' : 'text-gray-500']">4</span>
                </div>
                <h3 class="text-lg font-heading font-semibold mb-2 text-white">生产交付</h3>
                <p class="text-sm text-gray-400 text-center">AI打版后自动生产并寄送</p>
              </div>
            </div>
          </div>
          
          <!-- 步骤内容 -->
          <div class="glow-card rounded-2xl p-8">
            <!-- 步骤1: 选择或生成 -->
            <div v-if="currentStep === 1" class="step-content">
              <div class="text-center mb-8">
                <h3 class="text-3xl font-heading font-bold mb-4 gradient-text">选择图案来源</h3>
                <p class="text-gray-300">从风格库选择或使用AI生成专属图案</p>
              </div>
              
              <div class="grid md:grid-cols-2 gap-8 mb-8">
                <!-- 从风格库选择 -->
                <div 
                  @click="selectPatternSource('gallery')"
                  :class="[
                    'glow-card rounded-xl p-8 cursor-pointer transition-all duration-300',
                    patternSource === 'gallery' ? 'border-2 border-accent-primary bg-accent-primary/10' : 'hover:bg-white/5'
                  ]"
                >
                  <div class="flex flex-col items-center text-center">
                    <div class="w-20 h-20 bg-accent-primary/20 rounded-full flex items-center justify-center mb-4">
                      <svg class="w-10 h-10 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                      </svg>
                    </div>
                    <h4 class="text-xl font-heading font-semibold mb-2 text-white">从风格库选择</h4>
                    <p class="text-gray-400 text-sm">浏览丰富的刺绣风格库，选择您喜欢的图案</p>
                  </div>
                </div>
                
                <!-- AI生成图案 -->
                <div 
                  @click="selectPatternSource('ai')"
                  :class="[
                    'glow-card rounded-xl p-8 cursor-pointer transition-all duration-300',
                    patternSource === 'ai' ? 'border-2 border-accent-primary bg-accent-primary/10' : 'hover:bg-white/5'
                  ]"
                >
                  <div class="flex flex-col items-center text-center">
                    <div class="w-20 h-20 bg-accent-cta/20 rounded-full flex items-center justify-center mb-4">
                      <svg class="w-10 h-10 text-accent-cta" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                      </svg>
                    </div>
                    <h4 class="text-xl font-heading font-semibold mb-2 text-white">AI 生成图案</h4>
                    <p class="text-gray-400 text-sm">使用AI智能生成专属刺绣图案</p>
                  </div>
                </div>
              </div>
              
              <!-- 风格库选择区域 -->
              <div v-if="patternSource === 'gallery'" class="mt-8">
                <h4 class="text-xl font-heading font-semibold mb-4 text-white">选择风格</h4>
                <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                  <div 
                    v-for="style in availableStyles" 
                    :key="style.id"
                    @click="selectedStyle = style"
                    :class="[
                      'glow-card rounded-lg p-4 cursor-pointer transition-all duration-300',
                      selectedStyle?.id === style.id ? 'border-2 border-accent-primary bg-accent-primary/10' : 'hover:bg-white/5'
                    ]"
                  >
                    <div class="aspect-square bg-gradient-to-br from-accent-primary/20 to-accent-cta/20 rounded-lg mb-2 flex items-center justify-center">
                      <svg class="w-12 h-12 text-accent-primary/40" fill="currentColor" viewBox="0 0 24 24">
                        <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
                      </svg>
                    </div>
                    <p class="text-sm font-medium text-white text-center">{{ style.name }}</p>
                    <p class="text-xs text-gray-400 text-center mt-1">¥{{ style.price }}</p>
                  </div>
                </div>
              </div>
              
              <!-- AI生成区域 -->
              <div v-if="patternSource === 'ai'" class="mt-8">
                <div class="text-center mb-6">
                  <button @click="goToAIGeneration" class="neon-button px-8 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer">
                    前往AI智能打版流程
                  </button>
                </div>
                <div class="text-center text-sm text-gray-400">
                  <p>💡 提示：在AI智能打版流程中完成图案生成后，可直接进入下单流程</p>
                </div>
              </div>
              
              <!-- 下一步按钮 -->
              <div class="flex justify-end mt-8">
                <button 
                  @click="nextStep" 
                  :disabled="!canProceedStep1"
                  class="neon-button px-8 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                >
                  下一步
                </button>
              </div>
            </div>
            
            <!-- 步骤2: 定制参数 -->
            <div v-if="currentStep === 2" class="step-content">
              <div class="text-center mb-8">
                <h3 class="text-3xl font-heading font-bold mb-4 gradient-text">定制参数</h3>
                <p class="text-gray-300">选择尺寸、颜色、材质等参数，所见即所绣</p>
              </div>
              
              <div class="grid md:grid-cols-2 gap-8">
                <!-- 左侧：参数设置 -->
                <div class="space-y-6">
                  <!-- 尺寸选择 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-300 mb-3">选择尺寸</label>
                    <div class="grid grid-cols-2 gap-3">
                      <button 
                        v-for="size in sizeOptions"
                        :key="size.value"
                        @click="orderParams.size = size.value"
                        :class="[
                          'px-4 py-3 glow-card rounded-lg text-left transition-all duration-300 cursor-pointer',
                          orderParams.size === size.value ? 'border-2 border-accent-primary bg-accent-primary/10' : 'hover:bg-white/5'
                        ]"
                      >
                        <p class="text-white font-medium">{{ size.label }}</p>
                        <p class="text-xs text-gray-400 mt-1">{{ size.desc }}</p>
                      </button>
                    </div>
                    <!-- 自定义尺寸 -->
                    <div v-if="orderParams.size === 'custom'" class="mt-3 grid grid-cols-2 gap-3">
                      <input 
                        v-model.number="orderParams.customWidth"
                        type="number"
                        placeholder="宽度(cm)"
                        class="glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent"
                      />
                      <input 
                        v-model.number="orderParams.customHeight"
                        type="number"
                        placeholder="高度(cm)"
                        class="glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent"
                      />
                    </div>
                  </div>
                  
                  <!-- 材质选择 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-300 mb-3">选择材质</label>
                    <select 
                      v-model="orderParams.fabric"
                      class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer"
                    >
                      <option v-for="fabric in fabricOptions" :key="fabric" :value="fabric">{{ fabric }}</option>
                    </select>
                  </div>
                  
                  <!-- 线材选择 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-300 mb-3">选择线材</label>
                    <select 
                      v-model="orderParams.thread"
                      class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer"
                    >
                      <option v-for="thread in threadOptions" :key="thread" :value="thread">{{ thread }}</option>
                    </select>
                  </div>
                  
                  <!-- 智能颜色选择 -->
                  <div>
                    <SmartColorPicker v-model="orderParams.colors" />
                  </div>
                  
                  <!-- 数量 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-300 mb-3">数量</label>
                    <div class="flex items-center space-x-4">
                      <button @click="orderParams.quantity > 1 && orderParams.quantity--" class="w-10 h-10 glow-card hover:bg-white/10 rounded-lg flex items-center justify-center transition-all duration-300 cursor-pointer">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M20 12H4" />
                        </svg>
                      </button>
                      <span class="text-xl font-semibold text-white w-12 text-center">{{ orderParams.quantity }}</span>
                      <button @click="orderParams.quantity++" class="w-10 h-10 glow-card hover:bg-white/10 rounded-lg flex items-center justify-center transition-all duration-300 cursor-pointer">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                        </svg>
                      </button>
                    </div>
                  </div>
                  
                  <!-- 精修选项 -->
                  <div>
                    <label class="block text-sm font-medium text-gray-300 mb-3">精修选项</label>
                    <div class="space-y-2">
                      <label class="flex items-center space-x-3 cursor-pointer">
                        <input 
                          v-model="orderParams.needRefinement"
                          type="checkbox"
                          class="w-5 h-5 text-accent-primary bg-transparent border-gray-600 rounded focus:ring-accent-primary"
                        />
                        <span class="text-gray-300">需要图案精修</span>
                      </label>
                      <label v-if="orderParams.needRefinement" class="flex items-center space-x-3 cursor-pointer">
                        <input 
                          v-model="orderParams.openInWilcom"
                          type="checkbox"
                          class="w-5 h-5 text-accent-primary bg-transparent border-gray-600 rounded focus:ring-accent-primary"
                        />
                        <span class="text-gray-300">在Wilcom中打开进行精修</span>
                      </label>
                    </div>
                  </div>
                </div>
                
                <!-- 右侧：实时预览 -->
                <div>
                  <h4 class="text-xl font-heading font-semibold mb-4 text-white">实时预览</h4>
                  <div class="glow-card rounded-xl p-6">
                    <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden mb-4">
                      <canvas ref="previewCanvas" class="w-full h-full"></canvas>
                    </div>
                    <div class="space-y-2 text-sm">
                      <div class="flex justify-between">
                        <span class="text-gray-400">尺寸:</span>
                        <span class="text-white">{{ getSizeLabel() }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-400">材质:</span>
                        <span class="text-white">{{ orderParams.fabric }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-400">线材:</span>
                        <span class="text-white">{{ orderParams.thread }}</span>
                      </div>
                      <div class="flex justify-between">
                        <span class="text-gray-400">颜色数:</span>
                        <span class="text-white">{{ orderParams.colors.length }}</span>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 价格预览 -->
                  <div class="mt-6 glow-card rounded-xl p-6">
                    <div class="flex justify-between items-center mb-2">
                      <span class="text-gray-400">预估价格</span>
                      <span class="text-2xl font-heading font-bold gradient-text">¥{{ estimatedPrice }}</span>
                    </div>
                    <p class="text-xs text-gray-500 mt-2">价格会根据最终参数调整</p>
                  </div>
                </div>
              </div>
              
              <!-- 按钮组 -->
              <div class="flex justify-between mt-8">
                <button @click="prevStep" class="px-8 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                  上一步
                </button>
                <button @click="nextStep" class="neon-button px-8 py-3 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                  下一步
                </button>
              </div>
            </div>
            
            <!-- 步骤3: 确认下单 -->
            <div v-if="currentStep === 3" class="step-content">
              <div class="text-center mb-8">
                <h3 class="text-3xl font-heading font-bold mb-4 gradient-text">确认下单</h3>
                <p class="text-gray-300">预览最终效果，确认信息并完成支付</p>
              </div>
              
              <div class="grid md:grid-cols-3 gap-8">
                <!-- 左侧：订单详情 -->
                <div class="md:col-span-2 space-y-6">
                  <!-- 最终预览 -->
                  <div class="glow-card rounded-xl p-6">
                    <h4 class="text-xl font-heading font-semibold mb-4 text-white">最终预览效果</h4>
                    <div class="aspect-square bg-gradient-to-br from-accent-primary/10 to-accent-cta/10 rounded-xl relative overflow-hidden mb-4">
                      <canvas ref="finalPreviewCanvas" class="w-full h-full"></canvas>
                    </div>
                    <div class="grid grid-cols-2 gap-4 text-sm">
                      <div>
                        <span class="text-gray-400">尺寸:</span>
                        <span class="text-white ml-2">{{ getSizeLabel() }}</span>
                      </div>
                      <div>
                        <span class="text-gray-400">材质:</span>
                        <span class="text-white ml-2">{{ orderParams.fabric }}</span>
                      </div>
                      <div>
                        <span class="text-gray-400">线材:</span>
                        <span class="text-white ml-2">{{ orderParams.thread }}</span>
                      </div>
                      <div>
                        <span class="text-gray-400">数量:</span>
                        <span class="text-white ml-2">{{ orderParams.quantity }}</span>
                      </div>
                    </div>
                  </div>
                  
                  <!-- 收货信息 -->
                  <div class="glow-card rounded-xl p-6">
                    <h4 class="text-xl font-heading font-semibold mb-4 text-white">收货信息</h4>
                    <div class="space-y-4">
                      <!-- 地址选择 -->
                      <div>
                        <label class="block text-sm text-gray-400 mb-2">选择收货地址</label>
                        <select 
                          v-model="shippingInfo.addressId"
                          class="w-full glow-card rounded-lg px-4 py-3 text-white bg-transparent focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 cursor-pointer mb-2"
                        >
                          <option value="">请选择地址</option>
                          <option v-for="addr in addresses" :key="addr.id" :value="addr.id">
                            {{ addr.fullAddress }} ({{ addr.name }} {{ addr.phone }})
                          </option>
                        </select>
                        <button @click="showAddAddress = true" class="text-sm text-accent-primary hover:text-accent-cta transition-colors duration-200 cursor-pointer">
                          + 添加新地址
                        </button>
                      </div>
                      
                      <!-- 添加地址表单 -->
                      <div v-if="showAddAddress" class="glow-card rounded-lg p-4 space-y-3">
                        <input 
                          v-model="newAddress.name"
                          type="text"
                          placeholder="收货人姓名"
                          class="w-full glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                        />
                        <input 
                          v-model="newAddress.phone"
                          type="tel"
                          placeholder="联系电话"
                          class="w-full glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                        />
                        <div class="grid grid-cols-3 gap-2">
                          <input 
                            v-model="newAddress.province"
                            type="text"
                            placeholder="省份"
                            class="glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                          />
                          <input 
                            v-model="newAddress.city"
                            type="text"
                            placeholder="城市"
                            class="glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                          />
                          <input 
                            v-model="newAddress.district"
                            type="text"
                            placeholder="区县"
                            class="glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                          />
                        </div>
                        <input 
                          v-model="newAddress.detail"
                          type="text"
                          placeholder="详细地址"
                          class="w-full glow-card rounded-lg px-4 py-2 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent text-sm"
                        />
                        <div class="flex justify-end space-x-2">
                          <button @click="showAddAddress = false" class="px-4 py-2 glow-card hover:bg-white/10 text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">
                            取消
                          </button>
                          <button @click="saveAddress" class="px-4 py-2 neon-button text-white rounded-lg text-sm transition-all duration-300 cursor-pointer">
                            保存
                          </button>
                        </div>
                      </div>
                      
                      <!-- 备注 -->
                      <div>
                        <label class="block text-sm text-gray-400 mb-2">备注（选填）</label>
                        <textarea 
                          v-model="orderParams.remark"
                          rows="3"
                          class="w-full glow-card rounded-lg px-4 py-3 text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-accent-primary transition-all duration-300 bg-transparent resize-none"
                          placeholder="如有特殊要求，请在此说明"
                        ></textarea>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 右侧：订单汇总 -->
                <div class="md:col-span-1">
                  <div class="glow-card rounded-xl p-6 sticky top-24">
                    <h4 class="text-lg font-heading font-semibold mb-4 text-white">订单汇总</h4>
                    <div class="space-y-3 mb-6">
                      <div class="flex justify-between text-sm">
                        <span class="text-gray-400">商品总额</span>
                        <span class="text-white">¥{{ estimatedPrice }}</span>
                      </div>
                      <div class="flex justify-between text-sm">
                        <span class="text-gray-400">运费</span>
                        <span class="text-white">¥{{ shippingFee }}</span>
                      </div>
                      <div class="border-t border-white/10 pt-3 flex justify-between">
                        <span class="text-white font-semibold">总计</span>
                        <span class="text-xl font-heading font-bold gradient-text">¥{{ totalPrice }}</span>
                      </div>
                    </div>
                    <button 
                      @click="submitOrder" 
                      :disabled="!canSubmit"
                      class="w-full neon-button px-6 py-4 text-white rounded-lg font-medium text-lg transition-all duration-300 cursor-pointer disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                      提交订单
                    </button>
                    <div class="mt-4 text-center">
                      <button @click="prevStep" class="text-sm text-gray-400 hover:text-white transition-colors duration-200 cursor-pointer">
                        返回修改
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            
            <!-- 步骤4: 生产交付 -->
            <div v-if="currentStep === 4" class="step-content">
              <div class="text-center mb-8">
                <h3 class="text-3xl font-heading font-bold mb-4 gradient-text">订单提交成功</h3>
                <p class="text-gray-300">您的订单已提交，我们将尽快为您处理</p>
              </div>
              
              <div class="max-w-2xl mx-auto space-y-6">
                <!-- 订单信息 -->
                <div class="glow-card rounded-xl p-6">
                  <div class="text-center mb-6">
                    <div class="w-20 h-20 bg-accent-primary/20 rounded-full flex items-center justify-center mx-auto mb-4">
                      <svg class="w-12 h-12 text-accent-primary" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                      </svg>
                    </div>
                    <h4 class="text-xl font-heading font-semibold text-white mb-2">订单号: {{ orderNo }}</h4>
                    <p class="text-gray-400">预计 {{ estimatedDays }} 天内完成生产并寄送</p>
                  </div>
                  
                  <!-- 生产流程 -->
                  <div class="space-y-4">
                    <div class="flex items-center space-x-4">
                      <div class="w-12 h-12 bg-accent-primary/20 rounded-full flex items-center justify-center flex-shrink-0">
                        <span class="text-accent-primary font-bold">1</span>
                      </div>
                      <div class="flex-1">
                        <p class="text-white font-medium">AI智能打版</p>
                        <p class="text-sm text-gray-400">正在生成DST文件...</p>
                      </div>
                      <div class="text-accent-primary">
                        <svg class="w-6 h-6 animate-spin" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                        </svg>
                      </div>
                    </div>
                    
                    <div class="flex items-center space-x-4 opacity-50">
                      <div class="w-12 h-12 bg-white/5 rounded-full flex items-center justify-center flex-shrink-0">
                        <span class="text-gray-500 font-bold">2</span>
                      </div>
                      <div class="flex-1">
                        <p class="text-gray-500 font-medium">生产制作</p>
                        <p class="text-sm text-gray-600">等待打版完成</p>
                      </div>
                    </div>
                    
                    <div class="flex items-center space-x-4 opacity-50">
                      <div class="w-12 h-12 bg-white/5 rounded-full flex items-center justify-center flex-shrink-0">
                        <span class="text-gray-500 font-bold">3</span>
                      </div>
                      <div class="flex-1">
                        <p class="text-gray-500 font-medium">质检包装</p>
                        <p class="text-sm text-gray-600">等待生产完成</p>
                      </div>
                    </div>
                    
                    <div class="flex items-center space-x-4 opacity-50">
                      <div class="w-12 h-12 bg-white/5 rounded-full flex items-center justify-center flex-shrink-0">
                        <span class="text-gray-500 font-bold">4</span>
                      </div>
                      <div class="flex-1">
                        <p class="text-gray-500 font-medium">发货寄送</p>
                        <p class="text-sm text-gray-600">等待质检完成</p>
                      </div>
                    </div>
                  </div>
                </div>
                
                <!-- 操作按钮 -->
                <div class="flex justify-center space-x-4">
                  <button @click="viewOrder" class="px-8 py-3 glow-card hover:bg-white/10 text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                    查看订单详情
                  </button>
                  <button @click="goHome" class="px-8 py-3 neon-button text-white rounded-lg font-medium transition-all duration-300 cursor-pointer">
                    返回首页
                  </button>
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
import { ref, computed, onMounted, watch, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import Navbar from '@/components/layout/Navbar.vue'
import Particles from '@/components/effects/Particles.vue'
import SmartColorPicker from '@/components/color/SmartColorPicker.vue'
import { customizeApi, userApi, orderApi } from '@/services/api'
import type { StyleVO } from '@/services/api/customize'
import type { AddressVO } from '@/services/api/user'

const router = useRouter()
const route = useRoute()

// 步骤控制
const currentStep = ref(1)
const patternSource = ref<'gallery' | 'ai' | null>(null)
const selectedStyle = ref<StyleVO | null>(null)

// 步骤1数据
const availableStyles = ref<StyleVO[]>([])
const aiGeneratedImageUrl = ref('') // 从AI打版页面传来的图片URL

// 检查是否从三方协同微调跳转过来
if (route.query.source === 'collaboration' && route.query.finalized === 'true') {
  // 从三方协同微调最终定稿后跳转过来，直接进入步骤2
  currentStep.value = 2
  patternSource.value = 'ai'
  // 接收图片URL
  if (route.query.imageUrl) {
    aiGeneratedImageUrl.value = route.query.imageUrl as string
  }
}

// 检查是否从AI打版页面跳转过来
console.log('=== CustomOrderFlow 页面加载 ===')
console.log('当前时间:', new Date().toLocaleTimeString())
console.log('完整 route.query:', JSON.stringify(route.query))
console.log('route.query.source:', route.query.source)
console.log('route.query.imageUrl:', route.query.imageUrl)
console.log('route.query.imageUrl 类型:', typeof route.query.imageUrl)

if (route.query.source === 'ai' && route.query.imageUrl) {
  aiGeneratedImageUrl.value = route.query.imageUrl as string
  patternSource.value = 'ai'
  currentStep.value = 2 // 直接进入步骤2
  console.log('✅ 从AI打版页面接收到图片URL:', aiGeneratedImageUrl.value)
  console.log('✅ 已设置 patternSource 为 ai，currentStep 为 2')
} else {
  console.log('⚠️ 未从AI打版页面跳转')
  console.log('   - source 是否为 ai:', route.query.source === 'ai')
  console.log('   - imageUrl 是否存在:', !!route.query.imageUrl)
  console.log('   - 当前 aiGeneratedImageUrl:', aiGeneratedImageUrl.value)
}

// 步骤2数据
const orderParams = ref({
  size: '10x10',
  customWidth: 10,
  customHeight: 10,
  fabric: '棉布',
  thread: '普通绣线',
  colors: ['#EF4444'],
  quantity: 1,
  needRefinement: false,
  openInWilcom: false,
  remark: ''
})

const sizeOptions = [
  { value: '10x10', label: '10cm × 10cm', desc: '小尺寸' },
  { value: '15x15', label: '15cm × 15cm', desc: '中尺寸' },
  { value: '20x20', label: '20cm × 20cm', desc: '大尺寸' },
  { value: 'custom', label: '自定义尺寸', desc: '自由定制' }
]

const fabricOptions = ['棉布', '丝绸', '麻布', '化纤', '混纺']
const threadOptions = ['普通绣线', '金线', '银线', '丝线', '棉线']
const colorOptions = ['#EF4444', '#3B82F6', '#10B981', '#8B5CF6', '#F59E0B', '#EC4899', '#06B6D4', '#84CC16']

// 步骤3数据
const shippingInfo = ref({
  addressId: null as number | null
})

const addresses = ref<AddressVO[]>([])
const showAddAddress = ref(false)
const newAddress = ref({
  name: '',
  phone: '',
  province: '',
  city: '',
  district: '',
  detail: ''
})

// 步骤4数据
const orderNo = ref('')
const estimatedDays = ref(7)

// 预览画布
const previewCanvas = ref<HTMLCanvasElement>()
const finalPreviewCanvas = ref<HTMLCanvasElement>()

// 计算属性
const canProceedStep1 = computed(() => {
  if (patternSource.value === 'gallery') {
    return selectedStyle.value !== null
  }
  return patternSource.value === 'ai'
})

const estimatedPrice = computed(() => {
  let basePrice = selectedStyle.value?.price || 299
  // 根据尺寸调整
  if (orderParams.value.size === 'custom') {
    const area = (orderParams.value.customWidth || 10) * (orderParams.value.customHeight || 10)
    basePrice = Math.round(basePrice * (area / 100))
  }
  // 根据数量调整
  return basePrice * orderParams.value.quantity
})

const shippingFee = ref(15)
const totalPrice = computed(() => estimatedPrice.value + shippingFee.value)

const canSubmit = computed(() => {
  return shippingInfo.value.addressId !== null
})

// 方法
function goHome() {
  router.push('/')
}

function goToAIGeneration() {
  router.push('/ai-generation')
}

function selectPatternSource(source: 'gallery' | 'ai') {
  patternSource.value = source
  if (source === 'gallery') {
    loadStyles()
  }
}

async function loadStyles() {
  try {
    availableStyles.value = await customizeApi.getStyles()
    if (availableStyles.value.length > 0 && !selectedStyle.value) {
      selectedStyle.value = availableStyles.value[0]
    }
  } catch (error) {
    console.error('加载风格列表失败:', error)
  }
}

function toggleColor(color: string) {
  const index = orderParams.value.colors.indexOf(color)
  if (index > -1) {
    orderParams.value.colors.splice(index, 1)
  } else {
    orderParams.value.colors.push(color)
  }
}

function getSizeLabel() {
  const size = sizeOptions.find(s => s.value === orderParams.value.size)
  if (size && size.value !== 'custom') {
    return size.label
  }
  return `${orderParams.value.customWidth}cm × ${orderParams.value.customHeight}cm`
}

function updatePreview() {
  if (!previewCanvas.value) return
  
  const canvas = previewCanvas.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  const container = canvas.parentElement
  if (!container) return
  
  canvas.width = container.clientWidth
  canvas.height = container.clientHeight
  
  // 清空画布
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  // 绘制预览图案
  ctx.fillStyle = orderParams.value.colors[0] || '#EF4444'
  ctx.fillRect(canvas.width * 0.2, canvas.height * 0.2, canvas.width * 0.6, canvas.height * 0.6)
  
  // 绘制其他颜色
  orderParams.value.colors.slice(1).forEach((color, index) => {
    ctx.fillStyle = color
    const size = 30
    const x = 20 + index * (size + 10)
    const y = 20
    ctx.fillRect(x, y, size, size)
  })
}

function updateFinalPreview() {
  if (!finalPreviewCanvas.value) return
  
  const canvas = finalPreviewCanvas.value
  const ctx = canvas.getContext('2d')
  if (!ctx) return
  
  const container = canvas.parentElement
  if (!container) return
  
  canvas.width = container.clientWidth
  canvas.height = container.clientHeight
  
  // 清空画布
  ctx.clearRect(0, 0, canvas.width, canvas.height)
  
  // 绘制最终预览
  updatePreview()
  // 可以在这里添加更复杂的预览逻辑
}

async function loadAddresses() {
  try {
    addresses.value = await userApi.listAddresses()
    if (addresses.value.length > 0 && !shippingInfo.value.addressId) {
      const defaultAddr = addresses.value.find(a => a.isDefault)
      shippingInfo.value.addressId = defaultAddr?.id || addresses.value[0].id
    }
  } catch (error) {
    console.error('加载地址列表失败:', error)
  }
}

async function saveAddress() {
  try {
    const addressId = await userApi.addAddress({
      name: newAddress.value.name,
      phone: newAddress.value.phone,
      province: newAddress.value.province,
      city: newAddress.value.city,
      district: newAddress.value.district,
      detail: newAddress.value.detail,
      isDefault: addresses.value.length === 0
    })
    
    await loadAddresses()
    shippingInfo.value.addressId = addressId
    showAddAddress.value = false
    
    // 重置表单
    newAddress.value = {
      name: '',
      phone: '',
      province: '',
      city: '',
      district: '',
      detail: ''
    }
  } catch (error) {
    console.error('保存地址失败:', error)
    alert('保存地址失败，请重试')
  }
}

async function submitOrder() {
  if (!canSubmit.value) {
    console.warn('⚠️ 无法提交订单：canSubmit 为 false')
    return
  }
  
  console.log('=== 开始提交订单 ===')
  
  try {
    // 计算尺寸
    let width = 10, height = 10
    if (orderParams.value.size === 'custom') {
      width = orderParams.value.customWidth || 10
      height = orderParams.value.customHeight || 10
    } else {
      const [w, h] = orderParams.value.size.split('x').map(Number)
      width = w
      height = h
    }
    
    // 获取图片URL（优先使用AI生成的图片，否则使用选中的风格）
    const imageUrl = aiGeneratedImageUrl.value || selectedStyle.value?.imageUrl || ''
    
    console.log('📊 订单数据检查:')
    console.log('   - aiGeneratedImageUrl:', aiGeneratedImageUrl.value)
    console.log('   - selectedStyle?.imageUrl:', selectedStyle.value?.imageUrl)
    console.log('   - 最终 imageUrl:', imageUrl)
    console.log('   - imageUrl 长度:', imageUrl.length)
    console.log('   - imageUrl 是否为空:', imageUrl === '')
    
    if (!imageUrl || imageUrl.trim() === '') {
      console.error('❌ 图片URL为空，无法提交订单')
      alert('请先上传图片或选择风格')
      return
    }
    
    // 映射刺绣风格到后端枚举
    const styleMap: Record<string, 'SU' | 'XIANG' | 'SHU' | 'YUE'> = {
      '苏绣': 'SU',
      '湘绣': 'XIANG',
      '蜀绣': 'SHU',
      '粤绣': 'YUE'
    }
    const embroideryStyle = styleMap[selectedStyle.value?.name || '苏绣'] || 'SU'
    
    const orderData = {
      imageUrl,
      embroideryStyle,
      width,
      height,
      fabricType: orderParams.value.fabric,
      threadType: orderParams.value.thread,
      quantity: orderParams.value.quantity,
      addressId: shippingInfo.value.addressId!,
      remark: orderParams.value.remark || ''
    }
    
    console.log('📦 提交订单数据:')
    console.log(JSON.stringify(orderData, null, 2))
    
    // 提交订单
    const orderNoResult = await customizeApi.submitOrder(orderData)
    
    console.log('✅ 订单提交成功！订单号:', orderNoResult)
    orderNo.value = orderNoResult
    currentStep.value = 4
  } catch (error: any) {
    console.error('❌ 提交订单失败:', error)
    console.error('   - 错误信息:', error.message)
    console.error('   - 错误详情:', error)
    alert('提交订单失败：' + (error.message || '请重试'))
  }
}

function nextStep() {
  if (currentStep.value < 4) {
    currentStep.value++
    if (currentStep.value === 3) {
      loadAddresses()
      nextTick(() => {
        updateFinalPreview()
      })
    }
  }
}

function prevStep() {
  if (currentStep.value > 1) {
    currentStep.value--
  }
}

function viewOrder() {
  router.push(`/orders`)
}

// 监听参数变化，更新预览
watch([orderParams, selectedStyle], () => {
  if (currentStep.value === 2) {
    nextTick(() => {
      updatePreview()
    })
  }
}, { deep: true })

onMounted(() => {
  window.scrollTo(0, 0)
  loadStyles()
  if (currentStep.value === 2) {
    nextTick(() => {
      updatePreview()
    })
  }
})
</script>
