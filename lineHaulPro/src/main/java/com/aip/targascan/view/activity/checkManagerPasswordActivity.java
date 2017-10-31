package com.aip.targascan.view.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.aip.targascan.R;
import com.aip.targascan.common.async.GetManagerPasswordAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.Pref;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.databinding.ActivityCheckPreviousDayOrderBinding;
import com.aip.targascan.vo.CheckOutDataVO;
import com.aip.targascan.vo.Login;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import roboguice.activity.RoboActivity;

/**
 * Created by aipxperts-ubuntu-01 on 26/10/17.
 */

public class checkManagerPasswordActivity extends RoboActivity {

    ActivityCheckPreviousDayOrderBinding binding;
    private checkManagerPasswordActivity activity;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_check_previous_day_order);
        activity = this;
        databaseHandler = DatabaseHandler.getInstance(activity);

        Intent intent= getIntent();
        CheckOutDataVO dataVO = null;
        if(intent != null)
        {
            try {
                dataVO = (CheckOutDataVO) intent.getSerializableExtra("data");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        binding.txtCount.setText(dataVO.getScrollToBottom().size()+"");
        binding.txtCount.setPaintFlags(binding.txtCount.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(dataVO.getScrollToBottom().size()==1)
        {
            binding.txtMessage.setText("Pending Carton");
            binding.txtMessage1.setText("Request Manager to enter passcode and bypass pending carton.");
        }else
        {
            binding.txtMessage.setText("Pending Cartons");
            binding.txtMessage1.setText("Request Manager to enter passcode and bypass pending cartons.");
        }

        final CheckOutDataVO finalDataVO = dataVO;
        binding.txtCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(activity, CheckOutDetailActivity.class);
                    i.putExtra("title", "Check Out");
                    i.putExtra("data", finalDataVO);
                    activity.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /*SpannableString clickedString
                = new SpannableString("You have "+dataVO.getScrollToBottom().size()+" carton  pending from yesterday.");

        if(dataVO.getScrollToBottom().size()<10)
        {

            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }

            }, 0, 8, 0);

            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {

                    //txt_client_agreement.callOnClick();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(true);
                }

            }, 9, 26, 0);

            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {
                    v.setClickable(false);

                    //txt_client_agreement.callOnClick();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }

            }, 27, clickedString.length(), 0);


        }else
        {

            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {
                    v.setClickable(false);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }

            }, 0, 8, 0);

            final CheckOutDataVO finalDataVO = dataVO;
            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent(activity, CheckOutDetailActivity.class);
                        i.putExtra("title", "Check Out");
                        i.putExtra("data", finalDataVO);
                        activity.startActivity(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //txt_client_agreement.callOnClick();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.blue));
                    ds.setUnderlineText(true);
                }

            }, 9, 27, 0);

            clickedString.setSpan(new ClickableSpan() {

                @Override
                public void onClick(View v) {
                    v.setClickable(false);

                    //txt_client_agreement.callOnClick();
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setColor(getResources().getColor(R.color.black));
                    ds.setUnderlineText(false);
                }

            }, 28, clickedString.length(), 0);


        }
        binding.txtMessage.setText(clickedString);

        binding.txtMessage.setMovementMethod(LinkMovementMethod.getInstance());*/
        binding.btnByPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.edtPasscodeNo.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Login login = databaseHandler.getContact();

                    GetManagerPasswordAsync async = new GetManagerPasswordAsync(activity,login.getDriverID(),md5(binding.edtPasscodeNo.getText().toString()), new ICallback() {

                        @Override
                        public void run(Object result) {
                            if(result == null)
                            {
                                L.alert(activity, "Server error! Please try again later.");
                                return;
                            }
                            String str = result.toString();
                            Log.e(getLocalClassName()+"result",""+str);
                            try {
                                JSONObject obj = new JSONObject(str);
                                String success = "";
                                String response = "";

                                if(obj.has("success"))
                                {
                                    success = obj.getString("success");
                                }


                                if (success.equalsIgnoreCase("true")) {
                                    //updatePassword();
                                    JSONObject json = obj.getJSONObject("response");
                                    JSONObject json1 = json.getJSONObject("data");
                                    String json2 = json1.getString("message");

                                    L.alert(activity, json2, new L.IL() {
                                        @Override
                                        public void onSuccess() {
                                            if(Pref.getValue(checkManagerPasswordActivity.this,"FromManagerPassword","").equalsIgnoreCase("checkin"))
                                            {
                                                goCheckIn();

                                            }else if( Pref.getValue(checkManagerPasswordActivity.this,"FromManagerPassword","").equalsIgnoreCase("checkout"))
                                            {
                                                checkCheckOut();
                                            }
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                                } else {
                                    L.alert(activity,""+"Authentication failed. Please try again.");

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                e.printStackTrace();
                                try {
                                    JSONObject jsonObject = new JSONObject(str);
                                    JSONArray jsonArray = jsonObject.getJSONArray("errors");
                                    for (int counter = 0; counter < jsonArray.length(); counter++) {

                                        JSONObject obj = jsonArray.getJSONObject(counter);
                                        L.alert(activity,""+obj.getString("message"));

                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }

                    });
                    async.execute();

                }else{
                    L.alert(activity,"Please provide valid passcode.");
                }
            }
        });

    }

    /**
     * this for when come from checkout screen
     */

    private void checkCheckOut() {
        if (Util.isNetAvailable(activity)) {

            String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
            Login login = databaseHandler.getContact();
            if (authKey != null && login != null && login.getDriverID() != null) {
                RequestParams params = new RequestParams();
                params.add(JsonKey.AUTH_KEY, authKey);
                params.add(JsonKey.KEY_DRIVER_ID, login.getDriverID());
                params.add(JsonKey.CheckOut.vehicleName, "0");
                params.add(JsonKey.CheckOut.endMiles, "0");
                params.add(JsonKey.CheckOut.checkOut, "Y");

                callCheckOut(params);

            } else {
                L.confirmDialog(activity, Constants.APP_NAME, activity.getResources().getString(R.string.force_login), "Login", "Cancel",
                        new L.IL() {

                            @Override
                            public void onSuccess() {
                                Intent i = new Intent(activity, LoginActivity.class);
                                activity.startActivity(i);
                                activity.finish();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        } else {
            L.alert(activity, activity.getResources().getString(R.string.offline_checkout));
        }
    }


    private void callCheckOut(RequestParams params) {
        RestClient.post(JsonKey.getURL_CHECK_OUT(), params, new AsyncHandler(activity, true) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;
                try {
                    Logger.info("#DASHBOARD:CHECKOUT#", response.toString());
                    responseJson = response.getJSONObject("response");
                    JSONObject dataJson = responseJson.getJSONObject("data");

                    if (dataJson.has("showForm") && dataJson.getString("showForm").equalsIgnoreCase("Y")) {
                        goCheckOut();
                    } else if (dataJson.has("showForm") && dataJson.getString("showForm").equalsIgnoreCase("scan")) {
                        goCheckIn();
                    } else if (dataJson.has("showForm") && dataJson.getString("showForm").equalsIgnoreCase("main")) {
                        doLogout();
                    } else {

                        // L.ok(activity, dataJson.getString("message"));
                        try {
                            Gson gson = new Gson();
                            CheckOutDataVO dataVO = gson.fromJson(dataJson.toString(), CheckOutDataVO.class);
                            Intent i = new Intent(activity, checkManagerPasswordActivity.class);
                            i.putExtra("title", "Check Out");
                            i.putExtra("data", dataVO);
                            activity.startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.showError(activity, response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                L.alert(activity, responseString);
            }
        });
    }
    private void doLogout() {
        Logger.debug(Constants.DEBUG, "Dashboard DoLogout() :: Success :: Goto :: login activity");
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
        if (databaseHandler != null)
            databaseHandler.deleteContact();

    }

    private void goCheckOut() {
        Intent i = new Intent(activity, CheckoutActivity.class);
        activity.startActivity(i);
    }

    /**
     * this for when come from Multiscan screen
     */


    public void callCheckIn(RequestParams params) {
        RestClient.post(JsonKey.getURL_CHECK_IN(), params, new AsyncHandler(activity, true) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;
                try {
                    Logger.info("#DASHBOARD:CHECKIN#", response.toString());
                    responseJson = response.getJSONObject("response");
                    JSONObject dataJson = responseJson.getJSONObject("data");

                    if (dataJson.has("success") && !(dataJson.getBoolean("success"))) // if
                    // an
                    // error
                    // is
                    // there
                    {
                        // handle error
                        if (Util.getErrorCode(responseJson) == 504) {
                            L.alert(activity, Util.getErrorMessage(activity, response), new L.IL() {

                                @Override
                                public void onSuccess() {
                                    changePassword();

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        } else
                            Util.showError(activity, response);

                    } else if (dataJson.has("showForm") && dataJson.getString("showForm").equalsIgnoreCase("Y")) {
                        Intent i = new Intent(activity, CheckInActivity.class);
                        activity.startActivity(i);
                        finish();
                    } else {
                        // L.ok(activity, dataJson.getString("message"));
                        try {
                            Gson gson = new Gson();
                            CheckOutDataVO dataVO = gson.fromJson(dataJson.toString(), CheckOutDataVO.class);
                            if (dataVO.getScrollToBottom() != null && dataVO.getScrollToBottom().size() > 0) {
                                Intent i = new Intent(activity, checkManagerPasswordActivity.class);
                                i.putExtra("title", "Check In");
                                i.putExtra("data", dataVO);
                                activity.startActivity(i);
                            } else
                                goInnerJob();
                        } catch (Exception e) {
                            e.printStackTrace();
                            goInnerJob();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Util.showError(activity, response);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                L.alert(activity, responseString);
            }
        });
    }
    public void goCheckIn() {
        if (Util.isNetAvailable(activity)) {

            String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
            Login login = databaseHandler.getContact();
            if (authKey != null && login != null && login.getDriverID() != null) {
                RequestParams params = new RequestParams();
                params.add(JsonKey.AUTH_KEY, authKey);
                params.add(JsonKey.KEY_DRIVER_ID, login.getDriverID());
                params.add(JsonKey.CheckIn.vehicleName, "0");
                params.add(JsonKey.CheckIn.startMiles, "0");
                params.add(JsonKey.CheckIn.checkIn, "Y");

                callCheckIn(params);

            } else
                goInnerJob();
        } else {
            goInnerJob();
        }
    }
    public void changePassword() {
        try {
            Intent i = new Intent(activity, ChangepwdActivity.class);
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void goInnerJob() {
        Intent i = new Intent(activity, MultiScanInner.class);
        activity.startActivity(i);
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
