package com.community.renter;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.renter.R;

public class CommunityMainActivity extends Activity implements FlatInfoFragment.OnFragmentInteractionListener{

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCommunityTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_main);

		mTitle = mDrawerTitle = getTitle();
		mCommunityTitles = getResources().getStringArray(
				R.array.CommunityDrawerOptions_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mCommunityTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); 
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); 
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d("renter", "drawer item clicked");
			selectItem(position);
		}
	}

	private void selectItem(int position) {
	
		/*switch(position){
		case 0:
			getFragmentManager().beginTransaction()
	        .replace(R.id.content_frame, new TicketsFragment(), "tickets")
	        .addToBackStack(null)
	        .commit();
			break;
		case 1:
			getFragmentManager().beginTransaction()
	        .replace(R.id.content_frame, new FlatInfoFragment(), "flatinfo")
	        .addToBackStack(null)
	        .commit();
			break;
		case 2:
			getFragmentManager().beginTransaction()
	        .replace(R.id.content_frame, new FacilitiesFragment(), "facilites")
	        .addToBackStack(null)
	        .commit();
			break;
		case 3:
			getFragmentManager().beginTransaction()
	        .replace(R.id.content_frame, new NotificationsFragment(), "notifications")
	        .addToBackStack(null)
	        .commit();
			break;
		case 4: // Payments fragment
			break;
		case 5:
			getFragmentManager().beginTransaction()
	        .replace(R.id.content_frame, new SettingsFragment(), "settings")
	        .addToBackStack(null)
	        .commit();
			break; 
		case 6: //Signout fragment
			break;
		default:
			
		}*/
		Log.d("renter","reached to selectitem()"+position);
		mDrawerList.setItemChecked(position, true);
		setTitle(mCommunityTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}


	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void gotoAddFlatFragment() {
		// TODO Auto-generated method stub
		
	}
}
