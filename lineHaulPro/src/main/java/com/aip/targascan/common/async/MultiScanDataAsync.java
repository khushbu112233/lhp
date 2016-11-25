package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.ProgressHUD;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Chg;
import com.aip.targascan.vo.Redeliver;

public class MultiScanDataAsync extends AsyncTask<Void, Void, String> implements OnCancelListener {

	private Context mContext;
	private ICallback callBack;
	private ProgressHUD mProgressHUD;


	public MultiScanDataAsync(Context context, ICallback callBack) {
		this.mContext = context;
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {
		try {
			mProgressHUD = ProgressHUD.show(mContext, Constants.MSG_WAIT, true, false, MultiScanDataAsync.this);
			mProgressHUD.setCancelable(true);
			mProgressHUD.setCanceledOnTouchOutside(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	@Override
	protected String doInBackground(Void... param) {
		String message = null;
		try {

			if(Util.isNetAvailable(mContext))
			{
				WebserviceReader reader = new WebserviceReader();
				Map<String, String> params = new HashMap<String, String>();
				String authKey = SharedPrefrenceUtil.getPrefrence(mContext, Constants.PREF_AUTH_KEY, "");
				params.put(JsonKey.AUTH_KEY, authKey);
				String response = reader.sendRequest(JsonKey.getURL_MULTISCAN_DATA_LOADER(), params);
				message = setUpData(response);
			}else{
				message = doOfflineTask();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return message;
	}
	
	@Override
	protected void onPostExecute(String result) {
		if(mProgressHUD != null && mProgressHUD.isShowing())
			mProgressHUD.dismiss();
		if (result != null) {
			callBack.run(result);
		}else{
			//L.alert(mContext, mContext.getResources().getString(R.string.error_server));
			callBack.run(result);
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(mProgressHUD != null && mProgressHUD.isShowing())
			mProgressHUD.dismiss();

	}

	private String doOfflineTask(){
		try {
			DatabaseHandler database = DatabaseHandler.getInstance(mContext);
			int size = database.getAllChg().size();
			if(size < 1)
			{
				return setUpData(Constants.STATIC_DATA);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String setUpData(String response){
		String message = null;
		DatabaseHandler database = DatabaseHandler.getInstance(mContext);
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