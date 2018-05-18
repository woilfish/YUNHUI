package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pengmin on 2018/4/23.
 * 任务bean
 */

public class TaskInfo {

    private int id;
    private String name;
    private String taskid;
    private String appid;
    private String appname;
    private String applink;
    private String createtime;
    private String updatetime;
    private String iconUrl;

    private static List<TaskInfo> taskInfos = new ArrayList<>();

    public TaskInfo() {
    }

    private TaskInfo(JSONObject jsonObject){
        this.initAttrWithJson(jsonObject);
    }

    private void initAttrWithJson(JSONObject jsonObject) {
        if(jsonObject.has("id")){
            this.setId(jsonObject.optInt("id"));
        }
        if(jsonObject.has("name")){
            this.setName(jsonObject.optString("name"));
        }
        if(jsonObject.has("taskid")){
            this.setTaskid(jsonObject.optString("taskid"));
        }
        if(jsonObject.has("appid")){
            this.setAppid(jsonObject.optString("appid"));
        }
        if(jsonObject.has("appname")){
            this.setAppname(jsonObject.optString("appname"));
        }
        if(jsonObject.has("applink")){
            this.setApplink(jsonObject.optString("applink"));
        }
        if(jsonObject.has("createtime")){
            this.setCreatetime(jsonObject.optString("createtime"));
        }
        if(jsonObject.has("updatetime")){
            this.setUpdatetime(jsonObject.optString("updatetime"));
        }
        if(jsonObject.has("iconUrl")){
            this.setIconUrl(jsonObject.optString("iconUrl"));
        }
    }

    public static List<TaskInfo> initAttrWithJson(JSONArray jsonArray, int pageNo) {
        if(pageNo == 1){
            taskInfos.clear();
        }
        for(int i = 0;i < jsonArray.length();i++){
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                TaskInfo taskInfo = new TaskInfo(jsonObject);
                taskInfos.add(taskInfo);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return taskInfos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getApplink() {
        return applink;
    }

    public void setApplink(String applink) {
        this.applink = applink;
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

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}
