package com.community.renter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.renter.R;

public class FlatsAdapter extends ArrayAdapter<Flat> {

	Context context = null;
	List<Flat> flats = null;

	public FlatsAdapter(Context context, int resource, List<Flat> flats) {
		super(context, resource, flats);
		this.context = context;
		this.flats = flats;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		setNotifyOnChange(true);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.flats_list_row, parent,
					false);
		}

		TextView mFlatNumber = (TextView) convertView
				.findViewById(R.id.flatNumber);
		TextView mTenantName = (TextView) convertView
				.findViewById(R.id.tenantName);
		TextView mTenantMailId = (TextView) convertView
				.findViewById(R.id.tenantMailId);
		TextView mFlatOccupiedStatus = (TextView) convertView
				.findViewById(R.id.flatOccupiedStatus);

		ImageView imageViewDelete = (ImageView) convertView
				.findViewById(R.id.deleteFlat);
		imageViewDelete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
				FlatInfoFragment.removeListViewItem(position);
			}
		});

		if (flats != null) {

			mFlatNumber.setText(flats.get(position).getFlatNumber());
			if (flats.get(position).getFlatOccupiedStatus().equals("true")) {
				mTenantName.setText(flats.get(position).getTenantName());
				mTenantMailId.setText(flats.get(position).getTenantMailId());
				mFlatOccupiedStatus.setText("Occupied");
			} else
				mFlatOccupiedStatus.setText("Vacant");
			return convertView;
		}
		return convertView;
	}

}
