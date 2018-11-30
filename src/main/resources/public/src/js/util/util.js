import QueryString from 'query-string';
import moment from 'moment';

let util = {
    /**
     * 获取查询参数
     */
    getUrlQueryParam(key) {
        let qureryObject = QueryString.parse(location.search);
        return qureryObject[key];
    },
    /**
     * 在原来URL参数后面追加值
     */
    appendUrlParam (url, key, value, sep) {
        if ((typeof value == "undefined") || value == null)
            value = "";

        var searchIndex = url.indexOf("?");
        if (searchIndex < 0) 
            return url + "?" + key + "=" + encodeURIComponent(value);

        var params = QueryString.parse(url.substr(searchIndex + 1));
        if (!params[key] && params[key] !== 0)
            params[key] = "";
        params[key] = params[key] + (sep || "") + value;
        return url.substr(0, searchIndex) + "?" + QueryString.stringify(params);
    },
    isLogin() {
        return false;
    },
    getPreviewUrl(fileName) {

        // let index = fileName.lastIndexOf(".");
        // let ext = fileName.substring(index + 1).toLocaleLowerCase();
        // if (ext == "ods" || ext == "xls" || ext == "xlsb" || ext == "xlsm" || ext == "xlsx") {
        //     //Excel
        //     return "http://officewebapps.huitax.com/x/_layouts/xlviewerinternal.aspx?WOPISrc=";
        // } else if (ext == "doc" || ext == "docm" || ext == "docx" || ext == "dot" || ext == "dotm" || ext == "dotx" || ext == "odt") {
        //     //Word
        //     return "http://officewebapps.huitax.com/wv/wordviewerframe.aspx?WOPISrc=";
        // } else if (ext == "pdf") {
        //     //pdf
        //     return "http://officewebapps.huitax.com/wv/wordviewerframe.aspx?PdfMode=1&WOPISrc=";
        // } else if (ext == "ppt" || ext == "pptm" || ext == "pptx" || ext == "odp" || ext == "pot" || ext == "potm" || ext == "potx" || ext == "pps" || ext == "ppsm" || ext == "ppsx") {
        //     //PowerPoint ppt
        //     return "http://officewebapps.huitax.com/p/PowerPointFrame.aspx?PowerPointView=ReadingView&WOPISrc=";
        // } else {
        //     return null;
        // }
        return `http://officewebapps.huitax.com/office/${fileName}`;
    },
    getUserInfo() {
        return {};
    },
    getUserName() {
        return "";
    },
    /**
     * 获取安全手机号码，隐藏中间位数
     */
    saveMobile(mobile) {
        if (mobile)
            return mobile.substr(0, 3) + "****" + mobile.substr(7);
        return "";
    },
    /**
     * 阿里图片缩放，其中 fit 可选:
     * lfit：等比缩放，限制在设定在指定w与h的矩形内的最大图片。默认
     * mfit：等比缩放，延伸出指定w与h的矩形框外的最小图片。
     * fill：固定宽高，将延伸出指定w与h的矩形框外的最小图片进行居中裁剪。
     * pad：固定宽高，缩略填充*
     * fixed：固定宽高，强制缩略
     */
    ossresize (str, width, height, fit) {
        if (str && (width || height)) {
            var params = ["image/resize"];
            if (fit)
                params.push("m_" + fit);
            if (width)
                params.push("w_" + width);
            if (height)
                params.push("h_" + height);
            str = util.appendUrlParam(str, "x-oss-process", params.join(","), ",");
        }
        return str;
    },
    /**
     * 阿里图片裁剪，
     * 其中 offset 裁剪原点，可选值九宫格[nw,north,ne,west,center,east,ne]，或者坐标点(x,y)
     */
    osscrop (str, width, height, offset) {
        if (str && (width || height)) {
            var params = ["image/crop"];
            if (width)
                params.push("w_" + width);
            if (height)
                params.push("h_" + height);
            if (offset) {
                if (["nw", "north", "ne", "west", "center", "east", "ne"].indexOf(offset) >= 0)
                    params.push("g_" + offset);
                else {
                    offset = offset.split(",");
                    offset = [parseInt(offset[0]), parseInt(offset[1])];
                    params.push("x_" + (offset[0] || 0));
                    params.push("y_" + (offset[1] || 0));
                }
            }
            str = util.appendUrlParam(str, "x-oss-process", params.join(","), ",");
        }
        return str;
    },
    getFriendlyDate (datetime) {
        if (datetime) {
            let date = new Date(datetime), today = new Date();
            if (date.getFullYear() != today.getFullYear()) {
                return moment(date).format("YYYY-MM-DD HH:mm");
            }
            else {
                let _time = today.getTime() - datetime;
                if (_time > 172800000)
                    return moment(datetime).format("MM-DD HH:mm");
                if (_time > 86400000)
                    return "1天前";
                if (_time > 3600000)
                    return parseInt(_time / 3600000) + "小时前";
                if (_time > 1800000)
                    return "30分钟前";
                if (_time > 300000)
                    return "5分钟前";
                return "刚刚";
            }
        }
        return "";
    }
}

export default util;