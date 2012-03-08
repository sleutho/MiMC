package com.sl.mimc;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class LatestActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latest);

		WebView view = (WebView) findViewById(R.id.webview);
		view.getSettings().setJavaScriptEnabled(true);

		
		NBAPIResponse nbapi = new NBAPIResponse();
		String response =
		nbapi.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest");
		
		// try parse the string to a JSON object
		JSONObject json = null;
		try {
			json = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String latest = null;
		String title = null;
		try {
			latest = json.getString("tag");
			json = json.getJSONObject("data");
			title = json.getString("TITLE");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		TextView text = (TextView) findViewById(R.id.textview);
		text.setText(title);
		
		view.loadUrl("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=entry&tag=" + latest);
	}
	
}
