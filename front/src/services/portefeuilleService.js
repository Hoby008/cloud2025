import apiService from './apiService'

export default {
  consulterSolde() {
    return apiService.get('/portefeuille/solde')
  },

  faireDepot(montant) {
    return apiService.post('/portefeuille/depot', { montant })
  },

  faireRetrait(montant) {
    return apiService.post('/portefeuille/retrait', { montant })
  },

  getPortefeuilleInfo() {
    return apiService.get('/portefeuille')
  }
}
