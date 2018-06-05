package com.yunhui.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.bean.UpdateAppInfo;
import com.yunhui.component.dialog.AlertDialog;
import com.yunhui.request.RequestUtil;
import com.yunhui.service.AppUpdateService;
import com.yunhui.util.AppUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by pengmin on 2018/4/3.
 * APP升级服务管理
 */

public class AppUpdateController implements ServiceConnection{

    private FragmentActivity context;
    /**
     * 是否需要强制升级
     */
    private boolean ForceUpdate;

    /**
     * 升级apk URL
     */
    private String ClientUrl = "";

    /**
     * 绑定服务接口
     */
    private AppUpdateService bindService;

    /**
     * 绑定升级ProgressDialog管理类
     */
    private UpgradeProgressDialog progressDialog = new UpgradeProgressDialog();

    public AppUpdateController(FragmentActivity context) {
        this.context = context;
    }

    /**
     * 检查app版本是否需要升级
     */
    public void checkAppUpdate(){

        RequestUtil request = RequestUtil.obtainRequest(context,"user/queryAppVersion", HttpRequest.RequestMethod.POST);

        request.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("updateurl")){
                    if(!AppUtil.getAppVersionCode(context).trim().equals(jsonObject.optString("serverVersion").trim())) {
                        ForceUpdate = jsonObject.optString("lastForce").equals("1");
                        ClientUrl = jsonObject.optString("updateurl");
                        UpdateAppInfo updateAppInfo = new UpdateAppInfo(jsonObject);
                        showUpdateDialog("APP版本升级", updateAppInfo.getUpgradeinfo());
                    }
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        request.setAutoShowProgress(false);
        request.setAutoShowToast(false);
        request.execute();
    }

    /**
     * 提示升级对话框
     */
    private void showUpdateDialog(String descTitle, String clientDesc){
        //中间显示版本更新描述的信息
        AlertDialog alertDialog = new AlertDialog();
        alertDialog.setShowTitleBg(false);
        alertDialog.setTitleColor(R.color.white);
        alertDialog.setTitle(descTitle);
        alertDialog.setMessage(clientDesc);
        alertDialog.setButtons(new String[]{context.getString(R.string.plat_upgrade)});
        alertDialog.setButtonsTextColor(R.color.color_white_8c8fa3,0);
        alertDialog.setDialogDelegate(new AlertDialog.AlertDialogDelegate() {

            @Override
            public void onButtonClick(AlertDialog dialog, View view, int index) {
                super.onButtonClick(dialog, view, index);
                dialog.dismiss();
                switch (index) {
                    case 0:
                        startDownload();
                        break;
                }
            }
        });
        alertDialog.show(context.getSupportFragmentManager());
    }

    /**
     * 开始下载升级apk,强制升级使用bindService,非强制升级使用startService
     */
    private void startDownload() {
        Log.d("checkUpdate", "Download APK ClientUrl    ：" + ClientUrl);
        Intent intent = new Intent(context, AppUpdateService.class);
        intent.putExtra(AppUpdateService.KEY_URL, ClientUrl);
        if (ForceUpdate) {
            context.bindService(intent, this, Activity.BIND_AUTO_CREATE);
        } else {
            context.startService(intent);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bindService = ((AppUpdateService.UpgradeServiceBinder) service).getService();
        bindService.setOnUpgradeProgressListener(new AppUpdateService.OnUpgradeProgressListener(){
            @Override
            public void onStart() {
                progressDialog.showProgressDialog();
                progressDialog.updateProgress(0);
            }

            @Override
            public void onProgress(int progress) {
                progressDialog.updateProgress(progress);
            }

            @Override
            public void onComplete(File file) {
                progressDialog.progressDialog.dismiss();
                showSuccessUpgradeDialog(file);
            }

            @Override
            public void onFailure() {
                progressDialog.progressDialog.dismiss();
                showFailUpgradeDialog();
            }

            @Override
            public void onFinished() {
                context.unbindService(AppUpdateController.this);
            }
        });
        bindService.startUpgrade(ClientUrl);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bindService = null;
    }

    /**
     * 创建升级成功对话框提示
     */
    private void showSuccessUpgradeDialog(final File file) {
        AlertDialog alertDialog = new AlertDialog();
        alertDialog.setTitle(context.getString(R.string.core_download_lakala_complete_prompt));
        alertDialog.setMessage(context.getString(R.string.core_download_lakala_complete_prompt));
        alertDialog.setButtons(new String[]{context.getString(R.string.ui_certain)});
        alertDialog.setButtonsTextColor(R.color.color_white_8c8fa3,0);
        alertDialog.setDialogDelegate(new AlertDialog.AlertDialogDelegate() {

            @Override
            public void onButtonClick(AlertDialog dialog, View view, int index) {
                super.onButtonClick(dialog, view, index);
                dialog.dismiss();
                if (index == 0) {
                    bindService.installApk(file);
                }
            }
        });
        alertDialog.show(context.getSupportFragmentManager());
    }

    /**
     * 创建升级错误对话框提示
     */
    private void showFailUpgradeDialog() {
        AlertDialog alertDialog = new AlertDialog();
        alertDialog.setTitle(context.getString(R.string.core_upgrade_lakala));
        alertDialog.setMessage(context.getString(R.string.core_download_lakala_fail));
        alertDialog.setButtons(new String[]{context.getString(R.string.ui_cancel), context.getString(R.string.plat_retry)});
        alertDialog.setButtonsTextColor(R.color.color_white_8c8fa3,0);
        alertDialog.setDialogDelegate(new AlertDialog.AlertDialogDelegate() {

            @Override
            public void onButtonClick(AlertDialog dialog, View view, int index) {
                super.onButtonClick(dialog, view, index);
                dialog.dismiss();
                if (index == 0 && ForceUpdate) {
                }

                else if (index == 1) {
                    startDownload();
                }
            }
        });
        alertDialog.show(context.getSupportFragmentManager());
    }



    /**
     * 下载拉卡拉进度对话框操作类
     */
    private class UpgradeProgressDialog {
        private ProgressDialog progressDialog;

        private void showProgressDialog() {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setTitle(context.getString(R.string.core_downloading_lakala));
                progressDialog.setIndeterminate(false);
                progressDialog.setCancelable(false);
                progressDialog.setProgress(100);
            }
            progressDialog.show();
        }

        /**
         * 更新进度条
         *
         * @param progress 进度 (max 100)
         */
        private void updateProgress(int progress) {
            progressDialog.setProgress(progress);
        }
    }

}
