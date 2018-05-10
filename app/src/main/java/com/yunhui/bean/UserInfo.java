package com.yunhui.bean;

import org.json.JSONObject;

/**
 * Created by pengmin on 2018/4/24.
 * 用户信息
 */

public class UserInfo {
    private long time;
    private String userId;
    private String token;
    private String mobile;

    public UserInfo() {
    }

    public UserInfo(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
        if(jsonObject.has("time")){
            this.setTime(jsonObject.optLong("time"));
        }
        if(jsonObject.has("userid")){
            this.setUserId(jsonObject.optString("userid"));
        }
        if(jsonObject.has("token")){
            this.setToken(jsonObject.optString("token"));
        }
        if(jsonObject.has("mobile")){
            this.setMobile(jsonObject.optString("mobile"));
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
