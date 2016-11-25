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
import com.aip.targascan.vo.DailyOrder;
import com.aip.targascan.vo.Redeliver;

public class GetDailyOrdersListAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Dialog dialog;
	private DatabaseHandler databaseHandler;
	boolean isProgressBar;

	public GetDailyOrdersListAsync(Context context, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.isProgressBar = isProgressBar;
		databaseHandler = DatabaseHandler.getInstance(context);
	}

	@Override
	protected void onPreExecute() {
		try {
			if (isProgressBar)
				dialog = ProgressDialog.show(mcontext, "Getting Daily Orders List...", "Loading");
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
			params.put(JsonKey.KEY_DRIVER_ID, databaseHandler.getContact().getDriverID());
			String response = reader.sendRequest(JsonKey.getURL_GET_DAILY_ORDERS(), params);
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

	private String setUpData(String response) {
		String message = null;
		DatabaseHandler database = DatabaseHandler.getInstance(mcontext);
		try {
			JSONObject obj = new JSONObject(response);
			if (obj.has("data")) {
				JSONArray array = obj.getJSONArray("data");
				database.deleteDailyOrder();
				for (int counter = 0; counter < array.length(); counter++) {
					DailyOrder dailyOrder = new DailyOrder();
					dailyOrder.setCartonNumText(array.getJSONObject(counter).getString("carton_num"));
					if (!array.getJSONObject(counter).getString("co_type").equals("null")) {
						dailyOrder.setCoTypeText(array.getJSONObject(counter).getString("co_type"));
					} else {
						dailyOrder.setCoTypeText("Unknown");
					}
					database.addDataDailyOrder(dailyOrder);					
				}
			}
			message = "Success";
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("#DataLoaderService#", "Error >> " + response);
			message = "Error";
		}

		return message;
	}

}