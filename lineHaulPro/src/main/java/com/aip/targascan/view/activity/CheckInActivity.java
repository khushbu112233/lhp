package com.aip.targascan.view.activity;



import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
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
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Login;
import com.loopj.android.http.RequestParams;
@ContentView(R.layout.activity_multiscan)
public class CheckInActivity extends RoboActivity{
	@InjectView(R.id.edtstartmilescheckin) 			EditText editstartmiles;
	@InjectView(R.id.edtvehiclenamecheckin) 			EditText editvehiclename;
	@InjectView(R.id.btnsavedetails) 			Button btnsavedetails;
	private CheckInActivity  activity;
	private DatabaseHandler databaseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = this;
		databaseHandler = DatabaseHandler.getInstance(activity);
		btnsavedetails.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isValidate())
				{
					goCheckIn();
				}
			}	
		});
	}
	
	private boolean isValidate(){
		if (Util.isEmpty(editstartmiles) && Util.isEmpty(editvehiclename)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_required_field).toString());
			return false;
			
		} else if (Util.isEmpty(editvehiclename)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_vehiclename).toString());
			return false;

		} else if (Util.isEmpty(editstartmiles)) {

			L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.validate_startmile).toString());
			return false;
		} 

		return true;
	}
	
	private void goCheckIn(){
		if(Util.isNetAvailable(activity))
		{

			String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
			Login login = databaseHandler.getContact();
			if(authKey != null && login != null && login.getDriverID() != null)
			{
				RequestParams params = new RequestParams();
				params.add(JsonKey.AUTH_KEY, authKey);
				params.add(JsonKey.KEY_DRIVER_ID, login.getDriverID());
				params.add(JsonKey.CheckIn.vehicleName, editvehiclename.getText().toString().trim());
				params.add(JsonKey.CheckIn.startMiles, editstartmiles.getText().toString().trim());
				params.add(JsonKey.CheckIn.checkIn, "N");
				
				callCheckIn(params);
				
			}else
				goInnerJob();
		}else{
			goInnerJob();
		}

	}
	
	private void callCheckIn(RequestParams params){
		RestClient.post(JsonKey.getURL_CHECK_IN(), params, new AsyncHandler(activity, true) {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					Logger.info("#CHECKIN:CHECKIN#", response.toString());
					JSONObject responseJson = response.getJSONObject("response");
					JSONObject dataJson = responseJson.getJSONObject("data");
					
					if(dataJson.has("goto"))
					{
						String go = dataJson.getString("goto");
						if(go != null && go.equalsIgnoreCase("scan"))
						{
							goInnerJob();
							
						}else{
							activity.finish();
						}
					}else if(dataJson.has("showForm")){
						goInnerJob();
					}
				} catch (Exception e) {
					e.printStackTrace();
					goInnerJob();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				L.alert(activity, responseString);
			}
		});
	}
	
	private void goInnerJob(){
		Intent i = new Intent(activity, MultiScanInner.class); 
		startActivity(i);
		activity.finish();
	}
}
