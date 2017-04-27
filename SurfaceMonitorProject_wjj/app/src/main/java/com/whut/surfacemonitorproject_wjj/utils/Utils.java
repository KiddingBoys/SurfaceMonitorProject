package com.whut.surfacemonitorproject_wjj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import android.os.ServiceManager;

/**
 * @author wujiajun
 */
public class Utils {
	
	private static final String TAG = Utils.class.getSimpleName();
	private static Object sMultiWinService;
    private static Method sGetMWMaintainedMethod = null;

	/**
	 * 获取总的CPU耗时
	 * @return
	 */
	public static long getTotalCpuTime() {
		String[] cpuInfos = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		long totalCpu = Long.parseLong(cpuInfos[2])
				+ Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
				+ Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
				+ Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
		return totalCpu;
	}

	/**
	 * 根据应用的pid得到应用的CPU耗时
	 * PID 指进程ID. PID是进程的身份标识
	 * @param pid
	 * @return
	 */
	public static long getAppCpuTime(int pid) {
		String[] cpuInfos = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream("/proc/" + pid + "/stat")), 1000);
			String load = reader.readLine();
			reader.close();
			cpuInfos = load.split(" ");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (cpuInfos == null || cpuInfos.length == 0) {
			Log.e(TAG, "cpuInfos is null | 0, return 0");
			return 0L;
		}
		long appCpuTime = Long.parseLong(cpuInfos[13])
				+ Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
				+ Long.parseLong(cpuInfos[16]);
		return appCpuTime;
	}

	/***********************START 判断当前是哪个应用***************************/
	public static boolean isSohuVideoApp(String name) {
		if (name.contains("com.sohu")) {
			if (name.contains("tv") || name.contains("video")) return true;
		}
		return false;
	}
	
	public static boolean isYoukuApp(String name) {
		if (name.equals("com.youku.phone") || name.equals("com.youku.tv")) return true;
		else return false;
	}
	
	public static boolean isTudouVideoApp(String name) {
		if (name.contains("com.tudou")) {
			if (name.contains("android")) return true;
		}
		return false;
	}
	
	public static boolean isQQliveApp(String name) {
		if (name.contains("com.tencent.qqlive")) return true;
		return false;
	}
	
	public static boolean isLetvClientApp(String name) {
		if (name.contains("com.letv.android.client")) return true;
		return false;
	}

	public static boolean isIQIYIApp(String name) {
		if (name.contains("com.qiyi.video")) return true;
		return false;
	}

	public static boolean isMangguoTVApp(String name) {
		if (name.contains("com.hunantv.imgo.activity")) return true;
		return false;
	}
	
	public static boolean isIQIYIHDApp(String name) {
		if (name.contains("com.qiyi.video.pad")) return true;
		return false;
	}

	public static boolean isAndroidBrowserApp(String name) {
		if (name.contains("com.android.browser")) return true;
		return false;
	}

	public static boolean isUCBrowserApp(String name) {
		if (name.contains("com.UCMobile")) return true;
		return false;
	}

	public static boolean isQQBrowserApp(String name) {
		if (name.contains("com.tencent.mtt")) return true;
		return false;
	}

	public static boolean isApusBrowserApp(String name) {
		if (name.contains("com.apusapps.browser")) return true;
		return false;
	}
	/***********************END 判断当前是哪个应用***************************/


	public static String getSufaceFlinger(String cmd) {
	    try {
			//使应用获取root权限
//			if(!isRootApplication){
//				if(upgradeRootPermission(mContext.getPackageCodePath())){
//					isRootApplication = true;
//					if(MyConstants.DEBUG){
//						Log.d("WJJ_TAG", "upgradeRootPermission is OK");
//					}
//				}
//			}
	    	Process proc = Runtime.getRuntime().exec(cmd);
	    	BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
	    	String line = "";
	    	String out = "";
	    	while ((line = in.readLine()) != null) {
				if(MyConstants.DEBUG){
					Log.d("WJJ_TAG", "getSufaceFlinger : proc ------> " + line);
				}

//	    		if (line.contains("HWC") && line.contains("SurfaceView")) {
//	    			break;
//	    		}
	        }
	    	if (line == null || TextUtils.isEmpty(line)) {
	    		Log.e("", "wjj ======= line is null!");
	    		return null;
	    	}
	    	String[] table = line.split("\\|");
	    	if (table.length < 10) {
	    		Log.e("", "wjj ======== line.split smaller than 10, it's unexcepted!");
	    		return null;
	    	}
	    	String[] res = table[7].trim().split(",");
	    	if (res.length < 4) {
	    		Log.e("", "wjj ======== resolution table smaller than 4, it's unexcepted!");
	    		return null;
	    	}
	    	out = res[2].trim() + "x" + res[3].trim();
	    	return out;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return null;
	    }
    }

	/**
	 * 读取surfaceFlinger文件分析出分辨率信息
	 * @return
     */
	public static String getSurfaceFlingerByFile(String filePath){
		try {
			String encoding = "GBK";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
//					if (!needVersion || versionKey.equals(lineTxt.substring(0,versionKey.length()))) {
				}
				read.close();
				return "";
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return null;
	}

	private static double n1 = 0.0;
	private static double n2 = 0.0;
	public static boolean resIsChange(double a1, double a2) {
		if ((a1 != n1) || (a2 != n2)) {
			n1 = a1;
			n2 = a2;
			return true;
		}
		return false;
	}
	
	private static int getWifiEnvScore(int rssi
			,int linkspeed
			,float nearFrequencyNum
			,float theSameFrequencyNum
			,float pagloss){
		float  a1= (rssi>-40)?100: (((rssi+85)/(float)(-40-(-85)))*100);
		float  nearnum=   ((float) (nearFrequencyNum*0.5+theSameFrequencyNum));
		float  a2 = (nearnum>20)?0:(((float)(20-nearnum)/20)*100);
		float  a3=(linkspeed>=72)?100:(linkspeed/(float)72);
		float  a4 = (pagloss <= 0.1)?100:((float)((0.1-pagloss)/0.9)*100);
		int  a=(int) (a1*0.3+a2*0.2+a3*0.3+a4*0.2);
		
		/*
		if(a<40){
			//return ""+a+" "+getResources().getString(R.string.wrong);
		}else if(a<60){
			//return ""+a+" "+getResources().getString(R.string.common);
		}else if(a<80){
			//return ""+a+" "+getResources().getString(R.string.good);
		}else{
			//return ""+a+" "+getResources().getString(R.string.very_good);
		}*/
		return a;
	}
	
	private static int getWifiRSssi(Context context) {
		WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi_service.getConnectionInfo();
		return wifiInfo.getRssi();
	}
	
	private static String getWifiBSSID(Context context) {
		if (!isWifiEnabled(context))
			return null;
		WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi_service.getConnectionInfo();

		return wifiInfo.getBSSID();

		// wifiInfo.getSSID();
		// wifiInfo.getIpAddress();
		// wifiInfo.getMacAddress();
		// wifiInfo.getNetworkId();
		// wifiInfo.getLinkSpeed();
	}
	
	private static boolean isWifiEnabled(Context context) {
		WifiManager wm = null;
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wm == null) return false;
		return wm.isWifiEnabled();
	}
	
	private static int getWifiLinkSpeed(Context context) {
		if (!isWifiEnabled(context)) return -1;
		WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi_service.getConnectionInfo();
		return wifiInfo.getLinkSpeed();
	}
	
	private static List<ScanResult> getListScanResult(Context context){
		if (!isWifiEnabled(context)) return null;
		WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		List<ScanResult> scanResults=wifi_service.getScanResults();
		return scanResults;
	}
	
	private static int getStaNumWithNeighborChannel(Context context, int channel) {
		int num = 0;
		if (channel <= 0)
			return 0;
		List<ScanResult> list = getListScanResult(context);
		if(list!=null){
			for(ScanResult scan:list){
				if (channel <= 14) {
					if ((channel-2) <= freq2Channel(scan.frequency, 20) && (channel + 2) >= freq2Channel(scan.frequency, 20))
					{
						num++;
					}
				}
				else
				{
					if (channel != freq2Channel(scan.frequency, 20) 
							&& (channel-4) >= freq2Channel(scan.frequency, 20) 
							&& (channel+4) <= freq2Channel(scan.frequency, 20))
					{
						num++;
					}
				}
			}
		}
		return num;
	}
	
	private static int getStaNumWithSameChannel(Context context, int channel) {
		int num = 0;
		if (channel <= 0) return 0;
		List<ScanResult> list = getListScanResult(context);
		if (list != null) {
			for (ScanResult scan : list){
				if (channel == freq2Channel(scan.frequency, 20))
				{
					num++;
				}
			}
		}
		return num;
	}
	
	private static int freq2Channel(int frequency, int bandwidth){
		int channel=-1;
		if (bandwidth == 80) {
	        if (2412 <= frequency && frequency <= 2484) {  /*2.4G Hz*/
	            return -1;
	        }
	        else if (5000<= frequency  && frequency <= 5900) {  /*5G Hz*/
	            channel = ((frequency-5000)/5) - 6;
	        }
	        else {
	        	return -1;
	        }
		}
		else if (bandwidth == 40) {
	        if (2412 <= frequency && frequency <= 2484) {  /*2.4G Hz*/
	        	channel = (frequency - 2412) / 5 + 1;
	        }
	        else if (5000<= frequency  && frequency <= 5900) {  /*5G Hz*/
	        	channel = ((frequency-5000)/5) - 2;
	        }
	        else {
	        	return -1;
	        }
		}
		else {
			if (2412 <= frequency && frequency <= 2484) {  /*2.4G Hz*/
	        	channel = (frequency - 2412) / 5 + 1;
	        }
	        else if (5000 <= frequency  && frequency <= 5900) {  /*5G Hz*/
	        	channel = ((frequency-5000)/5);
	        }
	        else {
	        	return -1;
	        }
			
		}
		return channel;
	}
	
	private static int getCurWifiFreq(Context context) {
		if (!isWifiConnected(context))
			return -1;
		WifiManager wifi_service = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifi_service.getConnectionInfo();
		if (wifi_service ==null || wifiInfo == null ) return -1;
		List<ScanResult> scanResults=wifi_service.getScanResults();
		if (scanResults ==null )
			return -1;
		for(ScanResult result : scanResults){
			if(result.BSSID.equalsIgnoreCase(wifiInfo.getBSSID()))
				return result.frequency;
		}
		return -1;
	}
	
	private static int getConnectedWifiChannel(Context context) {
		if (!isWifiConnected(context)) return -1;
		return freq2Channel(getCurWifiFreq(context), 20);
	}
	
	private static boolean isWifiConnected(Context context) {
		WifiManager wm = null;
		wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if (wm == null)
			return false;
		if (!wm.isWifiEnabled())
			return false;

		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifi == State.CONNECTED) {
			return true;
		}
		return false;
	}
	
	public static int getWifiEnvScore(Context mContext) {
		int wifichannel = getConnectedWifiChannel(mContext);
		int num_samechannel = getStaNumWithSameChannel(mContext, wifichannel);
		int num_neighborchannel = getStaNumWithNeighborChannel(mContext, wifichannel);
		int score = getWifiEnvScore(getWifiRSssi(mContext), getWifiLinkSpeed(mContext), num_neighborchannel, num_samechannel, 0);
		return score;
	}
	/******************END WiFi相关 **********************/

	/**
	 * 获取当前屏幕朝向
	 * @param mContext
	 * @return
	 */
	public static int getScreenDirection(Context mContext){
		return mContext.getResources().getConfiguration().orientation;
	}

	/**
	 * 初始化已经声明的方法
	 */
