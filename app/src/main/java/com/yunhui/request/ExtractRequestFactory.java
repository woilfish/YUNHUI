package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;

/**
 * Created by pengmin on 2018/5/7.
 */

public class ExtractRequestFactory {

    public static RequestUtil createExtractBill(Context context,String amount,String walletId){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/yhPrePay", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("businessid","DH");
        requestParams.put("amount",amount);
        requestParams.put("walletId",walletId);
        return  requestUtil;
    }

    public static RequestUtil sendExtractSms(Context context,String billId){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/sendPaySms", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("billId",billId);
        return requestUtil;
    }

    public static RequestUtil extract(Context context,String code,String billId){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/doPay", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("billId",billId);
        httpRequestParams.put("Code",code);
        return requestUtil;
    }

    public static RequestUtil querttPoundage(Context context,String billId){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/queryFee", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
//        httpRequestParams.put("billId",billId);
        return requestUtil;
    }

}
