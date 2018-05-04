package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;

/**
 * Created by pengmin on 2018/5/4.
 */

public class RetrievePasswordFactory {

    public static RequestUtil sendNoSms(Context context,String Mobile){

        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"api/sendNoSms", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("Mobile",Mobile);
        return requestUtil;
    }

    public static RequestUtil updataPwd(Context context,String Mobile,String code,String password,String confirmPassword){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"api/uppwd", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("mobile",Mobile);
        requestParams.put("code",code);
        requestParams.put("password",password);
        requestParams.put("confirmPassword",confirmPassword);
        return requestUtil;
    }
}
