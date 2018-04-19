package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;

/**
 * Created by pengmin on 2018/4/18.
 */

public class LoginRequestFactory {

    public static RequestUtil createLoginRequest(Context context,String mobile,String code,String password){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"api/login", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("mobile",mobile);
        requestParams.put("code",code);
        requestParams.put("password",password);
        return requestUtil;
    }
}
