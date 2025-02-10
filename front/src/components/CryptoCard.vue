<template>
  <v-card 
    class="crypto-card elevation-0" 
    outlined
  >
    <v-card-title class="d-flex align-center">
      <v-avatar size="40" class="mr-3">
        <v-img :src="crypto.logo" :alt="crypto.name"></v-img>
      </v-avatar>
      <div>
        <div class="text-h6 text-violet">{{ crypto.name }}</div>
        <div class="text-caption text-grey">{{ crypto.symbol }}</div>
      </div>
      <v-spacer></v-spacer>
      <div class="text-right">
        <div class="text-subtitle-2">{{ formatPrice(crypto.price) }}</div>
        <v-chip 
          :color="crypto.change24h > 0 ? 'success' : 'error'" 
          small
          outlined
        >
          {{ formatPercentage(crypto.change24h) }}
        </v-chip>
      </div>
    </v-card-title>
    <v-card-actions>
      <v-btn 
        text 
        color="primary" 
        block
        @click="$emit('trade', crypto)"
      >
        Échanger
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'

const props = defineProps({
  crypto: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['trade'])

const formatPrice = (price) => {
  return new Intl.NumberFormat('fr-FR', { 
    style: 'currency', 
    currency: 'EUR' 
  }).format(price)
}

const formatPercentage = (percentage) => {
  return `${percentage > 0 ? '+' : ''}${percentage.toFixed(2)}%`
}
</script>

<style scoped>
.crypto-card {
  border-radius: 16px !important;
  margin-bottom: 15px;
  transition: transform 0.3s ease;
}

.crypto-card:hover {
  transform: translateY(-5px);
}
</style>
