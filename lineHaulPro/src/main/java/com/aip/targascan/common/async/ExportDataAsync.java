package com.aip.targascan.common.async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.ProgressHUD;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.Login;

public class ExportDataAsync extends AsyncTask<Void, Void, String> implements OnCancelListener {
	private Context mContext;
	private IResultListner iResultListner;
	private ProgressHUD mProgressHUD;

	public ExportDataAsync(Context context, IResultListner iResultListner) {
		this.mContext = context;
		this.iResultListner = iResultListner;
	}

	@Override
	protected void onPreExecute() {
		try {
			mProgressHUD = ProgressHUD.show(mContext, Constants.MSG_WAIT, true, false, ExportDataAsync.this);
			mProgressHUD.setCancelable(true);
			mProgressHUD.setCanceledOnTouchOutside(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String doInBackground(Void... param) {
		String message = null;
		try {
			DatabaseHandler database = DatabaseHandler.getInstance(mContext);
			Login login = database.getContact();
			String driverId = login.getDriverID();
			String authKey = SharedPrefrenceUtil.getPrefrence(mContext, Constants.PREF_AUTH_KEY, null);

			WebserviceReader reader = new WebserviceReader();
			List<Cachedjob> cachedjobs = database.getCachedjob();

			for (Cachedjob cachedjob : cachedjobs) {

				Map<String, String> params = new HashMap<String, String>();
				params.put(JsonKey.MUTISCAN.ass1, cachedjob.getChg1());
				params.put(JsonKey.MUTISCAN.ass2, cachedjob.getChg2());
				params.put(JsonKey.MUTISCAN.cached_date, cachedjob.getCachedDate());
				params.put(JsonKey.MUTISCAN.cn, cachedjob.getcartoon1());
				params.put(JsonKey.MUTISCAN.co_type, cachedjob.getCompanyID());
				params.put(JsonKey.MUTISCAN.cust_name, cachedjob.getSignatureName());
				params.put(JsonKey.MUTISCAN.DID, driverId);
				params.put(JsonKey.MUTISCAN.redeliver, cachedjob.getRedelivery());
				params.put(JsonKey.MUTISCAN.servID, cachedjob.getServId());

				if (cachedjob.getEncodeSign() == null) {
					params.put(JsonKey.MUTISCAN.signData, "");
				} else if (cachedjob.getEncodeSign().toString().length() > 0) {
					params.put(JsonKey.MUTISCAN.signData, cachedjob.getEncodeSign());
				} else {
					params.put(JsonKey.MUTISCAN.signData, "");
				}
				params.put(JsonKey.MUTISCAN.system, cachedjob.getSystem());
				params.put(JsonKey.MUTISCAN.to, cachedjob.getTo());
				params.put(JsonKey.MUTISCAN.from, cachedjob.getFrom());

				params.put(JsonKey.AUTH_KEY, authKey);

				Log.v("params", "params: " + params.toString());

				String response = reader.sendRequest(JsonKey.getURL_MULTI_SCAN_CO_TYPE(), params);
				JSONObject responseJson = null;
				try {
					Logger.info("#DASHBOARD:MULTISCAN#", response);
					JSONObject jsonObject = new JSONObject(response);
					if (jsonObject != null) {
						responseJson = jsonObject.getJSONObject("response");
						JSONObject dataJson = responseJson.getJSONObject("data");

						if (dataJson.has("message")) {
							message = dataJson.getString("message");
						}

						if (dataJson.has("goto")) {
							if (dataJson.getString("goto").equalsIgnoreCase("scan")) {
								database.deleteCachedJob(cachedjob.getID());
							}
						} else {
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message = "Unknown Error!! Please try again";
		} finally {
		}

		return message;

	}

	@Override
	protected void onPostExecute(String result) {
		if (mProgressHUD != null && mProgressHUD.isShowing())
			mProgressHUD.dismiss();
		if (result != null) {
			iResultListner.result(result, true);
			L.ok(mContext, result);
		}

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (mProgressHUD != null && mProgressHUD.isShowing())
			mProgressHUD.dismiss();
	}
}
