package com.anyorderone;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.anyorderone.AllStore.MyLocationListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class LocationHelper {

    public static final String CLASSTAG = LocationHelper.class.getSimpleName();

    public static final double MILLION = 1e6;

    private static final DecimalFormat DEC_FORMAT = new DecimalFormat("###.##");

    public static final GeoPoint GOLDEN_GATE = new GeoPoint((int) (37.49 * LocationHelper.MILLION),
        (int) (-122.49 * LocationHelper.MILLION));

	private LocationManager lm;
	private LocationListener ll;
    
    // note GeoPoint stores lat/long as "integer numbers of microdegrees"
    // meaning int*1E6
    // parse Location into GeoPoint
    public static GeoPoint getGeoPoint(final Location loc) {
        int lat = (int) (loc.getLatitude() * LocationHelper.MILLION);
        int lon = (int) (loc.getLongitude() * LocationHelper.MILLION);
        return new GeoPoint(lat, lon);
    }

    // parse geoRssPoint into GeoPoint(<georss:point>36.835 -121.899</georss:point>)
    public static GeoPoint getGeoPoint(final String geoRssPoint) {
        Log.d(Constants.LOGTAG, LocationHelper.CLASSTAG + " getGeoPoint - geoRssPoint - " + geoRssPoint);
        GeoPoint returnPoint = null;
        String gPoint = geoRssPoint.trim();
        if (gPoint.indexOf(" ") != -1) {
            String latString = gPoint.substring(0, gPoint.indexOf(" "));
            String lonString = gPoint.substring(gPoint.indexOf(" "), gPoint.length());
            double latd = Double.parseDouble(latString);
            double lond = Double.parseDouble(lonString);
            int lat = (int) (latd * LocationHelper.MILLION);
            int lon = (int) (lond * LocationHelper.MILLION);
            returnPoint = new GeoPoint(lat, lon);
        }
        return returnPoint;
    }

    // parse geoRssPoint into GeoPoint(<georss:point>36.835 -121.899</georss:point>)
    public static ArrayList<Integer> getTestLocation(Context context) {
        Log.d(Constants.LOGTAG, LocationHelper.CLASSTAG + " getGeoPoint - geoRssPoint - ");
		double	latitude=39.967571,longitude = -75.532123;
		//latitude=39.819999694 ,longitude = -75.9599990845;
/*
        Geocoder gc= new Geocoder(context);
        List<Address> addresses;
        Address address = null;
        System.out.println("GET TEST LOCATION 1");
		try {
			addresses = gc.getFromLocationName("289 Live Oak Lane, West Chester,PA", 1);
			address = addresses.get(0);
			System.out.println("Adress name -->" + address.getFeatureName());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		latitude = address.getLatitude();
		longitude = address.getLongitude();
		*/
        ArrayList<Integer> point=new ArrayList<Integer>();
        try {
			point.add(new Integer((int)latitude));
			point.add(new Integer((int)longitude));
			}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        return point;
    }

    // parse double point(-127.50) into String (127.50W)
    public static String parsePoint(final double point, final boolean isLat) {
        Log.d(Constants.LOGTAG, LocationHelper.CLASSTAG + " parsePoint - point - " + point + " isLat - " + isLat);
        String result = LocationHelper.DEC_FORMAT.format(point);
        if (result.indexOf("-") != -1) {
            result = result.substring(1, result.length());
        }
        // latitude is decimal expressed as +- 0-90
        // (South negative, North positive, from Equator)
        if (isLat) {
            if (point < 0) {
                result += "S";
            } else {
                result += "N";
            }
        }
        // longitude is decimal expressed as +- 0-180
        // (West negative, East positive, from Prime Meridian)
        else {
            if (point < 0) {
                result += "W";
            } else {
                result += "E";
            }
        }
        Log.d(Constants.LOGTAG, LocationHelper.CLASSTAG + " parsePoint result - " + result);
        return result;
    }
    
    public  String getCurrentLocation(Context context) {
    	//Change this to retreive current location later
        Log.d(Constants.LOGTAG, LocationHelper.CLASSTAG + " getGeoPoint - geoRssPoint - ");
		double	latitude=39.967571,longitude = -75.532123;
		
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		ll = new MyLocationListener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
		Object lat = null;
		
		try{
			lat = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
		}catch(Exception ex){
			lat =null;
		}

		if (lat == null){
			ArrayList<Integer> point = LocationHelper.getTestLocation(context);
			latitude=point.get(0);
			longitude = point.get(1);
		} else {
			latitude=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
			longitude=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
		}
		
		System.out.println("Latitude -->" + latitude + "-->Longitude" + longitude);
		
        return latitude+","+longitude;
    }
    
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location argLocation) {
			// TODO Auto-generated method stub
			GeoPoint myGeoPoint = new GeoPoint(
					(int) (argLocation.getLatitude()),
					(int) (argLocation.getLongitude()));
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

    
}
