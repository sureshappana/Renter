package com.example.renter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class TenantPaymentFragment extends Fragment {

	public TenantPaymentFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tenant_payment,
				container, false);
	//	PMManager.addListener(listener);
		return view;
	}

}
