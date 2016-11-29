package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.WebserviceReader;

public class GetDriverRouteAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Dialog dialog;
	private DatabaseHandler databaseHandler;
	boolean isProgressBar;

	public GetDriverRouteAsync(Context context, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.isProgressBar = isProgressBar;
		databaseHandler = DatabaseHandler.getInstance(context);
	}

	@Override
	protected void onPreExecute() {
		try {
			if (isProgressBar)
				dialog = ProgressDialog.show(mcontext, "Getting Driver Route...", "Loading");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... param) {
		try {
			WebserviceReader reader = new WebserviceReader();
			Map<String, String> params = new HashMap<String, String>();
			params.put(JsonKey.KEY_DRIVER_ID, databaseHandler.getContact().getDriverID());
			Log.e("MainUrl", "" + JsonKey.getDriver_Route());
			String response = reader.sendRequest(JsonKey.getDriver_Route(), params);
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