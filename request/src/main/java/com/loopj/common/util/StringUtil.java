package com.loopj.common.util;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pengmin on 17/6/2.
 */

public class StringUtil {

    /**
     * 一分钟的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MINUTE = 60 * 1000;

    /**
     * 一小时的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_HOUR = 60 * ONE_MINUTE;

    /**
     * 一天的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_DAY = 24 * ONE_HOUR;

    /**
     * 一月的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_MONTH = 30 * ONE_DAY;

    /**
     * 一年的毫秒值，用于判断上次的更新时间
     */
    public static final long ONE_YEAR = 12 * ONE_MONTH;

    private static final char[] HEX_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private static final String HEX_STRING = "0123456789ABCDEF";

    /**
     * isNotEmpty
     *
     * @param string
     * @return
     */
    public static boolean isNotEmpty(String string) {
        return string != null && !"".equals(string.trim()) && !"null".equals(string.trim());
    }

    /**
     * 判断字符串是否为空
     *
     * @param string
     * @return 空返回true, 非空返回false
     */
    public static boolean isEmpty(String string) {
        return !isNotEmpty(string);
    }

    /**
     * trim
     *
     * @param string
     * @return
     */
    public static String trim(String string) {
        if (string == null || string.equals("null")) {
            return "";
        } else {
            return string.trim();
        }
    }

    /**
     * 16进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        if (hex == null) {
            return null;
        }
        hex = hex.toUpperCase();
        int    len    = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar  = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) HEX_STRING.indexOf(c);
        return b;
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            //取出字节的高四位 作为索引得到相应的十六进制标识符 注意无符号右移
            builder.append(HEX_CHAR[(b & 0xf0) >>> 4]);
            //取出字节的低四位 作为索引得到相应的十六进制标识符
            builder.append(HEX_CHAR[(b & 0x0f)]);
        }
        return builder.toString();
    }

    /**
     * @param o
     * @return
     */
    public static String toString(Object o) {
        return o == null ? "" : o.toString();
    }

    /**
     * formatString
     * 将string中的所有replace去掉
     *
     * @param string
     * @param replace
     * @return
     */
    public static String formatString(String string, String replace) {
        if (string == null) {
            return "";
        }
        String newString = string.replaceAll(replace, "");
        return newString;
    }

