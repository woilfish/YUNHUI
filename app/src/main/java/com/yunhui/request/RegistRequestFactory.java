package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.yunhui.bean.RequestRegistBean;
import com.yunhui.util.StringUtil;

/**
 * Created by pengmin on 2018/4/18.
 */

public class RegistRequestFactory {

    /**
     * 注册接口
     * @param context
     * @param requestRegistBean
     * @return
     */
    public static RequestUtil createRegistRequest(Context context, RequestRegistBean requestRegistBean){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"api/signUp", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("mobile",requestRegistBean.getMobile());
        requestParams.put("code",requestRegistBean.getCode());
        requestParams.put("password",requestRegistBean.getPassword());
        requestParams.put("confirmPassword",requestRegistBean.getConfirmPassword());
        requestParams.put("userName",requestRegistBean.getUserName());
        requestParams.put("identifier",requestRegistBean.getIdentifler());
        if(StringUtil.isNotEmpty(requestRegistBean.getInviteCode())) {
            requestParams.put("inviteCode",requestRegistBean.getInviteCode());
        }

        return requestUtil;
    }

    public static RequestUtil sendSms(Context context,String mobile){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"api/sendSms", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("Mobile",mobile);
        return requestUtil;
    }
}
