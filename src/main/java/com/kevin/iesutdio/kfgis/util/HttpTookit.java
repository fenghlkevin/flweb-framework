package com.kevin.iesutdio.kfgis.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpTookit implements Closeable {

	private CloseableHttpClient httpClient;
	public static final String CHARSET = "UTF-8";

	private void init(int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout).setConnectTimeout(connectTimeout).setSocketTimeout(socketTimeout)
				.build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	public HttpTookit() {
		init(15000, 15000, 15000);
	}

	public HttpTookit(int connectionRequestTimeout, int connectTimeout, int socketTimeout) {
		init(connectionRequestTimeout, connectTimeout, socketTimeout);
	}

	public byte[] doGet(String url, String charset) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;
		byte[] bs = null;
		try {
			response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				httpGet.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				bs = EntityUtils.toByteArray(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException("HttpClient,Close Response ERROR ");
				}
			}
		}
		return bs;
	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url
	 *            请求的url地址 ?之前的地址
	 * @param params
	 *            请求的参数
	 * @param charset
	 *            编码格式
	 * @return 页面内容
	 */
	public byte[] doPost(String url, Map<String, String> params, String charset, byte[] bytes) {

		List<NameValuePair> pairs = null;
		if (params != null && !params.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
		}
		HttpPost httpPost = new HttpPost(url);
		ByteArrayEntity bae = new ByteArrayEntity(bytes);
		httpPost.setEntity(bae);
		CloseableHttpResponse response = null;
		byte[] bs = null;
		try {
			response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 201) {
				httpPost.abort();
				throw new RuntimeException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				bs = EntityUtils.toByteArray(entity);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					throw new RuntimeException("HttpClient,Close Response ERROR ");
				}
			}
		}
		return bs;
	}

	@Override
	public void close() throws IOException {
		this.httpClient.close();

	}

}