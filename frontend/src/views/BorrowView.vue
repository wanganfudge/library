<template>
  <div>
    <h2>借阅管理</h2>

    <!-- 借书 -->
    <el-card style="margin-bottom: 20px;">
      <h3>📖 借书</h3>
      <el-form :inline="true">
        <el-form-item label="用户ID">
          <el-input-number v-model="borrowUserId" :min="1" />
        </el-form-item>
        <el-form-item label="图书ID">
          <el-input-number v-model="borrowBookId" :min="1" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="borrowBook">借书</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 查询某用户 -->
    <el-card style="margin-bottom: 20px;">
      <h3>🔍 查询用户借阅记录</h3>
      <el-form :inline="true">
        <el-form-item label="用户ID">
          <el-input-number v-model="queryUserId" :min="1" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="queryByUser">查询</el-button>
          <el-button @click="getAllRecords">显示全部</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 借阅记录列表 -->
    <el-table :data="records" border>
      <el-table-column prop="id" label="借阅编号" width="90" />
      <el-table-column prop="userName" label="用户" width="100" />
      <el-table-column prop="bookTitle" label="图书" />
      <el-table-column prop="borrowDate" label="借阅日期" width="110" />
      <el-table-column prop="dueDate" label="应还日期" width="110" />
      <el-table-column prop="returnDate" label="归还日期" width="110" />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">借阅中</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="success">已归还</el-tag>
          <el-tag v-else type="danger">已逾期</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="scope">
          <el-button
              v-if="scope.row.status === 0"
              type="success"
              size="small"
              @click="returnBook(scope.row.id)">
            归还
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
const records = ref([])
const borrowUserId = ref(1)
const borrowBookId = ref(1)
const queryUserId = ref(1)

const getAllRecords = async () => {
  const res = await axios.get(`${API}/borrow/list`)
  records.value = res.data
}

const queryByUser = async () => {
  const res = await axios.get(`${API}/borrow/user/${queryUserId.value}`)
  records.value = res.data
}

const borrowBook = async () => {
  const res = await axios.post(`${API}/borrow/${borrowUserId.value}/${borrowBookId.value}`)
  ElMessage.success(res.data)
  getAllRecords()
}

const returnBook = async (recordId) => {
  const res = await axios.post(`${API}/borrow/return/${recordId}`)
  ElMessage.success(res.data)
  getAllRecords()
}

onMounted(getAllRecords)
</script>