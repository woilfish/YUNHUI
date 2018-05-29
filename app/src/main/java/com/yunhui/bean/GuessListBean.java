package com.yunhui.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GuessListBean {

    private String id;
    private String phase;
    private String x_id;
    private String match_num;
    private String match_name;
    private String math_date;
    private String time_endsale;
    private String home_team;
    private String away_team;
    private String handicap;
    private String final_score;
    private String half_score;
    private String status;
    private String odds_h;
    private String odds_d;
    private String odds_a;
    private String createtime;
    private String updatetime;

    private List<GuessListBean> guessListBeans;

    private GuessListBean(JSONObject jsonObject) {
        this.initAttrWithJsonObject(jsonObject);
    }

    public GuessListBean(JSONArray jsonArray){
        this.initAttrWithJson(jsonArray);
    }

    private void initAttrWithJson(JSONArray jsonArray) {

        if(jsonArray.length() > 0){
            guessListBeans = new ArrayList<>();
            for(int i = 0;i < jsonArray.length();i++){
                GuessListBean guessListBean = null;
                try {
                    guessListBean = new GuessListBean(jsonArray.getJSONObject(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                guessListBeans.add(guessListBean);
            }
        }else{
            if(guessListBeans != null) guessListBeans.clear();
        }
    }

    private void initAttrWithJsonObject(JSONObject jsonObject) {

        if(jsonObject.has("id")){
            this.setId(jsonObject.optString("id"));
        }
        if(jsonObject.has("phase")){
            this.setPhase(jsonObject.optString("phase"));
        }
        if(jsonObject.has("x_id")){
            this.setX_id(jsonObject.optString("x_id"));
        }
        if(jsonObject.has("match_num")){
            this.setMatch_num(jsonObject.optString("match_num"));
        }
        if(jsonObject.has("match_name")){
            this.setMatch_name(jsonObject.optString("match_name"));
        }
        if(jsonObject.has("math_date")){
            this.setMath_date(jsonObject.optString("math_date"));
        }
        if(jsonObject.has("time_endsale")){
            this.setTime_endsale(jsonObject.optString("time_endsale"));
        }
        if(jsonObject.has("home_team")){
            this.setHome_team(jsonObject.optString("home_team"));
        }
        if(jsonObject.has("away_team")){
            this.setAway_team(jsonObject.optString("away_team"));
        }
        if(jsonObject.has("handicap")){
            this.setHandicap(jsonObject.optString("handicap"));
        }
        if(jsonObject.has("final_score")){
            this.setFinal_score(jsonObject.optString("final_score"));
        }
        if(jsonObject.has("half_score")){
            this.setHalf_score(jsonObject.optString("half_score"));
        }
        if(jsonObject.has("status")){
            this.setStatus(jsonObject.optString("status"));
        }
        if(jsonObject.has("odds_h")){
            this.setOdds_h(jsonObject.optString("odds_h"));
        }
        if(jsonObject.has("odds_d")){
            this.setOdds_d(jsonObject.optString("odds_d"));
        }
        if(jsonObject.has("odds_a")){
            this.setOdds_a(jsonObject.optString("odds_a"));
        }
        if(jsonObject.has("createtime")){
            this.setCreatetime(jsonObject.optString("createtime"));
        }
        if(jsonObject.has("updatetime")){
            this.setUpdatetime(jsonObject.optString("updatetime"));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getX_id() {
        return x_id;
    }

    public void setX_id(String x_id) {
        this.x_id = x_id;
    }

    public String getMatch_num() {
        return match_num;
    }

    public void setMatch_num(String match_num) {
        this.match_num = match_num;
    }

    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getMath_date() {
        return math_date;
    }

    public void setMath_date(String math_date) {
        this.math_date = math_date;
    }

    public String getTime_endsale() {
        return time_endsale;
    }

    public void setTime_endsale(String time_endsale) {
        this.time_endsale = time_endsale;
    }

    public String getHome_team() {
        return home_team;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }

    public String getHandicap() {
        return handicap;
    }

    public void setHandicap(String handicap) {
        this.handicap = handicap;
    }

    public String getFinal_score() {
        return final_score;
    }

    public void setFinal_score(String final_score) {
        this.final_score = final_score;
    }

    public String getHalf_score() {
        return half_score;
    }

    public void setHalf_score(String half_score) {
        this.half_score = half_score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOdds_h() {
        return odds_h;
    }

    public void setOdds_h(String odds_h) {
        this.odds_h = odds_h;
    }

    public String getOdds_d() {
        return odds_d;
    }

    public void setOdds_d(String odds_d) {
        this.odds_d = odds_d;
    }

    public String getOdds_a() {
        return odds_a;
    }

    public void setOdds_a(String odds_a) {
        this.odds_a = odds_a;
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
}
