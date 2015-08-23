package com.myweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static void sendHttpRequest(final String address,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while ((line = reader.readLine()) != null) {
						response.append(line);
					}
					if (listener != null) {
						// 回调onFinish()方法
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (listener != null) {
						// 回调onError()方法
						listener.onError(e);
					}
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}
			}
		}).start();
	}

	// 创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();

	/**
	 * @param url
	 *            发送请求的URL
	 * @return 服务器相应字符串
	 * @throws Exception
	 */

	public static String getRequest(final String url) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpGet对象
						HttpGet httpGet = new HttpGet(url);
						// 发送Get请求
						HttpResponse httpResponse = httpClient.execute(httpGet);
						// 如果服务器成功地返回响应
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// 获取服务器响应字符串
							String result = EntityUtils.toString(
									httpResponse.getEntity(), "utf-8");
							return result;
						}
						return null;
					}
				});
		new Thread(task).start();
		return task.get();
	}

	/**
	 * @param params
	 *            请求参数
	 * @return 服务器相应字符串
	 * @throws Exception
	 */
	public static String postRequest(final String url,
			final Map<String, String> rawParams) throws Exception {
		FutureTask<String> task = new FutureTask<String>(
				new Callable<String>() {
					@Override
					public String call() throws Exception {
						// 创建HttpPost对象
						HttpPost httpPost = new HttpPost(url);
						// 如果传递参数个数比较多，可以对传递的参数进行封装
						List<NameValuePair> params = new ArrayList<NameValuePair>();
						for (String key : rawParams.keySet()) {
							// 封装请求参数
							params.add(new BasicNameValuePair(key, rawParams
									.get(key)));
						}
						// 请求设置参数
						httpPost.setEntity(new UrlEncodedFormEntity(params,
								"gbk"));
						// 发送POST请求
						HttpResponse httpResponse = httpClient
								.execute(httpPost);
						// 如果服务器成功返回响应
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							// 获取服务器响应字符串
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							return result;
						}
						return null;

					}
				});

		new Thread(task).start();
		return task.get();
	}
}
