package in.jvapps.system_alert_window.utils;

import static android.content.Context.ACTIVITY_SERVICE;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class Commons {


    public static boolean isForceAndroidBubble(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
            if (activityManager != null) {
                PackageManager pm = context.getPackageManager();
                return !pm.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE) || pm.hasSystemFeature(PackageManager.FEATURE_RAM_LOW) || activityManager.isLowRamDevice();
            } else {
                LogUtils.getInstance().i("SAW:Commons", "Marking force android bubble as false");
            }
        }
        return false;
    }
}
