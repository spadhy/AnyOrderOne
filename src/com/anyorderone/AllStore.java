package com.anyorderone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.anyorderone.helpers.LocationHelper;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class AllStore extends MapActivity
// implements LocationListener
{
	private MapView mapView;
	private LocationManager lm;
	private LocationListener ll;
	private MapController mc;
	GeoPoint p = null;
	Drawable defaultMarker = null;
	StoresMapOverlay itemizedoverlay;
	List<Overlay> mapOverlays;	
	Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_store);
		mContext = getBaseContext();
		mapView = (MapView) findViewById(R.id.mapview);
		// show zoom in/out buttons
		mapView.setBuiltInZoomControls(true);
		// Standard view of the map(map/sat)
		mapView.setSatellite(false);
		// get controller of the map for zooming in/out
		mc = mapView.getController();
		// Zoom Level
		mc.setZoom(14);
		
/* USE THIS FOR DEFAULT BEHAVIOUR
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.redpin);
		StoresMapOverlay itemizedoverlay = new StoresMapOverlay(drawable);			
		GeoPoint point = new GeoPoint(39967571,-75532123);
		OverlayItem overlayitem = new OverlayItem(point, "Hellow!", "West Chester City!");		
		GeoPoint point2 = new GeoPoint(19240000,-99120000);
		OverlayItem overlayitem2 = new OverlayItem(point2, "Hola", "I'm in Mexico!");	
		itemizedoverlay.addOverlay(overlayitem);
		itemizedoverlay.addOverlay(overlayitem2);
		mapOverlays.add(itemizedoverlay);	
*/		
		
		double			latitude=35.967571 ,longitude = -89.532123;			
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		Object lat = null;
		
		try{
			lat = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		}catch(Exception ex){
			lat =null;
		}

		if (lat == null){
			ArrayList<Integer> point = LocationHelper.getTestLocation(mContext);
			latitude=point.get(0);
			longitude = point.get(1);
		} else {
			latitude=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
			longitude=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		}
		
		// Get the current location in start-up
		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.redpin);
		StoresMapOverlay itemizedoverlay = new StoresMapOverlay(drawable,this);			

		GeoPoint initGeoPoint = new GeoPoint(((int)latitude * 1000000),((int)longitude * 1000000));
        OverlayItem overlayitem = new OverlayItem(initGeoPoint,"me","I am here");
        itemizedoverlay.addOverlay(overlayitem);
        initGeoPoint = new GeoPoint(39967571,-75532123);
        overlayitem = new OverlayItem(initGeoPoint,"2ndpoint","I am here");
        itemizedoverlay.addOverlay(overlayitem);  
//GEOCODE SPECIFIC CODE (REMOVE WHEN NOT NECESSARY        
        Geocoder gc= new Geocoder(mContext, Locale.getDefault());
		System.out.println("Step 3");
        try {
        	List<Address> addresses = gc.getFromLocationName("Pizza+near+west chester,pa", 10);
//        	List<Address> addresses = gc.getFromLocationName("Pizza", 10, 39.931, -75.593, 39.993, -75.472);        	
			System.out.println("Step 4" + addresses.size());
			for (int i=0;i<addresses.size();i++){
				Address address = addresses.get(i);
				latitude = address.getLatitude();
				longitude = address.getLongitude();
				System.out.println("Step 411-->" + latitude + "---" + longitude);
				GeoPoint point = new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000));
				System.out.println("Step 5-->" + address.getFeatureName());
				overlayitem= new OverlayItem(point,address.getFeatureName(),address.getAddressLine(i));
				itemizedoverlay.addOverlay(overlayitem);        
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
//GEOCODE SPECIFIC CODE (REMOVE WHEN NOT NECESSARY        
//        mapOverlays.add(itemizedoverlay);		
        
        mapOverlays.add(itemizedoverlay);	
		mc.animateTo(initGeoPoint);
		
	}

	protected class MyLocationOverlay extends com.google.android.maps.Overlay {

		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
				long when) {
			Paint paint = new Paint();

			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();

			mapView.getProjection().toPixels(p, myScreenCoords);

			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);

			Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.icon);

			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("I am here...", myScreenCoords.x, myScreenCoords.y,
					paint);
			return true;
		}
	}

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude() * 1000000),
					(int) (argLocation.getLongitude() * 1000000));
			/*
			 * it will show a message on location change
			 * Toast.makeText(getBaseContext(), "New location latitude ["
			 * +argLocation.getLatitude() + "] longitude [" +
			 * argLocation.getLongitude()+"]", Toast.LENGTH_SHORT).show();
			 */
			System.out.println("Step 1");
	        OverlayItem overlayitem = new OverlayItem(myGeoPoint,"me","I am here");
	        itemizedoverlay.addOverlay(overlayitem);
			System.out.println("Step 2");			
			mc.animateTo(myGeoPoint);
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
		}
	}

	protected boolean isRouteDisplayed() {
		return false;
	}
}