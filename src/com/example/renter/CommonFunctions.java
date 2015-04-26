package com.example.renter;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

public class CommonFunctions {

	static ProgressDialog progressDialog = null;
	public static String FLATINFO_OBJECT = "FlatInfo"; 
	public static String USER_OBJECT = "_User";
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
}
