import apiService from './apiService'

export default {
  getUserProfile() {
    return apiService.get('/user/profile')
  },

  updateProfile(profileData) {
    return apiService.put('/user/profile', profileData)
  },

  uploadProfilePicture(file) {
    const formData = new FormData()
    formData.append('file', file)
    return apiService.post('/user/profile-picture', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
  }
}
