package com.mailboxplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.quietlycoding.android.picker.NumberPicker;

public class SendActivity extends Activity {

	Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);
		
		context=this;
		
		
	
		findViewById(R.id.buttonLetter).setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {
				Intent intent = new Intent(context, SendLetterActivity.class);
                startActivity(intent);
				//setContentView(R.layout.send_letter);
			}
		});
		
		
		findViewById(R.id.buttonPhoto).setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, 1);// 1 photo
				
				// TODO handle activity result
			}
		});
		
	}
	
}

