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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NewTransActivity extends Activity {
	String submitUrl = "http://cs121owl.appspot.com/finance/android/submit";

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
				//All conditions are met and the data will be input into the cloudstore
				String query = "?date=" + date + "&amount=" + amount;
				if(!desc.isEmpty())
					query += "&description=" + desc;
				if(!bus.isEmpty())
					query += "&business=" + bus;
				if(!cat.isEmpty())
					query += "&category=" + cat;
				if(!trans.isEmpty())
					query += "&transType=" + trans;
				Log.d("po",submitUrl + query);
				submitUrl += query;
				SubmitTask loaderTask = new SubmitTask();
				loaderTask.execute();
				Builder alert = new AlertDialog.Builder(NewTransActivity.this);
	            alert.setTitle("Success!");
	            alert.setMessage("You have filled out a proper transaction and it will be sent to the server!");
	            alert.setPositiveButton("OK", new DialogInterface.OnClickListener(){
	            	public void onClick(DialogInterface dialog, int d){
	            		finish();
	            	}
	            });
	            alert.show();
			}else{
				Builder alert = new AlertDialog.Builder(NewTransActivity.this);
	            alert.setTitle("Invalid Amount");
	            alert.setMessage("The \"Amount\" must be a number!");
	            alert.setPositiveButton("OK", null);
	            alert.show();
			}
		}
	}
	
	private class SubmitTask extends AsyncTask<Void, Void, Void>{		
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
		
		@Override
		protected void onPostExecute(Void result){
			submitUrl = "http://cs121owl.appspot.com/finance/android/submit"; //clear URL for next input
			super.onPostExecute(result);
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
