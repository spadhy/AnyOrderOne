package com.anyorderone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderNow extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rest_select);
		Button searchButton = (Button) findViewById(R.id.searchrest);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(OrderNow.this, StoreSearch.class);
				OrderNow.this.startActivity(myIntent);
				setResult(RESULT_OK);
				finish();
			}
		});
		
		Button confirmButton = (Button) findViewById(R.id.listrest);
		confirmButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(OrderNow.this, StoreList.class);
				// Intent myIntent = new Intent(OrderNow.this, AllStore.class);
				// Intent myIntent = new Intent(OrderNow.this, GPSmap.class);
				OrderNow.this.startActivity(myIntent);
				setResult(RESULT_OK);
				finish();
			}
		});
	}
}