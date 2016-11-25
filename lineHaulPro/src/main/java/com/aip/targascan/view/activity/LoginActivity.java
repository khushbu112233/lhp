package com.aip.targascan.view.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aip.targascan.R;
import com.aip.targascan.common.async.GetUrlAsync;
import com.aip.targascan.common.async.LoginAsync;
import com.aip.targascan.common.async.MultiScanDataAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ApplicationLog;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.L.IL;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.common.util.Util.IOnUrlSelcted;
import com.aip.targascan.entity.WebUrlBean;
import com.aip.targascan.vo.Login;
import com.google.gson.Gson;

@ContentView(R.layout.activity_login)
public class LoginActivity extends RoboActivity {

	protected static final String TABLE_LOGIN = "login";
	@InjectView(R.id.btnLogin)
	Button btnLogin;
	@InjectView(R.id.login_edtdriverid)
	EditText editDriverId;
	@InjectView(R.id.login_edtpwd)
	EditText editPwd;
	@InjectView(R.id.login_tvautologin)
	TextView txtAutoLogin;
	@InjectView(R.id.login_tvrememberme)
	TextView txtRememberMe;
	@InjectView(R.id.imgAppWeb)
	ImageView imgAppWeb;
	@InjectView(R.id.login_spinUrls)
	TextView spinUrls;

	private boolean isAutoLogin, isRememberMe;
	final DatabaseHandler database = DatabaseHandler.getInstance(this);
	private LoginActivity activity;

	private List<String> urlsNameList;

	private String selectedUrlName;

	List<WebUrlBean> webUrlBeans;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;

		ApplicationLog.writeToFile(activity, Constants.PKG_NAME, getResources().getString(R.string.app_name), Constants.DEBUG);

		Logger.debug(Constants.DEBUG, "Login Activity Started::::::::::::::::::::::::::::::::::::::::");
		// urlsNameList = Constants.WEB_URLS;

		// selectedUrlName = SharedPrefrenceUtil.getPrefrence(activity,
		// Constants.PREF_SELECTED_URL_NAME, JsonKey.DUMMY_URL);

		// JsonKey.setMAIN_URL(selectedUrlName);

		// spinUrls.setText(selectedUrlName);

		fillUrls();

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

