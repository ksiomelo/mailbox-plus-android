package com.mailboxplus;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mailboxplus.utils.ISO8601;

public class RequestHelper {
	SignInDbAdapter mSessionsDbHelper;
	private String AUTH_TOKEN  =  "";
	
	public RequestHelper(Context context){
		mSessionsDbHelper = new SignInDbAdapter(context);
        mSessionsDbHelper.open();
	}

	public boolean send_message(String text, String recipientId) {
		initializeAuthToken();
		
		DefaultHttpClient client = new DefaultHttpClient();
		String uri = Constants.BASE_URL + "/messages/send_new" ;
		HttpPost post = new HttpPost(uri);
		
		
		try {
	        
	        JSONObject obj = new JSONObject();
	        obj.put("auth_token", AUTH_TOKEN);
	        obj.put("text", text);
	        obj.put("recipient_id", recipientId);
	        obj.put("format", "json");
	        post.setEntity(new StringEntity(obj.toString(), "UTF-8"));
			
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");
		} catch (UnsupportedEncodingException e) {
			Log.e("Error", "" + e);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		String response = null;
		
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(post, responseHandler);
			Log.i("AUTHENTICATION", "Received: " + response + "!");
			
			JSONArray jObject = new JSONArray(response);
			
			return true;
//			for (int i = 0; i < jObject.length(); i++) {
//				JSONObject stp = jObject.getJSONObject(i);
//				String sType = stp.getString("stamp_type");
//				String code = stp.getString("code");
//				Date expiresAt =  ISO8601.toCalendar(stp.getString("expires_at")).getTime();
//				boolean valid = (Boolean) stp.getBoolean("valid");
//				
//				if (sType.equalsIgnoreCase("national"))
//					natStamps.add(new Stamp(sType,code,expiresAt,valid));
//				else if (sType.equalsIgnoreCase("international"))
//					intStamps.add(new Stamp(sType,code,expiresAt,valid));
//			}
			
		} catch (ClientProtocolException e) { // TODO: handle these exceptions, display appropriate msg
			e.printStackTrace();
			Log.e("ClientProtocol", "" + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IO", "" + e);
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		return true; 
		
	}
	
	public List[] buy_stamps(int nationalQte, int internationalQte) {
		initializeAuthToken();
		
		DefaultHttpClient client = new DefaultHttpClient();
		String uri = Constants.BASE_URL + "/stamps/buy/?international_qte="+internationalQte+"&national_qte="+nationalQte+"&auth_token="+AUTH_TOKEN+"&format=json" ;
		HttpGet get = new HttpGet(uri);
		
		
//		HttpParams params = new BasicHttpParams();
//		params.setParameter("auth_token", AUTH_TOKEN);
//		params.setParameter("international_qte", internationalQte);
//		params.setParameter("national_qte", nationalQte);
//		params.setParameter("format", "json");
//		
//
//
//		
//		get.setParams(params);
		get.setHeader("Accept", "application/json");
		//get.setHeader("Content-Type", "application/json");
			
			
		

		List<Stamp> natStamps = new ArrayList<Stamp>();
		List<Stamp> intStamps = new ArrayList<Stamp>();
		String response = null;
		
		try {
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(get, responseHandler);
			Log.i("AUTHENTICATION", "Received: " + response + "!");
			
			JSONArray jObject = new JSONArray(response);
			
			for (int i = 0; i < jObject.length(); i++) {
				JSONObject stp = jObject.getJSONObject(i);
				String sType = stp.getString("stamp_type");
				String code = stp.getString("code");
				Date expiresAt =  ISO8601.toCalendar(stp.getString("expires_at")).getTime();
				boolean valid = (Boolean) stp.getBoolean("valid");
				
				if (sType.equalsIgnoreCase("national"))
					natStamps.add(new Stamp(sType,code,expiresAt,valid));
				else if (sType.equalsIgnoreCase("international"))
					intStamps.add(new Stamp(sType,code,expiresAt,valid));
			}
			
		} catch (ClientProtocolException e) { // TODO: handle these exceptions, display appropriate msg
			e.printStackTrace();
			Log.e("ClientProtocol", "" + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("IO", "" + e);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			Log.e("Date parse", "" + e);
			e.printStackTrace();
		}
		
		return new List[] {natStamps,intStamps}; // returns an array of both national and international stamps
		
	}
	
	
	private void initializeAuthToken(){
	     Cursor sessionsCursor = mSessionsDbHelper.fetchAllSessions();
	     //startManagingCursor(sessionsCursor);
	     if (sessionsCursor.moveToLast()){
	    	 AUTH_TOKEN = sessionsCursor.getString(2);
	     } while (sessionsCursor.moveToNext());
	    }
	

}
