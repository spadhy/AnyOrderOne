package com.anyorderone;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.anyorderone.entities.BusinessInfo;
import com.anyorderone.helpers.Constants;

public class StoreDetails extends Activity {

    private static final String CLASSTAG = StoreDetails.class.getSimpleName();
    Button btnOrderNow, btnViewOnMap, btnCallNow;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.store_details);
		AnyOrderApplication application = (AnyOrderApplication) getApplication();
		BusinessInfo bInfo = application.getCurrentBusinessInfo();
        this.btnOrderNow = (Button) findViewById(R.id.ordernow);
        this.btnViewOnMap = (Button) findViewById(R.id.viewonmap);
        this.btnCallNow = (Button) findViewById(R.id.callnow);        
		if (bInfo != null) {
		    String bDetails = bInfo.toStringWOPhone();
		    String phone = bInfo.phone;
		    Log.i(Constants.LOGTAG + " " + StoreDetails.CLASSTAG + " ",bDetails);
		    TextView detailText = (TextView) findViewById(R.id.businessDetails);
		    TextView phoneText = (TextView) findViewById(R.id.phone);		    
		    if (bDetails != null && !bDetails.equals("")) {
		    	detailText.setText(bDetails);
		    }
		    if (phone != null) {
		    	phoneText.setText(":" + phone);
		    }
		}
        this.btnOrderNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                orderNow();
            }
        });
	}

	private void orderNow() {
    	Intent myIntent = new Intent(StoreDetails.this, ItemListByCategory.class);    	    	
    	StoreDetails.this.startActivity(myIntent);    	    	
	}
}
