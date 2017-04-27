package com.whut.surfacemonitorproject_wjj.surfacemonitorservice;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.surfacemonitorproject_wjj.utils.MyConstants;
import com.whut.surfaceproject_wjj.R;

import java.util.List;


public class SurfaceMonitorService extends Service {

	private final static String TAG = "SurfaceMonitorService";
	private RelativeLayout mMainLayout = null;
	private static final int MSG_DISPLAY_SURFACE = 0x1000;
	private long mPrevUidRx = 0;
	private long mPrevUidTx = 0;
	private TextView mTextView;
	private String mAppName = null;
	private double mLongTrafficValuePerSec = 0;
	private double mLongTrafficValuePerMin = 0;
	private SurfacePolicy mSurfacePolicy;
	private String mAppNameReserve = null;

	/**
	 * 间隔1秒刷新一次数据
	 */
	@SuppressLint("HandlerLeak")
	private Handler mTrafficMonitorHandler = new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		    // TODO Auto-generated method stub
		    super.handleMessage(msg);
		    switch (msg.what) {
			case MSG_DISPLAY_SURFACE:
//				String tempName = checkAppName();
				String tempName = "com.qiyi.video";
				if (MyConstants.DEBUG) {
					Log.d(TAG, "handleMessage: tempName = " + tempName);
				}
				if (!TextUtils.isEmpty(tempName)) {
					mSurfacePolicy.invokSurfaceDumpPerSec(tempName);
				}

				mTrafficMonitorHandler.removeMessages(MSG_DISPLAY_SURFACE);
				mTrafficMonitorHandler.sendEmptyMessageDelayed(MSG_DISPLAY_SURFACE, 1000);
				
				break;
			default:
				break;
			}
	    }
	};
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		RelativeLayout holder = (RelativeLayout)mMainLayout.findViewById(R.id.touchless_surface_holder);
		holder.removeAllViews();
		
		WindowManager windowmanager = (WindowManager)this.getSystemService("window");
		windowmanager.removeViewImmediate(mMainLayout);
		unregisterReceiver(mTrafficMonitorReceiver);
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i(TAG, "onStart");
		mAppName = getRunningAppName();
//		checkAppName();
		mAppNameReserve = "com.qiyi.video";

		mSurfacePolicy = new SurfacePolicy(this, new SurfacePolicy.SurfacePolicyCallback() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				showToast(getResources().getString(R.string.rise_resolution));
				return false;
			}

			@Override
			public double onSurfaceValuePerSecond(String value) {
				// TODO Auto-generated method stub
			    mTextView.setText(getResources().getString(R.string.real_traffic_display) + " : \n" + value);
			    mTextView.setTextColor(0xffffffff);
				return 0;
			}
		});
		
		mMainLayout = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.item_surface_show, null);
		WindowManager windowmanager = (WindowManager)this.getSystemService("window");
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		wmParams.format = 1;
		wmParams.gravity = Gravity.START;
		wmParams.width = 1200;
		wmParams.height = 1920;
		windowmanager.addView(mMainLayout, wmParams);
		
		mTextView = new TextView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mTextView.setLayoutParams(params);
		
		RelativeLayout holder = (RelativeLayout)mMainLayout.findViewById(R.id.touchless_surface_holder);
		holder.removeAllViews();
		mMainLayout.setLayoutParams(params);
		holder.addView(mTextView, 0);//在第一个位置添加view
		
		if (!mTrafficMonitorHandler.hasMessages(MSG_DISPLAY_SURFACE)) {
	        mTrafficMonitorHandler.sendEmptyMessageDelayed(MSG_DISPLAY_SURFACE, 1000);
		}
	    initBroadCastReceiver();
	}
	
	/**
	 *
	 * @return
     */
	private String checkAppName() {
		String name = getRunningAppName();
//		getRunningAppNameOnL();
		try {
			if (TextUtils.isEmpty(name) && !TextUtils.isEmpty(mAppNameReserve)) {
				return mAppNameReserve;
			}
			if (WhiteList.isWhiteListCheckPass(name)) {
				mAppNameReserve = name;
				return mAppNameReserve;
			}
		} finally {
			if (!TextUtils.isEmpty(mAppNameReserve) && !TextUtils.isEmpty(name) && !mAppNameReserve.equals(name)) {
				mSurfacePolicy.resetAllPolicy();
			}
		}
		return null;
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 得到当前运行的应用名称
	 * @return
     */
	private String getRunningAppName() {
//		getRunningAppNameOnL();
//        long ts = System.currentTimeMillis();
//        UsageStatsManager usageStatsManager = (UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
//        int retry = 3;
//        long time = ts - 200000;
//        List<UsageStats> queryUsageStats = null;
//        while(retry-- >= 0) {
//        	if (retry == 2)      time = ts - 200000;
//        	else if (retry == 1) time = ts - 2000000;
//        	else if (retry == 0) time = ts - 20000000;
//	        queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, time, ts);
//	        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
//	        	Log.e(TAG, "retrytime : " + retry);
//	        	continue;
//	        } else {
//	        	break;
//	        }
//        }
//        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
//        	return null;
//        }
//
//        UsageStats recentStats = null;
//        for (UsageStats usageStats : queryUsageStats) {
//            if(recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()){
//                recentStats = usageStats;
//            }
//        }
//		return recentStats.getPackageName();
		return "com.qiyi.video";
	}

	/**
	 * Android 5.0 中获取应用名的方法
	 */
	private void getRunningAppNameOnL() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();  

        for (RunningAppProcessInfo appProcess : appProcessList) {
            int pid = appProcess.pid;  
            String processName = appProcess.processName;
            Log.i(TAG, "processName: " + processName + "  pid: " + pid);
  
            String[] pkgNameList = appProcess.pkgList;
  
            for (int i = 0; i < pkgNameList.length; i++) {  
                String pkgName = pkgNameList[i];
                Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
            }  
        }
	}
	
	private void showToast(String msg) {
		Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	private BroadcastReceiver mTrafficMonitorReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String cmd = arg1.getAction();
			Log.i(TAG, "getReceiver cmd : " + cmd);
			if (Intent.ACTION_SCREEN_ON.equalsIgnoreCase(cmd)) {
				mSurfacePolicy.resetAllPolicy();
				mTrafficMonitorHandler.sendEmptyMessageDelayed(MSG_DISPLAY_SURFACE, 1000);
			} else if (Intent.ACTION_SCREEN_OFF.equalsIgnoreCase(cmd)) {
				mTrafficMonitorHandler.removeMessages(MSG_DISPLAY_SURFACE);
			}
		}
	};
	
	private void initBroadCastReceiver() {
		IntentFilter it = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		it.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mTrafficMonitorReceiver, it);
	}
}
