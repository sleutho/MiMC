package com.sl.mimc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ErrorNotification {

	static void noConnection(Context context) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(context.getText(R.string.noConnection))
		       .setCancelable(false)
		       .setNegativeButton(context.getText(R.string.noConnectionClose), new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}

}
