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

public class ChangepasswordAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Changepwd changepwd;
	private Dialog dialog;

	public ChangepasswordAsync(Context context, Changepwd changepwd, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.changepwd = changepwd;
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
			Log.d("current password is here :", "yes:" + changepwd.getCurrentPwd());
			Log.d("new password is here :", "yes:" + changepwd.getNewpwd());
			Log.d("confirm new password is here :", "yes:" + changepwd.getConfirmNewpwd());

			String authKey = SharedPrefrenceUtil.getPrefrence(mcontext, Constants.PREF_AUTH_KEY, "");

			// Constants.DEVICE_TYPE_ANDROID);
			params.put(JsonKey.KEY_CURRENTPASSWORD, changepwd.getCurrentPwd());
			params.put(JsonKey.KEY_NEWPASSWORD, changepwd.getNewpwd());
			params.put(JsonKey.KEY_CONFIRMNEWPASSWORD, changepwd.getConfirmNewpwd());
			params.put(JsonKey.AUTH_KEY, authKey);

			String response = reader.sendRequest(JsonKey.getCHANGEPWD_URL(), params);
			Log.d("response of cur password is here :", "yes:" + response);
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