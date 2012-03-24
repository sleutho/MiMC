package com.sl.mimc;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class EntryActivity extends Activity implements DialogInterface.OnCancelListener {

	final Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			Bundle data = msg.getData();
			
			if (data.containsKey("onPageFinished"))
			{
				if (progressDialog.isShowing())
					progressDialog.dismiss();
				return;
			}

			titleTextView.setText(data.getString("title"));
			dateTextView.setText(data.getString("date"));
			webView.loadUrl(data.getString("link"));

			if (data.getString("title").length() == 0) {
				ErrorNotification.noConnection(EntryActivity.this);
			}
		}
	};
	
	public void onStop() {
		super.onStop();
		
		if (requestThread != null &&
				requestThread.isAlive())
			requestThread.interrupt();
			
		Message msg = handler.obtainMessage();
		Bundle data = new Bundle();
		data.putBoolean("onPageFinished", true);
		msg.setData(data);
		handler.sendMessage(msg);
	}
	
	public void onCancel (DialogInterface dialog) {
		finish();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entry);
		
		requestThread = null;

		progressDialog = ProgressDialog.show(this,
				getText(R.string.progressTitle), getText(R.string.progressMsg), true, true, this);

		titleTextView = (TextView) findViewById(R.id.textview);
		dateTextView = (TextView) findViewById(R.id.date);

		webView = (WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		
		webView.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url)
			{
				Message msg = handler.obtainMessage();
				Bundle data = new Bundle();
				data.putBoolean("onPageFinished", true);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		});

		

		Intent intent = getIntent();
		latest = intent.getExtras().getBoolean("latest", false);

		if (latest) {
			entryTag = "";
			title = "";
			date = "";
		} else {
			entryTag = intent.getExtras().getString("entryTag");
			title = intent.getExtras().getString("entryTitle");
			date = intent.getExtras().getString("entryDate");
		}

		requestThread = new Thread(new Runnable() {
			public void run() {

				NBAPIResponse nbapi = new NBAPIResponse();

				if (latest) {

					String response = nbapi
							.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest");

					JSONObject json = null;
					try {
						if (response != null)
							json = new JSONObject(response);
					} catch (JSONException e) {
						e.printStackTrace();
					}

					if (json != null) {
						try {
							entryTag = json.getString("tag");
							json = json.getJSONObject("data");
							title = json.getString("TITLE");
							date = json.getString("DATE");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}

				Message msg = handler.obtainMessage();
				Bundle data = new Bundle();
				data.putString("title", title);
				data.putString("date", date);
				data.putString("link", link + entryTag);
				msg.setData(data);
				handler.sendMessage(msg);
			}
		});
		requestThread.start();
	}

	private ProgressDialog progressDialog;
	private TextView titleTextView;
	private TextView dateTextView;
	private WebView webView;
	
	private Thread requestThread;

	private boolean latest;
	private String entryTag;
	private String title;
	private String date;
	
	private final String link = "https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=entry&tag=";
}
