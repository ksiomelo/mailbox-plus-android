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

public class ScanActivity extends Activity {

	
	Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.send);
		IntentIntegrator ii = new IntentIntegrator(this);
		ii.initiateScan();
		
		
		
//		try {
//			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//	        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//	        startActivityForResult(intent, 0);
//	        
//        } catch (Exception e) {
//
//            Uri marketUri = Uri
//                    .parse("market://details?id=com.google.zxing.client.android");
//            Intent marketIntent = new Intent(Intent.ACTION_VIEW,
//                    marketUri);
//            startActivity(marketIntent);
//
//        }
		
		
	}
	
	String mailboxId;
	String userId;
	String ownerName;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
        super.onActivityResult(requestCode, resultCode, data);
        
        context=this;
        
        if (requestCode > 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.i("QRSCAN", contents);
                
                mailboxId = contents.split("#")[0];
                userId = contents.split("#")[1];
                ownerName = contents.split("#")[2];
                
                setContentView(R.layout.scan_send);
                
                ((TextView)findViewById(R.id.title_header)).setText(ownerName);
               
                
                findViewById(R.id.buttonMsg).setOnClickListener(new OnClickListener() {
                	
        			public void onClick(View arg0) {
        				
        				Intent intent = new Intent(getBaseContext(), ScanSendMessageActivity.class);
        				intent.putExtra("OWNER_NAME", ownerName);
        				intent.putExtra("USER_ID", userId);
        				startActivity(intent);
                    	
                    	
        			}
        		});
                
            }
            if(resultCode == Activity.RESULT_CANCELED){
			//handle cancel
            	finish();
			}
        }
	}
	
}
