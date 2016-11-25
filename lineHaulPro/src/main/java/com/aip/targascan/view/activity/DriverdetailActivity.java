package com.aip.targascan.view.activity;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aip.targascan.R;
import com.aip.targascan.common.database.DatabaseHandler;
import com.aip.targascan.common.util.AsyncHandler;
import com.aip.targascan.common.util.Constants;
import com.aip.targascan.common.util.JsonKey;
import com.aip.targascan.common.util.Logger;
import com.aip.targascan.common.util.RestClient;
import com.aip.targascan.common.util.SharedPrefrenceUtil;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.vo.DriverDetailVO;
import com.aip.targascan.vo.DriverDetailVO.DataVO;
import com.aip.targascan.vo.Login;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
@ContentView(R.layout.activity_driverdetail)
public class DriverdetailActivity extends RoboActivity{

	private DriverdetailActivity activity;
	@InjectView(R.id.lst) 
	private ListView mList;
	
	@InjectView(R.id.txtDriverNotFound)
	private TextView txtNotFound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activity = this;	
		
		String authKey = SharedPrefrenceUtil.getPrefrence(getApplicationContext(), Constants.PREF_AUTH_KEY, null);
		Login login = DatabaseHandler.getInstance(this).getContact();
		if(authKey != null && login != null && login.getDriverID() != null)
		{
			RequestParams params = new RequestParams();
			params.add(JsonKey.AUTH_KEY, authKey);
			params.add(JsonKey.KEY_DRIVER_ID, login.getDriverID());//login.getDriverID() or "8263"
			
			txtNotFound.setVisibility(View.GONE);
			fetchData(params);
		}else{
			Util.showLoginDialog(activity);
			txtNotFound.setVisibility(View.VISIBLE);
		}
	}
	
	private void fetchData(RequestParams params){
		RestClient.post(JsonKey.getURL_DRIVER_DETAIL(), params, new AsyncHandler(DriverdetailActivity.this, true) {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				JSONObject responseJson = null;
				try {
					Logger.info("#DASHBOARD:DRIVER DETAIL#", response.toString());
					responseJson = response.getJSONObject("response");
					//JSONObject dataJson = responseJson.getJSONObject("data");
						try {
							Gson gson = new Gson();
							DriverDetailVO dataVO = gson.fromJson(responseJson.toString(), DriverDetailVO.class); 
							List<DataVO> dataVOs = dataVO.getDataVOs();
							AdapterData adapterData = new AdapterData(dataVOs);
							mList.setAdapter(adapterData);
						} catch (Exception e) {
							e.printStackTrace();
							Util.showError(activity, response);
						}

				} catch (Exception e) {
					e.printStackTrace();
					txtNotFound.setVisibility(View.VISIBLE);
					Util.showError(activity, response);
				}
			}
			
		});
	}

	public class AdapterData extends BaseAdapter {

		private List<DataVO> dataVO;

		public AdapterData(List<DataVO> dataVos) {
			this.dataVO = dataVos;
		}

		@Override
		public int getCount() {
			return dataVO.size();
		}

		@Override
		public DataVO getItem(int position) {
			return dataVO.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolderMessage holder = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.row_driver_detail, parent, false);
				holder = new ViewHolderMessage();

				holder.txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);
				holder.txtOrderType = (TextView) convertView.findViewById(R.id.txtType);
				holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
				holder.txtCountAll = (TextView) convertView.findViewById(R.id.txtCountAll);
				holder.txtPickUp = (TextView) convertView.findViewById(R.id.txtPickUp);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolderMessage) convertView.getTag();
			}
			
			DataVO vo = getItem(position);

			if(vo != null)
			{
				
				
				holder.txtOrderNo.setText(vo.getWork_order_num());
				holder.txtOrderType.setText(vo.getCo_type());
				holder.txtAddress.setText(vo.getDelivery_address()+"\n"+vo.getCity()+" - "+vo.getZip());
				holder.txtCountAll.setText("Count : "+vo.getCount_all());
				holder.txtPickUp.setText("Pickup : "+vo.getPickup());
				
				try {
					int color = Color.parseColor("#"+vo.getColor());
					
					holder.txtOrderNo.setTextColor(color);
					holder.txtOrderType.setTextColor(color);
					holder.txtAddress.setTextColor(color);
					holder.txtCountAll.setTextColor(color);
					holder.txtPickUp.setTextColor(color);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
//				convertView.setBackgroundColor(Color.parseColor("#"+vo.getColor()));
			}

			return convertView; 
		}

	}
	static class ViewHolderMessage {
		private TextView txtOrderNo;
		private TextView txtOrderType;
		private TextView txtAddress;
		private TextView txtCountAll;
		private TextView txtPickUp;
	}
}

