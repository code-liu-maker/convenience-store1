import axios from 'axios'
import { ElMessage } from 'element-plus'

const http = axios.create({
    baseURL: '/api',
    timeout: 10000,
    withCredentials: true
})

// 请求拦截器
http.interceptors.request.use(config => {
    return config
}, error => {
    return Promise.reject(error)
})

// 响应拦截器
http.interceptors.response.use(response => {
    if (response.data.code !== '200') {
        ElMessage.error(response.data.message || '请求失败')
        return Promise.reject(response.data)
    }
    return response.data
}, error => {
    if (error.response && error.response.status === 401) {
        ElMessage.error('请先登录')
        window.location.href = '/login'
    } else {
        ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
})

export default http