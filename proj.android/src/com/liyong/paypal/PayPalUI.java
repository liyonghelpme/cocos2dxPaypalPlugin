package com.liyong.paypal;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

public class PayPalUI extends Activity implements  OnClickListener {
	ScrollView scroller;
	TextView labelSimplePayment;
	TextView labelParallelPayment;
	TextView labelChainedPayment;
	TextView labelPreapproval;
	TextView labelKey;
	TextView appVersion;
	EditText enterPreapprovalKey;
	Button exitApp;
	TextView title;
	TextView info;
	TextView extra;
	LinearLayout layoutSimplePayment;
	LinearLayout layoutSplitPayment;
	LinearLayout layoutChainedPayment;
	LinearLayout layoutPreapproval;
	
	// You will need at least one CheckoutButton, this application has four for examples
	CheckoutButton launchSimplePayment;
	CheckoutButton launchParallelPayment;
	CheckoutButton launchChainedPayment;
	CheckoutButton launchPreapproval;

	public static String resultTitle;
	public static String resultInfo;
	public static String resultExtra;

	protected static final int INITIALIZE_SUCCESS = 0;
	protected static final int INITIALIZE_FAILURE = 1;
	
	public static final String build = "10.12.09.8053";

	private static final int server = PayPal.ENV_SANDBOX;
	// The ID of your application that you received from PayPal
	private static final String appID = "APP-80W284485P519543T";
	private static final int request = 1;
	
	private void setupUI() {
		scroller = new ScrollView(this);
		scroller.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		scroller.setBackgroundColor(Color.BLACK);
		
		LinearLayout content = new LinearLayout(this);
		content.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		content.setGravity(Gravity.CENTER_HORIZONTAL);
		content.setOrientation(LinearLayout.VERTICAL);
		content.setPadding(10, 10, 10, 10);
		content.setBackgroundColor(Color.TRANSPARENT);
		
		layoutSimplePayment = new LinearLayout(this);
		layoutSimplePayment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutSimplePayment.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);
		layoutSimplePayment.setPadding(0, 5, 0, 5);
			
		labelSimplePayment = new TextView(this);
		labelSimplePayment.setGravity(Gravity.CENTER_HORIZONTAL);
		labelSimplePayment.setText("Simple Payment:");
		layoutSimplePayment.addView(labelSimplePayment);
		labelSimplePayment.setVisibility(View.GONE);
			
		content.addView(layoutSimplePayment);
			
		layoutSplitPayment = new LinearLayout(this);
		layoutSplitPayment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutSplitPayment.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutSplitPayment.setOrientation(LinearLayout.VERTICAL);
		layoutSplitPayment.setPadding(0, 5, 0, 5);
			
		labelParallelPayment = new TextView(this);
		labelParallelPayment.setGravity(Gravity.CENTER_HORIZONTAL);
		labelParallelPayment.setText("Parallel Payment:");
		layoutSplitPayment.addView(labelParallelPayment);
		labelParallelPayment.setVisibility(View.GONE);
			
		content.addView(layoutSplitPayment);
			
		layoutChainedPayment = new LinearLayout(this);
		layoutChainedPayment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutChainedPayment.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutChainedPayment.setOrientation(LinearLayout.VERTICAL);
		layoutChainedPayment.setPadding(0, 5, 0, 5);
			
		labelChainedPayment = new TextView(this);
		labelChainedPayment.setGravity(Gravity.CENTER_HORIZONTAL);
		labelChainedPayment.setText("Chained Payment:");
		layoutChainedPayment.addView(labelChainedPayment);
		labelChainedPayment.setVisibility(View.GONE);
			
		content.addView(layoutChainedPayment);
			
		layoutPreapproval = new LinearLayout(this);
		layoutPreapproval.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutPreapproval.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutPreapproval.setOrientation(LinearLayout.VERTICAL);
		layoutPreapproval.setPadding(0, 5, 0, 1);
			
		labelPreapproval = new TextView(this);
		labelPreapproval.setGravity(Gravity.CENTER_HORIZONTAL);
		labelPreapproval.setText("Preapproval:");
		layoutPreapproval.addView(labelPreapproval);
		labelPreapproval.setVisibility(View.GONE);
			
		content.addView(layoutPreapproval);
			
		LinearLayout layoutKey = new LinearLayout(this);
		layoutKey.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutKey.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutKey.setOrientation(LinearLayout.VERTICAL);
		layoutKey.setPadding(0, 1, 0, 5);
			
