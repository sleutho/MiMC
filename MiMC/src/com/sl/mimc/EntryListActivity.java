package com.sl.mimc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
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

public class EntryListActivity extends ListActivity {

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			SimpleAdapter listAdapter = new SimpleAdapter(EntryListActivity.this, mylist,
					R.layout.entrylistrow, 
					new String[] { "title", "date" },
					new int[] { R.id.col1, R.id.col2 });

			setListAdapter(listAdapter);

			progressDialog.dismiss();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		
		Bundle bundle = getIntent().getExtras();

		TextView titleTextView = (TextView) findViewById(R.id.textview);
		titleTextView.setText(bundle.getString("entryListTitle"));
		
		serverQuery = bundle.getString("entryListQuery");

		progressDialog = ProgressDialog.show(this,
				getText(R.string.progressTitle), getText(R.string.progressMsg));

		new Thread(new Runnable() {
			public void run() {

				NBAPIResponse nbapi = new NBAPIResponse();
				String response = nbapi.getText(serverQuery);

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
						map.put("tag", obj.getString("tag"));
						JSONObject data = obj.getJSONObject("data");
						map.put("title", data.getString("TITLE"));
						map.put("date", data.getString("DATE"));
						mylist.add(map);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				handler.sendEmptyMessage(0);
			}
		}).start();
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				
				LinearLayout layout = (LinearLayout) view;
				TextView col1 = (TextView)layout.getChildAt(0);
				
				String title = (String)col1.getText();
				//find tag
				String tag = null;
				Iterator<HashMap<String, String>> itr = mylist.iterator();
			    while (itr.hasNext()) {
			    	HashMap<String, String> element = itr.next();
			        if (element.get("title") == title) {
			        	tag = element.get("tag");
			        	break;
			        }
			    }
				
				Intent intent = new Intent(EntryListActivity.this, EntryActivity.class);
				intent.putExtra("entryTitle", title);
				intent.putExtra("entryTag", tag);
				startActivity(intent);
			}
		});

	}
	private String serverQuery;
	private ProgressDialog progressDialog;
	private static ArrayList<HashMap<String, String>> mylist;
}
