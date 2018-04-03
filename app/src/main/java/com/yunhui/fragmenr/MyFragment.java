package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunhui.activity.HomeActivity;
import com.yunhui.R;

/**
 * Created by pengmin on 2018/4/2.
 * 我的
 */

public class MyFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_my,null);
        return parentView;
    }
}
