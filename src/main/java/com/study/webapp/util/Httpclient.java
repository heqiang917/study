package com.study.webapp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * httpclient工具类
 * 
 * @author admin
 *
 *         2018年10月25日
 */
public class Httpclient {

	private static final int CODE = 200;

	/**
	 * post上传文件
	 * 
	 * @param file
	 * @param remote_url
	 * @return
	 */
	public static String postFile(MultipartFile file, String remote_url) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		try {
			String fileName = file.getOriginalFilename();
			HttpPost httpPost = new HttpPost(remote_url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addBinaryBody("form-file", file.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
			builder.addTextBody("filename", fileName);// 类似浏览器表单提交，对应input的name和value
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse res = httpClient.execute(httpPost);// 执行提交
			HttpEntity responseEntity = res.getEntity();
			if (responseEntity != null) {
				// 将响应内容转换为字符串
				result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * GET请求
	 * 
	 * @param httpclient
	 * @param response
	 * @param url
	 * @param param
	 * @param cookie
	 * @return
	 */
	public static CloseableHttpResponse doResponse(CloseableHttpClient httpclient, CloseableHttpResponse response,
			String url, Map<String, String> param, String cookie) {
		try {
			// 创建uri
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			URI uri = builder.build();

			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			if (cookie != null) {
				httpGet.setHeader("Cookie", cookie);
			}
			// 执行请求
			response = httpclient.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * GET请求返回response
	 * 
	 * @param url
	 * @param cookie
	 * @return
	 */
	public static CloseableHttpResponse doResponseGet(String url, String cookie) {

		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		try {
			response = doResponse(httpclient, response, url, null, cookie);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return response;
	}

	/**
	 * GET请求返回String
	 * 
	 * @param url
	 * @param param
	 * @param cookie
	 * @return
	 */
	public static String doGet(String url, Map<String, String> param, String cookie) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			response = doResponse(httpclient, response, url, param, cookie);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == CODE) {
				resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultString;
	}

	/**
	 * 带token的GET请求
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	public static String doGetToken(String url, String token) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			// 创建Http Get请求
			HttpGet httpGet = new HttpGet(url);
			// 把认证信息发到header中
			httpGet.setHeader("Authorization", "Bearer " + token);
			httpGet.setHeader("X-Requested-With", "XMLHttpRequest");
			// 执行http请求
			response = httpclient.execute(httpGet);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	/**
	 * GET请求获取图片
	 * 
	 * @param url
	 * @param cookie
	 * @return
	 */
	public static byte[] doImgGet(String url, String cookie) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		byte[] b = null;
		try {
			response = doResponse(httpclient, response, url, null, cookie);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == CODE) {
				InputStream in = response.getEntity().getContent();
				b = readInputStream(in);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	public static String doGet(String url) {
		return doGet(url, null, null);
	}

	public static String doGet(String url, String cookie) {
		return doGet(url, null, cookie);
	}

	/**
	 * POST请求
	 * 
	 * @param httpClient
	 * @param response
	 * @param url
	 * @param param
	 * @param cookie
	 * @return
	 */
	public static CloseableHttpResponse postResponse(CloseableHttpClient httpClient, CloseableHttpResponse response,
			String url, Map<String, String> param, String cookie) {

		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			if (cookie != null) {
				httpPost.setHeader("Cookie", cookie);
			}

			// 创建参数列表
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				// 模拟表单
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "utf-8");
				httpPost.setEntity(entity);
			}
			// 执行http请求
			response = httpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * POST请求返回response和String
	 * 
	 * @param url
	 * @param param
	 * @param cookie
	 * @return
	 */
	public static Map<String, Object> doPostObject(String url, Map<String, String> param, String cookie) {
		// 创建Httpclient对象
		Map<String, Object> map = new HashMap<>(2);
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {

			response = postResponse(httpClient, response, url, param, cookie);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
			map.put("response", response);
			map.put("result", resultString);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return map;
	}

	/**
	 * POST请求返回String
	 * 
	 * @param url
	 * @param param
	 * @param cookie
	 * @return
	 */
	public static String doPost(String url, Map<String, String> param, String cookie) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			response = postResponse(httpClient, response, url, param, cookie);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static String doPost(String url, String cookie) {
		return doPost(url, null, cookie);
	}

	public static String doPost(String url, Map<String, String> param) {
		return doPost(url, param, null);
	}

	/**
	 * POST请求带token
	 * 
	 * @param url
	 * @param token
	 * @return
	 */
	public static String doPostToken(String url, String token) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容

			// 把认证信息发到header中
			httpPost.setHeader("Authorization", "Bearer " + token);
			httpPost.setHeader("X-Requested-With", "XMLHttpRequest");
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	/**
	 * POST请求带json格式返回String
	 * 
	 * @param url
	 * @param json
	 * @return
	 */
	public static String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return resultString;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[2048];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

}
