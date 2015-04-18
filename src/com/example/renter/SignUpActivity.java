package com.example.renter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class SignUpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		getFragmentManager().beginTransaction()
				.replace(R.id.container, new TenantSignUpActivity(), "Tenant")
				.commit();
		
		Switch toggleSwitch = (Switch) findViewById(R.id.signUPSwitch);
		toggleSwitch
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.container,
											new TenantSignUpActivity(),
											
											"Tenant").commit();
						} else {
							getFragmentManager()
									.beginTransaction()
									.replace(R.id.container,
											new CommunitySignUpActivity(),
											"Community").commit();
						}
					}
				});

		// // try {
		//
		//
		// TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		//
		// Log.d("Renter", tabHost.toString());
		//
		// TabSpec tab1 = tabHost.newTabSpec("Tenant");
		// TabSpec tab2 = tabHost.newTabSpec("Community");
		//
		// tab1.setIndicator("Tab1");
		// tab1.setContent(new Intent(this, TenantSignUpActivity.class));
		//
		// tab2.setIndicator("Tab2");
		// tab2.setContent(new Intent(this, CommunitySignUpActivity.class));
		//
		// tabHost.addTab(tab1);
		// tabHost.addTab(tab2);
		//
		// // } catch(Exception ex){
		// // Log.d("Error", "SignUpActivity:onCreate:"+ex.getMessage());
		// // }
		//
		//
	}
}
