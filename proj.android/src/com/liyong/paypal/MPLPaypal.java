package com.liyong.paypal;

import java.util.Hashtable;

import org.cocos2dx.plugin.InterfaceIAP.IAPAdapter;

import com.paypal.android.MEP.PayPal;

public class MPLPaypal implements IAPAdapter {
	private static final int server = PayPal.ENV_SANDBOX;
	private static final String appID = "APP-80W284485P519543T";
	private static final int request = 1;
	
	@Override
	public void configDeveloperInfo(Hashtable<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSDKVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void payForProduct(Hashtable<String, String> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDebugMode(boolean arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
