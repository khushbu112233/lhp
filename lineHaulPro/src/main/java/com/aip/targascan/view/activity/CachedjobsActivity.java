package com.aip.targascan.view.activity;

import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.aip.targascan.R;
import com.aip.targascan.common.async.ExportDataAsync;
import com.aip.targascan.common.async.IResultListner;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.L.IL;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.Cachedjob;
import com.aip.targascan.vo.Login;
import com.loopj.android.http.RequestParams;

@ContentView(R.layout.activity_cachedjobs)
public class CachedjobsActivity extends RoboActivity {

	@InjectView(R.id.btnretransmit)
	Button retransmit;
	@InjectView(R.id.btndeleteall)
	Button deleteAll;
	@InjectView(R.id.cachedjob_txtCount)
	TextView txtCount;

	private CachedjobsActivity activity;
	private DatabaseHandler database;
	private int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = this;
		database = DatabaseHandler.getInstance(this);
		updateView();

		retransmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (Util.isNetAvailable(activity)) {
					// List<Cachedjob> cachedjobs = database.getCachedjob();
					Login login = database.getContact();
					if (login == null) {
						L.alert(activity, activity.getResources().getString(R.string.cach_job_login_error));
					} else {

						ExportDataAsync exportDataAsync = new ExportDataAsync(activity, new IResultListner() {

							@Override
							public void result(Object result, boolean isSuccess) {
								updateView();
							}
						});

						exportDataAsync.execute();
					}

				} else {
					L.alert(activity, getResources().getString(R.string.error_internet));
				}
			}
		});

		deleteAll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				L.confirmDialog(activity, getResources().getString(R.string.msg_delete_cachedjojb), new IL() {

					@Override
					public void onSuccess() {
						database.deleteCachedJob();
						updateView();
					}

					@Override
					public void onCancel() {

					}
				});
			}
		});
	}

	private void updateView() {
		try {
			count = database.getCachedjobCount();
			txtCount.setText(activity.getResources().getString(R.string.cachedjob_textview_text, String.valueOf(count)));

			if (count < 1) {
				retransmit.setBackgroundColor(Color.parseColor("#818185"));
				deleteAll.setBackgroundColor(Color.parseColor("#818185"));
				// retransmit.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_disable));
				// deleteAll.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.btn_disable));

				retransmit.setEnabled(false);
				deleteAll.setEnabled(false);
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
}
