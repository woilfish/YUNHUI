package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.ConsultingAdapter;
import com.yunhui.component.NavigationBar;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;

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
    private int pageNo = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_consulting,null);
        navigationBarManager();
        networkRequest();
        return parentView;
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
        consultingAdapter = new ConsultingAdapter(homeActivity,null);
        rlv_refreshList.setAdapter(consultingAdapter);
        rlv_refreshList.setOnRefreshListViewListener(this);
    }

    @Override
    public void onRefresh() {
        pageNo = 1;
        networkRequest();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        networkRequest();
    }

    /**
     * 网络请求
     */
    private void networkRequest(){

        RequestUtil request = RequestUtil.obtainRequest(homeActivity,"", HttpRequest.RequestMethod.POST);

        HttpRequestParams requestParams = request.getRequestParams();
        requestParams.put("pageNo",pageNo);

        request.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
            }

            @Override
            public void onFailure(HttpRequest request, BaseException exception) {
                super.onFailure(request, exception);
            }
        });

        request.execute();
    }
}
