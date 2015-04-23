package com.community.renter;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.renter.R;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class TicketsFragment extends Fragment {

	public TicketsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_community_tickets, container,
				false);
	}
	
	
	
	
	
}
