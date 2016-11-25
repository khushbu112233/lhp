package com.aip.targascan.service;

import android.content.Context;
import android.content.Intent;

public class ServiceHandler {
	
	private  Context activity;
	private static ServiceHandler mServiceHandler;
	private ServiceHandler(){}
	
	public static ServiceHandler getInstance(Context activity){
		if(mServiceHandler == null)
			mServiceHandler = new ServiceHandler();
		
		mServiceHandler.activity = activity;
		return mServiceHandler;
	}
	
	public void startDataLoaderService(){
		Intent intent = new Intent(activity, DataLoaderService.class);
		activity.startService(intent);
	}
}
