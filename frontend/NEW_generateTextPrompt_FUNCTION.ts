// 将这个函数复制到 AIGeneration.vue 中，替换原来的 generateTextPrompt 函数

async function generateTextPrompt() {
  if (!textInput.value.trim()) {
    alert('请先输入文字内容')
    return
  }
  
  if (!selectedEmbroideryStyle.value) {
    alert('请先选择刺绣风格')
    return
  }
  
  isGenerating.value = true
  
  try {
    // 调用后端AI预览接口生成刺绣效果图
    const response = await customizeApi.previewAiResult({
      text: textInput.value,
      fontStyle: fontStyle.value || '楷书',
      fontSize: 48,
      color: selectedColors.value[0] || '黑色',
      embroideryStyle: selectedEmbroideryStyle.value
    })
    
    // 保存生成的效果图URL
    generatedImageUrl.value = response.previewImageUrl
    
    // 生成描述性文字
    const styleName = embroideryStyles.find(s => s.id === selectedEmbroideryStyle.value)?.name || '通用'
    textPromptResult.value = `✅ AI已为您生成刺绣效果图！

📝 文字内容：${textInput.value}
🎨 刺绣风格：${styleName}
🧵 预估针数：${response.stitchCount} 针
🌈 使用颜色：${response.colorCount} 色

效果图已生成，请查看下方预览。`
    
    console.log('AI生成成功:', response)
  } catch (error) {
    console.error('AI生成失败:', error)
    alert('AI生成失败，请重试。请确保后端AI服务正常运行。')
  } finally {
    isGenerating.value = false
  }
}
