package com.loopj.common.util;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * Created by pengmin on 2018/4/22.
 * 获取手机信息
 */

public class DeviceUtil {

    private static final String CMCC_ISP    = "46000";//中国移动
    private static final String CMCC2_ISP   = "46002";//中国移动
    private static final String CU_ISP      = "46001";//中国联通
    private static final String CT_ISP      = "46003";//中国电信

    /**
     * getIMEI
     * 获取IMEI
     *
     * @return  返回设备 IMEI，可能为空
     */
    @SuppressLint("MissingPermission")
    @Deprecated
    public static String getIMEI(Context context) {
        if (context == null){
            return "";
        }

        String IMEI = "";
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            IMEI = manager.getDeviceId();
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        if (StringUtil.isEmpty(IMEI)){
            return "";
        }

        return StringUtil.trim(IMEI);
    }

    /**
     * getIMSI
     *
     * @return 返回IMSI，可能为空
     */
    @SuppressLint("MissingPermission")
    public static String getIMSI(Context context) {
        if (context == null){
            return "";
        }

        String IMSI = "";
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            IMSI = manager.getSubscriberId();
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        if (StringUtil.isEmpty(IMSI)){
            return "";
        }

        return IMSI;
    }

    /**
     * 获取sim卡内手机号码
     *
     * @return 返回phoneNumber，可能为空
     */
    @SuppressLint("MissingPermission")
    public static String getPhoneNumber(Context context) {
        if (context == null){
            return "";
        }

        String phoneNumber = "";
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            phoneNumber = manager.getLine1Number();
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        if (StringUtil.isEmpty(phoneNumber)){
            return "";
        }

        return phoneNumber;
    }

    /**
     * getMac
     * 获取Mac地址
     *
     * @return 返回 MAC 地址
     */
    public static String getMac(Context context) {
        if (context == null){
            return "";
        }
        try {
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return info.getMacAddress();
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        return "";

    }

    /**
     * 获取手机网络运营商类型
     *
     * @return 返回当前运营商
     */
    public static String getPhoneISP(Context context) {
        if (context == null){
            return "";
        }

        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String teleCompany = "";
            String np = manager.getNetworkOperator();

            if (np != null) {
                if (np.equals(CMCC_ISP) || np.equals(CMCC2_ISP)) {
                    //中国移动
                    teleCompany = "y";
                } else if (np.startsWith(CU_ISP)) {
                    //中国联通
                    teleCompany = "l";
                } else if (np.startsWith(CT_ISP)) {
                    //中国电信
                    teleCompany = "d";
                }
            }
            return StringUtil.trim(teleCompany);
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        return "";

    }

    /**
     * 获取手机标识
     *
     * @return String
     */
    public static String getPhoneModel() {
        return StringUtil.trim(Build.MODEL);
    }


    /**
     * getPhoneManufacturer
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getPhoneManufacturer() {
        // eg:motorola
        String phoneManufacturer = android.os.Build.MANUFACTURER;
        return StringUtil.isEmpty(phoneManufacturer) ? "" : StringUtil.trim(phoneManufacturer);
    }

    /**
     * getPhoneType
     * 获取手机型号
     *
     * @return String
     */
    public static String getPhoneType() {
        // eg:ME860_HKTW
        String phoneType = android.os.Build.PRODUCT;
        return StringUtil.isEmpty(phoneType) ? "" : StringUtil.trim(phoneType);
    }

    /**
     * getPhoneOSVersion
     * 获取系统版本
     *
     * @return
     */
    public static String getPhoneOSVersion() {
        String osVersion = String.valueOf(Build.VERSION.SDK_INT);
        return StringUtil.isEmpty(osVersion) ? "" : StringUtil.trim(osVersion);
    }

    /**
     * 检查gps是否可用
     *
     * @return
     */
    public static boolean isGpsAvaiable(Context context) {
        if (context == null){
            return false;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开gps和关闭gps
     *
     * @param context    Context
     * @return           操作是否成功
     */
    public static boolean autoGps(Context context) {
        if (context == null){
            return false;
        }
        try {
            Intent GPSIntent = new Intent();// 代码自动打开gps
            GPSIntent.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
            GPSIntent.setData(Uri.parse("custom:3"));
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            return false;
        }
        return true;
    }

    /**
     * 网络是否可用
     *
     * @return true可用 </br> false不可用
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null){
            return false;
        }
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null && info.isAvailable()){
                int type = info.getType();
                if (type == ConnectivityManager.TYPE_WIFI || type == ConnectivityManager.TYPE_MOBILE) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据IMEI,MAC,ANDROID_ID,BUILD信息，生成一个设备唯一id
     * @return
     */
    public static String getDeviceId(Context context){
        if (context == null){
            return "";
        }

        String imei = getIMEI(context);
        if (StringUtil.isNotEmpty(imei)) {

            Pattern pattern = Pattern.compile("[0]*");
            // 如果获取到的imei不全部为0，即有imei返回，就返回imei
            // 如果获取imei全部为0，则使用app自有算法生成设备id
            if (!pattern.matcher(imei).matches()) {
                return imei;
            }

            if (imei.replaceAll(imei.charAt(0)+"","").length() > 0){
                return imei;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();

        //使用设备信息的字符串，拼接一个类似imei的15位串
        stringBuilder.append("35");
        stringBuilder.append(Build.BOARD.length()%10);
        stringBuilder.append(Build.BRAND.length()%10);
        stringBuilder.append(Build.CPU_ABI.length()%10);
        stringBuilder.append(Build.DEVICE.length()%10);
        stringBuilder.append(Build.DISPLAY.length()%10);
        stringBuilder.append(Build.HOST.length()%10);
        stringBuilder.append(Build.ID.length()%10);
        stringBuilder.append(Build.MANUFACTURER.length()%10);
        stringBuilder.append(Build.MODEL.length()%10);
        stringBuilder.append(Build.PRODUCT.length()%10);
        stringBuilder.append(Build.TAGS.length()%10);
        stringBuilder.append(Build.TYPE.length()%10);
        stringBuilder.append(Build.USER.length()%10);

        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        stringBuilder.append(androidId);

        stringBuilder.append(getMac(context));

        return Digest.md5(stringBuilder.toString());
    }

    /**
     * BASEBAND-VER
     * 基带版本
     * return String
     */

    public static String getBasebandVer(){
        String Version = "";
        try {
            Class cl = Class.forName("android.os.SystemProperties");
            Object invoker = cl.newInstance();
            Method m = cl.getMethod("get", new Class[] { String.class,String.class });
            Object result = m.invoke(invoker, new Object[]{"gsm.version.baseband", "no message"});
            Version = (String)result;
        } catch (Exception e) {
        }
        return Version;
    }
}
