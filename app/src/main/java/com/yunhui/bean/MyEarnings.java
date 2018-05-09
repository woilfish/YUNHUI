package com.yunhui.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by pengmin on 2018/5/9.
 */

public class MyEarnings implements Serializable{

    private String total;
    private String btcoin;

    private MyEarnings() {
    }

    /**
     * 通过接口返回的参数，构造一个user对象
     *
     * @param jsonObject    数据
     */
    public MyEarnings(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
        if(jsonObject.has("total")){
            this.setTotal(jsonObject.optString("total"));
        }
        if(jsonObject.has("btcoin")){
            this.setBtcoin(jsonObject.optString("btcoin"));
        }
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBtcoin() {
        return btcoin;
    }

    public void setBtcoin(String btcoin) {
        this.btcoin = btcoin;
    }
}
