package com.liyong.paypal;

import java.math.BigDecimal;
import java.util.Hashtable;

import org.cocos2dx.hellocpp.HelloCpp;
import org.cocos2dx.plugin.InterfaceIAP.IAPAdapter;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONException;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class PaypalJava implements IAPAdapter {
	private static Handler mHandler;
	private static Activity mContext;
	private String CLIENT_ID = null;
	private String RECEIVER_EMAIL = null;
	private boolean debug=true;
	private static Intent intent = null;
	private static PaypalJava mAdapter = null;
	//HelloLua Activity
	public PaypalJava(Context context) {
		mContext = (Activity) context;
		HelloCpp hl = (HelloCpp)mContext;
		hl.paypalJava = this;
		mAdapter = this;
	}

	@Override
	public void configDeveloperInfo(Hashtable<String, String> cpInfo) {
		try{
			System.out.println("config DeveloperInfo "+cpInfo.toString());
			CLIENT_ID = cpInfo.get("CLIENT_ID");
			RECEIVER_EMAIL = cpInfo.get("RECEIVER_EMAIL");
			if(intent == null) {
				intent = new Intent(mContext, PayPalService.class);
				if(debug) {
					//intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
		
				} else {
					//intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
					intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_LIVE);
				}
				intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CLIENT_ID);
				mContext.startService(intent);
			} else {
				Log.e("Paypal", "�ظ����ã�");
			}
		}catch(Exception e) {
			Log.e("Paypal", "Paypal configure error");
			e.printStackTrace();
		}
	}

	@Override
	public String getSDKVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void payForProduct(Hashtable<String, String> info) {
		Log.d("Paypal", "payForproduct "+info.toString());
		BigDecimal v = new BigDecimal(info.get("number"));
		String currency = info.get("currency");
		String payerID = info.get("PAYER_ID");
		String productName = info.get("productName");
		PayPalPayment payment = new PayPalPayment(v, currency, productName);
		
		Intent intent = new Intent(mContext, PaymentActivity.class);
		if(debug) {
			//intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
			intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
		} else {
			//intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);
			intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_LIVE);
		}
		
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CLIENT_ID);
		//user id
		intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, payerID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, RECEIVER_EMAIL);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
		
		mContext.startActivityForResult(intent, 0);
	}
	public void onPayResult(int requestCode, int resultCode, Intent data){
		Log.d("payment Example", " "+Activity.RESULT_OK+" "+Activity.RESULT_CANCELED);
		if(resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if(confirm != null) {
				try{
					Log.i("payment Example", confirm.toJSONObject().toString(4));
					InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_SUCCESS, "����ɹ���������������֤һ�£�");
				}catch(JSONException e){
					Log.e("payment Example", "failed", e);
					InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "����ʧ�ܣ�");
				}
			} else {
				Log.d("payment Example", "success");
				InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_SUCCESS, "�����繺��ɹ���");
			}
		} else if(resultCode == Activity.RESULT_CANCELED) {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_CANCEL, "С����ȡ���˹���!");
			Log.i("Example", "user canceled");
		} else if(resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "����ʧ�ܣ�");
			Log.i("Example", "Invalid payment");
		} else {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "��֪������ʲô������ "+resultCode);
		}
	}
	@Override
	public void setDebugMode(boolean arg0) {
		System.out.println("debug set "+arg0);
		debug = arg0;
	}
	
}
