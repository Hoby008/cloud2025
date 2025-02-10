import { defineStore } from 'pinia'
import apiService from '@/services/apiService'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    token: localStorage.getItem('token') || null,
    isAuthenticated: false
  }),

  actions: {
    async login(credentials) {
      try {
        const response = await apiService.post('/auth/login', credentials)
        
        // Stocker le token et les informations utilisateur
        this.token = response.token
        this.user = response.user
        this.isAuthenticated = true

        // Sauvegarder dans le localStorage
        localStorage.setItem('token', this.token)
        
        return response
      } catch (error) {
        console.error('Erreur de connexion', error)
        throw error
      }
    },

    logout() {
      // Supprimer le token et les informations utilisateur
      this.token = null
      this.user = null
      this.isAuthenticated = false
      
      // Supprimer du localStorage
      localStorage.removeItem('token')
    },

    async fetchUserProfile() {
      try {
        const userProfile = await apiService.get('/user/profile')
        this.user = userProfile
        return userProfile
      } catch (error) {
        console.error('Impossible de charger le profil', error)
        throw error
      }
    }
  },

  getters: {
    getCurrentUser() {
      return this.user
    },
    getAuthToken() {
      return this.token
    }
  }
})
