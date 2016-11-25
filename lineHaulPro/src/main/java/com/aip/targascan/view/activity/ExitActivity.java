package com.aip.targascan.view.activity;

import roboguice.activity.RoboActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.aip.targascan.R;

public class ExitActivity extends RoboActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 
		 AlertDialog.Builder alertDialog = new AlertDialog.Builder(ExitActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("Confirm Delete...");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want to exit?");

			// Setting Icon to Dialog
			alertDialog.setIcon(R.drawable.delete);

			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
                  finish();
				// Write your code here to invoke YES event
				
				}
			});

			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,	int which) {
				// Write your code here to invoke NO event
				Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
				dialog.cancel();
				}
			});

			// Showing Alert Message
			alertDialog.show();
	}

}
