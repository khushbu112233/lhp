package com.aip.targascan.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.R;
import com.aip.targascan.common.async.GetAddressListAsync;
import com.aip.targascan.common.util.ICallback;
import com.google.gson.JsonObject;

public class AddressListActivity extends Activity {

	// List<ServerData> datas;
	// ServerData serverData;
	ListView mListView;
	List<String> AddressList;
	List<String> OrderList;
	double lat = 0.0, lng = 0.0;
	Boolean GpsEnable = false;
	SwipeRefreshLayout swipeRefreshLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);

		mListView = (ListView) findViewById(R.id.lst);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

		AddressList = new ArrayList<String>();
		OrderList = new ArrayList<String>();
		// datas = new ArrayList<ServerData>();
		// serverData = new ServerData();

		GetAddressListAsync async = new GetAddressListAsync(AddressListActivity.this, true, new ICallback() {

			@Override
			public void run(Object result) {

				Log.d("", "" + result.toString());

				try {
					JSONObject jsonObject = new JSONObject(result.toString());
					Log.e("Addresses", "Addresses \n" + jsonObject.toString(4));

					if (jsonObject.getString("code").equals("200")) {

						AddressList.clear();
						OrderList.clear();

						JSONArray jsonArray = jsonObject.getJSONArray("data");
						for (int counter = 0; counter < jsonArray.length(); counter++) {

							JSONObject obj = jsonArray.getJSONObject(counter);
							// serverData = new ServerData();
							// serverData.setZip(obj.getString("zip"));
							// serverData.setDelivery_address2(obj.getString("delivery_address2"));
							// serverData.setDelivery_address(obj.getString("delivery_address"));
							// serverData.setDriver_id(obj.getString("driver_id"));
							// serverData.setApp_ordering(obj.getString("app_ordering"));
							// serverData.setCarton_num(obj.getString("carton_num"));
							// serverData.setDelivery_name(obj.getString("delivery_name"));
							// serverData.setDate(obj.getString("date"));
							// serverData.setCity(obj.getString("city"));
							// datas.add(serverData);

							try {

								String address = "";
								Boolean flag = false;

								if (obj.has("delivery_address")) {
									address = obj.getString("delivery_address").trim() + "\n";
								}

								if (obj.has("city")) {
									if (!obj.getString("city").equals("")) {
										address = address + obj.getString("city").trim();
										flag = true;
									}
								}

								if (obj.has("zip")) {
									if (!obj.getString("zip").equals("")) {
										if(flag){
											address = address + ", ";
										}
										address = address + obj.getString("zip").trim();
									}
								}

								if (address.length() > 0) {
									AddressList.add(address);
									if (obj.has("work_order_num")) {
										String work_order_num = obj.getString("work_order_num").trim();
										OrderList.add(work_order_num);
									} else {
										OrderList.add("");
									}
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}

					Log.d("WorkOrderList", "WorkOrderList \n" + OrderList.toString());
					Log.d("AddressList", "AddressList \n" + AddressList.toString());

					if (AddressList.size() > 0) {

						AdapterData adapterData = new AdapterData(OrderList, AddressList);
						mListView.setAdapter(adapterData);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		});

		async.execute();

		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);

				GetAddressListAsync async = new GetAddressListAsync(AddressListActivity.this, true, new ICallback() {

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

								AddressList.clear();
								OrderList.clear();

								JSONArray jsonArray = jsonObject.getJSONArray("data");
								for (int counter = 0; counter < jsonArray.length(); counter++) {

									JSONObject obj = jsonArray.getJSONObject(counter);
									// serverData = new ServerData();
									// serverData.setZip(obj.getString("zip"));
									// serverData.setDelivery_address2(obj.getString("delivery_address2"));
									// serverData.setDelivery_address(obj.getString("delivery_address"));
									// serverData.setDriver_id(obj.getString("driver_id"));
									// serverData.setApp_ordering(obj.getString("app_ordering"));
									// serverData.setCarton_num(obj.getString("carton_num"));
									// serverData.setDelivery_name(obj.getString("delivery_name"));
									// serverData.setDate(obj.getString("date"));
									// serverData.setCity(obj.getString("city"));
									// datas.add(serverData);

									try {

										String address = "";
										Boolean flag = false;

										if (obj.has("delivery_address")) {
											address = obj.getString("delivery_address").trim() + "\n";
										}

										if (obj.has("city")) {
											if (!obj.getString("city").equals("")) {
												address = address + obj.getString("city").trim();
												flag = true;
											}
										}

										if (obj.has("zip")) {
											if (!obj.getString("zip").equals("")) {
												if(flag){
													address = address + ", ";
												}
												address = address + obj.getString("zip").trim();
											}
										}

										if (address.length() > 0) {
											AddressList.add(address);
											if (obj.has("work_order_num")) {
												String work_order_num = obj.getString("work_order_num").trim();
												OrderList.add(work_order_num);
											} else {
												OrderList.add("");
											}
										}

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							}

							Log.d("WorkOrderList", "WorkOrderList \n" + OrderList.toString());
							Log.d("AddressList", "AddressList \n" + AddressList.toString());

							if (AddressList.size() > 0) {

								AdapterData adapterData = new AdapterData(OrderList, AddressList);
								mListView.setAdapter(adapterData);
							}

							swipeRefreshLayout.setRefreshing(false);

						} catch (JSONException e) {
							e.printStackTrace();
							swipeRefreshLayout.setRefreshing(false);
						}

					}
				});

				async.execute();
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// Intent mIntent = new Intent(AddressListActivity.this,
				// MapActivity.class);
				// mIntent.putExtra("address", AddressList.get(arg2));
				// startActivity(mIntent);

				Log.e("Position  ", "Position: " + arg2);

				try {

					LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

					if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						final AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
						builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
								.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
									public void onClick(final DialogInterface dialog, final int id) {
										startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
										GpsEnable = true;
									}
								}).setNegativeButton("No", new DialogInterface.OnClickListener() {
									public void onClick(final DialogInterface dialog, final int id) {
										dialog.cancel();
									}
								});
						final AlertDialog alert = builder.create();
						alert.show();
					} else {
						GpsEnable = true;
					}

					if (GpsEnable) {

						Location myLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

						getLatLongFromGivenAddress(AddressList.get(arg2));

						// String uri = "http://maps.google.com/maps?saddr=" +
						// myLocation.getLatitude() + "," +
						// myLocation.getLongitude()
						// + "&daddr=" + 23.022505 + "," + 72.571362;

						String uri = "http://maps.google.com/maps?saddr=" + (myLocation.getLatitude()) + "," + (myLocation.getLongitude())
								+ "&daddr=" + lat + "," + lng;

						Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));

						intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
						startActivity(intent);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void getLatLongFromGivenAddress(String address) {

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		Log.e("Address", "" + address);
		
		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {

				lat = addresses.get(0).getLatitude();
				lng = addresses.get(0).getLongitude();

			}
			
			Log.e("Geocoder Value: ", "lat: " + lat + "lng: " + lng);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class AdapterData extends BaseAdapter {

		private List<String> dataVO;
		private List<String> dataV1;

		public AdapterData(List<String> dataVos, List<String> dataV1s) {
			this.dataVO = dataVos;
			this.dataV1 = dataV1s;
		}

		@Override
		public int getCount() {
			return dataVO.size();
		}

		@Override
		public String getItem(int position) {
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
				convertView = inflater.inflate(R.layout.row_addresslist, parent, false);
				holder = new ViewHolderMessage();

				holder.txtOrderNo = (TextView) convertView.findViewById(R.id.txtOrderNo);
				holder.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolderMessage) convertView.getTag();
			}

			try {

				final String vo = dataVO.get(position);
				final String v1 = dataV1.get(position);

				if (vo != null) {
					holder.txtOrderNo.setText(vo);
				}

				if (v1 != null) {
					holder.txtAddress.setText(v1);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			// convertView.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			//
			// try {
			//
			// LocationManager locationManager = (LocationManager)
			// getSystemService(Context.LOCATION_SERVICE);
			//
			// if
			// (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			// {
			// final AlertDialog.Builder builder = new
			// AlertDialog.Builder(AddressListActivity.this);
			// builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(false)
			// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			// public void onClick(final DialogInterface dialog, final int id) {
			// startActivity(new
			// Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			// GpsEnable = true;
			// }
			// }).setNegativeButton("No", new DialogInterface.OnClickListener()
			// {
			// public void onClick(final DialogInterface dialog, final int id) {
			// dialog.cancel();
			// }
			// });
			// final AlertDialog alert = builder.create();
			// alert.show();
			// } else {
			// GpsEnable = true;
			// }
			//
			// if (GpsEnable) {
			//
			// Location myLocation =
			// locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			//
			// getLatLongFromGivenAddress(AddressList.get(position));
			//
			// // String uri = "http://maps.google.com/maps?saddr="
			// // +
			// // myLocation.getLatitude() + "," +
			// // myLocation.getLongitude()
			// // + "&daddr=" + 23.022505 + "," + 72.571362;
			//
			// String uri = "http://maps.google.com/maps?saddr=" +
			// (myLocation.getLatitude()) + ","
			// + (myLocation.getLongitude()) + "&daddr=" + lat + "," + lng;
			//
			// Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
			// Uri.parse(uri));
			//
			// intent.setClassName("com.google.android.apps.maps",
			// "com.google.android.maps.MapsActivity");
			// startActivity(intent);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			//
			// }
			// });

			return convertView;
		}

	}

	static class ViewHolderMessage {
		private TextView txtOrderNo;
		private TextView txtAddress;
	}

}
