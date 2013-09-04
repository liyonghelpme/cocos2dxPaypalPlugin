package com.liyong.paypal;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.cocos2dx.hellocpp.HelloCpp;
import org.cocos2dx.plugin.InterfaceIAP.IAPAdapter;
import org.cocos2dx.plugin.InterfaceIAP;
import org.cocos2dx.plugin.PluginWrapper;
import org.json.JSONException;

import com.amazon.inapp.purchasing.PurchasingManager;
import com.liyong.iap.IapActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class PaypalJava implements IAPAdapter {
	private static Handler mHandler;
	private static Activity mContext;
	private String CLIENT_ID = null;
	private String RECEIVER_EMAIL = null;
	private boolean debug=true;
	private static Intent intent = null;
	private static PaypalJava mAdapter = null;
	public static int Result = 0;
	//HelloLua Activity
	public PaypalJava(Context context) {
		mContext = (Activity) context;
		HelloCpp hl = (HelloCpp)mContext;
		hl.paypalJava = this;
		mAdapter = this;
		Log.e("PayPal", "init PayPal Java finish");
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
				ComponentName cn = mContext.startService(intent);
				Log.e("Paypal", "ComponentName "+cn);
			} else {
				Log.e("Paypal", "重复配置！");
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
		String num = info.get("number");
		//下层传来了这个invoice账号
		String iv = info.get("invoice");
		int invoice = 0;
		if(iv != null) {
			invoice = Integer.parseInt(iv);
		}
		
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
		
		//mContext.startActivityForResult(intent, 0);
		
		//Intent uiIntent = new Intent(mContext, PayPalUI.class);
		//mContext.startActivityForResult(uiIntent, 0);
		
		//产品名称订单编号
		/*
		Intent wi = new Intent(mContext, MyWeb.class);
		wi.putExtra("invoice", invoice);
		wi.putExtra("item_name", productName);
		wi.putExtra("amount", num);
		wi.putExtra("currency_code", currency);
		
		mContext.startActivityForResult(wi, 0);
		
		*/
		
		/*
		Intent iap = new Intent(mContext, IapActivity.class);
		iap.putExtra("productName", productName);
		mContext.startActivityForResult(iap, 0);
		*/
		
		//获取 可购买的物品
		
		Set<String> s = new HashSet<String>();
		s.add("com.liyong.crystal0");
		PurchasingManager.initiateItemDataRequest(s);
		
		String requestId = PurchasingManager.initiatePurchaseRequest("com.liyong.crystal0");
		Log.e("IAP", "requestID "+requestId);
	}
	public void onPayResult(int requestCode, int resultCode, Intent data){
		Log.d("payment Example", "onPayResult "+Activity.RESULT_OK+" "+Activity.RESULT_CANCELED);
		Log.d("payment Example", "requestCode resultCode "+requestCode+" "+resultCode+" "+data);
		if(resultCode == Activity.RESULT_OK) {
			PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
			if(confirm != null) {
				try{
					Log.i("payment Example", confirm.toJSONObject().toString(4));
					InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_SUCCESS, "购买成功，到服务器上验证一下？");
				}catch(JSONException e){
					Log.e("payment Example", "failed", e);
					InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "购买失败！");
				}
			} else {
				Log.d("payment Example", "purchase success");
				InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_SUCCESS, "无网络购买成功！");
			}
		} else if(resultCode == Activity.RESULT_CANCELED) {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_CANCEL, "小朋友取消了购买!");
			Log.i("Example", "user canceled");
		} else if(resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "购买失败！");
			Log.i("Example", "Invalid payment");
		} else {
			InterfaceIAP.onPayResult(mAdapter, InterfaceIAP.PAYRESULT_FAIL, "不知道返回什么代码了 "+resultCode);
		}
	}
	@Override
	public void setDebugMode(boolean arg0) {
		System.out.println("debug set "+arg0);
		debug = arg0;
	}
	
}
