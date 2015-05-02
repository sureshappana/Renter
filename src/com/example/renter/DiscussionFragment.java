package com.example.renter;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
		getTenantDetails();
		sendImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// if (!flag)
				// getTenantDetails();
				if (message.getText().toString().length() > 0
						&& tenantApartnmentNo != null) {
					ParseObject mDiscussionObject = new ParseObject(
							CommonFunctions.MESSAGE_TABLE);
					mDiscussionObject
							.put(CommonFunctions.MESSAGE_TABLE_COMMUNITY_ID,
									CommonFunctions
											.trimString(ParseUser
													.getCurrentUser()
													.get(CommonFunctions.USER_TABLE_COMMUNITYID)
													.toString()));

					mDiscussionObject.put(
							CommonFunctions.MESSAGE_TABLE_APARTMENT_NO,
							tenantApartnmentNo);
					mDiscussionObject.put(
							CommonFunctions.MESSAGE_TABLE_USERNAME, tenantName);
					mDiscussionObject.put(
							CommonFunctions.MESSAGE_TABLE_MESSAGE_CONTENT,
							message.getText().toString());
					mDiscussionObject.saveInBackground();	
					message.setText("");
					updateListView();
				}

			}

		});

		return view;
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
		ParseQuery<ParseObject> queryRetriveMessage = ParseQuery
				.getQuery(CommonFunctions.MESSAGE_TABLE);
		queryRetriveMessage.whereEqualTo(
				CommonFunctions.MESSAGE_TABLE_COMMUNITY_ID, CommonFunctions
						.trimString(ParseUser.getCurrentUser()
								.get(CommonFunctions.USER_TABLE_COMMUNITYID)
								.toString()));
		queryRetriveMessage.orderByAscending(CommonFunctions.MESSAGE_TABLE_UPDATED_AT);
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
									CommonFunctions
											.trimString(messageObj
													.getUpdatedAt()
													.toString())

							));
							
						}

						MessageAdapter adapter = new MessageAdapter(
								getActivity(), R.layout.chat_list_row,
								messageList);
						messagesListView.setAdapter(adapter);
						
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