//	private static void initDeclaredMethods() {
//        try {
//            Class<?> cStub = Class.forName("android.os.IMultiWinService$Stub");
//            Method asInterface = cStub.getMethod("asInterface", IBinder.class);
//            sMultiWinService = asInterface.invoke(null, ServiceManager.getService("multiwin"));
//            Class<?> clazz = sMultiWinService.getClass();
//            sGetMWMaintainedMethod = clazz.getDeclaredMethod(
//                "getMWMaintained", (Class[]) null);
//        } catch (ClassNotFoundException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        } catch (NoSuchMethodException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        } catch (IllegalAccessException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        } catch (IllegalArgumentException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        } catch (InvocationTargetException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        } catch (NullPointerException e) {
//            Log.e(TAG, "initDeclaredMethods failed:" + e.toString());
//        }
//    }

    @SuppressWarnings("unchecked")
    public static boolean getMWMaintained() {
//    	if(sGetMWMaintainedMethod == null || sMultiWinService == null){
//    		initDeclaredMethods();
//    	}
//        Method method = sGetMWMaintainedMethod;
//        if (method != null) {
//            try {
//                return (Boolean)method.invoke(sMultiWinService, (Object[]) null);
//            } catch (Exception e) {
//                Log.e(TAG, "call method " + method.getName() + " failed !!!", e);
//            }
//        }
        return true;
    }



	/*******************************************************************/
	/** start 供本类调用*/
	/*******************************************************************/

	/**
	 * 使应用获取root权限
	 * @param pkgCodePath
	 * @return
	 */
	private static boolean isRootApplication = false;
	private static boolean upgradeRootPermission(String pkgCodePath) {
		Process process = null;
		DataOutputStream os = null;
		try {
			String cmd = "chmod 777 " + pkgCodePath;
			process = Runtime.getRuntime().exec("su"); // 切换到root帐号
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(cmd + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
		return true;
	}

	/**
	 * 检测当前已经安装的支持的视频播放应用
	 */
	public static void showInstalledVideoApps(Context context){
		String[] illegalPackageNames = new String[] {
				"com.qihoo360.mobilesafe", "com.qihoo.antivirus",
				"com.qihoo.security", "com.qihoo.appstore", "com.cleanmaster.security", "com.cleanmaster.mguard",
				"com.cleanmaster.mguard_cn",
				"com.wandoujia.phoenix2", // 豌豆荚
				"com.tencent.android.qqdownloader", // QQ应用宝
				"com.ksmobile.launcher", "com.cm.launcher" };

		String[] trafficVideoApps = new String[]{
				"com.hunantv.imgo.activity"
				,"com.storm.smart"
				,"com.youku.phone"
				,"com.tudou.android"
				,"com.tencent.qqlive"
				,"com.letv.android.client"
				,"com.sohu.sohuvideo"
				,"com.qiyi.video"
				,"com.qiyi.video.pad"
				,"com.android.browser"
				,"com.UCMobile"
				,"com.tencent.mtt"
				,"com.apusapps.browser"
		};
		HashMap<String,String> appNameMap = new HashMap<>();
		appNameMap.put(trafficVideoApps[0],"芒果TV");
		appNameMap.put(trafficVideoApps[1],"暴风影音");
		appNameMap.put(trafficVideoApps[2],"优酷");
		appNameMap.put(trafficVideoApps[3],"土豆");
		appNameMap.put(trafficVideoApps[4],"腾讯视频");
		appNameMap.put(trafficVideoApps[5],"乐视TV");
		appNameMap.put(trafficVideoApps[6],"搜狐视频");
		appNameMap.put(trafficVideoApps[7],"爱奇艺");
		appNameMap.put(trafficVideoApps[12],"Apus Browser");

		ArrayList<String> installedVideoAppList = new ArrayList<>();
		for (String packageName : trafficVideoApps) {
			if (PackageUtil.isInstalled(context, packageName)) {
				// 构造一个崩溃
//				throw new OutOfMemoryError("Illegal package[" + packageName + "] detected!");
				if(appNameMap.get(packageName) != null){
					installedVideoAppList.add(appNameMap.get(packageName));
				}else{
					installedVideoAppList.add(packageName);
				}
			}
		}
		//获取已经安装的应用
		Toast.makeText(context,"本机已安装支持的视频应用 ：" + installedVideoAppList.toString(),Toast.LENGTH_LONG).show();
	}


}
