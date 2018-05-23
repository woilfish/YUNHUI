package com.yunhui.bean;

import org.json.JSONObject;

/**
 * Created by pengmin on 2018/5/10.
 */

public class MyMillInfo {

    private String prdId;
    private String count;
    private Double benefit;

    public MyMillInfo() {
    }

    /**
     * 通过接口返回的参数，构造一个user对象
     *
     * @param jsonObject    数据
     */
    public MyMillInfo(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
        if(jsonObject.has("prdId")){
            this.setPrdId(jsonObject.optString("prdId"));
        }
        if(jsonObject.has("count")){
            this.setCount(jsonObject.optString("count"));
        }
        if(jsonObject.has("benefit")){
            this.setBenefit(jsonObject.optDouble("benefit"));
        }
    }

    public String getPrdId() {
        return prdId;
    }

    public void setPrdId(String prdId) {
        this.prdId = prdId;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Double getBenefit() {
        return benefit;
    }

    public void setBenefit(Double benefit) {
        this.benefit = benefit;
    }
}
