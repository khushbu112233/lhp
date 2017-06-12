package com.aip.targascan.common.util;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class AsyncHandler extends JsonHttpResponseHandler implements OnCancelListener{

	private Context mContext;
	private boolean showProgressHud;
	private ProgressHUD mProgressHUD;

	public AsyncHandler(Context context, boolean showProgressHud){
		this.mContext = context;
		this.showProgressHud = showProgressHud;
	}

	@Override
	public void onStart() {
		if(showProgressHud)
		{
			mProgressHUD = ProgressHUD.show(mContext, Constants.MSG_WAIT, true, false, AsyncHandler.this);
			mProgressHUD.setCancelable(true);
			mProgressHUD.setCanceledOnTouchOutside(false);
		}
	}

	@Override
	public void onFinish() {

		if(showProgressHud)
		{
			if(mProgressHUD != null && mProgressHUD.isShowing())
				mProgressHUD.dismiss();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if(mProgressHUD != null && mProgressHUD.isShowing())
			mProgressHUD.dismiss();

	}

	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
		if(responseString != null)
			L.alert(mContext, responseString);
		else
			L.alert(mContext, "Server taking too long time! Please try again");
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
		if(errorResponse != null)
			L.alert(mContext, errorResponse.toString());
		else
			L.alert(mContext, "Server taking too long time! Please try again");
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
		if(errorResponse != null)
			L.alert(mContext, errorResponse.toString());
		else
			L.alert(mContext, "Server taking too long time! Please try again");
	}


}
