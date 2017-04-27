package com.whut.surfacemonitorproject_wjj.policy.player;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;

import com.whut.surfacemonitorproject_wjj.surfacemonitorservice.PlayerPolicy;
import com.whut.surfacemonitorproject_wjj.utils.Utils;


public class IQIYIPolicy extends PlayerPolicy {
	private final static int IQIYI_VIDEO_RESOLUTION_THREDHOLD = 600;
	private final static int IQIYI_VIDEO_ADVERTISE_TIME = 90 * 1000;
	private final static int IQIYI_VIDEO_SHOWTOAST_TIME = 30000 * 1000;
	private final static int IQIYI_TOAST_DISP_PERIOD = 100;
	private final static int IQIYI_WIFI_THREDHOLD = 50;
	
	private final static int MSG_TIPS_OVER_3MIN = 0x1000;
	private final static int MSG_TRAFFIC_STATISTICS_1MIN = 0x1001;
	private final static int MSG_SURFACE_LOWRES = 0x1002;
	private Context mContext;
	private SurfaceEvent mSurfaceEvent;
	private boolean mIsAllowToast = true;
	private Handler mEventHandler = new Handler() {
        public void handleMessage(Message msg) {
             switch (msg.what) {   
                  case MSG_TIPS_OVER_3MIN:
      				  mEventHandler.removeMessages(MSG_TIPS_OVER_3MIN);
      				  mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
                      mIsAllowToast = false;
                      break;
                  case MSG_TRAFFIC_STATISTICS_1MIN:
                	  break;
                  case MSG_SURFACE_LOWRES:
                	  mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
                	  mSurfaceEvent.onLowSurfaceVideo();
                	  break;
             }   
             super.handleMessage(msg);   
        }
    };
	public IQIYIPolicy (Context context, SurfaceEvent event) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mSurfaceEvent = event;
	}

	@Override
	public String invokSurfaceDump() {
		// TODO Auto-generated method stub
		String out = Utils.getSufaceFlinger("dumpsys SurfaceFlinger");
		int score = Utils.getWifiEnvScore(this.mContext);
		if (out != null) {
			String[] res = out.split("x");
			if (Utils.resIsChange(Double.parseDouble(res[0]), Double.parseDouble(res[1]))) {
				resetAll();//分辨率发生变化则重置提示的倒计时
			}
			double minRes = Math.min(Double.parseDouble(res[0]), Double.parseDouble(res[1]));//取最小的分辨率垂直像素
			if (mIsAllowToast && minRes <= IQIYI_VIDEO_RESOLUTION_THREDHOLD) {
				// video height is smaller than 600 in IQIYI, will toast
				if (!mEventHandler.hasMessages(MSG_SURFACE_LOWRES) && score >= IQIYI_WIFI_THREDHOLD
						&& Utils.getScreenDirection(mContext) == Configuration.ORIENTATION_LANDSCAPE) {
				    mEventHandler.sendEmptyMessageDelayed(MSG_SURFACE_LOWRES, IQIYI_VIDEO_ADVERTISE_TIME);
				    if(!mEventHandler.hasMessages(MSG_TIPS_OVER_3MIN)){
					    mEventHandler.sendEmptyMessageDelayed(MSG_TIPS_OVER_3MIN, IQIYI_VIDEO_SHOWTOAST_TIME);
				    }
				}
			} else {
			    mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
			}
		}
		if(Utils.getMWMaintained() || Utils.getScreenDirection(mContext) != Configuration.ORIENTATION_LANDSCAPE){
			resetAll();
		}
		return out + " \n WIFI : " + score + " \n orientation : " + Utils.getScreenDirection(mContext) + " \n 其他 : null";
	}

	@Override
	public void resetAll() {
		// TODO Auto-generated method stub
		mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
		mEventHandler.removeMessages(MSG_TIPS_OVER_3MIN);
		mIsAllowToast = true;
	}

}
