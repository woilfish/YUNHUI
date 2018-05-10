package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/5/9.
 */

public class MyEarnings implements Serializable{

    private String total;
    private String btcoin;
    private List<MyMillInfo> myMillInfos = null;

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
        if(jsonObject.has("list")){
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            if(jsonArray.length() > 0){
                myMillInfos = new ArrayList<>();
                for(int i = 0;i < jsonArray.length();i++){
                    MyMillInfo myMillInfo = new MyMillInfo(jsonArray.optJSONObject(i));
                    myMillInfos.add(myMillInfo);
                }
            }
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

    public List<MyMillInfo> getMyMillInfos() {
        return myMillInfos;
    }

    public void setMyMillInfos(List<MyMillInfo> myMillInfos) {
        this.myMillInfos = myMillInfos;
    }
}
