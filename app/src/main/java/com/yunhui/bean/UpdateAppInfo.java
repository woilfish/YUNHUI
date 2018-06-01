package com.yunhui.bean;

import org.json.JSONObject;

public class UpdateAppInfo {
    // app名字
    public String appname;
    //服务器版本
    public String serverVersion;
    //服务器标志
    public String serverFlag;
    //强制升级
    public String lastForce;
    //app最新版本地址
    public String updateurl;
    //升级信息
    public String upgradeinfo;

    public UpdateAppInfo(JSONObject jsonObject) {

        if(jsonObject.has("appname")){
            this.setAppname(jsonObject.optString("appname"));
        }
        if(jsonObject.has("serverVersion")){
            this.setServerVersion(jsonObject.optString("serverVersion"));
        }
        if(jsonObject.has("serverFlag")){
            this.setServerFlag(jsonObject.optString("serverFlag"));
        }
        if(jsonObject.has("lastForce")){
            this.setLastForce(jsonObject.optString("lastForce"));
        }
        if(jsonObject.has("updateurl")){
            this.setUpdateurl(jsonObject.optString("updateurl"));
        }
        if(jsonObject.has("upgradeinfo")){
            this.setUpgradeinfo(jsonObject.optString("upgradeinfo"));
        }
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(String serverVersion) {
        this.serverVersion = serverVersion;
    }

    public String getServerFlag() {
        return serverFlag;
    }

    public void setServerFlag(String serverFlag) {
        this.serverFlag = serverFlag;
    }

    public String getLastForce() {
        return lastForce;
    }

    public void setLastForce(String lastForce) {
        this.lastForce = lastForce;
    }

    public String getUpdateurl() {
        return updateurl;
    }

    public void setUpdateurl(String updateurl) {
        this.updateurl = updateurl;
    }

    public String getUpgradeinfo() {
        return upgradeinfo;
    }

    public void setUpgradeinfo(String upgradeinfo) {
        this.upgradeinfo = upgradeinfo;
    }
}
