package com.sl.mimc;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.widget.TextView;

public class EntryActivity extends Activity {

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle data = msg.getData();

			titleTextView.setText(data.getString("title"));
			webView.loadUrl(data.getString("link"));

			progressDialog.dismiss();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);

		progressDialog = ProgressDialog.show(this,
				getText(R.string.progressTitle), getText(R.string.progressMsg));

		titleTextView = (TextView) findViewById(R.id.textview);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);

		Intent intent = getIntent();
		latest = intent.getExtras().getBoolean("latest", false);

		if (!latest) {
			entryTag = intent.getExtras().getString("entryTag");
			title = intent.getExtras().getString("entryTitle");
		}

		new Thread(new Runnable() {
			public void run() {

				NBAPIResponse nbapi = new NBAPIResponse();

				if (latest) {

					String response = nbapi
							.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest");

					JSONObject json = null;
					try {
						json = new JSONObject(response);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					try {
						entryTag = json.getString("tag");
						json = json.getJSONObject("data");
						title = json.getString("TITLE");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				Message msg = handler.obtainMessage();
				Bundle data = new Bundle();
				data.putString("title", title);
				data.putString("link", link + entryTag);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		}).start();

	}

	private ProgressDialog progressDialog;
	private TextView titleTextView;
	private WebView webView;

	private boolean latest;
	private String entryTag;
	private String title;
	
	private final String link = "https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=entry&tag=";
}
