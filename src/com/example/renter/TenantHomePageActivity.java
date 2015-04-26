package com.example.renter;

import com.community.renter.CommunityMainActivity;
import com.community.renter.TicketsFragment;
import com.parse.ParseUser;

import android.app.Activity;
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

public class TenantHomePageActivity extends Activity implements
		TicketListFragment.OnFragmentInteractionListener {

	TicketDetailsFragment mTicketDetailsFragment;
	Ticket mTicket;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mCommunityTitles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tenant_home_page);

		// try {
		// mTicketListFragment = new TicketListFragment();
		// getFragmentManager()
		// .beginTransaction()
		// .add(R.id.containerItem, mTicketListFragment,
		// RenterConstantVariables.TICKET_LIST_FRAGMENT)
		// .commit();
		// } catch (Exception e1) {
		// e1.printStackTrace();
		// }
		mTitle = mDrawerTitle = getTitle();
		mCommunityTitles = getResources().getStringArray(
				R.array.TenantDrawerOptions_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layoutTenant);
		mDrawerList = (ListView) findViewById(R.id.left_drawerTenant);
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
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_homepage_tenant_activity, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		int id = item.getItemId();
		
		if (id == R.id.buttonAddTicket) {
			Intent i = new Intent(TenantHomePageActivity.this,
					TenantAddTicketActivity.class);
			i.putExtra(RenterConstantVariables.ADD_TICKET,
					RenterConstantVariables.ADD_TICKET);
			startActivity(i);
		} else if (id == R.id.buttonEditTicket) {
			Intent i = new Intent(TenantHomePageActivity.this,
					TenantAddTicketActivity.class);
			i.putExtra(RenterConstantVariables.EDIT_TICKET, mTicket);
			startActivity(i);
		}

		else if (id == R.id.sortByDate) {

		} else if (id == R.id.sortByStatus) {

		}
		return super.onOptionsItemSelected(item);
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
					.replace(R.id.content_frameTenant,
							new TicketListFragment(),
							RenterConstantVariables.TICKET_LIST_FRAGMENT)
					.commit();
			break;
		case 5:
			ParseUser.logOut();
			Intent intent = new Intent(TenantHomePageActivity.this,
					LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
		default:
		}
	}

	@Override
	public void onClickingonTicket(Ticket mTicket) {

		try {
			this.mTicket = mTicket;
			mTicketDetailsFragment = TicketDetailsFragment.instanceOf(mTicket);
			getFragmentManager()
					.beginTransaction()
					.replace(R.id.content_frameTenant, mTicketDetailsFragment,
							RenterConstantVariables.TICKET_DETAILS_FRAGMENT)
					.addToBackStack(null).commit();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
