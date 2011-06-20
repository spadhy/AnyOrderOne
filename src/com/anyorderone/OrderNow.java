package com.anyorderone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.anyorderone.helpers.PopulateMapforStartup;

public class OrderNow extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ordernow);
		putInAppContext();
		Button searchButton = (Button) findViewById(R.id.searchrest);
		searchButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent myIntent = new Intent(OrderNow.this, StoreSearch.class);
				OrderNow.this.startActivity(myIntent);
				setResult(RESULT_OK);
//				finish();
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
//				finish();
			}
		});
	
		Button testBut = (Button) findViewById(R.id.testbut);
		testBut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String uri = "geo:"+ 39.967571 + "," + -75.532123 + "?q=pizza";
    	    	startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
    	    	setResult(RESULT_OK);
				
//				Intent myIntent = new Intent(OrderNow.this, BusinessList.class);
				//Intent myIntent = new Intent(OrderNow.this, ExpandableListSample.class);
				// Intent myIntent = new Intent(OrderNow.this, AllStore.class);
				// Intent myIntent = new Intent(OrderNow.this, GPSmap.class);
//				OrderNow.this.startActivity(myIntent);
//				setResult(RESULT_OK);
//				finish();
			}
		});
	
	}
	private void putInAppContext() {
		AnyOrderApplication application = (AnyOrderApplication) getApplication();
		application.setStaticHMap(PopulateMapforStartup.getStaticValues());
	}
	
}