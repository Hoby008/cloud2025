<template>
  <v-container fluid fill-height class="login-background">
    <v-layout align-center justify-center>
      <v-flex xs12 sm8 md4>
        <v-card 
          class="elevation-6 login-card" 
          outlined
        >
          <v-toolbar 
            flat 
            color="white" 
            class="text-center pa-6"
          >
            <v-toolbar-title class="text-h5 text-violet font-weight-bold">
              Crypto6
            </v-toolbar-title>
          </v-toolbar>
          
          <v-card-text>
            <v-form @submit.prevent="handleLogin">
              <v-text-field
                prepend-inner-icon="mdi-account"
                name="login"
                label="Nom d'utilisateur"
                type="text"
                v-model="username"
                :rules="[v => !!v || 'Nom d\'utilisateur requis']"
                required
                outlined
                dense
                color="primary"
                class="mb-3"
              ></v-text-field>

              <v-text-field
                prepend-inner-icon="mdi-lock"
                name="password"
                label="Mot de passe"
                type="password"
                v-model="password"
                :rules="[v => !!v || 'Mot de passe requis']"
                required
                outlined
                dense
                color="primary"
                class="mb-4"
              ></v-text-field>

              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn 
                  type="submit" 
                  color="primary" 
                  :loading="loading"
                  block
                  large
                  class="btn-login"
                >
                  Connexion
                </v-btn>
              </v-card-actions>

              <div class="text-center mt-4">
                <a href="#" class="text-violet text-decoration-none">
                  Mot de passe oublié ?
                </a>
              </div>
            </v-form>
          </v-card-text>

          <v-alert 
            v-if="error" 
            type="error" 
            dismissible
            outlined
            class="mx-6 mb-4"
          >
            {{ error }}
          </v-alert>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

const router = useRouter()
const authStore = useAuthStore()

const username = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

const handleLogin = async () => {
  loading.value = true
  error.value = null

  try {
    await authStore.login({
      username: username.value,
      password: password.value
    })
    
    // Redirection après connexion réussie
    router.push('/')
  } catch (loginError) {
    error.value = 'Identifiants incorrects. Veuillez réessayer.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-background {
  background-color: #f4f4f8;
}

.login-card {
  border-radius: 16px !important;
  background-color: white !important;
  box-shadow: 0 10px 30px rgba(106, 90, 205, 0.1) !important;
}

.text-violet {
  color: #6a5acd !important;
}

.btn-login {
  border-radius: 10px !important;
  text-transform: none !important;
  font-weight: 600 !important;
  height: 50px !important;
}

.v-form {
  padding: 20px;
}

.v-input {
  border-radius: 10px !important;
}
</style>
