import apiService from './apiService'

const BASE_URL = '/operations'

export default {
  async getAllOperations() {
    try {
      const response = await apiService.get(`${BASE_URL}`)
      return response.data
    } catch (error) {
      throw error
    }
  },

  async getRecentOperations() {
    try {
      const response = await apiService.get(`${BASE_URL}/recent`)
      return response.data
    } catch (error) {
      throw error
    }
  },

  async deposit(amount, method) {
    try {
      const response = await apiService.post(`${BASE_URL}/deposit`, {
        amount,
        method
      })
      return response.data
    } catch (error) {
      throw error
    }
  },

  async withdraw(amount, method) {
    try {
      const response = await apiService.post(`${BASE_URL}/withdraw`, {
        amount,
        method
      })
      return response.data
    } catch (error) {
      throw error
    }
  },

  async getOperationTypes() {
    try {
      const response = await apiService.get(`${BASE_URL}/types`)
      return response.data
    } catch (error) {
      throw error
    }
  }
}
