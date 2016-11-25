package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Chg;
import com.aip.targascan.vo.Redeliver;

public class GetCompanyListAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Dialog dialog;
	private DatabaseHandler databaseHandler;
	boolean isProgressBar;

	public GetCompanyListAsync(Context context, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.isProgressBar = isProgressBar;
		databaseHandler = DatabaseHandler.getInstance(context);
	}

	@Override
	protected void onPreExecute() {
		try {
			if (isProgressBar)
				dialog = ProgressDialog.show(mcontext, "Getting Company List...", "Loading");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... param) {
		try {
			String message = null;
			WebserviceReader reader = new WebserviceReader();
			Map<String, String> params = new HashMap<String, String>();
			String authKey = SharedPrefrenceUtil.getPrefrence(mcontext, Constants.PREF_AUTH_KEY, "");
			params.put(JsonKey.AUTH_KEY, authKey);
			String response = reader.sendRequest(JsonKey.getURL_MULTISCAN_DATA_LOADER(), params);
			message = setUpData(response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (dialog != null)
			dialog.dismiss();
		callBack.run(result);
	}
	
	private String setUpData(String response){
		String message = null;
		DatabaseHandler database = DatabaseHandler.getInstance(mcontext);
		try {
			JSONObject obj = new JSONObject(response);
			if(obj.has("response"))
			{
				JSONObject responseJson = obj.getJSONObject("response");
				response = responseJson.toString();
				if(responseJson.has("message"))
				{
					message  = responseJson.getString("message");
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
						Log.d("co_type:", name);
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
						Log.d("chg:", text);
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
							Log.d("redeliver:", txt);
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
			e.printStackTrace();
			Log.e("#DataLoaderService#", "Error >> "+response);
		}

		return message;
	}
	
}