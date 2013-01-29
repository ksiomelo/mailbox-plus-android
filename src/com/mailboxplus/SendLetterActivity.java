package com.mailboxplus;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.quietlycoding.android.picker.NumberPicker;

public class SendLetterActivity extends Activity {
	
	RequestHelper req;
	Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_letter);
		
		context = this;
		
		req = new RequestHelper(this);
		
		
		((NumberPicker)findViewById(R.id.numberNational)).setOnChangeListener(new NumberPicker.OnChangedListener() {
			
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				
				int national = newVal;
				int international = ((NumberPicker)findViewById(R.id.numberInternational)).getCurrent();
				
				TextView text = (TextView) findViewById(R.id.pricePreview);
				text.setText(Double.toString(national*Constants.PRICE_NATIONAL_STAMP + international*Constants.PRICE_INTERNATIONAL_STAMP));
			}
		}); 
		
		((NumberPicker)findViewById(R.id.numberInternational)).setOnChangeListener(new NumberPicker.OnChangedListener() {
			
			public void onChanged(NumberPicker picker, int oldVal, int newVal) {
				int national = ((NumberPicker)findViewById(R.id.numberNational)).getCurrent();
				int international = newVal;
			    
				TextView text = (TextView) findViewById(R.id.pricePreview);
				text.setText(Double.toString(national*Constants.PRICE_NATIONAL_STAMP + international*Constants.PRICE_INTERNATIONAL_STAMP));
			}
		}); 
		
		
		findViewById(R.id.buttonBuy).setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View arg0) {

				new GetStampsTask().execute(); 
				
			}
		});
		
	}
	
	
	    private class GetStampsTask extends AsyncTask<Void, Void, Void> {  
	        private ProgressDialog progressDialog;  
	        private List<Stamp>[] stamps;
	        
	        protected void onPreExecute() {  
	                progressDialog = ProgressDialog.show(SendLetterActivity.this,  
	                                  "", "Loading. Please wait...", true);  
	        }  
	        @Override  
	        protected Void doInBackground(Void... arg0) {
	                try {  
	                	
	                	int national = ((NumberPicker)findViewById(R.id.numberNational)).getCurrent();
	    				int international = ((NumberPicker)findViewById(R.id.numberInternational)).getCurrent();
	    			    
	    				stamps = req.buy_stamps(national, international);
	    				
	    				
	               } catch (Exception e) {  
	                       Log.e("GetStampsActivity", "Error loading JSON", e);  
	               }  
	               return null;  
	  }  
	  @Override  
	  protected void onPostExecute(Void result) {  
	          progressDialog.dismiss();  
	          
	          setContentView(R.layout.get_stamps);
	          
	          
	          ListView nationalListView = (ListView) findViewById(R.id.list_national_stamps);
	          StampsAdapter adapter1 = new StampsAdapter(context, R.id.list_national_stamps, stamps[0]);
	          nationalListView.setAdapter(adapter1); 
	          
	          ListView internationalListView = (ListView) findViewById(R.id.list_international_stamps);
	          StampsAdapter adapter2 = new StampsAdapter(context, R.id.list_international_stamps, stamps[1]);
	          internationalListView.setAdapter(adapter2);
	          

	  }  
	}
	    
	

	
}
