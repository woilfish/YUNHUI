package com.yunhui.activity;

import android.os.Bundle;
import android.os.Message;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.adapter.MyBettingAdapter;
import com.yunhui.bean.MyBettingInfo;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RequestUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyBettingActivity extends BaseActionBarActivity implements RefreshListView.OnRefreshListViewListener{

    private RefreshListView refreshListView;
    private int page = 1;
    private int pageSize = 10;
    private List<MyBettingInfo> myBettingInfos = new ArrayList<>();
    private MyBettingAdapter myBettingAdapter;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mybetting);
        ActivityQueueManager.getInstance().pushActivity(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        queryGuessList();
    }

    private void initView() {
        navigationBar.setTitle("我的投注");
        navigationBar.setBackground(R.color.color_4F5051);
        refreshListView = findViewById(R.id.myBettingList);
        myBettingAdapter = new MyBettingAdapter(MyBettingActivity.this,myBettingInfos);
        refreshListView.setAdapter(myBettingAdapter);
        refreshListView.setOnRefreshListViewListener(this);
        refreshListView.setPullRefreshEnable(true);
        refreshListView.setPullLoadEnable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    @Override
    public void onRefresh() {
        myBettingInfos.clear();
        page = 0;
        queryGuessList();
    }

    @Override
    public void onLoadMore() {
        page ++;
        queryGuessList();
    }

    private void queryGuessList(){

        RequestUtil requestUtil = RequestUtil.obtainRequest(MyBettingActivity.this,"user/queryGuessList", HttpRequest.RequestMethod.POST);
        HttpRequestParams httpRequestParams = requestUtil.getRequestParams();
        httpRequestParams.put("pageNo",page);
        httpRequestParams.put("pageSize",pageSize);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                refreshListView.stopLoadMore();
                refreshListView.stopRefresh();
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("list") && jsonObject.optJSONArray("list").length() > 0){
                    MyBettingInfo myBettingInfo = new MyBettingInfo(jsonObject.optJSONArray("list"));
                    myBettingInfos.addAll(myBettingInfo.getMyBettingInfos());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myBettingAdapter.refreshData(myBettingInfos);
                        }
                    });
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
                refreshListView.stopLoadMore();
                refreshListView.stopRefresh();
            }
        });
        requestUtil.execute();
    }
}
