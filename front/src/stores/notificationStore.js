import { defineStore } from 'pinia'

export const useNotificationStore = defineStore('notification', {
  state: () => ({
    show: false,
    message: '',
    color: 'success',
    timeout: 3000
  }),
  actions: {
    success(message) {
      this.show = true
      this.message = message
      this.color = 'success'
    },
    error(message) {
      this.show = true
      this.message = message
      this.color = 'error'
    },
    warning(message) {
      this.show = true
      this.message = message
      this.color = 'warning'
    },
    hide() {
      this.show = false
    }
  }
})
