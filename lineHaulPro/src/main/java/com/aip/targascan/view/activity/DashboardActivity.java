package com.aip.targascan.view.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.Adapter.ListCotypeAdapter;
import com.aip.targascan.R;
import com.aip.targascan.common.async.ChangepasswordAsync;
import com.aip.targascan.common.async.CompanyRejectionListAsync;
import com.aip.targascan.common.async.DriverRouteSubmitAsync;
import com.aip.targascan.common.async.DriverRouteSubmitBackgroundAsync;
import com.aip.targascan.common.async.ExportDataAsync;
import com.aip.targascan.common.async.GetAddressListAsync;
import com.aip.targascan.common.async.GetDailyOrdersListAsync;
import com.aip.targascan.common.async.GetProductDetailAsync;
import com.aip.targascan.common.async.IResultListner;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ASyncCheck;
import com.aip.targascan.common.util.ApplicationLog;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Pref;
import com.aip.targascan.common.util.WebserviceReader;
import com.aip.targascan.common.util.L.IL;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.model.ProductDetail;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.CheckOutDataVO;
import com.aip.targascan.vo.CompanyRejection;
import com.aip.targascan.vo.DriverRouteData;
import com.aip.targascan.vo.Login;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;

@ContentView(R.layout.activity_dashboard)
public class DashboardActivity extends RoboActivity {
    @InjectView(R.id.ivlinehauls)
    ImageView ivlinehauls;
    @InjectView(R.id.ivchangepwd)
    ImageView ivchangepassword;
    @InjectView(R.id.ivdriverdetails)
    ImageView ivdriverdetails;
    @InjectView(R.id.ivcachedjobs)
    ImageView ivcachedjobs;
    @InjectView(R.id.tvcachedjobs)
    TextView tvcachedjobs;
    @InjectView(R.id.textview_line_hauls)
    TextView textview_line_hauls;
    @InjectView(R.id.tvdeletecachedjobs)
    TextView tvdeletecachedjobs;
    @InjectView(R.id.ivcheckout)
    ImageView ivcheckout;
    @InjectView(R.id.ivgroupphoto)
    ImageView ivgroupphoto;
    @InjectView(R.id.ivexit)
    ImageView ivexit;
    @InjectView(R.id.ivmultiscan)
    ImageView ivmultiscan;
    // @InjectView(R.id.ivpreferences) ImageView ivpreferences;
    // @InjectView(R.id.ivscansettings) ImageView ivscansettings;
    // @InjectView(R.id.ivscanninghelp) ImageView ivscanninghelp;
    @InjectView(R.id.ivrouteapp)
    ImageView ivrouteapp;
    @InjectView(R.id.ll_linehauls)
    LinearLayout lllinehauls;
    @InjectView(R.id.ll_changepassword)
    LinearLayout llchangepassword;
    @InjectView(R.id.ll_driverdetails)
    LinearLayout lldriverdetails;
    @InjectView(R.id.ll_multiscan)
    LinearLayout llmultiscan;
    @InjectView(R.id.llcachedjobs)
    LinearLayout llcachedjob;
    @InjectView(R.id.lldeletecachedjobs)
    LinearLayout lldeletecachedjob;
    @InjectView(R.id.lladdresslist)
    LinearLayout lladdresslist;
    // @InjectView(R.id.ll_scansettings) LinearLayout llscansettings;
    @InjectView(R.id.ll_routeapp)
    LinearLayout llrouteapp;
    @InjectView(R.id.ll_groupphoto)
    LinearLayout llgroupphoto;
    @InjectView(R.id.ll_logout)
    LinearLayout lllogout;
    @InjectView(R.id.ll_checkout)
    LinearLayout llcheckout;
    @InjectView(R.id.ll_reference)
    LinearLayout llreference;
    @InjectView(R.id.ll_scan)
    LinearLayout ll_scan;

    @InjectView(R.id.search_cartun)
    EditText search_cartun;
    @InjectView(R.id.imgAppWeb)
    ImageView imgAppWeb;

    /* @InjectView(R.id.ivsearch)
     ImageView ivsearch;*/
    @InjectView(R.id.ivscan)
    ImageView ivscan;

    @InjectView(R.id.txt_document_scan)
    TextView txt_document_scan;

    @InjectView(R.id.txt_logout)
    TextView txt_logout;

    @InjectView(R.id.ivsearch_cartun)
    ImageView ivsearch_cartun;


    /* @InjectView(R.id.txt_search)
     TextView txt_search;*/
    private DashboardActivity activity;
    private DatabaseHandler databaseHandler;
    private int count;

