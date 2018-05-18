package com.yunhui.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

/**
 * Created by pengmin on 2018/5/18.
 */

public class AppUtil {

    /**
     * 获取目前软件版本code
     *
     * @param  context Context
     * @return String
     */
    public static String getAppVersionCode(Context context) {
        if (context == null){
            return "";
        }
        String versionCode;
        try {
            PackageManager pm = context.getPackageManager();

            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode        = pi.versionCode + "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return versionCode;
    }

    /**
     * 获取目前软件版本name
     *
     * @param  context Context
     * @return String
     */
    public static String getAppVersionName(Context context) {
        if (context == null){
            return "";
        }
        String versionName;
        try {
            PackageManager  pm = context.getPackageManager();

            PackageInfo     pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName        = pi.versionName + "";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return versionName;
    }

    /**
     * 从AndroidManifest中获取channel值
     *
     * @param  context Context
     * @return String
     */
    public static String getAppChannel(Context context){
        if (context == null){
            return "";
        }
        int channel;
        try {
            PackageManager  pm    = context.getPackageManager();
            ApplicationInfo info  = pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);

            channel               = info.metaData.getInt("Channel ID");
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        return channel+"";
    }

    /**
     * 从AndroidManifest中获取crashVersion
     *
     * @param  context Context
     * @return String
     */
    public static String getCrashVersion(Context context){
        if (context == null){
            return "";
        }
        String crashVersion;
        try {
            PackageManager  pm    = context.getPackageManager();
            ApplicationInfo info  = pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            crashVersion          = info.metaData.getString("CRASH");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return crashVersion;
    }

    /**
     * app是否为前台可见
     *
     * @param  context Context
     * @return boolean
     */
    public static boolean isAppRunningForeground(Context context){
        if (context == null){
            return false;
        }
        String packageName        = context.getPackageName();
        String topActivityPkgName = getTopActivityPkgName(context);

        return packageName!=null && topActivityPkgName!=null
                && topActivityPkgName.equals(packageName);
    }

    /**
     * 获取Device栈顶程序包名
     *
     * @param  context Context
     * @return String
     */
    public static String getTopActivityPkgName(Context context){
        if (context == null){
            return "";
        }

        String          topActivityPkgName  =   null;
        ActivityManager activityManager     =   (ActivityManager)(context.getSystemService(android.content.Context.ACTIVITY_SERVICE )) ;
        //android.app.ActivityManager.getRunningTasks(int maxNum)
        //int maxNum--->The maximum number of entries to return in the list
        //即最多取得的运行中的任务信息(RunningTaskInfo)数量
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);

        if(runningTaskInfos != null && runningTaskInfos.size() > 0){
            ComponentName f=runningTaskInfos.get(0).topActivity;
            topActivityPkgName = f.getPackageName();
        }
        //按下Home键盘后 topActivityClassName=com.android.launcher2.Launcher
        return topActivityPkgName;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名（例如：com.XXX.XXXX.XXXXService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceRunning(Context mContext, String serviceName) {
        boolean isRunning = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            LogUtil.print("is running service name " + mName);
            if (mName.equals(serviceName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
