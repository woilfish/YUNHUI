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

    private String title;//标题
    private String time;//时间
    private String content;//内容

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

        if(jsonObject.has("title")){
            this.setTitle(jsonObject.optString("title"));
        }
        if(jsonObject.has("time")){
            this.setTime(jsonObject.optString("time"));
        }
        if(jsonObject.has("content")){
            this.setContent(jsonObject.optString("content"));
        }
    }

    private static List<ConsultingInfo> initAttrWithJson(JSONArray jsonArray, String pageNo) {
        if("1".equals(pageNo)){
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
