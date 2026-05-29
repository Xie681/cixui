import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface CartItem {
  id: string
  name: string
  image: string
  price: number
  quantity: number
}

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>([])

  const addItem = (item: Omit<CartItem, 'quantity'>) => {
    const existingItem = items.value.find(i => i.id === item.id)
    if (existingItem) {
      existingItem.quantity++
    } else {
      items.value.push({ ...item, quantity: 1 })
    }
  }

  const removeItem = (id: string) => {
    const index = items.value.findIndex(i => i.id === id)
    if (index > -1) {
      items.value.splice(index, 1)
    }
  }

  const updateQuantity = (id: string, quantity: number) => {
    const item = items.value.find(i => i.id === id)
    if (item) {
      item.quantity = quantity
      if (item.quantity <= 0) {
        removeItem(id)
      }
    }
  }

  const clearCart = () => {
    items.value = []
  }

  const total = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  return {
    items,
    addItem,
    removeItem,
    updateQuantity,
    clearCart,
    total
  }
})
