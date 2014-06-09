package com.isaaclean.projectowl;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FinanceActivity extends Activity {
	ListView itemList;
	String[] itemArray = {"Currently no items"};
	ArrayAdapter<String> itemAdapter;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_finance);
		context = this;
		
		itemList = (ListView) findViewById(R.id.itemList);		
		itemAdapter = new ArrayAdapter<String>(this, R.layout.finance_list_item, itemArray);
		itemList.setAdapter(itemAdapter);
		
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
			try{
				Thread.sleep(3000);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result){
			dialog.dismiss();
			super.onPostExecute(result);
		}
	}
}
