package com.aip.targascan.view.frag;

import me.dm7.barcodescanner.core.CameraUtils;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.aip.targascan.R;
import com.aip.targascan.common.util.L;
import com.google.zxing.Result;

public class SimpleScannerActivity extends ActionBarActivity implements ZXingScannerView.ResultHandler, OnClickListener {
	private ZXingScannerView mScannerView;
	
	private ImageView mFlash;
	
	private boolean isFlashSupported;
	
	private boolean isFlashOn;

	@Override
	public void onCreate(Bundle state) {
		super.onCreate(state);
		setContentView(R.layout.layout_camera_scan);
		mScannerView = (ZXingScannerView)findViewById(R.id.scanner);
		mFlash = (ImageView)findViewById(R.id.imgFlash);
		
		mFlash.setOnClickListener(this);
		
		isFlashSupported = CameraUtils.isFlashSupported(getApplicationContext());
		
		isFlashOn = false;
		mFlash.setImageResource(R.drawable.ic_flash_off);
	}

	@Override
	public void onResume() {
		super.onResume();
		mScannerView.setResultHandler(this);
		mScannerView.startCamera();
	}

	@Override
	public void onPause() {
		super.onPause();
		mScannerView.stopCamera();
	}

	@Override
	public void handleResult(Result rawResult) {
		if(rawResult!= null && rawResult.getText() != null)
		{
			Intent intent = new Intent();
			intent.putExtra("result", rawResult.getText());
			setResult(RESULT_OK, intent);
			mScannerView.stopCamera();
			SimpleScannerActivity.this.finish();
		}else
			mScannerView.startCamera();
		/*Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();*/
	}

	@Override
	public void onClick(View v) {

		if(isFlashSupported)
		{
			if(isFlashOn)
			{
				mFlash.setImageResource(R.drawable.ic_flash_off);
				isFlashOn = false;
			}else{
				mFlash.setImageResource(R.drawable.ic_flash_on);
				isFlashOn = true;
			}
			mScannerView.toggleFlash();
			
		}else
			L.error(getApplicationContext(), "Oops!! Flash light is not supported in your device");
	}
	
}
