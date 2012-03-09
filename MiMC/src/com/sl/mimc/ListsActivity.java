package com.sl.mimc;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class ListsActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		NBAPIResponse nbapi = new NBAPIResponse();
		String response = nbapi
				.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=categories");

		// try parse the string to a JSON object
		JSONArray json = null;
		try {
			json = new JSONArray(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}


		mylist = new ArrayList<HashMap<String, String>>();
		try {
			for (int i = 0; i != json.length(); ++i) {
				JSONObject obj = json.getJSONObject(i);
				
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("col1", obj.getString("name"));
				map.put("col2", obj.getString("count"));
				mylist.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		SimpleAdapter listAdapter = new SimpleAdapter(this, mylist, R.layout.listrow,
		            new String[] {"col1", "col2"}, new int[] {R.id.col1, R.id.col2});

		setListAdapter(listAdapter);

//		ListView lv = getListView();
//		lv.setTextFilterEnabled(true);
//
//		lv.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// When clicked, show a toast with the TextView text
//				Toast.makeText(getApplicationContext(),
//						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//			}
//		});

	}
	
	private static ArrayList<HashMap<String, String>> mylist;
}
