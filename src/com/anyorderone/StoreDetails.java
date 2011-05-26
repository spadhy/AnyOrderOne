package com.anyorderone;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

public class StoreDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_details);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    String title = extras.getString("title");
		    String address = extras.getString("address");
		    EditText titleText = (EditText) findViewById(R.id.editText1);
		    EditText addressText = (EditText) findViewById(R.id.editText2);		    
		    if (title != null) {
		    	titleText.setText(title);
		    }
		    if (address != null) {
		    	addressText.setText(address);
		    }
		}		
	}

}
