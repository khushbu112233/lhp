package com.aip.targascan.view.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.aip.targascan.R;
import com.aip.targascan.common.async.CheckCartonNumberAsync;
import com.aip.targascan.common.async.GetMustScanCoType;
import com.aip.targascan.common.async.MultiScanDataAsync;
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
import com.aip.targascan.model.Co_type;
import com.aip.targascan.view.frag.SimpleScannerActivity;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.DailyOrder;
import com.aip.targascan.vo.DriverLabel;
import com.aip.targascan.vo.Login;
import com.aip.targascan.vo.OrderDeliver;
import com.loopj.android.http.RequestParams;
import com.scanlibrary.PickImageFragment;
import com.scanlibrary.ResultFragment;
import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;
import com.scanlibrary.ScanFragment;
import com.scanlibrary.Utils;

@ContentView(R.layout.activity_multi_scan_inner)
public class MultiScanInner extends RoboActivity {
    @InjectView(R.id.multiscan_scrll_2)
    ScrollView rootScrollView_2;
    @InjectView(R.id.btnSavedetail_2)
    Button btnSavedetail_2;
    @InjectView(R.id.custom_lin)
    LinearLayout custom_lin;
    @InjectView(R.id.multiscan_scrll)
    ScrollView rootScrollView;
    @InjectView(R.id.btnSavedetail)
    Button btnSavedetail;
    @InjectView(R.id.btnLock)
    Button btnLock;
    @InjectView(R.id.imgLock)
    ImageView imgLock;
    @InjectView(R.id.txtLock)
    TextView txtLock;
    // @InjectView(R.id.cartoon_btnScan) Button btnSacn;
    @InjectView(R.id.multiscan_editSignature)
    EditText editsignature;
    @InjectView(R.id.multiscan_spinnerchg1)
    Spinner spinChg1;
    @InjectView(R.id.multiscan_spinnerCompanyid)
    Spinner spinCompanyid;
    @InjectView(R.id.multiscan_spinnerchg2)
    Spinner spinChg2;
    // @InjectView(R.id.cartoon_editCartoon1) EditText editcartoon1;
    @InjectView(R.id.multiscan_spinnerRedelivery)
    Spinner spinRedelivery;
    @InjectView(R.id.multiscan_editTo)
    EditText editto;
    @InjectView(R.id.multiscan_editFrom)
    EditText editfrom;
    @InjectView(R.id.imgSingature)
    ImageView imgSignature;
    @InjectView(R.id.imgDocumentScan)
    ImageView imgDocumentScan;
    @InjectView(R.id.tblCartoonContainer)
    LinearLayout tblCartoonContainer;

    private MultiScanInner activity;
    private static final int SIGN_CODE = 1;

    private String encodedString="",encodedString1="";
    private static final int REQUEST_CODE = 99;
    private static final int REQUEST_CODE1 = 98;
    private final int ZBAR_SCANNER_REQUEST = 100;
    private LayoutInflater mLayoutInflater;

    private boolean checkCartonNumberFlag = true;
    private boolean checkBackPressFlag = false;
    private boolean MasterCompanyEditable = true;
    public static final int MEDIA_TYPE_IMAGE = 1;
    boolean ScanBarcode = false;
    boolean is_match=false;
    public static Uri fileUri;
    DatabaseHandler database;
    JSONObject UploadData = new JSONObject();
    JSONArray UploadArrayData = new JSONArray();

    private static final String IMAGE_DIRECTORY_NAME = "Camera";
    View tempView;
    TextView txtCompany;
    Dialog mDialogRowBoardList;
    String path;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    ImageView img_main,back;
    TextView title;
    ProgressBar progressBar;
    Bitmap resizedBitmap;
    private int mCartoonCnt;
    private int mSelectedPos;
    private int mSelectedPosOfBarcode;
    private List<EditText> mCartoons;
    private List<TextView> mCartoonsText;
    private List<Spinner> mCartoonsType;
    private List<String> mBarcodeImage;
    private List<String> mBarcodeImageValue;
    ArrayList<Co_type> co_type_list=new ArrayList<>();
    String db_carton_num="",db_ass1="",db_ass2="",db_co_type="",old_timestamp="";
    boolean isCaptureCarton=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        database = DatabaseHandler.getInstance(this);

        mLayoutInflater = LayoutInflater.from(this);

        txtCompany = (TextView) findViewById(R.id.txtCompany);

