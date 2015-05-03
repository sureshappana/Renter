package com.community.renter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.classes.renter.Ticket;
import com.common.renter.CommonFunctions;
import com.common.renter.FacilitiesFragment;
import com.common.renter.LoginActivity;
import com.common.renter.RenterConstantVariables;
import com.common.renter.SettingsFragment;
import com.common.renter.TicketDetailsFragment;
import com.common.renter.TicketListFragment;
import com.example.renter.R;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class CommunityMainActivity extends Activity implements
		TicketListFragment.OnFragmentInteractionListener {

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

		CommonFunctions.UserTableClass.CurrentUserDetails();
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

	@Override
	public void onBackPressed() {
		Fragment myFragment = (Fragment) getFragmentManager()
				.findFragmentByTag(RenterConstantVariables.TICKET_LIST_FRAGMENT);
		if (myFragment instanceof TicketListFragment) {
			finish();
		} else {
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new TicketListFragment(),
							RenterConstantVariables.TICKET_LIST_FRAGMENT)
					.commit();
		}

	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		return super.onOptionsItemSelected(item);
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

		switch (position) {
		case 0:
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new TicketListFragment(),
							RenterConstantVariables.TICKET_LIST_FRAGMENT)
					.commit();
			break;
		case 1:
			gotoFlatInfoFragment();
			break;
		case 2:
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new FacilitiesFragment(),
							"facilites").commit();
			break;
		case 3:
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new NotificationsFragment(),
							"notifications").commit();
			break;
		case 4: // Payments fragment break; case 5:

			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, new SettingsFragment(),
							"settings").commit();
			break;

		case 5: // Signout fragment break; default:
			ParseUser.logOut();
			ParseInstallation installation = ParseInstallation
					.getCurrentInstallation();
			installation.put("channels", new ArrayList<String>());
			installation.saveInBackground();

			Intent intent = new Intent(CommunityMainActivity.this,
					LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		}

		Log.d("renter", "reached to selectitem()" + position);
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

	public void gotoFlatInfoFragment() {
		getFragmentManager()
				.beginTransaction()
				.replace(R.id.content_frame, new FlatInfoFragment(), "flatinfo")
				.commit();

	}

	@Override
	public void onClickingonTicket(Ticket mTicket) {
		try {
			TicketDetailsFragment mTicketDetailsFragment = TicketDetailsFragment
					.instanceOf(mTicket);
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frame, mTicketDetailsFragment,
							RenterConstantVariables.TICKET_DETAILS_FRAGMENT)
					.addToBackStack(null).commit();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
