package com.liyong.paypal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.apache.commons.httpclient.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.*;
public class MyWeb extends Activity {
	WebView wv;
	FrameLayout fl;
	int result = 0;
	int invoice;
	String item_name;
	String amount;
	String currency_code;
	
	private void setupUI() {
		wv = new WebView(this);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.getSettings().setSupportZoom(true);
		wv.getSettings().setBuiltInZoomControls(true);
		try {
			String normal = "invoice="+URLEncoder.encode(""+invoice, "UTF-8")+"&"+"item_name="+URLEncoder.encode(item_name, "UTF-8")+"&amount="+URLEncoder.encode(amount, "UTF-8")+"&currency_code="+URLEncoder.encode(currency_code, "UTF-8");
			Log.e("PayPal", "normal "+normal);
			String url = "http://www.caesarsgame.com/charge.php?"+normal;
			Log.e("PayPal", "url: "+url);
			wv.loadUrl(url);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		wv.requestFocus();
		wv.setWebViewClient(new WebViewClient() {
			
		});
		wv.setWebChromeClient(new WebChromeClient() {
			//跳转到success 页面表示成功
			@Override
			public void onReceivedTitle(WebView view, String title) {
				Log.e("PayPal", "title "+title);
				
				if(title.equals("success")) {
					PaypalJava.Result = 1;
					result = 1;
					Log.e("PayPal", "success now !");
				}
				
			}
		});
		
		LinearLayout topLayout = new LinearLayout(this);
		topLayout.setOrientation(LinearLayout.VERTICAL);
		Button back = new Button(this);
		back.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		back.setText("关闭");
		back.setTextColor(Color.argb(255, 0, 0, 255));
		back.setTextSize(14);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent data = new Intent();
				//从我方服务器上面再检测一下
				if(result == 0) {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					HttpGet httpget = new HttpGet("http://caesarsgame.com:9070/checkBuyRecord?uid=1&invoice="+invoice);
					ResponseHandler<String> responseHandler = new BasicResponseHandler();
					try {
						String responseBody = httpClient.execute(httpget, responseHandler);
						System.out.println(responseBody);
						JSONObject obj = new JSONObject(responseBody);
						result = obj.getInt("code");
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(result == 0) {
					setResult(Activity.RESULT_CANCELED, data);
				}else
					setResult(Activity.RESULT_OK, data);
				finish();
			}
			
		});
		topLayout.addView(back);
		topLayout.addView(wv);
		fl.addView(topLayout);
	}
	protected void onCreate(Bundle sv) {
		super.onCreate(sv);
		
		fl = new FrameLayout(this);
		addContentView(fl, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		Intent in = getIntent();
		
		invoice = in.getIntExtra("invoice", 0);
		item_name = in.getStringExtra("item_name");
		amount = in.getStringExtra("amount");
		currency_code = in.getStringExtra("currency_code");
		
		if(invoice != 0) {
			setupUI();
		} else {
			//自己获得
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://caesarsgame.com:9070/genRecordId?uid=1&kind=0");
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			try {
				String responseBody = httpClient.execute(httpget, responseHandler);
				System.out.println(responseBody);
				JSONObject obj = new JSONObject(responseBody);
				invoice = obj.getInt("invoice");
				setupUI();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(wv.canGoBack() && keyCode == KeyEvent.KEYCODE_BACK) {
			//wv.goBack();
		} else {
			//finish();
		}
		return false;
	}
}
