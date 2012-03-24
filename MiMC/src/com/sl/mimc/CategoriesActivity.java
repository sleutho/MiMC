package com.sl.mimc;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class CategoriesActivity extends ListActivity implements DialogInterface.OnCancelListener {

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			SimpleAdapter listAdapter = new SimpleAdapter(CategoriesActivity.this, mylist,
					R.layout.listrow, 
					new String[] { "col1", "col2" },
					new int[] { R.id.col1, R.id.col2 });

			setListAdapter(listAdapter);

			progressDialog.dismiss();
			
			if (mylist.isEmpty()) {
				ErrorNotification.noConnection(CategoriesActivity.this);
			}
		}
	};
	
	public void onStop() {
		super.onStop();
		
		if (requestThread != null &&
				requestThread.isAlive())
			requestThread.interrupt();
	}
	
	public void onCancel (DialogInterface dialog) {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);

		requestThread = null;
		
		TextView titleTextView = (TextView) findViewById(R.id.textview);
		titleTextView.setText(R.string.categories);

		progressDialog = ProgressDialog.show(this,
				getText(R.string.progressTitle), getText(R.string.progressMsg), true, true, this);

		requestThread = new Thread(new Runnable() {
			public void run() {

				NBAPIResponse nbapi = new NBAPIResponse();
				String response = nbapi
						.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=categories");

				JSONArray json = null;
				try {
					if (response != null)
						json = new JSONArray(response);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				mylist = new ArrayList<HashMap<String, String>>();
				if (json != null) {
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
				}

				handler.sendEmptyMessage(0);
			}
		});
		requestThread.start();

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				
				LinearLayout layout = (LinearLayout) view;
				TextView col1 = (TextView)layout.getChildAt(0);
				
				CharSequence category = col1.getText();
				
				Intent intent = new Intent(CategoriesActivity.this, EntryListActivity.class);
				intent.putExtra("entryListTitle", category);
				intent.putExtra("entryListQuery", 
						"https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=categories&category=" + category);
				startActivity(intent);
			}
		});
		
	}
	
	private Thread requestThread;
	private ProgressDialog progressDialog;
	private static ArrayList<HashMap<String, String>> mylist;
}
