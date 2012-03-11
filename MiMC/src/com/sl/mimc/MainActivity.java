package com.sl.mimc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.latest).setOnTouchListener(this);
		findViewById(R.id.contact).setOnTouchListener(this);
		findViewById(R.id.archive).setOnTouchListener(this);
		findViewById(R.id.categories).setOnTouchListener(this);
	}

	public void onClick(View v) {
		final TextView view = (TextView) v;
		if (view != null) {
			String text = (String) view.getText();
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
				intent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { "sleuthold@beuth-hochschule.de" });
				intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
						"App contact");

				intent = Intent.createChooser(intent,
						getText(R.string.sendMail));
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

	public boolean onTouch(View v, MotionEvent event) {

		if (!(v instanceof TextView)) {
			return false;
		}
		
		final TextView view = (TextView) v;
		
		String text = (String) view.getText();
		int image = getActiveImage(text);

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			view.setCompoundDrawablesWithIntrinsicBounds(0, image, 0, 0);
			return false;
		case MotionEvent.ACTION_MOVE:
			int[] location = new int[2];
			view.getLocationOnScreen(location);
			int px = (int)event.getRawX();
			int py = (int)event.getRawY();
			if (location[1] > py ||
					location[0] > px ||
					location[1] + view.getHeight() < py ||
					location[0] + view.getWidth() < px) {
				view.setCompoundDrawablesWithIntrinsicBounds(0, image - 1, 0, 0);
			}
			return false;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			view.setCompoundDrawablesWithIntrinsicBounds(0, image - 1, 0, 0);
			return false;
		}
		return false;
	}
}
