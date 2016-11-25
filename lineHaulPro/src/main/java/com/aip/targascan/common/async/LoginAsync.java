package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Login;

public class LoginAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Login login;
	private Dialog dialog;

	boolean isProgressBar;


	public LoginAsync(Context context, Login login, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.login = login;
		this.isProgressBar = isProgressBar;
	}

	@Override
	protected void onPreExecute() {
		try {
			if(isProgressBar)
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
			Log.d("driverid is here :", "yes:" + login.getDriverID());
			Log.d("Password is here :", "yes:" + login.getPassword());
			Log.d("username is here :", "yes:" + login.getuserName());
			Log.d("autologin is here :", "yes:" + login.getAutologin());
			Log.d("rememberme is here :", "yes:" + login.getRememberme());
			// params.put(JsonKey.KEY_DEVICE_TYPE,
			// Constants.DEVICE_TYPE_ANDROID);
			params.put(JsonKey.KEY_DRIVER_ID, login.getDriverID());

			params.put(JsonKey.KEY_USERNAME, login.getuserName());
			params.put(JsonKey.KEY_PASSWORD, login.getPassword());
			params.put(JsonKey.KEY_AUTOLOGIN, String.valueOf(login.getAutologin()));
			params.put(JsonKey.KEY_REMEMBERME, String.valueOf(login.getRememberme()));

			Log.e("MainUrl", "" + JsonKey.getLOGIN_URL());
			String response = reader.sendRequest(JsonKey.getLOGIN_URL(), params);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if(dialog != null)
			dialog.dismiss();
		callBack.run(result);
	}
}