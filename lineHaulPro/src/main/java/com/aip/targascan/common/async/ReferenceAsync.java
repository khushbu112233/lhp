package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.Map;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Login;
import com.aip.targascan.vo.Reference;

public class ReferenceAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Reference reference;
	private Dialog dialog;

	boolean isProgressBar;

	public ReferenceAsync(Context context, Reference reference, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.reference = reference;
		this.isProgressBar = isProgressBar;
	}

	@Override
	protected void onPreExecute() {
		try {
			if (isProgressBar)
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
			DatabaseHandler database = DatabaseHandler.getInstance(mcontext);
			Login login = database.getContact();
			String driverId = login.getDriverID();

			params.put(JsonKey.KEY_DRIVER_ID, login.getDriverID());
			params.put(JsonKey.AUTH_KEY, authKey);
			params.put("co_type", reference.get_co_type());
			params.put("reference_number", reference.get_reference_number());
			params.put("comments", reference.get_comments());

			String response = reader.sendRequest(JsonKey.getReference_URL(), params);
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