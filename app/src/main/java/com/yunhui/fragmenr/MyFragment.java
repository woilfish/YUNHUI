package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhui.activity.HomeActivity;
import com.yunhui.R;
import com.yunhui.component.image.CircleImageView;
import com.yunhui.component.linearlayout.LabelItemView;

/**
 * Created by pengmin on 2018/4/2.
 * 我的
 */

public class MyFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private View parentView;
    private CircleImageView civ_userPhoto;
    private TextView tv_userPhoneNum;
    private TextView tv_userName;
    private LabelItemView liv_exchange;
    private LabelItemView liv_myTask;
    private LabelItemView liv_buy;
    private LabelItemView liv_myInvitation;
    private LabelItemView liv_aboutMy;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_my,null);
        initView();
        return parentView;
    }

    private void initView() {
        civ_userPhoto = parentView.findViewById(R.id.userphoto);
        tv_userPhoneNum = parentView.findViewById(R.id.userphonenum);
        tv_userName = parentView.findViewById(R.id.username);
        liv_exchange = parentView.findViewById(R.id.exchange);
        liv_myTask = parentView.findViewById(R.id.myTask);
        liv_buy = parentView.findViewById(R.id.buy);
        liv_myInvitation = parentView.findViewById(R.id.myInvitation);
        liv_aboutMy = parentView.findViewById(R.id.aboutMy);
        liv_exchange.setOnClickListener(this);
        liv_myTask.setOnClickListener(this);
        liv_buy.setOnClickListener(this);
        liv_myInvitation.setOnClickListener(this);
        liv_aboutMy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.exchange:
                break;
            case R.id.myTask:
                break;
            case R.id.buy:
                break;
            case R.id.myInvitation:
                break;
            case R.id.aboutMy:
                break;
        }
    }
}