    /**
     * 字符串模糊处理，*号代替
     *
     * @param string 原字符
     * @param start  模糊开始下标
     * @param end    模糊结束下标
     * @return 138****5678
     */
    public static String formatStringVague(String string, int start, int end) {
        if (StringUtil.isEmpty(string)) {
            return string;
        }

        if (start < 0 || end > (string.length() - 1)) {
            return string;
        }

        StringBuilder sb = new StringBuilder();
        char[]        c  = string.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (i >= start && i <= end) {
                sb.append("*");
            } else {
                sb.append(c[i]);
            }
        }
        return sb.toString();
    }

    /**
     * formatString
     * 去除字符中间的 "空格/-/," 等间隔符
     *
     * @param string 要格式化的字符
     * @return 格式化后的字符
     */
    public static String formatString(String string) {
        if (string == null) {
            return "";
        }
        String newString = string.replaceAll(" ", "")
                .replaceAll("-", "")
                .replaceAll(",", "");
        return newString;
    }

    /**
     * 格式话文件内容，去空格换行符
     */
    public static String formatFileContent(String string) {
        if (string == null) {
            return "";
        }
        String newString = string.replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("\r", "");
        return newString;
    }

    /**
     * suffixSpaceToString
     * 字符串后端加全角空格，对齐成指定数量个汉字
     *
     * @param string
     * @param len
     * @return
     */
    public static String suffixSpaceToString(String string, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        int           length        = string.length();
        int           appendCount   = length < len ? len - length : 0;

        for (int i = 0; i < appendCount; i++) {
            stringBuilder.append("　");
        }
        return string + stringBuilder.toString();
    }

    /**
     * addSpaceToStringFront
     * 字符串前端加全角空格，对齐成指定数量个汉字
     *
     * @param string
     * @param len    指定对齐位数
     * @return
     */
    public static String addSpaceToStringFront(String string, int len) {
        StringBuilder stringBuilder = new StringBuilder();
        int           length        = string.length();
        int           appendCount   = length < len ? len - length : 0;

        for (int i = 0; i < appendCount; i++) {
            stringBuilder.append("　");
        }
        return stringBuilder.toString() + string;
    }

    /**
     * formatAmount
     * 金额格式化
     *
     * @param s 金额
     * @return 格式后的金额(###,###.##)
     */
    public static String formatAmount(String s) {
        return formatAmount(s, false);
    }

    /**
     * formatAmount
     * 金额格式化
     *
     * @param s          金额
     * @param isInitNull 是否初始化为空字符
     * @return 格式后的金额(###,###.##)
     */
    public static String formatAmount(String s, boolean isInitNull) {
        String result = "";
        if (!isNotEmpty(s)) {
            return "";
        }
        String temp = s;
        s = formatString(s);// 去除string可能的分隔符
        double num = 0.0;
        try {
            num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            //TODO throw something..
        }
        if (num == 0) {
            if (isInitNull) {
                return "";
            } else {
                return "0.00";
            }
        }
        if (num < 1) {//小于1情况特殊处理
            if (s.length() == 4) {//0.05
                return temp;
            } else if (s.length() == 3) {//0.5
                return temp + "0";
            }
        }
        NumberFormat forMater = new DecimalFormat("#,###.00");
        result = forMater.format(num);

        if (result.startsWith(".")) {
            result = "0" + result;
        }
        return result;
    }

    /**
     * 判断某字符串是否是网址
     *
     * @param urlString
     * @return
     */
    public static boolean isURL(String urlString) {
        String  regex   = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt    = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        boolean isMatch = matcher.matches();
        return isMatch;
    }

    /**
     * 将一个字符串转换成整型值，如果 number 不能转换成整型则返回 defaultValue
     *
     * @param number 一个数字字符串
     * @return 转换后的整型值。
     */
    public static int StringToInt(String number, int defaultValue) {
        if (StringUtil.isEmpty(number)) {
            return defaultValue;
        } else {
            try {
                return Integer.valueOf(number);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    /**
     * 将一个字符串转换成浮点型值，如果 number 不能转换成整型则返回 defaultValue
     *
     * @param number 一个数字字符串
     * @return 转换后的整型值。
     */
    public static Float StringTofloat(String number, float defaultValue) {
        if (StringUtil.isEmpty(number)) {
            return defaultValue;
        } else {
            try {
                return Float.valueOf(number);
            } catch (Exception e) {
                return defaultValue;
            }
        }
    }

    /**
     * 将字符串中的所有的数字格式化成指定颜色。
     *
     * @param text  字符串
     * @param color 颜色
     * @return 返回格式化后的字符串对象 SpannableStringBuilder。
     */
    public static SpannableStringBuilder formatNumberColor(String text, int color) {
        return formatTextColor(text, "[0-9]+\\.*[0-9]*", color);
    }

    /**
     * 将字符串中与正则表达式匹配的文字设置成指定颜色。
     *
     * @param text          字符串
     * @param patternString 用于筛选的正则表达式。
     * @param color         颜色
     * @return 返回格式化后的字符串对象 SpannableStringBuilder。
     */
    public static SpannableStringBuilder formatTextColor(String text, String patternString, int color) {
        if (text == null) {
            return new SpannableStringBuilder("");
        }

        SpannableStringBuilder style = new SpannableStringBuilder(text);

        if (patternString == null) {
            return style;
        }

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            style.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return style;
    }


    /**
     * 。是否是纯数字字符串
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum   = pattern.matcher(str);
        return isNum.matches();
    }

    /**
     * 是否为纯字母
     *
     * @param str
     * @return
     */
    public static boolean isLetters(String str) {
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 判断字符串中是否有连续相同字符长度超过4位
     *
     * @param str
     * @return
     */
    public static boolean isSeriesSame(String str) {
        return isSeriesSame(str, 4);
    }

    /**
     * 判断字符串中是否有连续相同字符
     *
     * @param str             String
     * @param MAX_SAME_LENGTH 可以存在连续相同字符串的最大长度
     * @return boolean
     */
    public static boolean isSeriesSame(String str, int MAX_SAME_LENGTH) {
        boolean same = false;
        String  regex;
        String  temp;
        int     MAX  = MAX_SAME_LENGTH + 1;
        for (int i = 0; i < str.length(); i++) {
            //如果校验剩余长度小于或等于允许的最大长度，那么 break
            if ((str.length() - i) < MAX) {
                break;
            }

            regex = getSeriesString(str.substring(i, i + 1), MAX);
            temp = str.substring(i, MAX + i);
            same = temp.equals(regex);

            //如果相同则跳出循环
            if (same) {
                break;
            }
        }
        return same;
    }

    private static String getSeriesString(String item, int len) {
        String temp = "";
        for (int i = 0; i < len; i++) {
            temp = temp.concat(item);
        }
        return temp;
    }

    //升序
    private static String upOrderStr;
    //降序
    private static String downOrderStr;

    static {
        for (int i = 33; i < 127; i++) {
            upOrderStr += Character.toChars(i)[0];
        }
        downOrderStr = new StringBuilder(upOrderStr).reverse().toString();
    }

    /**
     * 判断字符串是否有顺序
     *
     * @param str
     * @return
     */
    public static boolean isOrder(String str) {
        if (!str.matches("((\\d)|([a-z])|([A-Z]))+")) {
            return false;
        }
        return upOrderStr.contains(str) || downOrderStr.contains(str);
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 检测String是否全是中文
     *
     * @param name
     * @return
     */
    public static boolean checkNameChese(String name) {
        boolean res   = true;
        char[]  cTemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!isChinese(cTemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }

    /**
     * 返回长度为【strLength】的随机数，在前面补0
     */
    public static String getRandom(int strLength) {

        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    /**
     * bytes 转换为long数字字符
     * @param var0      待转换byte数组
     * @param isSigned  是否有符号（即有/无符号长整型）
     * @return
     */
    public static String bytesToLongStr(byte[] var0, boolean isSigned){
        long result = bytesToLong(var0,isSigned);
        if (result == -1) return "";

        return result + "";
    }

    /**
     *  bytes 转换为long数字
     * @param var0      待转换byte数组
     * @param isSigned  是否有符号（即有/无符号长整型）
     * @return 转换的Long整数
     */
    public static long bytesToLong(byte[] var0, boolean isSigned) {
        if(var0 == null) {

            return -1L;
        } else if(var0.length < 1) {

            return -1L;
        } else if(var0.length == 1) {
            return (long)(var0[0] & 255);
        } else {
            long var2 = 0L;
            int var4;
            if(isSigned) {
                var2 = (long)(var0[0] & 255);

                for(var4 = 1; var4 < var0.length; ++var4) {
                    var2 <<= 8;
                    var2 |= (long)(var0[var4] & 255);
                }
            } else {
                var2 = (long)var0[var0.length - 1];

                for(var4 = var0.length - 2; var4 >= 0; --var4) {
                    var2 <<= 8;
                    var2 |= (long)(var0[var4] & 255);
                }
            }

            return var2;
        }
    }

    /**
     * 转换byte 为 有符号长整型
     * @param var0  待转换byte数组
     * @return
     */
    public static long bytesToLong(byte[] var0) {
        try {
            return bytesToLong(var0, true);
        } catch (Exception var1) {
            return 0L;
        }
    }

    /**
     *   有/符号长整型 转换为 bytes数数组
     * @param var0      需要转换的Long整数
     * @param var2      转换后字节数组的长度
     * @param isSigned  是否为 有符号长整型
     * @return
     */
    public static byte[] longToBytes(long var0, int var2, boolean isSigned) {
        try {
            if(var2 < 1) {
                return null;
            } else {
                long var4 = var0;
                byte[] var6 = new byte[var2];
                int var7;
                if(isSigned) {
                    for(var7 = var6.length - 1; var7 > -1; --var7) {
                        var6[var7] = Long.valueOf(var4 & 255L).byteValue();
                        var4 >>= 8;
                    }
                } else {
                    for(var7 = 0; var7 < var6.length; ++var7) {
                        var6[var7] = Long.valueOf(var4 & 255L).byteValue();
                        var4 >>= 8;
                    }
                }

                return var6;
            }
        } catch (Exception var8) {
            return null;
        }
    }

    /**
     *  有符号长整型 转换为 bytes数数组
     * @param var0  需要转换的Long整数
     * @param var2  转换后字节数组的长度
     * @return
     */
    public static byte[] longToBytes(long var0, int var2) {
        try {
            return longToBytes(var0, var2, true);
        } catch (Exception var3) {
            return null;
        }
    }

    /**
     * 小端模式转换 ，支持1和4字节
     * @param bytes
     * @return
     */
    public  static int bytesToInt(byte[] bytes) {
        int addr=0;
        if(bytes.length==1){
            addr = bytes[0] & 0xFF;
        }else{
            addr = bytes[0] & 0xFF;
            addr |= ((bytes[1] << 8) & 0xFF00);
            addr |= ((bytes[2] << 16) & 0xFF0000);
            addr |= ((bytes[3] << 24) & 0xFF000000);
        }
        return addr;
    }
}
