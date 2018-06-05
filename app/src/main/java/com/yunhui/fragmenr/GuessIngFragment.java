package com.yunhui.fragmenr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.activity.BettingActivity;
import com.yunhui.activity.HomeActivity;
import com.yunhui.adapter.GuessAdapter;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.RechargeBean;
import com.yunhui.clickinterface.ListItemClickHelp;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;
import com.yunhui.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 竞猜
 */
public class GuessIngFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener,ListItemClickHelp{

    private HomeActivity homeActivity;
    private View parentView;
    private TextView tv_guessDate;
    private RefreshListView rlv_guessList;
    private GuessAdapter guessAdapter;
    private List<GuessListBean> guessListBeans;
    private List<GuessListBean> guessListBeansSelect;
    private List<String> positions;
    private Button b_guessOk;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 000000:
                    guessAdapter.refreshData(guessListBeans);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_guess,null);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        positions = new ArrayList<>();
        queryTeams();
    }

    public void queryTeams(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(homeActivity,"user/queryTeams", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("teams")){
                    JSONArray jsonArray = jsonObject.optJSONArray("teams");
                    if(jsonArray.length() > 0) {
                        GuessListBean guessListBean = new GuessListBean(jsonArray);
                        guessListBeans = guessListBean.getGuessListBeans();
                        Message message = new Message();
                        message.what = 000000;
                        handler.sendMessage(message);
                    }else{
                        ToastUtil.toast(homeActivity,"当前没有竞猜比赛，稍后再试");
                    }
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }

    private void initView() {

        tv_guessDate = parentView.findViewById(R.id.guessdate);
        rlv_guessList = parentView.findViewById(R.id.guessList);
        b_guessOk = parentView.findViewById(R.id.guessOk);
        tv_guessDate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        guessAdapter = new GuessAdapter(homeActivity,guessListBeans,this);
        rlv_guessList.setAdapter(guessAdapter);
        rlv_guessList.setOnRefreshListViewListener(this);
        rlv_guessList.setPullRefreshEnable(false);
        rlv_guessList.setPullLoadEnable(false);
        b_guessOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(positions.size() >= 2){
                    guessListBeansSelect = new ArrayList<>();
                    for(int i = 0;i < positions.size();i++){
                        guessListBeansSelect.add(guessListBeans.get(Integer.parseInt(positions.get(i))));
                    }
                    Intent bettingIntent = new Intent(homeActivity, BettingActivity.class);
                    bettingIntent.putExtra("list",(Serializable)guessListBeansSelect);
                    startActivity(bettingIntent);
                }else{
                    ToastUtil.toast(homeActivity,"至少选择2场比赛才可以下注");
                }
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onClick(View item, View parent, int position, int which) {

        LinearLayout l_homeTeam = null;
        LinearLayout l_flatTeam = null;
        LinearLayout l_visitingTeam = null;
        TextView tv_firstTeam = null,tv_firstOdds = null,tv_flat = null,tv_flatT = null,tv_secondTeam = null,tv_secondOdds = null;

        if (position >= rlv_guessList.getFirstVisiblePosition() && position <= rlv_guessList.getLastVisiblePosition()) {
            int positionInListView = position - rlv_guessList.getFirstVisiblePosition() + 1;
            l_homeTeam = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.homeTeam);
            l_flatTeam= rlv_guessList.getChildAt(positionInListView).findViewById(R.id.flatTeam);
            l_visitingTeam = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.visitingTeam);
            tv_firstTeam = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.firstteam);
            tv_firstOdds = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.firstodds);
            tv_flat = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.flat);
            tv_flatT = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.flatT);
            tv_secondTeam = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.secondteam);
            tv_secondOdds = rlv_guessList.getChildAt(positionInListView).findViewById(R.id.secondodds);
        }



        switch (which){
            case R.id.homeTeam:
                if(guessListBeans.get(position).isHome()){
                    l_homeTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_firstTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_firstOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setHome(false);
                    guessListBeans.get(position).setFlat(false);
                    guessListBeans.get(position).setVisiting(false);
                }else {
                    l_homeTeam.setBackground(getResources().getDrawable(R.drawable.bg_left));
                    tv_firstTeam.setTextColor(getResources().getColor(R.color.white));
                    tv_firstOdds.setTextColor(getResources().getColor(R.color.white));
                    l_flatTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_flat.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_flatT.setTextColor(getResources().getColor(R.color.color_959697));
                    l_visitingTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_secondTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_secondOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setHome(true);
                    guessListBeans.get(position).setFlat(false);
                    guessListBeans.get(position).setVisiting(false);
                }
                break;
            case R.id.flatTeam:
                if(guessListBeans.get(position).isFlat()){
                    l_flatTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_flat.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_flatT.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setFlat(false);
                    guessListBeans.get(position).setHome(false);
                    guessListBeans.get(position).setVisiting(false);
                }else {
                    l_flatTeam.setBackgroundColor(getResources().getColor(R.color.color_EE9707));
                    tv_flat.setTextColor(getResources().getColor(R.color.white));
                    tv_flatT.setTextColor(getResources().getColor(R.color.white));
                    l_visitingTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_secondTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_secondOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    l_homeTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_firstTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_firstOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setFlat(true);
                    guessListBeans.get(position).setHome(false);
                    guessListBeans.get(position).setVisiting(false);
                }
                break;
            case R.id.visitingTeam:
                if(guessListBeans.get(position).isVisiting()){
                    l_visitingTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_secondTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_secondOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setVisiting(false);
                    guessListBeans.get(position).setHome(false);
                    guessListBeans.get(position).setFlat(false);
                }else {
                    l_visitingTeam.setBackground(getResources().getDrawable(R.drawable.bg_right));
                    tv_secondTeam.setTextColor(getResources().getColor(R.color.white));
                    tv_secondOdds.setTextColor(getResources().getColor(R.color.white));
                    l_homeTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_firstTeam.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_firstOdds.setTextColor(getResources().getColor(R.color.color_959697));
                    l_flatTeam.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_flat.setTextColor(getResources().getColor(R.color.color_959697));
                    tv_flatT.setTextColor(getResources().getColor(R.color.color_959697));
                    guessListBeans.get(position).setVisiting(true);
                    guessListBeans.get(position).setHome(false);
                    guessListBeans.get(position).setFlat(false);
                }
                break;
        }

        if(judgeHas(position) && judgeOnClick(guessListBeans.get(position))){
            positions.remove(String.valueOf(position));
        }else if(!judgeHas(position) && !judgeOnClick(guessListBeans.get(position))){
            positions.add(String.valueOf(position));
        }
    }

    public boolean judgeOnClick(GuessListBean guessListBean){

        if(!guessListBean.isHome() && !guessListBean.isFlat() && !guessListBean.isVisiting()){
            return true;
        }
        return false;
    }

    public boolean judgeHas(int position){
        if(positions.contains(String.valueOf(position))){
            return true;
        }
        return false;
    }
}
