package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Changepwd;
import com.aip.targascan.vo.DriverRouteData;

public class DriverRouteSubmitAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private DriverRouteData routeData;
	private Dialog dialog;
	private String data;

	public DriverRouteSubmitAsync(Context context, DriverRouteData driverRouteData, String data, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.routeData = driverRouteData;
		this.data = data;
	}

	@Override
	protected void onPreExecute() {
		try {
			dialog = ProgressDialog.show(mcontext, "Please Wait", "Loading");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... param) {
		try {

			WebserviceReader reader = new WebserviceReader();
			Map<String, String> params = new HashMap<String, String>();

			String authKey = SharedPrefrenceUtil.getPrefrence(mcontext, Constants.PREF_AUTH_KEY, "");
			params.put(JsonKey.AUTH_KEY, authKey);

//			params.put("driver_route_id", routeData.getDriver_route_id());
//			params.put("driver_route_timestatus_id", routeData.getDriver_route_timestatus_id());
//			params.put("driver_id", routeData.getDriver_id());
//			params.put("timestamp", routeData.getTimestamp());
//			params.put("gps_lat", routeData.getGps_lat());
//			params.put("gps_log", routeData.getGps_log());
			params.put("data", data);

			String response = reader.sendRequest(JsonKey.getInsert_Driver_Route(), params);

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
		if (result != null) {
			callBack.run(result);
		} else
			L.alert(mcontext, "Server error!Please try again later.");
	}
}