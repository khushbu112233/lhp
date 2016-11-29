package com.aip.targascan.view.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.aip.targascan.R;
import com.aip.targascan.common.async.DriverRouteSubmitAsync;
import com.aip.targascan.common.async.GetDriverRouteAsync;
import com.aip.targascan.common.async.GetDriverRouteTimeStatusAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.DriverRoute;
import com.aip.targascan.vo.DriverRouteData;
import com.aip.targascan.vo.DriverRouteTimeStatus;

@ContentView(R.layout.activity_line_hauls)
public class LineHaulActivity extends RoboActivity implements LocationListener {

    ICallback icallback;
    @InjectView(R.id.spinnerchg1)
    Spinner spinner1;
    @InjectView(R.id.spinnerchg2)
    Spinner spinner2;
    @InjectView(R.id.edtDateTime)
    TextView edtDateAndTime;
    @InjectView(R.id.btnUpdate)
    Button btnupdate;
    @InjectView(R.id.dateTime)
    LinearLayout dateTime;

    private LocationManager mLocMgr;

    private LineHaulActivity activity;
    private DatabaseHandler databaseHandler;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private boolean isTimeSet = false;
    private boolean isDateSet = false;

    private Calendar calendar;
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    String driver_route_id, driver_route_timestatus_id;

    double gps_lat = 0.0;
    double gps_log = 0.0;

    // private String mCurrPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        databaseHandler = DatabaseHandler.getInstance(activity);
        // mCurrPwd = databaseHandler.getPassword();

        if (Util.isNetAvailable(activity)) {
            GetDriverRoute();
        } else {
            setDriverRoute();
            setDriverRouteTimeStatus();
        }

        dateTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }

        });

        btnupdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    doTask();
                }
            }
        });

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        String am_pm = "PM";

        if(c.get(Calendar.AM_PM) == Calendar.AM)
            am_pm = "AM";

        edtDateAndTime.setText(mYear + "-" + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + String.format("%02d", mDay) + " " + String.format("%02d", mHour) + ":" + String.format("%02d", mMinute) + " " + am_pm);

    }

    public void showDatePickerDialog() {
        // Get Current Date
        c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                isDateSet = true;
                isTimeSet = false;
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                c.set(mYear, mMonth, mDay, mHour, mMinute);
                String am_pm = "PM";
                if(c.get(Calendar.AM_PM) == Calendar.AM) {
                    am_pm = "AM";
                }
                edtDateAndTime.setText(mYear + "-" + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + String.format("%02d", mDay) + " " + String.format("%02d", mHour) + ":" + String.format("%02d", mMinute) + " " + am_pm);
                showTimePickerDialog();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void showTimePickerDialog() {
        // Get Current Time
        c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                isTimeSet = true;
                mHour = hourOfDay;
                mMinute = minute;
                c.set(mYear, mMonth, mDay, mHour, mMinute);
                String am_pm = "PM";
                if(c.get(Calendar.AM_PM) == Calendar.AM) {
                    am_pm = "AM";
                }
                edtDateAndTime.setText(mYear + "-" + c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + "-" + String.format("%02d", mDay) + " " + String.format("%02d", mHour) + ":" + String.format("%02d", mMinute) + " " + am_pm);
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void setDriverRoute() {

        final ArrayList<DriverRoute> driverList = databaseHandler.getAllDriverRoute();
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Driver Route");

        for (int counter = 0; counter < driverList.size(); counter++) {
            strings.add(driverList.get(counter).getDriver_route_name());
        }

        spinner1.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, strings));

        spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (arg2 > 0)
                        driver_route_id = driverList.get(arg2 - 1).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setDriverRouteTimeStatus() {

        final ArrayList<DriverRouteTimeStatus> driverStatusList = databaseHandler.getAllDriverRouteTimeStatus();
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Route Time Status");

        for (int counter = 0; counter < driverStatusList.size(); counter++) {
            strings.add(driverStatusList.get(counter).getPlace_name());
        }

        spinner2.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, strings));

        spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    if (arg2 > 0)
                        driver_route_timestatus_id = driverStatusList.get(arg2 - 1).getId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void GetDriverRoute() {
        // TODO Auto-generated method stub

        GetDriverRouteAsync async = new GetDriverRouteAsync(activity, true, new ICallback() {

            @Override
            public void run(Object result) {
                // TODO Auto-generated method stub
                Log.d("GetDriverRouteAsync", "" + result.toString());

                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (jsonObject.optString("code").equals("200")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        databaseHandler.deleteDriverRoute();
                        for (int counter = 0; counter < array.length(); counter++) {
                            DriverRoute driverRoute = new DriverRoute();
                            driverRoute.setId(array.getJSONObject(counter).getString("id"));
                            driverRoute.setDriver_route_name((array.getJSONObject(counter).getString("driver_route_name")));
                            databaseHandler.addDriverRoute(driverRoute);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                setDriverRoute();
                GetDriverRouteTimeStatus();
            }
        });
        async.execute();

    }

    private void GetDriverRouteTimeStatus() {

        GetDriverRouteTimeStatusAsync async = new GetDriverRouteTimeStatusAsync(activity, true, new ICallback() {

            @Override
            public void run(Object result) {
                // TODO Auto-generated method stub
                Log.d("GetDriverRouteTimeStatusAsync", "" + result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (jsonObject.optString("code").equals("200")) {
                        JSONArray array = jsonObject.getJSONArray("data");
                        databaseHandler.deleteDriverRouteTimeStatus();
                        for (int counter = 0; counter < array.length(); counter++) {
                            DriverRouteTimeStatus driverRouteTimeStatus = new DriverRouteTimeStatus();
                            driverRouteTimeStatus.setId(array.getJSONObject(counter).getString("id"));
                            driverRouteTimeStatus.setPlace_name(array.getJSONObject(counter).getString("place_name"));
                            databaseHandler.addDriverRouteTimeStatus(driverRouteTimeStatus);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setDriverRouteTimeStatus();
            }
        });
        async.execute();
    }

    private boolean isValidate() {

        if (spinner1.getSelectedItemPosition() == 0) {
            L.alert(LineHaulActivity.this, "LineHaul Pro", "Please select driver route");
            return false;
        }

        if (spinner2.getSelectedItemPosition() == 0) {
            L.alert(LineHaulActivity.this, "LineHaul Pro", "Pleace select route time status");
            return false;
        }

        if (!isDateSet) {
            L.alert(LineHaulActivity.this, "LineHaul Pro", "Pleace select date and time");
            return false;
        }

        if (!isTimeSet) {
            L.alert(LineHaulActivity.this, "LineHaul Pro", "Pleace select date and time");
            return false;
        }

        calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay, mHour, mMinute);

        if (calendar.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            L.alert(LineHaulActivity.this, "LineHaul Pro", "Pleace select future date and time");
            return false;
        }

        return true;

    }

    private void doTask() {

        calendar = Calendar.getInstance();
        calendar.set(mYear, mMonth, mDay, mHour, mMinute);

        DriverRouteData driverRouteData = new DriverRouteData();
        driverRouteData.setDriver_id(databaseHandler.getContact().getDriverID());
        driverRouteData.setTimestamp(calendar.getTimeInMillis() / 1000 + "");
        driverRouteData.setDriver_route_id(driver_route_id);
        driverRouteData.setDriver_route_timestatus_id(driver_route_timestatus_id);
        driverRouteData.setGps_lat(gps_lat + "");
        driverRouteData.setGps_log(gps_log + "");

        databaseHandler.addDriverRouteTimeStatuDetails(driverRouteData);

        if (!Util.isNetAvailable(activity)) {
            isDateSet = false;
            isTimeSet = false;
            edtDateAndTime.setText("");
            setDriverRoute();
            setDriverRouteTimeStatus();
            L.ok(activity, getResources().getString(R.string.msg_cach_job_insert));
            finish();
            return;
        }

        String data = Util.cur2Json(databaseHandler.getDriverRouteTimeStatuDetails()).toString();

        DriverRouteSubmitAsync async = new DriverRouteSubmitAsync(activity, driverRouteData, data, new ICallback() {

            @Override
            public void run(Object result) {
                // TODO Auto-generated method stub
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());

                    if (jsonObject.getString("code").equals("200")) {
                        isDateSet = false;
                        isTimeSet = false;
                        edtDateAndTime.setText("");
                        setDriverRoute();
                        setDriverRouteTimeStatus();
                        L.ok(activity, jsonObject.getString("message"));
                        databaseHandler.deleteDriverRouteTimeStatusDetails();
                        finish();
                    } else {
                        L.alert(LineHaulActivity.this, "LineHaul Pro", jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        async.execute();
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        gps_lat = location.getLatitude();
        gps_log = location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

}