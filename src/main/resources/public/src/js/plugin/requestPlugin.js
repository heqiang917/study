import Request from '../util/request'; 
export default class RequestPlugin {
    /**
     * 异步请求接口插件
     * @param {Object} Vue 
     * @param {Object} options 
     */
    static install(Vue, options){ 
        Vue.prototype.$request =Request;
    }
}