package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/2.
 * 咨询栏目信息
 */

public class ConsultingInfo {

    private String msgId;//信息Id
    private String createtime;//信息创建时间
    private String updateTime;//信息更新时间
    private String message;//信息内容
    private String title;//标题
    private String time;//时间
    private String type;//类型

    private static List<ConsultingInfo> consultingInfos = new ArrayList<>();

    private ConsultingInfo() {
    }

    /**
     * 通过接口返回的参数，构造一个user对象
     *
     * @param jsonObject    数据
     */
    private ConsultingInfo(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
        if(jsonObject.has("msgId")){
            this.setMsgId(jsonObject.optString("msgId"));
        }
        if(jsonObject.has("createtime")){
            this.setCreatetime(jsonObject.optString("createtime"));
        }
        if(jsonObject.has("updateTime")){
            this.setUpdateTime(jsonObject.optString("updateTime"));
        }
        if(jsonObject.has("message")){
            this.setMessage(jsonObject.optString("message"));
        }
        if(jsonObject.has("title")){
            this.setTitle(jsonObject.optString("title"));
        }
        if(jsonObject.has("time")){
            this.setTime(jsonObject.optString("time"));
        }
        if(jsonObject.has("type")){
            this.setType(jsonObject.optString("type"));
        }

    }

    public static List<ConsultingInfo> initAttrWithJson(JSONArray jsonArray, int pageNo) {
        if(pageNo == 1){
            consultingInfos.clear();
        }
        for(int i = 0;i < jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ConsultingInfo consultingInfo = new ConsultingInfo(jsonObject);
                consultingInfos.add(consultingInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return consultingInfos;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static List<ConsultingInfo> getConsultingInfos() {
        return consultingInfos;
    }

    public static void setConsultingInfos(List<ConsultingInfo> consultingInfos) {
        ConsultingInfo.consultingInfos = consultingInfos;
    }
}
