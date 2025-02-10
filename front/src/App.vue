<template>
  <v-app class="app-container">
    <v-navigation-drawer 
      v-model="drawer" 
      app 
      color="white" 
      class="sidebar-custom"
    >
      <v-list>
        <v-list-item 
          v-for="(item, index) in menuItems" 
          :key="index"
          :to="item.route"
          class="nav-item"
        >
          <v-icon class="mr-3 text-violet">{{ item.icon }}</v-icon>
          <v-list-item-title class="text-violet">{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <v-app-bar 
      app 
      color="white" 
      elevation="1" 
      class="app-bar-custom"
    >
      <v-app-bar-nav-icon 
        @click="drawer = !drawer" 
        class="text-violet"
      ></v-app-bar-nav-icon>
      
      <v-toolbar-title class="text-violet font-weight-bold">
        Crypto6
      </v-toolbar-title>
      
      <v-spacer></v-spacer>
      
      <v-btn 
        icon 
        @click="toggleTheme" 
        class="theme-toggle"
      >
        <v-icon class="text-violet">mdi-theme-light-dark</v-icon>
      </v-btn>
      
      <v-btn 
        class="ml-3 btn-profile"
        text
      >
        <v-icon left class="text-violet">mdi-account-circle</v-icon>
        Mon Profil
      </v-btn>
    </v-app-bar>

    <v-main class="main-content">
      <v-container fluid>
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </v-container>
    </v-main>

    <v-footer 
      app 
      color="white" 
      class="footer-custom text-center"
    >
      <v-container>
        <span class="text-violet">
          &copy; {{ new Date().getFullYear() }} Crypto6 Platform
        </span>
      </v-container>
    </v-footer>
    
    <GlobalNotification />
  </v-app>
</template>

<script setup>
import { ref } from 'vue'
import { useTheme } from 'vuetify'
import GlobalNotification from '@/components/GlobalNotification.vue'

const drawer = ref(false)
const theme = useTheme()

const menuItems = [
  { title: 'Accueil', route: '/', icon: 'mdi-home' },
  { title: 'Portefeuille', route: '/portefeuille', icon: 'mdi-wallet' },
  { title: 'Opérations', route: '/operations', icon: 'mdi-swap-horizontal' },
  { title: 'Notifications', route: '/notifications', icon: 'mdi-bell' },
  { title: 'Profil', route: '/profil', icon: 'mdi-account' }
]

const toggleTheme = () => {
  theme.global.name.value = theme.global.current.value.dark ? 'light' : 'dark'
}
</script>

<style scoped>
.app-container {
  background-color: #f4f4f8 !important;
}

.sidebar-custom {
  background-color: white !important;
  border-right: 1px solid rgba(106, 90, 205, 0.1);
}

.nav-item {
  margin: 10px 0;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.nav-item:hover {
  background-color: rgba(106, 90, 205, 0.05);
}

.app-bar-custom {
  background-color: white !important;
  border-bottom: 1px solid rgba(106, 90, 205, 0.1);
}

.main-content {
  background-color: #f4f4f8;
}

.footer-custom {
  background-color: white !important;
  border-top: 1px solid rgba(106, 90, 205, 0.1);
}

.text-violet {
  color: #6a5acd !important;
}

.theme-toggle, .btn-profile {
  color: #6a5acd !important;
}
</style>
