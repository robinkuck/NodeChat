package de.robinkuck.nodechat.android.utils;

import android.app.*;
import android.content.Context;
import android.os.Handler;
import android.provider.Settings;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import de.robinkuck.nodechat.android.App;
import de.robinkuck.nodechat.android.managers.CustomActivityManager;

public class Utils {

    public static boolean isMyAppRunning() {
        return CustomActivityManager.getInstance().getCurrentActivity() != null;
    }

    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void waitUntil(final int millis, final Runnable runnable) {
        new Handler().postDelayed(runnable, millis);
    }

    public static String getDeviceID(final Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static boolean isIpReachable(String targetIp) {
        boolean result = false;
        try {
            InetAddress target = InetAddress.getByName(targetIp);
            result = target.isReachable(5000);
        } catch (UnknownHostException ex) {
            System.out.println(ex.toString());
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return result;
    }

    //DP to Pixel
    public static float dpToPixel(float dp) {
        final float density = App.getInstance().getResources().getDisplayMetrics().density;
        return dp * density + 0.5f;
    }

}
