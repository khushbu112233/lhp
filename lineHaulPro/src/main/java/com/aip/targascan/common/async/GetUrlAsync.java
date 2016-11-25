package com.aip.targascan.common.async;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.WebserviceReader;

public class GetUrlAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Dialog dialog;

	boolean isProgressBar;


	public GetUrlAsync(Context context, boolean isProgressBar, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.isProgressBar = isProgressBar;
	}

	@Override
	protected void onPreExecute() {
		try {
			if(isProgressBar)
				dialog = ProgressDialog.show(mcontext, "Getting Urls...", "Loading");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(Void... param) {
		try {
			WebserviceReader reader = new WebserviceReader();
//			Map<String, String> params = new HashMap<String, String>();

			String response = reader.sendRequest(JsonKey.URL_GET_DOMAINS);
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