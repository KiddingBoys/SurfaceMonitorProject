package com.whut.surfacemonitorproject_wjj.surfacemonitorservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.whut.surfacemonitorproject_wjj.utils.MyConstants;


/**
 * 当接收到开机（或亮屏）广播时，启动Service
 */
public class BootReceiver extends BroadcastReceiver {

	private final String TAG = "BootReceiver";

	final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
//	final String SCREEN_ON = "android.intent.action.SCREEN_ON";
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
        if (BOOT_ACTION.equals(intent.getAction())) {
			if(MyConstants.DEBUG){
				Log.d(TAG, "onReceive : BOOT_ACTION " + BOOT_ACTION);
			}
//      	    context.startService(new Intent(context, SurfaceMonitorService.class));
        }
	}
}
