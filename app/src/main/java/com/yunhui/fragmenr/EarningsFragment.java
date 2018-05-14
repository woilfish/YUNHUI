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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.common.exception.BaseException;
import com.loopj.common.httpEx.HttpRequest;
import com.loopj.common.httpEx.HttpRequestParams;
import com.loopj.common.httpEx.IHttpRequestEvents;
import com.yunhui.activity.BuyillMInfoActivity;
import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.adapter.ProductMachineAdapter;
import com.yunhui.bean.MyEarnings;
import com.yunhui.bean.ProductMachine;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.request.RequestUtil;
import com.yunhui.util.DateUtil;
import com.yunhui.util.StringUtil;
import com.yunhui.util.ToastUtil;

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
    private TextView tv_earningdate;
    private TextView tv_allTotalRevenue;
    private LinearLayout l_bronze;
    private LinearLayout l_silver;
    private LinearLayout l_gold;
    private TextView tv_allSilver;
    private TextView tv_allBronze;
    private TextView tv_bronzeNum;
    private TextView tv_silverNum;
    private TextView tv_allGold;
    private TextView tv_goldNum;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            rlv_earningsrefeesh.stopRefresh();
            switch (msg.what){
                case 1:
                    productMachineAdapter.refreshData(productMachines);
                    setListViewHeightBasedOnChildren(rlv_earningsrefeesh);
                    break;
                case 2:
                    MyEarnings myEarnings = (MyEarnings) msg.obj;
                    tv_allTotalRevenue.setText(myEarnings.getTotal());
                    tv_earningshiteday.setText(myEarnings.getTotal() + " BTC");
                    if(myEarnings.getMyMillInfos() == null || myEarnings.getMyMillInfos().size() == 0){
                        l_bronze.setVisibility(View.GONE);
                        l_silver.setVisibility(View.GONE);
                        l_gold.setVisibility(View.GONE);
                    }else{
                        for(int i = 0;i < myEarnings.getMyMillInfos().size();i++){
                            if(StringUtil.isEmpty(myEarnings.getMyMillInfos().get(i).getPrdId())){
                                continue;
                            }
                            switch (Integer.parseInt(myEarnings.getMyMillInfos().get(i).getPrdId())){
                                case 1:
                                    l_bronze.setVisibility(View.VISIBLE);
                                    tv_allBronze.setText(myEarnings.getMyMillInfos().get(i).getCount());
                                    tv_bronzeNum.setText(myEarnings.getMyMillInfos().get(i).getBenefit());
                                    break;
                                case 2:
                                    l_silver.setVisibility(View.VISIBLE);
                                    tv_allSilver.setText(myEarnings.getMyMillInfos().get(i).getCount());
                                    tv_silverNum.setText(myEarnings.getMyMillInfos().get(i).getBenefit());
                                    break;
                                case 3:
                                    l_gold.setVisibility(View.VISIBLE);
                                    tv_allGold.setText(myEarnings.getMyMillInfos().get(i).getCount());
                                    tv_goldNum.setText(myEarnings.getMyMillInfos().get(i).getBenefit());
                                    break;
                            }
                        }
                    }
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button btn = (Button) view;
            int postion = (Integer) btn.getTag();
            Intent intent = new Intent(homeActivity, BuyillMInfoActivity.class);
            intent.putExtra("millInfo",productMachines.get(postion));
            startActivityForResult(intent,9999);
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
        allTotalRevenueRequets();
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        tv_earningshiteday =  parentView.findViewById(R.id.earningshiteday);
        rlv_earningsrefeesh = parentView.findViewById(R.id.earningsrefeesh);
        tv_earningdate = parentView.findViewById(R.id.earningdate);
        tv_allTotalRevenue = parentView.findViewById(R.id.allTotalRevenue);
        tv_earningdate.setText(DateUtil.getCurrentDate() + " " + DateUtil.getWeekOfDate());
        l_bronze = parentView.findViewById(R.id.bronze);
        l_silver = parentView.findViewById(R.id.silver);
        l_gold = parentView.findViewById(R.id.gold);
        tv_allBronze = parentView.findViewById(R.id.allbronze);
        tv_allSilver = parentView.findViewById(R.id.allsilver);
        tv_bronzeNum = parentView.findViewById(R.id.bronzenum);
        tv_silverNum = parentView.findViewById(R.id.silvernum);
        tv_allGold = parentView.findViewById(R.id.allgold);
        tv_goldNum = parentView.findViewById(R.id.goldnum);
        productMachineAdapter = new ProductMachineAdapter(homeActivity,productMachines,onClickListener);
        rlv_earningsrefeesh.setAdapter(productMachineAdapter);
        rlv_earningsrefeesh.setOnRefreshListViewListener(this);
        rlv_earningsrefeesh.setPullRefreshEnable(false);
        rlv_earningsrefeesh.setPullLoadEnable(false);
    }

    @Override
    public void onRefresh() {
        productMachineRequest();
        allTotalRevenueRequets();
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
                    productMachines = ProductMachine.initAttrWithJson(jsonObject.optJSONArray("list"),1);
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

    private void allTotalRevenueRequets(){
        RequestUtil requestUtil = RequestUtil.obtainRequest(homeActivity,"user/queryUserBenefit", HttpRequest.RequestMethod.POST);

        requestUtil.setIHttpRequestEvents(new IHttpRequestEvents(){
            @Override
            public void onSuccess(HttpRequest request) {
                super.onSuccess(request);
                JSONObject jsonObject = (JSONObject) request.getResponseHandler().getResultData();
                if(jsonObject != null && jsonObject.has("total") && jsonObject.has("btcoin")){
                    MyEarnings myEarnings = new MyEarnings(jsonObject);
                    Message message = new Message();
                    message.what = 2;
                    message.obj = myEarnings;
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


    public void setListViewHeightBasedOnChildren(ListView listView) {

            // 获取ListView对应的Adapter

           ListAdapter listAdapter = listView.getAdapter();

           if (listAdapter == null) {
               return;
           }

           int totalHeight = 0;

           for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

                View listItem = listAdapter.getView(i, null, listView);

                listItem.measure(0, 0); // 计算子项View 的宽高

                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

           }

           ViewGroup.LayoutParams params = listView.getLayoutParams();

           params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

           // listView.getDividerHeight()获取子项间分隔符占用的高度

           // params.height最后得到整个ListView完整显示需要的高度

           listView.setLayoutParams(params);

    }
}
