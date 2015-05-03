package com.tenant.renter;

import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.common.renter.CommonFunctions;
import com.common.renter.RenterConstantVariables;
import com.example.renter.R;
import com.paymill.android.api.Transaction;
import com.paymill.android.factory.PMFactory;
import com.paymill.android.factory.PMPaymentMethod;
import com.paymill.android.factory.PMPaymentParams;
import com.paymill.android.listener.PMGenerateTokenListener;
import com.paymill.android.listener.PMInitListener;
import com.paymill.android.listener.PMTransListener;
import com.paymill.android.payment.CardTypeParser;
import com.paymill.android.payment.CardTypeParser.CardType;
import com.paymill.android.payment.CreditCardValidator;
import com.paymill.android.service.PMError;
import com.paymill.android.service.PMManager;
import com.paymill.android.service.PMService;

/**
 * A simple {@link Fragment} subclass.
 * 
 */
public class PaymentFragment extends Fragment {

	public static final int MAX_NEEDED_NUMBERS = 6;
	public static final int milleniumPrefix = 2;
	public static final int centuryPrefix = 0;
	boolean status = false;

	PMPaymentMethod CCard;
	CardType cardType = CardType.YetUnknown;
	String firstNumbers;

	EditText creditCardNumber;
	ImageView ccImage;
	TextView dateLabel;
	EditText dateText;
	String month;
	String year;
	TextView verificationLabel;
	EditText verification;
	TextView nameLabel;
	EditText name;
	EditText amount;
	Button triggerButton;
	EditText currency;

