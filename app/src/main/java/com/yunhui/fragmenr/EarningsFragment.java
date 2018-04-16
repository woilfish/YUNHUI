package com.yunhui.fragmenr;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.component.refreshlistview.RefreshListView;

/**
 * Created by pengmin on 2018/4/2.
 * 收益
 */

public class EarningsFragment extends BaseFragment implements RefreshListView.OnRefreshListViewListener{

    private HomeActivity homeActivity;
    private View parentView;
    private EditText et_earningshiteday;
    private RefreshListView rlv_earningsrefeesh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_earnings,null);
        initView();
        return parentView;
    }

    @SuppressLint("WrongViewCast")
    private void initView() {
        et_earningshiteday =  parentView.findViewById(R.id.earningshiteday);
        rlv_earningsrefeesh = parentView.findViewById(R.id.earningsrefeesh);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
