package com.sl.mimc;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class LatestActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latest);

		WebView view = (WebView) findViewById(R.id.webview);
		
		//NBAPIResponse nbapi = new NBAPIResponse();
		//String response =
		//nbapi.getText("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest");
		
		view.loadUrl("https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=entry&tag=2012-02-26T07_07_06.txt");
	}
	
}
