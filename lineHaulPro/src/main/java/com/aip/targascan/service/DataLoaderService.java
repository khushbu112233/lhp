package com.aip.targascan.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Chg;
import com.aip.targascan.vo.Redeliver;

public class DataLoaderService extends IntentService {


	public DataLoaderService() {
		super("DataLoaderService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			if(Util.isNetAvailable(getApplicationContext()))
			{
				String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
				if(authKey != null)
				{
					WebserviceReader reader = new WebserviceReader();
					Map<String, String> params = new HashMap<String, String>();

					params.put(JsonKey.AUTH_KEY, authKey);
					String response = reader.sendRequest(JsonKey.getURL_MULTISCAN_DATA_LOADER(), params);
					doDownloadingTask(response);
				}
			}else{
				doOfflineTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doOfflineTask(){
		try {
			DatabaseHandler database = DatabaseHandler.getInstance(getApplicationContext());
			int size = database.getAllChg().size();
			if(size < 1)
			{
				doDownloadingTask(Constants.STATIC_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doDownloadingTask(String response){
		try {
				DatabaseHandler database = DatabaseHandler.getInstance(getApplicationContext());
				try {
					JSONObject obj = new JSONObject(response);
					if(obj.has("response"))
					{
						SharedPrefrenceUtil.setPrefrence(getApplicationContext(), Constants.PREF_IS_VALID_AUTH_KEY, true);
						JSONObject responseJson = obj.getJSONObject("response");
						response = responseJson.toString();
						if(responseJson.has("message"))
						{
							String message  = responseJson.getString("message");
							Log.d("#DataLoaderService#", message);
						}
						if(responseJson.has("co_type"))
						{
							JSONArray cast = responseJson.getJSONArray("co_type");
							String name = "";
							database.deleteCompany();
							for (int i = 0; i < cast.length(); i++) {
								JSONObject actor = cast.getJSONObject(i);
								name = actor.getString( "name");
								//Log.d("name:", name);
								database.addDataCompany(name);
							}
						}
						if(responseJson.has("chg"))
						{
							JSONArray cast1 = responseJson.getJSONArray("chg");
							String text = "";
							String data = "";
							database.deleteChg();
							for (int i = 0; i < cast1.length(); i++) {
								JSONObject actor = cast1.getJSONObject(i);
								text = actor.getString( "text");
								data = actor.getString("data");
								//Log.d("text:", text);
								//Log.d("data:", data);

								Chg chg = new Chg();
								chg.setchgData(((data)));
								chg.setchgText(text);
								database.addDataChg(chg);

							}
						}
						if(responseJson.has("redeliver"))
						{
							JSONArray cast2 = responseJson.getJSONArray("redeliver");
							String txt = "";
							database.deleteRedeliver();
							for (int i = 0; i < cast2.length(); i++) {
								JSONObject actor = cast2.getJSONObject(i);
								txt = actor.getString( "text");
								String data = actor.getString( "data");
								try {
									Redeliver redeliver = new Redeliver();
									try {
										redeliver.setredeliverData(data);
									} catch (Exception e) {
									}
									redeliver.setredeliverText(txt);
									database.addDataRedeliver((redeliver));
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}
					}
				} catch (JSONException e) {
					SharedPrefrenceUtil.setPrefrence(getApplicationContext(), Constants.PREF_IS_VALID_AUTH_KEY, false);
					e.printStackTrace();
					Log.e("#DataLoaderService#", "Error >> "+response);
				}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


} 
