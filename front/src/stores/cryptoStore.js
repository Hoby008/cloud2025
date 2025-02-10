import { defineStore } from 'pinia'
import cryptoService from '@/services/cryptoService'

export const useCryptoStore = defineStore('crypto', {
  state: () => ({
    availableCryptos: [],
    userCryptos: [],
    selectedCrypto: null,
    loading: false,
    error: null
  }),

  actions: {
    async fetchAvailableCryptos() {
      this.loading = true
      this.error = null
      try {
        this.availableCryptos = await cryptoService.getAvailableCryptos()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    async fetchUserCryptos() {
      this.loading = true
      this.error = null
      try {
        this.userCryptos = await cryptoService.getUserCryptos()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    selectCrypto(crypto) {
      this.selectedCrypto = crypto
    },

    async buyCrypto(cryptoId, amount) {
      this.loading = true
      this.error = null
      try {
        await cryptoService.buyCrypto(cryptoId, amount)
        await this.fetchUserCryptos()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    },

    async sellCrypto(cryptoId, amount) {
      this.loading = true
      this.error = null
      try {
        await cryptoService.sellCrypto(cryptoId, amount)
        await this.fetchUserCryptos()
      } catch (error) {
        this.error = error.message
      } finally {
        this.loading = false
      }
    }
  },

  getters: {
    getCryptoById: (state) => (id) => {
      return state.availableCryptos.find(crypto => crypto.id === id)
    },

    getUserCryptoBalance: (state) => (cryptoId) => {
      const userCrypto = state.userCryptos.find(crypto => crypto.id === cryptoId)
      return userCrypto ? userCrypto.balance : 0
    }
  }
})
