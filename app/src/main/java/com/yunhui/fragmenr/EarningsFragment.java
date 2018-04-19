package com.yunhui.fragmenr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.ProductMachineAdapter;
import com.yunhui.bean.ProductMachine;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by pengmin on 2018/4/2.
 * 收益
 */

public class EarningsFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener{

    private HomeActivity homeActivity;
    private View parentView;
    private TextView tv_earningshiteday;
    private RefreshListView rlv_earningsrefeesh;
    private List<ProductMachine> productMachines;
    private ProductMachineAdapter productMachineAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    productMachineAdapter.refreshData(productMachines);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_earnings,null);
        initView();
        return parentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        productMachineRequest();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        tv_earningshiteday =  parentView.findViewById(R.id.earningshiteday);
        rlv_earningsrefeesh = parentView.findViewById(R.id.earningsrefeesh);
        productMachineAdapter = new ProductMachineAdapter(homeActivity,productMachines);
        rlv_earningsrefeesh.setAdapter(productMachineAdapter);
        rlv_earningsrefeesh.setOnRefreshListViewListener(this);
        rlv_earningsrefeesh.setPullRefreshEnable(false);
        rlv_earningsrefeesh.setPullLoadEnable(false);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    private void productMachineRequest(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(homeActivity,"user/queryCoinPros", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject.has("list")){
                    productMachines = ProductMachine.initAttrWithJson(jsonObject.optJSONArray("list"));
                    Message message = new Message();
                    message.what = 1;
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
}
