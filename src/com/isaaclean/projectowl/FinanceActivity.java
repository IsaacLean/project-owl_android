package com.isaaclean.projectowl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FinanceActivity extends Activity {
	Context context;
	String feedUrl = "http://otispost.appspot.com/finance.json";
	
	ListView itemList;
	ArrayList<String> itemArrayList = new ArrayList<String>();
	ArrayAdapter<String> itemAdapter;
	
	ListView descList;
	ArrayList<String> descArrayList = new ArrayList<String>();
	ArrayAdapter<String> descAdapter;
	
	ListView busList;
	ArrayList<String> busArrayList = new ArrayList<String>();
	ArrayAdapter<String> busAdapter;
	
	ListView catList;
	ArrayList<String> catArrayList = new ArrayList<String>();
	ArrayAdapter<String> catAdapter;
	
	ListView transList;
	ArrayList<String> transArrayList = new ArrayList<String>();
	ArrayAdapter<String> transAdapter;
	
	ListView amountList;
	ArrayList<Float> amountArrayList = new ArrayList<Float>();
	ArrayAdapter<Float> amountAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_finance);
		context = this;
		
		itemList = (ListView) findViewById(R.id.itemList);		
		itemAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, itemArrayList);
		itemList.setAdapter(itemAdapter);
		
		descList = (ListView) findViewById(R.id.descList);		
		descAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, descArrayList);
		descList.setAdapter(descAdapter);
		
		busList = (ListView) findViewById(R.id.busList);		
		busAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, busArrayList);
		busList.setAdapter(busAdapter);
		
		catList = (ListView) findViewById(R.id.catList);		
		catAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, catArrayList);
		catList.setAdapter(catAdapter);
		
		transList = (ListView) findViewById(R.id.transList);		
		transAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, transArrayList);
		transList.setAdapter(transAdapter);
		
		amountList = (ListView) findViewById(R.id.amountList);		
		amountAdapter = new ArrayAdapter<Float>(this, R.layout.finance_list_item, amountArrayList);
		amountList.setAdapter(amountAdapter);
		
		ItemListTask loaderTask = new ItemListTask();
		loaderTask.execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.finance, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class ItemListTask extends AsyncTask<Void, Void, Void>{		
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute(){
			dialog = new ProgressDialog(context);
			dialog.setTitle("Loading items...");
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params){
			HttpClient client = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(feedUrl);
			/*try {
				client.execute(new HttpGet("http://otispost.appspot.com/finance/android/submit?q=itsalive"));
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
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
				
				String jsonData = builder.toString();
				
				//Start reading JSON
				//Log.i("ProjectOwl", jsonData);
				JSONObject json = new JSONObject(jsonData);
				JSONArray items = json.getJSONArray("items");
				
				itemArrayList.add("Date");
				descArrayList.add("Desc.");
				busArrayList.add("Business");
				catArrayList.add("Category");
				transArrayList.add("Trans.");
				amountArrayList.add(Float.parseFloat("0.0"));
				
				for(int i=0; i < items.length(); ++i){
					JSONObject item = items.getJSONObject(i);
					itemArrayList.add(item.getString("date"));
					descArrayList.add(item.getString("description"));
					busArrayList.add(item.getString("business"));
					catArrayList.add(item.getString("category"));
					transArrayList.add(item.getString("transType"));
					amountArrayList.add(Float.parseFloat(item.getString("amount")));
				}
			}catch(ClientProtocolException e){
				e.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}catch(JSONException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			dialog.dismiss();
			itemAdapter.notifyDataSetChanged();
			descAdapter.notifyDataSetChanged();
			busAdapter.notifyDataSetChanged();
			catAdapter.notifyDataSetChanged();
			transAdapter.notifyDataSetChanged();
			amountAdapter.notifyDataSetChanged();
			super.onPostExecute(result);
		}
	}
}
