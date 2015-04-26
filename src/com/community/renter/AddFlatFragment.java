package com.community.renter;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.community.renter.DisplayFlatFragment.OnFragmentInteractionListener;
import com.example.renter.CommonFunctions;
import com.example.renter.R;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class AddFlatFragment extends Fragment {
	OnFragmentInteractionListener mListener;
	public AddFlatFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_community_add_flat,
				container, false);
		
		view.findViewById(R.id.addFlatButton).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String mflatNumber = ((EditText)view.findViewById(R.id.flatNumberEditText)).getText().toString();
				String mtenantName = ((EditText)view.findViewById(R.id.tenantNameEditText)).getText().toString();
				String mtenantMailId = ((EditText)view.findViewById(R.id.tenantMailIdEditText)).getText().toString();
				
				if(mflatNumber.isEmpty()){
					CommonFunctions.toastMessage(getActivity(), "Flat Number can't be empty");
				} else {
					ParseObject flatInfo = new ParseObject(CommonFunctions.FLATINFO_OBJECT);
					flatInfo.add("communityObject", ParseUser.getCurrentUser().getObjectId());
					flatInfo.add("flatNumber", mflatNumber);
					flatInfo.add("tenantName", mtenantName);
					flatInfo.add("tenantMailId", mtenantMailId);
					flatInfo.add("isOccupied", true);
					flatInfo.add("isMainTenant", true);
					flatInfo.saveInBackground();
					CommonFunctions.toastMessage(getActivity(), "Flat Added");
					mListener.gotoFlatInfoFragment();
				}
			}
		});
		
		
		
		return view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException ex) {
			throw new ClassCastException(activity.toString()
					+ " should implment OnFragmentInteractionListener");
		}
	}
	
	public interface OnFragmentInteractionListener{
		public void gotoFlatInfoFragment();
	}

}
