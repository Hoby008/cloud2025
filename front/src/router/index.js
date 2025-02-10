import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Accueil',
    component: () => import('../views/AccueilView.vue')
  },
  {
    path: '/portefeuille',
    name: 'Portefeuille',
    component: () => import('../views/PortefeuilleView.vue')
  },
  {
    path: '/operations',
    name: 'Operations',
    component: () => import('../views/OperationsView.vue')
  },
  {
    path: '/notifications',
    name: 'Notifications',
    component: () => import('../views/NotificationsView.vue')
  },
  {
    path: '/profil',
    name: 'Profil',
    component: () => import('../views/ProfilView.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/LoginView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Navigation guard pour l'authentification
router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token') // Vérifier l'authentification
  
  if (to.name !== 'Login' && !isAuthenticated) {
    next({ name: 'Login' })
  } else {
    next()
  }
})

export default router
