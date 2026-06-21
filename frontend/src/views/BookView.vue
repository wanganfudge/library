<template>
  <div>
    <h2>图书管理</h2>

    <!-- 条件查询 -->
    <el-card style="margin-bottom: 20px;">
      <el-form :inline="true">
        <el-form-item label="关键字">
          <el-input
              v-model="searchKeyword"
              placeholder="书名 / 作者 / ISBN"
              style="width: 250px"
              @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 添加图书 -->
    <el-card style="margin-bottom: 20px;">
      <h3>添加图书</h3>
      <el-form :inline="true" :model="newBook">
        <el-form-item label="书名">
          <el-input v-model="newBook.title" placeholder="书名" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="newBook.author" placeholder="作者" />
        </el-form-item>
        <el-form-item label="ISBN">
          <el-input
              v-model="newBook.isbn"
              placeholder="ISBN（纯数字）"
              oninput="value=value.replace(/[^\d]/g,'')"
          />
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="newBook.publisher" placeholder="出版社" />
        </el-form-item>
        <el-form-item label="出版年份">
          <el-input-number v-model="newBook.publishYear" :min="1900" :max="2030" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="newBook.stock" :min="0" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addBook" :loading="loading">添加</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 图书列表 -->
    <el-table :data="books" border>
      <el-table-column type="index" label="序号" width="80" />
      <el-table-column prop="title" label="书名" />
      <el-table-column prop="author" label="作者" />
      <el-table-column prop="isbn" label="ISBN" />
      <el-table-column prop="publisher" label="出版社" />
      <el-table-column prop="publishYear" label="出版年份" width="100" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEdit(scope.row)">
            编辑
          </el-button>
          <el-button type="danger" size="small" @click="deleteBook(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 编辑对话框 -->
    <el-dialog v-model="editDialogVisible" title="修改图书" width="500px">
      <el-form :model="editBook" label-width="100px">
        <el-form-item label="书名">
          <el-input v-model="editBook.title" />
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="editBook.author" />
        </el-form-item>
        <el-form-item label="ISBN">
          <el-input
              v-model="editBook.isbn"
              oninput="value=value.replace(/[^\d]/g,'')"
          />
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="editBook.publisher" />
        </el-form-item>
        <el-form-item label="出版年份">
          <el-input-number v-model="editBook.publishYear" :min="1900" :max="2030" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="editBook.stock" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateBook" :loading="updateLoading">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '../utils/request'
import { ElMessage } from 'element-plus'

const books = ref([])
const searchKeyword = ref('')
const loading = ref(false)
const updateLoading = ref(false)

const newBook = ref({
  title: '',
  author: '',
  isbn: '',
  publisher: '',
  publishYear: 2024,
  stock: 1
})

// 编辑相关
const editDialogVisible = ref(false)
const editBook = ref({})

const getBooks = async () => {
  try {
    const res = await request.get('/book/list')
    books.value = res.data
  } catch (e) {
    books.value = []
  }
}

const handleSearch = async () => {
  try {
    const res = await request.get(`/book/search?keyword=${searchKeyword.value}`)
    books.value = res.data
  } catch (e) {
    books.value = []
  }
}

const resetSearch = () => {
  searchKeyword.value = ''
  getBooks()
}

const addBook = async () => {
  if (!newBook.value.title) {
    ElMessage.warning('请输入书名')
    return
  }

  loading.value = true
  try {
    const res = await request.post('/book/add', newBook.value)
    if (res.data.success) {
      ElMessage.success(res.data.message)
      newBook.value = { title: '', author: '', isbn: '', publisher: '', publishYear: 2024, stock: 1 }
      getBooks()
    } else {
      ElMessage.error(res.data.message)
    }
  } finally {
    loading.value = false
  }
}

const openEdit = (row) => {
  editBook.value = { ...row }
  editDialogVisible.value = true
}

const updateBook = async () => {
  updateLoading.value = true
  try {
    const res = await request.put('/book/update', editBook.value)
    if (res.data.success) {
      ElMessage.success(res.data.message)
      editDialogVisible.value = false
      getBooks()
    } else {
      ElMessage.error(res.data.message)
    }
  } finally {
    updateLoading.value = false
  }
}

const deleteBook = async (id) => {
  try {
    const res = await request.delete(`/book/${id}`)
    if (res.data.success) {
      ElMessage.success(res.data.message)
      getBooks()
    } else {
      ElMessage.error(res.data.message)
    }
  } catch (e) {
    ElMessage.error('删除失败')
  }
}

onMounted(getBooks)
</script>