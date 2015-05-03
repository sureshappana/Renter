package com.common.renter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
	//Parse.enableLocalDatastore(this);

	Parse.initialize(this, RenterConstantVariables.PARSE_APPLICATION_ID,
			RenterConstantVariables.PARSE_CLIENT_KEY);

	
	ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}
