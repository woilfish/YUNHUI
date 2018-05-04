package com.yunhui.pay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * Created by pengmin on 2018/5/4.
 */

public class AlRunnable implements Runnable {

    public static final int SDK_PAY_FLAG = 1;
    private Handler handler;
    private Activity activity;
    private String orderInfo;
    public AlRunnable(Activity activity, Handler handler, String orderInfo) {
        this.activity = activity;
        this.handler = handler;
        this.orderInfo = orderInfo;
    }

    @Override
    public void run() {
        PayTask alipay = new PayTask(activity);
        Map<String,String> result = alipay.payV2(orderInfo,true);

        Message message = new Message();
        message.what = SDK_PAY_FLAG;
        message.obj = result;
        handler.sendMessage(message);
    }
}
