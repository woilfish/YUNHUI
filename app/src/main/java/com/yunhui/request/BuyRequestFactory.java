package com.yunhui.request;

import android.content.Context;

import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;

/**
 * Created by pengmin on 2018/5/4.
 */

public class BuyRequestFactory {

    /**
     * 创建订单
     */
    public static RequestUtil createPayBill(Context context,String amount,String BusinessId,String coinProductId,String coinCount){
        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/yhPrePay", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("amount",amount);
        requestParams.put("businessid",BusinessId);
        requestParams.put("coinProductId",coinProductId);
        requestParams.put("coinCount",coinCount);
        return requestUtil;
    }

    /**
     * 充值支付
     */
    public static RequestUtil doAlPay(Context context,String billId){

        RequestUtil requestUtil = RequestUtil.obtainRequest(context,"user/yhPay", HttpRequest.RequestMethod.POST);
        HttpRequestParams requestParams = requestUtil.getRequestParams();
        requestParams.put("billId",billId);
        return requestUtil;
    }
}
