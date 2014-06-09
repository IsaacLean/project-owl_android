package com.isaaclean.projectowl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewTransActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_trans);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	/* Called when "Submit" button is pressed */
	public void newTrans(View view) {
		EditText inputDate = (EditText) findViewById(R.id.field_date);
		String date = inputDate.getText().toString();
		EditText inputDesc = (EditText) findViewById(R.id.field_desc);
		String desc = inputDesc.getText().toString();
		EditText inputBus = (EditText) findViewById(R.id.field_bus);
		String bus = inputBus.getText().toString();
		EditText inputCat = (EditText) findViewById(R.id.field_cat);
		String cat = inputCat.getText().toString();
		EditText inputTrans = (EditText) findViewById(R.id.field_trans);
		String trans = inputTrans.getText().toString();
		EditText inputAmount = (EditText) findViewById(R.id.field_amount);
		String amount = inputAmount.getText().toString();
		
		if(date.isEmpty() || amount.isEmpty()){
			Builder alert = new AlertDialog.Builder(NewTransActivity.this);
            alert.setTitle("Invalid Input");
            alert.setMessage("The \"Date\" and \"Amount\" fields must have a value!");
            alert.setPositiveButton("OK", null);
            alert.show();
		}else{
			boolean isNotFloat = false;
			
			try{
				Float.parseFloat(amount);
			}catch(Exception e){
				isNotFloat = true;
			}
			
			if(isNotFloat == false){
				Log.d("po","yeah");
				SubmitTask loaderTask = new SubmitTask();
				loaderTask.execute();
			}
		}
	}
	
	private class SubmitTask extends AsyncTask<Void, Void, Void>{
		String submitUrl = "http://otispost.appspot.com/finance/android/submit?q=itsalivezz35";
		
		@Override
		protected Void doInBackground(Void... params){
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(submitUrl);
			
			try{
				HttpResponse response = client.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				
				if(statusCode != 200){
					return null;
				}
				
				InputStream jsonStream = response.getEntity().getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(jsonStream));
				StringBuilder builder = new StringBuilder();
				String line;
				
				while((line = reader.readLine()) != null){
					builder.append(line);
				}
			}catch(ClientProtocolException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
			return null;
		}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_new_trans,
					container, false);
			return rootView;
		}
	}

}