		enterPreapprovalKey = new EditText(this);
		enterPreapprovalKey.setLayoutParams(new LayoutParams(200, 45));
		enterPreapprovalKey.setGravity(Gravity.CENTER);
		enterPreapprovalKey.setSingleLine(true);
		enterPreapprovalKey.setHint("Enter PA Key");
		layoutKey.addView(enterPreapprovalKey);
		enterPreapprovalKey.setVisibility(View.GONE);
		labelKey = new TextView(this);
		labelKey.setGravity(Gravity.CENTER_HORIZONTAL);
		labelKey.setPadding(0, -5, 0, 0);
		labelKey.setText("(Required for Preapproval)");
		layoutKey.addView(labelKey);
		labelKey.setVisibility(View.GONE);
		content.addView(layoutKey);
			
		title = new TextView(this);
		title.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		title.setPadding(0, 5, 0, 5);
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setTextSize(30.0f);
		title.setVisibility(TextView.GONE);
		content.addView(title);
			
		info = new TextView(this);
		info.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		info.setPadding(0, 5, 0, 5);
		info.setGravity(Gravity.CENTER_HORIZONTAL);
		info.setTextSize(20.0f);
		info.setVisibility(TextView.VISIBLE);
		info.setText("Initializing Library...");
		content.addView(info);
			
		extra = new TextView(this);
		extra.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		extra.setPadding(0, 5, 0, 5);
		extra.setGravity(Gravity.CENTER_HORIZONTAL);
		extra.setTextSize(12.0f);
		extra.setVisibility(TextView.GONE);
		content.addView(extra);
		
		LinearLayout layoutExit = new LinearLayout(this);
		layoutExit.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutExit.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutExit.setOrientation(LinearLayout.VERTICAL);
		layoutExit.setPadding(0, 15, 0, 5);
		
		exitApp = new Button(this);
		exitApp.setLayoutParams(new LayoutParams(200, LayoutParams.WRAP_CONTENT)); //Semi mimic PP button sizes
		exitApp.setOnClickListener(this);
		exitApp.setText("Exit");
		layoutExit.addView(exitApp);
		content.addView(layoutExit);
		
		appVersion = new TextView(this);
		appVersion.setGravity(Gravity.CENTER_HORIZONTAL);
		appVersion.setPadding(0, -5, 0, 0);
		appVersion.setText("\n\nSimple Demo Build " + build + "\nMPL Library Build " + PayPal.getBuild());
		content.addView(appVersion);
		appVersion.setVisibility(View.GONE);
		
