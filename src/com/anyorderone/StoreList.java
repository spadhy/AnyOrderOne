/*
 * Display the list of stores in the area described in list view format
 */
package com.anyorderone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.TextView;

import com.anyorderone.entities.BusinessInfo;
import com.anyorderone.helpers.BusinessInfoAdapter;
import com.anyorderone.helpers.Constants;
import com.anyorderone.helpers.InformationFetcher;

public class StoreList extends ListActivity {
	private List<BusinessInfo> businessInfos;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	// TODO Move the url value to database SQLLITE Database
	private static final String URL = "http://192.168.1.22:8080/ThisHastoWork/rest/businessinfolist/list";

	private InformationFetcher iFetcher;
	private static final String CLASSTAG = StoreList.class.getSimpleName();
	private ProgressDialog progressDialog;

	private TextView empty;
	private JSONObject json;
	private Context mCtx;

	private LocationManager locationManager;
	private LocationProvider locationProvider;
	private Double mLat, mLong;

    String zipAdd="", types = "";
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG
					+ " worker thread done, setup ListAdapter");
			progressDialog.dismiss();
			if ((businessInfos == null) || (businessInfos.size() == 0)) {
				empty.setText("No Data Found, please check with  other data");
			} else {
				BusinessInfoAdapter bInfoAdapter = new BusinessInfoAdapter(
						StoreList.this, businessInfos);
				setListAdapter(bInfoAdapter);
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG + " onCreate");
		this.setContentView(R.layout.storelist);
		this.empty = (TextView) findViewById(R.id.empty);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(this.empty);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			zipAdd = extras.getString("ZIPADD");
			types = extras.getString("TYPES").equals("ANY")?"":"&"+extras.getString("TYPES");			
		}
	}

	@SuppressWarnings("unused")
	@Override
	protected void onResume() {
		super.onResume();
		iFetcher = new InformationFetcher();
		 businessInfos = new ArrayList<BusinessInfo>();
		/*
		 * this.locationManager = (LocationManager)
		 * getSystemService(Context.LOCATION_SERVICE); this.locationProvider =
		 * this.locationManager.getProvider(LocationManager.GPS_PROVIDER); if
		 * (this.locationProvider == null) { this.locationProvider =
		 * this.locationManager.getProvider(LocationManager.NETWORK_PROVIDER); }
		 * Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG +
		 * "   locationProvider from criteria - "+ this.locationProvider); if
		 * (this.locationProvider != null) {
		 * this.locationManager.requestLocationUpdates
		 * (this.locationProvider.getName(), 0, 0, this.locationListener); }
		 * else { Log.e(Constants.LOGTAG, " " + StoreList.CLASSTAG +
		 * "  NO LOCATION PROVIDER AVAILABLE"); Toast.makeText(this,
		 * "Your application cannot continue, the GPS location provider is not available at this time."
		 * , Toast.LENGTH_SHORT).show(); finish(); }
		 */
		try {
			if (zipAdd == null || zipAdd.equals("")) {
//When search is done current location
				// json = iFetcher.connect(URL, mLat.toString(),
				// mLong.toString());
				json = iFetcher.connect();
			} else {
				//When search is done thru search page					
		        Geocoder gc= new Geocoder(getBaseContext(), Locale.getDefault());
		        String urlQueryStr = "";
		        List<Address> addresses = gc.getFromLocationName(zipAdd , 1);
//		        List<Address> addresses = gc.getFromLocationName("19380", 1);
				Double latitude= addresses.get(0).getLatitude();
				Double longitude = addresses.get(0).getLongitude();
				
				urlQueryStr = "?LATLONG=" +latitude+","+longitude +types;
	        	System.out.println("-->" + urlQueryStr);				
				json = iFetcher.connect(urlQueryStr);
			}
			fillData();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		// set the current item to the Application (global state placed there)
		AnyOrderApplication application = (AnyOrderApplication) getApplication();
		application.setCurrentBusinessInfo(this.businessInfos.get(position));
		BusinessInfo bInfo = application.getCurrentBusinessInfo();
		Log.i(Constants.LOGTAG + "app bsiness info-->", bInfo.address);
		Intent intent = new Intent(StoreList.this, StoreDetails.class);
		startActivity(intent);
	}

	private void fillData() throws JSONException {
		Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG + " filldata");
		this.progressDialog = ProgressDialog.show(this, " Working...",
				" Retrieving Businessless", true, false);
		new Thread() {
			@Override
			public void run() {
				try {
					getBusinessInfoObjects(json);
					for (int k = 0; k < businessInfos.size(); k++) {
						System.out.println("Businessinfo object-->"
								+ businessInfos.get(k).name);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	private void getBusinessInfoObjects(JSONObject json) throws JSONException {
		boolean array = true;
		JSONArray jsonarr = new JSONArray();
		BusinessInfo bInfo = new BusinessInfo();
		try{
			jsonarr = json.getJSONArray("businessInfo");
			Log.i("jsonarr Array 1-->", new Integer(jsonarr.length()).toString());
		}catch (JSONException ex) {
			array = false;
		}
		if (!array) {
			JSONObject jsonObj = json.getJSONObject("businessInfo");
			addBusinessInfo(jsonObj);
		} else {
			for (int j = 0; j < jsonarr.length(); j++) {
				JSONObject jsonObj = jsonarr.getJSONObject(j);				
				addBusinessInfo(jsonObj);
			}
		}
		Log.i(StoreList.CLASSTAG, "bInfo  1-->" + businessInfos.size());
	}

	private void addBusinessInfo(JSONObject jsonObj) throws JSONException {
		BusinessInfo bInfo = new BusinessInfo();
		Log.i("jsonObj -->", jsonObj.toString());
		bInfo.name = jsonObj.getString("businessName");
		bInfo.desc = jsonObj.getString("businessDesc");
		JSONObject jsonObjAdd = jsonObj.getJSONObject("businessAddress");
		bInfo.address = jsonObjAdd.getString("address1");
		bInfo.city = jsonObjAdd.getString("city");
		bInfo.state = jsonObjAdd.getString("state");
		bInfo.zip = jsonObjAdd.getString("zip");
		bInfo.phone = jsonObjAdd.getString("phone");
		JSONObject jsonObjCat = jsonObj.getJSONObject("businessCatalog");
		bInfo.catalogid = jsonObjCat.getString("id");
		// returnArray[j] = bInfo.toString();
		businessInfos.add(bInfo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_insert);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			/*
			 * mDbHelper.deleteNote(info.id); fillData();
			 */
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		/* fillData(); */
	}

	private final LocationListener locationListener = new LocationListener() {

		public void onLocationChanged(Location loc) {
			mLat = loc.getLatitude();
			mLong = loc.getLongitude();
			Log.v(StoreList.CLASSTAG, "Lat+Long" + mLat + "--" + mLong);

		}

		public void onProviderDisabled(final String s) {
		}

		public void onProviderEnabled(final String s) {
		}

		public void onStatusChanged(final String s, final int i, final Bundle b) {
		}
	};

}