	public PaymentFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.credit_card_fragment, container,
				false);
		PMManager.init(getActivity(), PMService.ServiceMode.TEST,
				RenterConstantVariables.PAYMILL_ID, null, null);

		creditCardNumber = (EditText) v.findViewById(R.id.cardNumberText);
		ccImage = (ImageView) v.findViewById(R.id.cc_imageview);
		ccImage.setVisibility(View.GONE);
		dateLabel = (TextView) v.findViewById(R.id.dateLabel);
		dateText = (EditText) v.findViewById(R.id.dateText);

		verificationLabel = (TextView) v.findViewById(R.id.verificationLabel);
		verification = (EditText) v.findViewById(R.id.verificationText);
		nameLabel = (TextView) v.findViewById(R.id.nameLabel);
		name = (EditText) v.findViewById(R.id.nameText);
		amount = (EditText) v.findViewById(R.id.amount);
		amount.setInputType(3);

		triggerButton = (Button) v.findViewById(R.id.cc_trigger_btn);

		triggerButton.setOnClickListener(sendBtnListener);

		PMManager.addListener(listener);
		PMManager.addListener(initpmlistener);

		currency = (EditText) v.findViewById(R.id.currency);
		currency.setText("USD");
		currency.setKeyListener(null);
		currency.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				LinearLayout layout = new LinearLayout(getActivity());
				ListView listView = new ListView(getActivity());
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1,
						getResources().getStringArray(R.array.Currecy_array));
				listView.setAdapter(adapter);
				layout.addView(listView);
				// alertDialog.setView(layout);
				alertDialog.setTitle("Select Currency");
				alertDialog.setAdapter(adapter, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						currency.setText(adapter.getItem(which).toString());

					}
				});
				alertDialog.show();
			}
		});

		return v;
	}

	PMTransListener listenerTrans = new PMTransListener() {

		@Override
		public void onTransactionFailed(PMError arg0) {
			Log.d("PM", "Transaction failed:" + arg0.toString());
			// Log.d("PM", "Transaction failed:" + arg0.getMessage());
			CommonFunctions.stopProgressDialog();
			CommonFunctions.toastMessage(
					getActivity(),
					"Your payment processing is failed. Error:"
							+ arg0.toString());

		}

		@Override
		public void onTransaction(Transaction arg0) {
			CommonFunctions.stopProgressDialog();
			Log.d("PM",
					"Transaction Successful. Transaction Id:" + arg0.getId());
			CommonFunctions.toastMessage(getActivity(),
					"Your payment is successfully processed. Unique Transaction number is:"
							+ arg0.getId());

		}
	};

	PMGenerateTokenListener listener = new PMGenerateTokenListener() {
		public void onGenerateTokenFailed(PMError error) {
			Log.d("PM", "Error:" + error.toString() + "//"
					+ error.getBridgeError().name());
		}

		public void onGenerateToken(String token) {
			Log.d("PM", "Token:" + token);
		}
	};
	PMInitListener initpmlistener = new PMInitListener() {

		@Override
		public void onInitFailed(PMError arg0) {
			// TODO Auto-generated method stub
			Log.d("PM", "Init Listener Failed:" + arg0.toString());
			status = false;
		}

		@Override
		public void onInit(String arg0) {
			Log.d("PM", "Init Listener Sucess:" + arg0);
			status = true;
		}
	};

	View.OnClickListener sendBtnListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			if (creditCardNumber.getText().toString().length() > MAX_NEEDED_NUMBERS)
				cardType = CardTypeParser.CardType.getCardType(creditCardNumber
						.getText().toString());

			if (CreditCardValidator.validate(getActivity(), name,
					creditCardNumber, dateText, verification, cardType, amount)) {
				month = dateText.getText().toString().substring(0, 2);
				year = dateText.getText().toString().substring(3, 5);
				// CCard = PMFactory.genCardPayment(
				// name.getText().toString(),
				// creditCardNumber.getText().toString()
				// .replaceAll("\\s", ""),
				// month,
				// Integer.toString(milleniumPrefix)
				// + Integer.toString(centuryPrefix) + year,
				// verification.getText().toString());
				// triggerButton.setEnabled(false);
				Log.d("renter", "trigger button clicked");
				processPayment();

			}
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		creditCardNumber.addTextChangedListener(CCTextWatcher);
		dateText.addTextChangedListener(dateTextWatcher);
		verification.addTextChangedListener(validationTextWatcher);
		amount.addTextChangedListener(amountTextWatcher);
	}

	@Override
	public void onPause() {
		super.onPause();
		creditCardNumber.removeTextChangedListener(CCTextWatcher);
		dateText.removeTextChangedListener(dateTextWatcher);
		verification.removeTextChangedListener(validationTextWatcher);
		amount.addTextChangedListener(amountTextWatcher);
	};

	TextWatcher amountTextWatcher = new TextWatcher() {

		String amountString = null;

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// int amountCursor;

			amount.setTextColor(getResources().getColor(
					R.color.defaultTextColor));
			amountString = amount.getText().toString();

			// amount.removeTextChangedListener(this);
			// amountCursor = amount.getSelectionStart();
			// amount.setSelection(amountCursor);
			// amount.addTextChangedListener(this);

			// amountDecimal = Double.parseDouble(amount.getText().toString());
			String[] amountStringArray = amountString.split(Pattern.quote("."));

			if (amountStringArray.length <= 1) {
				amount.setTextColor(getResources().getColor(
						R.color.defaultTextColor));
			} else if (amountStringArray.length == 2) {
				if (amountStringArray[1].length() > 2) {
					amount.setTextColor(getResources().getColor(
							R.color.errorTextColor));
				} else {
					amount.setTextColor(getResources().getColor(
							R.color.defaultTextColor));
				}
			} else {
				amount.setTextColor(getResources().getColor(
						R.color.errorTextColor));
			}
		}

	};

	TextWatcher CCTextWatcher = new TextWatcher() {

		int ccardLength = 0;
		boolean isDelete = false;

		@Override
		public void onTextChanged(CharSequence s, final int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			creditCardNumber.setTextColor(getResources().getColor(
					R.color.defaultTextColor));
			String ccnumber = creditCardNumber.getText().toString();
			ccnumber = showCCardWithIntervals(ccnumber.replaceAll("\\s", ""),
					cardType);
			// we use isDelete in order to know if the cursor is moving forward
			// or backwards
			if (ccardLength < ccnumber.length())
				isDelete = false;
			else
				isDelete = true;
			// We save the last result numbers, to avoid unnecessary checks
			String firstNumbersSaved = "";

			firstNumbersSaved = ccnumber.substring(0,
					Math.min(MAX_NEEDED_NUMBERS + 1, ccnumber.length()))
					.replaceAll("\\s", "");

			// decode card type and set filter to the edit text
			if (firstNumbers == null || !firstNumbers.equals(firstNumbersSaved)) {
				firstNumbers = firstNumbersSaved;
				cardType = CardTypeParser.CardType.getCardType(firstNumbers);
				int cardNumberSize = cardType.getMaxLength()
						+ cardType.getNumberOfIntervals();
				creditCardNumber
						.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
								cardNumberSize) });
			}

			// TODO should we really do this each time someone types? isn't it
			// enough to do this when we detect the card type?
			CreditCardValidator.isValidCardType(getActivity(),
					creditCardNumber, cardType);
			if (cardType != CardType.YetUnknown && cardType != CardType.Invalid) {
				ccImage.setVisibility(View.VISIBLE);
				ccImage.setImageResource(cardType.getImageId());
			} else {
				ccImage.setVisibility(View.GONE);
			}

			int ccMaxLen = cardType.getMaxLength()
					+ cardType.getNumberOfIntervals();
			if (ccnumber.length() > ccMaxLen) {
				ccnumber = ccnumber.substring(0, ccMaxLen);
			}
			// TODO END
			// set text and cursor position:
			// should remove the listener to change the text and set it again at
			// the right position
			creditCardNumber.removeTextChangedListener(this);
			int start = getCCardMarkerPosition(
					creditCardNumber.getSelectionStart(), isDelete, cardType);
			creditCardNumber.setText(ccnumber);
			creditCardNumber.setSelection(start < ccnumber.length() ? start
					: ccnumber.length());
			creditCardNumber.addTextChangedListener(this);

			// if the card is valid move to next field
			if (cardType != CardType.Invalid
					&& ccnumber.length() == cardType.getMaxLength()
							+ cardType.getNumberOfIntervals()) {
				if (CreditCardValidator.validateCreditCardNumber(getActivity(),
						creditCardNumber, cardType)) {
					dateText.requestFocus();
				}
			}

			ccardLength = ccnumber.length();

			if (cardType != CardType.Invalid) {

				InputFilter[] filters = new InputFilter[1];
				filters[0] = new InputFilter.LengthFilter(
						cardType.getCVCLength());
				verification.setFilters(filters);

				// validate CVC input if there is any
				if (verification.length() > 0) {
					CreditCardValidator.validateCheckNumber(getActivity(),
							verification, cardType);
				}
			}
		}

		int getCCardMarkerPosition(int oldPosition, boolean isDelete,
				CardType currentType) {
			int newPosition = oldPosition;

			if (currentType.getMarkersPositions().contains(oldPosition))
				if (isDelete)
					return newPosition--;
				else
					newPosition++;
			return newPosition++;
		}

		String showCCardWithIntervals(String noIntervalsString,
				CardType currentType) {
			String result = "";
			int index = 1;
			for (char c : noIntervalsString.toCharArray()) {
				result += c;
				if (currentType.getSpacesPositions().contains(index))
					result += " ";
				index++;
			}
			return result;
		}
	};

	TextWatcher dateTextWatcher = new TextWatcher() {

		int dateCursor;
		String formatedDate;
		boolean isDateDelete = false;
		int dateLength;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {

			dateText.setTextColor(getResources().getColor(
					R.color.defaultTextColor));
			formatedDate = dateText.getText().toString();
			isDateDelete = (dateLength > formatedDate.length());

			dateText.removeTextChangedListener(this);
			dateCursor = dateText.getSelectionStart();
			dateFormated(isDateDelete);
			dateText.setText(formatedDate);
			dateText.setSelection(dateCursor);
			dateText.addTextChangedListener(this);

			if (formatedDate.length() > 4) {
				if (CreditCardValidator.validateDateFormat(formatedDate)
						&& CreditCardValidator.validateDate(getActivity(),
								dateText)) {
					verification.requestFocus();

				} else {
					dateText.setTextColor(getResources().getColor(
							R.color.errorTextColor));
				}
			}
			dateLength = formatedDate.length();
		}

		void dateFormated(boolean isDelete) {

			formatedDate = formatedDate.replaceAll("/", "");
			if (formatedDate.length() == 3 && !isDelete && dateCursor == 3)
				dateCursor++;
			if (formatedDate.length() == 2 && isDelete && dateCursor > 1)
				dateCursor--;
			if (formatedDate.length() > 2) {
				formatedDate = formatedDate.substring(0, 2) + "/"
						+ formatedDate.substring(2);
			}
		}
	};

	TextWatcher validationTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

			verification.setTextColor(getResources().getColor(
					R.color.defaultTextColor));

			if (cardType == CardType.YetUnknown) {
				return;
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {

		}

	};

	public void processPayment() {

		if (status == false) {
			PMManager.init(getActivity(), PMService.ServiceMode.TEST,
					RenterConstantVariables.PAYMILL_ID, null, null);
		} else {
				Log.d("renter", "payment processing functions started");
				CommonFunctions.toastMessage(getActivity(),
						"Processing your payment. Please wait...");

				PMPaymentMethod method = PMFactory.genCardPayment(
						name.getText().toString(),
						creditCardNumber.getText().toString()
								.replaceAll("\\s", "").toString(),
						month,
						Integer.toString(milleniumPrefix)
								+ Integer.toString(centuryPrefix) + year,
						verification.getText().toString());
				PMPaymentParams params = PMFactory.genPaymentParams(currency
						.getText().toString(), (int) (Double.parseDouble(amount
						.getText().toString()) * 100), null);
				PMManager.addListener(listenerTrans);
				PMManager.transaction(getActivity(), method, params, false);
				Log.d("renter", "payment processing functions completed");

		}
	}

}
