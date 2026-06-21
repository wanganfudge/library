import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 10000  // 10秒超时
})

// 请求拦截器
request.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = token
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

// 响应拦截器
request.interceptors.response.use(
    response => {
        return response
    },
    error => {
        if (error.response) {
            ElMessage.error('服务器错误：' + error.response.status)
        } else if (error.request) {
            ElMessage.error('网络连接失败，请检查后端是否启动')
        } else {
            ElMessage.error('请求错误：' + error.message)
        }
        return Promise.reject(error)
    }
)

export default request