package com.aip.targascan.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.aip.targascan.R;
import com.aip.targascan.common.util.CaptureSignatureView;

public class SignatureActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_signature);
        
        LinearLayout lnrSignature = (LinearLayout)findViewById(R.id.lnrSignature);
        final CaptureSignatureView signatureView = new CaptureSignatureView(this, null);
        lnrSignature.addView(signatureView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        
        Button btnTakeSingature = (Button)findViewById(R.id.btnTakeSginature);
        btnTakeSingature.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignatureActivity.this, MultiScanInner.class);
				intent.putExtra("sign", signatureView.getBytes());
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
        
        Button btnClearSingature = (Button)findViewById(R.id.btnClear);
        btnClearSingature.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				signatureView.ClearCanvas();
			}
		});
        
    }
}
