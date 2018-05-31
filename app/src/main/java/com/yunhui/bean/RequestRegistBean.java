package com.yunhui.bean;

/**
 * Created by pengmin on 2018/4/18.
 * 注册请求实体
 */

public class RequestRegistBean {

    private String mobile;
    private String code;
    private String password;
    private String confirmPassword;
    private String userName;
    private String identifler;
    private String inviteCode;

    public RequestRegistBean() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIdentifler() {
        return identifler;
    }

    public void setIdentifler(String identifler) {
        this.identifler = identifler;
    }


    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }
}
