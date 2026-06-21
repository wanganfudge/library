<template>
  <div class="login-container">
    <el-card class="login-box">
      <h2>📚 图书管理系统</h2>
      <h3>用户登录</h3>

      <el-form :model="loginForm" label-width="0">
        <el-form-item>
          <el-input
              v-model="loginForm.username"
              placeholder="用户名"
              prefix-icon="User"
          />
        </el-form-item>

        <el-form-item>
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="密码"
              prefix-icon="Lock"
              @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              style="width: 100%"
              @click="handleLogin"
              :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>

      <p style="color: #999; font-size: 12px; text-align: center;">
        默认账号：admin / 123456
      </p>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const router = useRouter()
const API = 'http://localhost:8080'

const loginForm = ref({
  username: '',
  password: ''
})

const loading = ref(false)

const handleLogin = async () => {
  if (!loginForm.value.username || !loginForm.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  loading.value = true
  try {
    const res = await axios.post(`${API}/api/login`, loginForm.value)
    if (res.data.success) {
      // 保存登录状态
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('username', loginForm.value.username)
      ElMessage.success('登录成功')
      router.push('/book')  // 跳转到图书页面
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (error) {
    ElMessage.error('登录失败，请检查后端是否启动')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f0f2f5;
}
.login-box {
  width: 400px;
}
.login-box h2, .login-box h3 {
  text-align: center;
  margin-bottom: 20px;
}
</style>