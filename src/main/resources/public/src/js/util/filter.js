import Vue from 'vue';
import moment from 'moment';
import numeral from 'numeral';
import _ from 'lodash';
import Utils from './util';

/**
 * 格式化日期 需以yyyy-MM-dd hh:mm:ss格式传入
 * @param y
 * @param m
 * @param d
 */
// Vue.filter('strDateFormat', (date, type) => {
//     if (date) {
//         date = date.toString();
//         let time = "";
//         if (date.indexOf("-") > -1 || date.indexOf(":") > -1) {
//             let list = date.split(" ");
//             if (list.length > 0) {
//                 let dates = list[0].split("-");
//                 let times = [];
//                 if (list[1]) {
//                     times = list[1].split(":");
//                 }

//                 let y = parseInt(dates[0]);
//                 let m = parseInt(dates[1] - 1);
//                 let d = parseInt(dates[2]);

//                 if (times.length > 0) {
//                     let h = parseInt(times[0]);
//                     let min = parseInt(times[1]);
//                     let s = parseInt(times[2]);
//                     time = new Date(y, m, d, h, min, s);
//                 } else {
//                     time = new Date(y, m, d);
//                 }
//             }
//         } else {
//             date = parseInt(date);
//             time = new Date(date);
//         }
//         return time.format(type);
//     }
// });

/**
 * 函数参考
 * http://itbilu.com/nodejs/npm/VkCir3rge.html
 * http://momentjs.com/docs/
 * {{1483592375911 |moment('YYYY-MM-DD HH:mm:ss')}}
 * {{'2017-01-22' |moment('YYYY-MM-DD HH:mm:ss')}}
 * 
 */
Vue.filter('moment', (date, format) => {
    return moment(date).format(format);

});
/**
 * http://numeraljs.com/
 * {{'1000' |numeral('0,0')}}
 * {{'1000' |numeral('0.00')}}
 */
Vue.filter('numeral', (number, format) => {
    if (_.isNil(number)) {
        number = 0;
    }
    return numeral(number).format(format);

});

/**
 * 获取字符长度，中文占2个字符
 */
Vue.filter('charLength', (str) => {
    if (!str) return 0;
    if (typeof str != "string") {
        str += "";
    }
    // 把双字节的替换成两个单字节的然后再获得长度
    return str.replace(/[^\x00-\xff]/g, "01").length;
});

/**
 * 获取用户头像
 */
Vue.filter('headimg', (str, defval, width, height) => {
    if (defval && !isNaN(defval)) {
        height = width;
        width = defval;
        defval = null;
    }
    if (str && /aliyuncs/.test(str)) {
        if (width || height) {
            width = width || height;
            height = height || width;
            var params = ["image/resize", "m_fixed"];
            if (width && width != "auto")
                params.push("w_" + width);
            if (height && height != "auto")
                params.push("h_" + height);
            if (params.length > 2) {
                params = params.join(",");
                var searchIndex = str.indexOf("?");
                if (searchIndex < 0)
                    str += "?x-oss-process=" + params;
                else {
                    var _params = str.substr(searchIndex + 1).split("&");
                    _params = _.map(_params, function (temp) {
                        if (/^x-oss-process=/.test(temp))
                            temp += "," + params;
                        return temp;
                    });
                    str = str.substr(0, searchIndex) + "?" + _params.join("&");
                }
            }
        }
    }
    return str || defval || "/images/user_def.png";
});

/**
 * 获取圈子头像
 */
Vue.filter('circleimg', (str, width, height) => {
    if (str && /aliyuncs/.test(str)) {
        if (width || height) {
            width = width || height;
            height = height || width;
            var params = ["image/resize", "m_fixed"];
            if (width && width != "auto")
                params.push("w_" + width);
            if (height && height != "auto")
                params.push("h_" + height);
            if (params.length > 2) {
                params = params.join(",");
                var searchIndex = str.indexOf("?");
                if (searchIndex < 0)
                    str += "?x-oss-process=" + params;
                else {
                    var _params = str.substr(searchIndex + 1).split("&");
                    _params = _.map(_params, function (temp) {
                        if (/^x-oss-process=/.test(temp))
                            temp += "," + params;
                        return temp;
                    });
                    str = str.substr(0, searchIndex) + "?" + _params.join("&");
                }
            }
        }
    }
    return str || "/images/circle_def.png";
});
/**
 * 阿里图片缩放，其中 tfit 可选:
 * lfit：等比缩放，限制在设定在指定w与h的矩形内的最大图片。默认
 * mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
 * fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
 * pad：固定宽高，缩略填充*
 * fixed：固定宽高，强制缩略
 */
Vue.filter("ossimg", (str, width, height, tfit) => {
    if (str && (width || height)) {
        var params = ["image/resize"];
        if (tfit)
            params.push("m_" + tfit);
        if (width)
            params.push("w_" + width);
        if (height)
            params.push("h_" + height);
        str += str.indexOf("?") < 0 ? "?" : "&";
        str += "x-oss-process=" + params.join(",");
    }
    return str;
});
/**
 * 阿里图片缩放，其中 fit 可选:
 * lfit：等比缩放，限制在设定在指定w与h的矩形内的最大图片。默认
 * mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
 * fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
 * pad：固定宽高，缩略填充*
 * fixed：固定宽高，强制缩略
 */
Vue.filter("ossresize", (str, width, height, fit) => {
    return Utils.ossresize(str, width, height, fit);
});
/**
 * 阿里图片裁剪，
 * 其中 offset 裁剪原点，可选值九宫格[nw,north,ne,west,center,east,ne]，或者坐标点(x,y)
 */
Vue.filter("osscrop", (str, width, height, offset) => {
    return Utils.osscrop(str, width, height, offset);
});
/**
 * 超出数字后显示数字+
 */
Vue.filter('overnum', (value, max) => {
    var val = parseInt(value);
    if (isNaN(val))
        return value;
    max = parseInt(max) || 10000;
    return val < max ? val : (max + "+");
});
/**
 * 显示缩写数字，如：1k, 12k
 */
Vue.filter('shortnum', (value) => {
    var val = parseInt(value);
    if (isNaN(val))
        return value;
    if (val < 1000)
        return val;
    return parseInt(val / 1000) + "K";
});
