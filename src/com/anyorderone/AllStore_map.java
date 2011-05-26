package com.anyorderone;

import java.io.IOException;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class AllStore_map extends MapActivity 
//implements LocationListener 
{
	MapController mapController;
	double			latitude		= 39.9599990845, longitude = 75.819999694;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	EditText		txted			= null;
    	Button			btnSimple		= null;
		super.onCreate(savedInstanceState);
        setContentView(R.layout.all_store);

		// Creating TextBox displying Lat, Long
		String currentLocation = "Lat: " + latitude + " Lng: " + longitude;
        MapView mapView = (MapView) findViewById(R.id.mapview);
        GeoPoint point = new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000));
		mapView.setSatellite(true);
		mapController = mapView.getController();
		mapController.setCenter(point);
		mapController.setZoom(14);
		
        Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
        StoresMapOverlay itemizedoverlay = new StoresMapOverlay(drawable);
        List<Overlay> mapOverlays = mapView.getOverlays();

        OverlayItem overlayitem = new OverlayItem(point,"me","I am here");
        itemizedoverlay.addOverlay(overlayitem);        
        LocationListener ll = new MyLocationListener();
        System.out.println("Step 1");
        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        System.out.println("Step 2" + lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) +"-->" + lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        GeoPoint initGeoPoint = new GeoPoint((int)(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude()*1000000),
                (int)(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude()*1000000));
        System.out.println("Step 3");
        OverlayItem overlayitem2 = new OverlayItem(initGeoPoint, "Sekai, konichiwa!", "I'm in Japan!");        
        mapController.animateTo(initGeoPoint);
        itemizedoverlay.addOverlay(overlayitem2);      
        System.out.println("Step 4");
        mapOverlays.add(itemizedoverlay);		
        System.out.println("Step 5");
/*		Geocoder gc= new Geocoder(this);
        try {
        	List<Address> addresses = gc.getFromLocationName("64 Ewald Avenue, Marlboro,MA", 1);
			for (int i=0;i<addresses.size();i++){
				Address address = addresses.get(i);
				latitude = address.getLatitude();
				longitude = address.getLongitude();
				point = new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000));
				mapController.animateTo(point);
				mapController.setZoom(14);			
				overlayitem = new OverlayItem(point,address.getFeatureName(),"I am here");
				itemizedoverlay.addOverlay(overlayitem);        
				mapOverlays.add(itemizedoverlay);
				mapView.setBuiltInZoomControls(true);
				System.out.println("address-->" + address.getFeatureName()+"-->" + address.toString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/        mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return true;
	}

/*	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			GeoPoint p = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			mapController.animateTo(p);
			mapController.setCenter(p);
			mapController.setZoom(14);			
			}
		
	}
*/

	private class MyLocationListener implements LocationListener{

	      public void onLocationChanged(Location argLocation) {
	       // TODO Auto-generated method stub
	       GeoPoint myGeoPoint = new GeoPoint(
	        (int)(argLocation.getLatitude()*1000000),
	        (int)(argLocation.getLongitude()*1000000));
	       /*
	        * it will show a message on 
	        * location change
	       Toast.makeText(getBaseContext(),
	               "New location latitude [" +argLocation.getLatitude() +
	               "] longitude [" + argLocation.getLongitude()+"]",
	               Toast.LENGTH_SHORT).show();
	        */

	       mapController.animateTo(myGeoPoint);

	      }

	      public void onProviderDisabled(String provider) {
	       // TODO Auto-generated method stub
	      }

	      public void onProviderEnabled(String provider) {
	       // TODO Auto-generated method stub
	      }

	      public void onStatusChanged(String provider,
	        int status, Bundle extras) {
	       // TODO Auto-generated method stub
	      }
	     }    
}
