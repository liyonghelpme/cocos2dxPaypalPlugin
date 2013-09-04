/****************************************************************************
Copyright (c) 2010-2012 cocos2d-x.org

http://www.cocos2d-x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/
package org.cocos2dx.hellocpp;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.lib.Cocos2dxGLSurfaceView;
import org.cocos2dx.plugin.PluginWrapper;

import com.amazon.inapp.purchasing.PurchasingManager;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.liyong.iap.AmazonIAP;
import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.sdk.payments.PayPalService;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class HelloCpp extends Cocos2dxActivity{
	
	public com.liyong.paypal.PaypalJava paypalJava;

	@Override
	protected void onStart() {
		super.onStart();
		PurchasingManager.registerObserver(new AmazonIAP(this));
	}
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		PluginWrapper.init(this);
		PluginWrapper.setGLSurfaceView(Cocos2dxGLSurfaceView.getInstance());
	
	}
	
	
	public void onDestroy() {
		stopService(new Intent(this, PayPalService.class));
		super.onDestroy();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d("Paypal HelloCpp", "onActivityResult "+requestCode+" "+resultCode);
		paypalJava.onPayResult(requestCode, resultCode, data);
		
	}
	
    static {
         System.loadLibrary("hellocpp");
    }
}
