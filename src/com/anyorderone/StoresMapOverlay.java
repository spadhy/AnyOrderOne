package com.anyorderone;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class StoresMapOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	public StoresMapOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	public StoresMapOverlay(Drawable defaultMarker, Context context) {
		  super(boundCenterBottom(defaultMarker));
		  mContext = context;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}

	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	@Override
	public int size() {
	  return mOverlays.size();
	}
	
	
	@Override
	protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
	    	Intent myIntent = new Intent(mContext, StoreDetails.class);
//	    	Intent myIntent = new Intent(OrderNow.this, GPSmap.class);
	    	String title = item.getTitle();
	    	String address = item.getSnippet();
	    	myIntent.putExtra("title", title);
	    	myIntent.putExtra("address", address);	    	
	    	mContext.startActivity(myIntent);    	    	
/*
		  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		  dialog.setTitle(item.getTitle());
		  dialog.setMessage(item.getSnippet());
		  AlertDialog alert = dialog.create(); 
 		  alert.show();
*/ 		  
		  return true;
	}	
	
}
