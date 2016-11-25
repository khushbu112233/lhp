package com.aip.targascan.common.util;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RestClient {

	//private static final String BASE_URL = "http://pitsupport.handyortungen-kostenlos.de/api/";

	private static AsyncHttpClient client = new AsyncHttpClient();
	//Initialise block
	{
		//client.setMaxRetriesAndTimeout(5, 120000);
		//client.setResponseTimeout(120000);
		client.setTimeout(120000);
	}
	
	public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		//client.setBasicAuth("client", "password");
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		//client.setBasicAuth("client", "password");
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
	//	client.setResponseTimeout(120000);
		client.setTimeout(120000);
		Log.d("TIME OUT SET", "120000");
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return relativeUrl;
	}
	
}