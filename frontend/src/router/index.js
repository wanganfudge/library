import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import BookView from '../views/BookView.vue'
import UserView from '../views/UserView.vue'
import BorrowView from '../views/BorrowView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    { path: '/', redirect: '/login' },
    { path: '/book', name: 'book', component: BookView },
    { path: '/user', name: 'user', component: UserView },
    { path: '/borrow', name: 'borrow', component: BorrowView }
  ]
})

// 路由守卫：未登录跳转到登录页
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else {
    next()
  }
})

export default router