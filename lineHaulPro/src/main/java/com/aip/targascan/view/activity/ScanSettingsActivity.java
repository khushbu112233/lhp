package com.aip.targascan.view.activity;

import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.aip.targascan.R;
import com.aip.targascan.common.util.Pref;

import roboguice.activity.RoboActivity;

/**
 * Created by aipxperts-ubuntu-01 on 2/6/17.
 */

public class ScanSettingsActivity extends RoboActivity {
    Switch SyncSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.scan_setting_layout);
        SyncSwitch = (Switch) findViewById(R.id.SyncSwitch);

      //  Pref.setValue(ScanSettingsActivity.this,"auto_scan","0");
        if(Pref.getValue(ScanSettingsActivity.this,"auto_scan","").equalsIgnoreCase("1")){
            SyncSwitch.setChecked(true);
            SyncSwitch.setBackgroundResource(R.drawable.switch_on);

        }else{
            SyncSwitch.setChecked(false);
            SyncSwitch.setBackgroundResource(R.drawable.switch_off);


        }
        SyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    SyncSwitch.setBackgroundResource(R.drawable.switch_on);
                    Pref.setValue(ScanSettingsActivity.this,"auto_scan","1");
                    // ((DashboardActivity) getActivity()).getAllContacts();

                }else{
                    SyncSwitch.setBackgroundResource(R.drawable.switch_off);
                    Pref.setValue(ScanSettingsActivity.this,"auto_scan","0");


                }
            }
        });
    }
}
