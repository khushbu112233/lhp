package com.aip.targascan.common.async;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.WebserviceReader;

import java.util.HashMap;
import java.util.Map;

public class GetManagerPasswordAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private String carton_num;
	String driverId;
	private Dialog dialog;

	public GetManagerPasswordAsync(Context context,String driverId, String carton_num, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.carton_num = carton_num;
		this.driverId = driverId;
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

			// Constants.DEVICE_TYPE_ANDROID);
			params.put(JsonKey.KEY_MASTER_PASSWORD,carton_num);
			params.put(JsonKey.KEY_DRIVER_ID,driverId);
			params.put(JsonKey.AUTH_KEY, authKey);

			String response = reader.sendRequest(JsonKey.getCHECK_MANAGER_PASSWORD_URL(), params);
			//Log.e("detail","response"+response);
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