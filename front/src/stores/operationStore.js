import { defineStore } from 'pinia'
import operationService from '@/services/operationService'

export const useOperationStore = defineStore('operation', {
  state: () => ({
    operations: [],
    recentOperations: [],
    loading: false,
    error: null
  }),

  actions: {
    async fetchOperations() {
      this.loading = true
      this.error = null
      try {
        this.operations = await operationService.getAllOperations()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    async fetchRecentOperations() {
      this.loading = true
      this.error = null
      try {
        this.recentOperations = await operationService.getRecentOperations()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    async createDeposit(amount, method) {
      this.loading = true
      this.error = null
      try {
        await operationService.deposit(amount, method)
        await this.fetchRecentOperations()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    async createWithdrawal(amount, method) {
      this.loading = true
      this.error = null
      try {
        await operationService.withdraw(amount, method)
        await this.fetchRecentOperations()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    }
  },

  getters: {
    getOperationsByType: (state) => (type) => {
      return state.operations.filter(op => op.type === type)
    },

    getOperationsByStatus: (state) => (status) => {
      return state.operations.filter(op => op.status === status)
    }
  }
})