        imgLock.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

            }
        });
        if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {

/**
 * this is api for master company one and if master company one then must scan document .
 */
            if (Util.isNetAvailable(activity)) {
                new GetMustScanCoType(activity, false, new ICallback() {

                    @Override
                    public void run(Object result) {
                        // TODO Auto-generated method stub
                        try {
                            Log.e("GetMustScanCoType", "" + result.toString());
                            JSONObject object = new JSONObject(result.toString());
                            JSONObject jsonObject = object.getJSONObject("data");
                            // Log.e("jsonObject",""+jsonObject);
                            JSONArray array = jsonObject.getJSONArray("co_type");

                            Co_type[] categories = new Co_type[array.length()];

                            co_type_list.clear();
                            if(database.get_CO_TYPE().size()>0)
                            {
                                database.deleteMUST_CO_TYPE();
                            }
                            for (int counter = 0; counter < array.length(); counter++) {
                                categories[counter] = new Co_type();
                                categories[counter].setCo_type_name(array.getString(counter).toString());
                                co_type_list.add(categories[counter]);

                                Log.e("co_type_list", "" + co_type_list.size() + "   " + array.length());
                                if (!database.get_CO_TYPE().contains(array.getString(counter).toString())) {
                                    database.addCo_type(array.getString(counter).toString());
                                }

                                //  co_type_list.add(array.getString(counter).toString());

                                // Log.e("array",""+array.getString(counter).toString()+"  "+co_type_list);
                            }

                            //databaseHandler.deleteCompanyRejection();
                       /* for (int counter = 0; counter < array.length(); counter++) {
                            CompanyRejection rejection = new CompanyRejection();
                            rejection.setMaster_company_id(array.getJSONObject(counter).getString("master_company_id"));
                            rejection.setEditing(array.getJSONObject(counter).getString("editing"));
                          //  databaseHandler.addCompanyRejection(rejection);
                        }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).execute();
            }
        }
        btnLock.setOnLongClickListener(new OnLongClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Util.hideKeyboard(activity, v);
                if (imgLock.getVisibility() == View.GONE) {
                    imgLock.setVisibility(View.VISIBLE);
                    txtLock.setVisibility(View.VISIBLE);
                    btnLock.setBackground(getResources().getDrawable(R.drawable.lock_icon));
                } else {
                    imgLock.setVisibility(View.GONE);
                    txtLock.setVisibility(View.GONE);
                    btnLock.setBackground(getResources().getDrawable(R.drawable.unlock_icon));
                }
                return false;
            }
        });

        btnSavedetail_2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // prepareUpload(login.getDriverID());

                if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {

                    OrderDeliver orderDeliver = new OrderDeliver();
                    orderDeliver.setOrderDeliverCartonNum("");
                    orderDeliver.setOrderDeliverAss1("");
                    orderDeliver.setOrderDeliverAss2("");
                    orderDeliver.setOrderDeliverCoType("");
                    orderDeliver.setOrderDeliverTimestamp("");

                    database.addOrderDeliverLabel(orderDeliver);
                    Cachedjob cachedjob = getData();
                    Cursor cursor = database.getORDER_DELIVER(mCartoonsText.get(0).getText().toString(), cachedjob.getChg1(), cachedjob.getChg2(), Pref.getValue(MultiScanInner.this, "co_typek", ""));
                    if (cursor.moveToFirst() && cursor.getCount() > 0) {
                        Log.e("time_stamp", "" + cursor.getString(cursor.getColumnIndex("time_stamp")));
                        cursor.moveToFirst();

                        // Log.e("curre_timestamp",""+ts);
                        old_timestamp = cursor.getString(cursor.getColumnIndex("time_stamp"));
                        db_carton_num = cursor.getString(cursor.getColumnIndex("carton_num"));
                        db_ass1 = cursor.getString(cursor.getColumnIndex("ass1"));
                        db_ass2 = cursor.getString(cursor.getColumnIndex("ass2"));
                        db_co_type = cursor.getString(cursor.getColumnIndex("co_type"));

                    }
                    Long tsLong = System.currentTimeMillis() / 1000;
                    String ts = tsLong.toString();
                    Log.e("ts", "" + ts);
                    Log.e("old_timestamp", "" + old_timestamp);
                    if (!old_timestamp.equalsIgnoreCase("")) {
                        if (Long.parseLong(ts) - Long.parseLong(old_timestamp) > 86400) {

                            database.deleteOlder1Day(cursor.getString(cursor.getColumnIndex("carton_num")));

                            Cursor cursor1 = database.getORDER_DELIVER(mCartoonsText.get(0).getText().toString(), cachedjob.getChg1(), cachedjob.getChg2(), Pref.getValue(MultiScanInner.this, "co_typek", ""));
                            Log.e("count", "" + cursor.getCount());
                            if (cursor1.moveToFirst() && cursor1.getCount() > 0) {
                                Log.e("time_stamp1", "" + cursor1.getString(cursor1.getColumnIndex("time_stamp")));
                                cursor1.moveToFirst();
                                old_timestamp = cursor1.getString(cursor1.getColumnIndex("time_stamp"));
                                db_carton_num = cursor1.getString(cursor1.getColumnIndex("carton_num"));
                                db_ass1 = cursor1.getString(cursor1.getColumnIndex("ass1"));
                                db_ass2 = cursor1.getString(cursor1.getColumnIndex("ass2"));
                                db_co_type = cursor1.getString(cursor1.getColumnIndex("co_type"));

                            }
                        } else {
                            Log.e("delete", "delete");
                        }
                    }
                    /**
                     * check in database and after check i add entry in db and order .
                     */
                    if (!db_carton_num.equalsIgnoreCase("") && !db_ass1.equalsIgnoreCase("") && !db_ass2.equalsIgnoreCase("") && !db_co_type.equalsIgnoreCase("")) {
                        if (db_carton_num.equalsIgnoreCase(mCartoonsText.get(0).getText().toString()) && db_ass1.equalsIgnoreCase(cachedjob.getChg1()) && db_ass2.equalsIgnoreCase(cachedjob.getChg2()) && db_co_type.equalsIgnoreCase(Pref.getValue(MultiScanInner.this, "co_typek", ""))) {
                            Toast.makeText(MultiScanInner.this, "You may not submit the same accessorial for this order twice, please un-selected them", Toast.LENGTH_LONG).show();
                        } else {


                            int flag = 0;
                            UploadData = new JSONObject();
                            UploadArrayData = new JSONArray();

                            for (int i = 0; i < mCartoonsType.size(); i++) {
                                if (mBarcodeImage.get(i).length() < 1 && mBarcodeImageValue.get(i).equals("Rejected")) {
                                    if(isCaptureCarton) {
                                        flag = 1;
                                        Toast.makeText(activity, "Please capture photo of all cartons", Toast.LENGTH_LONG).show();
                                        break;
                                    }
                                }/*else if(mBarcodeImageValue.get(i).equals("edix")||mBarcodeImageValue.get(i).equals("front")||mBarcodeImageValue.get(i).equals("clear")) {
                            if (encodedString1.equalsIgnoreCase("")) {
                                L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.multiscan_targascan_errormsg_document).toString());

                            }
                        }
*/
                                try {
                                    if(isCaptureCarton) {
                                        if (mBarcodeImageValue.get(i).equals("Rejected")) {
                                            JSONObject object = new JSONObject();
                                            object.put("driver_id", database.getContact().getDriverID());
                                            object.put("carton_num", mCartoonsText.get(i).getText().toString());
                                            object.put("barcode", mBarcodeImage.get(i));
                                            object.put("cust_name", editsignature.getText());
                                            UploadArrayData.put(object);
                                        }else {
                                            try {
                                                UploadData.put(mCartoonsText.get(i).getText().toString(), mCartoonsType.get(i).getSelectedItem()
                                                        .toString());
                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }
                                        }
                                    } else {
                                        try {
                                            UploadData.put(mCartoonsText.get(i).getText().toString(), mCartoonsType.get(i).getSelectedItem()
                                                    .toString());
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            if (flag == 1) {

                                return;
                            }

                            if (Util.isNetAvailable(activity)) {
                                Login login = database.getContact();
                                if (login == null) {
                                    L.confirmDialog(activity, activity.getResources().getString(R.string.cach_job_confirm), "Yes", "No",
                                            new L.IL() {

                                                @Override
                                                public void onSuccess() {
                                                    doCachedJob();

                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                } else {
                                    prepareUploadRouteLabel(database.getContact().getDriverID());
                                    // prepareUpload(login.getDriverID());
                                }
                            } else {
                                doCachedJob();
                                // database.deleteContact();
                            }



                        }
                    } else {


                        int flag = 0;
                        UploadData = new JSONObject();
                        UploadArrayData = new JSONArray();

                        for (int i = 0; i < mCartoonsType.size(); i++) {
                            if (mBarcodeImage.get(i).length() < 1 && mBarcodeImageValue.get(i).equals("Rejected")) {
                                if(isCaptureCarton) {
                                    Toast.makeText(activity, "Please capture photo of all cartons", Toast.LENGTH_LONG).show();
                                    flag = 1;
                                    break;
                                }


                            }/*else if(mBarcodeImageValue.get(i).equals("edix")||mBarcodeImageValue.get(i).equals("front")||mBarcodeImageValue.get(i).equals("clear")) {
                            if (encodedString1.equalsIgnoreCase("")) {
                                L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.multiscan_targascan_errormsg_document).toString());

                            }
                        }
*/
                            try {
                                if(isCaptureCarton)
                                {
                                    if (mBarcodeImageValue.get(i).equals("Rejected")) {
                                        JSONObject object = new JSONObject();
                                        object.put("driver_id", database.getContact().getDriverID());
                                        object.put("carton_num", mCartoonsText.get(i).getText().toString());
                                        object.put("barcode", mBarcodeImage.get(i));
                                        object.put("cust_name", editsignature.getText());
                                        UploadArrayData.put(object);
                                    }else {
                                        try {
                                            UploadData.put(mCartoonsText.get(i).getText().toString(), mCartoonsType.get(i).getSelectedItem()
                                                    .toString());
                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    try {
                                        UploadData.put(mCartoonsText.get(i).getText().toString(), mCartoonsType.get(i).getSelectedItem()
                                                .toString());
                                    } catch (JSONException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        if (flag == 1) {
                            return;
                        }

                        if (Util.isNetAvailable(activity)) {
                            Login login = database.getContact();
                            if (login == null) {
                                L.confirmDialog(activity, activity.getResources().getString(R.string.cach_job_confirm), "Yes", "No",
                                        new L.IL() {

                                            @Override
                                            public void onSuccess() {
                                                doCachedJob();

                                            }

                                            @Override
                                            public void onCancel() {

                                            }
                                        });
                            } else {
                                prepareUploadRouteLabel(database.getContact().getDriverID());
                                // prepareUpload(login.getDriverID());
                            }
                        } else {
                            doCachedJob();
                            // database.deleteContact();
                        }



                    }


//				Log.e("Dhims", "UploadData: " + UploadData.toString());
//				Log.e("Dhims", "UploadArrayData Size: " + UploadArrayData.length());
//				Log.e("Dhims", "UploadArrayData: " + UploadArrayData.toString());
                }else
                {

                    if (MasterCompanyEditable) {

                        int flag = 0;
                        UploadData = new JSONObject();
                        UploadArrayData = new JSONArray();

                        for (int i = 0; i < mCartoonsType.size(); i++) {
                            if (mCartoonsType.get(i).getSelectedItem().equals("Unknown")) {
                                Toast.makeText(activity, "Please select company", Toast.LENGTH_LONG).show();
                                flag = 1;
                                break;
                            }
                            try {
                                Pref.setValue(MultiScanInner.this, "co_typek", mCartoonsType.get(i).getSelectedItem().toString());
                                UploadData.put(mCartoonsText.get(i).getText().toString(), mCartoonsType.get(i).getSelectedItem().toString());
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        if (flag == 1) {
                            return;
                        }

                        if (Util.isNetAvailable(activity)) {
                            Login login = database.getContact();
                            if (login == null) {
                                L.confirmDialog(activity, activity.getResources().getString(R.string.cach_job_confirm), "Yes", "No",
                                        new L.IL() {

                                            @Override
                                            public void onSuccess() {

                                                doCachedJob();
                                            }

                                            @Override
                                            public void onCancel() {

                                            }
                                        });
                            } else {
                                prepareUpload(login.getDriverID());

                            }
                        } else {
                            doCachedJob();

                            // database.deleteContact();
                        }

                    }

                }
            }
        });

        btnSavedetail.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isvalidate()) {

                    Util.hideKeyboard(activity, btnSavedetail);
                    checkBackPressFlag = false;

                    if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {

                        try {

                            Cursor cursor = database.getCompanyRejection(SharedPrefrenceUtil.getPrefrence(activity,
                                    Constants.PREF_MASTER_COMPANY_KEY, "0"));

                            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                                if (cursor.getString(cursor.getColumnIndex("editing")).equals("0")) {
                                    MasterCompanyEditable = false;
                                } else {
                                    MasterCompanyEditable = true;
                                }
                            }

                            if (cursor != null) {
                                cursor.close();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        MasterCompanyEditable = true;
                    }
                       /* }else {
                            L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                                    getResources().getText(R.string.not_match).toString());
                        }*/
                    // }
                    if (MasterCompanyEditable) {
                        txtCompany.setText("Confirm Company ID");
                    } else {
                        txtCompany.setText("Confirm Cartons");
                    }

                    checkCartonNumber();
                /*    List<String> DailyCo_TYPE_List = database.get_CO_TYPE();
                    List<String> DailyCompany = database.getAllCompany();
                    Log.e("DailyCo_TYPE_List",""+DailyCo_TYPE_List.size());
                    Log.e("DailyCompany",""+DailyCompany.size());
                    for(int co=0;co<DailyCo_TYPE_List.size();co++)
                    {
                        for(int co1=0;co1<DailyCompany.size();co1++)

                        {
                            if(DailyCo_TYPE_List.get(co).equalsIgnoreCase(DailyCompany.get(co1)))
                            {
                                Log.e("match","match"+co);
                                if()
                            }

                        }
                    }
*/

                    // if (Util.isNetAvailable(activity)) {
                    // checkBackPressFlag = false;
                    // checkCartonNumber();
                    // Login login = database.getContact();
                    // if (login == null) {
                    // L.confirmDialog(activity,
                    // activity.getResources().getString(R.string.cach_job_confirm),
                    // "Yes", "No",
                    // new L.IL() {
                    //
                    // @Override
                    // public void onSuccess() {
                    // doCachedJob();
                    // }
                    //
                    // @Override
                    // public void onCancel() {
                    //
                    // }
                    // });
                    // } else {
                    // prepareUpload(login.getDriverID());
                    // }
                    // } else {
                    // doCachedJob();
                    // database.deleteContact();
                    // }
                }

            }

        });

        imgSignature.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, SignatureActivity.class);
                activity.startActivityForResult(i, SIGN_CODE);
            }
        });

        imgDocumentScan.setOnClickListener(new ScanButtonClickListener(ScanConstants.OPEN_CAMERA));

            /*
         * btnSacn.setOnClickListener(new OnClickListener() {
		 *
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(activity, ZBarScannerActivity.class);
		 * startActivityForResult(intent, ZBAR_SCANNER_REQUEST); } });
		 */

		/*
         * if(Util.isNetAvailable(activity)) {
		 *
		 * }else{ prepareSpinner(); }
		 */

        MultiScanDataAsync async = new MultiScanDataAsync(activity, new ICallback() {

            @Override
            public void run(Object result) {
                prepareSpinner();
            }
        });

        async.execute();

        spinChg1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (!spinChg1.getSelectedItem().toString().trim().equals("None")) {
                    if (spinChg1.getSelectedItem().toString().equalsIgnoreCase(spinChg2.getSelectedItem().toString())) {

                        L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                                getResources().getText(R.string.not_match).toString());


                    } else {
                        try {
                            AssetFileDescriptor afd = getAssets().openFd("Cha-Ching.mp3");
                            MediaPlayer player = new MediaPlayer();
                            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        spinChg2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (!spinChg2.getSelectedItem().toString().trim().equals("None")) {
                    if (spinChg1.getSelectedItem().toString().equalsIgnoreCase(spinChg2.getSelectedItem().toString())) {

                        L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                                getResources().getText(R.string.not_match).toString());
                    } else {
                        try {
                            AssetFileDescriptor afd = getAssets().openFd("Cha-Ching.mp3");
                            MediaPlayer player = new MediaPlayer();
                            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        addCartoon(false);

    }
    private class ScanButtonClickListener implements View.OnClickListener {

        private int preference;

        public ScanButtonClickListener(int preference) {
            this.preference = preference;
        }

        public ScanButtonClickListener() {
        }

        @Override
        public void onClick(View v) {
            if(Pref.getValue(MultiScanInner.this,"Selected_url","").equalsIgnoreCase("YCOL")) {
                if (encodedString1.equalsIgnoreCase("")) {
                    if(Pref.getValue(MultiScanInner.this,"auto_scan","").equalsIgnoreCase("1"))
                    {
                        startScan(preference);
                    }else
                    {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CODE1);
                            } else {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                // Log.v("fileUri",fileUri+"--");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                // start the image capture Intent
                                startActivityForResult(intent, REQUEST_CODE1);

                            }
                        } else {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            // Log.v("fileUri",fileUri+"--");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            // start the image capture Intent
                            startActivityForResult(intent, REQUEST_CODE1);

                        }

                      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //   intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                        startActivityForResult(intent, REQUEST_CODE1);*/
                    }

                } else {
                    showInputDialog(preference);

                }
            }else
            {
                L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.Only_Ycol).toString());
            }

        }
    }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }
    protected void showInputDialog(final int preference) {

        // get prompts.xml view
       /* LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.manage_gladiator_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);
        final AlertDialog alert = alertDialogBuilder.create();*/
        mDialogRowBoardList = new Dialog(activity);
        mDialogRowBoardList.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialogRowBoardList.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mDialogRowBoardList.setContentView(R.layout.document_add_dialod_layout);
        mDialogRowBoardList.setCancelable(false);
        mWindow = mDialogRowBoardList.getWindow();
        mLayoutParams = mWindow.getAttributes();
        mLayoutParams.gravity = Gravity.BOTTOM;
        mWindow.setAttributes(mLayoutParams);
        final TextView txt_preview = (TextView)mDialogRowBoardList.findViewById(R.id.txt_preview);
        final TextView txt_clear = (TextView) mDialogRowBoardList.findViewById(R.id.txt_clear);
        final TextView txt_rescan= (TextView) mDialogRowBoardList.findViewById(R.id.txt_rescan);
        final Button cancel = (Button) mDialogRowBoardList.findViewById(R.id.cancel);

        txt_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog listDialog = new Dialog(activity);
                LayoutInflater li = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                listDialog.setContentView(R.layout.doc_file_image_layout);
                listDialog.setCanceledOnTouchOutside(true);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = listDialog.getWindow();
                lp.copyFrom(window.getAttributes());
                DisplayMetrics displaymetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;
                lp.width =width-100 ;
                lp.height = height-200;
                window.setAttributes(lp);

                listDialog.getWindow().setGravity(Gravity.CENTER);
                listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                back = (ImageView)listDialog.findViewById(R.id.back);
                img_main = (ImageView)listDialog.findViewById(R.id.img_main);
                progressBar=(ProgressBar)listDialog.findViewById(R.id.progressBar);
                if(resizedBitmap!=null)
                {
                    img_main.setImageBitmap(resizedBitmap);
                }


                // progressBar.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listDialog.dismiss();
                    }
                });
                listDialog.show();
                mDialogRowBoardList.dismiss();
            }
        });

        txt_rescan.setOnClickListener(new View.OnClickListener() {

                                          @Override
                                          public void onClick(View v) {
                                              if(Pref.getValue(MultiScanInner.this,"auto_scan","").equalsIgnoreCase("1"))
                                              {
                                                  startScan(preference);
                                              }else
                                              {
                                                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                  //   intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                                  startActivityForResult(intent, REQUEST_CODE1);
                                              }
                                              mDialogRowBoardList.dismiss();
                                          }
                                      }
        );
        txt_clear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                encodedString1="";
                imgDocumentScan.setImageResource(R.drawable.scan_document);
                mDialogRowBoardList.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {

                                          mDialogRowBoardList.dismiss();
                                      }
                                  }

        );

        mDialogRowBoardList.show();
    }
    protected void startScan(int preference) {
        Intent intent = new Intent(this, ScanActivity.class);
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (checkBackPressFlag == true) {
            super.onBackPressed();
        } else {
            checkBackPressFlag = true;
            checkCartonNumberFlag = true;
            rootScrollView.setVisibility(View.VISIBLE);
            rootScrollView_2.setVisibility(View.GONE);
        }
    }

    protected void checkCartonNumber() {
        rootScrollView.setVisibility(View.GONE);
        rootScrollView_2.setVisibility(View.VISIBLE);

        custom_lin.removeAllViews();

        TextView title = new TextView(activity);
        title.setText("Confirm Company ID");
        title.setTextSize(30);
        title.setTextColor(Color.BLACK);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 50, 0, 50);
        title.setVisibility(View.GONE);

        custom_lin.addView(title);
        custom_lin.addView(btnSavedetail_2);

        String str = getCartoons();
        try {
            JSONObject jsonObject = new JSONObject(str);
            Iterator<?> keys = jsonObject.keys();

            int i = 1;
            mCartoonsText = new ArrayList<TextView>();
            mCartoonsType = new ArrayList<Spinner>();
            mBarcodeImage = new ArrayList<String>();
            mBarcodeImageValue = new ArrayList<String>();

            custom_lin.setPadding(20, 50, 20, 50);

            final List<String> cmpList = database.getAllCompany();
            cmpList.remove("Select Company");
            cmpList.add(0, "Unknown");

            final List<DailyOrder> DailyOrderList = database.getAllDailyOrder();
            List<String> DailyOrderCartonNunberList = database.getAllCartonNumber();

            List<String> DailyOrderCartonCoTypeList = database.getAllCartonCoType();
            List<String> remainingCartonNumber = new ArrayList<String>();


            for (int j = 0; j < DailyOrderCartonCoTypeList.size(); j++) {
                if (!cmpList.contains(DailyOrderCartonCoTypeList.get(j))) {
                    cmpList.add(DailyOrderCartonCoTypeList.get(j));
                }
            }

            while (keys.hasNext()) {
                String key = (String) keys.next();
                TextView textView = new TextView(MultiScanInner.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(25);
                textView.setPadding(10, 10, 10, 10);
                textView.setText(jsonObject.getString(key));
                custom_lin.addView(textView, i);
                textView.setId(i);
                mCartoonsText.add(textView);

                Spinner spinner = new Spinner(MultiScanInner.this);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, cmpList);
                spinner.setId(i + 1);
                spinner.setAdapter(arrayAdapter);
                mCartoonsType.add(spinner);

                if (!DailyOrderCartonNunberList.contains(jsonObject.getString(key))) {
                    remainingCartonNumber.add(jsonObject.getString(key));
                }

                for (int counter = 0; counter < DailyOrderList.size(); counter++) {
                    if (DailyOrderList.get(counter).getCartonNumText().endsWith(jsonObject.getString(key))) {
                        if (cmpList.contains(DailyOrderList.get(counter).getCoTypeText())) {
                            spinner.setSelection(cmpList.indexOf(DailyOrderList.get(counter).getCoTypeText()));
                            Pref.setValue(MultiScanInner.this,"co_typek",cmpList.indexOf(DailyOrderList.get(counter).getCoTypeText()));

                        }
                        break;
                    }
                }


                // EditText editText = new
                // EditText(MultiScanInner.this);
                // for(DailyOrder order : DailyOrderList){
                // if(order.getCartonNumText().equals(jsonObject.getString(key))){
                // editText.setText(order.getCoTypeText());
                // break;
                // }
                // }
                // custom_lin.addView(editText, i + 1);
                // custom_lin.addView(spinner, i + 1);

                final View mView = getLayoutInflater().inflate(R.layout.row_cartoons_type, null);
                mView.setId(i + 1);
                TextView scan = (TextView) mView.findViewById(R.id.txtScan);
                TextView scanData = (TextView) mView.findViewById(R.id.txtScanData);

                try {
                    Cursor cursor = database.getCartonNunber(jsonObject.getString(key));
                    Log.e("Dhims", "" + DatabaseUtils.dumpCursorToString(cursor));
                    if (cursor.moveToFirst() && cursor.getCount() > 0) {
                        Log.e("cotype",""+cursor.getString(cursor.getColumnIndex("co_type")));
                        Pref.setValue(MultiScanInner.this,"co_typek",cursor.getString(cursor.getColumnIndex("co_type")));
                        cursor.moveToFirst();
                        if (cursor.getString(cursor.getColumnIndex("co_type")).equalsIgnoreCase("Unknown")) {
                            scanData.setText("Rejected");
                            mBarcodeImageValue.add("Rejected");
                            isCaptureCarton=false;
                        }/*else if(cursor.getString(cursor.getColumnIndex("co_type")).equalsIgnoreCase("edix")||cursor.getString(cursor.getColumnIndex("co_type")).equalsIgnoreCase("front")||cursor.getString(cursor.getColumnIndex("co_type")).equalsIgnoreCase("clear")){
                            mBarcodeImageValue.add(cursor.getString(cursor.getColumnIndex("co_type")));
                           // mBarcodeImageValue.add("Accepted");
                        } */else {
                            scanData.setText("Accepted");
                            scan.setVisibility(View.GONE);
                            mBarcodeImageValue.add("Accepted");
                        }
                    } else {
                        scanData.setText("Rejected");
                        mBarcodeImageValue.add("Rejected");
                        isCaptureCarton=false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // if
                // (DailyOrderCartonNunberList.contains(jsonObject.getString(key)))
                // {
                // scanData.setText("Accepted");
                // } else {
                // scanData.setText("Rejected");
                // }

                scan.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // Log.e("Dhims", "" + mView.getId());
                        mSelectedPosOfBarcode = mView.getId();
                        tempView = mView;
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                        File output = new File(dir, "lhp_camera.png");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
                        path = output.getAbsolutePath();
                        startActivityForResult(cameraIntent, mSelectedPosOfBarcode);
                    }
                });
                mBarcodeImage.add("");
                // custom_lin.addView(mView, i + 1);

                /**
                 * ask for select company and not to need cartoon photo when rejected then ask for capture cartoon number
                 */
                if (MasterCompanyEditable) {
                    custom_lin.addView(spinner, i + 1);

                    isCaptureCarton=false;
                } else {
                    custom_lin.addView(mView, i + 1);
                    isCaptureCarton=true;
                }

                i = i + 2;
            }

            if (Util.isNetAvailable(activity) && remainingCartonNumber.size() > 0 && checkCartonNumberFlag == true) {
                checkCartonNumberFlag = false;
                new CheckCartonNumberAsync(activity, true, remainingCartonNumber, new ICallback() {

                    @Override
                    public void run(Object result) {
                        // TODO Auto-generated method stub
                        checkCartonNumber();
                    }
                }).execute();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void prepareSpinner() {

        try {
            spinChg1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, database.getAllChg()));
            spinChg2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, database.getAllChg()));

            spinRedelivery.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, database.getAllRedeliver()));

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,
                    database.getAllCompany());
            spinCompanyid.setAdapter(arrayAdapter);

            if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equals("YCOL")) {
                spinCompanyid.setSelection(1);
            } else {
                spinCompanyid.setSelection(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Cachedjob getData() {
        Cachedjob cachedjob = new Cachedjob();
        try {
            String signaturename = editsignature.getText().toString().trim();
            String companyid = spinCompanyid.getSelectedItem().toString().trim();
            String chg1 = spinChg1.getSelectedItem().toString().trim();
            String chg2 = spinChg2.getSelectedItem().toString().trim();
            String cartoon1 = getCartoons();
            String redelivery = spinRedelivery.getSelectedItem().toString().trim();
            String to = editto.getText().toString().trim();
            String from = editfrom.getText().toString().trim();

            cachedjob.setSignatureName(signaturename);
            cachedjob.setCompanyID(companyid);
            cachedjob.setChg1(chg1);
            cachedjob.setChg2(chg2);
            // cachedjob.setCartoon1(cartoon1);
            cachedjob.setCartoon1(UploadData.toString());
            cachedjob.setRedelivery(redelivery);
            cachedjob.setTo(to);
            cachedjob.setFrom(from);

            cachedjob.setCachedDate(Util.getCurrentTime() + "");
            cachedjob.setSystem(Constants.SYSTEM_VALUE);
            cachedjob.setServId(Constants.SERVID_VALUE);

            cachedjob.setEncodeSign(encodedString);
            if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {
                cachedjob.setDocumentData(encodedString1);
            }
            Log.e("encodedString1",""+encodedString1);

            SharedPrefrenceUtil.setPrefrence(activity, Constants.PREF_SELECTED_ITEM, spinCompanyid.getSelectedItemPosition());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (int counter = 0; counter < UploadArrayData.length(); counter++) {
                try {
                    DriverLabel driverLabel = new DriverLabel();
                    driverLabel.setDriver_id(UploadArrayData.getJSONObject(counter).getString("driver_id"));
                    driverLabel.setCarton_num(UploadArrayData.getJSONObject(counter).getString("carton_num"));
                    driverLabel.setBarcode(UploadArrayData.getJSONObject(counter).getString("barcode"));
                    driverLabel.setCust_name(UploadArrayData.getJSONObject(counter).getString("cust_name"));
                    database.addDriverLabel(driverLabel);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cachedjob;
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean isvalidate() {
        /*
         * if(isEmpty(editsignature) && isEmpty(editto) && isEmpty(editfrom) ) {
		 * L.alert(activity,
		 * getResources().getText(R.string.multiscan_targascan_title
		 * ).toString(),
		 * getResources().getText(R.string.multiscan_targascan_errormsg_alldetail
		 * ).toString());
		 *
		 * return false; }else {
		 */
        if ((encodedString == null || encodedString.length() == 0)
                && spinRedelivery.getSelectedItem().toString().trim().equals("Completed")) {
            L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.multiscan_targascan_errormsg_sign).toString());
            return false;
        } else if (isEmpty(editsignature) && spinRedelivery.getSelectedItem().toString().trim().equals("Completed")) {
            L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                    getResources().getText(R.string.multiscan_targascan_errormsg_signaturename).toString());
            return false;
        } else if (!validateCartoon()) {
            L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                    getResources().getText(R.string.multiscan_targascan_errormsg_cartoon1).toString());
            return false;
        }else if(!spinChg1.getSelectedItem().toString().isEmpty()&&!spinChg2.getSelectedItem().toString().isEmpty()) {

            if (!spinChg1.getSelectedItem().toString().trim().equals("None") && !spinChg2.getSelectedItem().toString().trim().equals("None")) {
                if (spinChg1.getSelectedItem().toString().equalsIgnoreCase(spinChg2.getSelectedItem().toString())) {

                    L.alert(activity, getResources().getText(R.string.multiscan_targascan_title).toString(),
                            getResources().getText(R.string.not_match).toString());

                    return false;
                }
            }
        }
        /*
         * else if(isEmpty(editto)) { L.alert(activity,
		 * getResources().getText(R.
		 * string.multiscan_targascan_title).toString(),
		 * getResources().getText(R.
		 * string.multiscan_targascan_errormsg_to).toString()); return false;
		 * }else if(isEmpty(editfrom)) { L.alert(activity,
		 * getResources().getText
		 * (R.string.multiscan_targascan_title).toString(),
		 * getResources().getText
		 * (R.string.multiscan_targascan_errormsg_from).toString()); return
		 * false; }
		 */

        // }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == mSelectedPosOfBarcode && ScanBarcode == false) {

            try {

                Bitmap bp = BitmapFactory.decodeFile(path.toString(), null);

                bp = Bitmap.createScaledBitmap(bp, 500, 500, true);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bp.compress(Bitmap.CompressFormat.JPEG, 50, baos);

                ImageView img = (ImageView) tempView.findViewById(R.id.imgScan);
                img.setImageBitmap(Bitmap.createScaledBitmap(bp, 50, 50, true));
                TextView txt = (TextView) tempView.findViewById(R.id.txtScanData);
                // txt.setVisibility(View.INVISIBLE);

                // added lines
                // bp.recycle();
                // bp = null;
                // added lines
                byte[] b = baos.toByteArray();

                mBarcodeImage.set((mSelectedPosOfBarcode / 2) - 1, Base64.encodeToString(b, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK && requestCode == mSelectedPos && ScanBarcode == true) {
            try {
                mCartoons.get(mSelectedPos).setText(data.getStringExtra("result"));
                if (mSelectedPos == mCartoonCnt - 1) {
                    addCartoon(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (data != null && data.hasExtra("sign")) {
            byte[] signData = data.getByteArrayExtra("sign");
            if (signData != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(signData, 0, signData.length);
                int width = 640;
                int height = 240;
                try {
                    imgSignature.buildDrawingCache();
                    Bitmap bmap = imgSignature.getDrawingCache();

                    width = bmap.getWidth();
                    height = bmap.getHeight();
                } catch (Exception e) {
                }

                try {
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                    imgSignature.setImageBitmap(resizedBitmap);
                    if(encodedString1.equalsIgnoreCase("")) {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.scan_document);
                        Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(icon, width, height, false);
                        imgDocumentScan.setImageBitmap(resizedBitmap1);
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if(data!=null && data.hasExtra("scannedResult"))
        {

            final int destHeight = 800;
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {

                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options. inSampleSize = 4;

                options. inPurgeable = true ;

                options.inJustDecodeBounds = false;

                // bitmap = BitmapFactory.decodeFile(ResultFragment.fileUri.getPath(),options);
                bitmap = ResultFragment.bitmap;
                //   bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                //   fileUri=  Utils.getUri(MultiScanInner.this, bitmap);
                //  bitmap = BitmapFactory.decodeFile(uri.getPath(),options);
                // getContentResolver().delete(uri, null, null);
                // imgDocumentScan.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 50, 50, true));
                int width = 100;
                int height = 100;
                imgDocumentScan.buildDrawingCache();
                Bitmap bmap = imgDocumentScan.getDrawingCache();

                width = bmap.getWidth();
                height = bmap.getHeight();
                Log.e("width",""+width);
                Log.e("height",""+height);
                resizedBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                int width1 = 640;
                int height1 = 240;
                try {
                    imgSignature.buildDrawingCache();
                    Bitmap bmap1 = imgSignature.getDrawingCache();

                    width1 = bmap1.getWidth();
                    height1 = bmap1.getHeight();
                } catch (Exception e) {
                }


                Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(bitmap, width1, height1, false);
                imgDocumentScan.setImageBitmap(resizedBitmap1);


                int desWidth = destHeight/ bitmap.getWidth();
                //   Bitmap resizedBitmap3 = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth()*desWidth, destHeight, false);


                //    Bitmap resizedBitmap3 = BITMAP_RESIZER(bitmap,desWidth*bitmap.getWidth(),destHeight);
                //   Bitmap resizedBitmap3 = Bitmap.createScaledBitmap(bitmap, 1200, 1200, false);
                final int REQUIRED_SIZE = 200;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(REQUIRED_SIZE);
                //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encodedString1 = Base64.encodeToString(byteArray, Base64.DEFAULT);

                if(encodedString.equalsIgnoreCase("")) {
                    if (!encodedString1.equalsIgnoreCase("")) {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.sign_pic);
                        Bitmap resizedBitmap2 = Bitmap.createScaledBitmap(icon, width, height, false);
                        imgSignature.setImageBitmap(resizedBitmap2);
                    }
                }
                Log.e("encodedString1",""+encodedString1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(resultCode == RESULT_OK && requestCode ==REQUEST_CODE1)
        {
            final int destHeight = 800;
            Bitmap thumbnail=null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options. inSampleSize = 4;

                options. inPurgeable = true ;

                options.inJustDecodeBounds = false;

                thumbnail = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);
                //  thumbnail = (Bitmap) data.getExtras().get("data");

                int width = 100;
                int height = 100;
                imgDocumentScan.buildDrawingCache();
                Bitmap bmap = imgDocumentScan.getDrawingCache();

                width = bmap.getWidth();
                height = bmap.getHeight();
                resizedBitmap = Bitmap.createScaledBitmap(thumbnail, width, height, true);
                int width1 = 640;
                int height1 = 240;
                try {
                    imgSignature.buildDrawingCache();
                    Bitmap bmap1 = imgSignature.getDrawingCache();

                    width1 = bmap1.getWidth();
                    height1 = bmap1.getHeight();
                } catch (Exception e) {
                }


                Bitmap resizedBitmap1 = Bitmap.createScaledBitmap(thumbnail, width1, height1, false);
                imgDocumentScan.setImageBitmap(resizedBitmap1);



                int desWidth = destHeight/ thumbnail.getWidth();
                Log.e("desWidth",""+desWidth);
                //  Bitmap resizedBitmap3 = BITMAP_RESIZER(thumbnail,thumbnail.getWidth(),thumbnail.getHeight());

                final int REQUIRED_SIZE = 200;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(REQUIRED_SIZE);
                //  ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                encodedString1 = Base64.encodeToString(byteArray, Base64.DEFAULT);



                if(encodedString.equalsIgnoreCase("")) {
                    if (!encodedString1.equalsIgnoreCase("")) {
                        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                                R.drawable.sign_pic);
                        Bitmap resizedBitmap2 = Bitmap.createScaledBitmap(icon, width, height, false);
                        imgSignature.setImageBitmap(resizedBitmap2);
                    }
                }

            }  catch (Exception e) {
                e.printStackTrace();
            }
        }


        ScanBarcode = false;

    }
    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    /**
     * add order in database for not add same order with in 1  day
     */
    private void add_db_order_deliver_1_day()
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        Cachedjob cachedjob = getData();
        OrderDeliver orderDeliver = new OrderDeliver();
        orderDeliver.setOrderDeliverCartonNum(mCartoonsText.get(0).getText().toString());
        orderDeliver.setOrderDeliverAss1(cachedjob.getChg1());
        orderDeliver.setOrderDeliverAss2(cachedjob.getChg2());
        orderDeliver.setOrderDeliverCoType(Pref.getValue(MultiScanInner.this,"co_typek",""));
        orderDeliver.setOrderDeliverTimestamp(ts);

        database.addOrderDeliverLabel(orderDeliver);
    }
    private void doCachedJob() {


        List<String> DailyCompany = database.getAllCompany();
        if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {

            List<String> DailyCo_TYPE_List = database.get_CO_TYPE();
            for(int co=0;co<DailyCo_TYPE_List.size();co++)
            {
                Log.e("match","match"+DailyCo_TYPE_List.get(co));
                if(Pref.getValue(MultiScanInner.this,"co_typek","").equalsIgnoreCase(DailyCo_TYPE_List.get(co)))
                {
                    //  Toast.makeText(MultiScanInner.this, "cotype"+DailyCo_TYPE_List.get(co), Toast.LENGTH_SHORT).show();
                    is_match=true;
                }
            }
        }


        if(is_match) {
            if (encodedString1.equalsIgnoreCase("") && spinRedelivery.getSelectedItem().toString().trim().equals("Completed")) {
                Pref.setValue(MultiScanInner.this,"co_typek","");
                L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.multiscan_targascan_errormsg_document).toString());
/*
                Intent intent = new Intent(activity, MultiScanInner.class);
                activity.startActivity(intent);
                activity.finish();*/
            } else {
                database.addCachedjob(getData());
                add_db_order_deliver_1_day();
                L.ok(activity, activity.getResources().getString(R.string.msg_cach_job_insert));

                Intent intent = new Intent(activity, MultiScanInner.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }else
        {
            database.addCachedjob(getData());
            add_db_order_deliver_1_day();
            L.ok(activity, activity.getResources().getString(R.string.msg_cach_job_insert));

            Intent intent = new Intent(activity, MultiScanInner.class);
            activity.startActivity(intent);
            activity.finish();

        }
    }

    private void prepareUpload(String deriverId) {
        // add_db_order_deliver_1_day();

        List<String> DailyCompany = database.getAllCompany();
        if (SharedPrefrenceUtil.getPrefrence(activity, Constants.PREF_SELECTED_URL_NAME, "").equalsIgnoreCase("YCOL")) {

            List<String> DailyCo_TYPE_List = database.get_CO_TYPE();
            for(int co=0;co<DailyCo_TYPE_List.size();co++)
            {
               /* for(int co1=0;co1<DailyCompany.size();co1++)
                {*/
                Log.e("match","match"+DailyCo_TYPE_List.get(co));
                if(Pref.getValue(MultiScanInner.this,"co_typek","").equalsIgnoreCase(DailyCo_TYPE_List.get(co)))
                {
                    // Toast.makeText(MultiScanInner.this, "cotype"+DailyCo_TYPE_List.get(co), Toast.LENGTH_SHORT).show();

                    is_match=true;
                }
                //}
            }
        }



        if(is_match) {
            if (encodedString1.equalsIgnoreCase("") && spinRedelivery.getSelectedItem().toString().trim().equals("Completed")) {
                Pref.setValue(MultiScanInner.this,"co_typek","");

                L.alert(activity, Constants.APP_NAME, getResources().getText(R.string.multiscan_targascan_errormsg_document).toString());
               /* Intent intent = new Intent(activity, MultiScanInner.class);
                activity.startActivity(intent);
                activity.finish();*/
            } else {
                try {
                    Cachedjob cachedjob = getData();
                    RequestParams params = new RequestParams();
                    params.add(JsonKey.MUTISCAN.ass1, cachedjob.getChg1());
                    params.add(JsonKey.MUTISCAN.ass2, cachedjob.getChg2());
                    params.add(JsonKey.MUTISCAN.cached_date, cachedjob.getCachedDate());
                    // params.add(JsonKey.MUTISCAN.cn, cachedjob.getcartoon1());
                    params.add(JsonKey.MUTISCAN.cn, UploadData.toString());
                    params.add(JsonKey.MUTISCAN.co_type, cachedjob.getCompanyID());
                    params.add(JsonKey.MUTISCAN.cust_name, cachedjob.getSignatureName());
                    params.add(JsonKey.MUTISCAN.DID, deriverId);
                    params.add(JsonKey.MUTISCAN.redeliver, cachedjob.getRedelivery());
                    params.add(JsonKey.MUTISCAN.servID, cachedjob.getServId());
                    params.add(JsonKey.MUTISCAN.signData, cachedjob.getEncodeSign());
                    params.add(JsonKey.MUTISCAN.system, cachedjob.getSystem());
                    params.add(JsonKey.MUTISCAN.to, cachedjob.getTo());
                    params.add(JsonKey.MUTISCAN.from, cachedjob.getFrom());
                    params.add(JsonKey.MUTISCAN.documentData, cachedjob.getDocumentData());
                    String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
                    params.add(JsonKey.AUTH_KEY, authKey);

                    doUpload(params);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else
        {
            Log.e("CTS","CTSKHU");
            Cachedjob cachedjob = getData();
            RequestParams params = new RequestParams();
            params.add(JsonKey.MUTISCAN_old.ass1, cachedjob.getChg1());
            params.add(JsonKey.MUTISCAN_old.ass2, cachedjob.getChg2());
            params.add(JsonKey.MUTISCAN_old.cached_date, cachedjob.getCachedDate());
            // params.add(JsonKey.MUTISCAN.cn, cachedjob.getcartoon1());
            params.add(JsonKey.MUTISCAN_old.cn, UploadData.toString());
            params.add(JsonKey.MUTISCAN_old.co_type, Pref.getValue(MultiScanInner.this, "co_typek", ""));
            params.add(JsonKey.MUTISCAN_old.cust_name, cachedjob.getSignatureName());
            params.add(JsonKey.MUTISCAN_old.DID, deriverId);
            params.add(JsonKey.MUTISCAN_old.redeliver, cachedjob.getRedelivery());
            params.add(JsonKey.MUTISCAN_old.servID, cachedjob.getServId());
            params.add(JsonKey.MUTISCAN_old.signData, cachedjob.getEncodeSign());
            params.add(JsonKey.MUTISCAN_old.system, cachedjob.getSystem());
            params.add(JsonKey.MUTISCAN_old.to, cachedjob.getTo());
            params.add(JsonKey.MUTISCAN_old.from, cachedjob.getFrom());
            params.add(JsonKey.MUTISCAN.documentData, cachedjob.getDocumentData());
            String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
            params.add(JsonKey.AUTH_KEY, authKey);

            doUpload(params);
        }
    }

    private void prepareUploadRouteLabel(String driverId) {
        if (UploadArrayData.length() > 0) {
            RequestParams params = new RequestParams();
            params.add("data", UploadArrayData.toString());
            // Log.e("Dhims: ", "--\n" + UploadArrayData.toString());
            doUploadDriverRouteLabel(params, driverId);
        } else {
            prepareUpload(driverId);
        }
    }

    private void doUploadDriverRouteLabel(final RequestParams params, final String driverId) {
        RestClient.post(JsonKey.getURL_MULTI_SCAN_CO_TYPE_DRIVER_ROUTE_LABEL(), params, new AsyncHandler(activity, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;
                try {
                    Logger.info("#DASHBOARD:MULTISCAN#", params + ">> statusCode: " + statusCode);
                    Logger.info("#DASHBOARD:MULTISCAN#", params + ">>response: " + response.toString());

                    if (response.getString("code").equals("200")) {

                        prepareUpload(driverId);

                    } else {
                        String message = "";
                        if (response.has("message")) {
                            message = response.getString("message");
                            L.ok(activity, message);
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

    private void doUpload(final RequestParams params) {

        RestClient.post(JsonKey.getURL_MULTI_SCAN_CO_TYPE(), params, new AsyncHandler(activity, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;

                try {
                    Logger.info("#DASHBOARD:MULTISCAN#", params + ">>" + response.toString());
                    responseJson = response.getJSONObject("response");
                    JSONObject dataJson = responseJson.getJSONObject("data");
                    String message = "";

                    add_db_order_deliver_1_day();
                    if (dataJson.has("message")) {
                        message = dataJson.getString("message");
                        L.ok(activity, message);
                    }

                    if (dataJson.has("goto")) {
                        if (dataJson.getString("goto").equalsIgnoreCase("scan")) {
                            Intent intent = new Intent(activity, MultiScanInner.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else if (dataJson.getString("goto").equalsIgnoreCase("main")) {
                            activity.finish();
                        }
                        checkBackPressFlag = true;
                        checkCartonNumberFlag = true;
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

    private void doUpload1(final RequestParams params) {
        RestClient.post(JsonKey.getURL_MULTI_SCAN_CO_TYPE_old(), params, new AsyncHandler(activity, true) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONObject responseJson = null;

                try {
                    Logger.info("#DASHBOARD:MULTISCAN#", params + ">>" + response.toString());
                    responseJson = response.getJSONObject("response");
                    JSONObject dataJson = responseJson.getJSONObject("data");
                    String message = "";

                    add_db_order_deliver_1_day();
                    if (dataJson.has("message")) {
                        message = dataJson.getString("message");
                        L.ok(activity, message);
                    }

                    if (dataJson.has("goto")) {
                        if (dataJson.getString("goto").equalsIgnoreCase("scan")) {
                            Intent intent = new Intent(activity, MultiScanInner.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else if (dataJson.getString("goto").equalsIgnoreCase("main")) {
                            activity.finish();
                        }
                        checkBackPressFlag = true;
                        checkCartonNumberFlag = true;
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


    private void addCartoon(final boolean canScroll) {

        if (mCartoonCnt < Constants.MAX_CARTOON_COUNT) {
            View view = mLayoutInflater.inflate(R.layout.item_cartoon, null);

            final TextView txtTitle = (TextView) view.findViewById(R.id.cartoon_txtTitle);
            final EditText editScan = (EditText) view.findViewById(R.id.cartoon_editCartoon1);
            final Button btnScan = (Button) view.findViewById(R.id.cartoon_btnScan);
            btnScan.setId(mCartoonCnt);
            ++mCartoonCnt;
            editScan.setId(mCartoonCnt);
            editScan.setSingleLine(true);
            if (mCartoons == null)
                mCartoons = new ArrayList<EditText>();
            mCartoons.add(editScan);
            if (mCartoonCnt > 1) {
                try {
                    mCartoons.get(mCartoonCnt - 1).setNextFocusDownId(mCartoonCnt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            txtTitle.setText("Carton" + mCartoonCnt);
            btnScan.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    ScanBarcode = true;
                    mSelectedPos = v.getId();
                    Intent intent = new Intent(activity, SimpleScannerActivity.class);
                    // intent.putExtra(ZBarConstants.SCAN_MODES, new
                    // int[]{Symbol.DATABAR, Symbol.DATABAR_EXP, Symbol.CODABAR,
                    // Symbol.CODE128, Symbol.CODE39, Symbol.CODE93,
                    // Symbol.ISBN10, Symbol.ISBN13});
                    startActivityForResult(intent, mSelectedPos);
                }
            });

            editScan.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        if (editScan.getId() == mCartoonCnt && mCartoons.get(mCartoonCnt - 1).getText().toString().trim().length() > 0) {
                            addCartoon(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            tblCartoonContainer.addView(view);
            if (canScroll) {
                // editScan.requestFocus();
                // doScrollToBottom(editScan);
            }
        }
    }

    private boolean validateCartoon() {
        for (EditText editText : mCartoons) {
            if (editText.getText().toString().trim().length() > 0)
                return true;
        }

        return false;
    }

    private String getCartoons() {
        JSONObject json = new JSONObject();
        int i = 1;
        for (EditText editText : mCartoons) {
            String cartoon = editText.getText().toString().trim();
            if (cartoon.length() > 0)
                try {
                    json.put("cn" + i++, cartoon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        }
        Logger.info("##Cartoons##", json.toString());
        return json.toString();
    }

    private void doScrollToBottom(final EditText editText) {
        try {
            rootScrollView.post(new Runnable() {
                @Override
                public void run() {
                    rootScrollView.scrollTo(0, editText.getBottom());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}