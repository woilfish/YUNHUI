package com.yunhui.controller;

import android.support.v4.app.FragmentActivity;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.request.RequestUtil;

/**
 * Created by pengmin on 2018/4/3.
 * APP升级服务管理
 */

public class AppUpdateController {

    private FragmentActivity context;

    public AppUpdateController(FragmentActivity context) {
        this.context = context;
    }

    /**
     * 检查app版本是否需要升级
     */
    public void checkAppUpdate(){

        RequestUtil request = RequestUtil.obtainRequest(context,"", HttpRequest.RequestMethod.POST);

        request.setIHttpRequestEvents(new IHttpRequestEvents(){

        });
        request.setAutoShowProgress(false);
        request.setAutoShowToast(false);
        request.execute();
    }
}
