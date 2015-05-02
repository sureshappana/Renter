package com.example.renter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class DiscussionFragment extends Fragment {

	ImageView sendImage = null;
	ListView messagesListView = null;
	EditText message = null;
	ArrayList<Message> messageList = new ArrayList<Message>();

	static String tenantName = null;
	static String tenantApartnmentNo = null;
	static boolean flag = false;

	public DiscussionFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_discussion, container,
				false);

		messagesListView = (ListView) view.findViewById(R.id.messagesListView);

		message = (EditText) view.findViewById(R.id.messageEditText);
		sendImage = (ImageView) view.findViewById(R.id.sendImage);
		message.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					sendMessage();

					return true;
				}
				return false;
			}
		});
		message.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event == null) {
					if (actionId == EditorInfo.IME_ACTION_DONE
							|| actionId == EditorInfo.IME_ACTION_NEXT
							|| (actionId == EditorInfo.IME_NULL && event
									.getAction() == KeyEvent.ACTION_DOWN))
						;
					sendMessage();
				}
				return false;
			}
		});
		getTenantDetails();
		sendImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				sendMessage();
			}

		});

		return view;
	}

	private void sendMessage() {

		// if (!flag)
		// getTenantDetails();
		if (message.getText().toString().length() > 0
				&& tenantApartnmentNo != null) {
			ParseObject mDiscussionObject = new ParseObject(
					CommonFunctions.MESSAGE_TABLE);
			mDiscussionObject.put(
					CommonFunctions.MESSAGE_TABLE_COMMUNITY_ID,
					CommonFunctions.trimString(ParseUser.getCurrentUser()
							.get(CommonFunctions.USER_TABLE_COMMUNITYID)
							.toString()));

			mDiscussionObject.put(CommonFunctions.MESSAGE_TABLE_APARTMENT_NO,
					tenantApartnmentNo);
			mDiscussionObject.put(CommonFunctions.MESSAGE_TABLE_USERNAME,
					tenantName);
			mDiscussionObject.put(
					CommonFunctions.MESSAGE_TABLE_MESSAGE_CONTENT, message
							.getText().toString());
			mDiscussionObject.saveInBackground();

			String mCommunityMessagesPushMessageChannel = null;
			if (ParseUser.getCurrentUser()
					.get(CommonFunctions.USER_TABLE_ISCOMMUNITY).toString()
					.equals("true")) {
				mCommunityMessagesPushMessageChannel = "Messages_"
						+ CommonFunctions.trimString(ParseUser.getCurrentUser()
								.getObjectId());
			} else {

				mCommunityMessagesPushMessageChannel = "Messages_"
						+ CommonFunctions.trimString(ParseUser.getCurrentUser()
								.get(CommonFunctions.USER_TABLE_COMMUNITYID)
								.toString());
			}
			updateListView();
			String currentInstallationId = ParseInstallation
					.getCurrentInstallation().getInstallationId();
			ParseQuery pushQuery = ParseInstallation.getQuery();
			pushQuery.whereNotEqualTo("user", ParseUser.getCurrentUser());
			pushQuery.whereEqualTo("channels",
					mCommunityMessagesPushMessageChannel);
			JSONObject data = null;
			try {
				data = new JSONObject("{\"alert\": \""+ message.getText()+"\",\"title\": \""+ tenantName +"\"}");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			ParsePush push = new ParsePush();
			// push.setChannel(mCommunityMessagesPushMessageChannel);
			push.setQuery(pushQuery);
			//push.setMessage(message.getText().toString());
			push.setData(data);
			push.sendInBackground(new SendCallback() {

				@Override
				public void done(ParseException e) {
					if (e == null) {
						Log.d("push", "success");
					} else {
						Log.d("push", "failed:" + e.toString());
					}

				}
			});

			message.setText("");

		}

	}

	private void getTenantDetails() {
		ParseQuery<ParseObject> query = ParseQuery
				.getQuery(CommonFunctions.FLATINFO_TABLE);
		query.whereEqualTo(CommonFunctions.FLATINFO_TABLE_TENANT_MAILID,
				CommonFunctions.trimString(ParseUser.getCurrentUser()
						.getEmail()));
		query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> tenantList, ParseException e) {
				if (e == null) {
					if (tenantList.size() > 0) {
						ParseObject tenant = tenantList.get(0);
						tenantName = CommonFunctions.trimString(tenant.get(
								CommonFunctions.FLATINFO_TABLE_TENANT_NAME)
								.toString());
						tenantApartnmentNo = CommonFunctions
								.trimString(tenant
										.get(CommonFunctions.FLATINFO_TABLE_FLAT_NUMBER)
										.toString());
					}
				} else {
					Log.d("score", "Error: " + e.getMessage());
				}
			}
		});
		flag = true;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		updateListView();
		super.onViewCreated(view, savedInstanceState);
	}

	
	private void updateListView() {
		messageList.clear();
		CommonFunctions.hideKeyboard(getActivity());
		ParseQuery<ParseObject> queryRetriveMessage = ParseQuery
				.getQuery(CommonFunctions.MESSAGE_TABLE);
		if (ParseUser.getCurrentUser().getBoolean(
				CommonFunctions.USER_TABLE_ISCOMMUNITY)) {
			queryRetriveMessage.whereEqualTo(
					CommonFunctions.MESSAGE_TABLE_COMMUNITY_ID, CommonFunctions
							.trimString(ParseUser.getCurrentUser().getObjectId()
									.toString()));
		
		} else {
			queryRetriveMessage.whereEqualTo(
					CommonFunctions.MESSAGE_TABLE_COMMUNITY_ID, CommonFunctions
							.trimString(ParseUser.getCurrentUser()
									.get(CommonFunctions.USER_TABLE_COMMUNITYID)
									.toString()));
		
		}
		queryRetriveMessage
				.orderByAscending(CommonFunctions.MESSAGE_TABLE_UPDATED_AT);
		queryRetriveMessage.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> arg0, ParseException arg1) {
				if (arg1 == null) {
					if (arg0 != null) {
						// String message, String name, String apt_no, String
						// time) {
						for (ParseObject messageObj : arg0) {
							messageList.add(new Message(
									CommonFunctions
											.trimString(messageObj
													.get(CommonFunctions.MESSAGE_TABLE_MESSAGE_CONTENT)
													.toString()),
									CommonFunctions
											.trimString(messageObj
													.get(CommonFunctions.MESSAGE_TABLE_USERNAME)
													.toString()),
									CommonFunctions
											.trimString(messageObj
													.get(CommonFunctions.MESSAGE_TABLE_APARTMENT_NO)
													.toString()),
									CommonFunctions.trimString(messageObj
											.getUpdatedAt().toString())

							));

						}

						MessageAdapter adapter = new MessageAdapter(
								getActivity(), R.layout.chat_list_row,
								messageList);
						messagesListView.setAdapter(adapter);
						// messagesListView.smoothScrollToPosition(adapter.getCount()
						// - 1);
						messagesListView.setSelection(adapter.getCount() - 1);
					}
				} else {
					Log.d("renter", "Error in retreiving the messages");
					CommonFunctions.toastMessage(getActivity(),
							"Error in retrieving the message");
				}

			}
		});

	}
}
