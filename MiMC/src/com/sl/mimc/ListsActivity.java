package com.sl.mimc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListsActivity extends ListActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

		String[] list = new String[json.length()];

		try {
			for (int i = 0; i != json.length(); ++i) {
				JSONObject obj = json.getJSONObject(i);
				list[i] = obj.getString("name");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		setListAdapter(new ArrayAdapter<String>(this, R.layout.list, list));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),
						((TextView) view).getText(), Toast.LENGTH_SHORT).show();
			}
		});

	}
}
