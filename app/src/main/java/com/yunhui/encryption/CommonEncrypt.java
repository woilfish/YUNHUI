package com.yunhui.encryption;

import android.text.TextUtils;

import com.pengmin.encryption.YHJniUtils;

/**
 * Created by pengmin on 2018/5/5.
 */

public class CommonEncrypt {

    /**
     * 登录密码加密
     *
     * @param password  密码原文
     * @return 加密后的密文，如果加密失败返回空串。
     */
    public static String loginEncrypt(String password){
        if (TextUtils.isEmpty(password)){
            return "";
        }
        String publicKey = YHJniUtils.getString();
        RSAEncrypt rsaEncrypt = new RSAEncrypt(publicKey);
        return rsaEncrypt.encrypt(password.getBytes()).toUpperCase();
    }
}
