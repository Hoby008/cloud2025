import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api'

class ApiService {
  constructor() {
    this.api = axios.create({
      baseURL: API_BASE_URL,
      headers: {
        'Content-Type': 'application/json'
      },
      withCredentials: true
    })

    // Intercepteur pour ajouter le token d'authentification
    this.api.interceptors.request.use(
      config => {
        const token = localStorage.getItem('token')
        if (token) {
          config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
      },
      error => Promise.reject(error)
    )

    // Intercepteur pour gérer les erreurs globalement
    this.api.interceptors.response.use(
      response => response,
      error => {
        if (error.response) {
          switch (error.response.status) {
            case 401:
              // Token expiré ou invalide
              localStorage.removeItem('token')
              window.location = '/login'
              break
            case 403:
              console.error('Accès refusé')
              break
            case 404:
              console.error('Ressource non trouvée')
              break
            case 500:
              console.error('Erreur serveur interne')
              break
          }
        }
        return Promise.reject(error)
      }
    )
  }

  // Méthodes génériques pour les requêtes API
  async get(url, config = {}) {
    try {
      const response = await this.api.get(url, config)
      return response.data
    } catch (error) {
      this.handleError(error)
    }
  }

  async post(url, data, config = {}) {
    try {
      const response = await this.api.post(url, data, config)
      return response.data
    } catch (error) {
      this.handleError(error)
    }
  }

  async put(url, data, config = {}) {
    try {
      const response = await this.api.put(url, data, config)
      return response.data
    } catch (error) {
      this.handleError(error)
    }
  }

  async delete(url, config = {}) {
    try {
      const response = await this.api.delete(url, config)
      return response.data
    } catch (error) {
      this.handleError(error)
    }
  }

  // Gestion centralisée des erreurs
  handleError(error) {
    if (error.response) {
      console.error('Erreur de réponse:', error.response.data)
    } else if (error.request) {
      console.error('Pas de réponse du serveur')
    } else {
      console.error('Erreur de configuration', error.message)
    }
    throw error
  }
}

export default new ApiService()
