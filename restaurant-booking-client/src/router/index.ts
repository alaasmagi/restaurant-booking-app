import { createRouter, createWebHistory } from 'vue-router'
import BookingView from '@/views/BookingView.vue'
import AdminView from '@/views/AdminView.vue'
import BookingDetailView from '@/views/BookingDetailView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', component: BookingView },
    { path: '/admin', component: AdminView },
    { path: '/booking/:id', component: BookingDetailView },
  ],
})

export default router
