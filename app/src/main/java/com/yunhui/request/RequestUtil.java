package com.yunhui.request;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.loopj.common.exception.BaseException;
import com.yunhui.component.dialog.ProgressDialog;
import com.yunhui.config.Config;
import com.loopj.common.httpEx.DefaultHttpResponseHandler;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.HttpResponseHandler;
import com.yunhui.util.LogUtil;
import com.yunhui.util.ToastUtil;

/**
 * Created by pengmin on 2018/3/27.
 * 业务请求基础类
 */

public class RequestUtil  extends HttpRequest{

    public static final String SCHEME_MSVR = "lklopenapi://";
    private static final String LOG_TAG = RequestUtil.class.getName();
    private boolean isAddCommonParams = true;
    //自动处理 progress dialog
    private boolean autoShowProgress;
    private ProgressDialog progressDialog;
    private String progressMessage;
    private boolean autoShowToast = true;

    public RequestUtil(Context context) {
        this(context, new DefaultHttpResponseHandler());
    }

    public RequestUtil(Context context,HttpResponseHandler responseHandler) {
        this(context,responseHandler,true);
    }

    public RequestUtil(Context context, HttpResponseHandler responseHandler, boolean isAddPublicParam) {
        super(responseHandler, context);
        this.isAddCommonParams = isAddPublicParam;
        this.requestParams = new HttpRequestParams();
        setTimeout(95 * 1000); // http 超时时间 95秒
    }

    /**
     * 根据上下文，请求地址，请求方法获取request对象,该方法自动处理 progress dialog
     *
     * @param context 当前请求的 context
     * @param url     当前请求的 url 地址
     * @param method  当前请求的 method 方法
     * @return {@link RequestUtil}
     */
    public static RequestUtil obtainRequest(Context context, String url, RequestMethod method) {
        RequestUtil request = new RequestUtil(context);
        request.setRequestURL(url);
        request.setRequestMethod(method);
        request.setAutoShowProgress(true);
        return request;
    }

    /**
     * 设置自动处理 progress dialog
     *
     * @param autoShowProgress 设置是否显示 ProgressBar
     */
    public RequestUtil setAutoShowProgress(boolean autoShowProgress) {
        this.autoShowProgress = autoShowProgress;
        return this;
    }

    /**
     * 设置自动toast
     *
     * @param autoToast 设置是否自动显示 设置自动toast
     */
    public RequestUtil setAutoShowToast(boolean autoToast) {
        this.autoShowToast = autoToast;
        return this;
    }

    /**
     * 设置 progress message
     *
     * @param message ProgressBar 的消息文本
     * @return
     */
    public RequestUtil setProgressMessage(String message) {
        this.progressMessage = message;
        return this;
    }


    /**
     * 获取BaseUrl
     *
     * @return Base Url
     */
    public static String getBaseUrl() {
        return Config.getBaseRequestUrl();
    }

    /**
     * 根据 URL 的 Secheme 替换主机地址
     *
     * @param url 当前请求的 url 地址
     * @return 当前请求的 url 地址
     */
    public static String replaceHostByUrlSecheme(String url) {
        if (url == null) {
            return "";
        }
        //绝对路径
        if (url.contains(SCHEME_MSVR)) {
            url = getBaseUrl() + url.substring(SCHEME_MSVR.length());
        }
        return url;
    }

    /**
     * @param isAddCommonParams 是否添加公用参数
     */
    public void isAddCommonParams(boolean isAddCommonParams) {
        this.isAddCommonParams = isAddCommonParams;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (autoShowProgress) {
            showProgressDialog();
        }
    }

    @Override
    public void onSuccess() {
        super.onSuccess();
    }

    @Override
    public void onFailure(BaseException error) {
        super.onFailure(error);
        autoToastErrorMessage(error.getMessage());
    }

    @Override
    protected void onExecuteBefore() {
        requestURL = replaceHostByUrlSecheme(requestURL);
        if (!requestURL.contains("://")) {
            //URL 不是绝对路径则，在前面添加主机头。
            requestURL = getBaseUrl().concat(requestURL);
        }
        requestURL = requestURL.trim();


        //该参数可用于分别缓存用户化的数据。
        HttpRequestParams params = this.requestParams;

        if (isAddCommonParams) {
            addCommonParams(params);
        }

        //添加GIZP 头
        setHeader("Accept-Encoding", "gzip");
    }

    /**
     * 添加公共参数
     * @param params
     */
    private void addCommonParams(HttpRequestParams params){
        if (params == null){
            params = new HttpRequestParams();
        }

        //todo 添加公共参数

    }

    /**
     * 设置accept
     *
     * @param value 当前请求 Head 的 Accept 的值
     */
    public void setAccept(String value) {
        setHeader("Accept", value);
    }


    @Override
    public void onFinish() {
        super.onFinish();
        if(autoShowProgress){
            dismissProgressDialog();
        }
        if (Config.isDebug()) {
            HttpResponseHandler rp = getResponseHandler();

            Object[] objects = new Object[5];
            objects[0] = getRequestURL();
            objects[1] = getRequestParams();
            objects[2] = rp.getResultCode();
            objects[3] = rp.getResultMessage();
            objects[4] = rp.getResultData();

            Log.d(LOG_TAG, String.format("Details : " + "\nURL: %s" + "\nPARAMS: %s" + "\nCODE: %s" + "\nMSG: %s" + "\nDATA: %s", objects));
        }
    }

    /**
     * 显示 ProgressBar
     */
    private void showProgressDialog() {
        if (getContext() == null) {
            return;
        }
        if (getContext() instanceof FragmentActivity) {
            progressDialog = new ProgressDialog();
            ((FragmentActivity) getContext()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (progressDialog == null) {
                        return;
                    }
                    progressDialog.setProgressMessage(progressMessage);
                    progressDialog.show(((FragmentActivity) context).getSupportFragmentManager());
                }
            });
        }
    }

    /**
     * 隐藏 ProgressBar
     */
    private void dismissProgressDialog() {
        Context context = getContext();
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (progressDialog == null) {
                            return;
                        }
                        progressDialog.dismissAllowingStateLoss();
                        progressDialog = null;
                    } catch (Exception e) {
                        LogUtil.print(e.getMessage());
                    }
                }
            });
        }
    }

    /**
     * toast 提示错误信息
     *
     * @param msg
     */
    private void autoToastErrorMessage(String msg) {
        if (autoShowToast) {
            ToastUtil.toast(context.getApplicationContext(), msg);
        }
    }

}
