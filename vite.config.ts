import {defineConfig, loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig(({mode})=>{
  switch (mode) {
    case "development":
      console.log("当前运行环境为：开发环境");
      break;
    case "test":
      console.log("当前运行环境为：测试环境");
      break;
    case "production":
      console.log("当前运行环境为：生产环境");
      break;
  }
  return{
    plugins: [vue()],
    define: {
      'process.env': {}
    },
  }
})
