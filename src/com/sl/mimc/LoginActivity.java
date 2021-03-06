package com.sl.mimc;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class LoginActivity extends AccountAuthenticatorActivity {

	protected void onCreate (Bundle icicle)
	{
		super.onCreate(icicle);
		
		AccountManager accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType("com.sl.mimc.account");

		if (accounts.length > 0) {

			runOnUiThread(new Runnable() {
				public void run() {
					// Display toast for "Only one account supported."
					// Redirect to account management.
					Toast.makeText(LoginActivity.this,
							R.string.oneAccount, Toast.LENGTH_LONG).show();
				}
			});
			Intent i = new Intent();
			this.setResult(RESULT_CANCELED, i);

			finish();
		}
		
		String userName = "Anonymous";
		String password = "password";
		Account newAccount = new Account(userName, "com.sl.mimc.account");
	    accountManager.addAccountExplicitly(newAccount,password, null);
		
	    Intent i = new Intent();
	    i.putExtra(AccountManager.KEY_ACCOUNT_NAME, userName);
	    i.putExtra(AccountManager.KEY_ACCOUNT_TYPE, "com.sl.mimc.account");
	    i.putExtra(AccountManager.KEY_AUTHTOKEN,"com.sl.mimc.account");
	    i.putExtra(AccountManager.KEY_PASSWORD, password);
	    this.setAccountAuthenticatorResult(i.getExtras());
	    this.setResult(RESULT_OK, i);
		
		finish();
	}

}
