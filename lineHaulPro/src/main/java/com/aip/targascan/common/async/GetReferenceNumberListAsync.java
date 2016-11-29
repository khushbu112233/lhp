package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

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

public class GetReferenceNumberListAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Dialog dialog;
	private DatabaseHandler databaseHandler;
	boolean isProgressBar;

	public GetReferenceNumberListAsync(Context context, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.isProgressBar = isProgressBar;
		databaseHandler = DatabaseHandler.getInstance(context);
	}

	@Override
	protected void onPreExecute() {
		try {
			if (isProgressBar)
				dialog = ProgressDialog.show(mcontext, "Getting Reference No...", "Loading");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... param) {
		try {

			String authKey = SharedPrefrenceUtil.getPrefrence(mcontext, Constants.PREF_AUTH_KEY, "");

			WebserviceReader reader = new WebserviceReader();
			Map<String, String> params = new HashMap<String, String>();
			params.put(JsonKey.KEY_DRIVER_ID, databaseHandler.getContact().getDriverID());
			params.put(JsonKey.KEY_AUTHKEY, authKey);
			Log.e("MainUrl", "" + JsonKey.getReference_LIST());
			String response = reader.sendRequest(JsonKey.getReference_LIST(), params);
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
}