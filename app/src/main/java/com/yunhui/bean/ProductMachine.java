package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/19.
 * 挖矿机器
 */

public class ProductMachine {

    private String id;
    private String title;
    private int amout;
    private String dayBenifit;
    private String circle;
    private String totalBenifit;
    private String content;
    private String type;
    private String state;
    private String createtime;
    private String updateTime;

    private static List<ProductMachine> productMachines = new ArrayList<>();

    public ProductMachine() {
    }

    /**
     * 通过接口返回的参数，构造一个user对象
     *
     * @param jsonObject    数据
     */
    private ProductMachine(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
       if(jsonObject.has("id")){
           this.setId(jsonObject.optString("id"));
       }
       if(jsonObject.has("title")){
           this.setTitle(jsonObject.optString("title"));
       }
       if(jsonObject.has("amount")){
           this.setAmout(jsonObject.optInt("amount"));
       }
       if(jsonObject.has("dayBenifit")){
           this.setDayBenifit(jsonObject.optString("dayBenifit"));
       }
       if(jsonObject.has("circle")){
           this.setCircle(jsonObject.optString("circle"));
       }
       if(jsonObject.has("totalBenifit")){
           this.setTotalBenifit(jsonObject.optString("totalBenifit"));
       }
       if(jsonObject.has("content")){
           this.setContent(jsonObject.optString("content"));
       }
       if(jsonObject.has("type")){
           this.setType(jsonObject.optString("type"));
       }
       if(jsonObject.has("state")){
           this.setState(jsonObject.optString("state"));
       }
       if(jsonObject.has("createtime")){
           this.setCreatetime(jsonObject.optString("createtime"));
       }
       if(jsonObject.has("updateTime")){
           this.setUpdateTime(jsonObject.optString("updateTime"));
       }
    }

    public static List<ProductMachine> initAttrWithJson(JSONArray jsonArray,int pageNo) {
        if(pageNo == 1){
            productMachines.clear();
        }
        for(int i = 0;i < jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ProductMachine productMachine = new ProductMachine(jsonObject);
                productMachines.add(productMachine);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return productMachines;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmout() {
        return amout;
    }

    public void setAmout(int amout) {
        this.amout = amout;
    }

    public String getDayBenifit() {
        return dayBenifit;
    }

    public void setDayBenifit(String dayBenifit) {
        this.dayBenifit = dayBenifit;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getTotalBenifit() {
        return totalBenifit;
    }

    public void setTotalBenifit(String totalBenifit) {
        this.totalBenifit = totalBenifit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public static List<ProductMachine> getProductMachines() {
        return productMachines;
    }

    public static void setProductMachines(List<ProductMachine> productMachines) {
        ProductMachine.productMachines = productMachines;
    }
}
