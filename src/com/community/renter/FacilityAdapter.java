package com.community.renter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.renter.Facility;
import com.example.renter.R;

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
	public View getView(int position, View convertView, ViewGroup parent) {
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

		if (facilities != null) {

			mFacilityName.setText(facilities.get(position).getFacilityName());
			mFacilityTotal.setText("Total:"+facilities.get(position).getFacilityTotal());
			mFacilityAvailable.setText("Available:"+facilities.get(position).getFacilityAvailable());
			return convertView;
		}
		return convertView;
	}

}
