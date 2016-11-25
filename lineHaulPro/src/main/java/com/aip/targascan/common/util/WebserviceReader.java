package com.aip.targascan.common.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class WebserviceReader {

	StringBuilder builder;
	String resObj = "";

	public String sendRequest(String url) {
		try {

			HttpGet getRequest = new HttpGet(url);
			
			//HttpClient httpClient = new DefaultHttpClient();
			
//			Added for to change the execution time 
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
			HttpConnectionParams.setSoTimeout(httpParameters, 100000);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			


			HttpResponse response = httpClient.execute(getRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			resObj = s.toString();
		} catch (Exception e) {
			Log.d("RESPONSE ERROR", e.toString());
		}
		return resObj;
	}

	public String sendRequest(String url, Map<String, String> map) {
		try {
			//HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(url);
			
//			Added for to change the execution time 
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
			HttpConnectionParams.setSoTimeout(httpParameters, 100000);
			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			
			
			
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			for (Map.Entry<String, String> entry : map.entrySet()) {
				reqEntity.addPart(entry.getKey(), new StringBody(entry.getValue()));
			}
			postRequest.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();
			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			resObj = s.toString();
		} catch (Exception e) {
			Log.d("RESPONSE ERROR", e.toString());
		}
		return resObj;
	}
}