    int is_Scan_enable=0;
    private boolean isNewVersionAvailable = false;
    private AlertDialog UpdateDialog;
    boolean isDuplicate=false;
    ArrayList<ProductDetail> productDetailArrayList1 = new ArrayList<ProductDetail>();
    public ArrayList<ProductDetail> productDetailArrayList=new ArrayList<>();
    ArrayList<String> products = new ArrayList<String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        databaseHandler = DatabaseHandler.getInstance(activity);

        if(Pref.getValue(DashboardActivity.this,"auto_scan","").equalsIgnoreCase("")) {
            if (is_Scan_enable == 1) {
                txt_document_scan.setText("Crop Enabled");

                ivscan.setAlpha(255);
                is_Scan_enable=0;
            } else if (is_Scan_enable == 0) {
                txt_document_scan.setText("Crop Disabled");
                ivscan.setAlpha(127);
                is_Scan_enable=1;
            }
        }else
        {
            if(Pref.getValue(DashboardActivity.this,"auto_scan","").equalsIgnoreCase("1")) {
                txt_document_scan.setText("Crop Enabled");
                ivscan.setAlpha(255);
                is_Scan_enable=0;
            }else
            {
                txt_document_scan.setText("Crop Disabled");
                ivscan.setAlpha(127);
                is_Scan_enable=1;
            }
        }
        new ASyncCheck(activity, new ICallback() {

            @Override
            public void run(Object result) {
                // TODO Auto-generated method stub
                try {
                    if (result != null) {
                        try {

                            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                            String version = pInfo.versionName;

                            // if (value(version) < value(result.toString())) {
                            // Log.d("new version", "new version");
                            // isNewVersionAvailable = true;
                            // showUpdateDialog();
                            // } else {
                            // Log.d("old version", "old version");
                            // isNewVersionAvailable = false;
                            // }

                            if (version.equals(result.toString())) {
                                Log.d("same version", "same version");
                                isNewVersionAvailable = false;
                            } else {
                                Log.d("new version", "new version");
                                isNewVersionAvailable = true;
                                showUpdateDialog();
                            }

                        } catch (NameNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).execute();

        if (Util.isNetAvailable(activity)) {
            new GetDailyOrdersListAsync(activity, false, new ICallback() {

                @Override
                public void run(Object result) {
                    // TODO Auto-generated method stub
                    if (result != null) {
                        Log.d("GetDailyOrdersListAsync", "true" + result.toString());
                    } else {
                        Log.d("GetDailyOrdersListAsync", "false");
                    }
                }
            }).execute();
        }

        ApplicationLog.writeToFile(activity, Constants.PKG_NAME, getResources().getString(R.string.app_name), Constants.DEBUG);

        Logger.debug(Constants.DEBUG, "DashBoard Activity Started::::::::::::::::::::::::::::::::::::::::");
        boolean isValidAuthKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_IS_VALID_AUTH_KEY, true);
        if (!isValidAuthKey) {
            doLogout();
        }


        llchangepassword.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        lldriverdetails.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (Util.isNetAvailable(activity)) {
                        Intent i = new Intent(activity, DriverdetailActivity.class);
                        activity.startActivity(i);
                    } else {
                        L.alert(activity, activity.getResources().getString(R.string.error_internet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        llmultiscan.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
				/*
				 * Intent i= new Intent(activity, CheckInActivity.class);
				 * activity.startActivity(i);
				 */
                goCheckIn();
                // goInnerJob();
            }
        });

        llcheckout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
				/*
				 * Intent i= new Intent(activity, CheckoutActivity.class);
				 * activity.startActivity(i);
				 */
                checkCheckOut();
            }
        });

        lladdresslist.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                GetAddressListAsync async = new GetAddressListAsync(DashboardActivity.this, true, new ICallback() {

                    @Override
                    public void run(Object result) {

                        // try{
                        //
                        // JsonObject jsonObject = (JsonObject) result;
                        //
                        // Log.d("", "" + jsonObject.toString(4));
                        //
                        // }catch(Exception e){
                        // e.printStackTrace();
                        // }

                        try {
                            JSONObject jsonObject = new JSONObject(result.toString());
                            Log.e("Addresses", "Addresses \n" + jsonObject.toString(4));

                            if (jsonObject.getString("code").equals("200")) {
                                try {
                                    Intent i = new Intent(activity, AddressListActivity.class);
                                    activity.startActivity(i);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if(jsonObject.getString("code").equals("201")){
                                L.alert(DashboardActivity.this, jsonObject.getString("message"));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                async.execute();



            }
        });
        ll_scan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //  if(Pref.getValue(DashboardActivity.this,"auto_scan","").equalsIgnoreCase("")) {
                if (is_Scan_enable == 0) {
                    txt_document_scan.setText("Crop Disabled");
                    ivscan.setAlpha(127);
                    Pref.setValue(DashboardActivity.this, "auto_scan", "0");
                    is_Scan_enable = 1;
                }else  if (is_Scan_enable == 1) {
                    txt_document_scan.setText("Crop Enabled");
                    is_Scan_enable = 0;
                    ivscan.setAlpha(255);
                    Pref.setValue(DashboardActivity.this, "auto_scan", "1");
                }

            }
        });
        llcachedjob.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // Intent i = new Intent(activity,
                    // CachedjobsActivity.class);
                    // activity.startActivity(i);

                    if ((databaseHandler.getCachedjobCount() + databaseHandler.getDriverRouteTimeStatusDetailsCount()) < 1) {
                        L.alert(activity, "No jobs found!");
                        return;
                    }

                    L.confirmDialog(activity, getResources().getString(R.string.msg_retransmit_cachedjojb), new IL() {

                        @Override
                        public void onSuccess() {
                            if (Util.isNetAvailable(activity)) {
                                // List<Cachedjob> cachedjobs =
                                // database.getCachedjob();
                                Login login = databaseHandler.getContact();
                                if (login == null) {
                                    L.alert(activity, activity.getResources().getString(R.string.cach_job_login_error));
                                } else {

                                    if (databaseHandler.getCachedjobCount() > 0) {
                                        ExportDataAsync exportDataAsync = new ExportDataAsync(activity, new IResultListner() {

                                            @Override
                                            public void result(Object result, boolean isSuccess) {
                                                UpdateUi();
                                                try {
                                                    Log.v("ExportDataAsync", result.toString());
                                                } catch (Exception e) {
//													e.printStackTrace();
                                                }
                                            }
                                        });
                                        exportDataAsync.execute();
                                    }

                                    try {
                                        Cursor cursor = databaseHandler.getDriverRouteTimeStatuDetails();
                                        if (cursor.moveToFirst() && cursor.getCount() > 0) {
                                            cursor.moveToFirst();
                                            UploadDriverRouteTimeStatusDetails(cursor);
                                        }
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    try {
                                        Cursor cursor = databaseHandler.getDriverLabel();
                                        if (cursor.moveToFirst() && cursor.getCount() > 0) {
                                            cursor.moveToFirst();
                                            UploadDriverLabel(cursor);
                                        }
                                        if (cursor != null) {
                                            cursor.close();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }

                            } else {
                                L.alert(activity, getResources().getString(R.string.error_internet));
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ivsearch_cartun.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!search_cartun.getText().toString().trim().equalsIgnoreCase("")) {
                    if (Util.isNetAvailable(activity)) {
                        search_cartun_number();
                    } else {
                        L.alert(activity, activity.getResources().getString(R.string.error_internet));
                    }

                }
            }
        });
        search_cartun.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            if(!search_cartun.getText().toString().trim().equalsIgnoreCase("")) {
                                if (Util.isNetAvailable(activity)) {
                                    search_cartun_number();
                                } else {
                                    L.alert(activity, activity.getResources().getString(R.string.error_internet));
                                }
                            }
                        }

                        return false;
                    }


                });

        lldeletecachedjob.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // // Intent i = new Intent(activity,
                    // // CachedjobsActivity.class);
                    // // activity.startActivity(i);

                    if ((databaseHandler.getCachedjobCount() + databaseHandler.getDriverRouteTimeStatusDetailsCount()) < 1) {
                        L.alert(activity, "No jobs found!");
                        return;
                    }

                    L.confirmDialog(activity, getResources().getString(R.string.msg_delete_cachedjojb), new IL() {

                        @Override
                        public void onSuccess() {
                            checkPassword();
                        }

                        private void checkPassword() {
                            // TODO Auto-generated method stub
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            final EditText input = new EditText(activity);
                            input.setHint("Password");
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            builder.setView(input);

                            builder.setTitle("Please enter password to delete jobs.");
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Login login = databaseHandler.getContact();
                                    if (login == null) {
                                        L.alert(activity, activity.getResources().getString(R.string.cach_job_login_error));
                                    } else {
                                        if (login.getPassword().equals(input.getText().toString())) {
                                            databaseHandler.deleteCachedJob();
                                            databaseHandler.deleteDriverRouteTimeStatusDetails();
                                            databaseHandler.deleteDriverLabel();
                                            UpdateUi();
                                        } else {
                                            L.confirmDialog(activity, "Current password not matched", new IL() {

                                                @Override
                                                public void onSuccess() {
                                                    // TODO Auto-generated
                                                    // method stub
                                                    checkPassword();
                                                }

                                                @Override
                                                public void onCancel() {
                                                    // TODO Auto-generated
                                                    // method stub

                                                }
                                            });
                                        }
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // llscansettings.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        //
        // }
        // });
        // ivscanninghelp.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        //
        // }
        // });
        llrouteapp.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.openApp(activity, Constants.APP_ROUTE_APP);
            }
        });
        llgroupphoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                Util.openApp(activity, Constants.APP_GROUP_PHOTO);
            }
        });
        // ivpreferences.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // try
        // {
        // /*Intent i= new Intent(activity,PreferenceActivity.class);
        // activity.startActivity(i);*/
        // }catch(Exception e)
        // {
        // e.printStackTrace();
        // }
        // }
        // });
      /*  ll_search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(activity, OrderListDisplayActivity.class);
                    activity.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });*/

        llreference.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    if (Util.isNetAvailable(activity)) {
                        Intent i = new Intent(activity, ReferenceActivity.class);
                        activity.startActivity(i);
                    } else {
                        L.alert(activity, activity.getResources().getString(R.string.error_internet));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        lllogout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                L.confirmDialog(activity, Constants.APP_NAME, activity.getResources().getString(R.string.msg_logout), "Yes", "No",
                        new IL() {

                            @Override
                            public void onSuccess() {
                                DatabaseHandler database;
                                database = DatabaseHandler.getInstance(activity);
                                database.deleteCompany();
                                doLogout();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
            }
        });

        imgAppWeb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Util.openUrl(DashboardActivity.this, Constants.WEB_URL);
            }
        });

        if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {
            lllinehauls.setVisibility(View.VISIBLE);
            textview_line_hauls.setVisibility(View.VISIBLE);
            textview_line_hauls.setText(R.string.textview_line_hauls);
            ivlinehauls.setImageResource(R.drawable.icon_check_out);
            ll_scan.setVisibility(View.VISIBLE);
            txt_document_scan.setVisibility(View.VISIBLE);
            lllinehauls.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    lineHauls();
                }
            });

        } else {
            lllinehauls.setVisibility(View.VISIBLE);
            textview_line_hauls.setVisibility(View.VISIBLE);
            textview_line_hauls.setText(R.string.textview_exit);
            ivlinehauls.setImageResource(R.drawable.icon_exit);
            lllogout.setVisibility(View.GONE);
            txt_logout.setVisibility(View.GONE);
            lllinehauls.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    L.confirmDialog(activity, Constants.APP_NAME, activity.getResources().getString(R.string.msg_logout), "Yes", "No",
                            new IL() {

                                @Override
                                public void onSuccess() {
                                    DatabaseHandler database;
                                    database = DatabaseHandler.getInstance(activity);
                                    database.deleteCompany();
                                    doLogout();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
            });

            ll_scan.setVisibility(View.GONE);
            txt_document_scan.setVisibility(View.GONE);
        }

        if (Util.isNetAvailable(activity)) {
            new CompanyRejectionListAsync(activity, false, new ICallback() {

                @Override
                public void run(Object result) {
                    // TODO Auto-generated method stub
                    try {
                        Log.d("CompanyRejection", "" + result.toString());
                        JSONObject object = new JSONObject(result.toString());
                        JSONArray array = object.getJSONArray("data");
                        databaseHandler.deleteCompanyRejection();
                        for (int counter = 0; counter < array.length(); counter++) {
                            CompanyRejection rejection = new CompanyRejection();
                            rejection.setMaster_company_id(array.getJSONObject(counter).getString("master_company_id"));
                            rejection.setEditing(array.getJSONObject(counter).getString("editing"));
                            databaseHandler.addCompanyRejection(rejection);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).execute();
        }

    }
    public void search_cartun_number()
    {
        productDetailArrayList.clear();
        productDetailArrayList1.clear();
        products.clear();
        GetProductDetailAsync async = new GetProductDetailAsync(activity, search_cartun.getText().toString(), new ICallback() {

            @Override
            public void run(Object result) {
                if(result == null)
                {
                    L.alert(activity, "Server error! Please try again later.");
                    return;
                }
                String str = result.toString();
                // Log.e("result",""+str);
                JSONObject json;
                try {
                    json = new JSONObject(str);
                    String code =json.getString("code");
                    if(code.equalsIgnoreCase("200"))
                    {
                        JSONArray jsonArray_data=json.getJSONArray("data");
                        ProductDetail[] productDetails =new ProductDetail[jsonArray_data.length()];
                        for(int i=0;i<jsonArray_data.length();i++)
                        {
                            productDetails[i]=new ProductDetail();
                            if(!jsonArray_data.getJSONObject(i).getString("date").equalsIgnoreCase("null")) {
                                productDetails[i].setDate(jsonArray_data.getJSONObject(i).getString("date"));
                            }else
                            {
                                productDetails[i].setDate("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("carton_num").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setCarton_num1(jsonArray_data.getJSONObject(i).getString("carton_num"));

                            }else
                            {
                                productDetails[i].setCarton_num1("");

                            }
                            if(!jsonArray_data.getJSONObject(i).getString("record_created").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setRecord_created(jsonArray_data.getJSONObject(i).getString("record_created"));

                            }else
                            {
                                productDetails[i].setRecord_created("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("driver_id").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setDriver_id1(jsonArray_data.getJSONObject(i).getString("driver_id"));

                            }else
                            {
                                productDetails[i].setDriver_id1("");

                            }
                            if(!jsonArray_data.getJSONObject(i).getString("cust_name").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setCust_name(jsonArray_data.getJSONObject(i).getString("cust_name"));

                            }else
                            {
                                productDetails[i].setCust_name("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("work_order_num").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setWork_order_num(jsonArray_data.getJSONObject(i).getString("work_order_num"));
                            }else
                            {
                                productDetails[i].setWork_order_num("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("delivery_name").equalsIgnoreCase("null")) {
                                productDetails[i].setDelivery_name(jsonArray_data.getJSONObject(i).getString("delivery_name"));
                            }else
                            {
                                productDetails[i].setDelivery_name("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("delivery_address").equalsIgnoreCase("null")) {
                                productDetails[i].setDelivery_address(jsonArray_data.getJSONObject(i).getString("delivery_address"));
                            }else
                            {
                                productDetails[i].setDelivery_address("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("zip").equalsIgnoreCase("null")) {
                                productDetails[i].setZip(jsonArray_data.getJSONObject(i).getString("zip"));
                            }else
                            {
                                productDetails[i].setZip("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("quantity").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setQuantity(jsonArray_data.getJSONObject(i).getString("quantity"));
                            }else
                            {
                                productDetails[i].setQuantity("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("weight").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setWeight(jsonArray_data.getJSONObject(i).getString("weight"));
                            }else
                            {
                                productDetails[i].setWeight("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("co_type").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setCo_type1(jsonArray_data.getJSONObject(i).getString("co_type"));
                            }else
                            {
                                productDetails[i].setCo_type1("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("redeliver").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setRedeliver1(jsonArray_data.getJSONObject(i).getString("redeliver"));
                            }else
                            {
                                productDetails[i].setRedeliver1("");

                            }
                            if(!jsonArray_data.getJSONObject(i).getString("assigned_route").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setAssigned_route(jsonArray_data.getJSONObject(i).getString("assigned_route"));
                            }else
                            {
                                productDetails[i].setAssigned_route("");

                            }
                            if(!jsonArray_data.getJSONObject(i).getString("osd").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setOsd(jsonArray_data.getJSONObject(i).getString("osd"));
                            }else
                            {
                                productDetails[i].setOsd("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("comment").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setComment(jsonArray_data.getJSONObject(i).getString("comment"));
                            }else
                            {
                                productDetails[i].setComment("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("pupname").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setPupname(jsonArray_data.getJSONObject(i).getString("pupname"));
                            }else
                            {
                                productDetails[i].setPupname("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("pupaddress").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setPupaddress(jsonArray_data.getJSONObject(i).getString("pupaddress"));
                            }else
                            {
                                productDetails[i].setPupaddress("");
                            }

                            if(!jsonArray_data.getJSONObject(i).getString("pupcity").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setPupcity(jsonArray_data.getJSONObject(i).getString("pupcity"));
                            }else
                            {
                                productDetails[i].setPupcity("");
                            }
                            if(!jsonArray_data.getJSONObject(i).getString("pupzip").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setPupzip(jsonArray_data.getJSONObject(i).getString("pupzip"));

                            }else
                            {
                                productDetails[i].setPupzip("");

                            }
                            if(!jsonArray_data.getJSONObject(i).getString("city").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setCity(jsonArray_data.getJSONObject(i).getString("city"));


                            }else
                            {
                                productDetails[i].setCity("");


                            }
                            if(!jsonArray_data.getJSONObject(i).getString("pickup").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setPickup(jsonArray_data.getJSONObject(i).getString("pickup"));

                            }else
                            {
                                productDetails[i].setPickup("");
                            }

                            if(!jsonArray_data.getJSONObject(i).getString("destination").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setDestination(jsonArray_data.getJSONObject(i).getString("destination"));

                            }else
                            {
                                productDetails[i].setDestination("");
                            }

                            if(!jsonArray_data.getJSONObject(i).getString("sig_string").equalsIgnoreCase("null"))
                            {
                                productDetails[i].setSig_string(jsonArray_data.getJSONObject(i).getString("sig_string"));

                            }else
                            {
                                productDetails[i].setSig_string("");
                            }
                            productDetailArrayList.add(productDetails[i]);
                        }
//                                            Log.e("#detail RESPONSE#",""+productDetailArrayList.get(0).getCarton_num1());
                        if(productDetailArrayList.size()>0)
                        {
                            if(productDetailArrayList.size()==1)
                            {
                                if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("YCOL")) {
                                    String url = "https://yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                    Pref.setValue(DashboardActivity.this, "Detail_url", url);
                                }else if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("CTS"))
                                {
                                    String url = "http://cargotracking.solutions/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                    Pref.setValue(DashboardActivity.this, "Detail_url", url);

                                }
                              /*  String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                Pref.setValue(DashboardActivity.this, "Detail_url", url);
*/

                                Intent intent =new Intent(DashboardActivity.this,ProductDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelableArrayList("productDetailArrayList", productDetailArrayList);
                                intent.putExtras(bundle);
                                intent.putExtra("warning","0");
                                startActivity(intent);

                            }else if(productDetailArrayList.size()>1)
                            {
                                products.clear();
                                for(int i=0;i<productDetailArrayList.size();i++)
                                {
                                    products.add(productDetailArrayList.get(i).getCo_type1());
                                }

                                String value = products.get(0);
                                Log.e("value",""+products);
                                if(products.size()>1) {
                                    for (int i = 1; i < products.size(); i++) {
                                        if (products.get(i).equalsIgnoreCase(value))
                                        {
                                            isDuplicate=true;
                                        }else
                                        {
                                            isDuplicate=false;
                                        }
                                    }
                                }
                                if(!isDuplicate) {
                                    dialog(products);
                                }else
                                {
                                    if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("YCOL")) {
                                        String url = "https://yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                        Pref.setValue(DashboardActivity.this, "Detail_url", url);
                                    }else if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("CTS"))
                                    {
                                        String url = "http://cargotracking.solutions/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                        Pref.setValue(DashboardActivity.this, "Detail_url", url);

                                    }
/*
                                    String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList.get(0).getCo_type1();
                                    Pref.setValue(DashboardActivity.this, "Detail_url", url);
*/

                                    Intent intent =new Intent(DashboardActivity.this,ProductDetailActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("productDetailArrayList", productDetailArrayList);
                                    intent.putExtras(bundle);
                                    intent.putExtra("warning","1");
                                    startActivity(intent);

                                }
                            }
                        }else
                        {

                            Toast.makeText(DashboardActivity.this,"No record found for carton number "+search_cartun.getText().toString()+".",Toast.LENGTH_LONG).show();
                            View view = getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }

        });
        async.execute();
    }
    private void UploadDriverLabel(Cursor cursor) {
        // TODO Auto-generated method stub
        try {
            String data = Util.cur2Json(cursor).toString();
            final RequestParams params = new RequestParams();
            params.add("data", data);
            // Log.e("Dhims: ", "--\n" + UploadArrayData.toString());

            RestClient.post(JsonKey.getURL_MULTI_SCAN_CO_TYPE_DRIVER_ROUTE_LABEL(), params, new AsyncHandler(activity, true) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONObject responseJson = null;
                    try {
                        Logger.info("#DASHBOARD:MULTISCAN#", params + ">> statusCode: " + statusCode);
                        Logger.info("#DASHBOARD:MULTISCAN#", params + ">>response: " + response.toString());

                        try {
                            Log.v("UploadDriverLabel", response.toString());
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }

                        if (response.getString("code").equals("200")) {
                            databaseHandler.deleteDriverLabel();
                            UpdateUi();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void UploadDriverRouteTimeStatusDetails(Cursor cursor) {
        // TODO Auto-generated method stub
        try {
            String data = Util.cur2Json(cursor).toString();
            DriverRouteData driverRouteData = new DriverRouteData();

            DriverRouteSubmitBackgroundAsync async = new DriverRouteSubmitBackgroundAsync(activity, driverRouteData, data, new ICallback() {

                @Override
                public void run(Object result) {
                    // TODO Auto-generated method stub
                    try {
                        JSONObject jsonObject = new JSONObject(result.toString());
                        try {
                            Log.v("DriverRouteSubmit", result.toString());
                        } catch (Exception e) {
                            // e.printStackTrace();
                        }
                        Log.e("#DashboardActivity", jsonObject.toString());
                        if (jsonObject.getString("code").equals("200")) {
                            databaseHandler.deleteDriverRouteTimeStatusDetails();
                            UpdateUi();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            async.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLogout() {
        Logger.debug(Constants.DEBUG, "Dashboard DoLogout() :: Success :: Goto :: login activity");
        Intent i = new Intent(activity, LoginActivity.class);
        activity.startActivity(i);
        activity.finish();
        if (databaseHandler != null)
            databaseHandler.deleteContact();

    }

    private void goCheckIn() {
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
                        new IL() {

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

    private void goInnerJob() {
        Intent i = new Intent(activity, MultiScanInner.class);
        activity.startActivity(i);
    }

    private void goCheckOut() {
        Intent i = new Intent(activity, CheckoutActivity.class);
        activity.startActivity(i);
    }

    private void callCheckIn(RequestParams params) {
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
                            L.alert(activity, Util.getErrorMessage(activity, response), new IL() {

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
                    } else {
                        // L.ok(activity, dataJson.getString("message"));
                        try {
                            Gson gson = new Gson();
                            CheckOutDataVO dataVO = gson.fromJson(dataJson.toString(), CheckOutDataVO.class);
                            if (dataVO.getScrollToBottom() != null && dataVO.getScrollToBottom().size() > 0) {
                                Intent i = new Intent(activity, CheckOutDetailActivity.class);
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
                            Intent i = new Intent(activity, CheckOutDetailActivity.class);
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

    private boolean isRouteApp, isGroupPhotoApp;

    // private final String TAG = "##DashboardActivity##";
    private void checkApp() {
        final PackageManager pm = getPackageManager();
        // get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {

            if (Constants.APP_GROUP_PHOTO.equalsIgnoreCase(packageInfo.packageName)) {
                isGroupPhotoApp = true;

            } else if (Constants.APP_ROUTE_APP.equalsIgnoreCase(packageInfo.packageName)) {
                isRouteApp = true;
            }
            // Log.d(TAG, "Installed package :" + packageInfo.packageName);
            // Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            // Log.d(TAG, "Launch Activity :" +
            // pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        search_cartun.setText("");
        products.clear();
        productDetailArrayList.clear();
        productDetailArrayList1.clear();
        checkApp();

        if (isRouteApp) {
            ((LinearLayout) findViewById(R.id.dashboard_lnrRouteAppContainer)).setVisibility(View.VISIBLE);
        }
        if (isGroupPhotoApp) {
            ((LinearLayout) findViewById(R.id.dashboard_lnrGroupPhotAppContainer)).setVisibility(View.VISIBLE);
        }

        UpdateUi();

        if (isNewVersionAvailable == true) {
            showUpdateDialog();
        }

    }

    public void lineHauls() {
        try {
            Intent i = new Intent(activity, LineHaulActivity.class);
            activity.startActivity(i);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void UpdateUi() {

        try {

            int count = databaseHandler.getCachedjobCount();
            int counter = databaseHandler.getDriverRouteTimeStatusDetailsCount();

            int total = count + counter;

            if (total < 1) {
                tvcachedjobs.setText(total + "");
                tvcachedjobs.setVisibility(View.GONE);
                tvdeletecachedjobs.setText(total + "");
                tvdeletecachedjobs.setVisibility(View.GONE);
            } else {
                tvcachedjobs.setText(total + "");
                tvcachedjobs.setVisibility(View.VISIBLE);
                tvdeletecachedjobs.setText(total + "");
                tvdeletecachedjobs.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void prepareUpload(String deriverId, Cachedjob cachedjob, String authKey) {
        try {
            RequestParams params = new RequestParams();
            params.add(JsonKey.MUTISCAN.ass1, cachedjob.getChg1());
            params.add(JsonKey.MUTISCAN.ass2, cachedjob.getChg2());
            params.add(JsonKey.MUTISCAN.cached_date, cachedjob.getCachedDate());
            params.add(JsonKey.MUTISCAN.cn, cachedjob.getcartoon1());
            params.add(JsonKey.MUTISCAN.co_type, cachedjob.getCompanyID());
            params.add(JsonKey.MUTISCAN.cust_name, cachedjob.getSignatureName());
            params.add(JsonKey.MUTISCAN.DID, deriverId);
            params.add(JsonKey.MUTISCAN.redeliver, cachedjob.getRedelivery());
            params.add(JsonKey.MUTISCAN.servID, cachedjob.getServId());
            params.add(JsonKey.MUTISCAN.signData, cachedjob.getEncodeSign());
            params.add(JsonKey.MUTISCAN.system, cachedjob.getSystem());
            params.add(JsonKey.MUTISCAN.to, cachedjob.getTo());
            params.add(JsonKey.MUTISCAN.from, cachedjob.getFrom());

            params.add(JsonKey.AUTH_KEY, authKey);

            doUpload(params);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doUpload(RequestParams params) {
        RestClient.post(JsonKey.getURL_MULTI_SCAN(), params, new AsyncHandler(activity, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;
                try {
                    Logger.info("#DASHBOARD:MULTISCAN#", response.toString());
                    responseJson = response.getJSONObject("response");
                    JSONObject dataJson = responseJson.getJSONObject("data");
                    String message = "";

                    if (dataJson.has("message")) {
                        message = dataJson.getString("message");
                        L.ok(activity, message);
                    }

                    if (dataJson.has("goto")) {
                        if (dataJson.getString("goto").equalsIgnoreCase("scan")) {

                        } else if (dataJson.getString("goto").equalsIgnoreCase("main")) {
                            activity.finish();
                        }
                    } else {
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

    private long value(String string) {
        string = string.trim();
        if (string.contains(".")) {
            final int index = string.lastIndexOf(".");
            return value(string.substring(0, index)) * 100 + value(string.substring(index + 1));
        } else {
            return Long.valueOf(string);
        }
    }

    private void showUpdateDialog() {

        if (UpdateDialog == null) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

            alertDialogBuilder.setTitle("New Version Available");
            alertDialogBuilder.setMessage("Check out the new update for LineHaul Pro. Available now on Google Play.").setCancelable(true)
                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                            i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.aip.targascan"));
                            activity.startActivity(i);
                        }
                    });

            UpdateDialog = alertDialogBuilder.create();
            UpdateDialog.setCancelable(false);
            UpdateDialog.show();

        } else if (!UpdateDialog.isShowing()) {
            UpdateDialog.show();
        }

    }
    private void dialog(ArrayList<String> cartuns) {
        // custom dialog

        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.cotype_detail_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        ImageView imgcancel = (ImageView) dialog.findViewById(R.id.imgcancel);
        TextView txt_des = (TextView)dialog.findViewById(R.id.txt_des);
        ListView list_co_Type = (ListView) dialog.findViewById(R.id.list_co_Type);
        txt_des.setText("Carton number "+search_cartun.getText().toString()+" available under multiple co type.  Select one to view carton details.");
        ListCotypeAdapter adapter = new ListCotypeAdapter(DashboardActivity.this,cartuns);
        list_co_Type.setAdapter(adapter);
        productDetailArrayList1.clear();
        list_co_Type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                for(int j = 0;j<productDetailArrayList.size();j++)
                {
                    if(j==i)
                    {
                        productDetailArrayList1.add(productDetailArrayList.get(j));

                    }
                }
                if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("YCOL")) {
                    String url = "https://yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList1.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList1.get(0).getCo_type1();
                    Pref.setValue(DashboardActivity.this, "Detail_url", url);
                }else if(Pref.getValue(DashboardActivity.this,"weburlname","").equalsIgnoreCase("CTS"))
                {
                    String url = "http://cargotracking.solutions/search_script/search_app.php?cn=" + productDetailArrayList1.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList1.get(0).getCo_type1();
                    Pref.setValue(DashboardActivity.this, "Detail_url", url);

                }

               /* String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn=" + productDetailArrayList1.get(0).getCarton_num1() + "&co_type=" + productDetailArrayList1.get(0).getCo_type1();
                Pref.setValue(DashboardActivity.this, "Detail_url", url);
*/
                Intent intent =new Intent(DashboardActivity.this,ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("productDetailArrayList", productDetailArrayList1);
                intent.putExtras(bundle);
                intent.putExtra("warning","0");
                startActivity(intent);
                dialog.dismiss();
                products.clear();
            }
        });
        imgcancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                products.clear();
            }
        });
        dialog.show();

    }
}
