<template>
  <div class="app">
    <el-container v-if="isLoggedIn">
      <el-header>
        <div class="header-content">
          <h1>📚 图书管理系统</h1>
          <div>
            <span>欢迎，{{ username }}</span>
            <el-button type="danger" size="small" @click="logout" style="margin-left: 10px">
              退出
            </el-button>
          </div>
        </div>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-menu :router="true" :default-active="$route.path">
            <el-menu-item index="/book">
              <span>图书管理</span>
            </el-menu-item>
            <el-menu-item index="/user">
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/borrow">
              <span>借阅管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>

    <!-- 未登录只显示路由内容（登录页） -->
    <router-view v-else></router-view>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const isLoggedIn = computed(() => {
  return !!localStorage.getItem('token') && route.path !== '/login'
})

const username = computed(() => {
  return localStorage.getItem('username') || ''
})

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.push('/login')
}
</script>

<style>
.app {
  height: 100vh;
}
.el-header {
  background: #409eff;
  color: white;
}
.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
}
.el-aside {
  background: #f5f7fa;
}
</style>