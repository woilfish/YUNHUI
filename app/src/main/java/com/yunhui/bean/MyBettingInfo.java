package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyBettingInfo implements Serializable{

    private String jnlid;
    private String userid;
    private double amount;
    private String num;
    private String status;
    private String createtime;
    private String updatetime;
    private String times;
    private String qrystate;
    private String totalbenefit;
    private List<GuessListBean> guessListBeans;
    private List<MyBettingInfo> myBettingInfos;

    public MyBettingInfo(JSONArray jsonArray){
        this.initAttrWithJson(jsonArray);
    }

    private MyBettingInfo(JSONObject jsonObject) {
        this.initAttrWithJsonObject(jsonObject);
    }

    private void initAttrWithJson(JSONArray jsonArray) {

        if(jsonArray.length() > 0){
            myBettingInfos = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                MyBettingInfo myBettingInfo = null;
                try {
                    myBettingInfo = new MyBettingInfo(jsonArray.getJSONObject(i));
                    myBettingInfos.add(myBettingInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            if(guessListBeans != null) guessListBeans.clear();
        }
    }

    private void initAttrWithJsonObject(JSONObject jsonObject) {
        if(jsonObject.has("jnlid")){
            this.setJnlid(jsonObject.optString("jnlid"));
        }
        if(jsonObject.has("userid")){
            this.setUserid(jsonObject.optString("userid"));
        }
        if(jsonObject.has("amount")){
            this.setAmount(jsonObject.optDouble("amount"));
        }
        if(jsonObject.has("num")){
            this.setNum(jsonObject.optString("num"));
        }
        if(jsonObject.has("status")){
            this.setStatus(jsonObject.optString("status"));
        }
        if(jsonObject.has("createtime")){
            this.setCreatetime(jsonObject.optString("createtime"));
        }
        if(jsonObject.has("updatetime")){
            this.setUpdatetime(jsonObject.optString("updatetime"));
        }
        if(jsonObject.has("times")){
            this.setTimes(jsonObject.optString("times"));
        }
        if(jsonObject.has("qrystate")){
            this.setQrystate(jsonObject.optString("qrystate"));
        }
        if(jsonObject.has("totalbenefit")){
            this.setTotalbenefit(jsonObject.optString("totalbenefit"));
        }
        if(jsonObject.has("list")){
            JSONArray jsonArray = jsonObject.optJSONArray("list");
            GuessListBean guessListBean = new GuessListBean(jsonArray);
            this.setGuessListBeans(guessListBean.getGuessListBeans());
        }
    }

    public String getJnlid() {
        return jnlid;
    }

    public void setJnlid(String jnlid) {
        this.jnlid = jnlid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public List<GuessListBean> getGuessListBeans() {
        return guessListBeans;
    }

    public void setGuessListBeans(List<GuessListBean> guessListBeans) {
        this.guessListBeans = guessListBeans;
    }

    public List<MyBettingInfo> getMyBettingInfos() {
        return myBettingInfos;
    }

    public void setMyBettingInfos(List<MyBettingInfo> myBettingInfos) {
        this.myBettingInfos = myBettingInfos;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getQrystate() {
        return qrystate;
    }

    public void setQrystate(String qrystate) {
        this.qrystate = qrystate;
    }

    public String getTotalbenefit() {
        return totalbenefit;
    }

    public void setTotalbenefit(String totalbenefit) {
        this.totalbenefit = totalbenefit;
    }
}
