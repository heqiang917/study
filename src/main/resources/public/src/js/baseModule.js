import '../scss/base.scss';
import '../scss/module.scss';
import '../scss/unit.scss';
import 'element-ui/lib/theme-default/index.css';
import '../scss/element.scss';

import Vue from 'vue';
import QueryString from "querystring";
import ElementUI from 'element-ui';
import RequestPlugin from './plugin/requestPlugin';
// import RegionPicker from './plugin/vue-region-picker';
// import CHINA_REGION from 'china-area-data';
import Cookies from 'js-cookie';
import _ from 'lodash';
// import Stomp from 'webstomp-client';
// import SockJS from 'sockjs-client';
import "babel-polyfill";
import "./util/filter";

import EventBus from "./eventBus";

Vue.use(ElementUI);
Vue.use(RequestPlugin);
// Vue.use(RegionPicker, {
//     region: CHINA_REGION,
//     vueVersion: 2
// });

//全局混合
Vue.mixin({
    methods: {
        _$showMsg (msg, type, duration, closeHandler) {
            if (msg) {
                if (msg instanceof Error)
                    msg = msg.toString();
                else if (msg.hasOwnProperty("msg"))
                    msg = msg.msg;
            }
            if (typeof type == "function") {
                closeHandler = type;
                duration = null;
                type = "error";
            }
            if (typeof duration == "function") {
                closeHandler = duration;
                duration = null;
            }
            if (!/^(error|success|warning|info)$/.test(type))
                type = "error";
            if (isNaN(duration) || !duration || duration < 0)
                duration = type == "error" ? 600000 : 2000;
            return this.$message({
                message: (msg || "出错了！"), 
                type: type, 
                duration: duration, 
                onClose: closeHandler,
                showClose: (type == "error")
            });
        },

        // 发起一个 GET 请求
        _$get (url, params, success, failure) {
            if (typeof params == "function") {
                failure = success;
                success = params;
                params = null;
            }
            let _failured = (data) => {
                if (typeof failure == "function") {
                    if (failure(data.code, data.msg, data) !== false)
                        this._$showMsg(data);
                }
                else if (failure !== false)
                    this._$showMsg(data);
            }
            if (params) {
                url += url.indexOf("?") < 0 ? "?" : "&";
                url += QueryString.stringify(params);
            }
            this.$request.get(url).then((result) => {
                if (result && result.code == 200) {
                    if (typeof success == "function")
                        success(result.data, this._$$pageInfo(result.pageInfo), result);
                }
                else {
                    _failured(result);
                }
            }).catch(_failured);
        },

        // 发起一个 POST 请求
        _$post (url, params, success, failure) {
            if (typeof params == "function") {
                failure = success;
                success = params;
                params = null;
            }
            let _failured = (data) => {
                if (typeof failure == "function") {
                    if (failure(data.code, data.msg, data) !== false)
                        this._$showMsg(data);
                }
                else if (failure !== false)
                    this._$showMsg(data);
            };
            this.$request.post(url, params)
                .then((result) => {
                    if (result && result.code == 200) {
                        if (typeof success == "function")
                            success(result.data, this._$$pageInfo(result.pageInfo), result);
                    }
                    else {
                        _failured(result);
                    }
                })
                .catch(_failured);
        },

        _$$pageInfo (data) {
            return data ? {page: data.pageNum, size: data.limit, total: data.total} : null;
        },

        _$isLogin () {
            return !!this._$getToken();
        },
        _$getUser () {
            return window["g_userinfo"];
        },
        _$getUserId () {
            let _userId = Cookies.get("userid") || 0;
            if (!_userId && window["g_userinfo"])
                _userId = window["g_userinfo"].id || 0;
            return _userId;
        },
        _$getToken () {
            return Cookies.get("TT_TOKEN") || null;
        },

        // 浏览器本地存储
        _$localCache (name, value) {
            if (localStorage && name) {
                if (typeof value === "undefined" || value === null) {
                    return localStorage[name];
                }
                else {
                    localStorage[name] = value;
                }
            }
        },

        _$num (value, decimal) {
            if (!isNaN(value) && (value || value === 0)) {
                value = parseFloat(value);
                decimal = parseInt(decimal);
                if (isNaN(decimal))
                    decimal = 2;
                return value.toFixed(decimal);
            }
            return NaN;
        },
        _$numf (value, decimal) {
            value = this._$num(value, decimal);
            if (isNaN(value))
                return NaN;
            value = value.split(".");
            if (value[0].length > 3) {
                var nums = value[0].split("").reverse();
                var _nums = [];
                for (var i = 0; i < nums.length; i++) {
                    if (i && (i % 3 === 0))
                        _nums.unshift(",");
                    _nums.unshift(nums[i]);
                }
                value[0] = _nums.join("");
                if (/^\-\,/.test(value[0]))
                    value[0] = value[0].replace(/^\-\,/, "-");
            }
            return value.join(".");
        },

        _$formatDate (date) {
            if (!date)
                return null;
            if (typeof date == "number")
                date = new Date(parseInt(date));
            if (date instanceof Date) {
                var result = "" + date.getFullYear();

                var month = date.getMonth() + 1;
                result += "-" + (month < 10 ? "0" : "") + month;

                var day = date.getDate();
                result += "-" + (day < 10 ? "0" : "") + day;

                var hours = date.getHours();
                result += " " + (hours < 10 ? "0" : "") + hours;

                var minutes = date.getMinutes();
                result += ":" + (minutes < 10 ? "0" : "") + minutes;

                // var seconds = date.getSeconds();
                // result += ":" + (seconds < 10 ? "0" : "") + seconds;

                return result;
            }
            return null;
        },

        _$fileExt (fileName) {
            if (/\.(xls|xlt|xla)/i.test(fileName))
                return "xls";
            if (/\.(doc|dot)/i.test(fileName))
                return "doc";
            if (/\.(ppt|pps|pot)/i.test(fileName))
                return "ppt";
            if (/\.(zip|rar|arj|gz)/i.test(fileName))
                return "zip";
            if (/\.(pdf)/i.test(fileName))
                return "pdf";
            if (/\.(png|jpg|gif|bmp|jpeg)/i.test(fileName))
                return "img";
            if (/\.(avi|mp4|mkv|rm|mov|ram|mpg|mpeg|qt|viv)/i.test(fileName))
                return "video";
            if (/\.(mp3|wma|wav)/i.test(fileName))
                return "audio";
            if (/\.(txt|log|css|js|html)/i.test(fileName))
                return "txt";
            return "def";
        },

        _$fileSize (value) {
            if (value < 1024)
                return `${value}B`;
            value = value / 1024;
            if (value < 1024)
                return `${value.toFixed(2)}KB`;
            value = value / 1024;
            if (value < 1024)
                return `${value.toFixed(2)}MB`;
            value = value / 1024;
            if (value < 1024)
                return `${value.toFixed(2)}GB`;
            value = value / 1024;
            return `${value.toFixed(2)}TB`;
        }
    }
});

export default class BaseModule {
    constructor(options) {
        this.options = options || {};
        // console.log('constructor', options);
    }

    init(...vueConfigs) {
        this.initFrameVue();

        _.each(vueConfigs, (item) => {
            new Vue(item);
        });
    }

    initFrameVue() {
    }

};