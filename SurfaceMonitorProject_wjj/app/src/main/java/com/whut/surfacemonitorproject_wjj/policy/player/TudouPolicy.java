package com.whut.surfacemonitorproject_wjj.policy.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whut.surfacemonitorproject_wjj.surfacemonitorservice.PlayerPolicy;
import com.whut.surfacemonitorproject_wjj.utils.Utils;


public class TudouPolicy extends PlayerPolicy {
	private final static int TUDOU_VIDEO_RESOLUTION_THREDHOLD = 600;
	private final static int TUDOU_VIDEO_ADVERTISE_TIME = 90 * 1000;
	private final static int TUDOU_TOAST_DISP_PERIOD = 100;
	private final static int TUDOU_WIFI_THREDHOLD = 50;
	
	private final static int MSG_TIPS_OVER_3MIN = 0x1000;
	private final static int MSG_TRAFFIC_STATISTICS_1MIN = 0x1001;
	private final static int MSG_SURFACE_LOWRES = 0x1002;
	private int mToastDispCount = 0;
	private Context mContext;
	private SurfaceEvent mSurfaceEvent;
	private boolean mIsAllowToast = true;
	private Handler mEventHandler = new Handler() {
        public void handleMessage(Message msg) {
             switch (msg.what) {   
                  case MSG_TIPS_OVER_3MIN:
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
	
	public TudouPolicy (Context context, SurfaceEvent event) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.mSurfaceEvent = event;
	}
	
	private int playerStatus(long totalTime, long cpuTime) {
		double cpuRatio = (double)cpuTime / (double)totalTime;
		if (cpuRatio < 0.01) return NO_PLAY;
	    return PLAY;
	}

	@Override
	public String invokSurfaceDump() {
		// TODO Auto-generated method stub
		String out = Utils.getSufaceFlinger("dumpsys SurfaceFlinger");
		int score = Utils.getWifiEnvScore(this.mContext);
		if (mIsAllowToast && out != null) {
			String[] res = out.split("x");
			if (Utils.resIsChange(Double.parseDouble(res[0]), Double.parseDouble(res[1]))) {
				mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
			}
			
			double minRes = Math.min(Double.parseDouble(res[0]), Double.parseDouble(res[1]));
			if (minRes <= TUDOU_VIDEO_RESOLUTION_THREDHOLD) {
				// video height is smaller than 600 in Letv, will toast
				if (!mEventHandler.hasMessages(MSG_SURFACE_LOWRES) && score >= TUDOU_WIFI_THREDHOLD) {
				    mEventHandler.sendEmptyMessageDelayed(MSG_SURFACE_LOWRES, TUDOU_VIDEO_ADVERTISE_TIME);
				}
			} else {
			    mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
			}
		}
		return out + " WIFI : " + score;
	}

	@Override
	public void resetAll() {
		// TODO Auto-generated method stub
		mEventHandler.removeMessages(MSG_SURFACE_LOWRES);
	}

}
