package com.aip.targascan.view.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.Adapter.ListCotypeAdapter;
import com.aip.targascan.R;
import com.aip.targascan.common.async.GetAddressListAsync;
import com.aip.targascan.common.async.GetProductDetailAsync;
import com.aip.targascan.common.util.ICallback;
import com.aip.targascan.common.util.L;
import com.aip.targascan.common.util.Pref;
import com.aip.targascan.common.util.Util;
import com.aip.targascan.model.ProductDetail;
import com.google.gson.JsonObject;

public class AddressListActivity extends Activity {

	// List<ServerData> datas;
	// ServerData serverData;
	ListView mListView;
	List<String> AddressList;
	List<String> OrderList;
	List<String> CartunList;
	double lat = 0.0, lng = 0.0;
	Boolean GpsEnable = false;
	SwipeRefreshLayout swipeRefreshLayout;
	boolean isDuplicate=false;
	ArrayList<String> products = new ArrayList<String>();
	ArrayList<ProductDetail> productDetailArrayList1 = new ArrayList<ProductDetail>();
	public ArrayList<ProductDetail> productDetailArrayList=new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_list);

		mListView = (ListView) findViewById(R.id.lst);
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

		AddressList = new ArrayList<String>();
		OrderList = new ArrayList<String>();
		CartunList = new ArrayList<String>();
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
						CartunList.clear();

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
									if (obj.has("carton_num")) {
										String work_order_num = obj.getString("carton_num").trim();
										CartunList.add(work_order_num);
									} else {
										CartunList.add("");
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

						AdapterData adapterData = new AdapterData(OrderList, AddressList,CartunList);
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
								CartunList.clear();

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
											if (obj.has("carton_num")) {
												String work_order_num = obj.getString("carton_num").trim();
												CartunList.add(work_order_num);
											} else {
												CartunList.add("");
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

								AdapterData adapterData = new AdapterData(OrderList, AddressList,CartunList);
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
		List<String> data;
		public AdapterData(List<String> dataVos, List<String> dataV1s,List<String> data) {
			this.dataVO = dataVos;
			this.dataV1 = dataV1s;
			this.data = data;
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
			final String v2 = data.get(position);

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

			final ViewHolderMessage finalHolder = holder;
			holder.txtOrderNo.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (Util.isNetAvailable(AddressListActivity.this)) {
						search_cartun_number(v2);
					} else {
						L.alert(AddressListActivity.this, getResources().getString(R.string.error_internet));
					}

				}
			});

			return convertView;
		}

	}

	static class ViewHolderMessage {
		private TextView txtOrderNo;
		private TextView txtAddress;
	}
	public void search_cartun_number(final String str)
	{
		productDetailArrayList.clear();
		productDetailArrayList1.clear();
		products.clear();
		GetProductDetailAsync async = new GetProductDetailAsync(AddressListActivity.this,str, new ICallback() {

			@Override
			public void run(Object result) {
				if(result == null)
				{
					L.alert(AddressListActivity.this, "Server error! Please try again later.");
					return;
				}
				String str1 = result.toString();
				//Log.e("result",""+str1);
				JSONObject json;
				try {
					json = new JSONObject(str1);
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
								String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn="+str+"&co_type="+productDetailArrayList.get(0).getCo_type1();
								Pref.setValue(AddressListActivity.this,"Detail_url",url);
								Intent intent =new Intent(AddressListActivity.this,ProductDetailActivity.class);
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
									dialog(products,str);
								}else
								{
									String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn="+str+"&co_type="+productDetailArrayList.get(0).getCo_type1();
									Pref.setValue(AddressListActivity.this,"Detail_url",url);

									Intent intent =new Intent(AddressListActivity.this,ProductDetailActivity.class);
									Bundle bundle = new Bundle();
									bundle.putParcelableArrayList("productDetailArrayList", productDetailArrayList);
									intent.putExtras(bundle);
									intent.putExtra("warning","1");
									startActivity(intent);

								}
							}
						}else
						{

							Toast.makeText(AddressListActivity.this,"No record found for carton number "+str+".",Toast.LENGTH_LONG).show();
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
	private void dialog(ArrayList<String> cartuns, final String str) {
		// custom dialog

		final Dialog dialog = new Dialog(AddressListActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.cotype_detail_dialog_layout);
		dialog.setCanceledOnTouchOutside(false);
		ImageView imgcancel = (ImageView) dialog.findViewById(R.id.imgcancel);
		TextView txt_des = (TextView)dialog.findViewById(R.id.txt_des);
		ListView list_co_Type = (ListView) dialog.findViewById(R.id.list_co_Type);
		txt_des.setText("Carton number "+str+" available under multiple co type.  Select one to view carton details.");
		ListCotypeAdapter adapter = new ListCotypeAdapter(AddressListActivity.this,cartuns);
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
				String url = "http://test.yourcargoonline.com/search_script/search_app.php?cn="+str+"&co_type="+productDetailArrayList1.get(0).getCo_type1();
				Pref.setValue(AddressListActivity.this,"Detail_url",url);

				Intent intent =new Intent(AddressListActivity.this,ProductDetailActivity.class);
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
