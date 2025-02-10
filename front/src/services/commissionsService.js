import apiService from './apiService'

export default {
  getCurrentCommissions() {
    return apiService.get('/admin/commissions/current')
  },

  updateCommissions(commissions) {
    return apiService.put('/admin/commissions/update', commissions)
  },

  resetCommissionsToDefault() {
    return apiService.post('/admin/commissions/reset')
  },

  analyserCommissions(requete) {
    return apiService.post('/admin/commissions/analyse', requete)
  },

  getTypesAnalyseCommissions() {
    return apiService.get('/admin/commissions/types-analyse')
  }
}
