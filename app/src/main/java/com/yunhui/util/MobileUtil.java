package com.yunhui.util;

import java.util.regex.Pattern;

/**
 * Created by pengmin on 2018/4/18.
 * 手机号工具类
 */

public class MobileUtil {

    // 判断手机号码是否规则
    public static boolean isPhoneNumber(String input) {
        String regex = "(1[0-9][0-9]|15[0-9]|18[0-9])\\d{8}";
        Pattern p = Pattern.compile(regex);
        return p.matches(regex, input);
    }

}
