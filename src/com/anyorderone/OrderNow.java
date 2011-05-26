package com.anyorderone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OrderNow extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_select);
        
      Button confirmButton = (Button) findViewById(R.id.button2);        
    	confirmButton.setOnClickListener(new View.OnClickListener() {
    	    public void onClick(View view) {
    	    	Intent myIntent = new Intent(OrderNow.this, StoreList.class);    	    	
//    	    	Intent myIntent = new Intent(OrderNow.this, AllStore.class);
//    	    	Intent myIntent = new Intent(OrderNow.this, GPSmap.class);
    	    	OrderNow.this.startActivity(myIntent);    	    	
    	    	
    	    	
/*    	    	String uri = "geo:"+ 39.967571 + "," + -75.532123 + "?q=pizza";
    	    	startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri)));
*/    	    	setResult(RESULT_OK);
    	        finish();
    	    }
    	});
    }
}