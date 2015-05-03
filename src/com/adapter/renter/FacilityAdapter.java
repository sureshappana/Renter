package com.adapter.renter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.classes.renter.Facility;
import com.common.renter.CommonFunctions;
import com.common.renter.FacilitiesFragment;
import com.common.renter.RenterConstantVariables;
import com.example.renter.R;
import com.parse.ParseUser;

public class FacilityAdapter extends ArrayAdapter<Facility> {

	Context context = null;
	List<Facility> facilities = null;

	public FacilityAdapter(Context context, int resource,
			List<Facility> facilities) {
		super(context, resource, facilities);
		this.context = context;
		this.facilities = facilities;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		setNotifyOnChange(true);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.facility_list_row, parent,
					false);
		}
		TextView mFacilityName = (TextView) convertView
				.findViewById(R.id.facilityNameTextView);
		TextView mFacilityTotal = (TextView) convertView
				.findViewById(R.id.facilityTotalTextView);
		TextView mFacilityAvailable = (TextView) convertView
				.findViewById(R.id.facilityAvailableTextView);

		
		ImageView imageViewDelete = (ImageView)convertView.findViewById(R.id.deleteFacility);
		
		if (!ParseUser.getCurrentUser().getBoolean(RenterConstantVariables.USER_TABLE_ISCOMMUNITY)) {
			imageViewDelete.setVisibility(View.INVISIBLE);
		}
		imageViewDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FacilitiesFragment.removeListViewItem(position);
			}
		});
		if (facilities != null) {

			mFacilityName.setText(facilities.get(position).getFacilityName());
			mFacilityTotal.setText("Total:"+facilities.get(position).getFacilityTotal());
			mFacilityAvailable.setText("Available:"+facilities.get(position).getFacilityAvailable());
			return convertView;
		}
		return convertView;
	}

}
