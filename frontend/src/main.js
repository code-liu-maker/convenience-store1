import './assets/main.css'
import { createApp } from 'vue'
import App from './App.vue'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElIcons from '@element-plus/icons-vue'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import 'dayjs/locale/zh-cn'

import router from './router/myRouter'

const myapp = createApp(App)

myapp.use(ElementPlus, { locale: zhCn })
myapp.use(router)

Object.keys(ElIcons).forEach(key => {
    myapp.component(key, ElIcons[key])
})

myapp.mount('#app')