				if (isValidate()) {
					if (Util.isNetAvailable(activity)) {
						doLogin();

					} else {
						// L.alert(activity,
						// activity.getResources().getString(R.string.error_internet));

						L.confirmDialog(activity, Constants.APP_NAME, activity.getResources().getString(R.string.offline_login), new IL() {

							@Override
							public void onSuccess() {
								updateLogin();
								database.deleteContact();
								goNext();
							}

							@Override
							public void onCancel() {

							}
						});
					}
				}
			}
		});

		imgAppWeb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.openUrl(LoginActivity.this, Constants.WEB_URL);
			}
		});

		spinUrls.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (urlsNameList != null && urlsNameList.size() > 0)
					Util.openUrlOption(activity, urlsNameList, onUrlSelcted);
				else
					L.alert(activity, "You don't have an internet connection. Please connect internet and try again.");
			}
		});

		setDetail(database.getContact());
	}

	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}

	private void updateLogin() {
		try {
			int cnt = database.getProfilesCount();
			Login login = getLogin();
			if (cnt <= 0) {
				database.addData(login);
				Log.d("Insert: ", "Inserting ..");
			} else {
				database.updateContact(login);
				Log.d("Update: ", "Updated successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Login mLogin;

	private Login getLogin() {
		String driverId = editDriverId.getText().toString().trim();
		String username = driverId;
		String password = editPwd.getText().toString().trim();

		if (mLogin == null)
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

	private void setDetail(Login login) {
		if (login != null && login.getRememberme() == 1) {
			try {
				editDriverId.setText(login.getDriverID());
				// editUsername.setText(login.getuserName());
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

	private boolean isValidate() {
		if (isEmpty(editDriverId)) {

			L.alert(LoginActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.login_driverid).toString());

			return false;

		}/*
		 * else if (isEmpty(editUsername)) {
		 * 
		 * L.alert(LoginActivity.this,
		 * getResources().getText(R.string.multiscan_targascan_title
		 * ).toString(), getResources()
		 * .getText(R.string.login_edtusername).toString()); return false; }
		 */else if (isEmpty(editPwd)) {

			L.alert(LoginActivity.this, getResources().getText(R.string.multiscan_targascan_title).toString(),
					getResources().getText(R.string.login_password).toString());

			return false;
		}

		return true;
	}

	private void doLogin() {
		try {
			LoginAsync async = new LoginAsync(LoginActivity.this, getLogin(), true, new ICallback() {

				@Override
				public void run(Object result) {
					if (result == null) {
						L.alert(LoginActivity.this, activity.getResources().getString(R.string.error_server));
						return;
					}
					Log.d("#LOGIN RESPONSE#", result.toString());
					String str = result.toString();
					try {
						JSONObject obj = new JSONObject(str);

						String success = "";
						String response = "";

						if (obj.has("success")) {
							success = obj.getString("success");
						}

						if (obj.has("response")) {
							try {
								JSONObject responseJson = obj.getJSONObject("response");
								response = responseJson.toString();
								if (responseJson.has("authKey")) {
									String authKey = responseJson.getString("authKey");
									Log.d("#authKey#", authKey);
									SharedPrefrenceUtil.setPrefrence(getApplicationContext(), Constants.PREF_IS_VALID_AUTH_KEY, true);
									SharedPrefrenceUtil.setPrefrence(activity, Constants.PREF_AUTH_KEY, authKey);
									SharedPrefrenceUtil.setPrefrence(getApplicationContext(), Constants.PREF_MASTER_COMPANY_KEY,
											responseJson.optString("master_company"));
									Log.d("URl", "URL: " + spinUrls.getText());
								}
							} catch (Exception e) {

							}
						}

						Log.d("#SUCCESS#", success);
						Log.d("#response#", response);
						if (success.equalsIgnoreCase("true")) {
							updateLogin();
							goNext();
						} else {

							Util.showError(activity, obj);
						}
					} catch (JSONException e) {
						e.printStackTrace();
						// L.alert(LoginActivity.this,
						// "Server error! Server response is "+result);
						L.alert(LoginActivity.this, "Invalid login detail");
					}
				}
			});
			async.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void goNext() {
		try {
			Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
			startActivity(i);
			LoginActivity.this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fillUrls() {

		if (Util.isNetAvailable(activity)) {
			Logger.debug(Constants.DEBUG, "LoginActivity :: fillUrl() :: Internet available true");
			GetUrlAsync getUrlAsync = new GetUrlAsync(activity, true, new ICallback() {

				@Override
				public void run(Object result) {
					try {
						if (result != null) {
							parseUrls((String) result);
						} else
							parseUrls(null);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			getUrlAsync.execute();
		} else {
			Logger.debug(Constants.DEBUG, "LoginActivity :: fillUrl() :: Internet available false :: parseUrls(null)");
			parseUrls(null);
		}
	}

	private void parseUrls(String response) {
		try {
			Logger.debug(Constants.DEBUG, "LoginActivity :: parseUrls() :: parseUrls input=" + response);
			if (response == null) {
				return;
				// response = SharedPrefrenceUtil.getPrefrence(activity,
				// Constants.PREF_STATIC_WEB_URL_DATA,
				// Constants.STATIC_URL_DATA);
			}

			JSONArray jsonArray = new JSONArray(response);

			Gson gson = new Gson();

			webUrlBeans = new ArrayList<WebUrlBean>();

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.optJSONObject(i);

				WebUrlBean webUrlBean = gson.fromJson(jsonObject.toString(), WebUrlBean.class);

				webUrlBeans.add(webUrlBean);
			}

			if (webUrlBeans.size() > 0) {
				urlsNameList = new ArrayList<String>();

				for (WebUrlBean webUrlBean : webUrlBeans) {
					selectedUrlName = webUrlBean.getName();

					// Log.e("#Web Url#", ">>"+selectedUrlName);

					urlsNameList.add(selectedUrlName);
				}

				if (urlsNameList != null && urlsNameList.size() > 0) {
					SharedPrefrenceUtil.setPrefrence(activity, Constants.PREF_STATIC_WEB_URL_DATA, response);
					setUrl(SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, urlsNameList.get(0)));
				}
			} else {

				/*
				 * String staticResponse =
				 * SharedPrefrenceUtil.getPrefrence(activity,
				 * Constants.PREF_STATIC_WEB_URL_DATA,
				 * Constants.STATIC_URL_DATA);
				 * if(!response.equalsIgnoreCase(staticResponse)) {
				 * parseUrls(staticResponse); }
				 */
			}

		} catch (Exception e) {
			e.printStackTrace();

			Logger.debug(Constants.DEBUG, "LoginActivity :: parseUrls() :: parseUrls error=" + e.getMessage());

			/*
			 * String staticResponse =
			 * SharedPrefrenceUtil.getPrefrence(activity,
			 * Constants.PREF_STATIC_WEB_URL_DATA, Constants.STATIC_URL_DATA);
			 * if(!response.equalsIgnoreCase(staticResponse)) {
			 * parseUrls(staticResponse); }
			 */
		}
	}

	IOnUrlSelcted onUrlSelcted = new IOnUrlSelcted() {

		@Override
		public void onUrlSelected(String webUrlName) {
			setUrl(webUrlName);
		}
	};

	private String getUrl(String urlName) {
		for (WebUrlBean webUrlBean : webUrlBeans) {
			String name = webUrlBean.getName();

			if (name.equalsIgnoreCase(urlName)) {
				return webUrlBean.getUrl();
			}
		}

		return JsonKey.DUMMY_URL;

	}

	private void setUrl(String webUrlName) {
		try {
			selectedUrlName = webUrlName.trim();

			// https://www.
			SharedPrefrenceUtil.setPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, webUrlName);			
			spinUrls.setText(webUrlName);

			String url = getUrl(webUrlName);

			Logger.debug(Constants.DEBUG, "LoginActivity :: setUrl() :: setUrl=" + url + " :: name=" + webUrlName);
			Log.e("#Web Url#", "Selected web url name=" + webUrlName + " && url is =" + url);
			JsonKey.setMAIN_URL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (urlsNameList == null || urlsNameList.size() < 1)
			fillUrls();
	}
}