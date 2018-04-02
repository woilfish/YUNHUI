package com.yunhui.config;

import android.content.Context;
import android.text.TextUtils;

import com.yunhui.YhApplication;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by pengmin on 2018/3/28.
 * 所有配置都在这里控制
 */

public class Config {

    private static  final Context context = YhApplication.getInstance();

    private static Properties properties;
    private static String urlFormProperty;
    private static String isDebug;

    public static boolean isDebug() {
        if (TextUtils.isEmpty(isDebug)){
            isDebug = properties.getProperty("isDebug", "true");
        }
        return "true".equals(isDebug);
    }

    static {
        properties = new Properties();
        try{
            properties.load(Config.class.getResourceAsStream("/assets/config/config.properties"));
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    /**
     *  获取请求URL
     */

    public static String getBaseRequestUrl(){
        return getBaseUrlFromProperty();
    }

    /**
     * 获取Property文件中设置的Url
     */
    public static String getBaseUrlFromProperty() {
        if (TextUtils.isEmpty(urlFormProperty)){
            urlFormProperty = getUrlParameter("url",true);
        }
        return urlFormProperty;
    }

    /**
     * 读取配置文件中的 URL 参数，如果URL不存参数存在则返回空串。
     * 如果URL 不以 "/" 结尾，则会自动补上。
     * @param key  参数 Key，如果是 Debug 模式则会在key 后面自动添加 "_debug"。
     * @param appendPathSeparate 是否在路么结尾追加分隔符
     * @return url 参数
     */
    private static String getUrlParameter(String key,boolean appendPathSeparate){
        String url = getParameter(key);
        if (!url.endsWith("/") && appendPathSeparate){
            url = url.concat("/");
        }

        return url;
    }

    /**
     * 获取配置参数，如果参数不存在则返回空串。
     * @param key 参数
     * @return 参数值
     */
    private static String getParameter(String key){
        return properties.getProperty(key,"");
    }

}
