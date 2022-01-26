import axios from 'axios'
import Vrouter from '../router/index.js'
import {ElMessage} from "element-plus";
let [pending,timer] = [[],null]
const time = 3000
const CancelToken = axios.CancelToken
let cancelPending = (config)=>{
    pending.forEach((k,i)=>{
        if(config){
            let configPath = config.url
            let type = config.method
            switch(type){
                case 'get':
                    configPath+=JSON.stringify(config.params)
                    break
                case 'delete':
                    configPath+=JSON.stringify(config.params)
                    break
                case 'post':
                    configPath+=config.data?JSON.stringify(config.data):''
                    break
                case 'put':
                    configPath+=config.data?JSON.stringify(config.data):''
                    break
                default:
                    break
            }
            if(k.curPath === configPath){
                k.Cancel('请勿重复请求！'+configPath)
            }
            pending.splice(i,1)
        }else{
            k.Cancel()
            pending.splice(i,1)
        }
        
    })
    clearTimeout(timer)
    timer = setTimeout(()=>{
        pending = []
    },time)
}
const Axios = axios.create({
    withCredentials:true,
    timeout:3*60*1000
})
Axios.interceptors.request.use((config)=>{
    config.headers.token = localStorage.getItem('token')
    let curPath = config.url
    let type = config.method
    switch(type){
        case 'get':
            curPath+=JSON.stringify(config.params)
            break
        case 'delete':
            curPath+=JSON.stringify(config.params)
            break
        case 'post':
            curPath+=config.data?JSON.stringify(config.data):''
            break
        case 'put':
            curPath+=config.data?JSON.stringify(config.data):''
            break
        default:
            break
    }
    cancelPending(config)
    config.cancelToken = new CancelToken(c=>{
        pending.push({'curPath':curPath,'Cancel':c})
    })
    return config
},error=>{
    console.log(error,'error')
    return Promise.reject(error)
})
Axios.interceptors.response.use(response=>{
    if(response.status===200){
        if(response.data.status===200){
            return response.data
        }else if(response.data.status===401){
            Vrouter.push({path:'/login'})
            return Promise.reject(response.data.status)
        }else{
            ElMessage.error(response.data.msg);
            return Promise.reject(response.data.msg)
        }
    }else{
        return Promise.reject(response.status)
    }
},error=>{
    if (error.response.status) {
        switch (error.response.status) {
            case 404:
                Vrouter.push({path:'/404'})
                ElMessage.error("网络请求不存在");
                break;
            // 其他错误，直接抛出错误提示
            default:
                ElMessage.error(error.response.data.message);
        }
    }
    return Promise.reject(error)
})
export default Axios