package com.sl.mimc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
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
			
			int image = getActiveImage(text);
			view.setCompoundDrawablesWithIntrinsicBounds(0, image, 0, 0);
			final int imageActive = image - 1; 
			new CountDownTimer(70, 70) {
				public void onFinish() {
					view.setCompoundDrawablesWithIntrinsicBounds(0, imageActive, 0, 0);
				}
				public void onTick(long millisUntilFinished) {}
			}.start();

			Intent intent = null;
			if (text == getText(R.string.latest)) {
			
				intent = new Intent(this, EntryActivity.class);
				intent.putExtra("latest", true);
			
			} else if (text == getText(R.string.categories)) {
			
				intent = new Intent(this, CategoriesActivity.class);
			
			} else if (text == getText(R.string.archive)) {
			
				intent = new Intent(this, ArchiveActivity.class);
			
			} else if (text == getText(R.string.contact)) {
				
				intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("plain/text");
				intent.putExtra(android.content.Intent.EXTRA_EMAIL, "sleuthold@beuth-hochschule.de");
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "App contact");
				intent.putExtra(android.content.Intent.EXTRA_TEXT, "Send from my Android device");

				intent = Intent.createChooser(intent, "Send mail...");
			}
			startActivity(intent);
		}
	}
	
	private int getActiveImage(String text) {
		if (text == getText(R.string.latest)) {
			return R.drawable.latest_active;
		} else if (text == getText(R.string.categories)) {
			return R.drawable.categories_active;
		} else if (text == getText(R.string.archive)) {
			return R.drawable.archive_active;
		} else if (text == getText(R.string.contact)) {
			return R.drawable.contact_active;
		}
		return 0;
	}
}
