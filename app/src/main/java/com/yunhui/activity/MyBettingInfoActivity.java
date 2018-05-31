package com.yunhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.adapter.GuessAdapter;
import com.yunhui.bean.MyBettingInfo;
import com.yunhui.clickinterface.ListItemClickHelp;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.manager.ActivityQueueManager;

public class MyBettingInfoActivity extends BaseActionBarActivity implements ListItemClickHelp,RefreshListView.OnRefreshListViewListener{

    private MyBettingInfo myBettingInfo;
    private TextView tv_money;
    private TextView tv_type;
    private TextView tv_billNum;
    private TextView tv_info;
    private RefreshListView rlv_mtBettingInfoList;
    private GuessAdapter guessAdapter;

    @Override
    protected void initActivity(Bundle savedInstanceState) {
        setContentView(R.layout.activity_mybetting_info);
        ActivityQueueManager.getInstance().pushActivity(this);
        myBettingInfo = (MyBettingInfo) getIntent().getSerializableExtra("info");
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityQueueManager.getInstance().popActivity(this);
    }

    private void initView() {
        navigationBar.setTitle("投注详情");
        navigationBar.setBackground(R.color.color_4F5051);
        tv_money = findViewById(R.id.money);
        tv_type = findViewById(R.id.type);
        tv_billNum = findViewById(R.id.billNum);
        tv_info = findViewById(R.id.info);
        rlv_mtBettingInfoList = findViewById(R.id.mtBettingInfoList);
        tv_money.setText(myBettingInfo.getNum() + "注" + "   云钻" + myBettingInfo.getAmount());
        tv_type.setText(myBettingInfo.getGuessListBeans().size() + " 串 "  + "1");
        tv_billNum.setText(myBettingInfo.getJnlid());
//        tv_info.setText();
        guessAdapter = new GuessAdapter(MyBettingInfoActivity.this,myBettingInfo.getGuessListBeans(),this);
        rlv_mtBettingInfoList.setAdapter(guessAdapter);
        rlv_mtBettingInfoList.setOnRefreshListViewListener(this);
        rlv_mtBettingInfoList.setPullRefreshEnable(false);
        rlv_mtBettingInfoList.setPullLoadEnable(false);
    }

    @Override
    public void onClick(View item, View parent, int position, int which) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
