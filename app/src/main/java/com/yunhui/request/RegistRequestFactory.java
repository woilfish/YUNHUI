package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.yunhui.bean.RequestRegistBean;

/**
 * Created by pengmin on 2018/4/18.
 */

public class RegistRequestFactory {

    public static RequestUtil createRegistRequest(Context context, RequestRegistBean requestRegistBean){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"signUp", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("mobile",requestRegistBean.getMobile());
        requestParams.put("code",requestRegistBean.getCode());
        requestParams.put("password",requestRegistBean.getPassword());
        requestParams.put("confirmPassword",requestRegistBean.getConfirmPassword());
        requestParams.put("userName",requestRegistBean.getUserName());
        requestParams.put("identifier",requestRegistBean.getIdentifler());

        return requestUtil;
    }
}
