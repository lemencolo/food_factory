// src/utils/request.js
import axios from 'axios';

const ocr_request = axios.create({
    //baseURL: 'http://192.168.1.56:8080',  // 注意！！ 这里是全局统一加上了 后端接口前缀 前缀，后端必须进行跨域配置！
    baseURL: 'http://localhost:8080',  // 注意！！ 这里是全局统一加上了 后端接口前缀 前缀，后端必须进行跨域配置！
    //baseURL: 'http://172.20.10.5:8080',
    //baseURL: 'http://192.168.237.224:8080',
    //baseURL: 'http://192.168.237.224:8080',
    timeout: 5000
});

// request 拦截器
ocr_request.interceptors.request.use(
    config => {
        config.headers['Content-Type'] = 'multipart/form-data';
        // 可以在这里添加 token 或其他通用请求头
        // config.headers['token'] = user.token;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);


export default ocr_request;