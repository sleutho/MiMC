package com.sl.mimc;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;

public class LoginActivity extends AccountAuthenticatorActivity {

	protected void onCreate (Bundle icicle)
	{
		super.onCreate(icicle);
		
		String userName = "Anonymous account";
		String password = "password";
		Account newAccount = new Account(userName, "com.sl.mimc.account");
	    AccountManager accountManager = AccountManager.get(this);
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
