package com.liyong.iap;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;

public class IapActivity extends Activity{
	IabHelper iabHelper;
	String key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqx49w8AVd22wNK1PJPjbaCasGnGdMKGPOLw6NjwmmLyLzP26EM02wCp9H8vKtb2gfCTUj9uuU4StaHeZIs+sqN0Y5qa7sCbQtGU5nf8BKECmRNIrPZ0LVh8OBykc/12dzvaIqyjPbNCd6mXmSH55u1HCtvM/qv1NytbkO8FDyf4ik3rrrFVv5UA/htMBxq3pJMqzA+tpPsFitUVqwYV7XRbUs7UBiu35BUyI1cDB/KQE5MdHIiLLbqI1uz5ObwUE9N63Su+kzpR9hjx3zSff9RfPikmbi7YMqctZK1sTerInX+Ljj/C8LkSSzI7IIDOzpBxultjNKnRbMMYe+BgTcwIDAQAB";
	IInAppBillingService mService;
	ServiceConnection sc;
	Activity act;
	Handler handler;
	int state = 0;
	String productName;
	Purchase purYet;
	
	IabHelper.QueryInventoryFinishedListener list = new IabHelper.QueryInventoryFinishedListener(){
		
		@Override
		public void onQueryInventoryFinished(IabResult result, Inventory inv) {
			// TODO Auto-generated method stub
			if(result.isFailure()) {
				Log.e("IAP", "Query Failed");
			}
			//String price = inv.getSkuDetails("com.liyong.test2").getPrice();
			//Log.e("IAP", "price"+price);
			
			//Purchase pur = inv.getPurchase(productName);
			
			//开始消费 就不能轻易退出了
			
			//如果有购买过相关产品的 则消耗掉
			if(inv.hasPurchase(productName)) {
				Log.e("IAP", "purchase yet please consume it now");
				state = 1;
				Purchase pur = inv.getPurchase(productName);
				iabHelper.consumeAsync(pur, consumeListener);
			} else {
			//user id 
				Log.e("IAP", "start Purchase");
				//state = 1;
				iabHelper.launchPurchaseFlow(act, productName, 1001, purchaseListener, "uid");
			}
		}
	};
	
	IabHelper.OnIabPurchaseFinishedListener purchaseListener = new IabHelper.OnIabPurchaseFinishedListener() {
		
		@Override
		public void onIabPurchaseFinished(IabResult result, Purchase info) {
			Log.e("IAP", "pucharse finished "+result+" "+info);
			int resCode = result.getResponse();
			
			//检测购买的商品已经 拥有了 则消费掉
			if(resCode == IabHelper.BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED) {
				//state = 1;
				//Purchase pur = new Purchase(IabHelper.ITEM_TYPE_INAPP, "ui d");
				//iabHelper.consumeAsync(info, consumeListener);
				Log.e("IAP", "Already owned");
				//iabHelper.consumeAsync(productName, consumeListener);
				//how to get purchase data and purchase signature
			} else {
				// TODO Auto-generated method stub
				if(result.isFailure()) {
					Log.e("IAP", "purchase failed");
					//iabHelper.consumeAsync(info, consumeListener);
				//购买完就消耗掉该产品
				} else {
					Log.e("IAP", "purchase suc "+info);
					//购买成功 必须等待消费掉才能退出
					state = 1;
					iabHelper.consumeAsync(info, consumeListener);
				
				}
			}
		}
	};
	
	IabHelper.OnConsumeFinishedListener consumeListener = new IabHelper.OnConsumeFinishedListener() {
		
		@Override
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			// TODO Auto-generated method stub
			if(result.isSuccess()) {
				Log.e("IAP", "consume success!!");
			} else {
				Log.e("IAP", "consume failed!");
			}
			Intent data = new Intent();
			
			state = 0;
			
			act.setResult(Activity.RESULT_OK, data);
			act.finish();
		}
	};
	
	protected void onCreate(Bundle sv) {
		super.onCreate(sv);
		act = this;
		Intent it = getIntent();
		productName = it.getStringExtra("productName");
		Log.e("IAP", "productName is "+productName);
	/*
	
		
		
		sc = new ServiceConnection(){
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				Log.e("IAP", "connected "+name);
				mService = IInAppBillingService.Stub.asInterface(service);
				ArrayList skuList = new ArrayList();
				skuList.add("com.liyong.test1");
				Bundle queryBundle = new Bundle();
				queryBundle.putStringArrayList("ITEM_ID_LIST", skuList);
				Bundle details = null;
				try {
					Log.e("IAP", "start getSkuDetails "+getPackageName());
					details = mService.getSkuDetails(2, getPackageName(), "inapp", queryBundle);
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(details != null) {
					Log.e("IAP", "details not null "+details);
					int response = details.getInt("RESPONSE_CODE");
					Log.e("IAP", "response is "+response);
					
					if(response == 0) {
						List<String> responseList = details.getStringArrayList("DETAILS_LIST");
						for(String thisResponse : responseList) {
							JSONObject object = null;
							try {
								object = new JSONObject(thisResponse);
								String sku = object.getString("productId");
								String price = object.getString("price");
							    Log.e("IAP", "sku price "+sku+" "+price);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
				}
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				Log.e("IAP", "Disconnected "+arg0);
				mService = null;
			}
		};
		
		bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"), sc, Context.BIND_AUTO_CREATE);
		*/
		//购买完之后 finish掉窗口
		
		
		iabHelper = new IabHelper(this, key);
		
		iabHelper.enableDebugLogging(true);
		//开始连接配置
		iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener(){
			
			@Override
			public void onIabSetupFinished(IabResult result) {
				Log.e("IAP", result.toString());
				// TODO Auto-generated method stub 
				if(!result.isSuccess()) {
					Log.e("IAP", "setup failed");
				} else {
					List<String> goods = new ArrayList<String>();
					goods.add("com.liyong.test1");
					goods.add("com.liyong.test2");
					//获取物品信息
					iabHelper.queryInventoryAsync(true, goods, list);
					
				}
			}
		});
			
		
		
		
	}
	
	protected void onDestroy(){
		super.onDestroy();
		
		if(iabHelper != null) {
			iabHelper.dispose();
		}
		iabHelper = null;
		
		/*
		if(sc != null) {
			unbindService(sc);
		}
		*/
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//购买中不能退出
		if(state == 0) {
			if(keyCode == KeyEvent.KEYCODE_BACK) {
				//wv.goBack();
				Log.e("IAP", "canceled");
				setResult(Activity.RESULT_CANCELED);
				finish();
			} 
		}
		return true;
	}
	//purchaseFlow 需要向Activity 再由Activity 处理结果给iabHelper
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		//if(resultCode == Activity.RESULT_CANCELED) {
		Log.e("IAP", "onActivityResult "+requestCode+" "+resultCode+" "+data);
		if(!iabHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
		//} 
		
	}
}
