package com.common.renter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.parse.ParseUser;

public class CommonFunctions {
	
	static ProgressDialog progressDialog = null;
	 
	public static String trimString(String str) {
		return str.replace("]", "").replace("[", "").trim();
	}

	public static void startProgressDialog(Context context, String str) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(str);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	public static void stopProgressDialog() {
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	public static void toastMessage(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	public static class UserTableClass{
		public static String mCurrentUser;
		public static Boolean mCurrentUserIsAdmin;
		
		
		public static void CurrentUserDetails(){
		mCurrentUser = ParseUser.getCurrentUser().getUsername();
		mCurrentUserIsAdmin = (Boolean) ParseUser.getCurrentUser().get(CommonFunctions.UserTableClass.IS_COMMUNITY);
		}
		public static String USERNAME = "username";
		public static String IS_COMMUNITY = "isCommunity";
		public static String OBJECT_ID = "objectId";
		public static String COMMUNITY_NAME = "CommunityName";
		public static String CONTACT_NUMBER = "ContactNumber";
	}
	public static class FlatInfoTableClass{
		public static String TENANT_MAIL_ID = "tenantMailId";
		public static String TENANT_FLAT_NO = "flatNumber";
		public static String TENANT_IS_OCCUPIED = "isOccupied";
		public static String TENANT_NAME = "tenantName";
	}
	
	public static void hideKeyboard(Activity activity) {   
	    View view = activity.getCurrentFocus();
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}
}
