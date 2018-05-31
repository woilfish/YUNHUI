package com.yunhui.activity;

import android.os.Bundle;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.R;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;
import com.yunhui.request.RequestUtil;

public class MyBettingActivity extends BaseActionBarActivity implements RefreshListView.OnRefreshListViewListener{

    private RefreshListView refreshListView;
    private int page = 0;
    private int pageSize = 10;

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

    }

    @Override
    public void onLoadMore() {

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
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });
        requestUtil.execute();
    }
}
