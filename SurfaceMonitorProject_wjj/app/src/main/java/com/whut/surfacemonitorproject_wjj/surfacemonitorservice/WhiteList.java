package com.whut.surfacemonitorproject_wjj.surfacemonitorservice;

/**
 * 检测packageName是否属于白名单
 * @author Administrator
 *
 */
public class WhiteList {
	private static final String TAG = "WhiteList";
	
	public static boolean isWhiteListCheckPass(String packageName) {
//		List<Package> list = null;
//		Package packageName1 = null;
//		XmlPullParser parser = Xml.newPullParser();
//		try {
////			FileInputStream fis = new FileInputStream("system/etc/traffic_applist.xml");
//			FileInputStream fis = new FileInputStream("sdcard/traffic_applist.xml");
//			parser.setInput(fis, "utf-8");
//			int eventType = parser.getEventType();
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//				switch (eventType) {
//				case XmlPullParser.START_TAG:
//					if (parser.getName().equals("packageNames")) {
//						list = new ArrayList<Package>();
//					} else if (parser.getName().equals("name")) {
//						packageName1 = new Package();
//						String str = parser.nextText();
//						packageName1.setPackageName(str);
//						list.add(packageName1);
//						packageName1 = null;
//					}
//					break;
//				case XmlPullParser.END_TAG:
//					break;
//				}
//				eventType = parser.next();
//			}
//
//			Iterator<Package> it = list.iterator();
//			while(it.hasNext()) {
//				Package p = it.next();
//				if (p.getPackageName().equals(packageName)) {
//					Log.i(TAG, "find whitelist package ");
//					return true;
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
		return true;
	}
	
	private static class Package {
		String packageName;

		@SuppressWarnings("unused")
		public Package(String packageName) {
			super();
			this.packageName = packageName;
		}

		public Package() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getPackageName() {
			return packageName;
		}

		public void setPackageName(String packageName) {
			this.packageName = packageName;
		}

		@Override
		public String toString() {
			return "Package [packageName=" + packageName + "]";
		}
	}

}
