package com.aip.targascan.view.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.aip.targascan.R;
import com.aip.targascan.common.async.LoginAsync;
import com.aip.targascan.common.async.MultiScanDataAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ApplicationLog;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.service.ServiceHandler;
import com.aip.targascan.vo.Login;
import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends Activity {

    // Splash screen timer
    private SplashActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.act_splash);
        activity = this;
        System.out.println("" + Util.getCurrentTime());

        ApplicationLog.writeToFile(activity, Constants.PKG_NAME, getResources().getString(R.string.app_name), Constants.DEBUG);

        Logger.debug(Constants.DEBUG, "Splash Activity Started::::::::::::::::::::::::::::::::::::::::");
        String selectedUrl = SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL, JsonKey.DUMMY_URL);
        Log.e("SpalshActivity", "Seleted " + selectedUrl);
        SharedPrefrenceUtil.setPrefrence(activity, Constants.PREF_SELECTED_URL, selectedUrl);

        JsonKey.setMAIN_URL(selectedUrl);

		/*if(Util.isNetAvailable(activity))
        {*/
        SharedPrefrenceUtil.setPrefrence(getApplicationContext(), Constants.PREF_IS_VALID_AUTH_KEY, true);
        ServiceHandler serviceHandler = ServiceHandler.getInstance(activity);
        serviceHandler.startDataLoaderService();
        //}

        try {
            DatabaseHandler databaseHandler = DatabaseHandler.getInstance(activity);
            Login login = databaseHandler.getContact();
            if (login != null && login.getAutologin() == 1) {
                if (Util.isNetAvailable(activity)) {
                    Logger.debug(Constants.DEBUG, "Splash OnCreate Internet available true");
                    doLogin(login);

                } else {
                    Logger.debug(Constants.DEBUG, "Splash OnCreate Internet not available: Go to Dashboard activity");
                    go(Constants.TIME_SPLASH, DashboardActivity.class);
                }
            } else {
                go(Constants.TIME_SPLASH, LoginActivity.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.debug(Constants.DEBUG, "Splash OnCreate Error 69 ::" + e.getMessage());
            go(Constants.TIME_SPLASH, LoginActivity.class);
        }


    }

    public void doTask() {
        try {
            MultiScanDataAsync async = new MultiScanDataAsync(activity, new ICallback() {

                @Override
                public void run(Object result) {
                    Logger.info("#Multi scan Data#", (String) result);
                }
            });
            async.execute();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            go(Constants.TIME_SPLASH_SMALL, LoginActivity.class);
        }
    }

    private void go(int time, final Class<?> clzz) {

        if (time == 0) {
            Intent i = new Intent(SplashActivity.this, clzz);
            startActivity(i);
            this.finish();
            return;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, clzz);
                startActivity(i);

                finish();
            }
        }, time);
    }

    private void doLogin(final Login login) {
        try {
            Logger.debug(Constants.DEBUG, "Splash doLogin() Internet available: doLogin called");
            LoginAsync async = new LoginAsync(SplashActivity.this, login, false, new ICallback() {

                @Override
                public void run(Object result) {
                    if (result == null) {
                        Logger.debug(Constants.DEBUG, "Splash doLogin() Login WS response == null ");
                        go(0, LoginActivity.class);
                        return;
                    }
                    Logger.debug(Constants.DEBUG, "Splash doLogin() Login WS response ::" + result.toString());
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
                                }
                            } catch (Exception e) {
                                go(0, LoginActivity.class);
                            }
                        }

                        Log.d("#SUCCESS#", success);
                        Log.d("#response#", response);
                        if (success.equalsIgnoreCase("true")) {
                            go(0, DashboardActivity.class);
                        } else {

                            go(0, LoginActivity.class);
                        }
                    } catch (JSONException e) {
                        Logger.debug(Constants.DEBUG, "Splash doLogin() JSON EXCPETION ::" + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
            async.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
