package com.yunhui.encryption;



import com.yunhui.util.StringUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * mab 过滤器
 *
 * @author xyz
 */
public class Mac {


    /**
     * 银联ANSI9.19--------------------------------------------------------------start>
     */

    private static Map<String, Integer> macPositionMap = new HashMap<String, Integer>();

    static {
        macPositionMap.put("_TimeStamp", 1);
        macPositionMap.put("Random", 2);
        macPositionMap.put("PayerAcNo", 3);
        macPositionMap.put("PayeeAcNo", 4);
        macPositionMap.put("Inpan", 5);
        macPositionMap.put("BillId", 6);
        macPositionMap.put("Amount", 7);
        macPositionMap.put("Fee", 8);

    }



    /**
     * 拼接银联ANSI9.19 mac效验参数
     * 在域和域之间插入一个空格
     * 删去所有域的起始空格和结尾空格
     */
    public static String resolveMacData(ConcurrentHashMap<String, String> parameters) {
        if (parameters == null || parameters.size() == 0)
            return "";

        List<BasicNameValuePair> mabList = new ArrayList<BasicNameValuePair>();

        Set<String> keysets = parameters.keySet();
        Iterator iterator = keysets.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (isContainMacData(key)) {
                if ("Amount".equals(key)) {
                    //如果金额的值为null，则不继续进行，调试时公缴业务出现amount为null的情况
                    String value = parameters.get(key);
                    if (value == null || "".equals(value.trim())) {
                        continue;
                    }
                    value = StringUtil.formatString(value);
                    BasicNameValuePair pair = new BasicNameValuePair("Amount", value);
                    mabList.add(pair);
                    //改变金额
                    parameters.replace(key, value);
                } else {
                    mabList.add(new BasicNameValuePair(key,parameters.get(key)));
                }
            }
        }

        if (mabList.isEmpty()) {
            return "";
        }

        int size = mabList.size();
        if (size > 1) {//按文档顺序排序
            Collections.sort(mabList, new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair object1, NameValuePair object2) {
                    String o1 = object1.getName();
                    String o2 = object2.getName();
                    int p1 = macPositionMap.get(o1);
                    int p2 = macPositionMap.get(o2);
                    return p1 - p2;
                }
            });
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            NameValuePair pair = mabList.get(i);
            sb.append(pair.getValue().trim());
            if (i != size - 1) {
                sb.append(" ");
            }
        }
        return filterMacData(sb.toString());
    }

    /**
     * 拼接银联ANSI9.19 mac效验参数
     * 在域和域之间插入一个空格
     * 删去所有域的起始空格和结尾空格
     */
    public static String resolveMacData(List<BasicNameValuePair> parameters) {
        if (parameters == null || parameters.size() == 0)
            return "";

        List<BasicNameValuePair> mabList = new ArrayList<BasicNameValuePair>();

        //版本号>=17,公缴业务，需要用subbusid的值替换busid 做MAC计算，
        //此处在遍历参数列表的时候记录下subbusid的值
        String subbusid = "";

        int size = parameters.size();
        for (int i = 0; i < size; i++) {
            String key = parameters.get(i).getName();
            if (isContainMacData(key)) {
                if ("Amount".equals(key)) {
                    //如果金额的值为null，则不继续进行，调试时公缴业务出现amount为null的情况
                    String value = parameters.get(i).getValue();
                    if (value == null || "".equals(value.trim())) {
                        continue;
                    }
                    value = StringUtil.formatString(value);
                    BasicNameValuePair pair = new BasicNameValuePair("Amount", value);
                    mabList.add(pair);
                    //改变金额
                    parameters.set(i, pair);
                } else {
                    mabList.add(parameters.get(i));
                }
            } else if ("subbusid".equals(key)) {
                //保存subbusid的值
                subbusid = parameters.get(i).getValue();
            }
        }

        if (mabList.isEmpty()) {
            return "";
        }

        size = mabList.size();
        if (size > 1) {//按文档顺序排序
            Collections.sort(mabList, new Comparator<NameValuePair>() {
                @Override
                public int compare(NameValuePair object1, NameValuePair object2) {
                    String o1 = object1.getName();
                    String o2 = object2.getName();
                    int p1 = macPositionMap.get(o1);
                    int p2 = macPositionMap.get(o2);
                    return p1 - p2;
                }
            });
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            NameValuePair pair = mabList.get(i);
            sb.append(pair.getValue().trim());
            if (i != size - 1) {
                sb.append(" ");
            }
        }
        return filterMacData(sb.toString());
    }

    /**
     * 是否存在需要银联ANSI9.19 需要效验的参数
     *
     * @param name
     *
     * @return
     */
    public static boolean isContainMacData(String name) {

             if (
                name.equals("_TimeStamp") ||
                name.equals("Random") ||
                name.equals("PayerAcNo") ||
                name.equals("PayeeAcNo") ||
                name.equals("Inpan") ||
                name.equals("BillId") ||
                name.equals("Amount") ||
                name.equals("Fee")) {
            return true;
        }
        return false;
    }

    /**
     * 银联mac效验过滤多余字符
     * 除了字母(A-Z，a-z)，数字(0-9)，空格，逗号(，)和点号(.)以外的字符都删去
     */
    public static String filterMacData(String str) {
        if (str == null) return null;
        // 只允数字
        String regEx = "[^0-9A-Za-z,. *]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        //替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();
    }
    /**
     * <end--------------------------------------------------------------
     */
}
