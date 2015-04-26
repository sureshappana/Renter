package com.community.renter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class FlatInfoFragment extends Fragment {
	OnFragmentInteractionListener mListener;

	
	
	public FlatInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_community_flat_info,
				container, false);
		setHasOptionsMenu(true);
		
		return view;
	}
	public interface OnFragmentInteractionListener{
		public void gotoAddFlatFragment();
		public void flatSelected(String flatNumber);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.flats_add_menu, menu);
	    super.onCreateOptionsMenu(menu, inflater);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        case R.id.action_addFlat:     
        	mListener.gotoAddFlatFragment();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
		
	};


	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString()
					+ " should implment OnFragmentInteractionListener");
		}
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		updateList();
	}
	
	void updateList() {
		CommonFunctions.startProgressDialog(getActivity(), "Fetching available flats");
		final ParseQueryAdapter<ParseObject> adapter  = new ParseQueryAdapter<ParseObject>(getActivity()
				.getApplicationContext(),
				new ParseQueryAdapter.QueryFactory<ParseObject>() {
					@Override
					public ParseQuery<ParseObject> create() {
						ParseQuery query = new ParseQuery("FlatInfo");
						query.whereEqualTo("communityObject", ParseUser.getCurrentUser().getObjectId());
						return query;
					}
				});
		adapter.setTextKey("flatNumber");
		ListView listView = (ListView) getActivity().findViewById(
				R.id.flatsListView);
		listView.setAdapter(adapter);
		CommonFunctions.stopProgressDialog();
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ListView listview = (ListView)parent;
				
				ParseObject po =(ParseObject) listview.getItemAtPosition(position);
				String str =  po.get("flatNumber").toString();
				mListener.flatSelected(CommonFunctions.trimString(str));			
			}
		});

	}
}
