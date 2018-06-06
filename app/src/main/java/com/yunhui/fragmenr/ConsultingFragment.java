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
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.ConsultingAdapter;
import com.yunhui.bean.ConsultingInfo;
import com.yunhui.component.NavigationBar;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by pengmin on 2018/4/2.
 * 咨询
 */

public class ConsultingFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener{

    private HomeActivity homeActivity;
    private View parentView;
    private NavigationBar navigationBar;
    private TextView tv_consultdate;
    private RefreshListView rlv_refreshList;
    private ConsultingAdapter consultingAdapter;
    private int pageNo = 0;
    private List<ConsultingInfo> consultingInfos;
    private boolean isRefOrLoad = true;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if(isRefOrLoad){
                        rlv_refreshList.stopRefresh();
                    }else{
                        rlv_refreshList.stopLoadMore();
                    }
                    consultingAdapter.refreshData(consultingInfos);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_consulting,null);
        navigationBarManager();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        consultingInforRequest();
    }

    /**
     * 设置导航栏
     */
    private void navigationBarManager() {
        navigationBar = homeActivity.getNavigationBar();
        navigationBar.setVisibility(View.GONE);
        tv_consultdate = parentView.findViewById(R.id.consultdate);
        tv_consultdate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        rlv_refreshList = parentView.findViewById(R.id.refreshlist);
        consultingAdapter = new ConsultingAdapter(homeActivity,consultingInfos);
        rlv_refreshList.setAdapter(consultingAdapter);
        rlv_refreshList.setOnRefreshListViewListener(this);
        rlv_refreshList.setPullRefreshEnable(true);
        rlv_refreshList.setPullLoadEnable(true);
    }

    @Override
    public void onRefresh() {
        pageNo = 0;
        isRefOrLoad = true;
        consultingInforRequest();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        isRefOrLoad = false;
        consultingInforRequest();
    }

    /**
     * 网络请求
     */
    private void consultingInforRequest(){

        RequestUtil request = RequestUtil.obtainRequest(homeActivity,"user/queryMsgList", HttpRequest.RequestMethod.POST);

        HttpRequestParams requestParams = request.getRequestParams();
        requestParams.put("pageIndex",pageNo);
        requestParams.put("pageSize",20);

        request.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();

                if(jsonObject.has("msgList")){
                    consultingInfos = ConsultingInfo.initAttrWithJson(jsonObject.optJSONArray("msgList"),pageNo);
                    Message message = new Message();
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });

        request.execute();
    }
}
