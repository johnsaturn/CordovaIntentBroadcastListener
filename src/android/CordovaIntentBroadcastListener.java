package com.john_saturn.cordova.plugins;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.cordova.CordovaPlugin;

import android.content.Intent;
import android.os.Bundle;

/**
 * This class implements a Cordova plugin that allows to execute javascript in the current webView
 * @author Jose Puertos
 * 
 */
public class CordovaIntentBroadcastListener extends CordovaPlugin {

	public static String ACTION="org.apache.cordova.CordovaPlugin.Intent";
	private static Logger log=Logger.getLogger(CordovaIntentBroadcastListener.class.getName());

	@Override
	public void onNewIntent(Intent intent) {
		//Invoke parent..
		super.onNewIntent(intent);

		log.info("Receiving Intent "+ intent.getAction() + ":" + intent );
		//Match the Action
		if(ACTION.equals(intent.getAction())){

			Bundle bundle=intent.getExtras();
			//Get all keys
			String[] keys=(String[])bundle.keySet().toArray();

			//Iterate over additional values
			for(int i=0;i<keys.length;i++){
				String key=keys[i];
				//Filter keys
				if(key.startsWith("JS")){
					String script=bundle.getString(key);
					try{
						this.webView.evaluateJavascript(script, null);
					}catch(Exception ex){
						log.log(Level.WARNING, "Error while processing intent", ex);
					}
				}

			}

		}

	}

}
