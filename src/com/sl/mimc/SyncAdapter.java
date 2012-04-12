package com.sl.mimc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

	private static final String latestLink = "https://cgi.beuth-hochschule.de/~sleuthold/nb_api/nb_api.cgi?q=latest";
	private static final String SYNC_LATEST = "com.sl.mimc.sync";
	private static final int SYNC_LATEST_ID = 1;
	private final Context mContext;

	public SyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
	}

	public void onPerformSync(Account arg0, Bundle arg1, String arg2,
			ContentProviderClient arg3, SyncResult arg4) {

		NBAPIResponse nbapi = new NBAPIResponse();
		String response = nbapi.getText(latestLink);

		JSONObject json = null;
		try {
			if (response != null)
				json = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String title = null;
		String date = null;

		if (json != null) {
			try {
				json = json.getJSONObject("data");
				title = json.getString("TITLE");
				date = json.getString("DATE");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		if (date == null) return;

		SharedPreferences settings = mContext.getSharedPreferences(SYNC_LATEST, 0);
		String latestDateOnRecord = settings.getString("latestDateOnRecord", "");


		SimpleDateFormat format = null;
		try {
			format = new SimpleDateFormat("EEEE, LLL d, yyyy", Locale.GERMANY);
		} catch (	java.lang.IllegalArgumentException e) {
			e.printStackTrace();
		}
		finally {}
		

		boolean notify = true;
		if (format != null && latestDateOnRecord.length() > 0) {
			try {
				Date onRecord = format.parse(latestDateOnRecord);
				Date latestDate = format.parse(date);

				notify = latestDate.compareTo(onRecord) > 0;

			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
		}
		notify = true;
		if (notify) {
			//notification
			Intent intent = new Intent(mContext, MainActivity.class);
			intent.putExtra("latest", true);
			
			PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

			Notification notification = new Notification(R.drawable.ic_launcher, title, 0);
			notification.setLatestEventInfo(mContext, title, date, contentIntent);

			NotificationManager mNotificationManager = 
				(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

			mNotificationManager.notify(SYNC_LATEST_ID, notification);
		}
		
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("latestDateOnRecord", date);
		editor.commit();
	}

}
