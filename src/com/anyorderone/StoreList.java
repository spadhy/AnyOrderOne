/*
 * Display the list of stores in the area described in list view format
 */
package com.anyorderone;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StoreList extends Activity {
	private List<BusinessInfo> businessInfos; 
	private static final int INSERT_ID = Menu.FIRST;
	private static final int DELETE_ID = Menu.FIRST + 1;
	// TODO Move the url value to database
	private static final String URL = "http://192.168.1.22:8080/ThisHastoWork/rest/businessinfolist";
	private InformationFetcher iFetcher;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.storelist);
		iFetcher = new InformationFetcher(this);
		JSONObject json = iFetcher.connect(URL);
		ListView listView = (ListView) findViewById(R.id.listViewStore);
		// By using setAdpater method in listview we an add string array in list.
		try {
			listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, fillData(json)));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
        // set the current item to the Application (global state placed there)
        AnyOrderApplication application = (AnyOrderApplication) getApplication();
        application.setCurrentBusinessInfo(this.businessInfos.get(position));
        Intent intent = new Intent(StoreList.this, StoreDetails.class);   
        startActivity(intent);
	}

	private String[] fillData(JSONObject json) throws JSONException {
		// A Simple JSONObject Parsing
		Log.i("Step 1 ",json.toString());
		JSONArray jsonarr = json.getJSONArray("businessInfo");
		Log.i("jsonarr Array 1-->", new Integer(jsonarr.length()).toString());
		
		String[] returnArray = new String[jsonarr.length()];
		BusinessInfo bInfo = new BusinessInfo();
		for (int j = 0; j < jsonarr.length(); j++) {
			JSONObject jsonObj = jsonarr.getJSONObject(j);
			Log.i("jsonObj -->", jsonObj.toString());
			bInfo.name = jsonObj.getString("businessName");
			bInfo.desc= jsonObj.getString("businessDesc");
			JSONObject jsonObjAdd = jsonObj.getJSONObject("businessAddress");
			Log.i("jsonarradd  1-->", jsonObjAdd.toString());
			bInfo.address= jsonObjAdd.getString("address1");
			bInfo.city= jsonObjAdd.getString("city");
			bInfo.state= jsonObjAdd.getString("state");			
			bInfo.zip= jsonObjAdd.getString("zip");			
			bInfo.phone= jsonObjAdd.getString("phone");
			Log.i("bInfo  1-->", bInfo.toString());
			returnArray[j] = bInfo.toString();
//			businessInfos.add(bInfo);
		}
		Log.i("Return array -->", returnArray.toString());
		return returnArray;
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
			createNote();
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

	private void createNote() {
		/*
		 * Intent i = new Intent(this, NoteEdit.class);
		 * startActivityForResult(i, ACTIVITY_CREATE);
		 */}

	/*
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) { //super.onListItemClick(l, v, position, id); Intent
	 * i = new Intent(this, NoteEdit.class);
	 * i.putExtra(NotesDbAdapter.KEY_ROWID, id); startActivityForResult(i,
	 * ACTIVITY_EDIT); }
	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		/* fillData(); */
	}

}
