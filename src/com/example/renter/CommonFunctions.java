package com.example.renter;

import com.parse.ParseUser;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class CommonFunctions {
	
	static ProgressDialog progressDialog = null;
	 
	
	public static final String FACILITY_TABLE = "Facilities";
	public static final String FACILITY_TABLE_COMMUNITY_OBJECT = "communityObject";
	public static final String FACILITY_TABLE_FACILITY_NAME = "FacilityName";
	public static final String FACILITY_TABLE_FACILITY_OCCUPIED = "Occupied";
	public static final String FACILITY_TABLE_FACILITY_TOTAL = "Total";
	
	public static final String FLATINFO_TABLE = "FlatInfo";
	public static final String FLATINFO_TABLE_FLAT_NUMBER = "flatNumber";
	public static final String FLATINFO_TABLE_TENANT_NAME = "tenantName";
	public static final String FLATINFO_TABLE_TENANT_MAILID = "tenantMailId";
	public static final String FLATINFO_TABLE_ADDEDBY = "AddedBy";
	public static final String FLATINFO_TABLE_ISOCCUPIED = "isOccupied";
	public static final String FLATINFO_TABLE_COMMUNITY_OBJECT = "communityObject";
	
	public static final String USER_TABLE = "_User";
	public static final String USER_TABLE_COMMUNITYID = "CommunityId";
	public static final String USER_TABLE_ISCOMMUNITY = "isCommunity";
	
	public static final String MESSAGE_TABLE = "Messages";
	public static final String MESSAGE_TABLE_COMMUNITY_ID = "CommunityId";
	public static final String MESSAGE_TABLE_APARTMENT_NO = "ApartmentNo";
	public static final String MESSAGE_TABLE_USERNAME = "Username";
	public static final String MESSAGE_TABLE_MESSAGEID = "MessageId";
	public static final String MESSAGE_TABLE_MESSAGE_CONTENT = "Content";
	public static final String MESSAGE_TABLE_UPDATED_AT = "updatedAt";
	
	public static final String PAYMILL_ID = "139329057943fdc2ff82599619b0631c";
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
		
	}
	public static class FlatInfoTableClass{
		public static String TENANT_MAIL_ID = "tenantMailId";
		public static String TENANT_FLAT_NO = "flatNumber";
		public static String TENANT_IS_OCCUPIED = "isOccupied";
	}
}
