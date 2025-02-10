<template>
  <v-snackbar
    v-model="notificationStore.show"
    :color="notificationColor"
    :timeout="notificationStore.timeout"
    class="global-notification"
    location="top right"
  >
    <div class="notification-content">
      <v-icon 
        left 
        class="mr-3"
      >
        {{ notificationIcon }}
      </v-icon>
      {{ notificationStore.message }}
    </div>
    
    <template v-slot:actions>
      <v-btn
        variant="text"
        @click="notificationStore.hide"
        class="close-btn"
      >
        Fermer
      </v-btn>
    </template>
  </v-snackbar>
</template>

<script setup>
import { computed } from 'vue'
import { useNotificationStore } from '@/stores/notificationStore'

const notificationStore = useNotificationStore()

const notificationColor = computed(() => {
  switch(notificationStore.type) {
    case 'success': return 'success'
    case 'error': return 'error'
    case 'warning': return 'warning'
    case 'info': return 'primary'
    default: return 'primary'
  }
})

const notificationIcon = computed(() => {
  switch(notificationStore.type) {
    case 'success': return 'mdi-check-circle'
    case 'error': return 'mdi-alert-circle'
    case 'warning': return 'mdi-alert'
    case 'info': return 'mdi-information'
    default: return 'mdi-bell'
  }
})
</script>

<style scoped>
.global-notification {
  margin-top: 60px;
  border-radius: 12px !important;
  box-shadow: 0 10px 30px rgba(106, 90, 205, 0.1) !important;
}

.notification-content {
  display: flex;
  align-items: center;
  font-weight: 500;
}

.close-btn {
  color: white !important;
  text-transform: none !important;
}
</style>
