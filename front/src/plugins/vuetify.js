import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { mdi } from 'vuetify/iconsets/mdi'
import '@mdi/font/css/materialdesignicons.css'

const lightTheme = {
  dark: false,
  colors: {
    primary: '#6a5acd',     // Violet principal
    secondary: '#8a2be2',   // Violet secondaire
    accent: '#9370db',      // Violet accent
    background: '#f4f4f8',  // Blanc cassé
    surface: '#ffffff',     // Blanc pur
    error: '#FF5252',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FFC107'
  }
}

const darkTheme = {
  dark: true,
  colors: {
    primary: '#6a5acd',     // Violet principal
    secondary: '#8a2be2',   // Violet secondaire
    accent: '#9370db',      // Violet accent
    background: '#121212',  // Noir profond
    surface: '#1E1E1E',     // Gris très foncé
    error: '#CF6679',
    info: '#2196F3',
    success: '#4CAF50',
    warning: '#FFC107'
  }
}

export default createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'lightTheme',
    themes: {
      lightTheme,
      darkTheme
    }
  },
  icons: {
    defaultSet: 'mdi',
    sets: {
      mdi
    }
  }
})
