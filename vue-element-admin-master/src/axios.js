import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080', // 你的后端服务地址
  timeout: 10000, // 请求超时时间
});

export default instance;
