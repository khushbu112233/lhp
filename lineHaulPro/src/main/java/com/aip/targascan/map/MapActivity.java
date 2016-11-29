package com.aip.targascan.map;

import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.aip.targascan.R;
import com.aip.targascan.map.GMapV2Direction.DirecitonReceivedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapActivity extends android.support.v4.app.FragmentActivity implements OnInfoWindowClickListener, DirecitonReceivedListener {

	private GoogleMap mMap;

	LatLng startPosition;
	String startPositionTitle;

	LatLng destinationPosition;
	String destinationPositionTitle;

	double lat = 0.0, lng = 0.0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (location != null) {
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Log.d("old", "lat :  " + latitude);
			Log.d("old", "long :  " + longitude);

			startPosition = new LatLng(latitude, longitude);
			startPositionTitle = "Me";

			getLatLongFromGivenAddress(getIntent().getStringExtra("address"));

			destinationPosition = new LatLng(lat, lng);
			destinationPositionTitle = getIntent().getStringExtra("address");

			setUpMapIfNeeded();

		}

	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		// mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		mMap.setMyLocationEnabled(true);
		mMap.setIndoorEnabled(true);
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		mMap.getUiSettings().setCompassEnabled(true);
		mMap.getUiSettings().setAllGesturesEnabled(true);

		mMap.setOnInfoWindowClickListener(this);

		onDraw();

	}

	public void clearMap() {
		mMap.clear();
	}

	public void onDraw() {

		clearMap();

		View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
		TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);
		numTxt.setText("S");

		MarkerOptions mDestination = new MarkerOptions().position(destinationPosition).title(destinationPositionTitle)
				.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapActivity.this, marker)));

		numTxt.setText("E");
		MarkerOptions mStart = new MarkerOptions().position(startPosition).title(startPositionTitle)
				.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(MapActivity.this, marker)));

		mMap.addMarker(mDestination);
		mMap.addMarker(mStart);

		new GetRotueListTask(MapActivity.this, startPosition, destinationPosition, GMapV2Direction.MODE_DRIVING, this).execute();

	}

	// Convert a view to bitmap
	public static Bitmap createDrawableFromView(Context context, View view) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		// ((Activity) context).getWindowManager().getDefaultDisplay()
		// .getMetrics(displayMetrics);
		view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
		view.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		view.draw(canvas);

		return bitmap;
	}

	@Override
	public void OnDirectionListReceived(List<LatLng> mPointList) {
		if (mPointList != null) {
			PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
			for (int i = 0; i < mPointList.size(); i++) {
				rectLine.add(mPointList.get(i));
			}
			mMap.addPolyline(rectLine);

			CameraPosition mCPFrom = new CameraPosition.Builder().target(startPosition).zoom(15.5f).bearing(0).tilt(25).build();
			final CameraPosition mCPTo = new CameraPosition.Builder().target(destinationPosition).zoom(15.5f).bearing(0).tilt(50).build();

			changeCamera(CameraUpdateFactory.newCameraPosition(mCPFrom), new CancelableCallback() {
				@Override
				public void onFinish() {
					changeCamera(CameraUpdateFactory.newCameraPosition(mCPTo), new CancelableCallback() {

						@Override
						public void onFinish() {

							LatLngBounds bounds = new LatLngBounds.Builder().include(startPosition).include(destinationPosition).build();
							changeCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50), null, false);
						}

						@Override
						public void onCancel() {
						}
					}, false);
				}

				@Override
				public void onCancel() {
				}
			}, true);
		}
	}

	/**
	 * Change the camera position by moving or animating the camera depending on
	 * input parameter.
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback, boolean instant) {

		if (instant) {
			mMap.animateCamera(update, 1, callback);
		} else {
			mMap.animateCamera(update, 4000, callback);
		}
	}

	@Override
	public void onInfoWindowClick(Marker selectedMarker) {

		if (selectedMarker.getTitle().equals(startPositionTitle)) {
			Toast.makeText(this, "Marker Clicked: " + startPositionTitle, Toast.LENGTH_LONG).show();
		} else if (selectedMarker.getTitle().equals(destinationPositionTitle)) {
			Toast.makeText(this, "Marker Clicked: " + destinationPositionTitle, Toast.LENGTH_LONG).show();
		}
		selectedMarker.hideInfoWindow();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public void getLatLongFromGivenAddress(String address) {

		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

		try {
			List<Address> addresses = geoCoder.getFromLocationName(address, 1);
			if (addresses.size() > 0) {

				lat = addresses.get(0).getLatitude();
				lng = addresses.get(0).getLongitude();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
