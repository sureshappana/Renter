package com.example.renter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
	//Parse.enableLocalDatastore(this);

	Parse.initialize(this, "05TSwhW3qWrySLyd1NWlvrW7on7CEM6MgF7jDw3y",
			"QueMQBK6Bkb5abIzWab76lMn6969xRiDS93LBBXP");

	
	ParseInstallation.getCurrentInstallation().saveInBackground();
  }
}
