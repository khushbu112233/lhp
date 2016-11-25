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
import com.aip.targascan.vo.Checkout;

public class CheckoutAsync extends AsyncTask<Void, Void, String> {

	private Context mcontext;
	private ICallback callBack;
	private Checkout checkout;
	private Dialog dialog;
	
	
	public CheckoutAsync(Context context,Checkout checkout, ICallback callBack) {
		this.mcontext = context;
		this.callBack = callBack;
		this.checkout =checkout;
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
			
			
		
			params.put(JsonKey.AUTH_KEY, checkout.getMiles());
			params.put(JsonKey.AUTH_KEY, checkout.getVehicleName());
		params.put(JsonKey.AUTH_KEY, authKey);
			String response = reader.sendRequest(JsonKey.getCHECKOUT_URL(), params);
			Log.d("response of cur password is here :", "yes:" + response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if(dialog != null )
			dialog.dismiss();
		if (result != null) {
			callBack.run(result);
		}else
			L.alert(mcontext, "Server error!Please try again later.");
	}
}