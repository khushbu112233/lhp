package com.aip.targascan.common.async;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.WebserviceReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by aipxperts-ubuntu-01 on 27/5/17.
 */

public class GetMustScanCoType extends AsyncTask<Void, Void, String> {
    private Context mcontext;
    private ICallback callBack;
    private Dialog dialog;
    private DatabaseHandler databaseHandler;
    boolean isProgressBar;

    public GetMustScanCoType(Context context, boolean isProgressBar, ICallback callBack) {
        this.mcontext = context;
        this.callBack = callBack;
        this.isProgressBar = isProgressBar;
        //databaseHandler = DatabaseHandler.getInstance(context);
    }

    @Override
    protected void onPreExecute() {
        try {
            if (isProgressBar)
                dialog = ProgressDialog.show(mcontext, "Getting scan type List...", "Loading");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... param) {
        try {
            WebserviceReader reader = new WebserviceReader();
            Map<String, String> params = new HashMap<String, String>();
            //  params.put(JsonKey.KEY_DRIVER_ID, databaseHandler.getContact().getDriverID());
            //   Log.d("MainUrl", "" + JsonKey.getURL_MUST_SCAN_CO_TYPE());
            String response = reader.sendRequest(JsonKey.getURL_MUST_SCAN_CO_TYPE());
         //   Log.e("response",response+"");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (dialog != null)
            dialog.dismiss();
        if (result != null)
            callBack.run(result);
    }
}
