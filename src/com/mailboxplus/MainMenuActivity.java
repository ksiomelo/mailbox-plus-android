package com.mailboxplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainMenuActivity extends Activity {
	Context context;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        context = this;
        
        findViewById(R.id.buttonInbox).setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				setContentView(R.layout.inbox);
				
			}
		});
        
		findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {
					
					public void onClick(View arg0) {
						Intent intent = new Intent(context, SendActivity.class);
                        startActivity(intent);
					}
				});
		
		findViewById(R.id.buttonCommunity).setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		findViewById(R.id.buttonScan).setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ScanActivity.class);
                startActivity(intent);				
			}
		});
        
    }
    
    // Header method to get new token
    public void onClickLogout(View v) {
    	getSharedPreferences("CurrentUser", MODE_PRIVATE).edit().clear().commit();
    	
    	SignInDbAdapter mSignInDbHelper = new SignInDbAdapter(this);
    	mSignInDbHelper.open();
    	mSignInDbHelper.deleteAllSessions();
    	mSignInDbHelper.close();
    	
    	finish();
    	Intent intent = new Intent(this, MailboxplusActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    	
    	
    }

}
