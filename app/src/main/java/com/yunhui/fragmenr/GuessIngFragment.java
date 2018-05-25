package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.activity.HomeActivity;
import com.yunhui.adapter.GuessAdapter;
import com.yunhui.bean.GuessListBean;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.util.DateUtil;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_guess,null);
        initView();
        return parentView;
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
