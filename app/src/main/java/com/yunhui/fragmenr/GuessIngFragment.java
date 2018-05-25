package com.yunhui.fragmenr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yunhui.R;
import com.yunhui.activity.HomeActivity;
import com.yunhui.component.refreshlistview.RefreshListView;
import com.yunhui.util.DateUtil;

/**
 * 竞猜
 */
public class GuessIngFragment extends BaseFragment {

    private HomeActivity homeActivity;
    private View parentView;
    private TextView tv_guessDate;
    private RefreshListView rlv_guessList;

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
//        taskAdapter = new TaskAdapter(homeActivity,taskInfoList,mListener);
//        rlv_taskList.setAdapter(taskAdapter);
//        rlv_taskList.setOnRefreshListViewListener(this);
//        rlv_taskList.setPullRefreshEnable(false);
//        rlv_taskList.setPullLoadEnable(false);
    }
}
