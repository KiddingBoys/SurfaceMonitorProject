package com.whut.surfacemonitorproject_wjj.utils;

/**
 * Created by wujiajun on 2017/4/27.
 */

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class PackageUtil {
    private static Map<String, Boolean> a = new HashMap(100);
    private static Map<String, Boolean> b = new HashMap(100);
    private static Map<String, Integer> c = new HashMap(100);

    public PackageUtil() {
    }

    public static final void requestRefreshCache(String var0) {
        a.remove(var0);
        b.remove(var0);
        c.remove(var0);
    }

    public static boolean isInstalledAndEnabled(Context var0, String var1) {
        if(!a.containsKey(var1)) {
            try {
                Context var6 = var0.getApplicationContext();
                PackageManager var7 = var6.getPackageManager();
                ApplicationInfo var4 = var7.getApplicationInfo(var1, 0);
                a.put(var1, Boolean.TRUE);
                b.put(var1, Boolean.valueOf(var4.enabled));
                Log.d("PackageInfoUtil", var1 + " installed=true, enabled=" + var4.enabled);
                return var4.enabled;
            } catch (Exception var5) {
                a.put(var1, Boolean.FALSE);
                b.put(var1, Boolean.FALSE);
                Log.d("PackageInfoUtil", var1 + " not installed");
                return false;
            }
        } else {
            Boolean var2 = (Boolean)a.get(var1);
            Boolean var3 = (Boolean)b.get(var1);
            Log.d("PackageInfoUtil", var1 + " installed=" + var2 + ", enabled=" + var3);
            return var2 != null && var2.booleanValue() && var3 != null && var3.booleanValue();
        }
    }

    public static boolean isInstalled(Context var0, String var1) {
        isInstalledAndEnabled(var0, var1);
        Boolean var2 = (Boolean)a.get(var1);
        Log.v("PackageInfoUtil", var1 + " installed=" + var2);
        return var2 == null?false:var2.booleanValue();
    }

    public static int getSelfVersionCode(Context var0) {
        return getVersionCode(var0, var0.getPackageName());
    }

    public static int getVersionCode(Context var0, String var1) {
        if(!c.containsKey(var1)) {
            try {
                int var4 = var0.getPackageManager().getPackageInfo(var1, 0).versionCode;
                c.put(var1, Integer.valueOf(var4));
                return var4;
            } catch (Exception var3) {
                Log.e("PackageInfoUtil", "", var3);
                return 0;
            }
        } else {
            Integer var2 = (Integer)c.get(var1);
            Log.v("PackageInfoUtil", "pkg=" + var1 + ", vc=" + var2);
            return var2 == null?0:var2.intValue();
        }
    }

    public static long getPackageFirstInstallTime(Context var0, String var1) {
        return getPackageFirstInstallTime(var0.getPackageManager(), var1);
    }

    public static long getPackageFirstInstallTime(PackageManager var0, String var1) {
        try {
            return var0.getPackageInfo(var1, 0).firstInstallTime;
        } catch (Exception var3) {
            Log.e("PackageInfoUtil", "", var3);
            return -1L;
        }
    }

    public static long getPackageUpdateTime(Context var0, String var1) {
        try {
            return var0.getPackageManager().getPackageInfo(var1, 0).lastUpdateTime;
        } catch (Exception var3) {
            Log.e("PackageInfoUtil", "", var3);
            return -1L;
        }
    }

    public static final boolean installAPK(Context var0, String var1) {
        Intent var2 = new Intent("android.intent.action.VIEW");
        var2.setDataAndType(Uri.parse("file://" + var1), "application/vnd.android.package-archive");
        var2.setFlags(268435456);

        try {
            var0.getApplicationContext().startActivity(var2);
            return true;
        } catch (Exception var4) {
            Log.e("PackageInfoUtil", "err", var4);
            return false;
        }
    }

    public static final boolean installAPK(Context var0, File var1) {
        return installAPK(var0, var1.getPath());
    }

    public static void showAppDetailsInfo(Context var0, String var1) {
        try {
            Intent var2 = new Intent();
            var2.addFlags(268435456);
            var2.addFlags(8388608);
            var2.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            Uri var3 = Uri.fromParts("package", var1, (String)null);
            var2.setData(var3);
            var0.startActivity(var2);
        } catch (Exception var4) {
            ;
        }

    }

    public static byte[] getPackageSignature(Context var0, String var1) {
        try {
            PackageInfo var2 = var0.getPackageManager().getPackageInfo(var1, 64);
            if(var2.signatures != null) {
                return getSigHash(var2.signatures);
            }
        } catch (Exception var3) {
            Log.e("PackageInfoUtil", "", var3);
        }

        return null;
    }

    public static byte[] getSigHash(Signature[] var0) {
        try {
            MessageDigest var1 = MessageDigest.getInstance("MD5");
            Signature[] var2 = var0;
            int var3 = var0.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Signature var5 = var2[var4];
                var1.update(var5.toByteArray());
            }

            return var1.digest();
        } catch (Exception var6) {
            Log.e("PackageInfoUtil", "", var6);
            return null;
        }
    }

    public static boolean isPackageSystem(Context var0, String var1) {
        PackageManager var2 = var0.getPackageManager();

        try {
            ApplicationInfo var3 = var2.getApplicationInfo(var1, 0);
            return (var3.flags & 1) == 1;
        } catch (Exception var4) {
            return true;
        }
    }

    public static boolean isInstallOnSdCard(Context var0, String var1) {
        boolean var2 = false;

        try {
            PackageManager var3 = var0.getPackageManager();
            ApplicationInfo var4 = var3.getApplicationInfo(var1, 0);
            if((var4.flags & 262144) != 0) {
                var2 = true;
            }
        } catch (NameNotFoundException var5) {
            Log.e("PackageInfoUtil", "[err]", var5);
        }

        Log.d("PackageInfoUtil", "isInstallOnSdCard res=" + var2);
        return var2;
    }

    public static boolean isLauncherApp(Context var0, String var1) {
        boolean var2 = false;
        if(!TextUtils.isEmpty(var1)) {
            PackageManager var3 = var0.getPackageManager();
            Intent var4 = new Intent("android.intent.action.MAIN");
            var4.addCategory("android.intent.category.HOME");
            var4.setPackage(var1);
            ResolveInfo var5 = null;

            try {
                var5 = var3.resolveActivity(var4, 0);
            } catch (Exception var7) {
                Log.e("PackageInfoUtil", "ERROR", var7);
            }

            var2 = var5 != null;
        }

        Log.d("PackageInfoUtil", "isLauncherApp res=" + var2);
        return var2;
    }

    public static String getTopPackageName(Context var0) {
        if(var0 == null) {
            return null;
        } else {
            ActivityManager var1 = (ActivityManager)ContextHelper.getSystemService(var0, "activity");
            Context var2 = var0.getApplicationContext();
            PackageManager var3 = var2.getPackageManager();
            if(var3.checkPermission("android.permission.GET_TASKS", var2.getPackageName()) == 0) {
                List var4 = null;

                try {
                    var4 = var1.getRunningTasks(1);
                } catch (Exception var7) {
                    Log.e("PackageInfoUtil", "[catched]", var7);
                    return null;
                }

                if(var4 != null && var4.size() > 0) {
                    ComponentName var5 = ((RunningTaskInfo)var4.get(0)).topActivity;
                    String var6 = var5.getPackageName();
                    Log.d("PackageInfoUtil", "top pkg=" + var6);
                    return var6;
                }
            }

            return null;
        }
    }

    public static String getTopAppPackageName(Context var0) {
        if(var0 == null) {
            return null;
        } else {
            ActivityManager var1 = (ActivityManager)ContextHelper.getSystemService(var0, "activity");
            Context var2 = var0.getApplicationContext();
            PackageManager var3 = var2.getPackageManager();
            List var4;
            String var6;
            if(VERSION.SDK_INT < 21 && var3.checkPermission("android.permission.GET_TASKS", var2.getPackageName()) == 0) {
                var4 = null;

                try {
                    var4 = var1.getRunningTasks(1);
                } catch (Exception var11) {
                    Log.e("PackageInfoUtil", "[catched]", var11);
                    return null;
                }

                if(var4 != null && var4.size() > 0) {
                    ComponentName var12 = ((RunningTaskInfo)var4.get(0)).topActivity;
                    var6 = var12.getPackageName();
                    Log.d("PackageInfoUtil", "top pkg=" + var6);
                    return var6;
                }
            } else {
                var4 = var1.getRunningAppProcesses();
                if(var4 != null && !var4.isEmpty()) {
                    RunningAppProcessInfo var5 = (RunningAppProcessInfo)var4.get(0);
                    var6 = var5.processName;
                    Log.d("PackageInfoUtil", "top process=" + var6 + ", size=" + var5.pkgList.length);
                    if(var5.pkgList != null && var5.pkgList.length > 0) {
                        String[] var7 = var5.pkgList;
                        int var8 = var7.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                            String var10 = var7[var9];
                            Log.d("PackageInfoUtil", "top child pkgs=" + var10);
                        }

                        return var5.pkgList[0];
                    }
                }
            }

            return null;
        }
    }

    public static List<String> getRecentAppPackageName(Context var0, int var1) {
        int var2 = var1 <= 0?1:var1;
        ArrayList var3 = new ArrayList(var2);
        LinkedHashSet var4 = new LinkedHashSet(var2);
        if(var0 != null && var1 > 0) {
            Context var5 = var0.getApplicationContext();
            ActivityManager var6 = (ActivityManager)var5.getSystemService("activity");
            PackageManager var7 = var5.getPackageManager();
            if(var6 != null && var7 != null) {
                List var8;
                int var9;
                int var10;
                if(VERSION.SDK_INT < 21 && var7.checkPermission("android.permission.GET_TASKS", var5.getPackageName()) == 0) {
                    var8 = null;

                    try {
                        var8 = var6.getRecentTasks(var1, 2);
                    } catch (Exception var16) {
                        Log.e("PackageInfoUtil", "[catched]", var16);
                        var8 = null;
                    }

                    if(var8 != null) {
                        var9 = var8.size();

                        for(var10 = 0; var10 < var9; ++var10) {
                            RecentTaskInfo var17 = (RecentTaskInfo)var8.get(var10);
                            Intent var12 = new Intent(var17.baseIntent);
                            if(var17.origActivity != null) {
                                var12.setComponent(var17.origActivity);
                            }

                            var12.setFlags(var12.getFlags() & -2097153 | 268435456);
                            ResolveInfo var13 = var7.resolveActivity(var12, 0);
                            if(var13 != null) {
                                ActivityInfo var14 = var13.activityInfo;
                                String var15 = var14.loadLabel(var7).toString();
                                if(var15 != null && var15.length() > 0) {
                                    var4.add(var13.activityInfo.packageName);
                                    Log.d("PackageInfoUtil", "recent pkg=" + var13.activityInfo.packageName);
                                } else {
                                    Log.d("PackageInfoUtil", "recent title is empty.so skipp =" + var13.activityInfo.packageName);
                                }
                            }
                        }
                    }
                } else {
                    var8 = var6.getRunningAppProcesses();
                    if(var8 != null && !var8.isEmpty()) {
                        var9 = var8.size();
                        var1 = var9 > var1?var1:var9;

                        for(var10 = 0; var10 < var1; ++var10) {
                            RunningAppProcessInfo var11 = (RunningAppProcessInfo)var8.get(var10);
                            Log.d("PackageInfoUtil", "recent process=" + var11.processName + ", size=" + var11.pkgList.length);
                            if(var11.pkgList != null && var11.pkgList.length > 0) {
                                Log.d("PackageInfoUtil", " packageName=" + var11.pkgList);
                                var4.add(var11.pkgList[0]);
                            }
                        }
                    }
                }

                Log.d("PackageInfoUtil", "packageSet=" + var4);
                var3.addAll(var4);
                Log.d("PackageInfoUtil", "resPackageList=" + var3);
                return var3;
            } else {
                return var3;
            }
        } else {
            return var3;
        }
    }

    public static int getPackageFlags(Context var0, String var1) {
        PackageManager var2 = var0.getPackageManager();

        try {
            ApplicationInfo var3 = var2.getApplicationInfo(var1, 0);
            return var3.flags;
        } catch (Exception var4) {
            Log.e("PackageInfoUtil", "", var4);
            return 0;
        }
    }

    public static final List<String> getRunningAppPackageName(Context var0, int var1) {
        if(var0 == null) {
            return null;
        } else {
            ArrayList var2 = new ArrayList();
            ActivityManager var3 = (ActivityManager)var0.getApplicationContext().getSystemService("activity");
            Context var4 = var0.getApplicationContext();
            PackageManager var5 = var4.getPackageManager();
            List var6;
            int var7;
            String var9;
            if(VERSION.SDK_INT < 21 && var5.checkPermission("android.permission.GET_TASKS", var4.getPackageName()) == 0) {
                var6 = null;

                try {
                    var6 = var3.getRunningTasks(var1);
                } catch (Exception var10) {
                    Log.e("PackageInfoUtil", "[catched]", var10);
                    return null;
                }

                if(var6 != null && var6.size() > 0) {
                    for(var7 = 0; var7 < var6.size(); ++var7) {
                        ComponentName var11 = ((RunningTaskInfo)var6.get(var7)).topActivity;
                        if(var11 != null) {
                            var9 = var11.getPackageName();
                            Log.d("PackageInfoUtil", "top pkg=" + var9);
                            var2.add(var9);
                        }
                    }

                    return var2;
                }
            } else {
                var6 = var3.getRunningAppProcesses();
                if(var6 != null && !var6.isEmpty()) {
                    var1 = var6.size() > var1?var1:var6.size();

                    for(var7 = 0; var7 < var1; ++var7) {
                        RunningAppProcessInfo var8 = (RunningAppProcessInfo)var6.get(var7);
                        var9 = var8.processName;
                        Log.d("PackageInfoUtil", "top process=" + var9 + ", size=" + var8.pkgList.length);
                        if(var8.pkgList != null && var8.pkgList.length > 0) {
                            Log.d("PackageInfoUtil", " packageName=" + var8.pkgList[0]);
                            var2.add(var8.pkgList[0]);
                        }
                    }

                    return var2;
                }
            }

            return null;
        }
    }
}
