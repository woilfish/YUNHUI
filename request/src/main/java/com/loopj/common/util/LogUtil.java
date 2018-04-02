package com.loopj.common.util;

/**
 * Created by pengmin on 17/6/2.
 */

public class LogUtil {

    /** TAG */
    private static final String TAG = "LKLBUSINESSSDK ---------->>>";
    /** debug开关 */
    private static final boolean DEBUG = true;
    /**
     * 一个参数的打印方法
     *
     * @param msg 需要打印的信息
     */
    public static void print(String msg) {
        print(null, msg, null);
    }

    /**
     * 打印异常
     * @param throwable 异常信息
     */
    public static void print(Throwable throwable){
        print(null,null,throwable);
    }

    /**
     * 自定义TAG打印
     * @param tag  自定义tag,为null则默认为 TAG
     * @param msg  需要打印的信息
     */
    public static void print(String tag,String msg){
        print(tag,msg,null);
    }

    /**
     * 自定义TAG打印
     * @param tag 自定义tag,为null则默认为 TAG
     * @param throwable 异常信息
     */
    public static void print(String tag,Throwable throwable){
        print(tag,null,throwable);
    }


    /**
     *  String.format(pattern,args);
     * @param pattern  pattern
     * @param args     args
     */
    public static void print(String pattern,Object...args){
        print(null,String.format(pattern,args),null);
    }

    /**
     * 三个参数的打印方法
     *
     * @param tag       自定义tag,为null则默认为 TAG
     * @param msg       打印信息
     * @param throwable 异常信息
     */
    public static void print(String tag, String msg, Throwable throwable) {
        if (!DEBUG)
            return;
        //设置TAG
        String t = TAG;
        if (tag != null){
            t = tag;
        }
        //打印输出
        if (msg != null){
            android.util.Log.d(t, msg);
        }
        if (throwable != null){
            android.util.Log.e(t, throwable.toString());
        }
    }

    /**
     * 一个参数的打印方法
     *
     * @param msg 需要打印的信息
     */
    public static void printE(String msg) {
        printE(null, msg, null);
    }

    /**
     * 自定义TAG打印
     *
     * @param tag 自定义tag,为null则默认为 TAG
     * @param msg 需要打印的信息
     */
    public static void printE(String tag, String msg) {
        printE(tag, msg, null);
    }

    /**
     * 三个参数的打印方法
     *
     * @param tag       自定义tag,为null则默认为 TAG
     * @param msg       打印信息
     * @param throwable 异常信息
     */
    public static void printE(String tag, String msg, Throwable throwable) {
        if (!DEBUG)
            return;
        //设置TAG
        String t = TAG;
        if (tag != null) {
            t = tag;
        }
        //打印输出
        if (msg != null) {
            android.util.Log.e(t, msg);
        }
        if (throwable != null) {
            android.util.Log.e(t, throwable.toString());
        }
    }

    /**
     *
     * @param tag
     * @param message
     */
    public static void logWithDivider(String tag,String message){

        LogUtil.print("\n"+tag +"=!=",message+"\n");
    }


}
