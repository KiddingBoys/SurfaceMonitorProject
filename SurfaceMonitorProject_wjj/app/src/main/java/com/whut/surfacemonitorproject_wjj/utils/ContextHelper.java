package com.whut.surfacemonitorproject_wjj.utils;

/**
 * Created by wujiajun on 2017/4/27.
 */

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class ContextHelper {
    public ContextHelper() {
    }

    public static boolean bindService(Context var0, Class<?> var1, String var2, ServiceConnection var3, int var4) {
        return var0.getApplicationContext().bindService((new Intent(var0, var1)).setAction(var2), var3, var4);
    }

    public static void unbindService(Context var0, ServiceConnection var1) {
        try {
            var0.getApplicationContext().unbindService(var1);
        } catch (Exception var3) {
            ;
        }

    }

    public static void startService(Context var0, Intent var1) {
        var0.getApplicationContext().startService(var1);
    }

    public static void stopService(Context var0, Intent var1) {
        var0.getApplicationContext().stopService(var1);
    }

    public static Object getSystemService(Context var0, String var1) {
        return var0.getApplicationContext().getSystemService(var1);
    }

    public static boolean startActivity(Context var0, Intent var1) {
        try {
            var0.getApplicationContext().startActivity(var1);
            return true;
        } catch (Exception var3) {
            return false;
        }
    }

    public static boolean startActivity(Context var0, Intent var1, boolean var2) {
        if(var2) {
            var1.addFlags(268435456);
        }

        return startActivity(var0, var1);
    }
}
