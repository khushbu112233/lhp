package com.aip.targascan.view.activity;

import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.aip.targascan.R;
import com.aip.targascan.common.async.ChangepasswordAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.L.IL;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Changepwd;

@ContentView(R.layout.activity_change_pwd)
public class ChangepwdActivity extends RoboActivity {

	ICallback icallback;
	@InjectView(R.id.edtchangepwd )           EditText currentpwd;
	@InjectView(R.id.edtnewpwd )              EditText newpassword;
	@InjectView(R.id.edtconfirmnewpwd )       EditText confirmnewpassword;
	@InjectView(R.id.btnUpdate)               Button btnupdate;
	
	private ChangepwdActivity  activity;
	private DatabaseHandler databaseHandler;
	//private String mCurrPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		databaseHandler = DatabaseHandler.getInstance(activity);
		//mCurrPwd = databaseHandler.getPassword();
		
		btnupdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.hideKeyboard(activity, confirmnewpassword);
				if(isValidate())
				{
					if(Util.isNetAvailable(activity))
					{
						doTask();
						
					}else{
						L.alert(activity, "Please check your internet connection");
					}
				}
			}
		});
	}

	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}

	private boolean isValidate(){
		
		if (isEmpty(currentpwd) && isEmpty(newpassword) && isEmpty(confirmnewpassword)) {
		
			L.alert(ChangepwdActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(), getResources()
					.getText(R.string.multiscan_targascan_errormsg_alldetail).toString());
			
			return false;
		} else if (isEmpty(currentpwd)) {

			L.alert(ChangepwdActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.login_curpassword).toString());

			return false;

		} else if (isEmpty(newpassword)) {

			L.alert(ChangepwdActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.login_newpassword).toString());

			return false;
		} else if (isEmpty(confirmnewpassword)) {

			L.alert(ChangepwdActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.login_newcfpassword).toString());

			return false;
		}else if (!newpassword.getText().toString().trim().equals(confirmnewpassword.getText().toString().trim())) {

			L.alert(ChangepwdActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.donotmatch_pwd).toString());

			return false;

		}
		return true;
	}
	
	private void doTask(){
		try {
			Changepwd changePwd = new Changepwd();
			
			changePwd.setCurrentPwd(currentpwd.getText().toString());
			changePwd.setNewpwd(newpassword.getText().toString());
			changePwd.setConfirmNewpwd(confirmnewpassword.getText().toString());

			Log.d("#Curret password#", currentpwd.getText().toString().trim());
			Log.d("#Change password#", newpassword.getText().toString().trim());
			Log.d("#Confirm password#", confirmnewpassword.getText().toString().trim());
			
			ChangepasswordAsync async = new ChangepasswordAsync(activity, changePwd, new ICallback() {

				@Override
				public void run(Object result) {
					if(result == null)
					{
						L.alert(activity, "Server error! Please try again later");
						return;
					}
					Log.d("#LOGIN RESPONSE#", result.toString());
					String str = result.toString();
					try {
						JSONObject obj = new JSONObject(str);

						String success = "";
						String response = "";

						if(obj.has("success"))
						{
							success = obj.getString("success");
						}

						if(obj.has("response"))
						{
							JSONObject responseJson = obj.getJSONObject("response");
							response = responseJson.toString();
							if(responseJson.has("message"))
							{
								response = responseJson.getString("message");
								Log.d("#Change password message#", response);
							}
						}

						Log.d("#SUCCESS#", success);
						Log.d("#response#", response);
						if (success.equalsIgnoreCase("true")) {
							//updatePassword();
							L.alert(activity, response, new IL() {
								
								@Override
								public void onSuccess() {
									onBackPressed();
								}
								
								@Override
								public void onCancel() {
									
								}
							});
						} else {
							Util.showError(activity, obj);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						L.alert(activity, "Server error! Server response is "+result);
					}
				}

			});
			async.execute();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	private void updatePassword(){
		String newPwd = newpassword.getText().toString().trim();
		databaseHandler.updatePassword(newPwd);
	}

}