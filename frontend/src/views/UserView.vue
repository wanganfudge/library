<template>
  <div>
    <h2>用户管理</h2>

    <el-card style="margin-bottom: 20px;">
      <el-form :inline="true" :model="newUser">
        <el-form-item label="用户名">
          <el-input v-model="newUser.username" placeholder="用户名" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="newUser.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="newUser.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item label="最大借阅">
          <el-input-number v-model="newUser.maxBorrow" :min="1" :max="10" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addUser">添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-table :data="users" border>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="username" label="用户名" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="maxBorrow" label="最大借阅" width="100" />
      <el-table-column label="操作" width="120">
        <template #default="scope">
          <el-button type="danger" size="small" @click="deleteUser(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const API = 'http://localhost:8080'
const users = ref([])
const newUser = ref({ username: '', password: '', realName: '', maxBorrow: 5 })

const getUsers = async () => {
  const res = await axios.get(`${API}/user/list`)
  users.value = res.data
}

const addUser = async () => {
  if (!newUser.value.username || !newUser.value.password) {
    ElMessage.warning('请填写完整信息')
    return
  }
  await axios.post(`${API}/user/add`, newUser.value)
  ElMessage.success('添加成功')
  newUser.value = { username: '', password: '', realName: '', maxBorrow: 5 }
  getUsers()
}

const deleteUser = async (id) => {
  await axios.delete(`${API}/user/${id}`)
  ElMessage.success('删除成功')
  getUsers()
}

onMounted(getUsers)
</script>