package com.futurePayment.http;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.futurePayment.constant.Uris;

import android.util.Log;

/**
 * 
 * @author luo
 * 
 */
public class MyHttpClient {
	private static int OK = 200;// OK: Success!
	private HttpClient http = new DefaultHttpClient();
	private String name;

	public MyHttpClient(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @param serviceType
	 *            服务类型
	 * @param json
	 *            发送的JSON请求
	 * @return 服务器返回
	 */
	public MyResponse post(int serviceType, JSONObject json) {
		MyResponse response = null;
		HttpPost post = new HttpPost(Uris.BASIC_URL);
		try {
			JSONObject set = new JSONObject();
			set.put("serviceType", serviceType);
			if (name != null) {
				set.put("name", name);
			}
			if (json != null) {
				set.put("data", json);
			}
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			// String requestJson = AES.encrypt(json.toString());
			params.add(new BasicNameValuePair("json", set.toString()));
			post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
			Log.i("test", "request:" + params.toString());
			HttpResponse res = http.execute(post);
			int statusCode = res.getStatusLine().getStatusCode();
			Log.i("test", "statusCode:" + statusCode);
			if (statusCode == OK) {
				response = new MyResponse(EntityUtils.toString(res.getEntity(),
						"utf-8"));
			}
			Log.i("test", "response:" + response.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	/**
	 * 访问url地址以inputstream形式返回
	 * 
	 * @param urlStr
	 *            url地址
	 * @return 结果
	 */
	public InputStream getInputStreamFromUrl(String urlStr) {
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			HttpURLConnection urlConn = (HttpURLConnection) url
					.openConnection();
			inputStream = urlConn.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inputStream;
	}
}
