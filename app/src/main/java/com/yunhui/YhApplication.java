package com.yunhui;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.loopj.common.util.LogUtil;
import com.loopj.common.util.StringUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.umeng.analytics.MobclickAgent;
import com.yunhui.bean.UserInfo;

/**
 * Created by pengmin on 2018/3/28.
 */

public class YhApplication extends Application {


    private static YhApplication mInstance;
    private String APPKEY = "5abb30fea40fa36fca0000a6";//友盟SDK
    /**
     * 渠道号
     */
    private String mChannelNo;

    private UserInfo userInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public static YhApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        mInstance = this;
        MultiDex.install(this);
        super.onCreate();

        try {
            mChannelNo = WalleChannelReader.getChannel(this.getApplicationContext());
        } catch (Exception e) {
            LogUtil.print(e.getMessage());
        }

        if (StringUtil.isEmpty(mChannelNo)) mChannelNo = "0";

        //初始化友盟
        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,APPKEY,mChannelNo));


    }


}
