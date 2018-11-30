package com.study.webapp.controller;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.study.webapp.util.Httpclient;
//import com.zhonghui.core.utils.MD5;

public class KuaiDi100 {
	private static final String CODE_URL = "http://www.kuaidi100.com/autonumber/auto";
	private static final String CODE_KEY = "OEwWoalk6182";
	private static final String POLL_URL = "https://poll.kuaidi100.com/poll/query.do";
	private static final String POLL_KEY = "OEwWoalk6182";
	private static final String POLL_CUSTOMER = "8EC1A8C132B42FC0669005BB8C70F088";

	public static void main(String[] args) {
		String num = "249429579210";
		String code = getcomCode(num);
		JSONObject obj = getPoll(num, code);
		System.out.println(obj.toJSONString());
	}

	public static String getcomCode(String num) {
		String code = "";
		try {
			String url = CODE_URL + "?num=" + num + "&key=" + CODE_KEY;
			String result = Httpclient.doGet(url);
			JSONArray arr = JSON.parseArray(result);
			JSONObject obj = arr.getJSONObject(0);
			code = obj.getString("comCode");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}

	public static JSONObject getPoll(String num, String code) {
		JSONObject obj = new JSONObject();
		try {
			JSONObject param = new JSONObject();
			param.put("com", code);
			param.put("num", num);
			String sign = "";
//			String sign = new MD5().getMD5ofStr(param.toJSONString() + POLL_KEY + POLL_CUSTOMER).toUpperCase();
			Map<String, String> params = new HashMap<>();
			params.put("param", param.toJSONString());
			params.put("sign", sign);
			params.put("customer", POLL_CUSTOMER);
			String result = Httpclient.doPost(POLL_URL, params);
			obj = JSON.parseObject(result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

}
