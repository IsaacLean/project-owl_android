package com.isaaclean.projectowl;

import java.io.IOException;

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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginActivity extends Activity {
	String username = "";
	String password = "";
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		context = this;

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
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
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			return rootView;
		}
	}
	
	public void submitLogin(View view){
		EditText inputUsername = (EditText) findViewById(R.id.field_username);
		username = inputUsername.getText().toString();
		EditText inputPassword = (EditText) findViewById(R.id.field_password);
		password = inputPassword.getText().toString();
		
		SubmitTask loaderTask = new SubmitTask();
		loaderTask.execute();
	}
	
	private class SubmitTask extends AsyncTask<Void, Void, Void>{	
		@Override
		protected Void doInBackground(Void... params){
			HttpClient client = new DefaultHttpClient();
			Log.d("po", "http://cs121owl.appspot.com/login/android/submit?username=" + username + "&password=" + password);
			HttpGet getRequest = new HttpGet("http://cs121owl.appspot.com/login/android/submit?username=" + username + "&password=" + password);
			
			try{
				HttpResponse response = client.execute(getRequest);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				
				if(statusCode != 200){
					/*Builder alert = new AlertDialog.Builder(LoginActivity.this);
		            alert.setTitle("Incorrect Login");
		            alert.setMessage("Your login credentials were incorrect!");
		            alert.setPositiveButton("OK", null);
		            alert.show();*/
					Log.d("po","This happened :(");
					return null;
				}else{
					Log.d("po", "YEAH!");
					Intent intent = new Intent(context, MainActivity.class);
				    startActivity(intent);
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
			Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setTitle("Incorrect Login");
            alert.setMessage("Your login credentials were incorrect!");
            alert.setPositiveButton("OK", null);
            alert.show();
			super.onPostExecute(result);
		}
	}

}
