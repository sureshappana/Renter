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
public class FlatInfoFragment extends Fragment {

	public FlatInfoFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_community_flat_info,
				container, false);
	}
	public interface OnFragmentInteractionListener{
		public void gotoAddFlatFragment();
	}
	OnFragmentInteractionListener mListener;

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
}
