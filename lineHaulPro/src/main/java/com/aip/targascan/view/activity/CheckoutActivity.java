package com.aip.targascan.view.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.aip.targascan.R;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.L.IL;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Checkout;
import com.aip.targascan.vo.Login;
import com.loopj.android.http.RequestParams;

public class CheckoutActivity extends Activity{
	private CheckoutActivity  activity;
	Checkout checkout = new Checkout();
	DatabaseHandler databaseHandler;
	EditText edtvehiclename;
	EditText edtmiles;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		edtvehiclename=(EditText)findViewById(R.id.edtvehiclenamecheckout);
		edtmiles=(EditText)findViewById(R.id.edtendmilescheckout);
		activity = this;
		databaseHandler = DatabaseHandler.getInstance(activity);

		Button btnSave = (Button)findViewById(R.id.btnUpdate);
		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(isValidate())
				{
					checkCheckOut();
				}
			}
		});
	} 

	private boolean isValidate(){
		if (Util.isEmpty(edtvehiclename) && Util.isEmpty(edtmiles)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_required_field).toString());
			return false;

		} else if (Util.isEmpty(edtvehiclename)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_vehiclename).toString());
			return false;

		} else if (Util.isEmpty(edtmiles)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_endmile).toString());
			return false;
		} 

		return true;
	}

	private void checkCheckOut(){
		if(Util.isNetAvailable(activity))
		{

			String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
			Login login = databaseHandler.getContact();
			if(authKey != null && login != null && login.getDriverID() != null)
			{
				RequestParams params = new RequestParams();
				params.add(JsonKey.AUTH_KEY, authKey);
				params.add(JsonKey.KEY_DRIVER_ID, login.getDriverID());
				params.add(JsonKey.CheckOut.vehicleName, edtvehiclename.getText().toString().trim());
				params.add(JsonKey.CheckOut.endMiles, edtmiles.getText().toString().trim());
				params.add(JsonKey.CheckOut.checkOut, "N");

				callCheckOut(params);

			}else{
				L.confirmDialog(activity, Constants.APP_NAME, activity.getResources().getString(R.string.force_login), "Login", "Cancel", new IL() {

					@Override
					public void onSuccess() {
						Intent i = new Intent(activity, LoginActivity.class); 
						startActivity(i);
						activity.finish();
					}

					@Override
					public void onCancel() {

					}
				});
			}
		}else{
			L.alert(activity, activity.getResources().getString(R.string.offline_checkout));
		}
	}

	private void goToMain(){
		activity.finish();
	}

	private void callCheckOut(RequestParams params){
		RestClient.post(JsonKey.getURL_CHECK_OUT(), params, new AsyncHandler(activity, true) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					Logger.info("#DASHBOARD:CHECKOUT#", response.toString());
					JSONObject responseJson = response.getJSONObject("response");
					JSONObject dataJson = responseJson.getJSONObject("data");

					if(dataJson.has("goto") && dataJson.getString("goto").equalsIgnoreCase("main")){
						L.ok(activity, "You have checked out successfully");
						goToMain();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				if(responseString != null && !TextUtils.isEmpty(responseString))
					L.alert(activity, responseString);
				else
					L.alert(activity, "Failed to checkout");
			}
		});
	}



}