package com.aip.targascan.view.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aip.targascan.R;
import com.aip.targascan.common.async.GetCompanyListAsync;
import com.aip.targascan.common.async.GetReferenceNumberListAsync;
import com.aip.targascan.common.async.ReferenceAsync;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.vo.Reference;
import com.aip.targascan.vo.ReferenceNo;
import com.google.gson.Gson;

public class ReferenceActivity extends Activity {

	private Context mContext;
	private Spinner spinCompanyid;
	private Spinner spinReferenceid;
	private EditText edt_reference_no;
	private EditText edt_reference_comment;
	private Button btnUpdate;
	List<ReferenceNo> list;
	ArrayList<String> ReferenceList;

	private DatabaseHandler database;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reference);

		mContext = this;
		database = DatabaseHandler.getInstance(this);

		spinCompanyid = (Spinner) findViewById(R.id.spi_reference_company_id);
		spinReferenceid = (Spinner) findViewById(R.id.spi_reference_no);
		edt_reference_comment = (EditText) findViewById(R.id.edt_reference_comment);
		edt_reference_no = (EditText) findViewById(R.id.edt_reference_no);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);

		list = new ArrayList<ReferenceNo>();
		ReferenceList = new ArrayList<String>();
		ReferenceList.add("Select Reference No");

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,
				database.getAllCompany());
		spinCompanyid.setAdapter(arrayAdapter);

		ArrayAdapter<String> arrayAdapterList = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, ReferenceList);
		spinReferenceid.setAdapter(arrayAdapterList);

		if (database.getAllCompany().size() == 1) {
			getCompanyList();
		}
		 getReferenceNumber();

		spinReferenceid.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				edt_reference_no.setText("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		edt_reference_no.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				spinReferenceid.setSelection(0);
				return false;
			}
		});

		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (spinCompanyid.getSelectedItem().toString().equals("Select Company")) {
					Toast.makeText(mContext, "Please select valid CompanyID.", Toast.LENGTH_LONG).show();
					return;
				} else if (edt_reference_no.getText().toString().isEmpty()
						&& spinReferenceid.getSelectedItem().equals("Select Reference No")) {
					Toast.makeText(mContext, "Please provide valid reference number.", Toast.LENGTH_LONG).show();
					return;
				} else if (edt_reference_comment.getText().toString().isEmpty()) {
					Toast.makeText(mContext, "Please provide valid comment.", Toast.LENGTH_LONG).show();
					return;
				}

				String Reference_Number = "";

				if (edt_reference_no.getText().toString().isEmpty()) {
					Reference_Number = list.get(spinReferenceid.getSelectedItemPosition() - 1).getReference_num();
				} else {
					Reference_Number = edt_reference_no.getText().toString();
				}

				Reference reference = new Reference();
				reference.set_co_type(spinCompanyid.getSelectedItem().toString());
				reference.set_comments(edt_reference_comment.getText().toString());
				reference.set_reference_number(Reference_Number);

				// Log.e("reference", "" + reference.toString());

				new ReferenceAsync(mContext, reference, true, new ICallback() {

					@Override
					public void run(Object result) {
						// TODO Auto-generated method stub
						String resultData = (String) result;
						// {"data":{"message":"Reference Number stored successfully!"},"code":"200"}

						try {
							JSONObject jsonObject = new JSONObject(resultData);
							if (jsonObject.getString("code").equals("200")) {
								Toast.makeText(mContext, jsonObject.getJSONObject("data").getString("message"), Toast.LENGTH_LONG).show();
								spinCompanyid.setSelection(0);
								spinReferenceid.setSelection(0);
								edt_reference_comment.setText("");
								edt_reference_no.setText("");
								edt_reference_no.requestFocus();
								getReferenceNumber();
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.e("ReferenceAsync", "" + resultData);
					}
				}).execute();
			}
		});

	}

	public void getCompanyList() {
		new GetCompanyListAsync(mContext, true, new ICallback() {

			@Override
			public void run(Object result) {
				// TODO Auto-generated method stub
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,
						database.getAllCompany());
				spinCompanyid.setAdapter(arrayAdapter);
			}
		}).execute();
	}

	public void getReferenceNumber() {

		new GetReferenceNumberListAsync(mContext, true, new ICallback() {

			@Override
			public void run(Object result) {
				// TODO Auto-generated method stub

				list = new ArrayList<ReferenceNo>();
				ReferenceList = new ArrayList<String>();
				ReferenceList.add("Select Reference No");

				try {
					Log.e("", "" + result.toString());
					JSONObject object = new JSONObject((String) result);
					JSONArray array = object.getJSONArray("data");

					for (int Counter = 0; Counter < array.length(); Counter++) {
						ReferenceNo no = new ReferenceNo();
						no = new Gson().fromJson(array.getJSONObject(Counter).toString(), ReferenceNo.class);
						list.add(no);
						ReferenceList.add(no.getCo_type() + "-" + no.getReference_num());
					}

					ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,
							ReferenceList);
					spinReferenceid.setAdapter(arrayAdapter);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).execute();
	}
}
