package com.community.renter;

import com.example.renter.R;
import com.example.renter.R.layout;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddFlatFragment extends Fragment {

	public AddFlatFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_community_add_flat,
				container, false);
	}

}
