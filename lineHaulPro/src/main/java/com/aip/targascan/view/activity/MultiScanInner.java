package com.aip.targascan.view.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.R;
import com.aip.targascan.common.async.CheckCartonNumberAsync;
import com.aip.targascan.common.async.MultiScanDataAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.view.frag.SimpleScannerActivity;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.DailyOrder;
import com.aip.targascan.vo.DriverLabel;
import com.aip.targascan.vo.DriverRouteData;
import com.aip.targascan.vo.Login;
import com.loopj.android.http.RequestParams;

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
    @InjectView(R.id.tblCartoonContainer)
    LinearLayout tblCartoonContainer;

    private MultiScanInner activity;
    private static final int SIGN_CODE = 1;
    private String encodedString;

    private final int ZBAR_SCANNER_REQUEST = 100;
    private LayoutInflater mLayoutInflater;

    private boolean checkCartonNumberFlag = true;
    private boolean checkBackPressFlag = false;
    private boolean MasterCompanyEditable = true;

    boolean ScanBarcode = false;

    DatabaseHandler database;
    JSONObject UploadData = new JSONObject();
    JSONArray UploadArrayData = new JSONArray();

    View tempView;
    TextView txtCompany;

    String path;

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

                } else {

                    int flag = 0;
                    UploadData = new JSONObject();
                    UploadArrayData = new JSONArray();

                    for (int i = 0; i < mCartoonsType.size(); i++) {
                        if (mBarcodeImage.get(i).length() < 1 && mBarcodeImageValue.get(i).equals("Rejected")) {
                            Toast.makeText(activity, "Please capture photo of all cartons", Toast.LENGTH_LONG).show();
                            flag = 1;
                            break;
                        }
                        try {
                            if (mBarcodeImageValue.get(i).equals("Rejected")) {
                                JSONObject object = new JSONObject();
                                object.put("driver_id", database.getContact().getDriverID());
                                object.put("carton_num", mCartoonsText.get(i).getText().toString());
                                object.put("barcode", mBarcodeImage.get(i));
                                object.put("cust_name", editsignature.getText());
                                UploadArrayData.put(object);
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

            List<String> cmpList = database.getAllCompany();
            cmpList.remove("Select Company");
            cmpList.add(0, "Unknown");

            List<DailyOrder> DailyOrderList = database.getAllDailyOrder();
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
                        cursor.moveToFirst();
                        if (cursor.getString(cursor.getColumnIndex("co_type")).equalsIgnoreCase("Unknown")) {
                            scanData.setText("Rejected");
                            mBarcodeImageValue.add("Rejected");
                        } else {
                            scanData.setText("Accepted");
                            scan.setVisibility(View.GONE);
                            mBarcodeImageValue.add("Accepted");
                        }
                    } else {
                        scanData.setText("Rejected");
                        mBarcodeImageValue.add("Rejected");
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

                if (MasterCompanyEditable) {
                    custom_lin.addView(spinner, i + 1);
                } else {
                    custom_lin.addView(mView, i + 1);
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
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();

                    encodedString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        ScanBarcode = false;

    }

    private void doCachedJob() {
        database.addCachedjob(getData());
        L.ok(activity, activity.getResources().getString(R.string.msg_cach_job_insert));

        Intent intent = new Intent(activity, MultiScanInner.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private void prepareUpload(String deriverId) {
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

            String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
            params.add(JsonKey.AUTH_KEY, authKey);

            doUpload(params);

        } catch (Exception e) {
            e.printStackTrace();
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

    private int mCartoonCnt;
    private int mSelectedPos;
    private int mSelectedPosOfBarcode;
    private List<EditText> mCartoons;
    private List<TextView> mCartoonsText;
    private List<Spinner> mCartoonsType;
    private List<String> mBarcodeImage;
    private List<String> mBarcodeImageValue;

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