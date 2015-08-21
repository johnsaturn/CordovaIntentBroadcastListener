package com.john_saturn.cordova.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * This class implements a Cordova plugin that allows to execute javascript in
 * the current webView
 * 
 * @author Jose Puertos
 * 
 */
public class CordovaIntentBroadcastListener extends CordovaPlugin {

	public static String ACTION = "org.apache.cordova.CordovaPlugin.Intent";
	protected static final Logger log = Logger
			.getLogger(CordovaIntentBroadcastListener.class.getName());

	MyBroadcastReceiver mReceiver = new MyBroadcastReceiver(this);
	IntentFilter intentFilter = new IntentFilter(ACTION);

	public class MyBroadcastReceiver extends BroadcastReceiver {

		private CordovaIntentBroadcastListener ci;

		public MyBroadcastReceiver(CordovaIntentBroadcastListener c) {
			ci = c;
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			CordovaIntentBroadcastListener.log
			.info(MyBroadcastReceiver.class.getSimpleName()
					+ " autoparts received broadcast" + intent);
			ci.onNewIntent(intent);
		}

	}

	@Override
	public void onResume(boolean flag) {
		cordova.getActivity().registerReceiver(mReceiver, intentFilter);
		super.onResume(flag);	

	}

	@Override
	public void onPause(boolean flag) {
		
		try {
			cordova.getActivity().unregisterReceiver(mReceiver);
		} catch (Exception ex) {
			log.info("Error autoparts unregistering " + ex.getMessage());
		}
		super.onPause(flag);
	}

	/**
	 * Called after plugin construction and fields have been initialized.
	 */
	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView) {
		super.initialize(cordova, webView);
		
		cordova.getActivity().registerReceiver(mReceiver, intentFilter);

	}

	@Override
	public void onNewIntent(Intent intent) {

		

		// Match the Action
		if (ACTION.equals(intent.getAction())) {

			// Check that extra info is presented
			Bundle bundle = intent.getExtras();
			if (bundle == null || bundle.keySet() == null)
				return;

			// Iterate all keys
			for (String key : bundle.keySet()) {
				
				if (key.startsWith("JS")) {
					String script = bundle.getString(key);
					try {
						this.webView.evaluateJavascript(script, null);
					} catch (Exception ex) {
						log.log(Level.WARNING, "Error while processing intent",
								ex);

					}
				}

			}

		}
	}

}
