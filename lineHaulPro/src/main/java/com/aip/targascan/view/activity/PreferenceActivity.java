package com.aip.targascan.view.activity;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.aip.targascan.R;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Login;
@ContentView(R.layout.activity_preferences)
public class PreferenceActivity extends RoboActivity {
	@InjectView(R.id.btnLogin)
	Button btnLogin;
	@InjectView(R.id.login_edtdriverid)
	EditText editDriverId;
	@InjectView(R.id.login_edtusername)
	EditText editUsername;
	@InjectView(R.id.login_edtpwd)
	EditText editPwd;
	@InjectView(R.id.login_tvautologin)
	TextView txtAutoLogin;
	@InjectView(R.id.login_tvrememberme)
	TextView txtRememberMe;
	
	
	private boolean isAutoLogin, isRememberMe;
	private PreferenceActivity activity;
	private DatabaseHandler database;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = this;
		database = DatabaseHandler.getInstance(activity);
		
		txtAutoLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isAutoLogin) {
					isAutoLogin = false;
					txtAutoLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.off_btn), null);
				} else {
					isAutoLogin = true;
					txtAutoLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.on_btn), null);
				}
			}
		});

		txtRememberMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRememberMe) {
					isRememberMe = false;
					txtRememberMe.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.off_btn), null);
				} else {
					isRememberMe = true;
					txtRememberMe.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.on_btn), null);
				}
			}
		});
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(isValidate()) 
				{
					updateLogin();
					L.ok(activity, "Preference updated successfully");
					activity.finish();
				}
			}
		});
		
		setDetail(database.getContact());
		
	}
	
	private void setDetail(Login login){
		if(login != null && login.getRememberme() == 1)
		{
			try {
				editDriverId.setText(login.getDriverID());
				editUsername.setText(login.getuserName());
				editPwd.setText(login.getPassword());

				isAutoLogin = login.getAutologin() == 1 ? true : false;
				isRememberMe = login.getRememberme() == 1 ? true : false;

				if (isAutoLogin) {
					txtAutoLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.on_btn), null);
				} else {
					txtAutoLogin.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.off_btn), null);
				}

				if (isRememberMe) {
					txtRememberMe.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.on_btn), null);
				} else {
					txtRememberMe.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.off_btn), null);
				}
			} catch (Exception e) {
			}
		}
	}
	
	private boolean isValidate(){
		if (Util.isEmpty(editDriverId) && Util.isEmpty(editUsername) && Util.isEmpty(editPwd)) {

			L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(), getResources()
					.getText(R.string.multiscan_targascan_errormsg_alldetail).toString());

			return false;
		} else if (Util.isEmpty(editDriverId)) {

			L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(), getResources()
					.getText(R.string.login_driverid).toString());

			return false;

		} else if (Util.isEmpty(editUsername)) {

			L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(), getResources()
					.getText(R.string.login_edtusername).toString());
			return false;
		} else if (Util.isEmpty(editPwd)) {

			L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(), getResources()
					.getText(R.string.login_password).toString());

			return false;
		} 

		return true;
	}
	
	private void updateLogin()
	{
		try {
			int cnt = database.getProfilesCount();
			Login login = getLogin();
			if (cnt <= 0) {
				database.addData(login);
				Log.d("Insert: ", "Inserting ..");
			}else{
				database.updateContact(login);
				Log.d("Update: ", "Updated successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Login mLogin;
	private Login getLogin(){
		String driverId = editDriverId.getText().toString().trim();
		String username = editUsername.getText().toString().trim();
		String password = editPwd.getText().toString().trim();

		if(mLogin == null)
			mLogin = new Login();

		mLogin.setDriverID(driverId);
		mLogin.setuserName(username);
		mLogin.setPassword(password);
		if (isAutoLogin) {
			mLogin.setAutologin(1);
		} else {
			mLogin.setAutologin(0);
		}
		if (isRememberMe) {
			mLogin.setRememberme(1);
		} else {
			mLogin.setRememberme(0);
		}

		return mLogin;
	}

}
