package com.whut.surfacemonitorproject_wjj.surfacemonitorservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;


import com.whut.surfacemonitorproject_wjj.policy.browser.ApusBrowserPlayerPolicy;
import com.whut.surfacemonitorproject_wjj.policy.browser.BrowserPlayerPolicy;
import com.whut.surfacemonitorproject_wjj.policy.browser.QQBrowserPlayerPolicy;
import com.whut.surfacemonitorproject_wjj.policy.browser.UCPlayerPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.IQIYIPadPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.IQIYIPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.LetvClientPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.MangguoTVPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.QQlivePolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.SohuVideoPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.TudouPolicy;
import com.whut.surfacemonitorproject_wjj.policy.player.YoukuPolicy;
import com.whut.surfacemonitorproject_wjj.utils.Utils;

import java.util.List;

/**
 * 图层策略类
 * @author wujiajun
 */
public class SurfacePolicy {

	/**
	 * 作为回调，在此类调用SurfaceMonitorService中实现的方法
	 */
	public interface SurfacePolicyCallback {
		public double onSurfaceValuePerSecond(String value);
		public boolean onLowSurfaceVideo();
	}


	private Context mContext = null;
	private long mPrevUidRx = 0;
	private long mPrevUidTx = 0;
	private long mCPUTotalTime = 0L;
	private long mAPPTotalTime = 0L;
	private SurfacePolicyCallback mSurfacePolicyCallback = null;

	private PackageManager mPM = null;//用于获取应用基本信息
	private YoukuPolicy mYoukuPolicy = null;
	private SohuVideoPolicy mSohuVideoPolicy = null;
	private TudouPolicy mTudouPolicy = null;
	private QQlivePolicy mQQlivePolicy = null;
	private LetvClientPolicy mLetvClientPolicy = null;
	private IQIYIPolicy mIQIYIPolicy = null;
	private MangguoTVPolicy mMangguoTVPolicy = null;
	private IQIYIPadPolicy mIQIYIPadPolicy = null;
	private BrowserPlayerPolicy mBrowserPlayerPolicy = null;
	private UCPlayerPolicy mUCPlayerPolicy = null;
	private QQBrowserPlayerPolicy mQQBrowserPlayerPolicy = null;
	private ApusBrowserPlayerPolicy mApusBrowserPlayerPolicy = null;

	public SurfacePolicy(Context context, SurfacePolicyCallback callback) {
		this.mContext = context;
		this.mSurfacePolicyCallback = callback;
		this.mPM = mContext.getPackageManager();

		/************************START 实例化具体的policy*****************************/
		mYoukuPolicy = new YoukuPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mSohuVideoPolicy = new SohuVideoPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mTudouPolicy = new TudouPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mQQlivePolicy = new QQlivePolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mLetvClientPolicy = new LetvClientPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mIQIYIPolicy = new IQIYIPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mMangguoTVPolicy = new MangguoTVPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mIQIYIPadPolicy = new IQIYIPadPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mBrowserPlayerPolicy = new BrowserPlayerPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mUCPlayerPolicy = new UCPlayerPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mQQBrowserPlayerPolicy = new QQBrowserPlayerPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		mApusBrowserPlayerPolicy = new ApusBrowserPlayerPolicy(this.mContext, new PlayerPolicy.SurfaceEvent() {
			@Override
			public boolean onLowSurfaceVideo() {
				// TODO Auto-generated method stub
				mSurfacePolicyCallback.onLowSurfaceVideo();
				return false;
			}
		});
		/************************END 实例化具体的policy*****************************/
	}

	/**
	 * 每秒执行一次获取图层信息
	 * @param name
     */
	public void invokSurfaceDumpPerSec(String name) {
		if (TextUtils.isEmpty(name)) return;
		ApplicationInfo ai = null;
		int pid = -1;
//		try {
//			ai = mPM.getApplicationInfo(name, PackageManager.GET_ACTIVITIES);
//		} catch (NameNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
		ActivityManager mActivityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
			String processName = appProcess.processName;
			if (processName.contains(name)) {
				pid = appProcess.pid;
				break;
			}
		}

//		if (ai != null) {
		Log.d("TTT", "pid : " + pid);

			long cpuTime = Utils.getTotalCpuTime();
			long appTime = Utils.getAppCpuTime(pid);
//			long cpuTime = 500;
//			long appTime = 300;

			String appNameShow = "";
			name = Utils.getAppNameByPid(mContext,pid);
			String out = "";
			if (Utils.isMangguoTVApp(name)){
				out = mMangguoTVPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[0];
//			}else if (Utils.isBao(name)){
//				out = mSohuVideoPolicy.invokSurfaceDump();
//				appNameShow = Utils.appsNameZh[1];
			}else if (Utils.isYoukuApp(name)){
				out = mYoukuPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[2];
			}else if (Utils.isTudouVideoApp(name)){
				out = mTudouPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[3];
			}else if (Utils.isQQliveApp(name)){
				out = mQQlivePolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[4];
			}else if (Utils.isLetvClientApp(name)){
				out = mLetvClientPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[5];
			}else if (Utils.isSohuVideoApp(name)){
				out = mSohuVideoPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[6];
			}else if (Utils.isIQIYIApp(name)){
				out = mIQIYIPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[7];
			}else if (Utils.isIQIYIHDApp(name)){
				out = mIQIYIPadPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[8];
			}else if (Utils.isAndroidBrowserApp(name)){
				out = mBrowserPlayerPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[9];
			}else if (Utils.isQQBrowserApp(name)){
				out = mQQBrowserPlayerPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[10];
			}else if (Utils.isApusBrowserApp(name)){
				out = mApusBrowserPlayerPolicy.invokSurfaceDump();
				appNameShow = Utils.appsNameZh[11];
			}

//			mSurfacePolicyCallback.onSurfaceValuePerSecond(out);
			mSurfacePolicyCallback.onSurfaceValuePerSecond("当前应用: " + appNameShow
					+out + "\n cpuTime = " + cpuTime
					+ " \n appTime = " + appTime
					+ " \n AppName = " + Utils.getAppNameByPid(mContext,pid));


			mCPUTotalTime = cpuTime;
			mAPPTotalTime = appTime;
//		}
	}

	public void resetAllPolicy() {
		mYoukuPolicy.resetAll();
		mSohuVideoPolicy.resetAll();
		mTudouPolicy.resetAll();
		mQQlivePolicy.resetAll();
		mLetvClientPolicy.resetAll();
		mIQIYIPolicy.resetAll();
		mMangguoTVPolicy.resetAll();
		mIQIYIPadPolicy.resetAll();
		mBrowserPlayerPolicy.resetAll();
		mUCPlayerPolicy.resetAll();
		mQQBrowserPlayerPolicy.resetAll();
		mApusBrowserPlayerPolicy.resetAll();
	}
}