		scroller.addView(content);
		setContentView(scroller);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == launchSimplePayment) {
			PayPalPayment payment = exampleSimplePayment();
			Intent checkoutIntent = PayPal.getInstance().checkout(payment, this, new ResultDelegate());
			startActivityForResult(checkoutIntent, request);
			
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode != request) {
			return;
		}
		launchSimplePayment.updateButton();
    	title.setText(resultTitle);
    	title.setVisibility(View.VISIBLE);
    	info.setText(resultInfo);
    	info.setVisibility(View.VISIBLE);
    	extra.setText(resultExtra);
    	extra.setVisibility(View.VISIBLE);

	}
	Handler hRefresh = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
		    	case INITIALIZE_SUCCESS:
		    		setupButtons();
		            break;
		    	case INITIALIZE_FAILURE:
		    		showFailure();
		    		break;
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread libraryInitializationThread = new Thread() {
			public void run() {
				initLibrary();
				
				if (PayPal.getInstance().isLibraryInitialized()) {
					hRefresh.sendEmptyMessage(INITIALIZE_SUCCESS);
				}
				else {
					hRefresh.sendEmptyMessage(INITIALIZE_FAILURE);
				}

			}
		};
		libraryInitializationThread.start();
		setupUI();
	}
	private void initLibrary() {
		PayPal pp = PayPal.getInstance();
		if(pp == null) {
			pp = PayPal.initWithAppID(this, appID, server );
			pp.setLanguage("zh_CN");
			pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER);
			pp.setShippingEnabled(true);
			pp.setDynamicAmountCalculationEnabled(false);
		}
	}
	public void setupButtons() {
		PayPal pp = PayPal.getInstance();
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchSimplePayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutSimplePayment.addView(launchSimplePayment);
		
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchParallelPayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchParallelPayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutSplitPayment.addView(launchParallelPayment);
		
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchChainedPayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchChainedPayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutChainedPayment.addView(launchChainedPayment);
		
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchPreapproval = pp.getCheckoutButton(this, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchPreapproval.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutPreapproval.addView(launchPreapproval);
		
		// Show our labels and the preapproval EditText.
		labelSimplePayment.setVisibility(View.VISIBLE);
		labelParallelPayment.setVisibility(View.VISIBLE);
		labelChainedPayment.setVisibility(View.VISIBLE);
		labelPreapproval.setVisibility(View.VISIBLE);
		enterPreapprovalKey.setVisibility(View.VISIBLE);
		labelKey.setVisibility(View.VISIBLE);
		appVersion.setVisibility(View.VISIBLE);
		
		info.setText("");
		info.setVisibility(View.GONE);
	}
	public void showFailure() {
		title.setText("FAILURE");
		info.setText("Could not initialize the PayPal library.");
		title.setVisibility(View.VISIBLE);
		info.setVisibility(View.VISIBLE);
	}
	
	private PayPalPayment exampleSimplePayment() {
		// Create a basic PayPalPayment.
		PayPalPayment payment = new PayPalPayment();
		// Sets the currency type for this payment.
    	payment.setCurrencyType("USD");
    	// Sets the recipient for the payment. This can also be a phone number.
    	payment.setRecipient("example-merchant-1@paypal.com");
    	// Sets the amount of the payment, not including tax and shipping amounts.
    	payment.setSubtotal(new BigDecimal("8.25"));
    	// Sets the payment type. This can be PAYMENT_TYPE_GOODS, PAYMENT_TYPE_SERVICE, PAYMENT_TYPE_PERSONAL, or PAYMENT_TYPE_NONE.
    	payment.setPaymentType(PayPal.PAYMENT_TYPE_GOODS);
    	
    	// PayPalInvoiceData can contain tax and shipping amounts. It also contains an ArrayList of PayPalInvoiceItem which can
    	// be filled out. These are not required for any transaction.
    	PayPalInvoiceData invoice = new PayPalInvoiceData();
    	// Sets the tax amount.
    	invoice.setTax(new BigDecimal("1.25"));
    	// Sets the shipping amount.
    	invoice.setShipping(new BigDecimal("4.50"));
    	
    	// PayPalInvoiceItem has several parameters available to it. None of these parameters is required.
    	PayPalInvoiceItem item1 = new PayPalInvoiceItem();
    	// Sets the name of the item.
    	item1.setName("Pink Stuffed Bunny");
    	// Sets the ID. This is any ID that you would like to have associated with the item.
    	item1.setID("87239");
    	// Sets the total price which should be (quantity * unit price). The total prices of all PayPalInvoiceItem should add up
    	// to less than or equal the subtotal of the payment.
    	item1.setTotalPrice(new BigDecimal("6.00"));
    	// Sets the unit price.
    	item1.setUnitPrice(new BigDecimal("2.00"));
    	// Sets the quantity.
    	item1.setQuantity(3);
    	// Add the PayPalInvoiceItem to the PayPalInvoiceData. Alternatively, you can create an ArrayList<PayPalInvoiceItem>
    	// and pass it to the PayPalInvoiceData function setInvoiceItems().
    	invoice.getInvoiceItems().add(item1);
    	
    	// Create and add another PayPalInvoiceItem to add to the PayPalInvoiceData.
    	PayPalInvoiceItem item2 = new PayPalInvoiceItem();
    	item2.setName("Well Wishes");
    	item2.setID("56691");
    	item2.setTotalPrice(new BigDecimal("2.25"));
    	item2.setUnitPrice(new BigDecimal("0.25"));
    	item2.setQuantity(9);
    	invoice.getInvoiceItems().add(item2);
    	
    	// Sets the PayPalPayment invoice data.
    	payment.setInvoiceData(invoice);
    	// Sets the merchant name. This is the name of your Application or Company.
    	payment.setMerchantName("The Gift Store");
    	// Sets the description of the payment.
    	payment.setDescription("Quite a simple payment");
    	// Sets the Custom ID. This is any ID that you would like to have associated with the payment.
    	payment.setCustomID("8873482296");
    	// Sets the Instant Payment Notification url. This url will be hit by the PayPal server upon completion of the payment.
    	payment.setIpnUrl("http://www.exampleapp.com/ipn");
    	// Sets the memo. This memo will be part of the notification sent by PayPal to the necessary parties.
    	payment.setMemo("Hi! I'm making a memo for a simple payment.");
    	
    	return payment;
	}

}
