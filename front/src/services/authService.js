import apiService from './apiService'

export default {
  login(credentials) {
    return apiService.post('/auth/login', credentials)
  },

  logout() {
    return apiService.post('/auth/logout')
  },

  validateToken(token) {
    return apiService.post('/auth/validate-token', { token })
  }
}
