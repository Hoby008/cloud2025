import apiService from './apiService'

export default {
  getCoursCryptomonnaies(params = {}) {
    return apiService.get('/cours-crypto', { params })
  },

  getCryptosDisponibles() {
    return apiService.get('/cours-crypto/disponibles')
  },

  getAnalysesCrypto(requete) {
    return apiService.post('/cours-crypto/analyse', requete)
  },

  getTypesAnalyseCrypto() {
    return apiService.get('/cours-crypto/types-analyse')
  }
}
