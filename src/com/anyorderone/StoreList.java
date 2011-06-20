/*
 * Display the list of stores in the area described in list view format
 */
package com.anyorderone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
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

import com.anyorderone.adapters.BusinessInfoAdapter;
import com.anyorderone.entities.BusinessInfo;
import com.anyorderone.helpers.Constants;
import com.anyorderone.helpers.InformationFetcher;
import com.anyorderone.helpers.LocationHelper;
import com.anyorderone.helpers.Utility;

public class StoreList extends ListActivity {
	private List<BusinessInfo> businessInfos;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	// TODO Move the url value to database SQLLITE Database

	private InformationFetcher iFetcher;
	private static final String CLASSTAG = StoreList.class.getSimpleName();
	private ProgressDialog progressDialog;

	private TextView empty;
	private JSONObject json;

	private boolean filldata = true;
	String zipAdd = "", types = "", names = "";
	
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {
			Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG + " worker thread done, setup ListAdapter");
			progressDialog.dismiss();
			if (!filldata) {
				StringBuilder validationText = new StringBuilder();
				validationText.append(getResources().getString(R.string.unable_to_fetch_businesslist));
				new AlertDialog.Builder(getBaseContext()).setTitle(getResources().getString(R.string.alert_title))
						.setMessage(validationText.toString())
						.setPositiveButton("Continue", new android.content.DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int arg1) {
								// in this case, don't need to do anything other
								// than close alert
							}
						}).show();
				validationText = null;
			}
			if ((businessInfos == null) || (businessInfos.size() == 0)) {
				empty.setText("No Data Found, please check with  other data");
			} else {
				BusinessInfoAdapter bInfoAdapter = new BusinessInfoAdapter(StoreList.this, businessInfos);
				setListAdapter(bInfoAdapter);
			}
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(Constants.LOGTAG, " " + StoreList.CLASSTAG + " onCreate");
		this.setContentView(R.layout.storelist);
		this.empty = (TextView) findViewById(R.id.empty);
		final ListView listView = getListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setEmptyView(this.empty);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			zipAdd = Utility.checkNullString(extras.getString("ZIPADD"));
			if (!zipAdd.equals("") ){
				names = Utility.checkNullString(extras.getString("NAMES"));
				names = names.equals("ANY") ? "" : "&names=" + extras.getString("NAMES");
				if (names.equals("")) {
					types = Utility.checkNullString(extras.getString("TYPES"));
					System.out.println("TYPES-->" + types);
					types = (types.equals("ANY") || types.equals("null"))?"":"&types=" + types;
				} else {
					types = "&types=food";
				}
				Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "zipadd--" + zipAdd + "--types--" + types + "--names--"
						+ names);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		iFetcher = new InformationFetcher();
		businessInfos = new ArrayList<BusinessInfo>();
		StringBuilder validationText = new StringBuilder();
		boolean valid = true;
		try {
			String urlQueryStr = "?LATLONG=39.9658560,-75.5271650";

			if (zipAdd.equals("") ){
				// TODO When search is done current location, get the current
				// location and form the URLQUERYSTRING

				urlQueryStr = "?LATLONG=" + getLatLong() +"&types=food";
				Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "URLQUERY STRING inside location based-->" + urlQueryStr);
				json = iFetcher.connect(urlQueryStr);
			} else {
				// When search is done thru search page
				Geocoder gc = new Geocoder(getBaseContext(), Locale.getDefault());

				List<Address> addresses = gc.getFromLocationName(zipAdd, 1);
				Double latitude = addresses.get(0).getLatitude();
				Double longitude = addresses.get(0).getLongitude();

				urlQueryStr = "?LATLONG=" + latitude + "," + longitude + types + names;
				Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "URLQUERY STRING inside search based-->" + urlQueryStr);
				json = iFetcher.connect(urlQueryStr);
			}
			if (validJson()){
				fillData();				
			} else {
				valid = false;
				validationText.append(getResources().getString(R.string.unable_to_fetch_businesslist));
			}

		} catch (JSONException e) {
			Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "Inside JSONException");
			e.printStackTrace();
			validationText.append(getResources().getString(R.string.unable_to_fetch_businesslist));
			valid = false;
		} catch (IOException e) {
			Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "Inside IOExeption");
			e.printStackTrace();
			validationText.append(getResources().getString(R.string.unable_to_fetch_businesslist));
			valid = false;
		} catch (Exception e) {
			Log.i(Constants.LOGTAG + StoreList.CLASSTAG, "Inside Exception");
			e.printStackTrace();
			validationText.append(getResources().getString(R.string.unable_to_fetch_businesslist));
			valid = false;
		}
		if (!valid) {
			Intent myIntent = new Intent(StoreList.this, StartNow.class);
			myIntent.putExtra("ERROR_VALUE", "Unable to Fetch List of Businesses for this location");
			StoreList.this.startActivity(myIntent);
			setResult(RESULT_OK);			
		}
	}

	@SuppressWarnings("unused")
	private boolean validJson() {
		boolean valid = true;
		try {
			JSONArray jsonarr = json.getJSONArray("businessInfo");
			Log.i("jsonarr Array 1-->", new Integer(jsonarr.length()).toString());
		} catch (JSONException ex) {
			Log.i("Inside JSONException", ex.getMessage());			
			valid = false;
		}
		try {
			if (!valid) {
				valid=true;
				JSONObject jsonObj = json.getJSONObject("businessInfo");
			}
		}catch (JSONException ex) {
			Log.i("Inside JSONException for Object", ex.getMessage());			
			valid = false;
		}
		return valid;
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

	private void fillData() throws Exception {
		Log.v(Constants.LOGTAG, " " + StoreList.CLASSTAG + " filldata");
		this.progressDialog = ProgressDialog.show(this, " Working...", " Retrieving Businessless", true, false);
		UnCaughtExceptionHandler uceh = new UnCaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(uceh);

		new Thread() {
			@Override
			public void run() {
				try {
					getBusinessInfoObjects(json);
					for (int k = 0; k < businessInfos.size(); k++) {
						System.out.println("Businessinfo object-->" + businessInfos.get(k).name);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					progressDialog.dismiss();
					throw new RuntimeException("Thrown from new Thread JSONException");
				} catch (Throwable t) {
					// TODO Auto-generated catch block
					t.printStackTrace();
					progressDialog.dismiss();
					throw new RuntimeException("Thrown from new Thread Exception");
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	class UnCaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
		public void uncaughtException(Thread t, Throwable e) {
			System.err.println("Throwable: " + e.getMessage());
			System.err.println(t.toString());
			filldata = false;
		}
	}

	private void getBusinessInfoObjects(JSONObject json) throws JSONException {
		boolean array = true;
		JSONArray jsonarr = new JSONArray();
		try {
			jsonarr = json.getJSONArray("businessInfo");
			Log.i("jsonarr Array 1-->", new Integer(jsonarr.length()).toString());
		} catch (JSONException ex) {
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
		bInfo.imgLocation = jsonObj.getString("imageLocation");
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
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			/*
			 * mDbHelper.deleteNote(info.id); fillData();
			 */
			return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		/* fillData(); */
	}
	
	private String getLatLong() {
		LocationHelper lh = new LocationHelper();
		String latLong = lh.getCurrentLocation(getBaseContext());
		return latLong;
	}	

}
