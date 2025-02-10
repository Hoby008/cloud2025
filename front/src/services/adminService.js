import apiService from './apiService'

export default {
  getAllUsers() {
    return apiService.get('/admin/users')
  },

  getUserById(userId) {
    return apiService.get(`/admin/users/${userId}`)
  },

  updateUserStatus(userId, status) {
    return apiService.put(`/admin/users/${userId}/status`, status)
  },

  rechercherOperationsValidation(filtres) {
    return apiService.post('/admin/operations/validation', filtres)
  },

  rechercherOperationsCrypto(filtres) {
    return apiService.post('/admin/operations/crypto', filtres)
  },

  getFiltresDisponibles() {
    return apiService.get('/admin/operations/filtres')
  }
}
