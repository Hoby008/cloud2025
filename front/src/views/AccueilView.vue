<template>
  <v-container fluid class="dashboard-background">
    <v-row>
      <v-col cols="12">
        <h1 class="text-h4 mb-6 text-violet font-weight-bold">
          Tableau de Bord Crypto
        </h1>
        
        <v-card 
          class="mb-6 dashboard-card elevation-0" 
          outlined
        >
          <v-card-title class="text-violet font-weight-medium">
            <v-icon left color="primary">mdi-wallet</v-icon>
            Portefeuille Global
          </v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="6">
                <div class="text-subtitle-1 text-grey">Solde Total</div>
                <div class="text-h5 text-violet font-weight-bold">
                  {{ totalBalance.toLocaleString() }} €
                </div>
              </v-col>
              <v-col cols="6">
                <div class="text-subtitle-1 text-grey">Crypto Préférée</div>
                <div class="text-h6 text-violet">
                  {{ preferredCrypto }}
                </div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <v-card 
          class="dashboard-card elevation-0" 
          outlined
        >
          <v-card-title class="text-violet font-weight-medium">
            <v-icon left color="primary">mdi-swap-horizontal</v-icon>
            Dernières Opérations
          </v-card-title>
          <v-card-text>
            <v-data-table
              :headers="operationHeaders"
              :items="recentOperations"
              class="operations-table"
              hide-default-footer
              disable-pagination
            >
              <template v-slot:item="{ item }">
                <tr class="operation-row">
                  <td>{{ formatDate(item.date) }}</td>
                  <td>
                    <v-chip 
                      :color="getOperationColor(item.type)"
                      small
                      outlined
                    >
                      {{ item.type }}
                    </v-chip>
                  </td>
                  <td>{{ item.amount.toLocaleString() }} €</td>
                  <td>{{ item.crypto }}</td>
                </tr>
              </template>
            </v-data-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/authStore'
import apiService from '@/services/apiService'

const authStore = useAuthStore()

const totalBalance = ref(0)
const preferredCrypto = ref('Bitcoin')
const recentOperations = ref([])

const operationHeaders = [
  { title: 'Date', value: 'date' },
  { title: 'Type', value: 'type' },
  { title: 'Montant', value: 'amount' },
  { title: 'Crypto', value: 'crypto' }
]

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  })
}

const getOperationColor = (type) => {
  switch(type) {
    case 'Dépôt': return 'success'
    case 'Retrait': return 'error'
    case 'Achat': return 'primary'
    case 'Vente': return 'warning'
    default: return 'grey'
  }
}

onMounted(async () => {
  try {
    const userProfile = await authStore.fetchUserProfile()
    totalBalance.value = userProfile.solde
    preferredCrypto.value = userProfile.cryptoPreferee

    const operations = await apiService.get('/operations/recent')
    recentOperations.value = operations
  } catch (error) {
    console.error('Erreur de chargement', error)
  }
})
</script>

<style scoped>
.dashboard-background {
  background-color: #f4f4f8;
  padding: 20px;
}

.dashboard-card {
  border-radius: 16px !important;
  background-color: white !important;
  box-shadow: 0 10px 30px rgba(106, 90, 205, 0.05) !important;
  margin-bottom: 20px;
}

.text-violet {
  color: #6a5acd !important;
}

.operations-table {
  background-color: transparent !important;
}

.operation-row {
  transition: background-color 0.3s ease;
}

.operation-row:hover {
  background-color: rgba(106, 90, 205, 0.05) !important;
}
</style>
