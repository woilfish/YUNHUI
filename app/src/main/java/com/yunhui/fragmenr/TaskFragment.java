package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunhui.HomeActivity;
import com.yunhui.R;

/**
 * Created by pengmin on 2018/4/2.
 * 任务
 */

public class TaskFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private View parentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        homeActivity = (HomeActivity) getActivity();
        parentView = inflater.inflate(R.layout.fragment_task,null);
        return parentView;
    }
}
