package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RechargeBean {

    private String id;
    private String name;
    private int price;
    private int amount;
    private String content;
    private String createtime;

    public List<RechargeBean> getRechargeBeans() {
        return rechargeBeans;
    }

    public void setRechargeBeans(List<RechargeBean> rechargeBeans) {
        this.rechargeBeans = rechargeBeans;
    }

    private List<RechargeBean> rechargeBeans;

    public RechargeBean(JSONArray jsonArray) {
        this.initAttrWithJson(jsonArray);
    }

    private RechargeBean(JSONObject jsonObject) {
        this.initAttrWithJsonObject(jsonObject);
    }

    private void initAttrWithJsonObject(JSONObject jsonObject) {

        if(jsonObject.has("id")){
            this.setId(jsonObject.optString("id"));
        }
        if(jsonObject.has("name")){
            this.setName(jsonObject.optString("name"));
        }
        if(jsonObject.has("price")){
            this.setPrice(jsonObject.optInt("price"));
        }
        if(jsonObject.has("amount")){
            this.setAmount(jsonObject.optInt("amount"));
        }
        if(jsonObject.has("content")){
            this.setContent(jsonObject.optString("content"));
        }
        if(jsonObject.has("createtime")){
            this.setCreatetime(jsonObject.optString("createtime"));
        }
    }

    private void initAttrWithJson(JSONArray jsonArray) {
        if(jsonArray.length() > 0){
            rechargeBeans = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                RechargeBean rechargeBean = null;
                try {
                    rechargeBean = new RechargeBean(jsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                rechargeBeans.add(rechargeBean);
            }
        }else{
            if(rechargeBeans != null) rechargeBeans.clear();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
