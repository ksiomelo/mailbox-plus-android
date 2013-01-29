package com.mailboxplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

public class ScanSendMessageActivity extends Activity {

	
	Context context;
	
	String ownerName;
	String userId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = this;
		
		setContentView(R.layout.scan_send_message); // send message screen
		
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			ownerName = extras.getString("OWNER_NAME");
			userId = extras.getString("USER_ID");
			
		}
		
    	((TextView)findViewById(R.id.title_header)).setText(ownerName);
		
		
    	findViewById(R.id.buttonSend).setOnClickListener(new OnClickListener() {
        	
			public void onClick(View arg0) {
				
				EditText textEdit = (EditText)findViewById(R.id.bodyMsg);
				String text = textEdit.getText().toString();
				
				 RequestHelper req = new RequestHelper(context);
				 boolean succ = req.send_message(text, userId);
				 
				 if (succ) {
					 textEdit.setEnabled(false);
					 ((TextView)findViewById(R.id.noticeTxt)).setText("Message sent!");
				 }
				
				
			}
		});
	}
	

	
}
