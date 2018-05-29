package com.yunhui.fragmenr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.activity.HomeActivity;
import com.yunhui.adapter.GuessAdapter;
import com.yunhui.bean.GuessListBean;
import com.yunhui.bean.RechargeBean;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * 竞猜
 */
public class GuessIngFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener{

    private HomeActivity homeActivity;
    private View parentView;
    private TextView tv_guessDate;
    private RefreshListView rlv_guessList;
    private GuessAdapter guessAdapter;
    private List<GuessListBean> guessListBeans;

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
                    GuessListBean guessListBean = new GuessListBean(jsonArray);
                    guessListBeans = guessListBean.getGuessListBeans();
                    Message message = new Message();
                    message.what = 000000;
                    handler.sendMessage(message);
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
        tv_guessDate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        guessAdapter = new GuessAdapter(homeActivity,guessListBeans);
        rlv_guessList.setAdapter(guessAdapter);
        rlv_guessList.setOnRefreshListViewListener(this);
        rlv_guessList.setPullRefreshEnable(false);
        rlv_guessList.setPullLoadEnable(false);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
