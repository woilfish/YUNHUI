package com.pengmin.encryption;

/**
 * Created by pengmin on 2018/5/6.
 */

public class YHJniUtils {

    static {
        System.loadLibrary("YHLibName");
    }

    public native static String getString();
}
