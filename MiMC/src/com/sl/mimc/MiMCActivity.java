package com.sl.mimc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class MiMCActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


	}

	public void onClick(View v) {
		final TextView view = (TextView) v;
		if (view != null) {
			String text = (String) view.getText();

			if (text == getText(R.string.latest)) {
				view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.contact, 0, 0);
				
				new CountDownTimer(100, 100) {
					public void onFinish() {
						
						view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.latest, 0, 0);
					}
					public void onTick(long millisUntilFinished) {}
				}.start();
				
				Intent intent = new Intent(this, LatestActivity.class);
				startActivity(intent);
				
			} else if (text == getText(R.string.categories)) {
				Intent intent = new Intent(this, ListsActivity.class);
				startActivity(intent);
			}
		}
	}
}