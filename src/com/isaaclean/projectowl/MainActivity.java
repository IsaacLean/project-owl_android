package com.isaaclean.projectowl;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNavTitles;
    private static int currPage = 0; //keeps track of current page for navigation
	
	private void openSettings(){
		Toast.makeText(this, "Click pressed \"Settings\"!", Toast.LENGTH_SHORT).show();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mNavTitles = getResources().getStringArray(R.array.nav_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mNavTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	// Inflate the menu items for use in the action bar
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	// The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
    	if (mDrawerToggle.onOptionsItemSelected(item)) {
    		return true;
    	}
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
    }
    
    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    
    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the page to show based on position
    	mDrawerList.setItemChecked(position, true);
        Fragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putInt(ContentFragment.ARG_POSITION, position);
        Log.d("ProjectOwl", Integer.toString(position));
        if(position == 1){
        	Intent intent = new Intent(this, FinanceActivity.class);
        	startActivity(intent);
        }else{
	        fragment.setArguments(args);
	
	        // Insert the fragment by replacing any existing fragment
	        FragmentManager fragmentManager = getFragmentManager();
	        fragmentManager.beginTransaction()
	                       .replace(R.id.content_frame, fragment)
	                       .commit();
	
	        // Update the title and close the drawer
	        setTitle(mNavTitles[position]);
	        mDrawerLayout.closeDrawer(mDrawerList);
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    /* Disable back button */
    @Override
    public void onBackPressed() {}
    
    public void sendMsg(View view) {
    	if(currPage == 0){
    		Toast.makeText(this, "hiii", Toast.LENGTH_SHORT).show();
    	}else{
    		Toast.makeText(this, "byeee", Toast.LENGTH_SHORT).show();
    	}
    }
    
    /**
     * Fragment that appears in the "content_frame", shows a page
     */
    public static class ContentFragment extends Fragment {
        public static final String ARG_POSITION = "nav_number";

        // Empty constructor required for fragment subclasses
        public ContentFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {            
            int i = getArguments().getInt(ARG_POSITION);
            currPage = i;
            String page = getResources().getStringArray(R.array.nav_array)[i];
            View rootView;
            
            if(currPage == 0){
            	rootView = inflater.inflate(R.layout.fragment_content, container, false);
            }else{
            	rootView = inflater.inflate(R.layout.fragment_content2, container, false);
            }

            /*Log.d("ProjectOwl", Integer.toString(i));
            Log.d("ProjectOwl", page);
            Log.d("ProjectOwl", Integer.toString(currPage));*/
            getActivity().setTitle(page);
            return rootView;
        }
    }
}
